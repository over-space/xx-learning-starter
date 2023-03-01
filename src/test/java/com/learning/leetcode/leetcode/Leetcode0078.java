package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lifang
 * @since 2022/1/4
 */
public class Leetcode0078 extends BaseTest implements Testing {

    @Override
    public void test() {
        List<List<Integer>> subsets = subsets(new int[]{1, 2, 3});
        logger.info("subsets: {}", subsets);
    }

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> subsets = new ArrayList<>();

        int n = nums.length;
        int len = 1 << n;
        for (int mask = 0; mask < len; ++mask) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < n; ++i) {
                int m = 1 << i;
                if ((mask & m) != 0) {
                    list.add(nums[i]);
                }
            }
            subsets.add(new ArrayList<>(list));
        }
        return subsets;
    }

    @Test
    public void a() {
        for (int i = 0; i < 20; i++) {
            String bin = Integer.toBinaryString(i);

            logger.info("val : {}, binary : {}, 1 << i : {}", i, String.format("%08s", bin), 1 << i);
        }
    }
}
