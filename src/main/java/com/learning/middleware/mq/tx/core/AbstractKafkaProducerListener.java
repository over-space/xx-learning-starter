package com.learning.middleware.mq.tx.core;

import com.learning.middleware.mq.tx.bo.MessageBody;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.support.ProducerListener;

public abstract class AbstractKafkaProducerListener implements ProducerListener<String, MessageBody> {

    private static final Logger logger = LogManager.getLogger(KafkaProducerListener.class);

    @Override
    public void onError(ProducerRecord<String, MessageBody> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        logger.warn("消息发送失败，msgId:{}", producerRecord.value().getMsgId());
    }

    @Override
    public void onSuccess(ProducerRecord<String, MessageBody> producerRecord, RecordMetadata recordMetadata) {
        logger.info("消息发送成功，msgId:{}", producerRecord.value().getMsgId());
    }
}
