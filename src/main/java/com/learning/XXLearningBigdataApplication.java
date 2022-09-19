package com.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class XXLearningBigdataApplication {

    public static void main(String[] args) {
        SpringApplication.run(XXLearningBigdataApplication.class, args);
    }

}
