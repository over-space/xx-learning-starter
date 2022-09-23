package com.learning.sharding.service;

import com.learning.sharding.entity.OrderEntity;
import com.learning.sharding.entity.OrderItemEntity;
import com.learning.sharding.entity.OrderLogEntity;

public interface OrderService {

    void insert(OrderEntity orderEntity, OrderItemEntity orderItemEntity);

    void deleteAll();

    void saveOrderLog(OrderLogEntity orderLogEntity);
}
