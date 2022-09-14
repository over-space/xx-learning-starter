package com.learning.mq.tx.repository;

import com.learning.mq.tx.entity.MsgRecordEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author 李芳
 * @since 2022/9/13
 */
public interface MsgRecordRepository extends CrudRepository<MsgRecordEntity, Long> {
}
