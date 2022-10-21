package com.learning.leetcode;

import com.learning.BaseTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lifang
 * @since 2021/11/11
 */
public class Leetcode0387 extends BaseTest implements Testing {

    @Override
    public void test() {
        int index = firstUniqChar("aabb");
        logger.info("result:{}", index);
    }


    public int firstUniqChar(String s) {
        char[] chars = s.toCharArray();
        Map<Character, Integer> map = new HashMap<>();
        for (char n : chars) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        for (int i = 0; i < chars.length; i++) {
            if(map.get(chars[i]) == 1){
                return i;
            }
        }
        return -1;
    }
}
