package com.learning.sharding.service;

import com.learning.sharding.entity.OrderEntity;
import com.learning.sharding.entity.OrderItemEntity;

public interface OrderService {

    void insert(OrderEntity orderEntity, OrderItemEntity orderItemEntity);

    void deleteAll();
}
