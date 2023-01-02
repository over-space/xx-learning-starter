package com.learning.io;

import com.learning.BaseTest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.CharsetUtil;
import org.junit.jupiter.api.Test;

public class NettyTest extends BaseTest {


    @Test
    void testByteBuf01(){

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
    void testByteBuf02(){
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
    void testNioEventLoopGroup(){
        NioEventLoopGroup thread = new NioEventLoopGroup(3);
        thread.execute(() -> {
            logger.info("-----------------------------------");
        });

        thread.execute(() -> {
            logger.info("-----------------------------------");
        });
    }

    private void printByteBuf(ByteBuf byteBuf){
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

        logger.info("------------------------------------------------------------------------------------------------");
    }

}
