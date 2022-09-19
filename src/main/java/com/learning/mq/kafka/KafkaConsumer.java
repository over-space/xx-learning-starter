package com.learning.mq.kafka;

import com.learning.mq.tx.bo.MessageBody;
import com.learning.mq.tx.consumer.AbstractKafkaConsumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author 李芳
 * @since 2022/9/14
 */
@Component
public class KafkaConsumer extends AbstractKafkaConsumer {

    private static final Logger logger = LogManager.getLogger(KafkaConsumer.class);

    private static LongAdder consumerBatchIndex = new LongAdder();


    @Override
    @KafkaListener(id = "consumer-a", topics = "${kafka.topic.bigdata-test}", groupId = "default-consumer",idIsGroup = false, concurrency = "3")
    public void batchConsumer(List<ConsumerRecord<String, MessageBody>> records, Acknowledgment acknowledgment){
        consumerBatchIndex.increment();
        for (ConsumerRecord<String, MessageBody> record : records) {
            logger.info("id:consumer-a, consumerBatchIndex:{}, size:{}, record:{}", consumerBatchIndex.intValue(), records.size(), record.value());
        }
        acknowledgment.acknowledge();
    }
}
