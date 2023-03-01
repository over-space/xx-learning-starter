package com.learning.leetcode;

import com.learning.BaseTest;
import com.learning.leetcode.base.ListNode;
import org.junit.Test;

/**
 * 82. 删除排序链表中的重复元素 II
 * 存在一个按升序排列的链表，给你这个链表的头节点 head ，请你删除链表中所有存在数字重复情况的节点，只保留原始链表中 没有重复出现 的数字。
 * <p>
 * 返回同样按升序排列的结果链表。
 * 输入：head = [1,2,3,3,4,4,5]
 * 输出：[1,2,5]
 *
 * @author lifang
 * @since 2021/3/25
 */
public class Leetcode0082_LinkList_004 extends BaseTest {

    @Test
    public void run() {
        ListNode head = new ListNode(1, new ListNode(1, new ListNode(1, new ListNode(4, new ListNode(5)))));
        deleteDuplicates(head);
    }

    public ListNode deleteDuplicates(ListNode head) {

        ListNode prev = head;
        ListNode cur = head;
        while (cur != null) {
            cur = cur.next;
        }
        return null;
    }
}
