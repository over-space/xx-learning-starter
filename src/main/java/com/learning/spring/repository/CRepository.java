package com.learning.spring.repository;

import com.learning.spring.entity.CEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author lifang
 * @since 2022/2/7
 */
public interface CRepository extends CrudRepository<CEntity, Long> {
}
