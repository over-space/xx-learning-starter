package com.learning.io.nio.sample;

import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class AcceptHandlerChooseSelectorStrategy implements ChooseSelectorStrategy{

    private AtomicInteger xid;
    private SelectorThread[] selectorThreads;

    public AcceptHandlerChooseSelectorStrategy(SelectorThread[] selectorThreads) {
        this.xid = new AtomicInteger(0);
        this.selectorThreads = selectorThreads;
    }

    @Override
    public SelectorThread chooseSelector(Channel channel) {
        if(channel instanceof ServerSocketChannel){
            return this.selectorThreads[0];
        }else{
            int index = xid.incrementAndGet() % (this.selectorThreads.length - 1);
            return this.selectorThreads[index + 1];
        }
    }
}
