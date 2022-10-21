package com.learning.leetcode;

import com.learning.BaseTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 3. 无重复字符的最长子串
 * 给定一个字符串，请你找出其中不含有重复字符的最长子串的长度。
 * <p>
 * 示例1:
 * <p>
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-substring-without-repeating-characters
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author lifang
 * @since 2021/4/9
 */
public class Leetcode0003 extends BaseTest {

    @Test
    public void run() {
        lengthOfLongestSubstring1("abcabcbb");
        lengthOfLongestSubstring2("abcabcbb");
        lengthOfLongestSubstring2(" ");
    }

    public int lengthOfLongestSubstring1(String s) {

        if (s == null || s.length() == 0) {
            return 0;
        }

        char[] chars = s.toCharArray();

        int result = 0;

        for (int i = 0; i < chars.length; i++) {

            char a = chars[i];

            int max = 1;

            List<Character> characters = new ArrayList<>();

            characters.add(a);

            for (int j = i + 1; j < chars.length; j++) {

                char b = chars[j];

                boolean contains = characters.contains(b);
                if (contains) {
                    break;
                } else {
                    characters.add(b);
                    max++;
                }
            }

            result = Math.max(max, result);
        }
        return result;
    }

    public int lengthOfLongestSubstring2(String s) {
        if (s.length() == 0) return 0;
        HashMap<Character, Integer> map = new HashMap<>();
        int max = 0;
        int left = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (map.containsKey(c)) {
                left = Math.max(left, map.get(c) + 1);
            }
            map.put(c, i);
            max = Math.max(max, i - left + 1);
        }
        return max;
    }
}
