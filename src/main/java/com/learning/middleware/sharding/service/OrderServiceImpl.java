package com.learning.middleware.sharding.service;

import com.google.common.collect.Lists;
import com.learning.middleware.sharding.entity.OrderItemEntity;
import com.learning.middleware.sharding.entity.OrderLogEntity;
import com.learning.middleware.sharding.entity.OrderEntity;
import com.learning.middleware.sharding.entity.OrderTypeEntity;
import com.learning.middleware.sharding.repository.OrderItemRepository;
import com.learning.middleware.sharding.repository.OrderLogRepository;
import com.learning.middleware.sharding.repository.OrderRepository;
import com.learning.middleware.sharding.repository.OrderTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderRepository orderRepository;
    @Resource
    private OrderItemRepository orderItemRepository;
    @Resource
    private OrderLogRepository orderLogRepository;
    @Resource
    private OrderTypeRepository orderTypeRepository;

    @Override
    @Transactional
    public void insert(OrderEntity orderEntity, OrderItemEntity orderItemEntity) {
        orderRepository.save(orderEntity);

        orderItemEntity.setOrderId(orderEntity.getId());
        orderItemRepository.save(orderItemEntity);

        // OrderLogEntity orderLogEntity = new OrderLogEntity();
        // orderLogEntity.setOrderType(1);
        // orderLogEntity.setCreatedDate(LocalDateTime.now());
        // orderLogRepository.save(orderLogEntity);
    }

    @Override
    @Transactional
    public void deleteAll() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        ;
    }

    @Override
    @Transactional
    public void saveOrderLog(OrderLogEntity orderLogEntity) {
        orderLogRepository.save(orderLogEntity);
    }

    @Override
    @Transactional
    public void deleteAllOrderLog() {
        orderLogRepository.deleteAll();
    }

    @Override
    public List<OrderLogEntity> findOrderLogList() {
        Iterable<OrderLogEntity> iterable = orderLogRepository.findAll();
        return Lists.newArrayList(iterable);
    }

    @Override
    public void saveOrderType(OrderTypeEntity orderTypeEntity) {
        orderTypeRepository.save(orderTypeEntity);
    }
}
