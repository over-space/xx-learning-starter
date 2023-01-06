package com.learning.io.rpc;

import com.learning.BaseTest;
import com.learning.utils.ByteUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcSampleTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(RpcSampleTest.class);

    @Test
    void run(){
        new Thread(() -> {
            try {
                testRpcServer();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        sleep(1);
        logger.info("rpc server started.....");

        testRpcClient();

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void testRpcClient() {

        try {

            // Fly fly = proxyGet(Fly.class);
            // fly.start("hello world!");

            AtomicInteger atomicInteger = new AtomicInteger(1);
            for (int i = 0; i < 20; i++) {
                CompletableFuture.runAsync(() -> {
                    Fly fly = proxyGet(Fly.class);
                    String threadName = fly.getThreadName("hello world, " + atomicInteger.incrementAndGet());
                    logger.info("Fly#getThreadName : {}", threadName);

                    Calc calc = proxyGet(Calc.class);
                    int num1 = calc.add(2, 3);
                    Assertions.assertEquals(num1, 5);

                    int num2 = calc.subtract(5, 2);
                    Assertions.assertEquals(num2, 3);

                }).exceptionally(e -> {
                    if(e instanceof Exception){
                        logger.error(e.getMessage(), e);
                    }
                    return null;
                });
            }

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }

    }

    void testRpcServer() throws InterruptedException {

        // 模拟一个简单的服务注册中心
        RegisterCenter registerCenter = initServerRegisterCenter();


        NioEventLoopGroup group = new NioEventLoopGroup(20);

        ServerBootstrap bootstrap = new ServerBootstrap();

        Channel server = bootstrap.group(group, group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel client) throws Exception {
                        logger.info("server accept client : {}", client.remoteAddress());
                        ChannelPipeline pipeline = client.pipeline();
                        pipeline.addLast(new ServerRequestDecoderHandler("server decoder"));// 先解码，再传给后续handler.
                        pipeline.addLast(new ServerRequestHandler(registerCenter));
                    }
                }).bind(9090)
                .sync()
                .channel();

        server.closeFuture().sync();
    }

    RegisterCenter initServerRegisterCenter(){
        Fly fly = new FlyImpl();
        Calc calc = new CalcImpl();

        RegisterCenter registerCenter = new RegisterCenter();
        registerCenter.register(Fly.class.getName(), fly);
        registerCenter.register(Calc.class.getName(), calc);
        return registerCenter;
    }

    private <T> T proxyGet(Class<T> interfaceInfo) {

        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] interfaces = {interfaceInfo};

        return (T) Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args) -> {

            try {
                // 1. 将参数封装成content
                String name = interfaceInfo.getName();
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                RpcContent content = new RpcContent(name, methodName, parameterTypes, args);
                byte[] msgBody = ByteUtils.toByteArray(content);

                // 2. 封装header
                RpcHeader header = createHeader(msgBody);
                byte[] msgHeader = ByteUtils.toByteArray(header);

                logger.info("*** header length : {}", msgHeader.length);

                // 3. 获取客户端连接
                ClientFactory factory = ClientFactory.getFactory();
                NioSocketChannel client = factory.getClient(new InetSocketAddress("127.0.0.1", 9090));

                // 4. 发送
                ByteBuf byteBuf = ByteUtils.createDirectBuffer(msgHeader.length + msgBody.length);
                long requestId = header.getRequestId();

                CompletableFuture<String> completableFuture = new CompletableFuture();
                ProxyResponseHandler.addCallback(requestId, completableFuture);

                byteBuf.writeBytes(msgHeader);
                byteBuf.writeBytes(msgBody);
                ChannelFuture channelFuture = client.writeAndFlush(byteBuf);
                channelFuture.sync();

                return completableFuture.get();
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
            return null;
        });
    }

    private RpcHeader createHeader(byte[] msgBody) {
        return createHeader(RpcHeader.FLAG_CLIENT, UUID.randomUUID().getLeastSignificantBits(), msgBody);
    }

    private RpcHeader createHeader(int flag, long requestId, byte[] msgBody) {
        RpcHeader header = new RpcHeader();
        header.setFlag(flag);
        header.setRequestId(requestId);
        header.setDataLen(msgBody.length);
        return header;
    }

    static class ProxyResponseHandler {
        static ConcurrentHashMap<Long,CompletableFuture<String>> mapping = new ConcurrentHashMap<>();

        public static void  addCallback(long requestId,CompletableFuture completableFuture){
            mapping.putIfAbsent(requestId,completableFuture);
        }
        public static void runCallback(MsgPack msgPack){
            long requestId = msgPack.getHeader().getRequestId();
            CompletableFuture completableFuture = mapping.get(requestId);
            completableFuture.complete(msgPack.getContent().getResult());
            remove(requestId);
        }

        private static void remove(long requestId) {
            mapping.remove(requestId);
        }
    }


    private static final class ClientFactory {

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
                clientList.putIfAbsent(address,new ClientPool(poolSize));
                clientPool = clientList.get(address);
            }

            int rand = ThreadLocalRandom.current().nextInt(poolSize);

            if( clientPool.getClientList()[rand] != null && clientPool.getClientList()[rand].isActive()){
                return clientPool.getClientList()[rand];
            }

            synchronized (clientPool.getLocks()[rand]){
                return clientPool.getClientList()[rand] = createClient(address);
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
                            p.addLast(new ServerRequestDecoderHandler("client decoder"));
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


   static class ServerRequestDecoderHandler extends ByteToMessageDecoder{

        private String sourceName;

       public ServerRequestDecoderHandler(String sourceName) {
           this.sourceName = sourceName;
       }

       /**
         * 客户端高并发情况下，服务端一次会收到多个byteBuf, byteBuf可能并不完整，所以需要将经过解码。
         */
        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {


            while (buf.readableBytes() >= RpcHeader.HEADER_LENGTH){
                logger.info("{}, byteBuf readableBytes : {}", sourceName, buf.readableBytes());

                // 1. 读取header数据
                byte[] headerBytes = new byte[RpcHeader.HEADER_LENGTH];
                // buf.readBytes(bytes);
                buf.getBytes(buf.readerIndex(), headerBytes); // get不移动指针，readerIndex保持不变。
                RpcHeader header = ByteUtils.toObject(headerBytes);

                logger.info("{}, rpc handler : {}", sourceName, header);

                // 2. 读取content数据
                if(buf.readableBytes() >= (header.getDataLen() + RpcHeader.HEADER_LENGTH)){

                    // 先移动指针，去掉header部门
                    logger.info("{}, readBytes before, readerIndex：{}, readableBytes : {}, dataLen : {}", sourceName, buf.readerIndex(), buf.readableBytes(), header.getDataLen());
                    buf.readBytes(RpcHeader.HEADER_LENGTH);
                    logger.info("{}, readBytes after, readerIndex：{}, readableBytes : {}, dataLen : {}", sourceName, buf.readerIndex(), buf.readableBytes(), header.getDataLen());


                    // 读取content
                    byte[] contentBytes = new byte[(int)header.getDataLen()];
                    buf.readBytes(contentBytes);

                    RpcContent content = ByteUtils.toObject(contentBytes);
                    logger.info("{}, rpc interface content: {}", sourceName, content);

                    list.add(new MsgPack(header, content));
                }else{
                    // buf数据不完整，在下一个buf中。
                    break;
                }
            }

        }
    }

    class ServerRequestHandler extends ChannelInboundHandlerAdapter{

        private RegisterCenter registerCenter;

        public ServerRequestHandler(RegisterCenter registerCenter){
            this.registerCenter = registerCenter;
        }

        //provider:
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            // ByteBuf buf = (ByteBuf) msg;
            // ByteBuf sendBuf = buf.copy();
            //
            // logger.info("server channel read, readable bytes : {}", buf.readableBytes());
            //
            // while (buf.readableBytes() >= 246){
            //
            //     // 1. 读取header数据
            //     byte[] bytes = new byte[246];
            //     // buf.readBytes(bytes);
            //     buf.getBytes(buf.readableBytes(), bytes); // get不移动指针，readableBytes保持不变。
            //     RpcHeader header = ByteUtils.toObject(bytes);
            //
            //     logger.info("server channel read, rpc header : {}", header);
            //
            //     // 2. 读取content数据
            //     if(buf.readableBytes() >= header.getDataLen()){
            //         // 先移动指针，去掉header部门
            //         buf.readBytes(246);
            //
            //         // 读取content
            //         byte[] data = new byte[(int)header.getDataLen()];
            //         buf.readBytes(data);
            //         RpcContent content = ByteUtils.toObject(data);
            //         logger.info("server channel read, rpc interface content: {}", content);
            //     }
            // }
            //
            //
            // ChannelFuture channelFuture = ctx.writeAndFlush(sendBuf);
            // channelFuture.sync();

            // 1. 当前主线程处理
            // 2. 自己开线程处理
            // 3. Netty线程处理
            // String ioThreadName = Thread.currentThread().getName();
            // ctx.executor().execute(() -> { // 当前IO线程
            ctx.executor().parent().next().execute(() -> {
                MsgPack msgPack = (MsgPack) msg;
                RpcHeader header = msgPack.getHeader();
                RpcContent content = msgPack.getContent();

                // String threadName = String.format("io-thread:%s, busines-thread: %s", ioThreadName, Thread.currentThread().getName());
                // RpcContent requestContent = new RpcContent(threadName);

                Object result = invoke(content, registerCenter);
                RpcContent requestContent = new RpcContent(result);
                byte[] requestContentBytes = ByteUtils.toByteArray(requestContent);

                RpcHeader requestHeader = createHeader(RpcHeader.FLAG_SERVER,header.getRequestId(), requestContentBytes);
                byte[] requestHeaderBytes = ByteUtils.toByteArray(requestHeader);

                logger.info("server channel read, interfaceName:{}, methodName:{}, args:{}, requestHeaderBytes: {}, requestContentBytes: {}",
                        content.getName(),content.getMethodName(), content.getArgs(), requestHeaderBytes.length, requestContentBytes.length);


                ByteBuf byteBuf = ByteUtils.createDirectBuffer(requestHeaderBytes.length + requestContentBytes.length);
                byteBuf.writeBytes(requestHeaderBytes);
                byteBuf.writeBytes(requestContentBytes);

                ctx.writeAndFlush(byteBuf);
            });
        }
    }

    private Object invoke(RpcContent content, RegisterCenter registerCenter){
        try {
            Object interfaceImpl = registerCenter.get(content.getName());
            Class<?> clazz = interfaceImpl.getClass();
            Method method = clazz.getMethod(content.getMethodName(), content.getParameterTypes());
            return method.invoke(interfaceImpl, content.getArgs());
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    static class ClientResponse extends ChannelInboundHandlerAdapter{

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // ByteBuf buf = (ByteBuf) msg;
            // if(buf.readableBytes() >= 246){
            //     byte[] bytes = new byte[246];
            //     buf.readBytes(bytes);
            //     RpcHeader header = ByteUtils.toObject(bytes);
            //     logger.info("client read, rpc header: {}", header);
            //
            //     ResponseHandler.runCallback(header.getRequestId());
            // }
            // super.channelRead(ctx, msg);

            MsgPack msgPack = ((MsgPack) msg);
            ProxyResponseHandler.runCallback(msgPack);
        }
    }

    static class MsgPack implements Serializable{

        private RpcHeader header;
        private RpcContent content;

        public MsgPack(RpcHeader header, RpcContent content) {
            this.header = header;
            this.content = content;
        }

        public RpcHeader getHeader() {
            return header;
        }

        public RpcContent getContent() {
            return content;
        }
    }

    static class ClientPool {
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

    class RpcHeader implements Serializable {

        private static final int HEADER_LENGTH = 246;

        private static final int FLAG_CLIENT = 0X141414;
        private static final int FLAG_SERVER = 0X141424;

        private int flag;
        private long requestId;
        private long dataLen;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public long getRequestId() {
            return requestId;
        }

        public void setRequestId(long requestId) {
            this.requestId = requestId;
        }

        public long getDataLen() {
            return dataLen;
        }

        public void setDataLen(long dataLen) {
            this.dataLen = dataLen;
        }

        @Override
        public String toString() {
            return "RpcHeader{" +
                    "flag=" + flag +
                    ", requestId=" + requestId +
                    ", dataLen=" + dataLen +
                    '}';
        }
    }

    class RpcContent implements Serializable {

        private String name;
        private String methodName;
        private Class<?>[] parameterTypes;
        private Object[] args;
        private Object result;

        public RpcContent() {
        }

        public RpcContent(Object result) {
            this.result = result;
        }

        public RpcContent(String name, String methodName, Class<?>[] parameterTypes, Object[] args) {
            this.name = name;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
            this.args = args;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        public void setParameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }

        public Object getResult() {
            return result;
        }

        @Override
        public String toString() {
            return "RpcContent{" +
                    "name='" + name + '\'' +
                    ", methodName='" + methodName + '\'' +
                    ", parameterTypes=" + Arrays.toString(parameterTypes) +
                    ", args=" + Arrays.toString(args) +
                    ", result=" + result +
                    '}';
        }
    }

    static class RegisterCenter{

        private static ConcurrentHashMap<String, Object> registerServerMap = new ConcurrentHashMap<>();


        public final static void register(String interfaceName, Object server){
            registerServerMap.putIfAbsent(interfaceName, server);
        }

        public final static Object get(String interfaceName){
            return registerServerMap.get(interfaceName);
        }
    }

    interface Fly {
        String getThreadName(String msg);
    }

    interface Calc{

        int add(int a, int b);

        int subtract(int a, int b);

    }

    class CalcImpl implements Calc {

        @Override
        public int add(int a, int b) {
            return a + b;
        }

        @Override
        public int subtract(int a, int b) {
            return a - b;
        }
    }



    class FlyImpl implements Fly{

        private final Logger logger = LogManager.getLogger(FlyImpl.class);


        @Override
        public String getThreadName(String msg) {
            logger.info("FlyImpl#getThreadName, msg:{}", msg);
            return msg;
        }
    }
}
