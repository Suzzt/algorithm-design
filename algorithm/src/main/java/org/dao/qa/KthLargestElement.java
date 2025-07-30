package main.java.org.dao.qa;

import java.util.*;

/**
 * 数组中第K大元素问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个整数数组 nums 和一个整数 k，返回数组中第 k 个最大的元素。
 * 注意是排序后的第 k 大元素，而不是第 k 个不同的元素。
 * 
 * <p><b>示例</b>:
 * 输入: nums = [3,2,1,5,6,4], k = 2
 * 输出: 5
 * 解释: 排序后数组为 [6,5,4,3,2,1]，第2大元素是5
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第215题)
 * 
 * <p><b>解题思路</b>:
 * 1. 快速选择法: 基于快排思想，平均时间复杂度O(n)，最坏O(n²)
 * 2. 堆排序法: 使用最小堆，时间复杂度O(n log k)，空间复杂度O(k)
 * 3. 排序法: 直接排序取值，时间复杂度O(n log n)
 * 4. 计数排序法: 适用于数值范围有限的情况，时间复杂度O(n+range)
 * 
 * <p><b>时间复杂度</b>:
 *  快速选择法: 平均O(n)，最坏O(n²)
 *  堆排序法: O(n log k)
 *  排序法: O(n log n)
 *  计数排序法: O(n + range)
 * 
 * <p><b>空间复杂度</b>:
 *  快速选择法: O(1)
 *  堆排序法: O(k)
 *  排序法: O(1) 或 O(n) 看排序算法
 *  计数排序法: O(range)
 * 
 * <p><b>应用场景</b>:
 * 1. 大数据处理中的Top-K问题
 * 2. 数据库查询优化
 * 3. 搜索引擎结果排序
 * 4. 实时数据流的统计分析
 * 5. 游戏排行榜系统
 */

public class KthLargestElement {
    
    // ========================= 解法1: 快速选择法 (推荐) =========================
    
    /**
     * 快速选择算法 - 基于快排思想
     * 
     * @param nums 输入数组
     * @param k 第k大元素
     * @return 第k大的元素值
     */
    public static int findKthLargestQuickSelect(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        
        // 第k大元素在排序数组中的索引为 nums.length - k
        return quickSelect(nums, 0, nums.length - 1, nums.length - k);
    }
    
    private static int quickSelect(int[] nums, int left, int right, int targetIndex) {
        if (left == right) {
            return nums[left];
        }
        
        // 随机化避免最坏情况
        int randomIndex = left + new Random().nextInt(right - left + 1);
        swap(nums, randomIndex, right);
        
        int pivotIndex = partition(nums, left, right);
        
        if (pivotIndex == targetIndex) {
            return nums[pivotIndex];
        } else if (pivotIndex > targetIndex) {
            return quickSelect(nums, left, pivotIndex - 1, targetIndex);
        } else {
            return quickSelect(nums, pivotIndex + 1, right, targetIndex);
        }
    }
    
    // Lomuto分区方案
    private static int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int i = left;
        
        for (int j = left; j < right; j++) {
            if (nums[j] <= pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        swap(nums, i, right);
        return i;
    }
    
    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    // ========================= 解法2: 堆排序法 =========================
    
    /**
     * 最小堆解法 - 维护大小为k的最小堆
     * 
     * @param nums 输入数组
     * @param k 第k大元素
     * @return 第k大的元素值
     */
    public static int findKthLargestMinHeap(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
        
        for (int num : nums) {
            if (minHeap.size() < k) {
                minHeap.offer(num);
            } else if (num > minHeap.peek()) {
                minHeap.poll();
                minHeap.offer(num);
            }
        }
        
        return minHeap.peek();
    }
    
    /**
     * 最大堆解法 - 构建最大堆然后取k次
     * 
     * @param nums 输入数组
     * @param k 第k大元素
     * @return 第k大的元素值
     */
    public static int findKthLargestMaxHeap(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        
        for (int num : nums) {
            maxHeap.offer(num);
        }
        
        int result = 0;
        for (int i = 0; i < k; i++) {
            result = maxHeap.poll();
        }
        
        return result;
    }
    
    // ========================= 解法3: 排序法 =========================
    
    /**
     * 直接排序解法
     * 
     * @param nums 输入数组
     * @param k 第k大元素
     * @return 第k大的元素值  
     */
    public static int findKthLargestSort(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        
        Arrays.sort(nums);
        return nums[nums.length - k];
    }
    
    // ========================= 解法4: 计数排序法 =========================
    
    /**
     * 计数排序解法 - 适用于数值范围有限的情况
     * 
     * @param nums 输入数组
     * @param k 第k大元素
     * @return 第k大的元素值
     */
    public static int findKthLargestCountingSort(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
        
        // 找到数组的最大值和最小值
        int min = Arrays.stream(nums).min().getAsInt();
        int max = Arrays.stream(nums).max().getAsInt();
        
        // 如果范围太大，不适合用计数排序
        if (max - min > 100000) {
            return findKthLargestQuickSelect(nums, k);
        }
        
        int[] count = new int[max - min + 1];
        
        // 计数
        for (int num : nums) {
            count[num - min]++;
        }
        
        // 从大到小遍历，找第k大元素
        int remaining = k;
        for (int i = count.length - 1; i >= 0; i--) {
            remaining -= count[i];
            if (remaining <= 0) {
                return i + min;
            }
        }
        
        return -1; // 不应该到达这里
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 数据流中的第K大元素 - 支持动态添加
     */
    public static class KthLargestInStream {
        private final int k;
        private final PriorityQueue<Integer> minHeap;
        
        public KthLargestInStream(int k, int[] nums) {
            this.k = k;
            this.minHeap = new PriorityQueue<>(k);
            
            for (int num : nums) {
                add(num);
            }
        }
        
        public int add(int val) {
            if (minHeap.size() < k) {
                minHeap.offer(val);
            } else if (val > minHeap.peek()) {
                minHeap.poll();
                minHeap.offer(val);
            }
            
            return minHeap.peek();
        }
        
        public int getKthLargest() {
            return minHeap.isEmpty() ? -1 : minHeap.peek();
        }
    }
    
    /**
     * Top-K问题 - 找到前K大的所有元素
     * 
     * @param nums 输入数组
     * @param k 前k大元素个数
     * @return 前k大元素列表（降序）
     */
    public static List<Integer> findTopKLargest(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new ArrayList<>();
        }
        
        k = Math.min(k, nums.length);
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
        
        for (int num : nums) {
            if (minHeap.size() < k) {
                minHeap.offer(num);
            } else if (num > minHeap.peek()) {
                minHeap.poll();
                minHeap.offer(num);
            }
        }
        
        List<Integer> result = new ArrayList<>(minHeap);
        result.sort(Collections.reverseOrder());
        return result;
    }
    
    /**
     * 游戏排行榜系统
     * 
     * @param scores 玩家分数数组
     * @param k 排行榜显示前k名
     * @return 排行榜信息
     */
    public static Map<String, Object> generateLeaderboard(int[] scores, int k) {
        Map<String, Object> leaderboard = new HashMap<>();
        
        if (scores == null || scores.length == 0 || k <= 0) {
            leaderboard.put("topScores", new ArrayList<>());
            leaderboard.put("kthScore", -1);
            leaderboard.put("totalPlayers", 0);
            return leaderboard;
        }
        
        List<Integer> topScores = findTopKLargest(scores, k);
        int kthScore = k <= scores.length ? findKthLargestMinHeap(scores, k) : -1;
        
        leaderboard.put("topScores", topScores);
        leaderboard.put("kthScore", kthScore);
        leaderboard.put("totalPlayers", scores.length);
        leaderboard.put("averageScore", Arrays.stream(scores).average().orElse(0.0));
        
        return leaderboard;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化快速选择过程
     * 
     * @param nums 输入数组
     * @param k 第k大元素
     */
    public static void visualizeQuickSelect(int[] nums, int k) {
        System.out.println("\n快速选择过程可视化:");
        System.out.println("原数组: " + Arrays.toString(nums));
        System.out.println("查找第 " + k + " 大元素");
        System.out.println("目标索引: " + (nums.length - k));
        
        int[] workingArray = nums.clone();
        System.out.println("\n步骤 | 左边界 | 右边界 | 枢轴值 | 枢轴索引 | 数组状态");
        System.out.println("-----|--------|--------|--------|----------|------------------");
        
        visualizeQuickSelectRecursive(workingArray, 0, workingArray.length - 1, 
                                    workingArray.length - k, 1);
    }
    
    private static int visualizeQuickSelectRecursive(int[] nums, int left, int right, 
                                                   int targetIndex, int step) {
        if (left == right) {
            System.out.printf("%4d | %6d | %6d | %6d | %8d | %s (找到!)\n", 
                             step, left, right, nums[left], left, Arrays.toString(nums));
            return nums[left];
        }
        
        // 随机化
        int randomIndex = left + new Random().nextInt(right - left + 1);
        swap(nums, randomIndex, right);
        
        int pivotValue = nums[right];
        int pivotIndex = partitionVisualize(nums, left, right);
        
        System.out.printf("%4d | %6d | %6d | %6d | %8d | %s\n", 
                         step, left, right, pivotValue, pivotIndex, Arrays.toString(nums));
        
        if (pivotIndex == targetIndex) {
            System.out.printf("%4s | %6s | %6s | %6s | %8s | 目标索引匹配!\n", 
                             "", "", "", "", "");
            return nums[pivotIndex];
        } else if (pivotIndex > targetIndex) {
            System.out.printf("%4s | %6s | %6s | %6s | %8s | 在左半部分继续\n", 
                             "", "", "", "", "");
            return visualizeQuickSelectRecursive(nums, left, pivotIndex - 1, targetIndex, step + 1);
        } else {
            System.out.printf("%4s | %6s | %6s | %6s | %8s | 在右半部分继续\n", 
                             "", "", "", "", "");
            return visualizeQuickSelectRecursive(nums, pivotIndex + 1, right, targetIndex, step + 1);
        }
    }
    
    private static int partitionVisualize(int[] nums, int left, int right) {
        int pivot = nums[right];
        int i = left;
        
        for (int j = left; j < right; j++) {
            if (nums[j] <= pivot) {
                swap(nums, i, j);
                i++;
            }
        }
        swap(nums, i, right);
        return i;
    }
    
    /**
     * 可视化堆的构建过程
     * 
     * @param nums 输入数组
     * @param k 第k大元素
     */
    public static void visualizeHeapProcess(int[] nums, int k) {
        System.out.println("\n最小堆过程可视化:");
        System.out.println("原数组: " + Arrays.toString(nums));
        System.out.println("维护大小为 " + k + " 的最小堆");
        
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
        
        System.out.println("\n步骤 | 当前元素 | 堆大小 | 堆顶元素 | 操作 | 堆状态");
        System.out.println("-----|----------|--------|----------|------|------------------");
        
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            String operation;
            
            if (minHeap.size() < k) {
                minHeap.offer(num);
                operation = "直接加入";
            } else if (num > minHeap.peek()) {
                minHeap.poll();
                minHeap.offer(num);
                operation = "替换堆顶";
            } else {
                operation = "跳过";
            }
            
            System.out.printf("%4d | %8d | %6d | %8s | %4s | %s\n", 
                             i + 1, num, minHeap.size(), 
                             minHeap.isEmpty() ? "null" : minHeap.peek().toString(),
                             operation, minHeap.toString());
        }
        
        System.out.println("\n第 " + k + " 大元素为: " + minHeap.peek());
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param size 数组大小
     * @param k 第k大元素
     * @param range 数值范围
     */
    public static void comparePerformance(int size, int k, int range) {
        // 生成测试数据
        int[] nums = generateRandomArray(size, -range, range);
        
        long start, end;
        
        // 测试快速选择法
        int[] nums1 = nums.clone();
        start = System.nanoTime();
        int result1 = findKthLargestQuickSelect(nums1, k);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试最小堆法
        int[] nums2 = nums.clone();
        start = System.nanoTime();
        int result2 = findKthLargestMinHeap(nums2, k);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试排序法
        int[] nums3 = nums.clone();
        start = System.nanoTime();
        int result3 = findKthLargestSort(nums3, k);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试计数排序法
        int[] nums4 = nums.clone();
        start = System.nanoTime();
        int result4 = findKthLargestCountingSort(nums4, k);
        end = System.nanoTime();
        long time4 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("数组大小: %d, k: %d, 数值范围: [-%d, %d]\n", size, k, range, range);
        System.out.println("===============================================");
        System.out.println("方法            | 时间(ms)   | 结果 | 相对速度");
        System.out.println("----------------|------------|------|----------");
        System.out.printf("快速选择法      | %.6f | %4d | 基准\n", time1 / 1_000_000.0, result1);
        System.out.printf("最小堆法        | %.6f | %4d | %.2fx\n", time2 / 1_000_000.0, result2, (double)time2/time1);
        System.out.printf("排序法          | %.6f | %4d | %.2fx\n", time3 / 1_000_000.0, result3, (double)time3/time1);
        System.out.printf("计数排序法      | %.6f | %4d | %.2fx\n", time4 / 1_000_000.0, result4, (double)time4/time1);
        System.out.println("===============================================");
        
        // 验证结果一致性
        boolean consistent = (result1 == result2) && (result2 == result3) && (result3 == result4);
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
    }
    
    // 生成随机数组
    private static int[] generateRandomArray(int size, int min, int max) {
        Random rand = new Random();
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = rand.nextInt(max - min + 1) + min;
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
        testCase("示例测试1", new int[]{3,2,1,5,6,4}, 2, 5);
        testCase("示例测试2", new int[]{3,2,3,1,2,4,5,5,6}, 4, 4);
        testCase("单元素数组", new int[]{1}, 1, 1);
        testCase("两元素数组", new int[]{3,1}, 1, 3);
        testCase("重复元素", new int[]{1,1,1,1}, 2, 1);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("k=1最大值", new int[]{7,2,9,1,5}, 1, 9);
        testCase("k=n最小值", new int[]{7,2,9,1,5}, 5, 1);
        testCase("负数数组", new int[]{-1,-3,-2,-5,-4}, 2, -2);
        testCase("混合数组", new int[]{-3,0,1,-2,4}, 3, 0);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 数据流处理
        System.out.println("\n数据流中的第K大元素:");
        int[] initialNums = {4, 5, 8, 2};
        KthLargestInStream stream = new KthLargestInStream(3, initialNums);
        System.out.println("初始化后第3大元素: " + stream.getKthLargest());
        System.out.println("添加3后第3大元素: " + stream.add(3));
        System.out.println("添加9后第3大元素: " + stream.add(9));
        System.out.println("添加4后第3大元素: " + stream.add(4));
        
        // 场景2: Top-K问题
        System.out.println("\nTop-K问题:");
        int[] scores = {95, 87, 92, 78, 85, 90, 88, 96, 82, 91};
        System.out.println("所有分数: " + Arrays.toString(scores));
        System.out.println("前5名分数: " + findTopKLargest(scores, 5));
        
        // 场景3: 游戏排行榜
        System.out.println("\n游戏排行榜:");
        int[] gameScores = {1500, 2300, 1800, 2100, 1950, 2250, 1750, 2050};
        Map<String, Object> leaderboard = generateLeaderboard(gameScores, 3);
        System.out.println("排行榜信息: " + leaderboard);
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        int[] demoArray = {3, 2, 1, 5, 6, 4};
        visualizeQuickSelect(demoArray, 2);
        visualizeHeapProcess(demoArray, 2);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100, 10, 100);
        comparePerformance(1000, 100, 1000);
        comparePerformance(10000, 1000, 10000);
    }
    
    private static void testCase(String name, int[] nums, int k, int expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("数组: " + Arrays.toString(nums));
        System.out.println("k: " + k);
        
        int result1 = findKthLargestQuickSelect(nums.clone(), k);
        int result2 = findKthLargestMinHeap(nums.clone(), k);
        int result3 = findKthLargestSort(nums.clone(), k);
        int result4 = findKthLargestCountingSort(nums.clone(), k);
        
        System.out.println("快速选择法结果: " + result1);
        System.out.println("最小堆法结果: " + result2);
        System.out.println("排序法结果: " + result3);
        System.out.println("计数排序法结果: " + result4);
        System.out.println("期望结果: " + expected);
        
        boolean isCorrect = (result1 == expected) && (result2 == expected) && 
                           (result3 == expected) && (result4 == expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小规模数组展示可视化
        if (nums.length <= 10) {
            visualizeQuickSelect(nums.clone(), k);
            visualizeHeapProcess(nums.clone(), k);
        }
    }
}