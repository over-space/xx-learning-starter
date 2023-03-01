package com.learning.basic.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author 李芳
 * @since 2022/10/20
 */
public class TestAdvice {

    private static final Logger logger = LogManager.getLogger(TestAdvice.class);


    @Pointcut(value = "@annotation(com.learning.basic.aop.Advice)")
    public void pointcutAround() {
    }

    @Around(value = "pointcutAround()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            logger.info("-------------------------------");
            return point.proceed();
        } catch (Exception e) {
            throw e;
        }
    }

}
