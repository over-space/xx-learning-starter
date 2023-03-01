package com.learning.leetcode;

import com.learning.BaseTest;

/**
 * 367. 有效的完全平方数
 * 给定一个 正整数 num ，编写一个函数，如果 num 是一个完全平方数，则返回 true ，否则返回 false 。
 * 进阶：不要 使用任何内置的库函数，如  sqrt 。
 *
 * @author lifang
 * @since 2021/11/4
 */
public class Leetcode0367 extends BaseTest implements Testing {

    @Override
    public void test() {
        boolean perfectSquare = isPerfectSquare(2000105819);
        logger.info("result: {}", perfectSquare);
    }

    public boolean isPerfectSquare(int num) {
        int left = 0, right = num;
        while (left <= right) {
            int mid = (right - left) / 2 + left;
            long square = (long) mid * mid;
            if (square < num) {
                left = mid + 1;
            } else if (square > num) {
                right = mid - 1;
            } else {
                return true;
            }
        }
        return false;
    }
}
