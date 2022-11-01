package com.learning.middleware.zookeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author 李芳
 * @since 2022/8/24
 */
public class ZookeeperLockTest {

    private static final Logger logger = LogManager.getLogger(ZookeeperLockTest.class);

    @Test
    public void test() throws InterruptedException {



        for (int i = 0; i < 20; i++) {

            ZookeeperLock zookeeperLock = new ZookeeperLock();

            final int v = i;
            CompletableFuture.supplyAsync(() -> {
                try {
                    zookeeperLock.lock();
                    TimeUnit.SECONDS.sleep(v % 3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                logger.info(" v : {}", v);
                zookeeperLock.release();
                return v;
            });
        }

        TimeUnit.SECONDS.sleep(10);
    }

}
