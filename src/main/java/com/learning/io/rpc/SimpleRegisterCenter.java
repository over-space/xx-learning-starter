package com.learning.io.rpc;

import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.ConcurrentHashMap;

public final class SimpleRegisterCenter {

    public static final String MODULE_SERVER_A = "server-a";
    public static final String MODULE_SERVER_B = "server-b";

    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> registerServerMap = new ConcurrentHashMap<>();
    private static SimpleRegisterCenter registerCenter = new SimpleRegisterCenter();

    private static ConcurrentHashMap<String, Object> serverMap;

    public static synchronized SimpleRegisterCenter getRegisterCenter(String module) {
        serverMap = registerServerMap.get(module);
        if(serverMap == null){
            registerServerMap.putIfAbsent(module, new ConcurrentHashMap<>());
            serverMap = registerServerMap.get(module);;
        }
        return registerCenter;
    }

    public final static void register(String interfaceName, Object server){
        serverMap.putIfAbsent(interfaceName, server);
    }

    public final static Object get(String interfaceName){
        return serverMap.get(interfaceName);
    }
}
