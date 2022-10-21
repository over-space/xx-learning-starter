package com.learning.leetcode;

import com.learning.BaseTest;

/**
 * @author lifang
 * @since 2021/10/14
 */
public class Leetcode0069 extends BaseTest implements Testing {

    @Override
    @org.testng.annotations.Test
    public void test() {
        int[] nums = {98,100,99,98,90, 30, 20, 10, 5, 3, 1};
        //             0, 1, 2, 3, 4, 5, 6, 7, 8
        int index = peakIndexInMountainArray(nums);
        logger.info("index:{}", index);
    }

    public int peakIndexInMountainArray(int[] arr) {
        return peakIndexInMountainArray03(arr);
    }

    private int peakIndexInMountainArray03(int[] arr){
        int ans = 0;
        int left = 0;
        int right = arr.length - 1;
        while (left <= right){
            int mid = (left + right) / 2;
            if(arr[mid] > arr[mid + 1]){
                ans = mid;
                right = mid - 1;
            }else{
                left = mid + 1;
            }
        }
        return ans;
    }

    private int peakIndexInMountainArray02(int[] arr){
        // 1, 2, 4, 2, 1
        int n = arr.length;
        int left = 1, right = n - 2, ans = 0;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] > arr[mid + 1]) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    /**
     * 枚举法
     * @param arr
     * @return
     */
    private int peakIndexInMountainArray01(int[] arr) {
        int len = arr.length;
        int index = 0;
        while (index < len - 1) {
            if (arr[index] > arr[index + 1]) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
