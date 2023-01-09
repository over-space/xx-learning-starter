package com.learning.io.rpc.proxy;

import com.learning.io.rpc.transport.ClientResponse;
import com.learning.io.rpc.transport.ServerRequestDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class ClientFactory {
    private final static int poolSize = 10;
    private final static ClientFactory factory = new ClientFactory();
    private final static ConcurrentHashMap<InetSocketAddress, ClientPool> clientList = new ConcurrentHashMap<>();


    private ClientFactory() {

    }

    public static ClientFactory getFactory() {
        return factory;
    }

    public static NioSocketChannel getClient(InetSocketAddress address){
        ClientPool clientPool = clientList.get(address);
        if(clientPool == null){
            synchronized (clientList) {
                clientPool = clientList.get(address);
                if(clientPool == null) {
                    clientList.putIfAbsent(address, new ClientPool(poolSize));
                    clientPool = clientList.get(address);
                }
            }
        }

        int rand = ThreadLocalRandom.current().nextInt(poolSize);

        if( clientPool.getClientList()[rand] != null && clientPool.getClientList()[rand].isActive()){
            return clientPool.getClientList()[rand];
        }else {
            synchronized (clientPool.getLocks()[rand]){
                if( clientPool.getClientList()[rand] != null && clientPool.getClientList()[rand].isActive()) {
                    return clientPool.getClientList()[rand];
                }else {
                    return clientPool.getClientList()[rand] = createClient(address);
                }
            }
        }
    }

    private static NioSocketChannel createClient(InetSocketAddress address) {
        //基于 netty 的客户端创建方式
        NioEventLoopGroup clientWorker = new NioEventLoopGroup(10);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(clientWorker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ServerRequestDecoder("client decoder"));
                        p.addLast(new ClientResponse());
                    }
                }).connect(address);
        try {
            NioSocketChannel client = (NioSocketChannel)connect.sync().channel();
            return client;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
