package com.learning.io.rpc;

import com.learning.io.rpc.service.Calc;
import com.learning.io.rpc.service.CalcImpl;
import com.learning.io.rpc.service.Fly;
import com.learning.io.rpc.service.FlyImpl;
import com.learning.io.rpc.transport.HttpServletRpcHandler;
import com.learning.io.rpc.transport.ServerRequestDecoder;
import com.learning.io.rpc.transport.ServerRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.net.InetSocketAddress;

public final class RpcServerStarter {
    private static final Logger logger = LogManager.getLogger(RpcServerStarter.class);


    public static void asyncStartHttpServer(String hostname, int port) {
        try {
            new Thread(() -> {
                startHttpServer(hostname, port);
            }).start();
            logger.info("********************************************************************************************");
            logger.info("*** start rpc by http server....");
            logger.info("********************************************************************************************");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    public static void startHttpServer(String hostname, int port) {
        initServerRegisterCenter();

        Server server = new Server(new InetSocketAddress(hostname, port));
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        server.setHandler(handler);
        handler.addServlet(HttpServletRpcHandler.class, "/*");

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void startWebsocketServer() throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(5);
        NioEventLoopGroup works = new NioEventLoopGroup(10);
        ServerBootstrap bootstrap = new ServerBootstrap();
        Channel channel = bootstrap.group(boss, works)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(1024 * 512))
                                .addLast(new WebSocketServerProtocolHandler("/websocket"));
                    }
                }).bind(9090)
                .sync()
                .channel();


    }

    public static void asyncStartNettyServer(){
        try {
            new Thread(() -> {
                startNettyServer();
            }).start();
            logger.info("********************************************************************************************");
            logger.info("*** start rpc by netty server....");
            logger.info("********************************************************************************************");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    public static void startNettyServer(){
        try {
            initServerRegisterCenter();

            NioEventLoopGroup group = new NioEventLoopGroup(20);
            ServerBootstrap bootstrap = new ServerBootstrap();

            Channel server = bootstrap.group(group, group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                            logger.info("server accept client : {}", client.remoteAddress());
                            ChannelPipeline pipeline = client.pipeline();
                            pipeline.addLast(new ServerRequestDecoder("server decoder"));// 先解码，再传给后续handler.
                            pipeline.addLast(new ServerRequestHandler());
                        }
                    }).bind(9090)
                    .sync()
                    .channel();

            server.closeFuture().sync();
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

    private static ServiceFactory initServerRegisterCenter() {
        Fly fly = new FlyImpl();
        Calc calc = new CalcImpl();

        ServiceFactory registerCenter = ServiceFactory.getServiceFactory();
        registerCenter.register(Fly.class.getName(), fly);
        registerCenter.register(Calc.class.getName(), calc);
        return registerCenter;
    }
}
