package com.learning.middleware.mq.tx.producer;

import com.alibaba.fastjson.JSONObject;
import com.learning.middleware.mq.tx.bo.MessageBody;
import com.learning.middleware.mq.tx.core.TransactionMessageThreadLocal;
import com.learning.middleware.mq.tx.entity.MsgRecordEntity;
import com.learning.middleware.mq.tx.config.KafkaCondition;
import com.learning.middleware.mq.tx.service.MsgRecordService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Conditional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/14
 */
@Service("kafkaTxMessageProducer")
@Conditional(KafkaCondition.class)
public class KafkaTxMessageProducer implements MessageProducer {

    private static final Logger logger = LogManager.getLogger(RocketmqTxMessageProducer.class);

    @Resource
    private KafkaTemplate<String, MessageBody> kafkaTemplate;
    @Resource
    private MsgRecordService msgRecordService;


    @Override
    @Transactional
    public void sendAfterCommit(String topic, MessageBody value) {
        sendAfterCommit(msgRecordService, topic, value);
    }

    @Override
    @Transactional
    public void sendMsgFromThreadLocal() {

        TransactionMessageThreadLocal.foreachTransactionMessage(transactionMessageIds -> {

            List<MsgRecordEntity> msgRecordEntityList = msgRecordService.findByIds(transactionMessageIds);

            msgRecordEntityList.stream().forEach(this::send);
        });
    }

    @Override
    public void send(MsgRecordEntity msgRecordEntity) {
        MessageBody messageBody = JSONObject.parseObject(msgRecordEntity.getMsgBody(), MessageBody.class);

        ProducerRecord producerRecord = StringUtils.isNotBlank(msgRecordEntity.getKey())
                ? new ProducerRecord<>(msgRecordEntity.getTopic(), msgRecordEntity.getKey(), messageBody)
                : new ProducerRecord<>(msgRecordEntity.getTopic(), messageBody);

        if (BooleanUtils.isTrue(messageBody.isTransaction())) {
            kafkaTemplate.executeInTransaction(kafkaOperations -> {
                kafkaOperations.execute(producer -> producer.send(producerRecord));
                return true;
            });
        } else {
            kafkaTemplate.send(producerRecord);
        }
    }
}
