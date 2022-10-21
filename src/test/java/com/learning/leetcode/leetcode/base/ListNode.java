package com.learning.leetcode.base;

/**
 * @author lifang
 * @since 2021/3/18
 */
public class ListNode {

    // 1 -> 2 -> 3
    public static ListNode DEFAULT_NODE = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));


    public int val;

    public ListNode next;

    ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    @Override
    public String toString() {
        return "ListNode{" +
                "val=" + val +
                '}';
    }
}
