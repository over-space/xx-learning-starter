package com.learning.leetcode;

import com.learning.BaseTest;
import com.learning.leetcode.base.ListNode;
import org.testng.annotations.Test;

/**
 * 反转一个单链表。
 * <p>
 * 示例:
 * <p>
 * 输入: 1->2->3->4->5->NULL
 * 输出: 5->4->3->2->1->NULL
 * 进阶:
 * 你可以迭代或递归地反转链表。你能否用两种方法解决这道题？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-linked-list
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author lifang
 * @since 2021/3/18
 */
public class Leetcode0206_LinkList_001 extends BaseTest {

    @Test
    public void run() {
        ListNode node = reverseList(ListNode.DEFAULT_NODE);
        while (node != null) {
            logger.info("result : {}", node);
            node = node.next;
        }
    }

    public ListNode reverseList(ListNode head) {
        // 1 -> 2 -> 3
        // 3 -> 2 -> 1
        ListNode prev = null;
        ListNode cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }
}
