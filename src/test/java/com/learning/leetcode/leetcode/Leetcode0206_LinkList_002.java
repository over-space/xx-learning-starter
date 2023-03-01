package com.learning.leetcode;

import com.learning.BaseTest;
import com.learning.leetcode.base.ListNode;
import org.junit.Test;

/**
 * 实现一种算法，找出单向链表中倒数第 k 个节点。返回该节点的值。
 * <p>
 * 注意：本题相对原题稍作改动
 * <p>
 * 示例：
 * <p>
 * 输入： 1->2->3->4->5 和 k = 2
 * 输出： 4
 * 说明：
 * <p>
 * 给定的 K 保证是有效的。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/kth-node-from-end-of-list-lcci
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author lifang
 * @since 2021/3/18
 */
public class Leetcode0206_LinkList_002 extends BaseTest {

    @Test
    public void run() {
        int val = kthToLast(ListNode.DEFAULT_NODE, 2);
        logger.info("val:{}", val);
    }

    public int kthToLast(ListNode head, int k) {
        ListNode slow = head;
        ListNode fast = head;
        for (int i = 0; i < k; i++) {
            fast = fast.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow.val;
    }
}
