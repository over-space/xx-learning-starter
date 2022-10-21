package com.learning.leetcode;

import com.learning.BaseTest;

/**
 * 1137. 第 N 个泰波那契数
 * 泰波那契序列Tn定义如下：
 *
 * T0 = 0, T1 = 1, T2 = 1, 且在 n >= 0的条件下 Tn+3 = Tn + Tn+1 + Tn+2
 *
 * 给你整数n，请返回第 n 个泰波那契数Tn 的值。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/n-th-tribonacci-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * @author lifang
 * @since 2021/11/16
 */
public class Leetcode1137 extends BaseTest implements Testing {

    @Override
    public void test() {
        int tribonacci = tribonacci(25);
        logger.info("tribonacci:{}", tribonacci);
    }

    public int tribonacci(int n) {
        if(n < 2) return n;
        int l0 = 0, l1 = 1, l2 = 1;
        int m = l2;
        for (int i = 3; i <= n; i++){
            m =  l0 + l1 + l2;
            l0 = l1;
            l1 = l2;
            l2 = m;
        }
        return m;
    }
}
