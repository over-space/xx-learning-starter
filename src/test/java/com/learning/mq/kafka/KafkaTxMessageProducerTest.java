package com.learning.mq.kafka;

import com.learning.spring.BService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author 李芳
 * @since 2022/9/14
 */
@SpringBootTest
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class KafkaTxMessageProducerTest {

    @Test
    void contextLoads() {
    }


    @Resource
    private BService bService;

    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            CompletableFuture.runAsync(() -> bService.save());
        }

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void test2() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            CompletableFuture.runAsync(() -> bService.save2());
        }

        TimeUnit.SECONDS.sleep(10);
    }
}
