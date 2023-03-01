package com.learning.leetcode;

import com.learning.BaseTest;

/**
 * 53. 最大子序和
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
 *
 * @author lifang
 * @since 2021/11/1
 */
public class Leetcode0053 extends BaseTest implements Testing {

    @Override
    public void test() {
        int[] nums = {-2, 1, -3, 4, -1, 1, 1, -6, 8};
        maxSubArray(nums);
    }

    public int maxSubArray(int[] nums) {
        int max = nums[0];
        int dp = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (dp <= 0) {
                dp = nums[i];
            } else {
                dp = dp + nums[i];
            }
            max = Math.max(max, dp);
        }
        return max;
    }
}
