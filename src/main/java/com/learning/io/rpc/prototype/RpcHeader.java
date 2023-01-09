package com.learning.io.rpc.prototype;

import java.io.Serializable;
import java.util.UUID;

public class RpcHeader implements Serializable {

    public static final int HEADER_LENGTH = 154;

    public static final String PROTOTYPE_NETTY_HTTP = "netty-http";
    public static final String PROTOTYPE_HTTP = "http";
    public static final String PROTOTYPE_RPC = "rpc";

    public static final int FLAG_CLIENT = 0X141414;
    public static final int FLAG_SERVER = 0X141424;

    public String prototype = PROTOTYPE_RPC;

    private int flag;
    private long requestId;
    private long contentLength;


    public static RpcHeader createHeader(byte[] msgBody) {
        return createHeader(RpcHeader.FLAG_CLIENT, UUID.randomUUID().getLeastSignificantBits(), msgBody);
    }

    public static RpcHeader createHeader(int flag, long requestId, byte[] msgBody) {
        RpcHeader header = new RpcHeader();
        header.setFlag(flag);
        header.setRequestId(requestId);
        header.setContentLength(msgBody.length);
        return header;
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String prototype) {
        this.prototype = prototype;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    @Override
    public String toString() {
        return "RpcHeader{" +
                "flag=" + flag +
                ", requestId=" + requestId +
                ", contentLength=" + contentLength +
                '}';
    }
}

    