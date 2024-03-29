package com.learning.middleware.mq.tx.core;

import com.learning.middleware.mq.tx.bo.MessageBody;
import com.learning.middleware.mq.tx.entity.MsgRecordEntity;
import com.learning.middleware.mq.tx.service.MsgRecordService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

/**
 * @author 李芳
 * @since 2022/9/16
 */
@Component
@ConditionalOnProperty(name = "kafka.transaction.message.mode", havingValue = "AOP")
public class KafkaProducerListener extends AbstractKafkaProducerListener {

    private static final Logger logger = LogManager.getLogger(KafkaProducerListener.class);

    @Autowired
    private MsgRecordService msgRecordService;

    @Override
    public void onError(ProducerRecord<String, MessageBody> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        logger.warn("消息发送失败，msgId:{}", producerRecord.value().getMsgId());
        msgRecordService.updateMsgStatus(producerRecord.value().getMsgId(), MsgRecordEntity.MsgSendStatus.UNSENT);
    }

    @Override
    public void onSuccess(ProducerRecord<String, MessageBody> producerRecord, RecordMetadata recordMetadata) {
        logger.info("消息发送成功，msgId:{}", producerRecord.value().getMsgId());
        msgRecordService.updateMsgStatus(producerRecord.value().getMsgId(), MsgRecordEntity.MsgSendStatus.SEND_OK);
    }
}
