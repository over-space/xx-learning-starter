package com.learning.spring;

import com.learning.spring.entity.AEntity;
import com.learning.spring.entity.BEntity;
import com.learning.spring.repository.ARepository;
import com.learning.spring.repository.BRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author lifang
 * @since 2022/2/7
 */
@Service
public class AService {

    private static final Logger logger = LogManager.getLogger(AService.class);


    @Resource
    private ARepository aRepository;
    @Resource
    private BRepository bRepository;


    @Transactional(rollbackFor = Exception.class)
    public void insertByTransactional() {
        try {
            insertA("insertByTransactional");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        logger.info("currentTransactionName : {}, {}", currentTransactionName, TransactionSynchronizationManager.isActualTransactionActive());

        BEntity b = new BEntity();
        b.setCreatedDate(LocalDateTime.now());
        b.setName("b1");
        bRepository.save(b);
    }

    public void insertByNonTransactional() {
        insertA("insertByNonTransactional");

        String currentTransactionName = TransactionSynchronizationManager.getCurrentTransactionName();

        logger.info("currentTransactionName : {}, {}", currentTransactionName, TransactionSynchronizationManager.isActualTransactionActive());

        BEntity b = new BEntity();
        b.setCreatedDate(LocalDateTime.now());
        b.setName("b2");
        bRepository.save(b);
    }

    private void insertA(String name) {
        AEntity a = new AEntity();
        a.setName(name);
        a.setAge(20);
        a.setCreatedDate(LocalDateTime.now());
        aRepository.save(a);
        throw new RuntimeException("RuntimeException");
    }

}
