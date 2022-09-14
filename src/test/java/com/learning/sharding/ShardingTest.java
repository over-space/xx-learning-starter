package com.learning.sharding;

import com.learning.sharding.entity.OrderEntity;
import com.learning.sharding.repository.OrderRepository;
import com.learning.sharding.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author 李芳
 * @since 2022/9/2
 */
@SpringBootTest
@Configuration(value = "application-sharding.properties")
public class ShardingTest {

    @Resource
    private OrderService orderService;

    @Test
    void contextLoads() {
    }


    @Test
    public void test(){
        for (int i = 1; i < 100; i++) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setId(Long.valueOf(i));
            orderEntity.setOrderType(i % 2);
            orderEntity.setCustomerId(i);
            orderEntity.setAmount(new Random().nextFloat() * 1000F);
            orderService.insert(orderEntity);
        }
    }
}
