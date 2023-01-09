package com.learning.io.rpc.prototype;

import java.io.Serializable;

public class RpcResponse implements Serializable {

    private Throwable exception;

    private Object result;

    public RpcResponse(Throwable exception) {
        this.exception = exception;
    }

    public RpcResponse(Object result) {
        this.result = result;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "result=" + result +
                ", exception=" + exception +
                '}';
    }
}
