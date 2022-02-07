package com.learning.spring.repository;

import com.learning.spring.entity.BEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author lifang
 * @since 2022/2/7
 */
public interface BRepository extends CrudRepository<BEntity, Long> {
}
