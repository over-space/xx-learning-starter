package com.learning;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * @author 李芳
 * @since 2022/9/21
 */
public abstract class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);


    @BeforeAll
    public static void beforeAll() {
        logger.info("================================================================================================");
        logger.info("-------------------------------------开始执行测试方法---------------------------------------------");
        logger.info("");
    }

    @AfterAll
    public static void afterAll() {
        logger.info("");
        logger.info("-------------------------------------测试方法执行完成---------------------------------------------");
        logger.info("================================================================================================");
    }

}
