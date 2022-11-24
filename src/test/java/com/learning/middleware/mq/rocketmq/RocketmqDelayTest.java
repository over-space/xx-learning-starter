package com.learning.middleware.mq.rocketmq;

import com.learning.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/11/24
 */
public class RocketmqDelayTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(RocketmqTest.class);

    private static final String NAME_SRV_ADDR = "192.168.137.211:9876;192.168.137.212:9876;192.168.137.213:9876";

    /**
     * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     * @throws Exception
     */
    @Test
    public void testProducer() throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("producer-dealy-01");
        producer.setNamesrvAddr(NAME_SRV_ADDR);
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message message;
            if(i % 2 == 0){
                message = new Message("topic-dealy-01", "tag-a", "key-abc", ("hello rocketmq0 " + i).getBytes());
            }else{
                message = new Message("topic-dealy-01", "tag-b", "key-123", ("hello rocketmq1 " + i).getBytes());
            }
            message.setWaitStoreMsgOK(true);
            message.setDelayTimeLevel((i % 4) + 1);
            SendResult send = producer.send(message);

            logger.info("SendResult : {}", send);
        }
        System.in.read();
    }

    @Test
    public void testConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-dealy-01");
        consumer.setNamesrvAddr(NAME_SRV_ADDR);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("topic-dealy-01", "*");
        consumer.registerMessageListener((MessageListenerOrderly) (list, consumeOrderlyContext) -> {
            list.forEach(msg -> {
                String text = new String(msg.getBody());
                logger.info("msg : {}", text);
            });
            return ConsumeOrderlyStatus.SUCCESS;
        });
        consumer.start();
        System.in.read();
    }
}
