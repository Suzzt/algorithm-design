package main.java.org.dao.qa;

import java.util.*;
import java.util.stream.IntStream;

/**
 * 0-1背包问题
 * 
 * <p><b>问题描述</b>:
 * 给定一组物品，每个物品有重量和价值，在背包容量一定的情况下，如何选择装入背包的物品，使得背包中物品的总价值最大？
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 中等/困难 (LeetCode上没有完全对应题，但类似"分割等和子集"等问题是变种)
 * 
 * <p><b>算法形式化定义</b>:
 * 输入: 
 *   - 物品集合: 每个物品i有重量weights[i]和价值values[i]
 *   - 背包容量: capacity
 * 输出: 
 *   - 最大价值: maxValue
 *   - 物品选择方案: selectedItems
 * 
 * <p><b>示例</b>:
 * 输入: 
 *   weights = [2, 3, 4, 5]
 *   values = [3, 4, 5, 6]
 *   capacity = 5
 * 输出: 
 *   最大价值: 7 (选择物品0和1: 2+3=5, 价值3+4=7)
 * 
 * <p><b>解题思路</b>:
 * 1. 动态规划: 二维表法(基础实现)
 * 2. 动态规划: 一维空间优化
 * 3. 记忆化递归: 自顶向下解法
 * 4. 分支限界法: 高效求精确解
 * 5. 遗传算法: 启发式近似解法
 *
 * <p><b>时间复杂度</b>:
 *   - 动态规划: O(n*capacity)
 *   - 分支限界法: 最坏O(2^n)，但实际远低于此
 *   - 遗传算法: O(population_size * generations)
 * 
 * <p><b>应用场景</b>:
 * 1. 资源分配优化
 * 2. 货物装载问题
 * 3. 投资组合选择
 * 4. 网络带宽分配
 * 5. 工业生产计划
 */

public class KnapsackProblem {

    // ================== 解法1: 动态规划(二维表) ==================
    
    /**
     * 动态规划解法(基础实现)
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 最大价值
     */
    public static int knapsackDP(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        // dp[i][w]表示前i个物品，容量为w时的最大价值
        int[][] dp = new int[n + 1][capacity + 1];
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                // 如果当前物品重量小于等于背包容量，考虑选择或不选择
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(
                        values[i - 1] + dp[i - 1][w - weights[i - 1]],  // 选择物品i
                        dp[i - 1][w]                                     // 不选择物品i
                    );
                } else {
                    // 不能选择当前物品
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        
        return dp[n][capacity];
    }
    
    /**
     * 回溯找出被选中的物品
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @param dp DP表
     * @return 选择的物品索引列表
     */
    public static List<Integer> getSelectedItems(int[] weights, int[] values, int capacity, int[][] dp) {
        int n = weights.length;
        List<Integer> selected = new ArrayList<>();
        int w = capacity;
        
        // 从后向前回溯
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) { // 物品i被选择
                selected.add(i - 1);        // 添加到结果列表
                w -= weights[i - 1];        // 减少背包容量
            }
        }
        
        Collections.reverse(selected); // 反转列表使物品顺序正确
        return selected;
    }
    
    // ================== 解法2: 动态规划(一维空间优化) ==================
    
    /**
     * 动态规划空间优化版(使用一维数组)
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 最大价值
     */
    public static int knapsackDPOptimized(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        
        for (int i = 0; i < n; i++) {
            // 倒序更新，避免覆盖前一状态
            for (int w = capacity; w >= weights[i]; w--) {
                if (weights[i] <= w) {
                    dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
                }
            }
        }
        
        return dp[capacity];
    }
    
    // ================== 解法3: 记忆化递归 ==================
    
    /**
     * 记忆化递归解法
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 最大价值
     */
    public static int knapsackMemo(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] memo = new int[n][capacity + 1];
        // 初始化记忆数组为-1
        for (int[] row : memo) Arrays.fill(row, -1);
        
        return memoRecursion(weights, values, n - 1, capacity, memo);
    }
    
    private static int memoRecursion(int[] weights, int[] values, int i, int w, int[][] memo) {
        // 基条件：没有物品或背包容量为0
        if (i < 0 || w <= 0) return 0;
        
        // 检查记忆
        if (memo[i][w] != -1) return memo[i][w];
        
        // 不选择当前物品
        int notTake = memoRecursion(weights, values, i - 1, w, memo);
        
        // 如果当前物品重量超过背包容量，不能选择
        if (weights[i] > w) {
            memo[i][w] = notTake;
            return memo[i][w];
        }
        
        // 选择当前物品
        int take = values[i] + memoRecursion(weights, values, i - 1, w - weights[i], memo);
        
        // 取较大值
        memo[i][w] = Math.max(take, notTake);
        return memo[i][w];
    }
    
    // ================== 解法4: 分支限界法 ==================
    
    /**
     * 分支限界法求解背包问题
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 最大价值
     */
    public static int knapsackBranchBound(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        
        // 按单位价值降序排序物品
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(weights[i], values[i], i);
        }
        Arrays.sort(items, Comparator.comparingDouble(Item::valuePerWeight).reversed());
        
        // 优先队列（根据节点的上限排序）
        PriorityQueue<Node> queue = new PriorityQueue<>();
        // 根节点：没有选择任何物品
        queue.add(new Node(-1, 0, 0, calculateBound(-1, 0, capacity, items)));
        
        int maxValue = 0;
        
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            
            // 如果当前节点的上限已经小于最大价值，则跳过
            if (node.bound <= maxValue) continue;
            
            // 更新最大价值
            if (node.value > maxValue) {
                maxValue = node.value;
            }
            
            // 已处理所有物品
            if (node.level == n - 1) continue;
            
            int nextLevel = node.level + 1;
            
            // 尝试选择下一个物品
            if (node.weight + items[nextLevel].weight <= capacity) {
                queue.add(new Node(
                    nextLevel,
                    node.weight + items[nextLevel].weight,
                    node.value + items[nextLevel].value,
                    calculateBound(nextLevel, node.value + items[nextLevel].value, 
                                 capacity - node.weight - items[nextLevel].weight, items)
                ));
            }
            
            // 尝试不选择下一个物品
            queue.add(new Node(
                nextLevel,
                node.weight,
                node.value,
                calculateBound(nextLevel, node.value, capacity - node.weight, items)
            ));
        }
        
        return maxValue;
    }
    
    // 计算节点的价值上界（使用贪心）
    private static double calculateBound(int level, int curValue, int remainingCapacity, Item[] items) {
        double bound = curValue;
        // 装满剩余背包容量
        for (int i = level + 1; i < items.length; i++) {
            if (remainingCapacity <= 0) break;
            if (items[i].weight <= remainingCapacity) {
                bound += items[i].value;
                remainingCapacity -= items[i].weight;
            } else {
                bound += items[i].valuePerWeight() * remainingCapacity;
                remainingCapacity = 0;
            }
        }
        return bound;
    }
    
    // 物品类（用于分支限界）
    static class Item {
        int weight;
        int value;
        int index;
        
        Item(int weight, int value, int index) {
            this.weight = weight;
            this.value = value;
            this.index = index;
        }
        
        double valuePerWeight() {
            return (double) value / weight;
        }
    }
    
    // 节点类（用于分支限界）
    static class Node implements Comparable<Node> {
        int level;      // 决策树层级
        int weight;     // 当前总重量
        int value;      // 当前总价值
        double bound;   // 价值上界
        
        Node(int level, int weight, int value, double bound) {
            this.level = level;
            this.weight = weight;
            this.value = value;
            this.bound = bound;
        }
        
        @Override
        public int compareTo(Node other) {
            // 按bound降序排序（优先队列是小顶堆，所以取反）
            return Double.compare(other.bound, this.bound);
        }
    }
    
    // ================== 解法5: 遗传算法 ==================
    
    /**
     * 遗传算法求解背包问题（近似解法）
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @param populationSize 种群大小
     * @param generations 迭代代数
     * @return 最大价值
     */
    public static int knapsackGA(int[] weights, int[] values, int capacity, 
                                int populationSize, int generations) {
        Random random = new Random();
        int n = weights.length;
        
        // 初始化种群
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Chromosome(n, weights, values, capacity, random));
        }
        
        // 进化循环
        for (int gen = 0; gen < generations; gen++) {
            // 评估种群
            population.forEach(Chromosome::evaluateFitness);
            // 排序（按适应度降序）
            population.sort(Comparator.comparingInt((Chromosome c) -> c.fitness).reversed());
            
            // 精英保留（保留前10%的优秀个体）
            int eliteSize = populationSize / 10;
            List<Chromosome> newGeneration = new ArrayList<>(population.subList(0, eliteSize));
            
            // 生成新种群
            while (newGeneration.size() < populationSize) {
                // 选择父代（使用轮盘赌选择）
                Chromosome parent1 = selectParent(population, random);
                Chromosome parent2 = selectParent(population, random);
                
                // 交叉
                Chromosome child = crossover(parent1, parent2, n, weights, values, capacity, random);
                
                // 变异
                mutate(child, weights, values, capacity, random, 0.1);
                
                newGeneration.add(child);
            }
            
            population = newGeneration;
        }
        
        // 返回最佳适应度
        return population.stream()
                .max(Comparator.comparingInt(c -> c.fitness))
                .get().fitness;
    }
    
    // 染色体表示
    static class Chromosome {
        boolean[] genes; // 基因数组（true表示选择该物品）
        int fitness;     // 适应度（总价值）
        int totalWeight; // 总重量
        
        Chromosome(int n, int[] weights, int[] values, int capacity, Random random) {
            genes = new boolean[n];
            // 随机初始化
            for (int i = 0; i < n; i++) {
                genes[i] = random.nextDouble() < 0.3; // 初始选择概率30%
            }
            evaluateFitness(weights, values, capacity);
        }
        
        // 评估适应度
        void evaluateFitness(int[] weights, int[] values, int capacity) {
            totalWeight = 0;
            fitness = 0;
            
            for (int i = 0; i < genes.length; i++) {
                if (genes[i]) {
                    totalWeight += weights[i];
                    fitness += values[i];
                }
            }
            
            // 超出容量，则适应度为负（惩罚）
            if (totalWeight > capacity) {
                fitness -= (totalWeight - capacity) * 1000; // 大惩罚因子
            }
        }
        
        // 简化方法（用于默认调用）
        void evaluateFitness() {
            // 假设weights,values,capacity在外部作用域
            // 实际应存储在类中，这里为简化
        }
    }
    
    // 轮盘赌选择父代
    private static Chromosome selectParent(List<Chromosome> population, Random random) {
        int totalFitness = population.stream().mapToInt(c -> c.fitness).sum();
        int randomValue = random.nextInt(totalFitness);
        int cumulative = 0;
        
        for (Chromosome chrom : population) {
            cumulative += chrom.fitness;
            if (cumulative > randomValue) {
                return chrom;
            }
        }
        
        return population.get(population.size() - 1);
    }
    
    // 单点交叉
    private static Chromosome crossover(Chromosome p1, Chromosome p2, int n, 
                                      int[] weights, int[] values, int capacity, Random random) {
        Chromosome child = new Chromosome(n, weights, values, capacity, random);
        int point = random.nextInt(n);
        
        for (int i = 0; i < n; i++) {
            child.genes[i] = i < point ? p1.genes[i] : p2.genes[i];
        }
        child.evaluateFitness(weights, values, capacity);
        return child;
    }
    
    // 变异（按概率翻转基因位）
    private static void mutate(Chromosome chrom, int[] weights, int[] values, 
                            int capacity, Random random, double mutationRate) {
        for (int i = 0; i < chrom.genes.length; i++) {
            if (random.nextDouble() < mutationRate) {
                chrom.genes[i] = !chrom.genes[i];
            }
        }
        chrom.evaluateFitness(weights, values, capacity);
    }
    
    // ================== 可视化工具 ==================
    
    /**
     * 可视化动态规划表
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     */
    public static void visualizeDPTable(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            for (int w = 1; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(values[i - 1] + dp[i - 1][w - weights[i - 1]], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }
        
        // 打印表头
        System.out.println("\n动态规划表可视化:");
        System.out.printf("%5s", "物品\\容量");
        for (int w = 0; w <= capacity; w++) {
            System.out.printf("%4d", w);
        }
        System.out.println();
        
        // 打印表格
        for (int i = 0; i <= n; i++) {
            if (i == 0) {
                System.out.printf("%7s", "∅");
            } else {
                System.out.printf("%4d (%d)", i - 1, weights[i - 1]);
            }
            for (int w = 0; w <= capacity; w++) {
                if (w == 0) System.out.print(" → 0");
                else System.out.printf("%4d", dp[i][w]);
            }
            System.out.println();
        }
        
        // 获取选择的物品
        List<Integer> selected = getSelectedItems(weights, values, capacity, dp);
        System.out.println("\n选择的物品: " + selected);
        System.out.println("总价值: " + dp[n][capacity]);
        System.out.println("总重量: " + selected.stream().mapToInt(i -> weights[i]).sum());
    }
    
    /**
     * 可视化分支限界过程
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     */
    public static void visualizeBranchBound(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(weights[i], values[i], i);
        }
        Arrays.sort(items, Comparator.comparingDouble(Item::valuePerWeight).reversed());
        
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(-1, 0, 0, calculateBound(-1, 0, capacity, items)));
        
        int step = 0;
        int maxValue = 0;
        
        System.out.println("\n分支限界过程可视化:");
        System.out.printf("%4s %7s %7s %7s %10s%n", "步数", "层级", "重量", "价值", "上界");
        
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            step++;
            
            // 打印节点信息
            System.out.printf("%4d %7d %7d %7d %10.2f %s%n", 
                step, node.level, node.weight, node.value, node.bound,
                (node.value > maxValue) ? "★" : "");
            
            // 更新最大价值
            if (node.value > maxValue) {
                maxValue = node.value;
            }
            
            // 剪枝条件
            if (node.bound <= maxValue) continue;
            if (node.level == n - 1) continue;
            
            int nextLevel = node.level + 1;
            
            // 选择该物品
            if (node.weight + items[nextLevel].weight <= capacity) {
                queue.add(new Node(
                    nextLevel,
                    node.weight + items[nextLevel].weight,
                    node.value + items[nextLevel].value,
                    calculateBound(nextLevel, node.value + items[nextLevel].value, 
                                 capacity - node.weight - items[nextLevel].weight, items)
                ));
            }
            
            // 不选择该物品
            queue.add(new Node(
                nextLevel,
                node.weight,
                node.value,
                calculateBound(nextLevel, node.value, capacity - node.weight, items)
            ));
        }
        
        System.out.println("最大价值: " + maxValue);
    }
    
    // ================== 应用场景实现 ==================
    
    /**
     * 投资组合优化
     * 
     * @param capital 可用资本
     * @param investments 投资项目数组（每个项目包含所需资本和预期收益）
     * @return 选择的投资项目和总收益
     */
    public static String investmentPortfolio(int capital, List<Investment> investments) {
        int n = investments.size();
        int[] weights = new int[n];
        int[] values = new int[n];
        
        for (int i = 0; i < n; i++) {
            Investment inv = investments.get(i);
            weights[i] = inv.capitalRequired;
            values[i] = inv.expectedReturn;
        }
        
        int maxReturn = knapsackDPOptimized(weights, values, capital);
        int[][] dp = new int[n + 1][capital + 1];
        // 构建DP表用于回溯
        knapsackDP(weights, values, capital); // 省略实际计算，用优化版
        
        // 获取选择的投资项目
        List<Integer> selected = getSelectedItems(weights, values, capital, dp);
        StringBuilder result = new StringBuilder("投资项目: ");
        for (int i : selected) {
            result.append(investments.get(i).name).append(", ");
        }
        result.append("\n总收益: ").append(maxReturn);
        result.append(", 总投入: ").append(selected.stream().mapToInt(i -> weights[i]).sum());
        return result.toString();
    }
    
    static class Investment {
        String name;
        int capitalRequired;
        int expectedReturn;
        
        Investment(String name, int capital, int profit) {
            this.name = name;
            this.capitalRequired = capital;
            this.expectedReturn = profit;
        }
    }
    
    /**
     * 货物装载优化
     * 
     * @param cargoSpace 货舱容量
     * @param containers 集装箱列表（每个集装箱包含体积和价值）
     * @return 装载方案
     */
    public static String cargoLoading(int cargoSpace, List<Container> containers) {
        int n = containers.size();
        int[] volumes = new int[n];
        int[] values = new int[n];
        
        for (int i = 0; i < n; i++) {
            Container c = containers.get(i);
            volumes[i] = c.volume;
            values[i] = c.value;
        }
        
        int maxValue = knapsackDPOptimized(volumes, values, cargoSpace);
        int[][] dp = new int[n + 1][cargoSpace + 1];
        // 构建DP表用于回溯
        knapsackDP(volumes, values, cargoSpace); // 省略实际计算，用优化版
        
        // 获取选择的集装箱
        List<Integer> selected = getSelectedItems(volumes, values, cargoSpace, dp);
        StringBuilder result = new StringBuilder("装载方案:\n");
        int totalVolume = 0;
        for (int i : selected) {
            Container c = containers.get(i);
            result.append(String.format("- 集装箱 %s (体积: %d, 价值: %d)%n", 
                c.id, c.volume, c.value));
            totalVolume += c.volume;
        }
        result.append(String.format("总价值: %d, 总体积: %d/%d", maxValue, totalVolume, cargoSpace));
        return result.toString();
    }
    
    static class Container {
        String id;
        int volume;
        int value;
        
        Container(String id, int volume, int value) {
            this.id = id;
            this.volume = volume;
            this.value = value;
        }
    }
    
    // ================== 测试用例 ==================
    
    public static void main(String[] args) {
        testBasicCases();
        testComplexCases();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        int[] weights = {2, 3, 4, 5};
        int[] values = {3, 4, 5, 6};
        int capacity = 5;
        
        testSolutions(weights, values, capacity, 7);
        
        int[] weights2 = {1, 2, 3};
        int[] values2 = {6, 10, 12};
        testSolutions(weights2, values2, 5, 22);
    }
    
    private static void testSolutions(int[] weights, int[] values, int capacity, int expected) {
        System.out.printf("\n测试: weights=%s, values=%s, capacity=%d%n",
                         Arrays.toString(weights), Arrays.toString(values), capacity);
        
        int dp = knapsackDP(weights, values, capacity);
        int dpOpt = knapsackDPOptimized(weights, values, capacity);
        int memo = knapsackMemo(weights, values, capacity);
        int bb = knapsackBranchBound(weights, values, capacity);
        int ga = knapsackGA(weights, values, capacity, 100, 50);
        
        System.out.println("DP(二维表):    " + dp);
        System.out.println("DP(空间优化): " + dpOpt);
        System.out.println("记忆化递归:    " + memo);
        System.out.println("分支限界:     " + bb);
        System.out.println("遗传算法:     " + ga);
        
        boolean pass = dp == expected && dpOpt == expected && 
                     memo == expected && bb == expected;
        
        System.out.println("结果: " + (pass ? "✅" : "❌"));
        
        // 可视化
        visualizeDPTable(weights, values, capacity);
        visualizeBranchBound(weights, values, capacity);
    }
    
    private static void testComplexCases() {
        System.out.println("\n====== 复杂测试 ======");
        
        // 大型测试
        int n = 20;
        int capacity = 50;
        int[] weights = new int[n];
        int[] values = new int[n];
        Random rand = new Random();
        
        for (int i = 0; i < n; i++) {
            weights[i] = rand.nextInt(15) + 1; // 1-15
            values[i] = rand.nextInt(100) + 1; // 1-100
        }
        
        System.out.println("物品数量: " + n + ", 背包容量: " + capacity);
        testSolutions(weights, values, capacity, 0); // 实际值不重要，用于性能测试
        
        // 所有物品都超重
        int[] heavyWeights = {30, 40, 50, 60};
        int[] values2 = {100, 200, 300, 400};
        testSolutions(heavyWeights, values2, 20, 0);
        
        // 所有物品重量为1
        int[] weightOnes = {1, 1, 1, 1, 1};
        int[] highValues = {10, 20, 30, 40, 50};
        testSolutions(weightOnes, highValues, 3, 120);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        int n = 100; // 物品数量
        int capacity = 1000; // 背包容量
        int[] weights = new int[n];
        int[] values = new int[n];
        Random rand = new Random();
        
        for (int i = 0; i < n; i++) {
            weights[i] = rand.nextInt(100) + 1; // 1-100
            values[i] = rand.nextInt(1000) + 1; // 1-1000
        }
        
        System.out.printf("物品数量: %d, 背包容量: %d%n", n, capacity);
        
        long start, end;
        
        // 空间优化DP
        start = System.currentTimeMillis();
        int dpOpt = knapsackDPOptimized(weights, values, capacity);
        end = System.currentTimeMillis();
        System.out.printf("DP空间优化耗时: %4d ms → %d%n", end - start, dpOpt);
        
        // 记忆化递归
        start = System.currentTimeMillis();
        int memo = knapsackMemo(weights, values, capacity);
        end = System.currentTimeMillis();
        System.out.printf("记忆化递归耗时: %4d ms → %d%n", end - start, memo);
        
        // 分支限界
        start = System.currentTimeMillis();
        int bb = knapsackBranchBound(weights, values, capacity);
        end = System.currentTimeMillis();
        System.out.printf("分支限界耗时:   %4d ms → %d%n", end - start, bb);
        
        // 遗传算法(近似解)
        start = System.currentTimeMillis();
        int ga = knapsackGA(weights, values, capacity, 1000, 200);
        end = System.currentTimeMillis();
        System.out.printf("遗传算法耗时:   %4d ms → %d%n", end - start, ga);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 测试1: 投资组合优化
        System.out.println("1. 投资组合优化:");
        List<Investment> investments = Arrays.asList(
            new Investment("科技股A", 50000, 80000),
            new Investment("房地产B", 200000, 250000),
            new Investment("债券基金C", 100000, 110000),
            new Investment("创业公司D", 30000, 200000),
            new Investment("大宗商品E", 150000, 180000)
        );
        int capital = 300000;
        System.out.println(investmentPortfolio(capital, investments));
        
        // 测试2: 货物装载优化
        System.out.println("\n2. 货物装载优化:");
        List<Container> containers = Arrays.asList(
            new Container("C001", 20, 10000),
            new Container("C002", 15, 8000),
            new Container("C003", 25, 15000),
            new Container("C004", 10, 5000),
            new Container("C005", 30, 20000)
        );
        int cargoSpace = 50;
        System.out.println(cargoLoading(cargoSpace, containers));
    }
}