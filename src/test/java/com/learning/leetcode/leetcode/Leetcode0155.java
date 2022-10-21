package com.learning.leetcode;

import com.learning.BaseTest;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author lifang
 * @since 2022/1/11
 */
public class Leetcode0155 extends BaseTest implements Testing {

    @Override
    public void test() {

    }

    class MinStack {

        Deque<Integer> xStack;

        Integer min;

        public MinStack() {
            xStack = new LinkedList<Integer>();
        }

        public void push(int x) {
            xStack.push(x);
        }

        public void pop() {
            xStack.pop();
        }

        public int top() {
            return xStack.peek();
        }

        public int getMin() {
            return min;
        }
    }

}
