package com.learning.zookeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author lifang
 * @since 2021/11/3
 */
public class ZookeeperTest {

    private static final Logger logger = LogManager.getLogger(ZookeeperTest.class);


    private ZooKeeper zookeeper;

    @BeforeTest
    public void getZooKeeperClient(){
        this.zookeeper = ZookeeperClient.getInstance().connection("192.168.200.213:2181,192.168.200.214:2181,192.168.200.215:2181/bigdata", 5000);
    }

    @Test
    public void testNode() throws InterruptedException, KeeperException {
        // 创建持久化节点
        zookeeper.exists("/zk-lock-card", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                logger.info("------------------------------------{}", event.getState());
                logger.info("------------------------------------{}", event.getType());
            }
        });
        zookeeper.delete("/zk-lock-card", 0);
        zookeeper.exists("/zk-lock-card", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                logger.info("------------------------------------{}", event.getState());
                logger.info("------------------------------------{}", event.getType());
            }
        });
        zookeeper.create("/zk-lock-card", "hello".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);

        zookeeper.create("/zk-lock-device", "true".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.EPHEMERAL);

        zookeeper.create("/zk-lock-msg", "true".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        TimeUnit.SECONDS.sleep(5);
    }

    @AfterTest
    public void close(){
        try {
            zookeeper.close();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
