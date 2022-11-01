package com.learning.middleware.mq.tx.service;

import com.learning.middleware.mq.tx.bo.MessageBody;
import com.learning.middleware.mq.tx.entity.MsgRecordEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/13
 */
public interface MsgRecordService {

    MsgRecordEntity save(String topic, MessageBody msg);

    MsgRecordEntity save(String topic, String tags, MessageBody msg);

    List<MsgRecordEntity> findByIds(List<Long> ids);

    List<MsgRecordEntity> findByMsgStatus(MsgRecordEntity.MsgSendStatus msgSendStatus, LocalDateTime createdDateTime);

    void updateMsgStatus(String msgId, MsgRecordEntity.MsgSendStatus msgSendStatus);
}
