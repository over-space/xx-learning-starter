package com.learning.io.rpc.proxy;

import com.learning.io.rpc.MsgPack;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyResponseHandler {
    static ConcurrentHashMap<Long, CompletableFuture<String>> mapping = new ConcurrentHashMap<>();

    public static void addCallback(long requestId, CompletableFuture completableFuture) {
        mapping.putIfAbsent(requestId, completableFuture);
    }

    public static void runCallback(MsgPack msgPack) {
        long requestId = msgPack.getHeader().getRequestId();
        CompletableFuture completableFuture = mapping.get(requestId);
        completableFuture.complete(msgPack.getContent().getResponse());
        remove(requestId);
    }

    private static void remove(long requestId) {
        mapping.remove(requestId);
    }
}
