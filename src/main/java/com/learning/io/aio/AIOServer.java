package com.learning.io.aio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author 李芳
 * @since 2022/12/20
 */
public class AIOServer {

    public static final int PORT = 5555;

    private static final Logger logger = LogManager.getLogger(AIOServer.class);


    public static void main(String[] args) throws Exception {
        //首先打开一个ServerSocket通道并获取AsynchronousServerSocketChannel实例：
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();

        //绑定需要监听的端口到serverSocketChannel:
        serverSocketChannel.bind(new InetSocketAddress(PORT));

        //实现一个CompletionHandler回调接口handler，
        //之后需要在handler的实现中处理连接请求和监听下一个连接、数据收发，以及通信异常。
        CompletionHandler<AsynchronousSocketChannel, Object> handler = new CompletionHandler<AsynchronousSocketChannel,
                        Object>() {
            @Override
            public void completed(final AsynchronousSocketChannel result, final Object attachment) {
                // 继续监听下一个连接请求
                serverSocketChannel.accept(attachment, this);
                try {
                    logger.info("接收到一个客户端连接：{}", result.getRemoteAddress());

                    // 给客户端发送数据并等待发送完成
                    result.write(ByteBuffer.wrap("From Server:Hello i am server".getBytes())).get();

                    ByteBuffer readBuffer = ByteBuffer.allocate(128);

                    // 阻塞等待客户端接收数据
                    result.read(readBuffer).get();

                    logger.info("接收到客户端消息：{}", new String(readBuffer.array()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(final Throwable exc, final Object attachment) {
                System.out.println();
                logger.info("出错了, msg:{}", exc.getMessage());
            }
        };

        System.in.read();
    }
}
