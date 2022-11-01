package com.learning.middleware.mq.sample.repository;

import com.learning.middleware.mq.sample.entity.DEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author lifang
 * @since 2022/2/7
 */
public interface DRepository extends CrudRepository<DEntity, Long> {
}
