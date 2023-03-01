package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.annotations.Test;

/**
 * 263. 丑数
 * 给你一个整数 n ，请你判断 n 是否为 丑数 。如果是，返回 true ；否则，返回 false 。
 * <p>
 * 丑数 就是只包含质因数2、3、5的正整数。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/ugly-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode0263 extends BaseTest {

    @Test
    void test() {
        boolean ugly = isUgly(79242);
        logger.info("ugly: {}", ugly);
    }

    public boolean isUgly(int n) {
        while (n > 0) {
            if (n % 2 == 0) n /= 2;
            else if (n % 3 == 0) n /= 3;
            else if (n % 5 == 0) n /= 5;
            else break;
        }
        return n == 1;
    }

}
