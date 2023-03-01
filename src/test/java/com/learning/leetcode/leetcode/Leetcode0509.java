package com.learning.leetcode;

import com.learning.BaseTest;

/**
 * 509. 斐波那契数
 * 斐波那契数，通常用 F(n) 表示，形成的序列称为 斐波那契数列 。该数列由 0 和 1 开始，后面的每一项数字都是前面两项数字的和。也就是：
 * <p>
 * F(0) = 0，F(1) = 1
 * F(n) = F(n - 1) + F(n - 2)，其中 n > 1
 * 给你 n ，请计算 F(n) 。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/fibonacci-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author lifang
 * @since 2021/11/15
 */
public class Leetcode0509 extends BaseTest implements Testing {

    @Override
    public void test() {
        int fib = fib(4);
        logger.info("fib : {}", fib);
    }

    public int fib(int n) {
        if (n < 2) return n;
        int l1 = 0, l2 = 0, m = 1;
        for (int i = 2; i <= n; i++) {
            l1 = l2;
            l2 = m;
            m = l1 + l2;
        }
        return m;
    }

}
