package com.learning.io.rpc.proxy;

import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientPool {
    private NioSocketChannel[] clientList;
    private Object[] locks;

    public ClientPool(int pool) {
        clientList = new NioSocketChannel[pool];
        locks = new Object[pool];
        for (int i = 0; i < pool; i++) {
            locks[i] = new Object();
        }
    }

    public NioSocketChannel[] getClientList() {
        return clientList;
    }

    public Object[] getLocks() {
        return locks;
    }
}
