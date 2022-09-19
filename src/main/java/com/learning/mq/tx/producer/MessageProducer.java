package com.learning.mq.tx.producer;

import com.learning.mq.tx.bo.MessageBody;
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

    ThreadLocal<List<Long>> threadLocal = new NamedInheritableThreadLocal("tx-message");

    default void sendAfterCommit(MsgRecordService msgRecordService, String topic, MessageBody messageBody){
        // 消息入库
        MsgRecordEntity msgRecordEntity = msgRecordService.save(topic, messageBody);

        List<Long> msgIds = threadLocal.get();

        msgIds = msgIds == null ? new CopyOnWriteArrayList<>() : msgIds;

        msgIds.add(msgRecordEntity.getId());

        threadLocal.set(msgIds);
    }

    void sendAfterCommit(String topic, MessageBody messageBody);

    void sendMsgFromThreadLocal();
}
