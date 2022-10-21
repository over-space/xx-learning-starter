package com.learning;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author 李芳
 * @since 2022/9/21
 */
public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);


    @BeforeAll
    public static void beforeAll() {
        logger.info("================================================================================================");
        logger.info("-------------------------------------开始执行测试方法---------------------------------------------");
        logger.info("");
    }

    @AfterAll
    public static void afterAll() {
        logger.info("");
        logger.info("-------------------------------------测试方法执行完成---------------------------------------------");
        logger.info("================================================================================================");
    }

    protected <T> CompletableFuture runAsync(Supplier<T> supplier){
        return CompletableFuture.supplyAsync(() -> {
            return supplier.get();
        });
    }

    protected void print(String msg){
        logger.info("input : {}", msg);
    }

    protected static void sleep(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
