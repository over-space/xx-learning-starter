package com.learning.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lifang
 * @since 2022/2/7
 */
@SpringBootTest
public class SpringTransactionalTest {

    @Resource
    private AService aService;
    @Resource
    private CService cService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testTransactional() {
        aService.insertByTransactional();
        aService.insertByNonTransactional();
    }

    @Test
    public void testLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<CompletableFuture> list = new ArrayList<>();

        list.add(CompletableFuture.runAsync(() -> {
            cService.insert("520");
        }, executorService));

        list.add(CompletableFuture.runAsync(() -> {
            cService.insert("508");
        }, executorService));

        list.add(CompletableFuture.runAsync(() -> {
            cService.insert("520");
        }, executorService));

        list.add(CompletableFuture.runAsync(() -> {
            cService.insert("512");
        }, executorService));

        list.add(CompletableFuture.runAsync(() -> {
            cService.insert("515");
        }, executorService));

        list.add(CompletableFuture.runAsync(() -> {
            cService.insert("503");
        }, executorService));

        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).exceptionally(e -> {
            if (e != null) {
                e.printStackTrace();
            }
            return null;
        }).join();
    }

    @Test
    public void testUpdateLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<CompletableFuture> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {

            list.add(CompletableFuture.runAsync(() -> {
                cService.update(88L);
            }, executorService));

            list.add(CompletableFuture.runAsync(() -> {
                cService.update(88L);
            }, executorService));

            list.add(CompletableFuture.runAsync(() -> {
                cService.update(88L);
            }, executorService));

            list.add(CompletableFuture.runAsync(() -> {
                cService.update(88L);
            }, executorService));

            list.add(CompletableFuture.runAsync(() -> {
                cService.update(88L);
            }, executorService));
        }

        CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).exceptionally(e -> {
            if (e != null) {
                e.printStackTrace();
            }
            return null;
        }).join();
    }
}
