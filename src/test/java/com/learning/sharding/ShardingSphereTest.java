package com.learning.sharding;

import com.learning.sharding.entity.OrderEntity;
import com.learning.sharding.entity.OrderItemEntity;
import com.learning.sharding.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * @author 李芳
 * @since 2022/9/2
 */
@SpringBootTest(classes = ShardingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Profile("sharding")
public class ShardingSphereTest {

    @Resource
    private OrderService orderService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testDelete() {
        orderService.deleteAll();
    }

    @Test
    public void testSave() {
        for (int i = 1; i <= 100; i++) {
            OrderEntity orderEntity = new OrderEntity();
            // orderEntity.setId(Long.valueOf(i));
            orderEntity.setOrderType(i % 2);
            orderEntity.setAmount(new Random().nextFloat() * 1000F);

            OrderItemEntity orderItemEntity = new OrderItemEntity();
            // orderItemEntity.setId(Long.valueOf(i));
            orderItemEntity.setOrderType(i % 2);
            orderItemEntity.setCreatedDate(LocalDateTime.now());
            orderItemEntity.setName("测试-" + new Random().nextInt(1000));

            orderService.insert(orderEntity, orderItemEntity);
        }
    }
}
