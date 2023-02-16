package com.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class XXLearningStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(XXLearningStarterApplication.class, args);
    }

}
