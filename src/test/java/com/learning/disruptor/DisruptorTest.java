package com.learning.disruptor;

import com.learning.BaseTest;
import com.learning.disruptor.demo02.OrderConsumer;
import com.learning.disruptor.demo02.OrderEvent;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

/**
 * @author lifang
 * @since 3/28/2022
 */
public class DisruptorTest extends BaseTest {


    @Test
    void testDisruptor(){

        DisruptorMessageProducer.publish(new OrderEvent(), Lists.newArrayList(new OrderConsumer("consumer-1"), new OrderConsumer("consumer-2")), (ringBuffer -> {

            IntStream.range(1, 500).parallel().forEach(index -> {
                long seq = ringBuffer.next();

                OrderEvent orderEvent = (OrderEvent) ringBuffer.get(seq);

                orderEvent.setMessage("hello disruptor, index:" + index);

                ringBuffer.publish(seq);
                logger.info("----------------------------------------------------- index: " + index + ", seq: " + seq);
            });

        }));
    }

}
