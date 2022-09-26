package com.learning.sharding.service;

import com.learning.sharding.entity.OrderEntity;
import com.learning.sharding.entity.OrderItemEntity;
import com.learning.sharding.entity.OrderLogEntity;
import com.learning.sharding.entity.OrderTypeEntity;

import java.util.List;

public interface OrderService {

    void insert(OrderEntity orderEntity, OrderItemEntity orderItemEntity);

    void deleteAll();

    void saveOrderLog(OrderLogEntity orderLogEntity);

    void deleteAllOrderLog();

    List<OrderLogEntity> findOrderLogList();

    void saveOrderType(OrderTypeEntity orderTypeEntity);
}
