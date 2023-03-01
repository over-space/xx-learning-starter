package com.learning.middleware.mq.tx.service;

import com.alibaba.fastjson.JSONObject;
import com.learning.middleware.mq.tx.bo.MessageBody;
import com.learning.middleware.mq.tx.entity.MsgRecordEntity;
import com.learning.middleware.mq.tx.repository.MsgRecordRepository;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/13
 */
@Service
public class MsgRecordServiceImpl implements MsgRecordService {

    @Resource
    private MsgRecordRepository msgRecordRepository;

    @Override
    public MsgRecordEntity save(String topic, MessageBody msg) {
        return save(topic, null, msg);
    }

    @Override
    @Transactional
    public MsgRecordEntity save(String topic, String tags, MessageBody msg) {
        MsgRecordEntity msgRecordEntity = new MsgRecordEntity();
        msgRecordEntity.setMsgId(msg.getMsgId());
        msgRecordEntity.setTopic(topic);
        msgRecordEntity.setTags(tags);
        msgRecordEntity.setMsgBody(JSONObject.toJSONString(msg));
        msgRecordEntity.setRetryCount(0);
        msgRecordEntity.setMsgStatus(0);
        msgRecordEntity.setCreatedDateTime(LocalDateTime.now());
        return msgRecordRepository.save(msgRecordEntity);
    }

    @Override
    public List<MsgRecordEntity> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        Iterable<MsgRecordEntity> iterable = msgRecordRepository.findAllById(ids);
        return IteratorUtils.toList(iterable.iterator());
    }

    @Override
    public List<MsgRecordEntity> findByMsgStatus(MsgRecordEntity.MsgSendStatus msgSendStatus, LocalDateTime createdDateTime) {
        return msgRecordRepository.findByMsgStatus(msgSendStatus.getValue(), createdDateTime);
    }

    @Override
    @Transactional
    public void updateMsgStatus(String msgId, MsgRecordEntity.MsgSendStatus msgSendStatus) {
        msgRecordRepository.updateMsgStatus(msgId, msgSendStatus.getValue());
    }
}
