package main.java.org.dao.qa;

import java.util.*;

/**
 * 零钱兑换问题
 * 
 * <p><b>问题描述</b>:
 * 给定不同面额的硬币coins和一个总金额amount，计算可以凑成总金额所需的最少硬币个数。
 * 如果没有任何一种硬币组合能组成总金额，返回-1。
 * 
 * <p><b>示例</b>:
 * 输入: coins = [1, 2, 5], amount = 11
 * 输出: 3 (因为 11 = 5 + 5 + 1)
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第322题)
 * 
 * <p><b>解题思路</b>:
 * 1. 动态规划: 自底向上构建最少硬币数
 * 2. BFS(广度优先搜索): 找出最小兑换路径
 * 3. 贪心算法: 在特殊条件下有效（如可整除的硬币）
 * 4. 记忆化递归: 自顶向下的递归解法
 * 
 * <p><b>时间复杂度</b>:
 *  动态规划: O(n×amount) - n为硬币种类数
 *  BFS: O(amount×k) - k为硬币组合数
 *  贪心: O(n log n) - 需要排序
 * 
 * <p><b>应用场景</b>:
 * 1. 自动售货机找零系统
 * 2. 货币兑换服务
 * 3. 在线支付系统
 * 4. 资源分配优化
 * 5. 游戏道具兑换
 */

public class CoinChange {

    // ========================= 解法1: 动态规划 =========================
    
    /**
     * 动态规划解法
     * 
     * @param coins 可用硬币面额数组
     * @param amount 目标金额
     * @return 最少硬币数，如无法兑换返回-1
     */
    public static int coinChangeDP(int[] coins, int amount) {
        // 处理边界情况
        if (amount < 0) return -1;
        if (amount == 0) return 0;
        
        // 初始化dp数组
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1); // 初始为极大值
        dp[0] = 0; // 金额0不需要硬币
        
        // 填充DP表
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    // ========================= 解法2: BFS方法 =========================
    
    /**
     * BFS解法 - 找出最小兑换路径
     * 
     * @param coins 可用硬币面额数组
     * @param amount 目标金额
     * @return 最少硬币数，如无法兑换返回-1
     */
    public static int coinChangeBFS(int[] coins, int amount) {
        if (amount == 0) return 0;
        
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[amount + 1];
        int steps = 0;
        
        queue.offer(0);
        visited[0] = true;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            steps++;
            
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                
                for (int coin : coins) {
                    int next = current + coin;
                    
                    if (next == amount) {
                        return steps;
                    }
                    
                    if (next < amount && !visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
                }
            }
        }
        
        return -1;
    }
    
    // ========================= 解法3: 贪心算法 =========================
    
    /**
     * 贪心解法 - 适用于硬币面额可整除的情况
     * 
     * @param coins 可用硬币面额数组
     * @param amount 目标金额
     * @return 最少硬币数，如无法兑换返回-1
     */
    public static int coinChangeGreedy(int[] coins, int amount) {
        // 排序硬币面额（降序）
        int[] sortedCoins = Arrays.copyOf(coins, coins.length);
        Arrays.sort(sortedCoins);
        
        // 贪心计算
        int count = 0;
        int remaining = amount;
        
        for (int i = sortedCoins.length - 1; i >= 0; i--) {
            int coin = sortedCoins[i];
            if (coin <= remaining) {
                int numCoins = remaining / coin;
                count += numCoins;
                remaining %= coin;
                
                if (remaining == 0) {
                    return count;
                }
            }
        }
        
        return -1;
    }
    
    // ========================= 解法4: 记忆化递归 =========================
    
    /**
     * 记忆化递归解法
     * 
     * @param coins 可用硬币面额数组
     * @param amount 目标金额
     * @return 最少硬币数，如无法兑换返回-1
     */
    public static int coinChangeMemo(int[] coins, int amount) {
        int[] memo = new int[amount + 1];
        Arrays.fill(memo, -2); // -2表示未计算
        return dfs(coins, amount, memo);
    }
    
    private static int dfs(int[] coins, int amount, int[] memo) {
        // 边界条件
        if (amount < 0) return -1;
        if (amount == 0) return 0;
        
        // 检查记忆
        if (memo[amount] != -2) {
            return memo[amount];
        }
        
        int min = Integer.MAX_VALUE;
        
        // 尝试所有硬币
        for (int coin : coins) {
            int res = dfs(coins, amount - coin, memo);
            if (res >= 0 && res < min) {
                min = res + 1;
            }
        }
        
        // 保存结果
        memo[amount] = (min == Integer.MAX_VALUE) ? -1 : min;
        return memo[amount];
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化动态规划表
     * 
     * @param coins 可用硬币面额数组
     * @param amount 目标金额
     */
    public static void visualizeDP(int[] coins, int amount) {
        System.out.println("\n动态规划表可视化:");
        System.out.println("硬币: " + Arrays.toString(coins));
        System.out.println("目标金额: " + amount);
        
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        
        // 打印表头
        System.out.print("金额 ");
        for (int i = 0; i <= amount; i++) {
            System.out.printf("%4d", i);
        }
        System.out.println();
        
        // 填充并打印表格
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
            
            System.out.printf("%4d: ", i);
            for (int j = 0; j <= i; j++) {
                if (dp[j] == amount + 1) {
                    System.out.print("  ∞ ");
                } else {
                    System.out.printf("%4d", dp[j]);
                }
            }
            System.out.println();
        }
        
        System.out.println("最少硬币数: " + 
                          (dp[amount] > amount ? -1 : dp[amount]));
    }
    
    /**
     * 可视化BFS路径
     * 
     * @param coins 可用硬币面额数组
     * @param amount 目标金额
     */
    public static void visualizeBFS(int[] coins, int amount) {
        System.out.println("\nBFS遍历过程可视化:");
        System.out.println("起点: 0");
        
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[amount + 1];
        int steps = 0;
        Map<Integer, Integer> parentMap = new HashMap<>();
        
        queue.offer(0);
        visited[0] = true;
        parentMap.put(0, null);
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            steps++;
            System.out.printf("步骤 %d: %n", steps);
            
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                
                for (int coin : coins) {
                    int next = current + coin;
                    
                    // 打印当前尝试
                    System.out.printf("  尝试: %d + %d = %d", current, coin, next);
                    
                    if (next == amount) {
                        System.out.println(" √ 找到方案!");
                        System.out.println("\n兑换路径:");
                        printPath(parentMap, next);
                        return;
                    }
                    
                    if (next < amount && !visited[next]) {
                        System.out.println(" → 加入队列");
                        visited[next] = true;
                        queue.offer(next);
                        parentMap.put(next, current);
                    } else {
                        System.out.println();
                    }
                }
            }
        }
        
        System.out.println("\n无法兑换目标金额");
    }
    
    // 打印兑换路径
    private static void printPath(Map<Integer, Integer> parentMap, int amount) {
        List<Integer> path = new ArrayList<>();
        Integer current = amount;
        
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }
        
        Collections.reverse(path);
        
        System.out.print("路径: ");
        for (int i = 1; i < path.size(); i++) {
            System.out.printf("%d → ", path.get(i) - path.get(i - 1));
        }
        System.out.println("\b\b= " + amount);
    }
    
    // ========================= 应用场景 =========================
    
    /**
     * 自动售货机找零系统
     * 
     * @param coins 系统支持的硬币面额
     * @param amount 需要找回的金额
     * @param customerCoins 顾客投入的硬币
     * @return 需要退还给顾客的硬币组合
     */
    public static List<Integer> vendingMachineChange(
        int[] coins, int amount, List<Integer> customerCoins) {
        
        // 计算顾客已付款
        int paid = customerCoins.stream().mapToInt(Integer::intValue).sum();
        int changeNeeded = paid - amount;
        
        // 验证付款
        if (changeNeeded < 0) {
            throw new IllegalArgumentException("付款不足");
        }
        if (changeNeeded == 0) {
            return Collections.emptyList();
        }
        
        // 计算找零组合
        return findMinChange(coins, changeNeeded);
    }
    
    // 寻找最少硬币的找零组合
    private static List<Integer> findMinChange(int[] coins, int amount) {
        // 边界情况
        if (amount == 0) return Collections.emptyList();
        
        // 初始化DP表和选择表
        int[] dp = new int[amount + 1];
        int[] choices = new int[amount + 1]; // 记录最后使用的硬币
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        
        // 填充DP表
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i && dp[i - coin] + 1 < dp[i]) {
                    dp[i] = dp[i - coin] + 1;
                    choices[i] = coin;
                }
            }
        }
        
        // 回溯找零组合
        if (dp[amount] > amount) return null;
        
        List<Integer> change = new ArrayList<>();
        int remaining = amount;
        while (remaining > 0) {
            int coin = choices[remaining];
            change.add(coin);
            remaining -= coin;
        }
        
        return change;
    }
    
    /**
     * 货币兑换服务
     * 
     * @param fromCurrency 源币种
     * @param toCurrency 目标币种
     * @param amount 兑换金额
     * @param exchangeRate 汇率表
     * @return 兑换后的金额和过程
     */
    public static String currencyExchange(
        String fromCurrency, String toCurrency, int amount, 
        Map<String, Map<String, Double>> exchangeRate) {
        
        // 创建币种数组
        List<String> currencies = new ArrayList<>(exchangeRate.keySet());
        int n = currencies.size();
        
        // 创建硬币系统模拟兑换比例
        int[] coins = new int[n];
        for (int i = 0; i < n; i++) {
            coins[i] = i + 1;
        }
        
        // 将问题映射为硬币找零问题
        int target = n; // 目标位置设为n
        
        // 计算路径
        List<Integer> path = findMinChange(coins, target);
        
        // 构建兑换路径
        StringBuilder pathStr = new StringBuilder();
        int current = 0;
        for (int coin : path) {
            String nextCur = currencies.get(current + coin - 1);
            pathStr.append(currencies.get(current)).append(" → ").append(nextCur).append(" ");
            current += coin;
        }
        
        return String.format(
            "兑换路径: %s= %s 金额: %.2f %s", 
            pathStr, toCurrency, amount * 0.95, toCurrency);
    }
    
    /**
     * 资源分配优化
     * 
     * @param resources 可用资源数量数组
     * @param requirements 任务需求数组
     * @return 最少的任务分配次数
     */
    public static int resourceAllocation(int[] resources, int[] requirements) {
        // 将问题转化为硬币问题：资源是硬币，需求是金额
        int maxRequirement = Arrays.stream(requirements).max().getAsInt();
        int total = Arrays.stream(resources).sum();
        
        // 使用DP计算每个需求的最小分配
        int[] dp = new int[maxRequirement + 1];
        Arrays.fill(dp, resources.length + 1);
        dp[0] = 0;
        
        for (int i = 1; i <= maxRequirement; i++) {
            for (int res : resources) {
                if (res <= i) {
                    dp[i] = Math.min(dp[i], dp[i - res] + 1);
                }
            }
        }
        
        // 计算总分配次数
        int totalAssignments = 0;
        for (int req : requirements) {
            if (req > 0) {
                if (dp[req] > resources.length) {
                    return -1; // 无法满足
                }
                totalAssignments += dp[req];
            }
        }
        
        return totalAssignments;
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
        int[] coins1 = {1, 2, 5};
        int amount1 = 11;
        testSolution(coins1, amount1, 3);
        
        int[] coins2 = {2};
        int amount2 = 3;
        testSolution(coins2, amount2, -1);
        
        int[] coins3 = {1};
        int amount3 = 0;
        testSolution(coins3, amount3, 0);
    }
    
    private static void testSolution(int[] coins, int amount, int expected) {
        System.out.printf("\n测试: coins=%s, amount=%d%n", 
                         Arrays.toString(coins), amount);
        
        int dp = coinChangeDP(coins, amount);
        int bfs = coinChangeBFS(coins, amount);
        int greedy = coinChangeGreedy(coins, amount);
        int memo = coinChangeMemo(coins, amount);
        
        System.out.println("动态规划: " + dp);
        System.out.println("BFS:     " + bfs);
        System.out.println("贪心算法: " + greedy);
        System.out.println("记忆化递归: " + memo);
        
        boolean pass = dp == expected && bfs == expected && 
                     memo == expected;
        
        System.out.println("结果: " + (pass ? "✅" : "❌"));
        
        // 可视化小规模问题
        if (amount <= 20) {
            visualizeDP(coins, amount);
            visualizeBFS(coins, amount);
        }
    }
    
    private static void testComplexCases() {
        System.out.println("\n====== 复杂测试 ======");
        
        // 美国硬币系统测试
        int[] usCoins = {1, 5, 10, 25};
        System.out.println("美国硬币系统 [1,5,10,25]");
        testSolution(usCoins, 41, 4);
        
        // 欧洲硬币系统测试
        int[] euCoins = {1, 2, 5, 10, 20, 50};
        testSolution(euCoins, 65, 3);
        
        // 无法凑成的金额
        int[] coins = {3, 7};
        testSolution(coins, 11, 3);
        testSolution(coins, 5, -1);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        int[] coins = {1, 2, 5, 10, 20, 50};
        int amount = 1000;
        
        long start, end;
        
        start = System.currentTimeMillis();
        coinChangeDP(coins, amount);
        end = System.currentTimeMillis();
        System.out.printf("动态规划用时: %d ms%n", end - start);
        
        start = System.currentTimeMillis();
        coinChangeBFS(coins, amount);
        end = System.currentTimeMillis();
        System.out.printf("BFS用时:     %d ms%n", end - start);
        
        start = System.currentTimeMillis();
        coinChangeMemo(coins, amount);
        end = System.currentTimeMillis();
        System.out.printf("记忆化递归用时: %d ms%n", end - start);
        
        start = System.currentTimeMillis();
        coinChangeGreedy(coins, amount);
        end = System.currentTimeMillis();
        System.out.printf("贪心算法用时: %d ms%n", end - start);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 测试1: 自动售货机找零系统
        System.out.println("1. 自动售货机找零系统:");
        int[] vendingCoins = {1, 5, 10, 25}; // 美分
        int itemPrice = 75; // 商品价格75美分
        List<Integer> customerPayment = Arrays.asList(25, 25, 25, 25); // 顾客支付1美元
        
        List<Integer> change = vendingMachineChange(
            vendingCoins, itemPrice, customerPayment);
        System.out.println("商品价格: " + itemPrice + "美分");
        System.out.println("顾客支付: " + customerPayment + "美分");
        System.out.println("找零硬币: " + change + "美分");
        
        // 测试2: 货币兑换服务
        System.out.println("\n2. 货币兑换服务:");
        Map<String, Map<String, Double>> exchangeRate = getStringMapMap();

        String result = currencyExchange("USD", "GBP", 100, exchangeRate);
        System.out.println(result);
        
        // 测试3: 资源分配优化
        System.out.println("\n3. 资源分配优化:");
        int[] resources = {1, 3, 5}; // 可用资源单位
        int[] requirements = {7, 4, 9}; // 任务需求
        int assignments = resourceAllocation(resources, requirements);
        System.out.println("资源: " + Arrays.toString(resources));
        System.out.println("任务需求: " + Arrays.toString(requirements));
        System.out.println("最少分配次数: " + assignments);
    }

    private static Map<String, Map<String, Double>> getStringMapMap() {
        Map<String, Map<String, Double>> exchangeRate = new HashMap<>();

        // USD 汇率
        Map<String, Double> usdMap = new HashMap<>();
        usdMap.put("EUR", 0.85);
        usdMap.put("JPY", 110.0);
        exchangeRate.put("USD", usdMap);

        // EUR 汇率
        Map<String, Double> eurMap = new HashMap<>();
        eurMap.put("JPY", 129.0);
        eurMap.put("GBP", 0.9);
        exchangeRate.put("EUR", eurMap);

        // JPY 汇率
        Map<String, Double> jpyMap = new HashMap<>();
        jpyMap.put("GBP", 0.0067);
        exchangeRate.put("JPY", jpyMap);

        // GBP 汇率
        Map<String, Double> gbpMap = new HashMap<>();
        gbpMap.put("USD", 1.38);
        exchangeRate.put("GBP", gbpMap);
        return exchangeRate;
    }
}