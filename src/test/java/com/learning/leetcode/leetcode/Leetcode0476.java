package com.learning.leetcode;

import com.learning.BaseTest;

/**
 * @author lifang
 * @since 2021/10/18
 */
public class Leetcode0476 extends BaseTest implements Testing {

    @Override
    public void test() {
        System.out.println(toBinary(5));
        System.out.println(toBinary(10));
        System.out.println(toBinary(2));
        System.out.println(toBinary(-2));
    }

    /**
     * 将整型转换成二进制
     *
     * @param num
     * @return
     */
    private String toBinary(int num) {

        boolean negative = num < 0;
        String s = "";

        if (negative) {
            num = -num;
        }
        int len = 0;
        while (num > 0) {
            if (num % 2 != 0) {
                s = "1" + s;
            } else {
                s = "0" + s;
            }
            num /= 2;
            len++;
        }

        for (int i = 0; i < 8 - len; i++) {
            if (negative) {
                s = "1" + s;
            } else {
                s = "0" + s;
            }
        }
        return s;
    }
}
