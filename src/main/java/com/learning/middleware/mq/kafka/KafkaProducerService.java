package com.learning.middleware.mq.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KafkaProducerService {

    private static final Logger logger = LogManager.getLogger(KafkaProducerService.class);

    public static void main(String[] args) {
        new KafkaProducerService().producer();
    }


    public void producer(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.200.211:9092,192.168.200.212:9092,192.168.200.213:9092");
        configs.put(ProducerConfig.ACKS_CONFIG, "all");

        KafkaProducer producer = new KafkaProducer(configs, new StringSerializer(), new StringSerializer());

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> {
            int i = 0;
            while (true) {
                if(i % 2 == 0) {
                    producer.send(new ProducerRecord("msn-topic-1", "order1","hello kafka-" + i));
                    try {
                        TimeUnit.MILLISECONDS.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(i > 1000){
                    break;
                }
                i++;
            }
        });

        executorService.execute(() -> {
            int i = 0;
            while (true) {
                if (i % 2 != 0) {
                    producer.send(new ProducerRecord("msn-topic-2", "order2", "hello kafka-" + i));
                    try {
                        TimeUnit.MILLISECONDS.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(i > 1000){
                    break;
                }
                i++;
            }
        });

        while(true){
            try {
                TimeUnit.MILLISECONDS.sleep(600);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}