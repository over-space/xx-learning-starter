package com.learning.mq.tx.producer;

import com.learning.mq.tx.bo.MessageBody;
import com.learning.mq.tx.core.TransactionMessageThreadLocal;
import com.learning.mq.tx.entity.MsgRecordEntity;
import com.learning.mq.tx.service.MsgRecordService;
import org.springframework.core.NamedInheritableThreadLocal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 李芳
 * @since 2022/9/14
 */
public interface MessageProducer {



    default void sendAfterCommit(MsgRecordService msgRecordService, String topic, MessageBody messageBody){
        // 消息入库
        MsgRecordEntity msgRecordEntity = msgRecordService.save(topic, messageBody);

        List<Long> transactionMessageIds = TransactionMessageThreadLocal.getTransactionMessageIds();

        transactionMessageIds.add(msgRecordEntity.getId());

    }

    void sendAfterCommit(String topic, MessageBody messageBody);

    void sendMsgFromThreadLocal();
}
