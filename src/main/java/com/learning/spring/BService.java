package com.learning.spring;

import com.learning.mq.tx.core.aop.annontion.TransactionalMessage;
import com.learning.mq.tx.producer.MessageProducer;
import com.learning.mq.tx.bo.MessageBody;
import com.learning.spring.entity.BEntity;
import com.learning.spring.repository.BRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lifang
 * @since 2022/2/7
 */
@Service
public class BService {

    @Resource
    private BRepository bRepository;

    @Transactional
    public void save(){
        BEntity entity = new BEntity();
        entity.setName("b_" + ThreadLocalRandom.current().nextLong(0, 999999999));
        entity.setCreatedDate(LocalDateTime.now());
        bRepository.save(entity);
    }

    @TransactionalMessage
    public void save2(){
        BEntity entity = new BEntity();
        entity.setName("b_" + ThreadLocalRandom.current().nextLong(0, 999999999));
        entity.setCreatedDate(LocalDateTime.now());
        bRepository.save(entity);
    }
}
