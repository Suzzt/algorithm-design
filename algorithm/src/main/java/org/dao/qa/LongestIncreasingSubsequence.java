package main.java.org.dao.qa;

import java.util.*;

/**
 * 最长递增子序列问题（Longest Increasing Subsequence, LIS）
 * 
 * <p><b>问题描述</b>:
 * 给定一个整数数组，找到其中最长严格递增子序列的长度。子序列不要求连续，但要求相对顺序不变。
 * 
 * <p><b>示例</b>:
 * 输入: [10,9,2,5,3,7,101,18]
 * 输出: 4 
 * 解释: 最长递增子序列是 [2,3,7,101]，长度为4。
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第300题)
 * 
 * <p><b>解题思路</b>:
 * 1. 动态规划: O(n²)解法，dp[i]表示以nums[i]结尾的LIS长度
 * 2. 贪心+二分查找: O(n log n)解法，维护一个递增序列
 * 3. 分治法: 结合归并排序的思路
 * 4. 树状数组/线段树: 处理更大规模数据
 * 
 * <p><b>时间复杂度</b>:
 *  动态规划: O(n²)
 *  贪心+二分: O(n log n)
 * 
 * <p><b>应用场景</b>:
 * 1. 股票价格分析（最长增长趋势）
 * 2. DNA序列分析
 * 3. 路径规划（寻找最长可行路径）
 * 4. 游戏AI（行为序列优化）
 * 5. 数据压缩（寻找有序模式）
 */

public class LongestIncreasingSubsequence {

    // ========================= 解法1: 动态规划 =========================
    
    /**
     * 动态规划解法
     * 
     * @param nums 输入数组
     * @return 最长递增子序列长度
     */
    public static int lengthOfLISDP(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int maxLen = 1;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        
        return maxLen;
    }
    
    /**
     * 获取最长递增子序列（动态规划回溯）
     * 
     * @param nums 输入数组
     * @return 最长递增子序列
     */
    public static List<Integer> getLISDP(int[] nums) {
        if (nums == null || nums.length == 0) return new ArrayList<>();
        
        int n = nums.length;
        int[] dp = new int[n];
        int[] prev = new int[n]; // 记录前驱索引
        Arrays.fill(dp, 1);
        Arrays.fill(prev, -1);
        
        int maxIndex = 0;
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
            if (dp[i] > dp[maxIndex]) {
                maxIndex = i;
            }
        }
        
        // 回溯构建LIS
        LinkedList<Integer> lis = new LinkedList<>();
        while (maxIndex != -1) {
            lis.addFirst(nums[maxIndex]);
            maxIndex = prev[maxIndex];
        }
        
        return lis;
    }
    
    // ========================= 解法2: 贪心+二分查找 =========================
    
    /**
     * 贪心+二分查找解法
     * 
     * @param nums 输入数组
     * @return 最长递增子序列长度
     */
    public static int lengthOfLISBinary(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        List<Integer> tails = new ArrayList<>();
        tails.add(nums[0]);
        
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > tails.get(tails.size() - 1)) {
                tails.add(nums[i]);
            } else {
                // 二分查找插入位置
                int pos = binarySearch(tails, nums[i]);
                tails.set(pos, nums[i]);
            }
        }
        
        return tails.size();
    }
    
    /**
     * 获取最长递增子序列（贪心+二分回溯）
     * 
     * @param nums 输入数组
     * @return 最长递增子序列
     */
    public static List<Integer> getLISBinary(int[] nums) {
        if (nums == null || nums.length == 0) return new ArrayList<>();
        
        int n = nums.length;
        int[] tails = new int[n]; // 存储长度为i+1的LIS的最小尾部值
        int[] indices = new int[n]; // 存储tails中元素在原数组中的索引
        int[] prev = new int[n]; // 记录前驱索引
        Arrays.fill(prev, -1);
        
        int len = 0; // 当前LIS长度
        
        for (int i = 0; i < n; i++) {
            int left = 0, right = len;
            // 二分查找插入位置
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < nums[i]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            tails[left] = nums[i];
            indices[left] = i;
            
            // 记录前驱（如果不是第一个元素）
            if (left > 0) {
                prev[i] = indices[left - 1];
            }
            
            if (left == len) {
                len++;
            }
        }
        
        // 回溯构建LIS
        LinkedList<Integer> lis = new LinkedList<>();
        int cur = indices[len - 1];
        while (cur != -1) {
            lis.addFirst(nums[cur]);
            cur = prev[cur];
        }
        
        return lis;
    }
    
    // 二分查找辅助方法
    private static int binarySearch(List<Integer> list, int target) {
        int left = 0, right = list.size() - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
    
    // ========================= 解法3: 分治法 =========================
    
    /**
     * 分治解法（结合归并排序思想）
     * 
     * @param nums 输入数组
     * @return 最长递增子序列长度
     */
    public static int lengthOfLISDivide(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        return divideAndConquer(nums, 0, nums.length - 1).size();
    }
    
    private static List<Integer> divideAndConquer(int[] nums, int left, int right) {
        if (left == right) {
            List<Integer> list = new ArrayList<>();
            list.add(nums[left]);
            return list;
        }
        
        int mid = left + (right - left) / 2;
        List<Integer> leftList = divideAndConquer(nums, left, mid);
        List<Integer> rightList = divideAndConquer(nums, mid + 1, right);
        
        return mergeLIS(leftList, rightList);
    }
    
    // 合并两个递增子序列
    private static List<Integer> mergeLIS(List<Integer> list1, List<Integer> list2) {
        List<Integer> merged = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i) < list2.get(j)) {
                merged.add(list1.get(i++));
            } else {
                merged.add(list2.get(j++));
            }
        }
        
        while (i < list1.size()) merged.add(list1.get(i++));
        while (j < list2.size()) merged.add(list2.get(j++));
        
        // 只保留递增部分
        List<Integer> lis = new ArrayList<>();
        int last = Integer.MIN_VALUE;
        for (int num : merged) {
            if (num > last) {
                lis.add(num);
                last = num;
            }
        }
        
        return lis;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化动态规划过程
     * 
     * @param nums 输入数组
     */
    public static void visualizeDP(int[] nums) {
        if (nums == null || nums.length == 0) return;
        
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        System.out.println("\n动态规划过程:");
        System.out.println("索引: " + arrayToString(nums));
        System.out.println("DP值: [1]");
        
        for (int i = 1; i < n; i++) {
            System.out.printf("\n处理 nums[%d] = %d\n", i, nums[i]);
            System.out.println("检查之前的元素:");
            
            for (int j = 0; j < i; j++) {
                String relation = nums[i] > nums[j] ? ">" : "<=";
                System.out.printf("  nums[%d]=%d %s nums[%d]=%d", 
                                 j, nums[j], relation, i, nums[i]);
                
                if (nums[i] > nums[j]) {
                    System.out.printf(" → 更新 dp[%d] = max(%d, %d+1) = %d\n", 
                                     i, dp[i], dp[j], Math.max(dp[i], dp[j] + 1));
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                } else {
                    System.out.println(" → 不更新");
                }
            }
            
            System.out.println("当前DP数组: " + Arrays.toString(Arrays.copyOf(dp, i+1)));
        }
        
        System.out.println("\n最终DP数组: " + Arrays.toString(dp));
        System.out.println("最长递增子序列长度: " + Arrays.stream(dp).max().getAsInt());
    }
    
    /**
     * 可视化贪心+二分过程
     * 
     * @param nums 输入数组
     */
    public static void visualizeBinary(int[] nums) {
        if (nums == null || nums.length == 0) return;
        
        List<Integer> tails = new ArrayList<>();
        tails.add(nums[0]);
        
        System.out.println("\n贪心+二分过程:");
        System.out.println("初始化 tails: [" + nums[0] + "]");
        
        for (int i = 1; i < nums.length; i++) {
            System.out.printf("\n处理 nums[%d] = %d\n", i, nums[i]);
            int last = tails.get(tails.size() - 1);
            
            if (nums[i] > last) {
                tails.add(nums[i]);
                System.out.printf("  %d > %d (尾部) → 添加到尾部\n", nums[i], last);
                System.out.println("  tails: " + tails);
            } else {
                // 二分查找过程可视化
                System.out.println("  二分查找插入位置:");
                int left = 0, right = tails.size() - 1;
                int steps = 1;
                
                while (left < right) {
                    int mid = left + (right - left) / 2;
                    System.out.printf("    步骤%d: left=%d, mid=%d, right=%d\n", 
                                     steps++, left, mid, right);
                    System.out.printf("    比较 tails[%d]=%d 和 %d\n", mid, tails.get(mid), nums[i]);
                    
                    if (tails.get(mid) < nums[i]) {
                        System.out.printf("    %d < %d → 向右查找 [left = mid+1 = %d]\n", 
                                         tails.get(mid), nums[i], mid+1);
                        left = mid + 1;
                    } else {
                        System.out.printf("    %d >= %d → 向左查找 [right = mid = %d]\n", 
                                         tails.get(mid), nums[i], mid);
                        right = mid;
                    }
                }
                
                System.out.printf("  找到位置: %d (原值: %d)\n", left, tails.get(left));
                tails.set(left, nums[i]);
                System.out.println("  更新后 tails: " + tails);
            }
        }
        
        System.out.println("\n最终 tails: " + tails);
        System.out.println("最长递增子序列长度: " + tails.size());
    }
    
    // ========================= 应用场景 =========================
    
    /**
     * 股票趋势分析
     * 
     * @param prices 股票价格数组
     * @return 最长增长趋势
     */
    public static List<Integer> stockTrendAnalysis(int[] prices) {
        List<Integer> trend = getLISBinary(prices);
        System.out.println("\n股票价格趋势分析:");
        System.out.println("价格序列: " + arrayToString(prices));
        System.out.println("最长增长趋势: " + trend);
        System.out.println("趋势长度: " + trend.size());
        return trend;
    }
    
    /**
     * 路径规划 - 寻找最长可行路径
     * 
     * @param elevations 海拔高度数组
     * @return 最长上升路径
     */
    public static List<Integer> pathPlanning(int[] elevations) {
        List<Integer> path = getLISBinary(elevations);
        System.out.println("\n路径规划:");
        System.out.println("海拔序列: " + arrayToString(elevations));
        System.out.println("最长上升路径: " + path);
        System.out.println("路径长度: " + path.size());
        return path;
    }
    
    /**
     * 游戏AI行为序列优化
     * 
     * @param actions 行为序列（用数值表示）
     * @return 最优行为序列
     */
    public static List<Integer> optimizeGameAI(int[] actions) {
        List<Integer> optimalSeq = getLISBinary(actions);
        System.out.println("\n游戏AI行为优化:");
        System.out.println("原始行为序列: " + arrayToString(actions));
        System.out.println("优化后行为序列: " + optimalSeq);
        return optimalSeq;
    }
    
    /**
     * DNA序列分析 - 寻找最长保守序列
     * 
     * @param sequences DNA序列数组（数值表示）
     * @return 最长保守序列
     */
    public static List<Integer> dnaConservativeSequence(int[] sequences) {
        List<Integer> conservativeSeq = getLISBinary(sequences);
        System.out.println("\nDNA序列分析:");
        System.out.println("DNA序列: " + arrayToString(sequences));
        System.out.println("最长保守序列: " + conservativeSeq);
        return conservativeSeq;
    }
    
    // ========================= 工具方法 =========================
    
    // 数组转字符串
    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testComplexCases();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        int[] nums1 = {10, 9, 2, 5, 3, 7, 101, 18};
        testLIS(nums1, 4);
        
        int[] nums2 = {0, 1, 0, 3, 2, 3};
        testLIS(nums2, 4);
        
        int[] nums3 = {7, 7, 7, 7, 7};
        testLIS(nums3, 1);
    }
    
    private static void testLIS(int[] nums, int expected) {
        System.out.println("\n输入: " + arrayToString(nums));
        
        int dpLen = lengthOfLISDP(nums);
        int binaryLen = lengthOfLISBinary(nums);
        int divideLen = lengthOfLISDivide(nums);
        
        System.out.println("DP解法: " + dpLen);
        System.out.println("贪心+二分: " + binaryLen);
        System.out.println("分治法: " + divideLen);
        
        boolean pass = dpLen == expected && binaryLen == expected && divideLen == expected;
        System.out.println("状态: " + (pass ? "✅" : "❌"));
        
        // 可视化小规模测试
        if (nums.length <= 10) {
            visualizeDP(nums);
            visualizeBinary(nums);
            
            System.out.println("DP回溯序列: " + getLISDP(nums));
            System.out.println("二分回溯序列: " + getLISBinary(nums));
        }
    }
    
    private static void testComplexCases() {
        System.out.println("\n====== 复杂测试 ======");
        
        // 完全递增序列
        int[] increasing = new int[20];
        for (int i = 0; i < increasing.length; i++) {
            increasing[i] = i;
        }
        testLIS(increasing, increasing.length);
        
        // 完全递减序列
        int[] decreasing = new int[20];
        for (int i = 0; i < decreasing.length; i++) {
            decreasing[i] = decreasing.length - i;
        }
        testLIS(decreasing, 1);
        
        // 随机序列
        Random rand = new Random();
        int[] randomArray = new int[15];
        for (int i = 0; i < randomArray.length; i++) {
            randomArray[i] = rand.nextInt(100);
        }
        System.out.println("\n随机序列测试: " + arrayToString(randomArray));
        testLIS(randomArray, -1); // 不验证结果
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 生成大规模数据
        int size = 100000;
        int[] largeArray = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            largeArray[i] = rand.nextInt(1000000);
        }
        
        System.out.println("数据规模: " + size);
        
        long start, end;
        
        // 贪心+二分法
        start = System.currentTimeMillis();
        int binaryLen = lengthOfLISBinary(largeArray);
        end = System.currentTimeMillis();
        System.out.printf("贪心+二分法: %d (耗时: %d ms)\n", binaryLen, end - start);
        
        // 分治法
        start = System.currentTimeMillis();
        int divideLen = lengthOfLISDivide(largeArray);
        end = System.currentTimeMillis();
        System.out.printf("分治法: %d (耗时: %d ms)\n", divideLen, end - start);
        
        // 动态规划（小规模测试）
        if (size <= 10000) {
            start = System.currentTimeMillis();
            int dpLen = lengthOfLISDP(largeArray);
            end = System.currentTimeMillis();
            System.out.printf("动态规划: %d (耗时: %d ms)\n", dpLen, end - start);
        }
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 股票趋势分析
        int[] stockPrices = {100, 105, 102, 110, 115, 108, 120, 118, 125};
        stockTrendAnalysis(stockPrices);
        
        // 场景2: 路径规划
        int[] elevations = {500, 550, 520, 600, 580, 650, 620, 700, 680};
        pathPlanning(elevations);
        
        // 场景3: 游戏AI行为优化
        int[] gameActions = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        optimizeGameAI(gameActions);
        
        // 场景4: DNA序列分析
        int[] dnaSequence = {5, 2, 8, 6, 3, 6, 9, 7, 9, 4, 5};
        dnaConservativeSequence(dnaSequence);
    }
}