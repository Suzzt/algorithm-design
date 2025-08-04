package main.java.org.dao.qa;

import java.util.*;

/**
 * 寻找两个正序数组的中位数问题
 * 
 * <p><b>问题描述</b>:
 * 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2，
 * 请找出这两个正序数组的中位数。要求算法的时间复杂度为 O(log(m+n))。
 * 
 * <p><b>示例</b>:
 * 输入: nums1 = [1,3], nums2 = [2]
 * 输出: 2.0
 * 解释: 合并数组 = [1,2,3]，中位数是 2
 * 
 * 输入: nums1 = [1,2], nums2 = [3,4]
 * 输出: 2.5
 * 解释: 合并数组 = [1,2,3,4]，中位数是 (2+3)/2 = 2.5
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第4题)
 * 
 * <p><b>解题思路</b>:
 * 1. 二分查找法: 对较短数组进行二分查找，时间复杂度O(log(min(m,n)))
 * 2. 递归法: 转化为寻找第k小元素问题，时间复杂度O(log(m+n))
 * 3. 归并法: 合并两个有序数组，时间复杂度O(m+n)
 * 4. 双指针法: 不完全合并，只找到中位数位置，时间复杂度O(m+n)
 * 
 * <p><b>时间复杂度</b>:
 *  二分查找法: O(log(min(m,n)))
 *  递归法: O(log(m+n))
 *  归并法: O(m+n)
 *  双指针法: O(m+n)
 * 
 * <p><b>空间复杂度</b>:
 *  二分查找法: O(1)
 *  递归法: O(log(m+n)) 递归栈
 *  归并法: O(m+n)
 *  双指针法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 大数据处理中的统计分析
 * 2. 数据库查询优化
 * 3. 分布式系统中的数据合并
 * 4. 实时数据流的中位数计算
 * 5. 机器学习中的特征工程
 */

public class MedianOfTwoSortedArrays {
    
    // ========================= 解法1: 二分查找法 (推荐) =========================
    
    /**
     * 二分查找法 - 最优解法
     * 核心思想：在较短数组上进行二分查找，找到合适的分割点，
     * 使得左半部分的最大值 <= 右半部分的最小值
     * 
     * @param nums1 第一个有序数组
     * @param nums2 第二个有序数组
     * @return 两个数组的中位数
     */
    public static double findMedianSortedArraysBinarySearch(int[] nums1, int[] nums2) {
        // 确保nums1是较短的数组，减少二分查找的复杂度
        if (nums1.length > nums2.length) {
            return findMedianSortedArraysBinarySearch(nums2, nums1);
        }
        
        int m = nums1.length;
        int n = nums2.length;
        int totalLeft = (m + n + 1) / 2; // 左半部分的元素个数
        
        // 在nums1上进行二分查找
        int left = 0, right = m;
        
        while (left < right) {
            int i = left + (right - left + 1) / 2; // nums1的分割点
            int j = totalLeft - i; // nums2的分割点
            
            // 如果nums1[i-1] > nums2[j]，说明i太大了，需要向左移动
            if (nums1[i - 1] > nums2[j]) {
                right = i - 1;
            } else {
                left = i;
            }
        }
        
        int i = left;
        int j = totalLeft - i;
        
        // 计算左半部分的最大值
        int nums1LeftMax = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
        int nums2LeftMax = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
        int leftMax = Math.max(nums1LeftMax, nums2LeftMax);
        
        // 如果总长度是奇数，直接返回左半部分的最大值
        if ((m + n) % 2 == 1) {
            return leftMax;
        }
        
        // 计算右半部分的最小值
        int nums1RightMin = (i == m) ? Integer.MAX_VALUE : nums1[i];
        int nums2RightMin = (j == n) ? Integer.MAX_VALUE : nums2[j];
        int rightMin = Math.min(nums1RightMin, nums2RightMin);
        
        // 总长度是偶数，返回左右两部分的平均值
        return (leftMax + rightMin) / 2.0;
    }
    
    // ========================= 解法2: 递归法 =========================
    
    /**
     * 递归法 - 转化为寻找第k小元素
     * 
     * @param nums1 第一个有序数组
     * @param nums2 第二个有序数组
     * @return 两个数组的中位数
     */
    public static double findMedianSortedArraysRecursive(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int total = m + n;
        
        if (total % 2 == 1) {
            // 奇数长度，找第(total+1)/2小的元素
            return findKthElement(nums1, 0, nums2, 0, total / 2 + 1);
        } else {
            // 偶数长度，找第total/2和total/2+1小的元素
            int left = findKthElement(nums1, 0, nums2, 0, total / 2);
            int right = findKthElement(nums1, 0, nums2, 0, total / 2 + 1);
            return (left + right) / 2.0;
        }
    }
    
    /**
     * 递归查找第k小的元素
     * 
     * @param nums1 数组1
     * @param start1 数组1的起始位置
     * @param nums2 数组2
     * @param start2 数组2的起始位置
     * @param k 第k小（从1开始）
     * @return 第k小的元素值
     */
    private static int findKthElement(int[] nums1, int start1, int[] nums2, int start2, int k) {
        // 边界条件：其中一个数组已经用完
        if (start1 >= nums1.length) {
            return nums2[start2 + k - 1];
        }
        if (start2 >= nums2.length) {
            return nums1[start1 + k - 1];
        }
        
        // 如果k=1，返回两个数组当前位置的较小值
        if (k == 1) {
            return Math.min(nums1[start1], nums2[start2]);
        }
        
        // 二分查找：比较两个数组的第k/2个元素
        int half = k / 2;
        int nums1Mid = start1 + half - 1;
        int nums2Mid = start2 + half - 1;
        
        int nums1Val = (nums1Mid >= nums1.length) ? Integer.MAX_VALUE : nums1[nums1Mid];
        int nums2Val = (nums2Mid >= nums2.length) ? Integer.MAX_VALUE : nums2[nums2Mid];
        
        if (nums1Val < nums2Val) {
            // 排除nums1的前k/2个元素
            return findKthElement(nums1, nums1Mid + 1, nums2, start2, k - half);
        } else {
            // 排除nums2的前k/2个元素
            return findKthElement(nums1, start1, nums2, nums2Mid + 1, k - half);
        }
    }
    
    // ========================= 解法3: 归并法 =========================
    
    /**
     * 归并法 - 完全合并两个数组
     * 
     * @param nums1 第一个有序数组
     * @param nums2 第二个有序数组
     * @return 两个数组的中位数
     */
    public static double findMedianSortedArraysMerge(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int[] merged = new int[m + n];
        
        int i = 0, j = 0, k = 0;
        
        // 合并两个有序数组
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                merged[k++] = nums1[i++];
            } else {
                merged[k++] = nums2[j++];
            }
        }
        
        // 处理剩余元素
        while (i < m) {
            merged[k++] = nums1[i++];
        }
        while (j < n) {
            merged[k++] = nums2[j++];
        }
        
        // 计算中位数
        int total = m + n;
        if (total % 2 == 1) {
            return merged[total / 2];
        } else {
            return (merged[total / 2 - 1] + merged[total / 2]) / 2.0;
        }
    }
    
    // ========================= 解法4: 双指针法 =========================
    
    /**
     * 双指针法 - 不完全合并，只找到中位数位置
     * 
     * @param nums1 第一个有序数组
     * @param nums2 第二个有序数组
     * @return 两个数组的中位数
     */
    public static double findMedianSortedArraysTwoPointers(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int total = m + n;
        
        int i = 0, j = 0;
        int prev = 0, curr = 0;
        
        // 只需要找到中位数位置即可
        for (int count = 0; count <= total / 2; count++) {
            prev = curr;
            
            if (i < m && (j >= n || nums1[i] <= nums2[j])) {
                curr = nums1[i++];
            } else {
                curr = nums2[j++];
            }
        }
        
        if (total % 2 == 1) {
            return curr;
        } else {
            return (prev + curr) / 2.0;
        }
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 多个有序数组的中位数
     * 
     * @param arrays 多个有序数组
     * @return 所有数组合并后的中位数
     */
    public static double findMedianMultipleArrays(int[][] arrays) {
        if (arrays == null || arrays.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        // 使用最小堆合并多个有序数组
        PriorityQueue<ArrayElement> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a.value, b.value)
        );
        
        int totalElements = 0;
        
        // 初始化堆，每个数组的第一个元素入堆
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) {
                minHeap.offer(new ArrayElement(arrays[i][0], i, 0));
                totalElements += arrays[i].length;
            }
        }
        
        if (totalElements == 0) {
            throw new IllegalArgumentException("所有数组都为空");
        }
        
        int targetIndex1 = (totalElements - 1) / 2;
        int targetIndex2 = totalElements / 2;
        int currentIndex = 0;
        int median1 = 0, median2 = 0;
        
        while (!minHeap.isEmpty()) {
            ArrayElement element = minHeap.poll();
            
            if (currentIndex == targetIndex1) {
                median1 = element.value;
            }
            if (currentIndex == targetIndex2) {
                median2 = element.value;
                break;
            }
            
            // 如果当前数组还有下一个元素，加入堆中
            if (element.index + 1 < arrays[element.arrayIndex].length) {
                minHeap.offer(new ArrayElement(
                    arrays[element.arrayIndex][element.index + 1],
                    element.arrayIndex,
                    element.index + 1
                ));
            }
            
            currentIndex++;
        }
        
        return totalElements % 2 == 1 ? median2 : (median1 + median2) / 2.0;
    }
    
    // 辅助类：用于多数组合并
    private static class ArrayElement {
        int value;
        int arrayIndex;
        int index;
        
        ArrayElement(int value, int arrayIndex, int index) {
            this.value = value;
            this.arrayIndex = arrayIndex;
            this.index = index;
        }
    }
    
    /**
     * 数据流中的中位数 - 支持动态添加元素
     */
    public static class MedianFinder {
        private PriorityQueue<Integer> maxHeap; // 存储较小的一半
        private PriorityQueue<Integer> minHeap; // 存储较大的一半
        
        public MedianFinder() {
            maxHeap = new PriorityQueue<>(Collections.reverseOrder());
            minHeap = new PriorityQueue<>();
        }
        
        public void addNum(int num) {
            if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
                maxHeap.offer(num);
            } else {
                minHeap.offer(num);
            }
            
            // 平衡两个堆的大小
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.offer(maxHeap.poll());
            } else if (minHeap.size() > maxHeap.size() + 1) {
                maxHeap.offer(minHeap.poll());
            }
        }
        
        public double findMedian() {
            if (maxHeap.size() == minHeap.size()) {
                return (maxHeap.peek() + minHeap.peek()) / 2.0;
            } else {
                return maxHeap.size() > minHeap.size() ? maxHeap.peek() : minHeap.peek();
            }
        }
        
        public int size() {
            return maxHeap.size() + minHeap.size();
        }
    }
    
    /**
     * 分布式系统中的中位数计算
     * 
     * @param partitions 各个分区的有序数据
     * @return 全局中位数
     */
    public static double findDistributedMedian(List<int[]> partitions) {
        // 移除空分区
        partitions = partitions.stream()
                              .filter(partition -> partition.length > 0)
                              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        if (partitions.isEmpty()) {
            throw new IllegalArgumentException("所有分区都为空");
        }
        
        if (partitions.size() == 1) {
            int[] single = partitions.get(0);
            int n = single.length;
            if (n % 2 == 1) {
                return single[n / 2];
            } else {
                return (single[n / 2 - 1] + single[n / 2]) / 2.0;
            }
        }
        
        // 对于多个分区，使用归并的方式
        int[][] arrays = partitions.toArray(new int[0][]);
        return findMedianMultipleArrays(arrays);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化二分查找过程
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     */
    public static void visualizeBinarySearch(int[] nums1, int[] nums2) {
        System.out.println("\n二分查找过程可视化:");
        System.out.println("数组1: " + Arrays.toString(nums1));
        System.out.println("数组2: " + Arrays.toString(nums2));
        
        if (nums1.length > nums2.length) {
            int[] temp = nums1;
            nums1 = nums2;
            nums2 = temp;
            System.out.println("交换数组，确保数组1较短");
        }
        
        int m = nums1.length;
        int n = nums2.length;
        int totalLeft = (m + n + 1) / 2;
        
        System.out.printf("总长度: %d, 左半部分需要: %d 个元素\n", m + n, totalLeft);
        System.out.println("\n步骤 | left | right | i | j | nums1左 | nums1右 | nums2左 | nums2右 | 操作");
        System.out.println("-----|------|-------|---|---|---------|---------|---------|---------|----------");
        
        int left = 0, right = m;
        int step = 1;
        
        while (left < right) {
            int i = left + (right - left + 1) / 2;
            int j = totalLeft - i;
            
            String nums1Left = (i == 0) ? "MIN" : String.valueOf(nums1[i - 1]);
            String nums1Right = (i == m) ? "MAX" : String.valueOf(nums1[i]);
            String nums2Left = (j == 0) ? "MIN" : String.valueOf(nums2[j - 1]);
            String nums2Right = (j == n) ? "MAX" : String.valueOf(nums2[j]);
            
            String operation;
            if (i > 0 && j < n && nums1[i - 1] > nums2[j]) {
                operation = "i太大，向左";
                right = i - 1;
            } else {
                operation = "继续";
                left = i;
            }
            
            System.out.printf("%4d | %4d | %5d | %d | %d | %7s | %7s | %7s | %7s | %s\n",
                             step++, left, right, i, j, nums1Left, nums1Right, nums2Left, nums2Right, operation);
        }
        
        int i = left;
        int j = totalLeft - i;
        
        int nums1LeftMax = (i == 0) ? Integer.MIN_VALUE : nums1[i - 1];
        int nums2LeftMax = (j == 0) ? Integer.MIN_VALUE : nums2[j - 1];
        int leftMax = Math.max(nums1LeftMax, nums2LeftMax);
        
        System.out.printf("\n最终分割: i=%d, j=%d\n", i, j);
        System.out.printf("左半部分最大值: %d\n", leftMax);
        
        if ((m + n) % 2 == 1) {
            System.out.printf("中位数: %.1f\n", (double)leftMax);
        } else {
            int nums1RightMin = (i == m) ? Integer.MAX_VALUE : nums1[i];
            int nums2RightMin = (j == n) ? Integer.MAX_VALUE : nums2[j];
            int rightMin = Math.min(nums1RightMin, nums2RightMin);
            System.out.printf("右半部分最小值: %d\n", rightMin);
            System.out.printf("中位数: %.1f\n", (leftMax + rightMin) / 2.0);
        }
    }
    
    /**
     * 可视化归并过程
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     */
    public static void visualizeMergeProcess(int[] nums1, int[] nums2) {
        System.out.println("\n归并过程可视化:");
        System.out.println("数组1: " + Arrays.toString(nums1));
        System.out.println("数组2: " + Arrays.toString(nums2));
        
        int m = nums1.length;
        int n = nums2.length;
        List<Integer> merged = new ArrayList<>();
        
        int i = 0, j = 0;
        System.out.println("\n步骤 | i | j | 选择元素 | 合并数组");
        System.out.println("-----|---|---|----------|------------------");
        
        int step = 1;
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                merged.add(nums1[i]);
                System.out.printf("%4d | %d | %d | %8d | %s\n", 
                                 step++, i, j, nums1[i], merged.toString());
                i++;
            } else {
                merged.add(nums2[j]);
                System.out.printf("%4d | %d | %d | %8d | %s\n", 
                                 step++, i, j, nums2[j], merged.toString());
                j++;
            }
        }
        
        while (i < m) {
            merged.add(nums1[i]);
            System.out.printf("%4d | %d | %d | %8d | %s\n", 
                             step++, i, j, nums1[i], merged.toString());
            i++;
        }
        
        while (j < n) {
            merged.add(nums2[j]);
            System.out.printf("%4d | %d | %d | %8d | %s\n", 
                             step++, i, j, nums2[j], merged.toString());
            j++;
        }
        
        int total = m + n;
        if (total % 2 == 1) {
            System.out.printf("\n中位数位置: %d, 中位数: %d\n", total / 2, merged.get(total / 2));
        } else {
            int left = merged.get(total / 2 - 1);
            int right = merged.get(total / 2);
            System.out.printf("\n中位数位置: %d和%d, 中位数: %.1f\n", 
                             total / 2 - 1, total / 2, (left + right) / 2.0);
        }
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param size1 第一个数组大小
     * @param size2 第二个数组大小
     */
    public static void comparePerformance(int size1, int size2) {
        // 生成测试数据
        int[] nums1 = generateSortedArray(size1, 1, 100);
        int[] nums2 = generateSortedArray(size2, 1, 100);
        
        long start, end;
        
        // 测试二分查找法
        start = System.nanoTime();
        double result1 = findMedianSortedArraysBinarySearch(nums1, nums2);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试递归法
        start = System.nanoTime();
        double result2 = findMedianSortedArraysRecursive(nums1, nums2);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试归并法
        start = System.nanoTime();
        double result3 = findMedianSortedArraysMerge(nums1, nums2);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试双指针法
        start = System.nanoTime();
        double result4 = findMedianSortedArraysTwoPointers(nums1, nums2);
        end = System.nanoTime();
        long time4 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("数组大小: [%d, %d]\n", size1, size2);
        System.out.println("=============================================");
        System.out.println("方法            | 时间(ms)   | 结果  | 相对速度");
        System.out.println("----------------|------------|-------|----------");
        System.out.printf("二分查找法      | %.6f | %.2f | 基准\n", time1 / 1_000_000.0, result1);
        System.out.printf("递归法          | %.6f | %.2f | %.2fx\n", time2 / 1_000_000.0, result2, (double)time2/time1);
        System.out.printf("归并法          | %.6f | %.2f | %.2fx\n", time3 / 1_000_000.0, result3, (double)time3/time1);
        System.out.printf("双指针法        | %.6f | %.2f | %.2fx\n", time4 / 1_000_000.0, result4, (double)time4/time1);
        System.out.println("=============================================");
        
        // 验证结果一致性
        boolean consistent = Math.abs(result1 - result2) < 1e-9 && 
                           Math.abs(result2 - result3) < 1e-9 && 
                           Math.abs(result3 - result4) < 1e-9;
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
    }
    
    // 生成有序数组
    private static int[] generateSortedArray(int size, int min, int max) {
        Random rand = new Random();
        int[] nums = new int[size];
        
        int current = min;
        for (int i = 0; i < size; i++) {
            nums[i] = current;
            current += rand.nextInt(3) + 1; // 每次增加1-3
            if (current > max) current = max;
        }
        
        return nums;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testEdgeCases();
        testApplicationScenarios();
        testPerformance();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        testCase("示例1", new int[]{1, 3}, new int[]{2}, 2.0);
        testCase("示例2", new int[]{1, 2}, new int[]{3, 4}, 2.5);
        testCase("不同长度", new int[]{1, 3, 5}, new int[]{2, 4, 6, 7, 8}, 4.0);
        testCase("重复元素", new int[]{1, 1, 2}, new int[]{1, 2, 3}, 1.5);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("空数组1", new int[]{}, new int[]{1}, 1.0);
        testCase("空数组2", new int[]{2}, new int[]{}, 2.0);
        testCase("单元素", new int[]{1}, new int[]{2}, 1.5);
        testCase("完全分离", new int[]{1, 2, 3}, new int[]{4, 5, 6}, 3.5);
        testCase("负数", new int[]{-5, -1}, new int[]{-3, 0, 2}, -1.0);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 多个数组的中位数
        System.out.println("\n多个有序数组的中位数:");
        int[][] multipleArrays = {
            {1, 4, 7},
            {2, 5, 8},
            {3, 6, 9}
        };
        System.out.println("数组组: " + Arrays.deepToString(multipleArrays));
        double multiMedian = findMedianMultipleArrays(multipleArrays);
        System.out.println("中位数: " + multiMedian);
        
        // 场景2: 数据流中的中位数
        System.out.println("\n数据流中的中位数:");
        MedianFinder medianFinder = new MedianFinder();
        int[] stream = {5, 15, 1, 3, 8, 7, 9, 2, 6, 10, 11, 4, 12, 13, 14};
        for (int num : stream) {
            medianFinder.addNum(num);
            System.out.printf("添加 %2d 后，中位数: %.1f (共%d个元素)\n", 
                             num, medianFinder.findMedian(), medianFinder.size());
        }
        
        // 场景3: 分布式系统
        System.out.println("\n分布式系统中位数计算:");
        List<int[]> partitions = Arrays.asList(
            new int[]{1, 5, 9},
            new int[]{2, 6, 10},
            new int[]{3, 7, 11},
            new int[]{4, 8, 12}
        );
        System.out.println("分区数据: " + partitions.stream()
                                                   .map(Arrays::toString)
                                                   .reduce((a, b) -> a + ", " + b)
                                                   .orElse(""));
        double distributedMedian = findDistributedMedian(partitions);
        System.out.println("分布式中位数: " + distributedMedian);
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        visualizeBinarySearch(new int[]{1, 3}, new int[]{2, 4});
        visualizeMergeProcess(new int[]{1, 3, 5}, new int[]{2, 4, 6});
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100, 150);
        comparePerformance(1000, 1500);
        comparePerformance(10000, 15000);
    }
    
    private static void testCase(String name, int[] nums1, int[] nums2, double expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("数组1: " + Arrays.toString(nums1));
        System.out.println("数组2: " + Arrays.toString(nums2));
        
        double result1 = findMedianSortedArraysBinarySearch(nums1, nums2);
        double result2 = findMedianSortedArraysRecursive(nums1, nums2);
        double result3 = findMedianSortedArraysMerge(nums1, nums2);
        double result4 = findMedianSortedArraysTwoPointers(nums1, nums2);
        
        System.out.printf("二分查找法结果: %.2f\n", result1);
        System.out.printf("递归法结果: %.2f\n", result2);
        System.out.printf("归并法结果: %.2f\n", result3);
        System.out.printf("双指针法结果: %.2f\n", result4);
        System.out.printf("期望结果: %.2f\n", expected);
        
        boolean isCorrect = Math.abs(result1 - expected) < 1e-9 && 
                           Math.abs(result2 - expected) < 1e-9 && 
                           Math.abs(result3 - expected) < 1e-9 && 
                           Math.abs(result4 - expected) < 1e-9;
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小数组展示可视化
        if (nums1.length + nums2.length <= 10) {
            visualizeBinarySearch(nums1, nums2);
        }
    }
}