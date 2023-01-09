package com.learning.io.rpc;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.learning.io.rpc.prototype.RpcContent;

import java.util.concurrent.ConcurrentHashMap;

public final class ServiceFactory {

    private static ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> registerServerMap = new ConcurrentHashMap<>();
    private static ServiceFactory registerCenter = new ServiceFactory();

    private static ConcurrentHashMap<String, Object> serverMap;

    public static synchronized ServiceFactory getServiceFactory() {
        return getServiceFactory(getModuleName());
    }

    public static synchronized ServiceFactory getServiceFactory(String moduleName) {
        serverMap = registerServerMap.get(moduleName);
        if(serverMap == null){
            registerServerMap.putIfAbsent(moduleName, new ConcurrentHashMap<>());
            serverMap = registerServerMap.get(moduleName);;
        }
        return registerCenter;
    }

    public static void register(String interfaceName, Object server){
        serverMap.putIfAbsent(interfaceName, server);
    }

    public static Object get(String interfaceName){
        return serverMap.get(interfaceName);
    }

    public static String getModuleName(){
        // 动态获取项目
        return "xx-learning-starter";
    }

    public static Object invoke(RpcContent content) {
        return invoke(content.getName(), content.getMethodName(), content.getParameterTypes(), content.getArgs());
    }

    public static Object invoke(String interfaceName, String methodName, Class<?>[] parameterTypes, Object[] args){
        Object server = getServiceFactory().get(interfaceName);
        MethodAccess methodAccess = MethodAccess.get(server.getClass());
        return methodAccess.invoke(server, methodName, parameterTypes, args);
    }
}
