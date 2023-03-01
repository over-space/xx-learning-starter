package com.learning.leetcode.juc;

import org.testng.annotations.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lifang
 * @since 2022/1/4
 */
public class ThreadPool {

    @Test
    public void test() {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2,
                10L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(5), new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            });
        }
    }

}
