package com.learning.leetcode;

import com.learning.BaseTest;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class Leetcode0091 extends BaseTest {

    @Test
    public void run(){
        int num = numDecodings("12");
        System.out.println(num);
    }

    public int numDecodings(String s) {

        int num = 0;

        Set<Integer> sets = new HashSet<>();

        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {

            Integer c = Integer.valueOf(chars[i]);

            if(c == 0) continue;

            if(!sets.contains(c)){
                sets.add(c);
                num++;
            }

            if(i + 1 < chars.length) {
                Integer n = c * 10 + Integer.valueOf(chars[i + 1]);
                if(n >= 1 && n <= 26){
                    if(!sets.contains(c)){
                        sets.add(c);
                        num++;
                    }
                }
            }
        }

        return 0;
    }
}
