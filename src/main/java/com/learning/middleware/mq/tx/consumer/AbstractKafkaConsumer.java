package com.learning.middleware.mq.tx.consumer;

import com.learning.middleware.mq.tx.bo.MessageBody;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/19
 */
public abstract class AbstractKafkaConsumer implements MessageConsumer{

    protected void consumer(ConsumerRecord<String, MessageBody> record, Acknowledgment acknowledgment){

    }

    /**
     * 批量消费
     * @param records
     * @param acknowledgment
     */
    protected void batchConsumer(List<ConsumerRecord<String, MessageBody>> records, Acknowledgment acknowledgment){

    }
}
