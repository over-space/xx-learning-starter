package com.learning.middleware.mq.tx.core;

import com.alibaba.fastjson.JSONObject;
import com.learning.middleware.mq.tx.bo.MessageBody;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author 李芳
 * @since 2022/9/16
 */
public class KafkaMessageBodyDeserializer implements Deserializer<MessageBody> {

    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String propertyName = isKey ? "key.deserializer.encoding" : "value.deserializer.encoding";
        Object encodingValue = configs.get(propertyName);
        if (encodingValue == null)
            encodingValue = configs.get("deserializer.encoding");
        if (encodingValue instanceof String)
            encoding = (String) encodingValue;
    }

    @Override
    public MessageBody deserialize(String topic, byte[] data) {
        try {
            if (data == null)
                return null;
            else {
                String str = new String(data, encoding);
                return JSONObject.parseObject(str, MessageBody.class);
            }
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("Error when deserializing byte[] to string due to unsupported encoding " + encoding);
        }
    }
}
