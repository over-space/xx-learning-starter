package com.learning.custom;

import com.learning.BaseTest;
import com.learning.leetcode.Testing;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

/**
 * @author lifang
 * @since 2022/1/5
 */
public class Exception extends BaseTest implements Testing {

    @Test
    public void test() {
        try {

            int i = 1 / 0;

        } catch (java.lang.Exception e) {
            e.printStackTrace();
            System.out.println("-------------------------------------------------------------------------------");
            logger.info(e.getMessage(), e);
            System.out.println("-------------------------------------------------------------------------------");
            String stackTrace = ExceptionUtils.getStackTrace(e);
            System.out.println(stackTrace);
            System.out.println("-------------------------------------------------------------------------------");
        }
    }

    @Test
    public void testLocalDateTime() {
        LocalDateTime current = LocalDateTime.now();
        System.out.println(current.getMonth().getValue());
        System.out.println(current.getMonthValue());
    }
}
