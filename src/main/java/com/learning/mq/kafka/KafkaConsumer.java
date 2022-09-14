package com.learning.mq.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/14
 */
@Component
public class KafkaConsumer {

    private static final Logger logger = LogManager.getLogger(KafkaConsumer.class);


    @KafkaListener(id = "consumer-a", topics = "${kafka.topic.bigdata-test}", groupId = "default-consumer")
    public void consumerA(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment){

        for (ConsumerRecord<String, String> record : records) {
            logger.info("id:consumer-a, size:{}, record:{}", records.size(), record.value());
        }

        acknowledgment.acknowledge();

    }

    @KafkaListener(id = "consumer-b", topics = "${kafka.topic.bigdata-test}", groupId = "default-consumer")
    public void consumerB(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment){
        for (ConsumerRecord<String, String> record : records) {
            logger.info("id:consumer-b, size:{}, record:{}", records.size(), record.value());
        }
        acknowledgment.acknowledge();
    }
}
