package com.learning.disruptor;

import com.learning.disruptor.demo02.MessageProducer;
import com.learning.disruptor.demo02.OrderConsumer;
import com.learning.disruptor.demo02.OrderEvent;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lifang
 * @since 3/28/2022
 */
public class DisruptorTest {


    @Test
    void testDisruptor(){

        MessageProducer.publish(new OrderEvent(), Lists.newArrayList(new OrderConsumer(), new OrderConsumer()), (ringBuffer -> {

            for (int i = 0; i < 100; i++) {
                long seq = ringBuffer.next();

                OrderEvent orderEvent = (OrderEvent) ringBuffer.get(seq);

                orderEvent.setMessage("hello disruptor, " + i);

                ringBuffer.publish(seq);
            }

        }));

    }

}
