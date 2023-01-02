package com.learning.io;

import com.learning.BaseTest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class NettyTest extends BaseTest {


    @Test
    void testByteBuf01() {

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(8, 20);
        printByteBuf(byteBuf);

        byteBuf.writeBytes(new byte[]{'a', 'b', 'c', 'd'});
        printByteBuf(byteBuf);

        byteBuf.writeBytes(new byte[]{'a', 'b', 'c', 'd'});
        printByteBuf(byteBuf);

        byteBuf.writeBytes(new byte[]{'a', 'b', 'c', 'd'});
        printByteBuf(byteBuf);

        byteBuf.writeBytes(new byte[]{'a', 'b', 'c', 'd'});
        printByteBuf(byteBuf);

        byteBuf.writeBytes(new byte[]{'a', 'b', 'c', 'd'});
        printByteBuf(byteBuf);

        // byteBuf.writeBytes(new byte[]{'a', 'b', 'c', 'd'});
        // printByteBuf(byteBuf);
    }

    @Test
    void testByteBuf02() {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(8, 20);
        printByteBuf(byteBuf);

        byteBuf.writeBytes(new byte[]{'a', 'b', 'c', 'd'});
        printByteBuf(byteBuf);

        logger.info("byteBuf.readCharSequence: {}", byteBuf.readCharSequence(byteBuf.readableBytes(), CharsetUtil.UTF_8));
        printByteBuf(byteBuf);

        byteBuf.resetReaderIndex();
        printByteBuf(byteBuf);
    }

    @Test
    void testNioEventLoopGroup() {
        NioEventLoopGroup thread = new NioEventLoopGroup(3);
        thread.execute(() -> {
            logger.info("-----------------------------------");
        });

        thread.execute(() -> {
            logger.info("-----------------------------------");
        });
    }

    @Test
    void testNettyClient() throws InterruptedException {
        NioEventLoopGroup thread = new NioEventLoopGroup(1);

        NioSocketChannel client = new NioSocketChannel();
        thread.register(client);

        // 接收server输出内容
        ChannelPipeline pipeline = client.pipeline();
        pipeline.addLast(new MyNettyInHandler());

        // 异步进行连接
        ChannelFuture connect = client.connect(new InetSocketAddress("127.0.0.1", 9090));
        connect.sync(); // 等待连接成功

        // 异步
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello netty server......\n".getBytes());
        ChannelFuture channelFuture = client.writeAndFlush(byteBuf);
        channelFuture.sync();// 等待数据write成功

        //等待服务端关闭连接
        connect.channel().closeFuture().sync();
    }

    @Test
    void testByteBuffer() {

        // ByteBuffer buffer = ByteBuffer.wrap(new byte[]{'1', '2','3', '4', '5'});// 堆内分配
        // ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        printByteBuffer(buffer);

        buffer.put("123456789".getBytes());
        buffer.put("abc".getBytes());
        printByteBuffer(buffer);

        byte b = buffer.get();
        printByteBuffer(buffer);
        logger.info("buffer#get() : {}", b);
        printLine();

        buffer.flip();
        printByteBuffer(buffer);

        buffer.compact();
        printByteBuffer(buffer);

        buffer.clear();
        printByteBuffer(buffer);
    }

    private void printByteBuffer(ByteBuffer buffer) {
        logger.info("position: {}", buffer.position());
        logger.info("limit: {}", buffer.limit());
        logger.info("capacity: {}", buffer.capacity());
        logger.info("buffer: {}", buffer);
        printLine();
    }

    private void printByteBuf(ByteBuf byteBuf) {
        logger.info("isReadable: {}", byteBuf.isReadable());
        logger.info("readableBytes: {}", byteBuf.readableBytes());
        logger.info("readerIndex: {}", byteBuf.readerIndex());
        logger.info("isWritable: {}", byteBuf.isWritable());
        logger.info("writableBytes: {}", byteBuf.writableBytes());
        logger.info("writerIndex: {}", byteBuf.writerIndex());
        logger.info("capacity: {}", byteBuf.capacity());
        logger.info("maxCapacity: {}", byteBuf.maxCapacity());
        logger.info("isDirect: {}", byteBuf.isDirect());
        logger.info("byteBuf.getCharSequence: {}", byteBuf.getCharSequence(0, byteBuf.readableBytes(), CharsetUtil.UTF_8));
        printLine();
    }

    class MyNettyInHandler extends ChannelInboundHandlerAdapter {

        private final Logger logger = LogManager.getLogger(MyNettyInHandler.class);

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            logger.info("***** client registered......");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("****** client active......");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            // CharSequence charSequence = byteBuf.readCharSequence(byteBuf.readableBytes(), CharsetUtil.UTF_8);
            CharSequence charSequence = byteBuf.getCharSequence(0, byteBuf.readableBytes(), CharsetUtil.UTF_8);
            logger.info("收到服务内容：{}", charSequence);

            // 将数据写回给服务端
            ctx.writeAndFlush(byteBuf);
        }
    }
}
