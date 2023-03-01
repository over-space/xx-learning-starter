package com.learning.io.nio.sample;

import java.nio.channels.Channel;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultChooseSelectorStrategy implements ChooseSelectorStrategy {

    private AtomicInteger xid;
    private SelectorThread[] selectorThreads;

    public DefaultChooseSelectorStrategy(SelectorThread[] selectorThreads) {
        this.xid = new AtomicInteger(0);
        this.selectorThreads = selectorThreads;
    }

    @Override
    public SelectorThread chooseSelector(Channel channel) {
        int index = xid.incrementAndGet() % selectorThreads.length;
        return this.selectorThreads[index];
    }
}
