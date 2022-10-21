package com.learning.leetcode;

import com.learning.BaseTest;

import java.util.ArrayList;
import java.util.List;

/**
 * 1276. 不浪费原料的汉堡制作方案
 * 
 * 圣诞活动预热开始啦，汉堡店推出了全新的汉堡套餐。为了避免浪费原料，请你帮他们制定合适的制作计划。
 *
 * 给你两个整数tomatoSlices和cheeseSlices，分别表示番茄片和奶酪片的数目。不同汉堡的原料搭配如下：
 *
 * 巨无霸汉堡：4 片番茄和 1 片奶酪
 * 小皇堡：2 片番茄和1 片奶酪
 * 请你以[total_jumbo, total_small]（[巨无霸汉堡总数，小皇堡总数]）的格式返回恰当的制作方案，使得剩下的番茄片tomatoSlices和奶酪片cheeseSlices的数量都是0。
 *
 * 如果无法使剩下的番茄片tomatoSlices和奶酪片cheeseSlices的数量为0，就请返回[]。
 *
 * 
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-burgers-with-no-waste-of-ingredients
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode1276 extends BaseTest {

    public void test(){
        List<Integer> result = numOfBurgers(16, 7);
        System.out.println(result);
    }

    public List<Integer> numOfBurgers(int tomatoSlices, int cheeseSlices) {
        List<Integer> result = new ArrayList<>();

        if(tomatoSlices % 2 != 0 || tomatoSlices < cheeseSlices * 2 || cheeseSlices * 4 < tomatoSlices){
            return result;
        }
        result.add(tomatoSlices / 2 - cheeseSlices);
        result.add(tomatoSlices / 2 - cheeseSlices);
        return result;
    }
}
