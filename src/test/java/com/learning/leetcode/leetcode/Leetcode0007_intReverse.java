package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.annotations.Test;

public class Leetcode0007_intReverse extends BaseTest {

    @Test
    public void test() {
        executor(-123);
    }

    private int executor(int x) {
        int result = reverse(x);
        logger.info("input:{}, output:{}", x, result);
        return result;
    }

    private int reverse(int x) {

        int res = 0;
        while (x != 0) {

            int digit = x % 10;

            if (res > Integer.MAX_VALUE / 10 || res < Integer.MIN_VALUE / 10) {
                return 0;
            }

            res = res * 10 + digit;
            x = x / 10;
        }
        return res;

        // int flag = x < 0 ? -1 : 1;
        //
        // int result = 0;
        //
        // while (true) {
        //     int mod = flag == 1 ? x % 10 : (x % 10) * -1;
        //     x = x / 10;
        //
        //     if (result > Integer.MAX_VALUE / 10 || (result == Integer.MAX_VALUE / 10 && mod > Integer.MAX_VALUE % 10)) {
        //         return 0;
        //     }
        //     if (result < Integer.MIN_VALUE / 10 || (result == Integer.MIN_VALUE / 10 && -mod < Integer.MIN_VALUE % 10)) {
        //         return 0;
        //     }
        //
        //     result = result * 10 + mod * flag;
        //
        //     if (x == 0) break;
        // }
        //
        // return result;
    }
}
