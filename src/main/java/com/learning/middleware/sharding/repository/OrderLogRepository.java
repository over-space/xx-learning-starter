package com.learning.middleware.sharding.repository;

import com.learning.middleware.sharding.entity.OrderLogEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author 李芳
 * @since 2022/9/2
 */
public interface OrderLogRepository extends CrudRepository<OrderLogEntity, Long> {
}
