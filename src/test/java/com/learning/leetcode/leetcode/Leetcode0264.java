package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.annotations.Test;

import java.util.Stack;

/**
 * 264. 丑数 II
 * 给你一个整数 n ，请你找出并返回第 n 个 丑数 。
 * <p>
 * 丑数 就是只包含质因数 2、3或 5 的正整数。
 */
public class Leetcode0264 extends BaseTest {

    @Test
    void test() {
        int i = nthUglyNumber(10);
        logger.info("result:{}", i);
    }

    public int nthUglyNumber(int n) {
        Stack<Integer> uglyNumbers = new Stack<>();
        for (int i = 1; ; i++) {
            int m = i;
            while (true) {
                if (uglyNumbers.size() == n) {
                    return uglyNumbers.pop();
                }
                if (m == 1) {
                    uglyNumbers.push(i);
                    break;
                }
                if (m % 2 == 0) m /= 2;
                else if (m % 3 == 0) m /= 3;
                else if (m % 5 == 0) m /= 5;
                else break;
            }
        }
    }

}
