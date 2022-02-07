package com.learning.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author lifang
 * @since 2022/2/7
 */
@SpringBootTest
public class SpringTransactionalTest {

    @Resource
    private AService aService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test() {
        aService.insert();
    }
}
