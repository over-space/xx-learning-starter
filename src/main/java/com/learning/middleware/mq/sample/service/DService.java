package com.learning.middleware.mq.sample.service;

import com.learning.middleware.mq.sample.entity.DEntity;
import com.learning.middleware.mq.sample.repository.DRepository;
import com.learning.middleware.mq.tx.bo.MessageBody;
import com.learning.middleware.mq.tx.producer.MessageProducer;
import com.learning.middleware.mq.tx.core.aop.annontion.TransactionalMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 李芳
 * @since 2022/10/21
 */
public class DService {

    @Resource
    private DRepository dRepository;

    @Resource
    private MessageProducer messageProducer;

    @Value("${kafka.topic.bigdata-test}")
    private String topic;

    @Transactional
    public void save(){
        DEntity entity = new DEntity();
        entity.setName("b_" + ThreadLocalRandom.current().nextLong(0, 999999999));
        entity.setCreatedDate(LocalDateTime.now());
        dRepository.save(entity);

        MessageBody messageBody = new MessageBody("b_" + entity.getId(), entity);
        messageProducer.sendAfterCommit(topic, messageBody);
    }

    @TransactionalMessage
    public void save2(){
        DEntity entity = new DEntity();
        entity.setName("b_" + ThreadLocalRandom.current().nextLong(0, 999999999));
        entity.setCreatedDate(LocalDateTime.now());
        dRepository.save(entity);

        MessageBody messageBody = new MessageBody("b_" + entity.getId(), entity);
        messageProducer.sendAfterCommit(topic, messageBody);
    }

}
