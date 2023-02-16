package com.learning.middleware.mq.tx.core;

import com.learning.middleware.mq.tx.bo.MessageBody;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

@Component
public class LoggerKafkaProducerListener extends AbstractKafkaProducerListener{

    @Override
    public void onError(ProducerRecord<String, MessageBody> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        super.onError(producerRecord, recordMetadata, exception);
    }

    @Override
    public void onSuccess(ProducerRecord<String, MessageBody> producerRecord, RecordMetadata recordMetadata) {
        super.onSuccess(producerRecord, recordMetadata);
    }
}
