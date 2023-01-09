package com.learning.io.rpc.transport;

import com.learning.io.rpc.MsgPack;
import com.learning.io.rpc.proxy.ProxyResponseHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientResponse extends ChannelInboundHandlerAdapter {

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
