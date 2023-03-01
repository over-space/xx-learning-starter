package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.annotations.Test;

/**
 * 1646. 获取生成数组中的最大值
 * 给你一个整数 n 。按下述规则生成一个长度为 n + 1 的数组 nums ：
 * <p>
 * nums[0] = 0
 * nums[1] = 1
 * 当 2 <= 2 * i <= n 时，nums[2 * i] = nums[i]
 * 当 2 <= 2 * i + 1 <= n 时，nums[2 * i + 1] = nums[i] + nums[i + 1]
 * 返回生成数组 nums 中的 最大 值。
 */
public class Leetcode1646 extends BaseTest {

    @Test
    public void test() {
        int result = getMaximumGenerated(7);
        logger.info("result: {}", result);
    }

    public int getMaximumGenerated(int n) {
        if (n <= 0) return 0;
        int[] nums = new int[n + 1];
        nums[0] = 0;
        nums[1] = 1;

        int ans = 0;
        for (int i = 2; i <= n; i++) {
            if (i % 2 == 0) nums[i] = nums[i / 2];
            else nums[i] = nums[i / 2] + nums[i / 2 + 1];
            ans = Math.max(ans, nums[i]);
        }
        return ans;
    }
}
