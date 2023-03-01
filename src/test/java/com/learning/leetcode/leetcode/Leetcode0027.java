package com.learning.leetcode;

import com.learning.BaseTest;
import org.junit.Test;

/**
 * 27. 移除元素
 * 给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
 * <p>
 * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
 * <p>
 * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
 * <p>
 * url: https://leetcode-cn.com/problems/remove-element/
 */
public class Leetcode0027 extends BaseTest {

    @Test
    public void test() {
        int[] nums = {3, 2, 2, 3};
        // removeElement(nums, 3);
        removeElement2(nums, 3);
    }

    public int removeElement(int[] nums, int val) {

        if (nums == null || nums.length <= 0) {
            return 0;
        }

        int len = nums.length;
        int left = 0;
        for (int right = 0; right < len; right++) {
            if (nums[right] != val) {
                nums[left] = nums[right];
                left++;
            }
        }
        return left;
    }

    public int removeElement2(int[] nums, int val) {

        if (nums == null || nums.length <= 0) {
            return 0;
        }

        int left = 0;
        int right = nums.length;
        while (left < right) {
            if (nums[left] == val) {
                nums[left] = nums[right - 1];
                right--;
            } else {
                left++;
            }
        }
        return left;
    }
}
