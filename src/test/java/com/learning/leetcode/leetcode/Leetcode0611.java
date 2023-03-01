package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * 给定一个包含非负整数的数组，你的任务是统计其中可以组成三角形三条边的三元组个数。
 */
public class Leetcode0611 extends BaseTest {


    /**
     * 三角形: 任意两边之和需要大于第三边。
     * 1. 先排序，那意味着 a + b > c
     */
    @Test
    public void test() {
        int count = triangleNumber(new int[]{2, 2, 3, 4});
        logger.info("count: {}", count);
    }


    public int triangleNumber(int[] nums) {
        Arrays.sort(nums);
        int len = nums.length;
        int count = 0;
        for (int a = 0; a <= len - 3; a++) {
            for (int b = a + 1; b <= len - 2; b++) {
                for (int c = b + 1; c <= len - 1; c++) {

                    logger.info("a: {}, b: {}, c: {}", a, b, c);

                    if (nums[a] + nums[b] > nums[c]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
