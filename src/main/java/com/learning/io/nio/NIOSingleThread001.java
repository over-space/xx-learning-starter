package com.learning.io.nio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 李芳
 * @since 2022/12/30
 */
public class NIOSingleThread001 {

    private static final Logger logger = LogManager.getLogger(NIOSingleThread001.class);

    public static final Integer PORT = 9090;

    private static ServerSocketChannel server = null;
    private static Selector selector = null;

    public static void main(String[] args) throws IOException {
        start();
    }

    public static void initSocket() throws IOException {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(PORT));

        selector = Selector.open();
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    private static void start() throws IOException {
        initSocket();
        logger.info("服务器启动了~~~~~~~");
        while (true) {
            // Set<SelectionKey> keys = selector.keys();
            // logger.info("keys.size : {}", keys.size());
            while (selector.select(50) > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();  //返回的有状态的fd集合
                Iterator<SelectionKey> iter = selectionKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove(); //set  不移除会重复循环处理
                    if (key.isAcceptable()) {
                        acceptHandler(key);
                    } else if (key.isReadable()) {
                        readHandler(key);
                    } else if (key.isWritable()) {
                        writeHandler(key);
                    }
                }
            }
        }
    }

    private static void writeHandler(SelectionKey key) {
        logger.info("触发执行writeHandler....");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        while (buffer.hasRemaining()) {
            try {

                client.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buffer.clear();
        key.cancel();
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readHandler(SelectionKey key) {
        logger.info("触发执行readHandler....");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        try {
            while (true) {
                read = client.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    // 将数据写回给客户端
                    client.write(ByteBuffer.wrap("hello world\n".getBytes()));
                    while (buffer.hasRemaining()) {
                        client.write(buffer);
                    }
                    logger.info("收到client数据：{}", new String(buffer.array()));
                    buffer.clear();
                } else if (read == 0) {
                    break;
                } else {
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel client = ssc.accept(); //来啦，目的是调用accept接受客户端  fd7
            client.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocate(8192);  //前边讲过了
            client.register(selector, SelectionKey.OP_READ, buffer);

            logger.info("新客户端：{}", client.getRemoteAddress());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
