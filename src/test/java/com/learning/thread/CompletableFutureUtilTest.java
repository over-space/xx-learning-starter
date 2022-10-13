package com.learning.thread;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureUtilTest extends BaseTest {

    @Test
    public void test(){

        CompletableFuture future1 = CompletableFuture.supplyAsync(() -> {
            sleep(3);
            return 1;
        });

        CompletableFuture future2 = CompletableFuture.supplyAsync(() -> {
            sleep(4);
            int a = 2 / 0;
            return 2;
        });

        CompletableFuture future3 = CompletableFuture.supplyAsync(() -> {
            sleep(5);
            return 3;
        });

        List<CompletableFuture> futureList = Arrays.asList(future1, future2, future3);
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).exceptionally(e -> {
            System.out.println("-------------------------------" + e.getMessage());
            return null;
        }).join();

        futureList.stream()
                .forEach(future -> {
                    try {
                        System.out.println(future.getNow(0));
                    } catch (Exception e) {
                        System.out.println("=============================");
                        System.out.println(e.getMessage());
                    }
                });
        sleep(5);
    }

    private static List<Integer> getEidList(){
        return Arrays.asList(1);
    }
}