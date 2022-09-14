package com.learning.mq.tx.bo;

import java.io.Serializable;

/**
 * @author 李芳
 * @since 2022/9/13
 */
public class MessageBody<T extends Serializable> implements Serializable {

    private String msgId;

    private Integer version;

    private T msg;

    public MessageBody() {
        this.version = 0;
    }

    public MessageBody(String msgId, T msg) {
        this();
        this.msgId = msgId;
        this.msg = msg;
    }

    public MessageBody(String msgId, T msg, int version) {
        this(msgId, msg);
        this.version = version;
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }
}
