package com.learning.disruptor.demo02;

import java.util.function.Consumer;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;


public class OrderProducer {

    public static void publish(Consumer<RingBuffer> consumer) {
        Disruptor<OrderEvent> disruptor = new Disruptor<>(new OrderEvent(), 16, r -> {
            return new Thread(r);
        });
    
        disruptor.handleEventsWith(new OrderConsumer());

        RingBuffer<OrderEvent> ringBuffer = disruptor.start();

        consumer.accept(ringBuffer);

        disruptor.shutdown();
    }
 
}
