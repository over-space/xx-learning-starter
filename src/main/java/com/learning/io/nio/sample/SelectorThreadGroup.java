package com.learning.io.nio.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup{

    private static final Logger logger = LogManager.getLogger(SelectorThreadGroup.class);


    public SelectorThread[] selectorThreads;
    private AtomicInteger xid;

    private ChooseSelectorStrategy chooseSelectorStrategy;

    public SelectorThreadGroup(int threadCount) throws IOException {
        this.xid = new AtomicInteger(0);
        this.selectorThreads = new SelectorThread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            this.selectorThreads[i] = new SelectorThread(this);

            new Thread(this.selectorThreads[i]).start();
        }
    }

    public void setChooseSelectorStrategy(ChooseSelectorStrategy chooseSelectorStrategy){
        this.chooseSelectorStrategy = chooseSelectorStrategy;
    }

    public ChooseSelectorStrategy getChooseSelectorStrategy() {
        if(this.chooseSelectorStrategy == null){
            this.chooseSelectorStrategy = new DefaultChooseSelectorStrategy(this.selectorThreads);
        }
        return this.chooseSelectorStrategy;
    }

    public void bind(int port) {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            // 选择注册的selector
            chooseSelector(server);

        }catch (Exception e){
            logger.info(e.getMessage(), e);
        }
    }

    public void chooseSelector(Channel channel) {
        SelectorThread selectorThread = getChooseSelectorStrategy().chooseSelector(channel);
        selectorThread.channelQueue.add(channel);
        selectorThread.getSelector().wakeup();

        // try {
            // selectorThread.getSelector().wakeup();
            // s.register(selectorThread.getSelector(), SelectionKey.OP_ACCEPT);
        // } catch (ClosedChannelException e) {
        //     logger.error(e.getMessage(), e);
        // }
    }

    private SelectorThread chooseSelector(){
        int index = this.xid.incrementAndGet() % this.selectorThreads.length;
        return this.selectorThreads[index];
    }
}
