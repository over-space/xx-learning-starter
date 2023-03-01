package com.learning.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.io.*;

public final class ByteUtils {

    private static final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public ByteUtils() {

    }

    public static synchronized <T extends Serializable> byte[] toByteArray(T obj) {
        byteArrayOutputStream.reset();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized <T extends Serializable> T toObject(byte[] bytes) {
        byteArrayOutputStream.reset();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (T) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized <T extends Serializable> T toObject(ByteBuf byteBuf) {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return toObject(bytes);
    }

    public static ByteBuf createDirectBuffer(int initialCapacity) {
        return PooledByteBufAllocator.DEFAULT.directBuffer(initialCapacity);
    }

    public static ByteBuf createDirectBuffer(int initialCapacity, byte[]... byteList) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(initialCapacity);
        for (byte[] bytes : byteList) {
            byteBuf.writeBytes(bytes);
        }
        return byteBuf;
    }

    public static ByteBuf copiedBuffer(byte[] bytes) {
        return Unpooled.copiedBuffer(bytes);
    }
}
