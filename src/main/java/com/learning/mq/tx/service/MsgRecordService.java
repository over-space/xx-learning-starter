package com.learning.mq.tx.service;

import com.learning.mq.tx.bo.MessageBody;
import com.learning.mq.tx.entity.MsgRecordEntity;

import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/13
 */
public interface MsgRecordService {

    MsgRecordEntity save(String topic, MessageBody msg);

    MsgRecordEntity save(String topic, String tags, MessageBody msg);

    List<MsgRecordEntity> findByIds(List<Long> ids);

    void updateMsgStatus(String msgId, MsgRecordEntity.MsgSendStatus msgSendStatus);
}
