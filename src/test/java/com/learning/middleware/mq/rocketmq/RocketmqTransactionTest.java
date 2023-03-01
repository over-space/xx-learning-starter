package com.learning.middleware.mq.rocketmq;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.learning.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 李芳
 * @since 2022/11/23
 */
public class RocketmqTransactionTest extends BaseTest {


    private static final Logger logger = LogManager.getLogger(RocketmqTransactionTest.class);

    private static final String NAME_SRV_ADDR = "192.168.137.212:9876";

    @Test
    public void testProducer() throws MQClientException, IOException {
        TransactionMQProducer producer = new TransactionMQProducer("test-producer-tx01");
        producer.setNamesrvAddr(NAME_SRV_ADDR);


        // 设置事务监听器
        producer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                String mode = message.getUserProperty("mode");
                // 执行本地事务
                Integer type = (Integer) o;
                if (type == 0) {
                    logger.info("UNKNOW: message: {}, type: {}, mode:{}", message, type, mode);
                    return LocalTransactionState.UNKNOW;
                } else if (type == 1) {
                    logger.info("ROLLBACK_MESSAGE: message: {}, type: {}, mode:{}", message, type, mode);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                logger.info("COMMIT_MESSAGE: message: {}, type: {}, mode:{}", message, type, mode);
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                Integer count = Integer.valueOf(messageExt.getUserProperty("count"));

                int reconsumeTimes = messageExt.getReconsumeTimes();

                logger.info("checkLocalTransaction message:{}, count:{}, reconsumeTimes:{}", new String(messageExt.getBody()), count, reconsumeTimes);

                messageExt.putUserProperty("count", (count + 1) + "");

                if (count >= 3) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }

                // 回查本地事务事件
                return LocalTransactionState.UNKNOW;
            }
        });


        producer.setExecutorService(new ThreadPoolExecutor(3, 10,
                1000, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(32), new ThreadFactoryBuilder().setNameFormat("tx_rocketmq_%d").build()));

        producer.start();

        for (int i = 0; i < 20; i++) {
            Message msg = new Message("topic_wang", "tag01", "key_transaction", ("hello rocketmq " + i).getBytes());
            msg.putUserProperty("mode", (i % 5) + "");
            msg.putUserProperty("count", 0 + "");
            producer.sendMessageInTransaction(msg, i % 3);
        }

        System.in.read();
    }

    @Test
    public void testConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-tx01");
        consumer.setNamesrvAddr(NAME_SRV_ADDR);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("topic_wang", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                msgs.forEach(msg -> System.out.println(msg));

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();

        System.in.read();
    }

}
