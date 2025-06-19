package main.java.org.dao.qa;


/**
 * 编辑距离问题 - 计算将一个字符串转换为另一个字符串所需的最少操作数
 * 
 * <p><b>问题描述</b>：
 * 给定两个单词 word1 和 word2，计算将 word1 转换成 word2 所需的最少操作数。
 * 允许的操作有三种：
 * 1. 插入一个字符
 * 2. 删除一个字符
 * 3. 替换一个字符
 * 
 * <p><b>问题难度</b>：🔥🔥🔥 困难 (LeetCode 第72题)
 * 
 * <p><b>解题思路</b>：
 * 使用动态规划(DP)方法：
 * 1. 定义状态：dp[i][j] 表示 word1 的前 i 个字符转换为 word2 的前 j 个字符所需的最少操作数
 * 2. 状态转移方程：
 *    a. 当 word1[i-1] == word2[j-1] 时：dp[i][j] = dp[i-1][j-1]
 *    b. 否则取以下三种操作中的最小值：
 *       - 插入操作：dp[i][j-1] + 1
 *       - 删除操作：dp[i-1][j] + 1
 *       - 替换操作：dp[i-1][j-1] + 1
 * 3. 初始化边界条件：
 *    - dp[0][j] = j（将空串转换为 word2 的前 j 个字符需要 j 次插入）
 *    - dp[i][0] = i（将 word1 的前 i 个字符转换为空串需要 i 次删除）
 * 
 * <p><b>时间复杂度</b>：O(m*n) - m 和 n 分别为两个单词的长度
 * <p><b>空间复杂度</b>：O(m*n) - 可优化为 O(min(m, n))
 * 
 * <p><b>应用场景</b>：
 * 1. 拼写检查与自动修正
 * 2. DNA序列比对
 * 3. 自然语言处理中的文本相似度计算
 * 4. 版本控制系统中的差异分析
 * 5. 语音识别中的错误纠正
 */

public class EditDistance {
    
    /**
     * 标准动态规划解法
     */
    public static int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        // dp[i][j] 表示 word1[0..i-1] 转换为 word2[0..j-1] 的最小操作数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界条件
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;  // 删除所有字符
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;  // 插入所有字符
        }
        
        // 填充 DP 表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // 情况1：两个字符相同，无需操作
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 情况2：取插入、删除、替换操作中的最小值
                    dp[i][j] = Math.min(
                        Math.min(dp[i][j - 1], dp[i - 1][j]),  // 插入或删除
                        dp[i - 1][j - 1]                       // 替换
                    ) + 1;  // 当前操作计数增加1
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 空间优化版动态规划（滚动数组）
     */
    public static int minDistanceOptimized(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        // 使用一维数组代替二维DP表
        int[] dp = new int[n + 1];
        
        // 初始化：空字符串转换
        for (int j = 0; j <= n; j++) {
            dp[j] = j;
        }
        
        for (int i = 1; i <= m; i++) {
            int prev = dp[0];  // 存储上一行左上角的值（dp[i-1][j-1]）
            dp[0] = i;        // 更新当前行第一列
            
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];  // 保存当前值（将在下一次迭代中成为左上角的值）
                
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[j] = prev;  // 字符匹配，无需操作
                } else {
                    // 取插入、删除、替换操作的最小值
                    dp[j] = Math.min(
                        Math.min(dp[j - 1], dp[j]),  // 插入和删除
                        prev                         // 替换
                    ) + 1;
                }
                
                prev = temp;  // 更新左上角的值
            }
        }
        
        return dp[n];
    }
    
    /**
     * 可视化编辑操作序列
     */
    public static void visualizeOperations(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化DP表
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i-1) == word2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = Math.min(dp[i-1][j-1], Math.min(dp[i][j-1], dp[i-1][j])) + 1;
                }
            }
        }
        
        // 回溯重建操作序列
        System.out.println("\n转换操作序列:");
        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && word1.charAt(i-1) == word2.charAt(j-1)) {
                System.out.printf("保留 '%s' (位置 %d -> %d)%n", 
                                 word1.charAt(i-1), i-1, j-1);
                i--;
                j--;
            } else if (j > 0 && dp[i][j] == dp[i][j-1] + 1) {
                System.out.printf("插入 '%s' (位置 %d) [操作计数+1]%n", 
                                 word2.charAt(j-1), j-1);
                j--;
            } else if (i > 0 && dp[i][j] == dp[i-1][j] + 1) {
                System.out.printf("删除 '%s' (位置 %d) [操作计数+1]%n", 
                                 word1.charAt(i-1), i-1);
                i--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i-1][j-1] + 1) {
                System.out.printf("替换 '%s' (位置 %d) 为 '%s' (位置 %d) [操作计数+1]%n", 
                                 word1.charAt(i-1), i-1, 
                                 word2.charAt(j-1), j-1);
                i--;
                j--;
            }
        }
    }
    
    /**
     * 打印DP表（调试用）
     */
    private static void printDPTable(String word1, String word2, int[][] dp) {
        System.out.println("\nDP表:");
        System.out.print("      ");
        for (char c : word2.toCharArray()) {
            System.out.print(c + "  ");
        }
        System.out.println();
        
        for (int i = 0; i <= word1.length(); i++) {
            if (i == 0) {
                System.out.print("   ");
            } else {
                System.out.print(word1.charAt(i-1) + "  ");
            }
            for (int j = 0; j <= word2.length(); j++) {
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * 测试用例和主函数
     */
    public static void main(String[] args) {
        String[][] testCases = {
            {"horse", "ros"},        // 3
            {"intention", "execution"}, // 5
            {"kitten", "sitting"},    // 3
            {"sunday", "saturday"},   // 3
            {"", "abc"},             // 3
            {"abc", ""},             // 3
            {"abc", "abc"},          // 0
            {"a", "b"}               // 1
        };
        
        int[] expected = {3, 5, 3, 3, 3, 3, 0, 1};
        
        System.out.println("编辑距离算法测试:");
        for (int i = 0; i < testCases.length; i++) {
            String word1 = testCases[i][0];
            String word2 = testCases[i][1];
            
            int result1 = minDistance(word1, word2);
            int result2 = minDistanceOptimized(word1, word2);
            
            System.out.println("\n====== 测试" + (i+1) + " ======");
            System.out.println("输入: \"" + word1 + "\" -> \"" + word2 + "\"");
            System.out.println("DP结果: " + result1);
            System.out.println("优化DP结果: " + result2);
            System.out.println("预期结果: " + expected[i]);
            
            boolean pass = result1 == expected[i] && result2 == expected[i];
            System.out.println("状态: " + (pass ? "通过 ✅" : "失败 ❌"));
            
            if (!pass || word1.length() < 10) {
                visualizeOperations(word1, word2);
            }
        }
    }
}