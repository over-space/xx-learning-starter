package com.learning.leetcode.leetcode.zcy;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;

/**
 * 生成窗口最大值数组
 *
 * @author 李芳
 * @since 2022/11/28
 */
public class ZCY_ArraySliding extends BaseTest {

    @Test
    void test() {
        int[] arr = {4, 3, 5, 4, 3, 3, 6, 7};
        int[] res = main(arr, 3);
        logger.info("res: {}", res);
    }

    public int[] main(int[] arr, int w) {
        // [4 3 5] 4 3 3 6 7 -> 5
        // 移动总次数： n - w + 1
        // 时间复杂度为：O(arr.length * w)

        int len = arr.length - w + 1;
        int[] res = new int[len];

        for (int i = 0; i < len; i++) {


            res[i] = arr[i];

            for (int j = i + 1; j < i + w; j++) {

                if (res[i] < arr[j]) {
                    res[i] = arr[j];
                }
            }
        }
        return res;
    }
}
