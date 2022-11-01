package com.learning.middleware.mq.tx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author 李芳
 * @since 2022/9/13
 */
@Entity
@Table(name = "t_msg_record")
public class MsgRecordEntity implements Serializable {

    public enum MsgSendStatus{
        /**
         * 未发送至MQ
         */
        UNSENT(0),

        /**
         * 发送成功
         */
        SEND_OK(1),

        /**
         * 消费成功
         */
        CONSUMED(2),

        /**
         * 消费失败
         */
        CONSUMED_FAILED(3);

        private int value;

        private MsgSendStatus(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 64)
    private String msgId;

    @Column(length = 64)
    private String topic;

    @Column(length = 64)
    private String tags;

    @Column(name = "key_")
    private String key;

    @Column(columnDefinition = "text")
    private String msgBody;

    private Integer retryCount;

    /**
     * 状态
     * @see MsgSendStatus
     */
    private Integer msgStatus;

    private LocalDateTime createdDateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Integer msgStatus) {
        this.msgStatus = msgStatus;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
