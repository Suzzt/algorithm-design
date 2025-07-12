package main.java.org.dao.qa;

import java.util.*;

/**
 * 背包问题 (Knapsack Problem) 解决方案
 * 
 * <p><b>问题描述：</b>
 * 给定一组物品，每个物品有重量和价值，以及一个容量为W的背包。
 * 选择物品放入背包，使得总价值最大而总重量不超过背包容量。
 * 
 * <p><b>示例：</b>
 * 重量: [1, 3, 4, 5], 价值: [10, 40, 50, 70], 容量: 7
 * 输出: 最大价值为80 (选择重量3和4的物品)
 * 
 * <p><b>解题思路：</b>
 * 1. 递归解法（指数复杂度）
 * 2. 基础动态规划（O(n×W)时间/空间）
 * 3. 空间优化动态规划（O(n×W)时间，O(W)空间）
 */
public class KnapsackProblem {

    // ========================== 核心解法 ==========================
    
    /**
     * 递归解法
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 最大价值
     */
    public static int knapsackRecursive(int[] weights, int[] values, int capacity) {
        return recursiveHelper(weights, values, capacity, weights.length);
    }
    
    private static int recursiveHelper(int[] weights, int[] values, int capacity, int n) {
        // 基础情况：没有物品或容量为0
        if (n == 0 || capacity == 0) 
            return 0;
        
        // 当前物品超重，跳过
        if (weights[n - 1] > capacity) 
            return recursiveHelper(weights, values, capacity, n - 1);
        
        // 选择当前物品和不选择当前物品的两种情况
        int include = values[n - 1] + recursiveHelper(weights, values, capacity - weights[n - 1], n - 1);
        int exclude = recursiveHelper(weights, values, capacity, n - 1);
        
        return Math.max(include, exclude);
    }
    
    /**
     * 基础动态规划解法
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 最大价值
     */
    public static int knapsackDP(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                // 当前物品超重
                if (weights[i - 1] > w) {
                    dp[i][w] = dp[i - 1][w];
                } 
                // 可以选择当前物品
                else {
                    dp[i][w] = Math.max(
                        dp[i - 1][w], 
                        dp[i - 1][w - weights[i - 1]] + values[i - 1]
                    );
                }
            }
        }
        
        return dp[n][capacity];
    }
    
    /**
     * 空间优化动态规划解法
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 最大价值
     */
    public static int knapsackOptimizedDP(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        
        for (int i = 0; i < n; i++) {
            // 逆序遍历防止覆盖
            for (int w = capacity; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
            }
        }
        
        return dp[capacity];
    }
    
    // ========================== 进阶功能：获取具体解 ==========================
    
    /**
     * 获取背包问题的最优解方案
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 包含的物品索引列表
     */
    public static List<Integer> getKnapsackSolution(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] > w) {
                    dp[i][w] = dp[i - 1][w];
                } else {
                    dp[i][w] = Math.max(
                        dp[i - 1][w], 
                        dp[i - 1][w - weights[i - 1]] + values[i - 1]
                    );
                }
            }
        }
        
        // 回溯获取解决方案
        List<Integer> solution = new ArrayList<>();
        int w = capacity;
        for (int i = n; i > 0 && w > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                solution.add(i - 1); // 添加物品索引
                w -= weights[i - 1];
            }
        }
        Collections.reverse(solution); // 恢复原始顺序
        return solution;
    }
    
    // ========================== 应用场景扩展 ==========================
    
    /**
     * 投资组合优化
     * 
     * @param capitals 项目投资额
     * @param profits 项目预期收益
     * @param totalCapital 总投资金额
     * @return 最优投资项目组合
     */
    public static List<Integer> investmentOptimization(int[] capitals, int[] profits, int totalCapital) {
        int maxProfit = knapsackOptimizedDP(capitals, profits, totalCapital);
        System.out.println("最大收益: " + maxProfit);
        return getKnapsackSolution(capitals, profits, totalCapital);
    }
    
    /**
     * 资源分配系统
     * 
     * @param resourceDemands 任务资源需求
     * @param taskValues 任务价值
     * @param totalResources 总资源量
     * @return 最优任务选择方案
     */
    public static List<Integer> resourceAllocation(int[] resourceDemands, int[] taskValues, int totalResources) {
        int maxValue = knapsackOptimizedDP(resourceDemands, taskValues, totalResources);
        System.out.println("最大总价值: " + maxValue);
        return getKnapsackSolution(resourceDemands, taskValues, totalResources);
    }
    
    /**
     * 云计算任务调度
     * 
     * @param taskRequirements 任务资源要求
     * @param taskPriorities 任务优先级
     * @param serverCapacity 服务器容量
     * @return 最优任务调度方案
     */
    public static List<Integer> cloudTaskScheduling(int[] taskRequirements, int[] taskPriorities, int serverCapacity) {
        int maxPriority = knapsackOptimizedDP(taskRequirements, taskPriorities, serverCapacity);
        System.out.println("最高总优先级: " + maxPriority);
        return getKnapsackSolution(taskRequirements, taskPriorities, serverCapacity);
    }
    
    /**
     * 货物装载优化系统
     * 
     * @param packageWeights 包裹重量
     * @param packageValues 包裹价值
     * @param truckCapacity 卡车容量
     * @return 最优装载方案
     */
    public static List<Integer> cargoLoadingOptimization(int[] packageWeights, int[] packageValues, int truckCapacity) {
        int maxValue = knapsackOptimizedDP(packageWeights, packageValues, truckCapacity);
        System.out.println("最大总价值: $" + maxValue);
        return getKnapsackSolution(packageWeights, packageValues, truckCapacity);
    }
    
    // ========================== 测试方法 ==========================
    
    public static void main(String[] args) {
        testAlgorithms();
        testApplicationScenarios();
    }
    
    private static void testAlgorithms() {
        System.out.println("===== 算法测试 =====");
        
        int[] weights = {1, 3, 4, 5};
        int[] values = {10, 40, 50, 70};
        int capacity = 7;
        
        int recResult = knapsackRecursive(weights, values, capacity);
        int dpResult = knapsackDP(weights, values, capacity);
        int optResult = knapsackOptimizedDP(weights, values, capacity);
        List<Integer> solution = getKnapsackSolution(weights, values, capacity);
        
        System.out.println("递归解法结果: " + recResult);
        System.out.println("动态规划结果: " + dpResult);
        System.out.println("优化动态规划结果: " + optResult);
        System.out.println("具体方案: " + solution + " (重量: " + solution.stream().mapToInt(i -> weights[i]).sum() + 
                          ", 价值: " + solution.stream().mapToInt(i -> values[i]).sum() + ")");
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n===== 应用场景测试 =====");
        
        // 场景1: 投资组合优化
        System.out.println("1. 投资组合优化:");
        int[] capitals = {10, 20, 30}; // 百万美元
        int[] profits = {60, 100, 120}; // 投资回报（百万）
        int totalCapital = 50; // 总投资金额
        
        List<Integer> investmentPlan = investmentOptimization(capitals, profits, totalCapital);
        System.out.println("最优投资项目索引: " + investmentPlan);
        System.out.println("投资项目金额: " + investmentPlan.stream().mapToInt(i -> capitals[i]).sum() + "M");
        
        // 场景2: 云计算任务调度
        System.out.println("\n2. 云计算任务调度:");
        int[] taskRequirements = {2, 1, 3, 2}; // CPU核数
        int[] taskPriorities = {12, 10, 20, 15}; // 任务优先级
        int serverCapacity = 5; // 服务器总CPU核数
        
        List<Integer> schedule = cloudTaskScheduling(taskRequirements, taskPriorities, serverCapacity);
        System.out.println("执行的任务索引: " + schedule);
        System.out.println("使用的CPU核数: " + schedule.stream().mapToInt(i -> taskRequirements[i]).sum());
        
        // 场景3: 货物装载优化
        System.out.println("\n3. 货物装载优化:");
        int[] weights = {10, 20, 30}; // 公斤
        int[] values = {60, 100, 120}; // 美元
        int truckCapacity = 50; // 公斤
        
        List<Integer> cargoPlan = cargoLoadingOptimization(weights, values, truckCapacity);
        System.out.println("装载货物索引: " + cargoPlan);
        System.out.println("总重量: " + cargoPlan.stream().mapToInt(i -> weights[i]).sum() + "kg");
    }
}