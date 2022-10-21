package com.learning.disruptor;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

/**
 *
 */
public class DisruptorMessageProducer<T extends EventFactory, K extends EventHandler> {


    public static <T extends EventFactory, K extends EventHandler> void publish(T event, K eventHandler, Consumer<RingBuffer> consumer) {
        publish(event, Lists.newArrayList(eventHandler), consumer);
    }

    public static <T extends EventFactory, K extends EventHandler> void publish(T event, List<K> eventHandler, Consumer<RingBuffer> consumer) {
        Disruptor<T> disruptor = getDisruptor(event);

        disruptor.handleEventsWith(eventHandler.toArray(new EventHandler[0]));

        RingBuffer<T> ringBuffer = disruptor.start();

        consumer.accept(ringBuffer);

        disruptor.shutdown();

    }

    private static <T extends EventFactory> Disruptor<T> getDisruptor(T event){
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("disruptor-thread-%s").build();
        return new Disruptor<>(event, 1024, threadFactory);
    }

}
