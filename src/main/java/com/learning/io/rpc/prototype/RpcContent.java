package com.learning.io.rpc.prototype;

import java.io.Serializable;
import java.util.Arrays;

public class RpcContent implements Serializable {

        private String name;
        private String methodName;
        private Class<?>[] parameterTypes;
        private Object[] args;
        private RpcResponse response;

        public RpcContent() {
        }

        public RpcContent(RpcResponse response) {
            this.response = response;
        }

        public RpcContent(String name, String methodName, Class<?>[] parameterTypes, Object[] args) {
            this.name = name;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
            this.args = args;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public Class<?>[] getParameterTypes() {
            return parameterTypes;
        }

        public void setParameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }

    public RpcResponse getResponse() {
        return response;
    }

    @Override
        public String toString() {
            return "RpcContent{" +
                    "name='" + name + '\'' +
                    ", methodName='" + methodName + '\'' +
                    ", parameterTypes=" + Arrays.toString(parameterTypes) +
                    ", args=" + Arrays.toString(args) +
                    ", response=" + response +
                    '}';
        }
}