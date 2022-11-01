package com.learning.middleware.mq.tx.core.aop;

import com.learning.middleware.mq.tx.producer.MessageProducer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 李芳
 * @since 2022/9/19
 */
@Aspect
@Component
@ConditionalOnProperty(name = "kafka.transaction.message.mode", havingValue = "AOP")
public class TransactionMessageAdvice {

    @Resource
    private MessageProducer messageProducer;

    @Pointcut(value = "@annotation(com.learning.middleware.mq.tx.core.aop.annontion.TransactionalMessage)")
    public void pointcutAround(){}

    @Around(value = "pointcutAround()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            return point.proceed();
        } catch (Exception e) {
            throw e;
        }finally {
            messageProducer.sendMsgFromThreadLocal();
        }
    }
}
