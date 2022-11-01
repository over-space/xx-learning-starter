package com.learning.middleware.sharding.repository;

import com.learning.middleware.sharding.entity.OrderTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 李芳
 * @since 2022/9/26
 */
@Repository
public interface OrderTypeRepository extends CrudRepository<OrderTypeEntity, Long> {
}
