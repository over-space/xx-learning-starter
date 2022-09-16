package com.learning.mq.tx.repository;

import com.learning.mq.tx.entity.MsgRecordEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author 李芳
 * @since 2022/9/13
 */
public interface MsgRecordRepository extends CrudRepository<MsgRecordEntity, Long> {

    @Modifying
    @Query("update MsgRecordEntity set msgStatus=?2 where msgId=?1")
    void updateMsgStatus(String msgId, int msgStatus);
}
