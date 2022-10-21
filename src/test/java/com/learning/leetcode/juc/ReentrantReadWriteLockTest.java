package com.learning.leetcode.juc;

import com.learning.BaseTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest extends BaseTest {

    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Lock readLock = lock.readLock();

    private Lock writeLock = lock.writeLock();

    private List<Integer> dataList = new ArrayList<>(100);

    private Random random = new Random();

    private ExecutorService executorService = Executors.newFixedThreadPool(10);


    @Test
    public void testLock() {
        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                while (true) {
                    write();
                    sleep(2);
                }
            });
        }

        for (int i = 0; i < 7; i++) {
            executorService.execute(() -> {
                while (true) {
                    read();
                    sleep(1);
                }
            });
        }

        while (true) {
            sleep(1);
        }
    }


    private void write() {
        writeLock.lock();
        dataList.add(random.nextInt(10000));
        if (dataList.size() > 1000) {
            dataList.remove(0);
        }
        writeLock.unlock();
    }

    private Integer read() {
        readLock.lock();
        int value = dataList.get(dataList.size() - 1);
        logger.info("value:{}, size:{}", value, dataList.size());
        readLock.unlock();
        return value;
    }
}
