package com.learning.mq.tx.repository;

import com.learning.mq.tx.entity.MsgRecordEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/13
 */
public interface MsgRecordRepository extends CrudRepository<MsgRecordEntity, Long> {

    @Modifying
    @Query("update MsgRecordEntity set msgStatus=?2 where msgId=?1")
    void updateMsgStatus(String msgId, int msgStatus);

    @Query("select msg from MsgRecordEntity msg where msg.msgStatus=?1 and msg.createdDateTime<=?2")
    List<MsgRecordEntity> findByMsgStatus(int msgStatus, LocalDateTime createdDateTime);
}
