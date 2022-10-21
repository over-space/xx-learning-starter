package com.learning.aop;

import com.learning.mq.tx.core.aop.annontion.TransactionalMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author 李芳
 * @since 2022/10/20
 */
@Service
public class AdviceService {

    private static final Logger logger = LogManager.getLogger(AdviceService.class);


    @TransactionalMessage
    public void test(){
        logger.info("=====================================");
    }

}
