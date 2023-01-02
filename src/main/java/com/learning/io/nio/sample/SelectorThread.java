package com.learning.io.nio.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class SelectorThread implements Runnable{

    private static final Logger logger = LogManager.getLogger(SelectorThread.class);


    private Selector selector;
    public LinkedBlockingQueue<Channel> channelQueue = new LinkedBlockingQueue<>();
    private SelectorThreadGroup selectorThreadGroup;

    public SelectorThread(SelectorThreadGroup selectorThreadGroup) throws IOException {
        this.selector = Selector.open();
        this.selectorThreadGroup = selectorThreadGroup;
    }

    @Override
    public void run() {

        while (true){
            try {
                // select()会阻塞住，等待selector.wakeup()唤醒
                logger.info("selector#select before, threadName:{}, channelQueue:{}", Thread.currentThread().getName(), this.channelQueue.size());
                int nums = this.selector.select();
                logger.info("selector#select after, threadName:{}, channelQueue:{}", Thread.currentThread().getName(), this.channelQueue.size());

                if(nums > 0){

                    Set<SelectionKey> selectionKeys = this.selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if(selectionKey.isAcceptable()){
                            acceptHandler(selectionKey);
                        }else if(selectionKey.isReadable()){
                            readHandler(selectionKey);
                        }else if(selectionKey.isWritable()){

                        }
                    }
                }

                if(!this.channelQueue.isEmpty()){
                    Channel channel = this.channelQueue.take();
                    if(channel instanceof ServerSocketChannel){
                        ServerSocketChannel server = ((ServerSocketChannel) channel);
                        server.register(this.selector, SelectionKey.OP_ACCEPT);
                        logger.info("server register, threadName:{}", Thread.currentThread().getName());
                    }else if(channel instanceof SocketChannel){
                        SocketChannel client = (SocketChannel) channel;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        client.register(selector, SelectionKey.OP_READ, buffer);
                        logger.info("client register, threadName:{}", Thread.currentThread().getName());
                    }
                }
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }

        }

    }

    private void readHandler(SelectionKey selectionKey) {
        logger.info("threadName:{}, readHandler....", Thread.currentThread().getName());

        ByteBuffer byteBuffer = (ByteBuffer)selectionKey.attachment();
        SocketChannel client = (SocketChannel) selectionKey.channel();
        byteBuffer.clear();
        while (true){
            try {
                int num = client.read(byteBuffer);
                if(num > 0){
                    // 读取到客户端输入内容
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()){
                        // 将客户端输入的内容，输出给客户端。
                        client.write(byteBuffer);
                    }
                    client.write(ByteBuffer.wrap("--------------------\n".getBytes()));
                    byteBuffer.clear();
                }else if(num == 0){
                    break;
                }else{
                    logger.info("client:{} closed....", client.getRemoteAddress());
                    selectionKey.cancel();
                    break;
                }
            }catch (Exception e){
                logger.error(e.getMessage(), e);
                selectionKey.cancel();
                break;
            }
        }
    }

    private void acceptHandler(SelectionKey selectionKey) {
        logger.info("threadName:{}, acceptHandler....", Thread.currentThread().getName());
        try {
            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel client = channel.accept();
            client.configureBlocking(false);

            selectorThreadGroup.chooseSelector(client);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    public Selector getSelector() {
        return selector;
    }
}
