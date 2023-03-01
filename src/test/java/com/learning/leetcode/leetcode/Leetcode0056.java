package com.learning.leetcode.leetcode;

import com.alibaba.fastjson.JSONArray;
import com.learning.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Leetcode0056 extends BaseTest {


    @Test
    void test() {
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] merge = merge2(intervals);
        System.out.println(JSONArray.toJSON(merge));
    }

    public int[][] merge2(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new int[0][2];
        }

        // 排序
        Arrays.sort(intervals, Comparator.comparingInt(num -> num[0]));

        List<int[]> mergeResult = new ArrayList<>();
        for (int i = 0; i < intervals.length; i++) {
            int left = intervals[i][0];
            int right = intervals[i][1];
            int mergeResultSize = mergeResult.size();

            if (mergeResult.isEmpty() || mergeResult.get(mergeResultSize - 1)[1] < left) {
                mergeResult.add(new int[]{left, right});
            } else {
                mergeResult.get(mergeResultSize - 1)[1] = Math.max(mergeResult.get(mergeResultSize - 1)[1], right);
            }
        }
        return mergeResult.toArray(new int[][]{});
    }


    public int[][] merge(int[][] intervals) {

        if (intervals == null || intervals.length == 0) {
            return new int[0][2];
        }

        // 先按数组第一位排序
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        System.out.println(JSONArray.toJSON(intervals));


        List<int[]> result = new ArrayList<>();

        // Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        // int[] index = {0};
        // Arrays.stream(intervals)
        //         .sorted(Comparator.comparingInt(num -> num[0]))
        //         .forEach(num -> {
        //             int i = index[0];
        //             int left = intervals[i][0];
        //             int right = intervals[i][1];
        //             // 如果列表为空,或者当前区间与上一区间不重合,直接添加
        //             if(result.size() == 0 || result.get(result.size() - 1)[1] < left){
        //                 result.add(new int[]{left, right});
        //             }else {
        //                 result.get(result.size() - 1)[1] = Math.max(result.get(result.size() - 1)[1], right);
        //             }
        //             index[0] = index[0]++;
        //         });


        // for (int[] interval : intervals) {
        //     int left = intervals[i][0];
        //     int right = intervals[i][1];
        //
        //     if(result.size() == 0 || result.get(result.size() - 1)[1] < left){
        //         result.add(new int[]{left, right});
        //     }else {
        //         result.get(result.size() - 1)[1] = Math.max(result.get(result.size() - 1)[1], right);
        //     }
        // }

        for (int i = 0; i < intervals.length; ++i) {
            int left = intervals[i][0];
            int right = intervals[i][1];

            if (result.size() == 0 || result.get(result.size() - 1)[1] < left) {
                result.add(new int[]{left, right});
            } else {
                result.get(result.size() - 1)[1] = Math.max(result.get(result.size() - 1)[1], right);
            }
        }
        return result.toArray(new int[][]{});
    }

}
