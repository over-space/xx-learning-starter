package com.learning.leetcode;

import com.learning.BaseTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 350. 两个数组的交集 II
 *
 * @author lifang
 * @since 2021/11/3
 */
public class Leetcode0350 extends BaseTest implements Testing {

    @Override
    public void test() {
        int[] intersect = intersect(new int[]{4, 5, 9}, new int[]{9, 4, 9, 8, 4});
        logger.info("intersect : {}", intersect);
    }

    public int[] intersect(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return intersect(nums2, nums1);
        }

        Map<Integer, Integer> sets = new HashMap<>();
        for (int num : nums1) {
            sets.put(num, sets.getOrDefault(num, 0) + 1);
        }

        int index = 0;
        int[] intersect = new int[nums1.length];
        for (int num : nums2) {
            Integer value = sets.getOrDefault(num, 0);
            if (value > 0) {
                intersect[index] = num;

                sets.put(num, value - 1);

                index++;
            }
        }
        return Arrays.copyOfRange(intersect, 0, index);
    }
}
