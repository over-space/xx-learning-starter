package com.learning.spring.repository;

import com.learning.spring.entity.AEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author lifang
 * @since 2022/2/7
 */
public interface ARepository extends CrudRepository<AEntity, Long> {
}
