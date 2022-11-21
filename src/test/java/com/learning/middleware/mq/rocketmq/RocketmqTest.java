package com.learning.middleware.mq.rocketmq;

import com.learning.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author 李芳
 * @since 2022/11/16
 */
public class RocketmqTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(RocketmqTest.class);

    @Test
    public void testProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("test-producer01");
        producer.setNamesrvAddr("192.168.137.212:9876");
        producer.start();

        for (int i = 0; i < 10; i++) {
            Message message;
            if(i % 2 == 0){
                message = new Message("wula", "tag-a", "abc_" + i, ("hello rocketmq " + i).getBytes());
            }else{
                message = new Message("wula", "tag-b", "abc_" + i, ("hello rocketmq " + i).getBytes());
            }
            message.setWaitStoreMsgOK(true);

            SendResult send = producer.send(message);

            logger.info("SendResult : {}", send);
        }
    }

    @Test
    public void testConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer01");
        consumer.setNamesrvAddr("192.168.137.212:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("wula","tag-a");


        // 并行
        // consumer.registerMessageListener(new MessageListenerConcurrently() {
        //     @Override
        //     public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        //         list.forEach(msg -> {
        //             String text = new String(msg.getBody());
        //             logger.info("msg : {}", text);
        //         });
        //         return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        //     }
        // });

        // 有序
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                list.forEach(msg -> {
                    String text = new String(msg.getBody());
                    logger.info("msg : {}", text);
                });
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });

        consumer.start();

        sleep(20);
    }

}
