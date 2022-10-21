package com.learning.leetcode;

import com.learning.BaseTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 118. 杨辉三角
 * 给定一个非负整数 numRows，生成「杨辉三角」的前 numRows 行。
 *
 * 在「杨辉三角」中，每个数是它左上方和右上方的数的和。
 * @author lifang
 * @since 2021/11/4
 */
public class Leetcode0118 extends BaseTest implements Testing {

    @Override
    public void test() {
        List<List<Integer>> list = generate(5);
        logger.info("result:{}", list);
    }

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> list = new ArrayList<>();

        List<Integer> last = null;
        for (int i = 0; i < numRows; i++) {

            List<Integer> row = new ArrayList<>();

            for (int j = 0; j <= i; j++) {

                if(j == 0 || i == j){

                    row.add(1);

                }else{

                    row.add(last.get(j - 1) + last.get(j));

                }

            }
            list.add(row);
            last = row;
        }
        return list;
    }

    public List<List<Integer>> generate01(int numRows) {

        List<List<Integer>> list = new ArrayList<>();

        if(numRows == 0) return null;

        if(numRows >= 1){
            list.add(Arrays.asList(1));
        }

        List<Integer> last = null;
        for (int i = 2; i <= numRows; i++) {

            if(last == null) {
                last = list.get(i - 2);
            }

            List<Integer> row = new ArrayList<>();
            row.add(1);

            for (int j = 1; j < last.size(); j++) {
                row.add(last.get(j - 1) + last.get(j));
            }

            row.add(1);
            list.add(row);
            last =  row;
        }
        return list;
    }
}
