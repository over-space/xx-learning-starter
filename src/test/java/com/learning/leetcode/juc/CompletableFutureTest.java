package com.learning.leetcode.juc;

import com.learning.BaseTest;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lifang
 * @since 2021/10/14
 */
public class CompletableFutureTest extends BaseTest {

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Test
    public void test() {
        CompletableFuture.runAsync(() -> {
            printLog("runAsync");
        }, executorService).thenAccept((e) -> {
            printLog("thenAccept");
        }).whenComplete((v, e) -> {
            printLog("whenComplete");
        });

        sleep(5 * 1000);
    }

    @Test
    public void test02() {
        CompletableFuture future01 = CompletableFuture.supplyAsync(() -> {
            sleep(new Random().nextInt(100));
            printLog("future01");
            return 1;
        }, executorService);

        CompletableFuture future02 = CompletableFuture.supplyAsync(() -> {
            sleep(new Random().nextInt(100));
            printLog("future02");
            return 2;
        }, executorService);

        CompletableFuture future03 = CompletableFuture.supplyAsync(() -> {
            sleep(new Random().nextInt(100));
            printLog("future03");
            return 3;
        }, executorService);

        CompletableFuture future04 = CompletableFuture.supplyAsync(() -> {
            sleep(new Random().nextInt(100));
            printLog("future04");
            return 4;
        }, executorService);

        CompletableFuture future05 = CompletableFuture.supplyAsync(() -> {
            sleep(new Random().nextInt(100));
            printLog("future05");
            return 5;
        }, executorService);

        CompletableFuture future06 = CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            printLog("future06");
            return 6;
        }, executorService);

        System.out.println(future01.getNow(-1));
        System.out.println(future02.getNow(-1));
        System.out.println(future03.getNow(-1));
        System.out.println(future04.getNow(-1));
        System.out.println(future05.getNow(-1));
        System.out.println(future06.getNow(-1));

        CompletableFuture.allOf(future01, future02, future03, future04, future05, future06).join();

        System.out.println(future01.getNow(0));
        System.out.println(future02.getNow(0));
        System.out.println(future03.getNow(0));
        System.out.println(future04.getNow(0));
        System.out.println(future05.getNow(0));
        System.out.println(future06.getNow(0));

    }

    @Test
    public void test03() {
        CompletableFuture future01 = CompletableFuture.runAsync(() -> {
            printLog("future01");
        }, executorService);

        CompletableFuture future02 = CompletableFuture.runAsync(() -> {
            printLog("future02");
        }, executorService);

        CompletableFuture future03 = CompletableFuture.runAsync(() -> {
            printLog("future03");
        }, executorService);

        CompletableFuture future04 = CompletableFuture.runAsync(() -> {
            printLog("future04");
        }, executorService);

        CompletableFuture future05 = CompletableFuture.runAsync(() -> {
            printLog("future05");
        }, executorService);

        CompletableFuture future06 = CompletableFuture.runAsync(() -> {
            printLog("future06");
            int i = 10 / 0;
        }, executorService);

        CompletableFuture.allOf(future01, future02, future03, future04, future05, future06)
                .exceptionally((e) -> {
                    return null;
                })
                .handle((v, e) -> {
                    System.out.println("finished " + e);
                    return "finished";
                });

        System.out.println("----------------------------");
        sleep(5);
    }

    @Test
    public void test04() {
        CompletableFuture.runAsync(() -> {
            printLog("runAsync");
        }, executorService).thenApply((e) -> {
            printLog("thenAccept");
            return 1;
        }).thenApply((e) -> {
            printLog("thenApplyAsync1");
            return 5;
        }).thenApply((e) -> {
            printLog("thenApplyAsync2");
            return 5;
        });

        System.out.println("----------------------------");
        sleep(5);
    }

    @Test
    public void test05() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {

            System.out.println("-------------------------------------------------------------------------------------");

            try {
                CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
                    sleep(4 * 1000);
                    return 5;
                });
                return integerCompletableFuture.get();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            System.out.println("-------------------------------------------------------------------------------------");
            return -1;
        }, executorService);

        logger.info("result:{}", future.get());

        System.out.println("----------------------------");
        wait(5 * 1000);
    }

    private void printLog(String msg) {
        sleep(new Random().nextInt(400));
        logger.info("threadName:{}, msg:{}", Thread.currentThread().getName(), msg);
    }
}
