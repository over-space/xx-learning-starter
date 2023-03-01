package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * 1218. 最长定差子序列
 * 给你一个整数数组arr和一个整数difference，请你找出并返回 arr中最长等差子序列的长度，该子序列中相邻元素之间的差等于 difference 。
 * <p>
 * 子序列 是指在不改变其余元素顺序的情况下，通过删除一些元素或不删除任何元素而从 arr 派生出来的序列。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-arithmetic-subsequence-of-given-difference
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author lifang
 * @since 2021/11/5
 */
public class Leetcode1218 extends BaseTest implements Testing {

    @Override
    public void test() {
        int i1 = longestSubsequence(new int[]{1, 2, 3, 4, 5}, 1);
        Assert.assertEquals(i1, 5);

        int i2 = longestSubsequence(new int[]{1, 3, 5, 7}, 1);
        Assert.assertEquals(i2, 1);

        int i3 = longestSubsequence(new int[]{1, 5, 7, 8, 5, 3, 4, 2, 1}, -2);
        Assert.assertEquals(i3, 4);
    }

    public int longestSubsequence(int[] arr, int difference) {
        int ans = 0;
        Map<Integer, Integer> dp = new HashMap<>();
        for (int num : arr) {
            dp.put(num, dp.getOrDefault(num - difference, 0) + 1);
            ans = Math.max(ans, dp.get(num));
        }
        return ans;
    }
}
