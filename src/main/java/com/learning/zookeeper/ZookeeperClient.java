package com.learning.zookeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author lifang
 * @since 2021/11/3
 */
public class ZookeeperClient implements Watcher {

    private static final Logger logger = LogManager.getLogger(ZookeeperClient.class);


    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private ZookeeperClient(){}

    public static ZookeeperClient getInstance(){
        return new ZookeeperClient();
    }

    public static ZooKeeper getDefaultZooKeeper(){
        return getInstance().connection("192.168.200.211:2181,192.168.200.212:2181,192.168.200.213:2181/bigdata", 5000);
    }

    @Override
    public void process(WatchedEvent event) {
        logger.info("触发创建ZookeeperClient#Watcher事件，stage: " + event.getState());
        if(event.getState() == Event.KeeperState.SyncConnected){
            countDownLatch.countDown();
        }
    }

    public ZooKeeper connection(String host, int timeout){
        ZooKeeper zookeeper = null;
        try {
            zookeeper = new ZooKeeper(host, timeout, this);

            // 等待zookeeper连接成功
            countDownLatch.await();
        }catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return zookeeper;
    }

}
