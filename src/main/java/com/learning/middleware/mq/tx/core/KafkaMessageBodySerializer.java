package com.learning.middleware.mq.tx.core;

import com.alibaba.fastjson.JSONObject;
import com.learning.middleware.mq.tx.bo.MessageBody;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author 李芳
 * @since 2022/9/16
 */
public class KafkaMessageBodySerializer implements Serializer<MessageBody> {

    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.serializer.encoding" : "value.serializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null)
            encodingValue = configs.get("serializer.encoding");
        if (encodingValue instanceof String)
            encoding = (String) encodingValue;
    }

    @Override
    public byte[] serialize(String topic, MessageBody data) {
        try {
            if (data == null)
                return null;
            else
                return JSONObject.toJSONString(data).getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when serializing string to byte[] due to unsupported encoding " + encoding);
        }
    }
}
