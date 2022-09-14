package com.learning.mq.tx;

import com.learning.mq.tx.bo.MessageBody;
import com.learning.mq.tx.config.KafkaCondition;
import com.learning.mq.tx.entity.MsgRecordEntity;
import com.learning.mq.tx.service.MsgRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Conditional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/14
 */
@Service("txKafkaMessageProducer")
@Conditional(KafkaCondition.class)
public class TxKafkaMessageProducer implements MessageProducer {

    private static final Logger logger = LogManager.getLogger(TxRocketMqMessageProducer.class);

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
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
        List<Long> msgIds = threadLocal.get();
        if (CollectionUtils.isEmpty(msgIds)) {
            return;
        }

        List<MsgRecordEntity> msgRecordEntityList = msgRecordService.findByIds(msgIds);
        for (MsgRecordEntity msgRecord : msgRecordEntityList) {
            try {

                kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
                    @Override
                    public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
                        //发送成功一条就删一条消息，这样数据库表也不会变大
                        msgRecord.setMsgStatus(1);
                        msgRecordService.update(msgRecord);
                    }

                    @Override
                    public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                        //发送失败就等待下次重试，并将消息保留在表中, 通过定时任务重试
                        msgRecord.setMsgStatus(3);
                        msgRecordService.update(msgRecord);
                    }
                });

                if (StringUtils.isNotBlank(msgRecord.getKey())) {
                    kafkaTemplate.send(msgRecord.getTopic(), msgRecord.getKey(), msgRecord.getMsgBody());
                } else {
                    kafkaTemplate.send(msgRecord.getTopic(), msgRecord.getMsgBody());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
