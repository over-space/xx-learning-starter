package com.learning.sharding.repository;

import com.learning.sharding.entity.OrderLogEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author 李芳
 * @since 2022/9/2
 */
public interface OrderLogRepository extends CrudRepository<OrderLogEntity, Long> {
}
