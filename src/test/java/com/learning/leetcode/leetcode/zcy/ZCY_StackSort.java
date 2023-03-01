package com.learning.leetcode.leetcode.zcy;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Stack;

/**
 * 用一个栈实现另一个栈的排序
 * 一个栈中元素的类型为整型，现在想将该栈从顶到底按从大到小的顺序排序，只许申请一
 * 个栈。除此之外，可以申请新的变量，但不能申请额外的数据结构。如何完成排序
 *
 * @author 李芳
 * @since 2022/11/28
 */
public class ZCY_StackSort extends BaseTest {

    @Test
    void test() {

        Stack stack = new Stack();
        stack.push(5);
        stack.push(2);
        stack.push(4);
        stack.push(2);
        stack.push(3);
        stack.push(1);
        stack.push(6);
        stack.push(3);

        logger.info("stack, sort before: {}", stack);
        sort(stack);
        logger.info("stack, sort after: {}", stack);
    }

    public void sort(Stack<Integer> stack) {
        Stack<Integer> help = new Stack<>();
        while (!stack.isEmpty()) {
            Integer cur = stack.pop();

            while (!help.isEmpty() && help.peek() < cur) {
                stack.push(help.pop());
            }

            help.push(cur);
        }
        while (!help.isEmpty()) {
            stack.push(help.pop());
        }
    }
}
