package com.learning.sharding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 李芳
 * @since 2022/9/19
 */
@EnableJpaRepositories
@SpringBootApplication(scanBasePackages = "com.learning.sharding")
@Profile("sharding")
public class ShardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingApplication.class, args);
    }

}
