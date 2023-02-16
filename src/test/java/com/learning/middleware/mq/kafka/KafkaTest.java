package com.learning.middleware.mq.kafka;

import com.learning.BaseTest;
import com.learning.middleware.mq.tx.bo.MessageBody;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
// @EnableScheduling
@EnableAspectJAutoProxy
@EnableKafka
@EnableTransactionManagement
public class KafkaTest extends BaseTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    final static String TOPIC = "topic_xxoo";

    @Test
    void contextLoads() {
    }

    @Test
    void testProducer(){
        for (int i = 0; i < 50; i++) {
            ProducerRecord record = new ProducerRecord<>(TOPIC, new MessageBody<>(UUID.randomUUID().toString(), "hello_kafka-" + i));
            kafkaTemplate.send(record);
        }
        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

