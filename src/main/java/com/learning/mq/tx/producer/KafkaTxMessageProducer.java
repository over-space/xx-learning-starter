package com.learning.mq.tx.producer;

import com.alibaba.fastjson.JSONObject;
import com.learning.mq.tx.bo.MessageBody;
import com.learning.mq.tx.config.KafkaCondition;
import com.learning.mq.tx.core.TransactionMessageThreadLocal;
import com.learning.mq.tx.entity.MsgRecordEntity;
import com.learning.mq.tx.service.MsgRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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

        TransactionMessageThreadLocal.foreach(transactionMessageIds -> {

            List<MsgRecordEntity> msgRecordEntityList = msgRecordService.findByIds(transactionMessageIds);

            for (MsgRecordEntity msgRecord : msgRecordEntityList) {
                try {
                    MessageBody messageBody = JSONObject.parseObject(msgRecord.getMsgBody(), MessageBody.class);
                    if (StringUtils.isNotBlank(msgRecord.getKey())) {
                        kafkaTemplate.send(msgRecord.getTopic(), msgRecord.getKey(), messageBody);
                    } else {
                        kafkaTemplate.send(msgRecord.getTopic(), messageBody);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

        });
    }
}
