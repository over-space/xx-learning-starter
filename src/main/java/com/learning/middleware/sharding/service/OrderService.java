package com.learning.middleware.sharding.service;

import com.learning.middleware.sharding.entity.OrderItemEntity;
import com.learning.middleware.sharding.entity.OrderLogEntity;
import com.learning.middleware.sharding.entity.OrderEntity;
import com.learning.middleware.sharding.entity.OrderTypeEntity;

import java.util.List;

public interface OrderService {

    void insert(OrderEntity orderEntity, OrderItemEntity orderItemEntity);

    void deleteAll();

    void saveOrderLog(OrderLogEntity orderLogEntity);

    void deleteAllOrderLog();

    List<OrderLogEntity> findOrderLogList();

    void saveOrderType(OrderTypeEntity orderTypeEntity);
}
