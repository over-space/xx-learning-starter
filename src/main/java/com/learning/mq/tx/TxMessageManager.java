package com.learning.mq.tx;

import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.annotation.Resource;

/**
 * @author 李芳
 * @since 2022/9/13
 */
@Component("transactionManager")
@Primary
public class TxMessageManager extends JpaTransactionManager {

    @Resource
    private MessageProducer messageProducer;

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        try {
            super.doCommit(status);
        } finally {
            // 数据库事务提交之后，发送MQ消息。
            messageProducer.sendMsgFromThreadLocal();
        }
    }

}
