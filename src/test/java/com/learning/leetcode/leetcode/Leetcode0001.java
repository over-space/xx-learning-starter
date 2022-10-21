package com.learning.leetcode;


import com.learning.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author lifang
 * @since 2021/1/14
 */
public class Leetcode0001 extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(Leetcode0001.class);


    @Test
    public void run() {
        prefixesDivBy5(new int[]{0, 1, 1});
    }

    public List<Boolean> prefixesDivBy5(int[] A) {

        int num = 0;
        for (int i : A) {
            num <<= 1;

            System.out.println(num);
            num += A[i];

            System.out.println(num);

            num %= 10;

            System.out.println(num % 5 == 0);

        }

        return null;
    }


}
