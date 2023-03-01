package com.learning.middleware.zookeeper;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

/**
 * @author 李芳
 * @since 2022/8/23
 */
public class ZookeeperLock implements Lock, Watcher {

    private static final Logger logger = LogManager.getLogger(ZookeeperLock.class);

    private final String PATH_DELIMITER = "/";
    private String ROOT_PATH = "/locks";
    private ZooKeeper zookeeper;
    private String CURRENT_LOCK;
    private String WAIT_LOCK;

    private CountDownLatch countDownLatch;

    public ZookeeperLock() {
        try {
            this.zookeeper = ZookeeperClient.getDefaultZooKeeper();
            Stat stat = this.zookeeper.exists(ROOT_PATH, false);
            if (Objects.isNull(stat)) {
                this.zookeeper.create(ROOT_PATH, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ZookeeperLock(String lockKey) {
        this();

        ROOT_PATH = StringUtils.startsWith(lockKey, PATH_DELIMITER) ? (ROOT_PATH + lockKey) : (ROOT_PATH + PATH_DELIMITER + lockKey);

        try {
            Stat stat = this.zookeeper.exists(ROOT_PATH, false);
            if (Objects.isNull(stat)) {
                this.zookeeper.create(ROOT_PATH, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean tryLock() {
        final String lock = ROOT_PATH + "/lock-";

        try {
            CURRENT_LOCK = zookeeper.create(lock, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            logger.info("创建临时顺序节点，path:{}", CURRENT_LOCK);

            final String currentNode = StringUtils.substringAfterLast(CURRENT_LOCK, PATH_DELIMITER);

            // 获取孩子结点（锁结点）
            // TreeSet<String> nodeList = zookeeper.getChildren(ROOT_PATH, false).stream()
            //         .sorted()
            //         .collect(Collectors.toCollection(TreeSet::new));
            // 创建孩子集合并添加到有序Set中
            List<String> children = zookeeper.getChildren(ROOT_PATH, false);
            SortedSet<String> nodeList = new TreeSet<>();
            for (String child : children) {
                nodeList.add(child);
            }

            String firstNode = nodeList.first();

            if (StringUtils.equals(currentNode, firstNode)) {
                // 获取到锁
                return true;
            }

            SortedSet<String> headNodeList = nodeList.headSet(currentNode);
            if (!headNodeList.isEmpty()) {
                WAIT_LOCK = ROOT_PATH + PATH_DELIMITER + headNodeList.last();
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void lock() {
        if (this.tryLock()) {
            return;
        }
        // 没有获取到锁，进入等待
        waitForLock(WAIT_LOCK);
    }

    @Override
    public void release() {
        try {
            System.out.println("--------------------释放锁，" + CURRENT_LOCK);
            zookeeper.delete(CURRENT_LOCK, -1);
            CURRENT_LOCK = null;
        } catch (InterruptedException | KeeperException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                zookeeper.close();
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("===========================" + countDownLatch);
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    private void waitForLock(String preNode) {
        try {
            // 监听上一个比自己小的结点
            Stat stat = zookeeper.exists(preNode, true);
            if (stat != null) {
                logger.info("等待获取锁：path: {}, currentLock:{}", preNode, CURRENT_LOCK);
                countDownLatch = new CountDownLatch(1);
                countDownLatch.await();
            }
        } catch (KeeperException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("锁等待异常，path:{}, msg:{}", preNode, e.getMessage());
        }
    }
}
