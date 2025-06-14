package main.java.org.dao.qa;

/**
 * 正则表达式匹配 - 实现支持 '.' 和 '*' 的正则表达式功能
 * 
 * <p><b>问题描述</b>:
 * 给定一个字符串 s 和一个字符模式 p，实现支持 '.' 和 '*' 的正则表达式匹配。
 *  - '.' 匹配任意单个字符
 *  - '*' 匹配零个或多个前面的元素
 * 匹配应该覆盖整个字符串 s。
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第10题)
 * 
 * <p><b>解题思路</b>:
 * 采用动态规划(DP)方法，定义 dp[i][j] 表示 s 的前 i 个字符和 p 的前 j 个字符是否匹配。
 * 主要处理三种情况：
 * 1. 普通字符匹配
 * 2. '.' 匹配任意字符
 * 3. '*' 匹配零个或多个前面元素
 * 
 * <p><b>关键算法</b>:
 * 1. 初始化 dp[0][0] = true (空字符串匹配空模式)
 * 2. 处理模式中 '*' 开头的特殊情况
 * 3. 状态转移方程处理三种匹配情况
 * 
 * <p><b>时间复杂度</b>: O(m*n) - m 为字符串长度, n 为模式长度
 * <p><b>空间复杂度</b>: O(m*n) - 可优化至 O(n)
 * 
 * <p><b>应用场景</b>:
 * 1. 正则表达式引擎实现
 * 2. 文本编辑器搜索功能
 * 3. 编译器词法分析
 * 4. 数据验证
 * 5. 生物信息学中的序列匹配
 */

public class RegularExpressionMatching {

    /**
     * 实现正则表达式匹配的核心方法
     * 
     * @param s 输入字符串
     * @param p 正则表达式模式
     * @return 是否完全匹配
     */
    public static boolean isMatch(String s, String p) {
        // 字符串和模式的长度
        int m = s.length();
        int n = p.length();
        
        // dp[i][j] 表示 s[0..i-1] 是否与 p[0..j-1] 匹配
        boolean[][] dp = new boolean[m + 1][n + 1];
        
        // 初始化：空字符串匹配空模式
        dp[0][0] = true;
        
        // 处理模式为 a*b*c*... 可以匹配空字符串的情况
        for (int j = 1; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                // '*' 匹配零个前面的元素：去掉 '*' 和它前面的字符
                dp[0][j] = dp[0][j - 2];
            }
        }
        
        // 动态规划填表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char sc = s.charAt(i - 1);
                char pc = p.charAt(j - 1);
                
                if (pc == '.' || sc == pc) {
                    // 情况1：字符匹配或遇到通配符'.'
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pc == '*') {
                    // 情况2：遇到'*'，需要分开处理
                    char prevChar = p.charAt(j - 2); // '*'前面的字符
                    
                    // 分支1：'*'匹配零个前面的字符
                    boolean matchZero = dp[i][j - 2];
                    
                    // 分支2：'*'匹配一个或多个前面的字符
                    boolean matchOneOrMore = (prevChar == '.' || sc == prevChar) && dp[i - 1][j];
                    
                    dp[i][j] = matchZero || matchOneOrMore;
                }
                // 否则不匹配，保持dp[i][j] = false
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 打印匹配矩阵（用于调试）
     */
    private static void printDP(String s, String p, boolean[][] dp) {
        int m = dp.length;
        int n = dp[0].length;
        
        System.out.print("    ");
        for (char c : p.toCharArray()) {
            System.out.print(c + " ");
        }
        System.out.println();
        
        for (int i = 0; i < m; i++) {
            if (i == 0) {
                System.out.print("  ");
            } else {
                System.out.print(s.charAt(i - 1) + " ");
            }
            
            for (int j = 0; j < n; j++) {
                System.out.print(dp[i][j] ? "T " : "F ");
            }
            System.out.println();
        }
    }
    
    /**
     * 测试用例和主函数
     * 
     * <p><b>测试用例说明</b>:
     * 1. 基础字符匹配
     * 2. '.'通配符匹配
     * 3. '*'匹配零个或多个
     * 4. 复杂组合模式
     * 5. 边界条件测试
     */
    public static void main(String[] args) {
        // 测试用例集
        String[][] testCases = {
            {"aa", "a"},     // false
            {"aa", "a*"},    // true
            {"ab", ".*"},    // true
            {"aab", "c*a*b"},// true
            {"mississippi", "mis*is*p*."},  // false
            {"ab", ".*c"},   // false
            {"a", "ab*"},    // true
            {"aaa", "a*a"},  // true
            {"", ".*"},      // true
            {"", "a*"},      // true
            {"a", ""},       // false
        };
        
        // 预期结果
        boolean[] expected = {
            false, true, true, true, false, 
            false, true, true, true, true, false
        };
        
        // 运行测试
        for (int i = 0; i < testCases.length; i++) {
            String s = testCases[i][0];
            String p = testCases[i][1];
            boolean result = isMatch(s, p);
            boolean pass = result == expected[i];
            
            System.out.println("测试" + (i + 1) + ": \"" + s + "\" 匹配 \"" + p + "\"");
            System.out.println("结果: " + (result ? "匹配 ✅" : "不匹配 ❌"));
            System.out.println("状态: " + (pass ? "通过 ✔️" : "失败 ✘"));
            System.out.println("--------------------------------");
        }
    }
}