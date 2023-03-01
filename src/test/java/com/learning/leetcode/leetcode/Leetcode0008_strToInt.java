package com.learning.leetcode;

import com.learning.BaseTest;
import org.junit.Test;

public class Leetcode0008_strToInt extends BaseTest {

    @Test
    public void run() {
        strToInt("-2147483649");
    }

    public int strToInt(String s) {
        int len = s.length();

        int flag = 1;

        int start = getStart(s, len);

        int result = 0;

        int i = start;
        while (i < len) {

            char v = s.charAt(i);

            if (i == start && v == '-') {

                flag = -1;

            } else if (i == start && v == '+') {

                flag = 1;

            } else if (Character.isDigit(v)) {
                // 数字类型

                int num = v - '0';

                if (result > Integer.MAX_VALUE / 10 || (result == Integer.MAX_VALUE / 10 && num > Integer.MAX_VALUE % 10)) {

                    // 越界
                    return Integer.MAX_VALUE;

                } else if (result < Integer.MIN_VALUE / 10 || (result == Integer.MIN_VALUE / 10 && -num < Integer.MIN_VALUE % 10)) {

                    return Integer.MIN_VALUE;

                }

                result = result * 10 + num * flag;

            } else {
                break;
            }
            i++;
        }
        return 0;
    }

    /**
     * 去除前面空格，返回字符串开始位置
     */
    private int getStart(String s, int len) {
        int start = 0;
        for (int i = 0; i < len && s.charAt(i) == ' '; i++) {
            start++;
        }
        return start;
    }

}
