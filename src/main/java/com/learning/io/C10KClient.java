package com.learning.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/12/5
 */
public class C10KClient {

    private static final Logger logger = LogManager.getLogger(C10KClient.class);

    public static void main(String[] args) throws Exception {

        List<SocketChannel> channelList = new ArrayList<>(55000);

        InetSocketAddress serverAddress = new InetSocketAddress("192.168.137.211", 9090);

        for (int i = 10000; i < 65000; i++) {

            SocketChannel clientA = SocketChannel.open();
            SocketChannel clientB = SocketChannel.open();

            // 绑定client ip地址及端口
            clientA.bind(new InetSocketAddress("192.168.8.100", i));
            clientB.bind(new InetSocketAddress("192.168.80.100", i));

            // 连接服务器
            clientA.connect(serverAddress);
            clientB.connect(serverAddress);

            channelList.add(clientA);
            channelList.add(clientB);
        }

        logger.info("clients : {}", channelList.size());

        System.in.read();
    }

}
