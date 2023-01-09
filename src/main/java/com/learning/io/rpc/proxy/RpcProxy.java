package com.learning.io.rpc.proxy;

import com.learning.io.rpc.ServiceFactory;
import com.learning.io.rpc.prototype.RpcContent;
import com.learning.io.rpc.prototype.RpcHeader;
import com.learning.io.rpc.prototype.RpcResponse;
import com.learning.utils.ByteUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class RpcProxy {

    private static final Logger logger = LogManager.getLogger(RpcProxy.class);


    public static <T> T proxyGet(Class<T> interfaceInfo, String prototype) {
        ClassLoader classLoader = interfaceInfo.getClassLoader();
        Class<?>[] interfaces = {interfaceInfo};

        return (T) Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args) -> {
            CompletableFuture<RpcResponse> completableFutureResponse = new CompletableFuture<>();
            try {
                // 1. 将参数封装成content
                String name = interfaceInfo.getName();
                String methodName = method.getName();
                Class<?>[] parameterTypes = method.getParameterTypes();
                RpcContent content = new RpcContent(name, methodName, parameterTypes, args);


                final String moduleName = "xxxxxxx";// ServiceFactory.getModuleName();
                if(ServiceFactory.getServiceFactory(moduleName).get(name) != null){
                    // local
                    // return InvokeUtil.invoke(ServiceFactory.getServiceFactory("module_xxxx"), content);
                    return ServiceFactory.getServiceFactory(moduleName).invoke(content);
                }else {
                    // rpc

                    if (RpcHeader.PROTOCOL_RPC.equalsIgnoreCase(prototype)) {
                        // rpc
                        invocationHandlerByRPC(content, completableFutureResponse);
                    } else if (RpcHeader.PROTOCOL_NETTY_HTTP.equalsIgnoreCase(prototype)) {
                        // netty http
                        invocationHandlerByNettyHttp(content, completableFutureResponse);
                    } else {
                        // http
                        invocationHandlerByCustomHttp(content, completableFutureResponse);
                    }
                }

            }catch (Exception e){
                logger.error(e.getMessage(), e);
                completableFutureResponse.complete(new RpcResponse(e));
            }
             return completableFutureResponse.get().getResult();
        });
    }



    private static void invocationHandlerByRPC(RpcContent content, CompletableFuture<RpcResponse> completableFutureResponse) throws InterruptedException {
        byte[] msgBody = ByteUtils.toByteArray(content);

        // 2. 封装header
        RpcHeader header = RpcHeader.createHeader(msgBody);
        byte[] msgHeader = ByteUtils.toByteArray(header);

        logger.info("*** header length : {}", msgHeader.length);

        // 3. 获取客户端连接
        ClientFactory factory = ClientFactory.getFactory();
        NioSocketChannel client = factory.getClient(new InetSocketAddress("127.0.0.1", 9090));

        // 4. 发送
        ByteBuf byteBuf = ByteUtils.createDirectBuffer(msgHeader.length + msgBody.length);
        long requestId = header.getRequestId();

        ProxyResponseHandler.addCallback(requestId, completableFutureResponse);

        byteBuf.writeBytes(msgHeader);
        byteBuf.writeBytes(msgBody);
        ChannelFuture channelFuture = client.writeAndFlush(byteBuf);
        channelFuture.sync();
    }


    private static void invocationHandlerByNettyHttp(RpcContent content, CompletableFuture<RpcResponse> completableFutureResponse) throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Channel client = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 512))
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        // 接收服务端返回信息
                                        FullHttpResponse response = (FullHttpResponse) msg;

                                        RpcContent content = ByteUtils.toObject(response.content());
                                        completableFutureResponse.complete(content.getResponse());
                                    }
                                });

                    }
                }).connect("127.0.0.1", 9090)
                .sync()
                .channel();

        // 发送数据
        byte[] contentBytes = ByteUtils.toByteArray(content);
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.POST, "/", ByteUtils.copiedBuffer(contentBytes));
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH,contentBytes.length);
        client.writeAndFlush(request).sync();
    }

    private static void invocationHandlerByCustomHttp(RpcContent content, CompletableFuture<RpcResponse> completableFutureResponse) throws IOException, ClassNotFoundException {
        URL url = new URL("http://127.0.0.1:9090/");

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        // post
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);

        OutputStream outputStream = httpURLConnection.getOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(outputStream);
        oout.writeObject(content); // 发送数据

        if(httpURLConnection.getResponseCode() == 200){
            InputStream inputStream = httpURLConnection.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            RpcContent  response = (RpcContent)objectInputStream.readObject();

            completableFutureResponse.complete(response.getResponse());
        }else{
            throw new RuntimeException("http response error, " + httpURLConnection.getResponseCode());
        }
    }
}
