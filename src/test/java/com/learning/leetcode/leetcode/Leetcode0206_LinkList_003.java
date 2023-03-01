package com.learning.leetcode;

import com.learning.BaseTest;
import com.learning.leetcode.base.ListNode;
import org.junit.Test;

/**
 * 给定一个链表，旋转链表，将链表每个节点向右移动k个位置，其中k是非负数。
 * <p>
 * 示例1:
 * <p>
 * 输入: 1->2->3->4->5->NULL, k = 2
 * 输出: 4->5->1->2->3->NULL
 * 解释:
 * 向右旋转 1 步: 5->1->2->3->4->NULL
 * 向右旋转 2 步: 4->5->1->2->3->NULL
 * 示例2:
 * <p>
 * 输入: 0->1->2->NULL, k = 4
 * 输出: 2->0->1->NULL
 * 解释:
 * 向右旋转 1 步: 2->0->1->NULL
 * 向右旋转 2 步: 1->2->0->NULL
 * 向右旋转 3 步:0->1->2->NULL
 * 向右旋转 4 步:2->0->1->NULL
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/rotate-list
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author lifang
 * @since 2021/3/19
 */
public class Leetcode0206_LinkList_003 extends BaseTest {

    @Test
    public void run() {
        rotateRight(ListNode.DEFAULT_NODE, 2);
    }

    public ListNode rotateRight(ListNode head, int k) {
        // 1 -> 2 -> 3 -> 4 -> 5 -> 1

        if (head == null) return null;
        if (head.next == null) return head;

        // 1. 将链表转换成环状
        ListNode old_list = head;
        int n = 1;
        while (old_list.next != null) {
            old_list = old_list.next;
            n++;
        }
        old_list.next = head;

        // 2. 移动指定位子
        ListNode new_tail = head;
        System.out.println("result : " + (n - k % n - 1));
        for (int i = 0; i < n - k % n - 1; i++) {
            new_tail = new_tail.next;
        }
        ListNode new_head = new_tail.next;
        new_tail.next = null;
        return new_head;
    }
}
