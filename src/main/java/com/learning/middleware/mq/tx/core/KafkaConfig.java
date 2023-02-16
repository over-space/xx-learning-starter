package com.learning.middleware.mq.tx.core;

import com.learning.middleware.mq.tx.bo.MessageBody;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.ProducerListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李芳
 * @since 2022/9/16
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    public String bootstrapServer;

    @Value("${spring.kafka.producer.retries:3}")
    private Integer retries;

    @Value("${spring.kafka.producer.acks:1}")
    private String acks;

    @Value("${spring.kafka.producer.batch-size:16384}")
    private Integer batchSize;

    @Value("${spring.kafka.producer.max-block-mse:4096}")
    private Integer maxBlock;

    @Value("${spring.kafka.producer.client-id}")
    private String clientId;

    @Value("${spring.kafka.consumer.enable-auto-commit:false}")
    private Boolean enableAutoCommit;
    @Value("${spring.kafka.consumer.auto-offset-reset:latest}")
    private String enableOffsetReset;


    // @Bean
    public ProducerFactory<String, MessageBody> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        //kafka 集群地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        //重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        //应答级别
        //acks=0 把消息发送到kafka就认为发送成功
        //acks=1 把消息发送到kafka leader分区，并且写入磁盘就认为发送成功
        //acks=all 把消息发送到kafka leader分区，并且leader分区的副本follower对消息进行了同步就任务发送成功
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        //KafkaProducer.send() 和 partitionsFor() 方法的最长阻塞时间 单位 ms
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, maxBlock);
        //批量处理的最大大小 单位 byte
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, maxBlock);
        //发送延时,当生产端积累的消息达到batch-size或接收到消息linger.ms后,生产者就会将消息提交给kafka
        props.put(ProducerConfig.LINGER_MS_CONFIG, batchSize);
        //生产者可用缓冲区的最大值 单位 byte
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        //每条消息最大的大小
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 1048576);
        //客户端ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        //Key 序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaMessageBodySerializer.class.getName());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaMessageBodyDeserializer.class.getName());
        //Value 序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaMessageBodySerializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaMessageBodyDeserializer.class.getName());

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, enableOffsetReset);

        //消息压缩：none、lz4、gzip、snappy，默认为 none。
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip");
        //自定义分区器
        // props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());
        return new DefaultKafkaProducerFactory<>(props);
    }

    // @Bean
    // public KafkaListenerContainerFactory kafkaListenerContainerFactory(){
    //     ConcurrentKafkaListenerContainerFactory<String,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    //
    //     factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
    //     //并发数量
    //     factory.setConcurrency(concurrency);
    //     //开启批量监听
    //     factory.setBatchListener(type);
    //     // 被过滤的消息将被丢弃
    //     factory.setAckDiscarded(true);
    //     // 设置记录筛选策略
    //     factory.setRecordFilterStrategy(new RecordFilterStrategy() {
    //         @Override
    //         public boolean filter(ConsumerRecord consumerRecord) {
    //             String msg = consumerRecord.value().toString();
    //             if(Integer.parseInt(msg.substring(msg.length() - 1)) % 2 == 0){
    //                 return false;
    //             }
    //             // 返回true消息将会被丢弃
    //             return true;
    //         }
    //     });
    //     // ack模式
    //     factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
    //     // 禁止消费者监听器自启动
    //     factory.setAutoStartup(false);
    //     return factory;
    // }

    // @Bean
    // public KafkaTemplate<String, MessageBody> kafkaTemplate(ProducerFactory<String, MessageBody> producerFactory,
    //                                                    ProducerListener<String, MessageBody> producerListener) {
    //     KafkaTemplate<String, MessageBody> kafkaTemplate = new KafkaTemplate<>(producerFactory);
    //     kafkaTemplate.setProducerListener(producerListener);// 设置生产者监听器
    //     return kafkaTemplate;
    // }

}
