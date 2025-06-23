package main.java.org.dao.qa;

/**
 * 反转链表问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个单链表的头节点，反转该链表并返回反转后的头节点。
 * 
 * <p><b>示例</b>:
 * 输入: 1->2->3->4->5->NULL
 * 输出: 5->4->3->2->1->NULL
 * 
 * <p><b>问题难度</b>: 🔥 简单 (LeetCode 第206题)
 * 
 * <p><b>解题思路</b>:
 * 1. 迭代法: 使用指针遍历链表并逐个反转节点
 * 2. 递归法: 递归地将问题分解为子问题
 * 3. 栈辅助法: 利用栈的LIFO特性实现反转
 * 
 * <p><b>时间复杂度</b>: O(n) - n为链表长度
 * <p><b>空间复杂度</b>: 
 *  迭代法: O(1)
 *  递归法: O(n) - 递归调用栈
 *  栈辅助法: O(n) - 栈空间
 * 
 * <p><b>应用场景</b>:
 * 1. 数据结构课程教学
 * 2. 链表相关算法的基础
 * 3. 需要链表倒序处理的场景
 * 4. 面试常见题
 */

public class ReverseLinkedList {

    // 链表节点定义
    static class ListNode {
        int val;
        ListNode next;
        
        ListNode(int val) {
            this.val = val;
        }
    }
    
    // ========================= 解法1: 迭代法 =========================
    
    /**
     * 迭代法反转链表
     * 
     * @param head 链表头节点
     * @return 反转后的链表头节点
     */
    public static ListNode reverseIterative(ListNode head) {
        ListNode prev = null;    // 前一个节点
        ListNode curr = head;    // 当前节点
        
        while (curr != null) {
            // 保存当前节点的下一个节点
            ListNode nextTemp = curr.next;
            
            // 反转当前节点的指针
            curr.next = prev;
            
            // 移动指针
            prev = curr;
            curr = nextTemp;
        }
        
        // 返回新的头节点
        return prev;
    }
    
    // ========================= 解法2: 递归法 =========================
    
    /**
     * 递归法反转链表
     * 
     * @param head 链表头节点
     * @return 反转后的链表头节点
     */
    public static ListNode reverseRecursive(ListNode head) {
        // 终止条件：空链表或单个节点
        if (head == null || head.next == null) {
            return head;
        }
        
        // 递归反转剩余部分
        ListNode reversed = reverseRecursive(head.next);
        
        // 将当前节点反转
        head.next.next = head;
        head.next = null;
        
        return reversed;
    }
    
    // ========================= 解法3: 栈辅助法 =========================
    
    /**
     * 利用栈反转链表
     * 
     * @param head 链表头节点
     * @return 反转后的链表头节点
     */
    public static ListNode reverseWithStack(ListNode head) {
        // 处理空链表情况
        if (head == null) return null;
        
        java.util.Deque<ListNode> stack = new java.util.LinkedList<>();
        
        // 将节点依次压入栈中
        ListNode current = head;
        while (current != null) {
            stack.push(current);
            current = current.next;
        }
        
        // 创建新链表的头节点
        ListNode newHead = stack.pop();
        current = newHead;
        
        // 从栈中弹出节点并链接
        while (!stack.isEmpty()) {
            current.next = stack.pop();
            current = current.next;
        }
        
        // 最后节点的next设为null
        current.next = null;
        
        return newHead;
    }
    
    // ========================= 工具方法 =========================
    
    /**
     * 创建链表
     * 
     * @param values 节点值数组
     * @return 链表头节点
     */
    public static ListNode createLinkedList(int[] values) {
        if (values == null || values.length == 0) return null;
        
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }
        
        return head;
    }
    
    /**
     * 打印链表
     * 
     * @param head 链表头节点
     */
    public static void printList(ListNode head) {
        StringBuilder sb = new StringBuilder();
        ListNode current = head;
        
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) {
                sb.append(" -> ");
            }
            current = current.next;
        }
        
        System.out.println(sb.toString());
    }
    
    /**
     * 可视化链表反转过程
     * 
     * @param head 原始链表头节点
     */
    public static void visualizeReverse(ListNode head) {
        System.out.println("\n链表反转过程可视化:");
        System.out.println("原始链表: ");
        printList(head);
        
        // 迭代法可视化
        System.out.println("\n迭代法反转过程:");
        visualizeIterativeProcess(head);
        
        // 递归法可视化
        System.out.println("\n递归法反转过程:");
        visualizeRecursiveProcess(head);
    }
    
    // 可视化迭代过程
    private static void visualizeIterativeProcess(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        int step = 1;
        
        while (curr != null) {
            System.out.printf("第%d步:\n", step++);
            System.out.println("前节点: " + (prev != null ? prev.val : "null"));
            System.out.println("当前节点: " + curr.val);
            
            // 保存下一节点
            ListNode nextTemp = curr.next;
            System.out.println("下一节点: " + (nextTemp != null ? nextTemp.val : "null"));
            
            // 反转指针
            curr.next = prev;
            System.out.println("指针反转: " + curr.val + " -> " + (prev != null ? prev.val : "null"));
            
            // 移动指针
            prev = curr;
            curr = nextTemp;
            
            // 打印当前状态
            System.out.print("当前链表状态: ");
            printPartialList(prev);
        }
        
        System.out.println("\n最终反转结果: ");
        printList(prev);
    }
    
    // 可视化递归过程
    private static void visualizeRecursiveProcess(ListNode head) {
        System.out.println("原始链表: ");
        printList(head);
        
        ListNode reversed = doReverseRecursive(head);
        
        System.out.println("反转后链表: ");
        printList(reversed);
    }
    
    // 实际递归执行
    private static ListNode doReverseRecursive(ListNode node) {
        // 基本情况
        if (node == null || node.next == null) {
            System.out.println("递归终止: " + (node != null ? node.val : "null"));
            return node;
        }
        
        System.out.printf("递归调用: 节点 %d\n", node.val);
        
        // 递归反转
        ListNode reversedList = doReverseRecursive(node.next);
        
        // 反转当前节点
        System.out.printf("反转指针: %d -> %d\n", node.next.val, node.val);
        node.next.next = node;
        node.next = null;
        
        System.out.print("当前链表状态: ");
        printList(reversedList);
        
        return reversedList;
    }
    
    // 打印部分链表
    private static void printPartialList(ListNode head) {
        ListNode current = head;
        StringBuilder sb = new StringBuilder();
        
        while (current != null) {
            sb.append(current.val);
            if (current.next != null) {
                sb.append(" -> ");
            }
            current = current.next;
        }
        
        System.out.println(sb.toString());
    }
    
    // ========================= 性能测试 =========================
    
    /**
     * 比较不同反转算法的性能
     * 
     * @param listSize 链表大小
     */
    public static void performanceTest(int listSize) {
        // 创建大链表
        int[] values = new int[listSize];
        for (int i = 0; i < listSize; i++) {
            values[i] = i;
        }
        ListNode bigList = createLinkedList(values);
        
        // 迭代法性能测试
        long start = System.nanoTime();
        reverseIterative(bigList);
        long iterativeTime = System.nanoTime() - start;
        
        // 递归法性能测试
        start = System.nanoTime();
        reverseRecursive(bigList);
        long recursiveTime = System.nanoTime() - start;
        
        // 栈辅助法性能测试
        start = System.nanoTime();
        reverseWithStack(bigList);
        long stackTime = System.nanoTime() - start;
        
        System.out.printf("\n链表大小: %d 节点\n", listSize);
        System.out.printf("迭代法用时: %d ns\n", iterativeTime);
        System.out.printf("递归法用时: %d ns\n", recursiveTime);
        System.out.printf("栈辅助法用时: %d ns\n", stackTime);
        System.out.printf("迭代法用时 / 递归法用时: %.2f\n", (double)iterativeTime/recursiveTime);
    }
    
    // ========================= 应用场景 =========================
    
    /**
     * 判断链表是否为回文
     * 
     * @param head 链表头节点
     * @return 是否回文
     */
    public static boolean isPalindrome(ListNode head) {
        // 处理空链表或单节点链表
        if (head == null || head.next == null) return true;
        
        // 使用快慢指针找到中点
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 反转后半部分链表
        ListNode secondHalf = reverseIterative(slow);
        ListNode saveSecondHalf = secondHalf;
        
        // 比较前后两部分
        ListNode firstHalf = head;
        boolean result = true;
        
        while (secondHalf != null) {
            if (firstHalf.val != secondHalf.val) {
                result = false;
                break;
            }
            firstHalf = firstHalf.next;
            secondHalf = secondHalf.next;
        }
        
        // 恢复链表原始结构
        reverseIterative(saveSecondHalf);
        
        return result;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testReverseAlgorithms();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testReverseAlgorithms() {
        System.out.println("====== 链表反转算法测试 ======");
        int[] testValues = {1, 2, 3, 4, 5};
        
        // 创建原始链表
        ListNode head = createLinkedList(testValues);
        System.out.println("原始链表: ");
        printList(head);
        
        // 迭代法反转
        System.out.println("\n迭代法反转: ");
        ListNode iterativeReversed = reverseIterative(head);
        printList(iterativeReversed);
        
        // 重新创建链表（因为第一次反转修改了原始结构）
        head = createLinkedList(testValues);
        
        // 递归法反转
        System.out.println("\n递归法反转: ");
        ListNode recursiveReversed = reverseRecursive(head);
        printList(recursiveReversed);
        
        // 重新创建链表
        head = createLinkedList(testValues);
        
        // 栈辅助法反转
        System.out.println("\n栈辅助法反转: ");
        ListNode stackReversed = reverseWithStack(head);
        printList(stackReversed);
        
        // 可视化反转过程
        System.out.println("\n====== 可视化测试 ======");
        head = createLinkedList(new int[]{1, 2, 3, 4});
        visualizeReverse(head);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        performanceTest(10000);
        performanceTest(100000);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 测试回文链表
        ListNode palindrome = createLinkedList(new int[]{1, 2, 3, 2, 1});
        ListNode notPalindrome = createLinkedList(new int[]{1, 2, 3, 4});
        
        System.out.print("链表: ");
        printList(palindrome);
        System.out.println("是否为回文: " + isPalindrome(palindrome));
        
        System.out.print("链表: ");
        printList(notPalindrome);
        System.out.println("是否为回文: " + isPalindrome(notPalindrome));
        
        // 测试排序反转
        System.out.println("\n链表排序与反转应用:");
        ListNode unsorted = createLinkedList(new int[]{5, 2, 8, 1, 3});
        System.out.println("原始链表: ");
        printList(unsorted);
        
        // 排序链表（简化版）
        ListNode sorted = sortLinkedList(unsorted);
        System.out.println("排序后链表: ");
        printList(sorted);
        
        // 反转排序后的链表（降序）
        ListNode reversed = reverseIterative(sorted);
        System.out.println("降序排列: ");
        printList(reversed);
    }
    
    // 简化版链表排序（冒泡排序-仅用于演示）
    private static ListNode sortLinkedList(ListNode head) {
        if (head == null || head.next == null) return head;
        
        boolean swapped;
        do {
            swapped = false;
            ListNode current = head;
            ListNode prev = null;
            
            while (current != null && current.next != null) {
                if (current.val > current.next.val) {
                    // 交换节点值
                    int temp = current.val;
                    current.val = current.next.val;
                    current.next.val = temp;
                    swapped = true;
                }
                prev = current;
                current = current.next;
            }
        } while (swapped);
        
        return head;
    }
}