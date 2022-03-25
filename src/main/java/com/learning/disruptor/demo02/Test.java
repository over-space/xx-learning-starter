package com.learning.disruptor.demo02;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.UUID;
import java.util.concurrent.ThreadFactory;


public class Test {
    
    public static void main(String[] args) {

        OrderProducer.publish((ringBuffer) -> {
            for (int i = 0; i < 1000; i++) {
                long seq = ringBuffer.next();
                OrderEvent orderEvent = (OrderEvent)ringBuffer.get(seq);
                orderEvent.setMessage("hello disruptor, msg: " + i);
                ringBuffer.publish(seq);
            }
        });

    }

}
