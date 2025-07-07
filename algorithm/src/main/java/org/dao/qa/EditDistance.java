package main.java.org.dao.qa;

import java.util.Arrays;

/**
 * Edit Distance（编辑距离）问题
 * 
 * <p><b>问题描述：</b>
 * 给定两个单词 word1 和 word2，计算将 word1 转换成 word2 所使用的最少操作次数。
 * 允许的编辑操作包括：插入、删除、替换
 * 
 * <p><b>示例：</b>
 * 输入：word1 = "horse", word2 = "ros"
 * 输出：3
 * 解释：
 * horse -> rorse (将 'h' 替换为 'r')
 * rorse -> rose (删除 'r')
 * rose -> ros (删除 'e')
 * 
 * <p><b>问题难度：</b>
 * LeetCode 第72题（困难难度）
 */
public class EditDistance {

    // ====================== 方法1：基本动态规划 ======================
    public static int minDistanceDP(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        // 初始化边界条件
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        // 动态规划填表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = min(
                        dp[i - 1][j] + 1,    // 删除
                        dp[i][j - 1] + 1,    // 插入
                        dp[i - 1][j - 1] + 1 // 替换
                    );
                }
            }
        }
        
        return dp[m][n];
    }

    // ====================== 方法2：空间优化动态规划 ======================
    public static int minDistanceOptimized(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        // 初始化第一行
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        // 滚动更新DP值
        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = min(
                        prev[j] + 1,     // 删除
                        curr[j - 1] + 1, // 插入
                        prev[j - 1] + 1  // 替换
                    );
                }
            }
            prev = Arrays.copyOf(curr, n + 1);
        }
        
        return prev[n];
    }

    // ====================== 方法3：递归+备忘录 ======================
    public static int minDistanceRecursive(String word1, String word2) {
        int[][] memo = new int[word1.length() + 1][word2.length() + 1];
        return helper(word1, word2, 0, 0, memo);
    }

    private static int helper(String word1, String word2, int i, int j, int[][] memo) {
        if (i == word1.length()) return word2.length() - j;
        if (j == word2.length()) return word1.length() - i;
        if (memo[i][j] != 0) return memo[i][j];
        
        if (word1.charAt(i) == word2.charAt(j)) {
            memo[i][j] = helper(word1, word2, i + 1, j + 1, memo);
        } else {
            int insert = helper(word1, word2, i, j + 1, memo) + 1;
            int delete = helper(word1, word2, i + 1, j, memo) + 1;
            int replace = helper(word1, word2, i + 1, j + 1, memo) + 1;
            memo[i][j] = min(insert, delete, replace);
        }
        
        return memo[i][j];
    }
    
    // ====================== 应用场景：文本相似度计算 ======================
    /**
     * 计算文本相似度百分比
     * 
     * @param source 原始文本
     * @param target 目标文本
     * @return 0.0 ~ 1.0 的相似度百分比
     */
    public static double textSimilarity(String source, String target) {
        if (source.isEmpty() && target.isEmpty()) return 1.0;
        
        int distance = minDistanceDP(source, target);
        int maxLen = Math.max(source.length(), target.length());
        return 1.0 - (double) distance / maxLen;
    }
    
    // 辅助方法：求三个数的最小值
    private static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    // ====================== 测试方法 ======================
    public static void main(String[] args) {
        testAlgorithm();
        testApplication();
    }
    
    private static void testAlgorithm() {
        System.out.println("===== 算法测试 =====");
        String[][] testCases = {
            {"horse", "ros", "3"},
            {"intention", "execution", "5"},
            {"kitten", "sitting", "3"},
            {"", "hello", "5"},
            {"abc", "abc", "0"},
            {"abcd", "acd", "1"}
        };
        
        int passed = 0;
        for (String[] testCase : testCases) {
            String word1 = testCase[0];
            String word2 = testCase[1];
            int expected = Integer.parseInt(testCase[2]);
            
            int result1 = minDistanceDP(word1, word2);
            int result2 = minDistanceOptimized(word1, word2);
            int result3 = minDistanceRecursive(word1, word2);
            
            boolean allMatch = (result1 == expected) 
                && (result2 == expected) 
                && (result3 == expected);
            
            System.out.printf("输入: \"%s\", \"%s\" | 预期: %d | ", word1, word2, expected);
            System.out.printf("DP: %d | 优化DP: %d | 递归: %d", result1, result2, result3);
            
            if (allMatch) {
                System.out.println("  ✅通过");
                passed++;
            } else {
                System.out.println("  ❌失败");
            }
        }
        System.out.printf("测试通过率: %d/%d\n", passed, testCases.length);
    }
    
    private static void testApplication() {
        System.out.println("\n===== 应用场景测试 =====");
        String[] sources = {
            "Hello World", 
            "The quick brown fox",
            "Java Programming Language",
            "C++ Programming Language"
        };
        
        String target = "Programming in Java";
        
        for (String source : sources) {
            double similarity = textSimilarity(source, target);
            System.out.printf("文本1: '%s'\n文本2: '%s'\n相似度: %.2f%%\n\n", 
                source, target, similarity * 100);
        }
    }
}