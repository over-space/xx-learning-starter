package com.learning.sharding.repository;

import com.learning.sharding.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author 李芳
 * @since 2022/9/2
 */
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
}
