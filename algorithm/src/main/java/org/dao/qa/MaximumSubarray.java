package main.java.org.dao.qa;

import java.util.*;

/**
 * 最大子数组和问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个整数数组 nums，找到一个具有最大和的连续子数组（子数组最少包含一个元素），
 * 返回其最大和。同时支持返回子数组的起始和结束位置。
 * 
 * <p><b>示例</b>:
 * 输入: nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出: 6
 * 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第53题)
 * 
 * <p><b>解题思路</b>:
 * 1. Kadane算法: 动态规划思想，维护当前最大和，时间复杂度O(n)
 * 2. 分治法: 分而治之，递归处理左右两部分，时间复杂度O(n log n)
 * 3. 暴力法: 枚举所有可能的子数组，时间复杂度O(n²)
 * 4. 前缀和法: 利用前缀和数组优化计算，时间复杂度O(n²)
 * 
 * <p><b>时间复杂度</b>:
 *  Kadane算法: O(n)
 *  分治法: O(n log n)
 *  暴力法: O(n²)
 *  前缀和法: O(n²)
 * 
 * <p><b>空间复杂度</b>:
 *  Kadane算法: O(1)
 *  分治法: O(log n) 递归栈
 *  暴力法: O(1)
 *  前缀和法: O(n)
 * 
 * <p><b>应用场景</b>:
 * 1. 股票买卖最大收益计算
 * 2. 信号处理中的最大能量段识别
 * 3. 数据挖掘中的异常检测
 * 4. 游戏开发中的最优策略选择
 * 5. 金融风险分析和投资组合优化
 */

public class MaximumSubarray {
    
    // 用于存储子数组信息的结果类
    public static class SubarrayResult {
        public int maxSum;
        public int startIndex;
        public int endIndex;
        public int[] subarray;
        
        public SubarrayResult(int maxSum, int startIndex, int endIndex, int[] originalArray) {
            this.maxSum = maxSum;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            if (startIndex >= 0 && endIndex >= 0 && startIndex <= endIndex) {
                this.subarray = Arrays.copyOfRange(originalArray, startIndex, endIndex + 1);
            } else {
                this.subarray = new int[0];
            }
        }
        
        @Override
        public String toString() {
            return String.format("MaxSum: %d, Range: [%d, %d], Subarray: %s", 
                               maxSum, startIndex, endIndex, Arrays.toString(subarray));
        }
    }
    
    // ========================= 解法1: Kadane算法 (推荐) =========================
    
    /**
     * Kadane算法 - 经典动态规划解法
     * 
     * @param nums 输入数组
     * @return 最大子数组和
     */
    public static int maxSubArrayKadane(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        int maxSoFar = nums[0];  // 全局最大值
        int maxEndingHere = nums[0];  // 当前位置最大值
        
        for (int i = 1; i < nums.length; i++) {
            // 状态转移：要么继续之前的子数组，要么重新开始
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }
        
        return maxSoFar;
    }
    
    /**
     * Kadane算法 - 返回详细信息（包含子数组位置）
     * 
     * @param nums 输入数组
     * @return 包含最大和、起始和结束位置的结果
     */
    public static SubarrayResult maxSubArrayKadaneDetailed(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        int maxSoFar = nums[0];
        int maxEndingHere = nums[0];
        int start = 0, end = 0, tempStart = 0;
        
        for (int i = 1; i < nums.length; i++) {
            if (maxEndingHere < 0) {
                maxEndingHere = nums[i];
                tempStart = i;
            } else {
                maxEndingHere += nums[i];
            }
            
            if (maxEndingHere > maxSoFar) {
                maxSoFar = maxEndingHere;
                start = tempStart;
                end = i;
            }
        }
        
        return new SubarrayResult(maxSoFar, start, end, nums);
    }
    
    // ========================= 解法2: 分治法 =========================
    
    /**
     * 分治法解决最大子数组和问题
     * 
     * @param nums 输入数组
     * @return 最大子数组和
     */
    public static int maxSubArrayDivideConquer(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        return maxSubArrayDivideConquerHelper(nums, 0, nums.length - 1);
    }
    
    private static int maxSubArrayDivideConquerHelper(int[] nums, int left, int right) {
        if (left == right) {
            return nums[left];
        }
        
        int mid = left + (right - left) / 2;
        
        // 递归求解左半部分和右半部分
        int leftMax = maxSubArrayDivideConquerHelper(nums, left, mid);
        int rightMax = maxSubArrayDivideConquerHelper(nums, mid + 1, right);
        
        // 计算跨越中点的最大子数组和
        int crossMax = maxCrossingSum(nums, left, mid, right);
        
        // 返回三者中的最大值
        return Math.max(Math.max(leftMax, rightMax), crossMax);
    }
    
    // 计算跨越中点的最大子数组和
    private static int maxCrossingSum(int[] nums, int left, int mid, int right) {
        // 计算左半部分的最大和（包含mid）
        int leftSum = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = mid; i >= left; i--) {
            sum += nums[i];
            leftSum = Math.max(leftSum, sum);
        }
        
        // 计算右半部分的最大和（包含mid+1）
        int rightSum = Integer.MIN_VALUE;
        sum = 0;
        for (int i = mid + 1; i <= right; i++) {
            sum += nums[i];
            rightSum = Math.max(rightSum, sum);
        }
        
        return leftSum + rightSum;
    }
    
    // ========================= 解法3: 暴力法 =========================
    
    /**
     * 暴力法 - 枚举所有可能的子数组
     * 
     * @param nums 输入数组
     * @return 最大子数组和详细信息
     */
    public static SubarrayResult maxSubArrayBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        int maxSum = Integer.MIN_VALUE;
        int bestStart = 0, bestEnd = 0;
        
        for (int i = 0; i < nums.length; i++) {
            int currentSum = 0;
            for (int j = i; j < nums.length; j++) {
                currentSum += nums[j];
                if (currentSum > maxSum) {
                    maxSum = currentSum;
                    bestStart = i;
                    bestEnd = j;
                }
            }
        }
        
        return new SubarrayResult(maxSum, bestStart, bestEnd, nums);
    }
    
    // ========================= 解法4: 前缀和法 =========================
    
    /**
     * 前缀和优化法
     * 
     * @param nums 输入数组
     * @return 最大子数组和详细信息
     */
    public static SubarrayResult maxSubArrayPrefixSum(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        // 构建前缀和数组
        int[] prefixSum = new int[nums.length + 1];
        for (int i = 0; i < nums.length; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        int maxSum = Integer.MIN_VALUE;
        int bestStart = 0, bestEnd = 0;
        
        for (int i = 0; i < nums.length; i++) {
            for (int j = i; j < nums.length; j++) {
                int currentSum = prefixSum[j + 1] - prefixSum[i];
                if (currentSum > maxSum) {
                    maxSum = currentSum;
                    bestStart = i;
                    bestEnd = j;
                }
            }
        }
        
        return new SubarrayResult(maxSum, bestStart, bestEnd, nums);
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 股票交易最大收益计算
     * 
     * @param prices 股票价格数组
     * @return 最大收益信息
     */
    public static Map<String, Object> calculateMaxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            Map<String, Object> result = new HashMap<>();
            result.put("maxProfit", 0);
            result.put("buyDay", -1);
            result.put("sellDay", -1);
            result.put("message", "价格数据不足，无法进行交易");
            return result;
        }
        
        // 将价格数组转换为价格变化数组
        int[] priceChanges = new int[prices.length - 1];
        for (int i = 0; i < priceChanges.length; i++) {
            priceChanges[i] = prices[i + 1] - prices[i];
        }
        
        // 使用Kadane算法找到最大收益段
        SubarrayResult result = maxSubArrayKadaneDetailed(priceChanges);
        
        Map<String, Object> tradingResult = new HashMap<>();
        tradingResult.put("maxProfit", Math.max(0, result.maxSum));
        tradingResult.put("buyDay", result.startIndex);
        tradingResult.put("sellDay", result.endIndex + 1);
        tradingResult.put("buyPrice", result.startIndex >= 0 ? prices[result.startIndex] : 0);
        tradingResult.put("sellPrice", result.endIndex + 1 < prices.length ? prices[result.endIndex + 1] : 0);
        tradingResult.put("priceChanges", Arrays.toString(result.subarray));
        
        return tradingResult;
    }
    
    /**
     * 信号处理 - 找到最大能量段
     * 
     * @param signal 信号强度数组
     * @param threshold 能量阈值
     * @return 最大能量段信息
     */
    public static Map<String, Object> findMaxEnergySegment(double[] signal, double threshold) {
        // 转换为整数数组便于处理
        int[] intSignal = Arrays.stream(signal)
                                .mapToInt(x -> (int)(x * 1000)) // 放大1000倍避免精度损失
                                .toArray();
        
        SubarrayResult result = maxSubArrayKadaneDetailed(intSignal);
        
        Map<String, Object> energyResult = new HashMap<>();
        energyResult.put("maxEnergy", result.maxSum / 1000.0); // 还原原始比例
        energyResult.put("startTime", result.startIndex);
        energyResult.put("endTime", result.endIndex);
        energyResult.put("duration", result.endIndex - result.startIndex + 1);
        energyResult.put("aboveThreshold", (result.maxSum / 1000.0) > threshold);
        energyResult.put("segment", Arrays.stream(result.subarray)
                                         .mapToDouble(x -> x / 1000.0)
                                         .toArray());
        
        return energyResult;
    }
    
    /**
     * 游戏开发 - 最优连击段计算
     * 
     * @param scores 每次攻击的得分数组
     * @return 最优连击段信息
     */
    public static Map<String, Object> calculateBestCombo(int[] scores) {
        if (scores == null || scores.length == 0) {
            return Collections.singletonMap("message", "没有攻击数据");
        }
        
        SubarrayResult result = maxSubArrayKadaneDetailed(scores);
        
        Map<String, Object> comboResult = new HashMap<>();
        comboResult.put("maxComboScore", result.maxSum);
        comboResult.put("comboStart", result.startIndex);
        comboResult.put("comboEnd", result.endIndex);
        comboResult.put("comboLength", result.endIndex - result.startIndex + 1);
        comboResult.put("comboSequence", result.subarray);
        comboResult.put("averageScore", result.subarray.length > 0 ? 
                       (double)result.maxSum / result.subarray.length : 0);
        
        return comboResult;
    }
    
    // ========================= 扩展问题：环形数组最大子数组和 =========================
    
    /**
     * 环形数组的最大子数组和
     * 
     * @param nums 环形数组
     * @return 最大子数组和
     */
    public static int maxSubArrayCircular(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        // 情况1: 最大子数组在中间（非环形）
        int maxKadane = maxSubArrayKadane(nums);
        
        // 情况2: 最大子数组跨越首尾（环形）
        int totalSum = Arrays.stream(nums).sum();
        
        // 计算最小子数组和
        int[] invertedNums = Arrays.stream(nums).map(x -> -x).toArray();
        int minSubArraySum = -maxSubArrayKadane(invertedNums);
        
        int maxCircular = totalSum - minSubArraySum;
        
        // 如果所有元素都是负数，maxCircular会是0，这时应该返回maxKadane
        return maxCircular == 0 ? maxKadane : Math.max(maxKadane, maxCircular);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化Kadane算法过程
     * 
     * @param nums 输入数组
     */
    public static void visualizeKadaneProcess(int[] nums) {
        if (nums == null || nums.length == 0) return;
        
        System.out.println("\nKadane算法过程可视化:");
        System.out.println("数组: " + Arrays.toString(nums));
        
        System.out.println("\n步骤 | 当前元素 | 当前最大 | 全局最大 | 决策");
        System.out.println("-----|----------|----------|----------|------------------");
        
        int maxSoFar = nums[0];
        int maxEndingHere = nums[0];
        
        System.out.printf("%4d | %8d | %8d | %8d | 初始化\n", 
                         1, nums[0], maxEndingHere, maxSoFar);
        
        for (int i = 1; i < nums.length; i++) {
            int prevMaxEndingHere = maxEndingHere;
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            
            String decision = (nums[i] > prevMaxEndingHere + nums[i]) ? 
                             "重新开始" : "继续累加";
            
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
            
            System.out.printf("%4d | %8d | %8d | %8d | %s\n", 
                             i + 1, nums[i], maxEndingHere, maxSoFar, decision);
        }
        
        System.out.println("\n最终结果: " + maxSoFar);
    }
    
    /**
     * JDK 1.8兼容的字符串重复方法
     * 
     * @param str 要重复的字符串
     * @param count 重复次数
     * @return 重复后的字符串
     */
    private static String repeatString(String str, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    /**
     * 可视化分治法过程
     * 
     * @param nums 输入数组
     */
    public static void visualizeDivideConquer(int[] nums) {
        System.out.println("\n分治法过程可视化:");
        System.out.println("数组: " + Arrays.toString(nums));
        visualizeDivideConquerHelper(nums, 0, nums.length - 1, 0);
    }
    
    private static int visualizeDivideConquerHelper(int[] nums, int left, int right, int depth) {
        String indent = repeatString("  ", depth);
        
        if (left == right) {
            System.out.printf("%s区间[%d,%d]: 单元素 %d\n", indent, left, right, nums[left]);
            return nums[left];
        }
        
        int mid = left + (right - left) / 2;
        System.out.printf("%s区间[%d,%d]: 分割点 %d\n", indent, left, right, mid);
        
        int leftMax = visualizeDivideConquerHelper(nums, left, mid, depth + 1);
        int rightMax = visualizeDivideConquerHelper(nums, mid + 1, right, depth + 1);
        int crossMax = maxCrossingSum(nums, left, mid, right);
        
        int result = Math.max(Math.max(leftMax, rightMax), crossMax);
        
        System.out.printf("%s区间[%d,%d]: 左=%d, 右=%d, 跨=%d, 最大=%d\n", 
                         indent, left, right, leftMax, rightMax, crossMax, result);
        
        return result;
    }
    
    /**
     * 绘制子数组和的分布图
     * 
     * @param nums 输入数组
     */
    public static void plotSubarraySums(int[] nums) {
        if (nums == null || nums.length == 0 || nums.length > 20) {
            System.out.println("数组为空或太大，无法绘制分布图");
            return;
        }
        
        System.out.println("\n子数组和分布图:");
        System.out.println("数组: " + Arrays.toString(nums));
        
        // 计算所有子数组和
        List<Integer> allSums = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int sum = 0;
            for (int j = i; j < nums.length; j++) {
                sum += nums[j];
                allSums.add(sum);
            }
        }
        
        int maxSum = Collections.max(allSums);
        int minSum = Collections.min(allSums);
        
        System.out.printf("\n范围: [%d, %d]\n", minSum, maxSum);
        
        // 简单的ASCII直方图
        Map<Integer, Integer> frequency = new HashMap<>();
        for (int sum : allSums) {
            frequency.put(sum, frequency.getOrDefault(sum, 0) + 1);
        }
        
        System.out.println("\n子数组和频率分布:");
        List<Integer> sortedSums = new ArrayList<>(frequency.keySet());
        Collections.sort(sortedSums);
        
        for (int sum : sortedSums) {
            int count = frequency.get(sum);
            String bar = repeatString("*", count);
            System.out.printf("%4d: %s (%d)\n", sum, bar, count);
        }
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param size 数组大小
     * @param range 数值范围
     */
    public static void comparePerformance(int size, int range) {
        // 生成测试数据
        int[] nums = generateRandomArray(size, -range, range);
        
        long start, end;
        
        // 测试Kadane算法
        start = System.nanoTime();
        int result1 = maxSubArrayKadane(nums);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试分治法
        start = System.nanoTime();
        int result2 = maxSubArrayDivideConquer(nums);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 小数组才测试暴力法
        long time3 = -1;
        int result3 = -1;
        if (size <= 1000) {
            start = System.nanoTime();
            result3 = maxSubArrayBruteForce(nums).maxSum;
            end = System.nanoTime();
            time3 = end - start;
        }
        
        System.out.println("\n性能比较:");
        System.out.printf("数组大小: %d, 数值范围: [-%d, %d]\n", size, range, range);
        System.out.println("===============================================");
        System.out.println("方法            | 时间(ms)   | 结果 | 相对速度");
        System.out.println("----------------|------------|------|----------");
        System.out.printf("Kadane算法      | %.6f | %4d | 基准\n", time1 / 1_000_000.0, result1);
        System.out.printf("分治法          | %.6f | %4d | %.2fx\n", time2 / 1_000_000.0, result2, (double)time2/time1);
        
        if (time3 > 0) {
            System.out.printf("暴力法          | %.6f | %4d | %.2fx\n", time3 / 1_000_000.0, result3, (double)time3/time1);
        }
        
        System.out.println("===============================================");
        
        // 验证结果一致性
        boolean consistent = (result1 == result2) && (time3 <= 0 || result1 == result3);
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
        testCase("经典示例", new int[]{-2,1,-3,4,-1,2,1,-5,4}, 6);
        testCase("全正数", new int[]{1,2,3,4,5}, 15);
        testCase("全负数", new int[]{-5,-2,-8,-1}, -1);
        testCase("单元素", new int[]{-3}, -3);
        testCase("两元素", new int[]{-1,2}, 2);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("包含零", new int[]{0,-1,0,2,0}, 2);
        testCase("交替正负", new int[]{1,-1,1,-1,1}, 1);
        testCase("大数组", new int[]{5,-3,5,-3,5,-3,5}, 10);
        testCase("前负后正", new int[]{-3,-2,-1,4,5,6}, 15);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 股票交易
        System.out.println("\n股票交易分析:");
        int[] prices = {7, 1, 5, 3, 6, 4, 2, 8};
        System.out.println("股票价格: " + Arrays.toString(prices));
        Map<String, Object> tradingResult = calculateMaxProfit(prices);
        System.out.println("交易分析: " + tradingResult);
        
        // 场景2: 信号处理
        System.out.println("\n信号处理分析:");
        double[] signal = {0.5, -0.2, 0.8, -0.1, 0.3, -0.7, 0.9, -0.3};
        System.out.println("信号强度: " + Arrays.toString(signal));
        Map<String, Object> energyResult = findMaxEnergySegment(signal, 0.5);
        System.out.println("能量分析: " + energyResult);
        
        // 场景3: 游戏连击
        System.out.println("\n游戏连击分析:");
        int[] scores = {10, -5, 15, -2, 8, -12, 20, -3};
        System.out.println("攻击得分: " + Arrays.toString(scores));
        Map<String, Object> comboResult = calculateBestCombo(scores);
        System.out.println("连击分析: " + comboResult);
        
        // 场景4: 环形数组
        System.out.println("\n环形数组测试:");
        int[] circularArray = {1, -2, 3, -2};
        System.out.println("环形数组: " + Arrays.toString(circularArray));
        System.out.println("普通最大子数组和: " + maxSubArrayKadane(circularArray));
        System.out.println("环形最大子数组和: " + maxSubArrayCircular(circularArray));
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        int[] demoArray = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        visualizeKadaneProcess(demoArray);
        visualizeDivideConquer(demoArray);
        plotSubarraySums(new int[]{-2, 1, -3, 4, -1});
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100, 50);
        comparePerformance(1000, 100);
        comparePerformance(10000, 1000);
    }
    
    private static void testCase(String name, int[] nums, int expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("数组: " + Arrays.toString(nums));
        
        int result1 = maxSubArrayKadane(nums);
        SubarrayResult result2 = maxSubArrayKadaneDetailed(nums);
        int result3 = maxSubArrayDivideConquer(nums);
        SubarrayResult result4 = maxSubArrayBruteForce(nums);
        
        System.out.println("Kadane算法结果: " + result1);
        System.out.println("Kadane详细结果: " + result2);
        System.out.println("分治法结果: " + result3);
        System.out.println("暴力法结果: " + result4);
        System.out.println("期望结果: " + expected);
        
        boolean isCorrect = (result1 == expected) && (result2.maxSum == expected) && 
                           (result3 == expected) && (result4.maxSum == expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小数组展示可视化
        if (nums.length <= 10) {
            visualizeKadaneProcess(nums);
        }
    }
}