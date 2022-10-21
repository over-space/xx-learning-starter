package com.learning.mq.sample.repository;

import com.learning.mq.sample.entity.DEntity;
import com.learning.spring.entity.BEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author lifang
 * @since 2022/2/7
 */
public interface DRepository extends CrudRepository<DEntity, Long> {
}
