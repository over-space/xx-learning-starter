package com.learning.mq.tx.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author 李芳
 * @since 2022/9/14
 */
public class RocketMqCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment() != null
                && context.getEnvironment().getProperty("spring.profiles.include") != null
                && context.getEnvironment().getProperty("spring.profiles.include").contains("rocketmq");
    }
}
