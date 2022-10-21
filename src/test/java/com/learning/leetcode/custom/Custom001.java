package com.learning.custom;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * 查找数组中重复的数字
 */
public class Custom001 extends BaseTest {


    @Test
    public void test(){
        Integer[] result = find1(new int[]{2, 0, 1, 5, 2, 3, 1});
        StringJoiner joiner = new StringJoiner(",");
        for (Integer integer : result) {
            joiner.add("" + integer);
        }
        logger.info("result:{}", joiner);
    }


    public Integer[] find1(int[] nums){
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if(nums[i] == nums[j]){
                    result.add(nums[i]);
                    break;
                }
            }
        }
        return result.toArray(new Integer[0]);
    }

}
