package com.learning.sharding;

import com.learning.sharding.entity.OrderEntity;
import com.learning.sharding.entity.OrderItemEntity;
import com.learning.sharding.entity.OrderLogEntity;
import com.learning.sharding.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

    @Test
    public void testSave2() {
        for (int i = 1; i <= 100; i++) {
            OrderLogEntity orderLogEntity = new OrderLogEntity();
            orderLogEntity.setOrderType(1);
            // orderLogEntity.setCreatedDate(LocalDateTime.of(LocalDate.of(ThreadLocalRandom.current().nextInt(1949, 2030), ThreadLocalRandom.current().nextInt(1, 12), ThreadLocalRandom.current().nextInt(1, 28)), LocalTime.MIN));
            orderLogEntity.setCreatedDate(LocalDateTime.of(LocalDate.of(2022, 9, 1), LocalTime.MIN));
            orderService.saveOrderLog(orderLogEntity);
        }
    }
}
