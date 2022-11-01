package com.learning.middleware.zookeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lifang
 * @since 2021/11/3
 */
public class ZookeeperTest {

    private static final Logger logger = LogManager.getLogger(ZookeeperTest.class);


    private ZooKeeper zookeeper;

    @BeforeTest
    public void getZooKeeperClient() {
        this.zookeeper = ZookeeperClient.getInstance().connection("192.168.200.211:2181,192.168.200.212:2181,192.168.200.213:2181/bigdata", 5000);
    }

    @Test
    public void testConnection() {
        logger.info("zookeeper: {}", this.zookeeper);
    }

    @Test
    public void testExists() throws InterruptedException, KeeperException {

        final String path = "/zk-lock-card";

        // 注册watcher
        Stat exists = zookeeper.exists(path, event -> {
            logger.info("------------- WatchedEvent, path{}, state:{}, type:{}", event.getPath(), event.getState(), event.getType());
        });

        if (exists != null) {
            logger.info("------------- 节点已存在， path:{}", path);
            zookeeper.delete(path, 0); // 触发watcher事件
        }

        zookeeper.exists(path, event -> {
            logger.info("1------------- WatchedEvent, path{}, state:{}, type:{}", event.getPath(), event.getState(), event.getType());
        });

        zookeeper.exists(path, event -> {
            logger.info("2------------- WatchedEvent, path{}, state:{}, type:{}", event.getPath(), event.getState(), event.getType());
        });

        // 创建持久化节点
        zookeeper.create(path, "true".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    @Test
    public void test() throws InterruptedException, KeeperException {
        String lockKey = "/zk_lock";

        Stat exists = zookeeper.exists(lockKey, false);
        if(exists == null) {
            zookeeper.create(lockKey, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        for (int i = 0; i < 10; i++) {

            zookeeper.create(lockKey + "/lock_" + i, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        }

        List<String> children = zookeeper.getChildren(lockKey, event -> {
            logger.info("------------- WatchedEvent, path{}, state:{}, type:{}", event.getPath(), event.getState(), event.getType());
        });

        for (String child : children) {
            logger.info("child: {}", child);
        }

        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testNode() throws InterruptedException, KeeperException {
        zookeeper.create("/zk-p-lock-card", "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zookeeper.create("/zk-e-lock-device", "true".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        zookeeper.create("/zk-es-lock-msg", "true".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        zookeeper.create("/zk-ps-lock-msg", "true".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

        TimeUnit.SECONDS.sleep(5);
    }

    @AfterTest
    public void close() {
        try {
            zookeeper.close();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
