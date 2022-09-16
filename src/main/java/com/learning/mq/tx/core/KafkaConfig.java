package com.learning.mq.tx.core;

import com.learning.mq.tx.bo.MessageBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

/**
 * @author 李芳
 * @since 2022/9/16
 */
@Configuration
public class KafkaConfig {


    @Bean
    public KafkaTemplate<String, MessageBody> kafkaTemplate(ProducerFactory<String, MessageBody> producerFactory,
                                                       ProducerListener<String, MessageBody> producerListener) {
        KafkaTemplate<String, MessageBody> kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setProducerListener(producerListener);// 设置生产者监听器
        return kafkaTemplate;
    }

}
