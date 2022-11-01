package com.learning.basic;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 李芳
 * @since 2022/10/21
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication(exclude = {
        ShardingSphereAutoConfiguration.class,
        KafkaAutoConfiguration.class
})
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }
}
