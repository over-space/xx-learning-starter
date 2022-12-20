package com.learning.io.aio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author 李芳
 * @since 2022/12/20
 */
public class AIOClient {

    private static final Logger logger = LogManager.getLogger(AIOClient.class);

    public static void main(String[] args) throws Exception {
        // 打开一个SocketChannel通道并获取AsynchronousSocketChannel实例
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();

        // 连接到服务器并处理连接结果
        client.connect(new InetSocketAddress("127.0.0.1", AIOServer.PORT), null, new CompletionHandler<Void, Void>() {
            @Override
            public void completed(final Void result, final Void attachment) {
                logger.info("成功连接到服务器!");
                try {
                    // 给服务器发送信息并等待发送完成
                    client.write(ByteBuffer.wrap("From client:Hello i am client".getBytes())).get();

                    ByteBuffer readBuffer = ByteBuffer.allocate(128);
                    // 阻塞等待接收服务端数据
                    client.read(readBuffer).get();
                    logger.info("response : {}", new String(readBuffer.array()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                logger.info("连接到服务器失败， msg:{}", exc.getMessage());
            }
        });

        System.in.read();
    }

}
