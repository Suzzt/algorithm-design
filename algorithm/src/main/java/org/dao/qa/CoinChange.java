package main.java.org.dao.qa;

import java.util.*;

/**
 * 硬币找零问题（Coin Change Problem）
 * 
 * <p><b>问题描述：</b>
 * 给定不同面额的硬币 coins 和一个总金额 amount。计算可以凑成总金额所需的最少的硬币个数。
 * 如果没有任何一种硬币组合能组成总金额，返回 -1。硬币数量无限。
 * 
 * <p><b>示例：</b>
 * 输入: coins = [1, 2, 5], amount = 11
 * 输出: 3 
 * 解释: 11 = 5 + 5 + 1
 * 
 * <p><b>问题难度：</b>
 * LeetCode 第322题（中等难度）
 * 
 * <p><b>解题思路：</b>
 * 1. 基础动态规划解法（时间复杂度O(n * k)，空间复杂度O(n)）
 * 2. 贪心+DFS回溯（适合小面额硬币）
 * 3. BFS搜索最小硬币数
 * 
 * <p><b>应用场景：</b>
 * 1. 自动售货机找零系统
 * 2. 银行系统最小现金支付
 * 3. 在线支付系统的零钱组合
 * 4. 游戏中的资源兑换系统
 */
public class CoinChange {
    
    // ========================= 解法1: 基础动态规划 =========================
    
    /**
     * 动态规划解法
     * 
     * @param coins 硬币面值数组
     * @param amount 目标金额
     * @return 最少硬币数，无法组合返回-1
     */
    public static int coinChangeDP(int[] coins, int amount) {
        // 边界情况处理
        if (amount < 1) return 0;
        if (coins == null || coins.length == 0) return -1;
        
        int[] dp = new int[amount + 1]; // dp[i]表示组成金额i所需的最小硬币数
        Arrays.fill(dp, amount + 1);  // 初始化为一个不可能的值（大于目标值）
        dp[0] = 0; // 金额0需要0个硬币
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    // ========================= 解法2: BFS搜索最小硬币数 =========================
    
    /**
     * BFS广度优先搜索解法（适合硬币面值分布合理时）
     * 
     * @param coins 硬币面值数组
     * @param amount 目标金额
     * @return 最少硬币数，无法组合返回-1
     */
    public static int coinChangeBFS(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (coins == null || coins.length == 0) return -1;
        
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[amount + 1]; // 避免重复计算
        int level = 0;
        
        queue.offer(0);
        visited[0] = true;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            level++;
            
            for (int i = 0; i < size; i++) {
                int sum = queue.poll();
                
                for (int coin : coins) {
                    int newSum = sum + coin;
                    
                    if (newSum == amount) {
                        return level;
                    }
                    
                    if (newSum < amount && !visited[newSum]) {
                        visited[newSum] = true;
                        queue.offer(newSum);
                    }
                }
            }
        }
        
        return -1;
    }
    
    // ========================= 解法3: 回溯+剪枝（贪心） =========================
    
    /**
     * 回溯法（贪心思想）找所有组合
     * 
     * @param coins 硬币面值数组
     * @param amount 目标金额
     * @return 所有可能的硬币组合（最少组合在前）
     */
    public static List<List<Integer>> findAllCombinations(int[] coins, int amount) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(coins); // 排序以便剪枝
        backtrack(coins, amount, coins.length - 1, new ArrayList<>(), result);
        return result;
    }
    
    private static void backtrack(int[] coins, int amount, int idx, 
                                List<Integer> current, List<List<Integer>> result) {
        if (amount == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        for (int i = idx; i >= 0; i--) {
            if (coins[i] <= amount && (current.isEmpty() || coins[i] <= current.get(current.size()-1))) {
                current.add(coins[i]);
                backtrack(coins, amount - coins[i], i, current, result);
                current.remove(current.size() - 1);
            }
        }
    }
    
    /**
     * 贪心+DFS找最小组合（假设硬币无限且面值有公因子）
     * 
     * @param coins 硬币面值数组
     * @param amount 目标金额
     * @return 最小硬币数，无法组合返回-1
     */
    public static int coinChangeGreedyDFS(int[] coins, int amount) {
        Arrays.sort(coins); // 从小到大排序
        // 从大到小排列硬币面值
        int[] sortedCoins = Arrays.copyOf(coins, coins.length);
        for (int i = 0; i < coins.length / 2; i++) {
            int temp = sortedCoins[i];
            sortedCoins[i] = sortedCoins[coins.length - 1 - i];
            sortedCoins[coins.length - 1 - i] = temp;
        }
        
        int[] min = {Integer.MAX_VALUE};
        dfs(sortedCoins, amount, 0, 0, min);
        return min[0] == Integer.MAX_VALUE ? -1 : min[0];
    }
    
    private static void dfs(int[] coins, int amount, int count, int idx, int[] min) {
        if (amount == 0) {
            min[0] = Math.min(min[0], count);
            return;
        }
        
        if (idx == coins.length) return;
        
        // 剪枝：如果当前硬币面值太大直接跳过
        // 剪枝：如果当前硬币数已经超过最小值，提前结束
        if (count + (amount + coins[idx] - 1) / coins[idx] >= min[0]) {
            return;
        }
        
        // 尝试使用当前面值的最大可能次数（从多到少）
        int maxK = amount / coins[idx];
        for (int k = maxK; k >= 0; k--) {
            if (count + k < min[0]) {
                dfs(coins, amount - k * coins[idx], count + k, idx + 1, min);
            } else {
                break;
            }
        }
    }
    
    // ========================= 应用场景部分 =========================
    
    /**
     * 自动售货机找零系统
     * 
     * @param amount 需找零金额
     * @param availableCoins 机器当前可用硬币（面值:数量）
     * @return 找零方案（硬币组合），无法找零返回空列表
     */
    public static List<String> vendingMachineChange(int amount, Map<Integer, Integer> availableCoins) {
        List<String> results = new ArrayList<>();
        List<Integer> coinList = new ArrayList<>();
        Map<Integer, Integer> coinCount = new HashMap<>();
        
        // 生成可用硬币列表（考虑数量）
        for (Map.Entry<Integer, Integer> entry : availableCoins.entrySet()) {
            int coin = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                coinList.add(coin);
                coinCount.put(coin, coinCount.getOrDefault(coin, 0) + 1);
            }
        }
        
        // 转换为数组
        int[] coins = coinList.stream().mapToInt(i -> i).toArray();
        int minCoins = coinChangeDP(coins, amount);
        if (minCoins != -1) {
            // 查找所有组合
            List<List<Integer>> combinations = findAllCombinations(coins, amount);
            for (List<Integer> combo : combinations) {
                if (combo.size() == minCoins && isValidCombination(combo, coinCount)) {
                    results.add(formatCombination(combo));
                }
            }
        }
        
        return results;
    }
    
    // 辅助方法：检查组合是否在可用硬币数量范围内
    private static boolean isValidCombination(List<Integer> combo, Map<Integer, Integer> coinCount) {
        Map<Integer, Integer> used = new HashMap<>();
        for (int coin : combo) {
            used.put(coin, used.getOrDefault(coin, 0) + 1);
            if (used.get(coin) > coinCount.getOrDefault(coin, 0)) {
                return false;
            }
        }
        return true;
    }
    
    // 格式化组合输出
    private static String formatCombination(List<Integer> combo) {
        Collections.sort(combo);
        Collections.reverse(combo);
        StringBuilder sb = new StringBuilder();
        for (int coin : combo) {
            sb.append(coin).append("+");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    /**
     * 游戏资源兑换系统
     * 
     * @param resources 拥有的资源值 [value]
     * @param targetValue 需要兑换的资源值
     * @return 最少资源使用方案
     */
    public static List<Integer> gameResourceExchange(int[] resources, int targetValue) {
        // 资源兑换相当于硬币找零
        int minCount = coinChangeDP(resources, targetValue);
        List<List<Integer>> allCombos = findAllCombinations(resources, targetValue);
        
        if (allCombos.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 找最小数量的组合
        for (List<Integer> combo : allCombos) {
            if (combo.size() == minCount) {
                return combo;
            }
        }
        
        return allCombos.get(0);
    }
    
    /**
     * 银行系统最小现金支付方案
     * 
     * @param amount 支付金额
     * @param denominations 可用面额（考虑面值优先级）
     * @return 最少张数组合
     */
    public static Map<Integer, Integer> bankPayment(int amount, int[] denominations) {
        Arrays.sort(denominations); // 从小排序到大
        
        // 动态规划找最小张数
        int minCoins = coinChangeDP(denominations, amount);
        if (minCoins == -1) {
            return Collections.emptyMap();
        }
        
        // 找出所有最小组合
        List<List<Integer>> allCombos = findAllCombinations(denominations, amount);
        
        // 找到面值最优的组合（优先使用大面值）
        List<Integer> bestCombo = null;
        int maxFaceValue = -1;
        
        for (List<Integer> combo : allCombos) {
            if (combo.size() == minCoins) {
                int faceValue = combo.stream().max(Integer::compareTo).get();
                if (faceValue > maxFaceValue) {
                    maxFaceValue = faceValue;
                    bestCombo = combo;
                }
            }
        }
        
        // 统计面值数量
        Map<Integer, Integer> result = new TreeMap<>(Collections.reverseOrder());
        for (int coin : bestCombo) {
            result.put(coin, result.getOrDefault(coin, 0) + 1);
        }
        
        return result;
    }
    
    // ========================= 测试方法 =========================
    
    public static void main(String[] args) {
        testAlgorithms();
        testApplicationScenarios();
    }
    
    private static void testAlgorithms() {
        System.out.println("====== 算法基本测试 ======");
        int[] coins = {1, 2, 5};
        int amount = 11;
        
        System.out.println("测试用例: 硬币 = " + Arrays.toString(coins) + ", 金额 = " + amount);
        System.out.println("动态规划结果: " + coinChangeDP(coins, amount));
        System.out.println("BFS结果: " + coinChangeBFS(coins, amount));
        System.out.println("贪心+DFS结果: " + coinChangeGreedyDFS(coins, amount));
        
        System.out.println("\n所有组合:");
        List<List<Integer>> combinations = findAllCombinations(coins, amount);
        for (int i = 0; i < Math.min(5, combinations.size()); i++) {
            System.out.println("  组合" + (i+1) + ": " + combinations.get(i));
        }
        
        // 大金额测试
        int[] coins2 = {1, 5, 10, 25, 50};
        int amount2 = 87;
        System.out.println("\n大金额测试: 硬币 = " + Arrays.toString(coins2) + ", 金额 = " + amount2);
        System.out.println("动态规划结果: " + coinChangeDP(coins2, amount2));
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 自动售货机找零系统
        System.out.println("1. 自动售货机找零系统测试:");
        Map<Integer, Integer> availableCoins = new HashMap<>();
        availableCoins.put(10, 5); // 5个10元硬币
        availableCoins.put(5, 10); // 10个5元硬币
        availableCoins.put(1, 20); // 20个1元硬币
        int changeAmount = 48;
        
        System.out.println("找零金额: " + changeAmount);
        System.out.println("可用硬币: " + availableCoins);
        
        List<String> changeOptions = vendingMachineChange(changeAmount, availableCoins);
        System.out.println("可能的找零方案:");
        for (int i = 0; i < Math.min(3, changeOptions.size()); i++) {
            System.out.println("  " + (i+1) + ") " + changeOptions.get(i));
        }
        
        // 场景2: 游戏资源兑换系统
        System.out.println("\n2. 游戏资源兑换系统测试:");
        int[] resources = {5, 10, 20, 50};
        int targetValue = 80;
        List<Integer> resourceCombo = gameResourceExchange(resources, targetValue);
        System.out.println("目标资源值: " + targetValue);
        System.out.println("资源组合: " + resourceCombo);
        
        // 场景3: 银行系统最小现金支付
        System.out.println("\n3. 银行支付系统测试:");
        int[] bankDenominations = {1, 5, 10, 20, 50, 100};
        int paymentAmount = 378;
        Map<Integer, Integer> paymentPlan = bankPayment(paymentAmount, bankDenominations);
        System.out.println("支付金额: " + paymentAmount);
        System.out.println("最小张数支付方案:");
        for (Map.Entry<Integer, Integer> entry : paymentPlan.entrySet()) {
            System.out.println("  " + entry.getKey() + "元 x " + entry.getValue() + " 张");
        }
    }
}