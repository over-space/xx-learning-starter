package com.learning.io.rpc;

import com.learning.io.rpc.prototype.RpcContent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvokeUtil {
    private static final Logger logger = LogManager.getLogger(InvokeUtil.class);

    public static Object invoke(SimpleRegisterCenter registerCenter, RpcContent content) {
        Object interfaceImpl = registerCenter.get(SimpleRegisterCenter.MODULE_SERVER_A, content.getName());
        return invoke(interfaceImpl, content.getMethodName(), content.getParameterTypes(), content.getArgs());
    }

    public static Object invoke(Object interfaceImpl, String method, Class<?>[] parameterTypes, Object[] args) {
        try {
            Class<?> clazz = interfaceImpl.getClass();
            Method m = clazz.getMethod(method, parameterTypes);
            return m.invoke(interfaceImpl, args);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
