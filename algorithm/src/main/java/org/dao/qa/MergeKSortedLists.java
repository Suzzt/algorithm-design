package main.java.org.dao.qa;

import java.util.*;

/**
 * 合并K个升序链表问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个链表数组，每个链表都已经按升序排列。
 * 将所有链表合并到一个升序链表中，返回合并后的链表。
 * 
 * <p><b>示例</b>:
 * 输入: lists = [[1,4,5],[1,3,4],[2,6]]
 * 输出: [1,1,2,3,4,4,5,6]
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第23题)
 * 
 * <p><b>解题思路</b>:
 * 1. 顺序合并: 遍历所有链表逐一合并，时间复杂度O(k²n)
 * 2. 分治合并: 两两合并链表直到剩下一个，时间复杂度O(knlogk)
 * 3. 优先级队列: 使用最小堆实时获取最小节点，时间复杂度O(knlogk)
 * 4. 暴力解法: 收集所有节点排序重建链表，时间复杂度O(knlogkn)
 * 
 * <p><b>时间复杂度</b>:
 *  顺序合并: O(k²n)
 *  分治合并: O(knlogk)
 *  优先级队列: O(knlogk)
 *  暴力解法: O(knlog(kn))
 * 
 * <p><b>空间复杂度</b>:
 *  顺序合并: O(1)
 *  分治合并: O(logk) 递归栈空间
 *  优先级队列: O(k)
 *  暴力解法: O(kn)
 * 
 * <p><b>应用场景</b>:
 * 1. 多路归并排序
 * 2. 大数据处理中的外部排序
 * 3. 分布式系统中的结果合并
 * 4. 日志系统的时间序列合并
 * 5. 流处理系统中的数据聚合
 */

public class MergeKSortedLists {
    
    // 链表节点定义
    static class ListNode {
        int val;
        ListNode next;
        
        ListNode(int val) {
            this.val = val;
        }
        
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
    
    // ========================= 核心解法1: 优先级队列(最小堆) =========================
    
    /**
     * 优先级队列解法 - 最优方法
     * 
     * @param lists 链表数组
     * @return 合并后链表
     */
    public static ListNode mergeKListsHeap(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        
        // 创建最小堆 - 按节点值排序
        PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> a.val - b.val);
        
        // 初始化: 所有链表的头节点入堆
        for (ListNode node : lists) {
            if (node != null) {
                heap.offer(node);
            }
        }
        
        // 虚拟头节点，方便操作
        ListNode dummy = new ListNode(-1);
        ListNode current = dummy;
        
        while (!heap.isEmpty()) {
            // 取出最小节点
            ListNode min = heap.poll();
            current.next = min;
            current = current.next;
            
            // 如果最小节点有下一个节点，加入堆中
            if (min.next != null) {
                heap.offer(min.next);
            }
        }
        
        return dummy.next;
    }
    
    // ========================= 核心解法2: 分治合并法 =========================
    
    /**
     * 分治合并法 - 空间效率高
     * 
     * @param lists 链表数组
     * @return 合并后链表
     */
    public static ListNode mergeKListsDivide(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return merge(lists, 0, lists.length - 1);
    }
    
    // 分治合并主体方法
    private static ListNode merge(ListNode[] lists, int left, int right) {
        if (left == right) return lists[left];
        
        int mid = left + (right - left) / 2;
        ListNode l1 = merge(lists, left, mid);
        ListNode l2 = merge(lists, mid + 1, right);
        return mergeTwoLists(l1, l2);
    }
    
    // 合并两个链表 - 常规方法
    private static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode current = dummy;
        
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                current.next = l1;
                l1 = l1.next;
            } else {
                current.next = l2;
                l2 = l2.next;
            }
            current = current.next;
        }
        
        current.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }
    
    // ========================= 核心解法3: 顺序合并法 =========================
    
    /**
     * 顺序合并法 - 简单直接
     * 
     * @param lists 链表数组
     * @return 合并后链表
     */
    public static ListNode mergeKListsSequential(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        
        ListNode result = lists[0];
        for (int i = 1; i < lists.length; i++) {
            result = mergeTwoLists(result, lists[i]);
        }
        return result;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 多路归并排序
     * 
     * @param arrays 有序数组组成的数组
     * @return 合并后的有序数组
     */
    public static int[] mergeKSortedArrays(int[][] arrays) {
        // 转换为链表数组
        ListNode[] lists = new ListNode[arrays.length];
        for (int i = 0; i < arrays.length; i++) {
            lists[i] = arrayToList(arrays[i]);
        }
        
        // 合并链表
        ListNode merged = mergeKListsHeap(lists);
        
        // 转换为数组
        return listToArray(merged);
    }
    
    /**
     * 外部排序模拟器
     * 
     * @param streams 多个有序数据流
     * @return 合并后的数据流
     */
    public static Iterator<Integer> externalSort(Queue<Integer>[] streams) {
        // 转换为链表数组
        ListNode[] lists = new ListNode[streams.length];
        for (int i = 0; i < streams.length; i++) {
            lists[i] = streamToList(new PriorityQueue<>(streams[i]));
        }
        
        // 合并链表
        ListNode merged = mergeKListsDivide(lists);
        
        // 转换为迭代器
        return listToIterator(merged);
    }
    
    /**
     * 流处理系统结果合并器
     * 
     * @param results 多处理器计算结果
     * @return 合并后的最终结果
     */
    public static List<Integer> aggregateResults(List<List<Integer>> results) {
        // 转换为链表数组
        ListNode[] lists = new ListNode[results.size()];
        for (int i = 0; i < results.size(); i++) {
            List<Integer> list = new ArrayList<>(results.get(i));
            Collections.sort(list); // 确保有序
            lists[i] = arrayToList(list.stream().mapToInt(n -> n).toArray());
        }
        
        // 合并链表
        ListNode merged = mergeKListsHeap(lists);
        
        // 转换为List
        return listToArrayList(merged);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化链表合并过程
     * 
     * @param lists 链表数组
     * @param method 合并方法 (1-堆, 2-分治, 3-顺序)
     */
    public static void visualizeMerge(ListNode[] lists, int method) {
        System.out.println("\n======== 链表合并可视化 ========");
        System.out.println("输入链表:");
        printLists(lists);
        
        ListNode result = null;
        switch (method) {
            case 1:
                System.out.println("\n使用优先级队列(最小堆)合并");
                result = mergeKListsHeap(lists);
                break;
            case 2:
                System.out.println("\n使用分治合并法");
                result = mergeKListsDivide(lists);
                break;
            case 3:
                System.out.println("\n使用顺序合并法");
                result = mergeKListsSequential(lists);
                break;
        }
        
        System.out.println("\n合并结果链表:");
        printList(result);
        
        if (method == 1) {
            visualizeHeapProcess(lists);
        }
    }
    
    /**
     * 可视化堆合并过程
     * 
     * @param lists 链表数组
     */
    private static void visualizeHeapProcess(ListNode[] lists) {
        System.out.println("\n最小堆操作过程:");
        PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> a.val - b.val);
        
        // 初始状态
        for (int i = 0; i < lists.length; i++) {
            if (lists[i] != null) {
                heap.offer(lists[i]);
            }
        }
        
        System.out.println("初始堆状态: " + heapToString(heap));
        
        // 逐步弹出元素
        ListNode dummy = new ListNode(-1);
        ListNode current = dummy;
        int step = 1;
        
        while (!heap.isEmpty()) {
            ListNode min = heap.poll();
            current.next = min;
            current = current.next;
            
            System.out.printf("步骤%d: 弹出 %d, ", step++, min.val);
            if (min.next != null) {
                System.out.printf("添加 %d, ", min.next.val);
                heap.offer(min.next);
            } else {
                System.out.printf("无新元素添加, ");
            }
            System.out.println("堆状态: " + heapToString(heap));
        }
    }
    
    // 打印链表数组
    public static void printLists(ListNode[] lists) {
        for (int i = 0; i < lists.length; i++) {
            System.out.printf("链表%d: ", i);
            printList(lists[i]);
        }
    }
    
    // 打印单个链表
    public static void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val);
            if (head.next != null) System.out.print("→");
            head = head.next;
        }
        System.out.println();
    }
    
    // 堆内容转字符串
    private static String heapToString(PriorityQueue<ListNode> heap) {
        if (heap.isEmpty()) return "[]";
        
        List<Integer> values = new ArrayList<>();
        for (ListNode node : heap.toArray(new ListNode[0])) {
            values.add(node.val);
        }
        Collections.sort(values);
        return values.toString();
    }
    
    // ========================= 辅助方法 =========================
    
    // 数组转链表
    public static ListNode arrayToList(int[] arr) {
        ListNode dummy = new ListNode(-1);
        ListNode curr = dummy;
        for (int num : arr) {
            curr.next = new ListNode(num);
            curr = curr.next;
        }
        return dummy.next;
    }
    
    // 链表转数组
    public static int[] listToArray(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        return list.stream().mapToInt(i -> i).toArray();
    }
    
    // 流转换为链表
    private static ListNode streamToList(Queue<Integer> stream) {
        ListNode dummy = new ListNode(-1);
        ListNode curr = dummy;
        while (!stream.isEmpty()) {
            curr.next = new ListNode(stream.poll());
            curr = curr.next;
        }
        return dummy.next;
    }
    
    // 链表转迭代器
    private static Iterator<Integer> listToIterator(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        return list.iterator();
    }
    
    // 链表转ArrayList
    private static List<Integer> listToArrayList(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        return list;
    }
    
    // ========================= 性能比较工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param k 链表数量
     * @param n 每个链表长度
     */
    public static void comparePerformance(int k, int n) {
        // 生成测试数据
        ListNode[] testData = generateTestLists(k, n);
        ListNode[] copy1 = Arrays.copyOf(testData, testData.length);
        ListNode[] copy2 = Arrays.copyOf(testData, testData.length);
        ListNode[] copy3 = Arrays.copyOf(testData, testData.length);
        
        // 测试1: 优先级队列
        long start = System.nanoTime();
        ListNode result1 = mergeKListsHeap(copy1);
        long time1 = System.nanoTime() - start;
        
        // 测试2: 分治合并
        start = System.nanoTime();
        ListNode result2 = mergeKListsDivide(copy2);
        long time2 = System.nanoTime() - start;
        
        // 测试3: 顺序合并
        start = System.nanoTime();
        ListNode result3 = mergeKListsSequential(copy3);
        long time3 = System.nanoTime() - start;
        
        // 验证结果一致性
        boolean consistent = validateResults(result1, result2) && 
                            validateResults(result2, result3);
        
        System.out.println("\n性能比较测试:");
        System.out.printf("%d个链表，每个约%d个节点\n", k, n);
        System.out.println("=======================================");
        System.out.println("方法             | 耗时(ns)     | 结果一致");
        System.out.println("-----------------|--------------|-----------");
        System.out.printf("优先级队列       | %12d | %s\n", time1, consistent);
        System.out.printf("分治合并法       | %12d | %s\n", time2, consistent);
        System.out.printf("顺序合并法       | %12d | %s\n", time3, consistent);
        System.out.println("=======================================");
    }
    
    // 生成测试链表
    private static ListNode[] generateTestLists(int k, int n) {
        ListNode[] lists = new ListNode[k];
        Random random = new Random();
        
        for (int i = 0; i < k; i++) {
            int[] arr = new int[n];
            int start = random.nextInt(100);
            for (int j = 0; j < n; j++) {
                arr[j] = start + j * 10 + random.nextInt(5);
            }
            lists[i] = arrayToList(arr);
        }
        return lists;
    }
    
    // 验证结果一致性
    private static boolean validateResults(ListNode list1, ListNode list2) {
        while (list1 != null && list2 != null) {
            if (list1.val != list2.val) return false;
            list1 = list1.next;
            list2 = list2.next;
        }
        return list1 == null && list2 == null;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testEdgeCases();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        // 创建三个链表
        ListNode l1 = arrayToList(new int[]{1, 4, 5});
        ListNode l2 = arrayToList(new int[]{1, 3, 4});
        ListNode l3 = arrayToList(new int[]{2, 6});
        ListNode[] lists = {l1, l2, l3};
        
        System.out.println("原始链表:");
        printLists(lists);
        
        System.out.println("\n优先级队列合并结果:");
        printList(mergeKListsHeap(lists));
        
        // 恢复列表
        lists = new ListNode[]{l1, l2, l3};
        System.out.println("\n分治合并结果:");
        printList(mergeKListsDivide(lists));
        
        // 恢复列表
        lists = new ListNode[]{l1, l2, l3};
        System.out.println("\n顺序合并结果:");
        printList(mergeKListsSequential(lists));
        
        // 可视化过程
        lists = new ListNode[]{l1, l2, l3};
        visualizeMerge(lists, 1);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        // 空数组
        ListNode[] empty = {};
        System.out.println("空数组合并测试: " + (mergeKListsHeap(empty) == null ? "通过" : "失败"));
        
        // 包含空链表
        ListNode[] withNull = {arrayToList(new int[]{1, 2}), null, arrayToList(new int[]{3, 4})};
        System.out.println("\n含空链表合并测试:");
        printList(mergeKListsHeap(withNull));
        
        // 单链表
        ListNode[] single = {arrayToList(new int[]{5, 10, 15})};
        System.out.println("\n单链表合并测试:");
        printList(mergeKListsHeap(single));
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        // 小规模测试
        comparePerformance(10, 50);
        
        // 大规模测试
        comparePerformance(100, 500);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 多路归并排序
        int[][] arrays = {
            {1, 4, 7, 10},
            {2, 5, 8, 11},
            {3, 6, 9, 12}
        };
        System.out.println("\n多路归并排序结果:");
        System.out.println(Arrays.toString(mergeKSortedArrays(arrays)));
        
        // 场景2: 外部排序模拟
        Queue<Integer>[] streams = new Queue[] {
            new PriorityQueue<>(Arrays.asList(1, 3, 5, 7)),
            new PriorityQueue<>(Arrays.asList(2, 4, 6, 8)),
            new PriorityQueue<>(Arrays.asList(0, 9, 10))
        };
        System.out.println("\n外部排序模拟结果:");
        Iterator<Integer> sorted = externalSort(streams);
        while (sorted.hasNext()) {
            System.out.print(sorted.next() + " ");
        }
        System.out.println();
        
        // 场景3: 流处理结果合并
        List<List<Integer>> results = Arrays.asList(
                Arrays.asList(10, 30, 50),
                Arrays.asList(20, 40, 60),
                Arrays.asList(15, 25, 35)
        );
        System.out.println("\n流处理结果合并:");
        System.out.println(aggregateResults(results));
    }
}