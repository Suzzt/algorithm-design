package main.java.org.dao.qa;

import java.util.*;

/**
 * 目标和 (Target Sum) 问题
 * 
 * <p><b>问题描述：</b>
 * 给定一个非负整数数组和一个目标整数S。每个元素可以添加+或-，求一共有多少种添加符号的方式使得整个数组和等于S。
 * 
 * <p><b>示例：</b>
 * 输入: nums = [1,1,1,1,1], S = 3
 * 输出: 5
 * 解释: 
 *   -1+1+1+1+1 = 3
 *   +1-1+1+1+1 = 3
 *   +1+1-1+1+1 = 3
 *   +1+1+1-1+1 = 3
 *   +1+1+1+1-1 = 3
 * 
 * <p><b>解题思路：</b>
 * 1. DFS递归回溯（适合小规模数据）
 * 2. 动态规划（背包问题变种）
 * 3. 空间优化动态规划
 */
public class TargetSum {

    // ========================== 基础解法 ==========================
    
    /**
     * DFS递归解法 (时间复杂度O(2^n))
     * 
     * @param nums 整数数组
     * @param S 目标值
     * @return 实现目标的方式数量
     */
    public static int findTargetSumWaysDFS(int[] nums, int S) {
        int[] count = new int[1];
        dfs(nums, S, 0, 0, count);
        return count[0];
    }
    
    private static void dfs(int[] nums, int target, int index, int currentSum, int[] count) {
        if (index == nums.length) {
            if (currentSum == target) {
                count[0]++;
            }
            return;
        }
        
        // 尝试加法
        dfs(nums, target, index + 1, currentSum + nums[index], count);
        // 尝试减法
        dfs(nums, target, index + 1, currentSum - nums[index], count);
    }
    
    /**
     * 动态规划解法 (时间复杂度O(n×sum))
     * 
     * @param nums 整数数组
     * @param S 目标值
     * @return 实现目标的方式数量
     */
    public static int findTargetSumWaysDP(int[] nums, int S) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 处理无效情况
        if (S > sum || S < -sum) return 0;
        
        int n = nums.length;
        int[][] dp = new int[n + 1][2 * sum + 1];
        dp[0][sum] = 1; // 初始偏移量为sum（对应和为0）
        
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= 2 * sum; j++) {
                // 跳过不可能状态
                if (dp[i - 1][j] == 0) continue;
                
                int num = nums[i - 1];
                // 添加正号的情况
                if (j + num <= 2 * sum) {
                    dp[i][j + num] += dp[i - 1][j];
                }
                // 添加负号的情况
                if (j - num >= 0) {
                    dp[i][j - num] += dp[i - 1][j];
                }
            }
        }
        
        return dp[n][S + sum];
    }
    
    /**
     * 空间优化动态规划解法 (时间复杂度O(n×sum))
     * 
     * @param nums 整数数组
     * @param S 目标值
     * @return 实现目标的方式数量
     */
    public static int findTargetSumWaysOptimizedDP(int[] nums, int S) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (S > sum || S < -sum) return 0;
        
        int n = nums.length;
        int[] dp = new int[2 * sum + 1];
        dp[sum] = 1;
        
        for (int num : nums) {
            int[] next = new int[2 * sum + 1];
            for (int j = 0; j <= 2 * sum; j++) {
                if (dp[j] == 0) continue;
                
                if (j + num <= 2 * sum) {
                    next[j + num] += dp[j];
                }
                if (j - num >= 0) {
                    next[j - num] += dp[j];
                }
            }
            dp = next;
        }
        
        return dp[S + sum];
    }
    
    // ========================== 应用场景扩展 ==========================
    
    /**
     * 密码破解计算器 - 计算特定字符组合达到目标数值的方式
     * 
     * @param asciiValues 字符ASCII值数组
     * @param targetASCII 目标ASCII值
     * @return 达成目标的方式数量
     */
    public static int passwordCrackerCalculator(int[] asciiValues, int targetASCII) {
        return findTargetSumWaysOptimizedDP(asciiValues, targetASCII);
    }
    
    /**
     * 电路电压组合计算器
     * 
     * @param voltages 电压值数组（正表示正极，负表示负极）
     * @param targetVoltage 目标电压
     * @return 达成目标电压的串联方式数量
     */
    public static int circuitVoltageCombination(int[] voltages, int targetVoltage) {
        // 将原电压值取绝对值作为操作数
        int[] absoluteVoltages = Arrays.stream(voltages).map(Math::abs).toArray();
        return findTargetSumWaysOptimizedDP(absoluteVoltages, targetVoltage);
    }
    
    /**
     * 数字营销ROI计算器 - 计算达到目标投资回报的方式数量
     * 
     * @param campaignROIs 各广告渠道的投资回报率数组
     * @param targetROI 目标投资回报率
     * @return 达成目标的方式数量
     */
    public static int marketingROICalculator(double[] campaignROIs, double targetROI) {
        // 将ROI值转换为整数处理（乘1000避免精度损失）
        int[] rois = Arrays.stream(campaignROIs).mapToInt(d -> (int)(d * 1000)).toArray();
        int target = (int)(targetROI * 1000);
        return findTargetSumWaysOptimizedDP(rois, target);
    }
    
    /**
     * 区块链交易签名验证 - 检查私钥组合达到目标哈希值的方式
     * 
     * @param keySegments 私钥分段值
     * @param targetHash 目标哈希值
     * @return 达成目标哈希的私钥组合方式
     */
    public static int blockchainSignatureVerification(int[] keySegments, int targetHash) {
        return findTargetSumWaysOptimizedDP(keySegments, targetHash);
    }
    
    // ========================== 测试方法 ==========================
    
    public static void main(String[] args) {
        testAlgorithms();
        testApplicationScenarios();
    }
    
    private static void testAlgorithms() {
        System.out.println("===== 算法测试 =====");
        int[] nums = {1, 1, 1, 1, 1};
        int S = 3;
        
        System.out.println("测试用例: nums = " + Arrays.toString(nums) + ", S = " + S);
        
        int dfsResult = findTargetSumWaysDFS(nums, S);
        int dpResult = findTargetSumWaysDP(nums, S);
        int optimizedDpResult = findTargetSumWaysOptimizedDP(nums, S);
        
        System.out.println("DFS递归结果: " + dfsResult);
        System.out.println("动态规划结果: " + dpResult);
        System.out.println("优化动态规划结果: " + optimizedDpResult);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n===== 应用场景测试 =====");
        
        // 场景1: 密码破解计算器
        System.out.println("1. 密码破解计算器测试:");
        int[] asciiValues = {65, 66, 67}; // ASCII: A=65, B=66, C=67
        int targetASCII = 70;
        int ways = passwordCrackerCalculator(asciiValues, targetASCII);
        System.out.printf("组合达成ASCII值 %d 的方式数量: %d\n", targetASCII, ways);
        
        // 场景2: 电路电压组合计算器
        System.out.println("\n2. 电路电压组合测试:");
        int[] voltages = {2, 3, 4}; // 电压值（方向可以反转）
        int targetVoltage = 1;
        int combinations = circuitVoltageCombination(voltages, targetVoltage);
        System.out.printf("达成电压 %dV 的组合方式数量: %d\n", targetVoltage, combinations);
        
        // 场景3: 数字营销ROI计算器
        System.out.println("\n3. 数字营销ROI计算:");
        double[] campaignROIs = {0.05, 0.10, 0.15}; // 各渠道投资回报率
        double targetROI = 0.30;
        int roiWays = marketingROICalculator(campaignROIs, targetROI);
        System.out.printf("达成投资回报率 %.2f 的方式数量: %d\n", targetROI, roiWays);
        
        // 场景4: 区块链交易签名验证
        System.out.println("\n4. 区块链签名验证测试:");
        int[] keySegments = {10, 20, 30}; // 私钥分段值
        int targetHash = 60;
        int signatures = blockchainSignatureVerification(keySegments, targetHash);
        System.out.printf("达成哈希值 %d 的私钥组合方式: %d\n", targetHash, signatures);
    }
}