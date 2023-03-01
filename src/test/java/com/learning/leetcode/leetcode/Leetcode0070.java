package com.learning.leetcode;

import com.learning.BaseTest;

/**
 * 70. 爬楼梯
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * <p>
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * <p>
 * 注意：给定 n 是一个正整数。
 *
 * @author lifang
 * @since 2021/11/16
 */
public class Leetcode0070 extends BaseTest implements Testing {

    @Override
    public void test() {
        int i = climbStairs(5);
        logger.info("i : {}", i);
    }

    public int climbStairs(int n) {
        if (n <= 3) return n;
        int l2 = 2, l3 = 3, m = l2 + l3;
        for (int i = 4; i <= n; i++) {
            m = l2 + l3;
            l2 = l3;
            l3 = m;
        }
        return m;
    }
}
