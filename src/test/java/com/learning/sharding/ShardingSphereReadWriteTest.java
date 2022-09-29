package com.learning.sharding;

import com.learning.BaseTest;
import com.learning.sharding.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import javax.annotation.Resource;

/**
 * @author 李芳
 * @since 2022/9/29
 */
@SpringBootTest(classes = ShardingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Profile("sharding-readwrite")
public class ShardingSphereReadWriteTest extends BaseTest {

    @Resource
    private OrderService orderService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test(){
        orderService.findOrderLogList();
    }
}
