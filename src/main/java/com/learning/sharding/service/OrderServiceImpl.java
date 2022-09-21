package com.learning.sharding.service;

import com.learning.sharding.entity.OrderEntity;
import com.learning.sharding.entity.OrderItemEntity;
import com.learning.sharding.entity.OrderLogEntity;
import com.learning.sharding.repository.OrderItemRepository;
import com.learning.sharding.repository.OrderLogRepository;
import com.learning.sharding.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService{

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private OrderItemRepository orderItemRepository;
    @Resource
    private OrderLogRepository orderLogRepository;

    @Override
    @Transactional
    public void insert(OrderEntity orderEntity, OrderItemEntity orderItemEntity){
        orderRepository.save(orderEntity);

        orderItemEntity.setOrderId(orderEntity.getId());
        orderItemRepository.save(orderItemEntity);

        OrderLogEntity orderLogEntity = new OrderLogEntity();
        orderLogEntity.setCreatedDate(LocalDateTime.now());
        orderLogRepository.save(orderLogEntity);
    }

    @Override
    @Transactional
    public void deleteAll() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();;
    }
}
