package com.learning.basic.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.learning.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author 李芳
 * @since 2022/10/21
 */
public class ThreadPollTest extends BaseTest {

    private static ThreadPoolExecutor threadPoolExecutor;


    @BeforeAll
    public static void beforeAll() {
        threadPoolExecutor = new ThreadPoolExecutor(3, 5,
                60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Test
    public void test() {
        IntStream.range(1, 30).parallel().forEach(num -> {
            threadPoolExecutor.execute(() -> {
                sleep(1);
                logger.info("num : {}", num);
            });
        });

        sleep(10);
    }

}
