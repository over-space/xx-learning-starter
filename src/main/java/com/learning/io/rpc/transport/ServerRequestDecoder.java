package com.learning.io.rpc.transport;

import com.learning.io.rpc.MsgPack;
import com.learning.io.rpc.prototype.RpcContent;
import com.learning.io.rpc.prototype.RpcHeader;
import com.learning.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ServerRequestDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LogManager.getLogger(ServerRequestDecoder.class);

    private String sourceName;

    public ServerRequestDecoder(String sourceName) {
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
            if((buf.readableBytes() - RpcHeader.HEADER_LENGTH) >= header.getContentLength()){

                // 先移动指针，去掉header部门
                logger.info("{}, readBytes before, readerIndex：{}, readableBytes : {}, contentLength : {}", sourceName, buf.readerIndex(), buf.readableBytes(), header.getContentLength());
                buf.readBytes(RpcHeader.HEADER_LENGTH);
                logger.info("{}, readBytes after, readerIndex：{}, readableBytes : {}, contentLength : {}", sourceName, buf.readerIndex(), buf.readableBytes(), header.getContentLength());


                // 读取content
                byte[] contentBytes = new byte[(int)header.getContentLength()];
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
