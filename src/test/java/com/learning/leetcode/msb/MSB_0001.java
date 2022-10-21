package com.learning.msb;

import com.learning.BaseTest;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lifang
 * @since 2021/12/28
 */
public class MSB_0001 extends BaseTest {

    private static final Random RANDOM = new Random();

    @Test
    void test(){
        for (int i = 0; i < 100; i++) {
            logger.info("{} >> 1: {}", i, i >> 1);
            logger.info("{} >> 2: {}", i, i >> 2);
        }
        int ans = run(new int[]{1, 3, 4, 5, 7, 10, 13}, 4);
        logger.info("ans : {}", ans);
    }

    @Test
    public void testRandom() throws Exception {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < 100; i++) {
            int v1 = current.nextInt(100);
            int v2 = RANDOM.nextInt(100);
            logger.info("v1 : {}, v2: {}",v1, v2);
        }
    }

    private static int run(int[] arr, int a){
        // arr = 1, 3, 4, 5, 7, 10, 13
        // a = 4;

        int len = arr.length;
        int ans = 1;
        for (int right = 0; right < len; right++){

            int step = nearestIndex(arr, right, arr[right] - a);

            ans = Math.max(ans, right - step + 1);

        }
        return ans;
    }

    private static int nearestIndex(int[] arr, int right, int value) {
        int left = 0;
        int index = right;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] >= value) {
                index = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return index;
    }

}
