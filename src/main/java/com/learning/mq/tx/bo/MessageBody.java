package com.learning.mq.tx.bo;

import java.io.Serializable;

/**
 * @author 李芳
 * @since 2022/9/13
 */
public class MessageBody<T extends Serializable> implements Serializable {

    private String msgId;

    private Boolean isTransaction;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean isTransaction() {
        return isTransaction;
    }

    public void setTransaction(Boolean transaction) {
        isTransaction = transaction;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "MessageBody{" +
                "msgId='" + msgId + '\'' +
                ", version=" + version +
                ", msg=" + msg +
                '}';
    }
}
