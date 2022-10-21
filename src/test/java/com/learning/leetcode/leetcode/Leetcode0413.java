package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.annotations.Test;

public class Leetcode0413 extends BaseTest {

    @Test
    public void test(){
        // [1, 2, 3, 8, 13, 18]
        // > [1, 2, 3]
        // > [3, 8, 13]
        // > [8, 13, 18]
        int count = numberOfArithmeticSlices(new int[]{1, 2, 3, 8, 13, 18});
        logger.info("count:{}", count);
    }

    public int numberOfArithmeticSlices(int[] nums) {

        if (nums == null || nums.length < 3) return 0;

        int len = nums.length;

        int sub = nums[0] - nums[1];
        int t = 0;
        int result = 0;
        for (int i = 2; i < len; i++) {
            int v = nums[i - 1] - nums[i];
            if(v == sub){
                t++;
            }else{
                sub = v;
                t = 0;
            }
            result += t;
        }
        return result;
    }
}
