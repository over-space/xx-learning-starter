package com.learning.io;

import com.learning.BaseTest;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class NettyTest extends BaseTest {

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

        // 等待服务端关闭连接
        connect.channel().closeFuture().sync();
    }

    @Test
    void testNettyServer() throws InterruptedException {
        NioEventLoopGroup thread = new NioEventLoopGroup(1);

        NioServerSocketChannel server = new NioServerSocketChannel();

        thread.register(server);

        ChannelPipeline pipeline = server.pipeline();
        // pipeline.addLast(new MyNettyAcceptHandler(thread, new NettyInitHandler()));
        // pipeline.addLast(new MyNettyAcceptHandler(thread, new ChannelInitialize() {
        //     @Override
        //     public void initializeChannel(ChannelHandlerContext ctx) {
        //         Channel client = ctx.channel();
        //         client.pipeline().addLast(new MyNettyInHandler());
        //     }
        // }));
        pipeline.addLast(new MyNettyAcceptHandler(thread, new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel client) throws Exception {
                client.pipeline().addLast(new MyNettyInHandler());
            }
        }));


        ChannelFuture channelFuture = server.bind(new InetSocketAddress("127.0.0.1", 9090));

        // 等待
        channelFuture.sync().channel().closeFuture().sync();
    }

    @Test
    void testNettyClientPro() throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup(1);

        Bootstrap bootstrap = new Bootstrap();
        Channel client = bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel client) throws Exception {
                    client.pipeline().addLast(new MyNettyInHandler());
                }
            }).connect(new InetSocketAddress("127.0.0.1", 9090))
            .sync()
            .channel();

        ByteBuf byteBuf = Unpooled.copiedBuffer("hello netty server......\n".getBytes());
        ChannelFuture channelFuture = client.writeAndFlush(byteBuf);
        channelFuture.sync();// 等待数据write成功

        client.closeFuture().sync();
    }

    @Test
    void testNettyServerPro() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);

        ServerBootstrap bootstrap = new ServerBootstrap();

        Channel server = bootstrap.group(group, group)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel client) throws Exception {
                    client.pipeline().addLast(new MyNettyInHandler());
                }
            }).bind(9090)
            .sync()
            .channel();

        server.closeFuture().sync();
    }

    @Test
    void testNettyClientBootstrap() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);

        Bootstrap bootstrap = new Bootstrap();
        Channel client = bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel client) throws Exception {
                    // 设置hander,接收server信息
                    logger.info("netty client : 接收服务端消息.....");
                    client.pipeline().addLast(new ClientResponse());
                }
            }).connect(new InetSocketAddress("127.0.0.1", 9090))
            .sync()
            .channel();

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(32, 512);
        byteBuf.writeBytes("hello netty server".getBytes());
        client.writeAndFlush(byteBuf);

        client.closeFuture().sync();
    }

    @Test
    void testNettyServerBootstrap() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        ServerBootstrap bootstrap = new ServerBootstrap();
        Channel server = bootstrap.group(group, group)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel client) throws Exception {
                    logger.info("netty server : 接收客户端消息.....");
                    client.pipeline().addLast(new ServerResponse());
                }
            }).bind(9090)
            .sync()
            .channel();
        server.closeFuture().sync();
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

    class MyNettyAcceptHandler extends ChannelInboundHandlerAdapter {

        private final Logger logger = LogManager.getLogger(MyNettyAcceptHandler.class);

        private final EventLoopGroup thread;
        private final ChannelHandler handler;

        public MyNettyAcceptHandler(EventLoopGroup thread, ChannelHandler handler) {
            this.thread = thread;
            this.handler = handler;
        }


        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            logger.info("***** server registered......");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("****** server active......");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // netty accept之后，获取client
            logger.info("msg objType: {}", msg);
            SocketChannel client = (SocketChannel) msg;

            logger.info("client : {}", client.remoteAddress());

            // 添加handler,接收client接收server端数据
            ChannelPipeline pipeline = client.pipeline();
            pipeline.addLast(handler);

            // 注册到selector
            thread.register(client);
        }
    }

    @ChannelHandler.Sharable
    class NettyInitHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            logger.info("***** client registered......");
            Channel client = ctx.channel();
            ChannelPipeline pipeline = client.pipeline();
            pipeline.addLast(new MyNettyInHandler());
            ctx.pipeline().remove(this);
        }
    }

    @ChannelHandler.Sharable
    abstract class ChannelInitialize extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            initializeChannel(ctx);
        }

        public abstract void initializeChannel(ChannelHandlerContext ctx);
    }

    // @ChannelHandler.Sharable 业务代码，如果添加@Sharable注解，业务上需要设计为单例的，不能存在共享资源。业务实现不可控。
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

    class ClientResponse extends ChannelInboundHandlerAdapter {

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

    class ServerResponse extends ChannelInboundHandlerAdapter {

        private final Logger logger = LogManager.getLogger(MyNettyInHandler.class);

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            logger.info("***** server registered......");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("****** server active......");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            // CharSequence charSequence = byteBuf.readCharSequence(byteBuf.readableBytes(), CharsetUtil.UTF_8);
            CharSequence charSequence = byteBuf.getCharSequence(0, byteBuf.readableBytes(), CharsetUtil.UTF_8);
            logger.info("收到服务内容：{}", charSequence);
            logger.info("channel : {}", ctx.channel().getClass());

            // 将内容写回给客户端
            // ctx.channel().writeAndFlush(byteBuf);
        }
    }
}
