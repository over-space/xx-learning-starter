package com.learning.leetcode;

import com.learning.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * @author 李芳
 * @since 2022/10/14
 */
public class HexTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(HexTest.class);


    @Test
    public void test(){
        String result1 = fun(7, 5);
        logger.info("result: {}", result1);

        String result2 = fun(15, 5);
        logger.info("result: {}", result2);

        String result3 = fun(15, 2);
        logger.info("result: {}", result3);
    }

    private String fun(int num, int hex){
        String result = "";

        int mod;
        int value = num;

        while (value > 0){
            mod = value % hex;
            value = value / hex;

            result = mod + result;
        }
        return result;
    }
}
