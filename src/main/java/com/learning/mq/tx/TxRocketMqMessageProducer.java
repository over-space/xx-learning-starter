package com.learning.mq.tx;

import com.alibaba.fastjson.JSONObject;
import com.learning.mq.tx.config.RocketMqCondition;
import com.learning.mq.tx.entity.MsgRecordEntity;
import com.learning.mq.tx.bo.MessageBody;
import com.learning.mq.tx.service.MsgRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/13
 */
@Component("txRocketMqMessageProducer")
@Conditional(RocketMqCondition.class)
public class TxRocketMqMessageProducer implements MessageProducer{

    private static final Logger logger = LogManager.getLogger(TxRocketMqMessageProducer.class);


    @Resource
    private DefaultMQProducer defaultMQProducer;
    @Resource
    private MsgRecordService msgRecordService;

    /**
     * 消息存进数据库，缓存在threadLocal中，事务提交后再发送消息
     */
    @Override
    public void sendAfterCommit(String topic, MessageBody value){
        sendAfterCommit(msgRecordService, topic, value);
    }

    /**
     * 事务提交后调用该方法
     */
    @Override
    public void sendMsgFromThreadLocal() {
        List<Long> msgIds = threadLocal.get();
        if(CollectionUtils.isEmpty(msgIds)){
            return;
        }

        List<MsgRecordEntity> msgRecordEntityList = msgRecordService.findByIds(msgIds);
        for (MsgRecordEntity msgRecord : msgRecordEntityList) {
            try {

                SendResult send = defaultMQProducer.send(new Message(msgRecord.getTopic(), msgRecord.getTags(), msgRecord.getKey(), msgRecord.getMsgBody().getBytes()));

                if (SendStatus.SEND_OK.equals(send.getSendStatus())) {
                    //发送成功一条就删一条消息，这样数据库表也不会变大
                    msgRecord.setMsgStatus(1);
                } else {
                    //发送失败就等待下次重试，并将消息保留在表中, 通过定时任务重试
                    msgRecord.setMsgStatus(3);
                }
                msgRecordService.update(msgRecord);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
        threadLocal.remove();
    }

    public SendResult send(String topic, Object value) {
        try {
            return defaultMQProducer.send(new Message(topic, JSONObject.toJSONString(value).getBytes()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public SendResult send(String topic, String tags, Object value) {
        try {
            return defaultMQProducer.send(new Message(topic, tags, JSONObject.toJSONString(value).getBytes()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
