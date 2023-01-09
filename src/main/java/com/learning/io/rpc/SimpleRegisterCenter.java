package com.learning.io.rpc;

import java.util.concurrent.ConcurrentHashMap;

public final class SimpleRegisterCenter {

    public static final String MODULE_SERVER_A = "server-a";
    public static final String MODULE_SERVER_B = "server-b";

    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> registerServerMap = new ConcurrentHashMap<>();

    private static SimpleRegisterCenter registerCenter = new SimpleRegisterCenter();

    public static SimpleRegisterCenter getRegisterCenter() {
        return registerCenter;
    }

    public final static synchronized void register(String module, String interfaceName, Object server){
        ConcurrentHashMap<String, Object> serverMap = registerServerMap.get(module);
        if(serverMap == null){
            registerServerMap.put(module, new ConcurrentHashMap<>());
        }
        serverMap = registerServerMap.get(module);
        serverMap.putIfAbsent(interfaceName, server);
    }

    public final static Object get(String module, String interfaceName){
        return registerServerMap.getOrDefault(module, new ConcurrentHashMap<>()).get(interfaceName);
    }
}
