package com.learning.disruptor.demo02;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.UUID;
import java.util.concurrent.ThreadFactory;


public class Test {
    
    public static void main(String[] args) {
        
        Disruptor disruptor = new Disruptor<>(new OrderEvent(), 16, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });

        disruptor.handleEventsWith(new OrderConsumer());
        RingBuffer<OrderEvent> ringBuffer = disruptor.start();

        for (long i = 10; i < 2000; i++) {
            long seq = ringBuffer.next();
            OrderEvent valueEvent = ringBuffer.get(seq);
            valueEvent.setMessage("hello disruptor-" + i);
            ringBuffer.publish(seq);
        }
        disruptor.shutdown();
    }

}
