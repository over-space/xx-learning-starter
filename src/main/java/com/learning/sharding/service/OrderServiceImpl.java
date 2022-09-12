package com.learning.sharding.service;

import com.learning.sharding.entity.OrderEntity;
import com.learning.sharding.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class OrderServiceImpl implements OrderService{

    @Resource
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public void insert(OrderEntity orderEntity){
        orderRepository.save(orderEntity);
    }
}
