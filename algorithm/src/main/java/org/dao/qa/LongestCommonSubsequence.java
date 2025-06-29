package main.java.org.dao.qa;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 最长公共子序列问题（Longest Common Subsequence, LCS）
 * 
 * <p><b>问题描述</b>:
 * 给定两个字符串text1和text2，返回这两个字符串的最长公共子序列的长度。
 * 子序列：通过删除某些字符但不改变字符相对顺序形成的新字符串。
 * 
 * <p><b>示例</b>:
 * 输入: text1 = "abcde", text2 = "ace"
 * 输出: 3 ("ace")
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第1143题)
 * 
 * <p><b>解题思路</b>:
 * 1. 动态规划: 二维DP表记录LCS长度
 * 2. 空间优化: 滚动数组减少空间复杂度
 * 3. 回溯重建: 根据DP表重建LCS字符串
 * 4. 应用扩展: 文本差异比较、DNA序列比对等
 * 
 * <p><b>时间复杂度</b>:
 *  动态规划: O(m*n) - m和n分别为两个字符串长度
 *  空间优化: O(min(m,n))
 * 
 * <p><b>应用场景</b>:
 * 1. 版本控制系统（如Git的diff功能）
 * 2. DNA序列比对（生物信息学）
 * 3. 文本相似度计算
 * 4. 抄袭检测系统
 * 5. 语音识别中的语音序列匹配
 */

public class LongestCommonSubsequence {
    
    // ========================= 解法1: 动态规划 =========================
    
    /**
     * 动态规划解法
     * 
     * @param text1 字符串1
     * @param text2 字符串2
     * @return 最长公共子序列长度
     */
    public static int lcsDP(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
    
    // ========================= 解法2: 空间优化 =========================
    
    /**
     * 动态规划空间优化版
     * 
     * @param text1 字符串1
     * @param text2 字符串2
     * @return 最长公共子序列长度
     */
    public static int lcsOptimized(String text1, String text2) {
        // 确保text2是较短的字符串
        if (text1.length() < text2.length()) {
            String temp = text1;
            text1 = text2;
            text2 = temp;
        }
        
        int m = text1.length();
        int n = text2.length();
        int[] dp = new int[n + 1];
        
        for (int i = 1; i <= m; i++) {
            int prev = 0; // 保存左上角的值
            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // 保存当前值
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[j] = prev + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                prev = temp; // 更新左上角值
            }
        }
        
        return dp[n];
    }
    
    // ========================= 解法3: 重建LCS字符串 =========================
    
    /**
     * 重建最长公共子序列字符串
     * 
     * @param text1 字符串1
     * @param text2 字符串2
     * @return 最长公共子序列字符串
     */
    public static String buildLCS(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 回溯重建LCS
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                lcs.append(text1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        
        return lcs.reverse().toString();
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化DP表
     * 
     * @param text1 字符串1
     * @param text2 字符串2
     */
    public static void visualizeDPTable(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 打印表头
        System.out.println("\nDP表可视化:");
        System.out.print("      ");
        for (char c : text2.toCharArray()) {
            System.out.print(c + "  ");
        }
        System.out.println();
        
        // 打印DP表
        for (int i = 0; i <= m; i++) {
            if (i > 0) {
                System.out.print(text1.charAt(i - 1) + " ");
            } else {
                System.out.print("  ");
            }
            
            for (int j = 0; j <= n; j++) {
                System.out.printf("%2d ", dp[i][j]);
            }
            System.out.println();
        }
        
        // 打印LCS长度
        System.out.println("\n最长公共子序列长度: " + dp[m][n]);
    }
    
    /**
     * 可视化LCS重建过程
     * 
     * @param text1 字符串1
     * @param text2 字符串2
     */
    public static void visualizeLCSRebuild(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        System.out.println("\nLCS重建过程:");
        StringBuilder lcs = new StringBuilder();
        int i = m, j = n;
        int step = 1;
        
        while (i > 0 && j > 0) {
            System.out.printf("步骤 %d: i=%d (%s), j=%d (%s)%n", 
                             step++, i, text1.charAt(i-1), j, text2.charAt(j-1));
            
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                System.out.printf("  匹配: %s == %s, 添加 '%s' 到LCS%n", 
                                 text1.charAt(i-1), text2.charAt(j-1), text1.charAt(i-1));
                lcs.append(text1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                System.out.printf("  向左移动: dp[%d][%d]=%d > dp[%d][%d]=%d%n", 
                                 i-1, j, dp[i-1][j], i, j-1, dp[i][j-1]);
                i--;
            } else {
                System.out.printf("  向上移动: dp[%d][%d]=%d <= dp[%d][%d]=%d%n", 
                                 i-1, j, dp[i-1][j], i, j-1, dp[i][j-1]);
                j--;
            }
        }
        
        String result = lcs.reverse().toString();
        System.out.println("\n重建的LCS: \"" + result + "\"");
        System.out.println("长度: " + result.length());
    }
    
    // ========================= 应用场景 =========================
    
    /**
     * 文本差异比较（类似diff工具）
     * 
     * @param text1 文本1
     * @param text2 文本2
     * @return 差异结果
     */
    public static String textDiff(String text1, String text2) {
        List<String> lcs = findLCSLines(text1, text2);
        String[] lines1 = text1.split("\n");
        String[] lines2 = text2.split("\n");
        
        StringBuilder diff = new StringBuilder();
        int i = 0, j = 0;
        
        while (i < lines1.length || j < lines2.length) {
            if (i < lines1.length && j < lines2.length && lines1[i].equals(lines2[j])) {
                // 相同行
                diff.append("  ").append(lines1[i]).append("\n");
                i++;
                j++;
            } else {
                // 检测删除的行
                while (i < lines1.length && (j >= lines2.length || !lcs.contains(lines1[i]))) {
                    diff.append("- ").append(lines1[i]).append("\n");
                    i++;
                }
                
                // 检测新增的行
                while (j < lines2.length && (i >= lines1.length || !lcs.contains(lines2[j]))) {
                    diff.append("+ ").append(lines2[j]).append("\n");
                    j++;
                }
            }
        }
        
        return diff.toString();
    }
    
    // 查找两个文本的LCS行
    private static List<String> findLCSLines(String text1, String text2) {
        String[] lines1 = text1.split("\n");
        String[] lines2 = text2.split("\n");
        
        int m = lines1.length;
        int n = lines2.length;
        int[][] dp = new int[m + 1][n + 1];
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (lines1[i - 1].equals(lines2[j - 1])) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 回溯重建LCS行
        List<String> lcsLines = new LinkedList<>();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (lines1[i - 1].equals(lines2[j - 1])) {
                lcsLines.add(lines1[i - 1]);
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        
        Collections.reverse(lcsLines);
        return lcsLines;
    }
    
    /**
     * DNA序列比对
     * 
     * @param dna1 DNA序列1
     * @param dna2 DNA序列2
     * @return 比对结果
     */
    public static String dnaAlignment(String dna1, String dna2) {
        String lcs = buildLCS(dna1, dna2);
        StringBuilder align1 = new StringBuilder();
        StringBuilder align2 = new StringBuilder();
        StringBuilder markers = new StringBuilder();
        
        int i = 0, j = 0;
        for (char c : lcs.toCharArray()) {
            // 处理dna1直到匹配字符
            while (i < dna1.length() && dna1.charAt(i) != c) {
                align1.append(dna1.charAt(i));
                align2.append("-");
                markers.append(" ");
                i++;
            }
            
            // 处理dna2直到匹配字符
            while (j < dna2.length() && dna2.charAt(j) != c) {
                align1.append("-");
                align2.append(dna2.charAt(j));
                markers.append(" ");
                j++;
            }
            
            // 添加匹配字符
            if (i < dna1.length() && j < dna2.length()) {
                align1.append(dna1.charAt(i));
                align2.append(dna2.charAt(j));
                markers.append("|");
                i++;
                j++;
            }
        }
        
        // 添加剩余字符
        while (i < dna1.length()) {
            align1.append(dna1.charAt(i));
            align2.append("-");
            markers.append(" ");
            i++;
        }
        
        while (j < dna2.length()) {
            align1.append("-");
            align2.append(dna2.charAt(j));
            markers.append(" ");
            j++;
        }
        
        return "序列1: " + align1.toString() + "\n" +
               "      " + markers.toString() + "\n" +
               "序列2: " + align2.toString();
    }
    
    /**
     * 版本控制系统diff算法简化版
     * 
     * @param oldVersion 旧版本内容
     * @param newVersion 新版本内容
     * @return 变更摘要
     */
    public static String versionControlDiff(String oldVersion, String newVersion) {
        List<String> oldLines = Arrays.asList(oldVersion.split("\n"));
        List<String> newLines = Arrays.asList(newVersion.split("\n"));
        
        int m = oldLines.size();
        int n = newLines.size();
        int[][] dp = new int[m + 1][n + 1];
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (oldLines.get(i - 1).equals(newLines.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 生成变更摘要
        StringBuilder diff = new StringBuilder();
        int i = m, j = n;
        
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && oldLines.get(i - 1).equals(newLines.get(j - 1))) {
                // 未更改的行
                i--;
                j--;
            } else if (j > 0 && (i == 0 || dp[i][j - 1] >= dp[i - 1][j])) {
                // 新增的行
                diff.insert(0, "+ " + newLines.get(j - 1) + "\n");
                j--;
            } else if (i > 0 && (j == 0 || dp[i][j - 1] < dp[i - 1][j])) {
                // 删除的行
                diff.insert(0, "- " + oldLines.get(i - 1) + "\n");
                i--;
            }
        }
        
        return diff.toString();
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testEdgeCases();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        String text1 = "abcde";
        String text2 = "ace";
        
        testLCS(text1, text2, 3);
        
        String text3 = "abc";
        String text4 = "abc";
        testLCS(text3, text4, 3);
        
        String text5 = "abc";
        String text6 = "def";
        testLCS(text5, text6, 0);
    }
    
    private static void testLCS(String text1, String text2, int expected) {
        int dpResult = lcsDP(text1, text2);
        int optResult = lcsOptimized(text1, text2);
        String lcsStr = buildLCS(text1, text2);
        
        System.out.printf("\n文本1: \"%s\"\n文本2: \"%s\"\n", text1, text2);
        System.out.println("DP结果: " + dpResult);
        System.out.println("优化DP结果: " + optResult);
        System.out.println("LCS字符串: \"" + lcsStr + "\"");
        
        boolean pass = dpResult == expected && optResult == expected;
        System.out.println("状态: " + (pass ? "✅" : "❌"));
        
        // 可视化
        if (text1.length() <= 10 && text2.length() <= 10) {
            visualizeDPTable(text1, text2);
            visualizeLCSRebuild(text1, text2);
        }
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        // 空字符串测试
        System.out.println("空字符串测试:");
        testLCS("", "", 0);
        testLCS("abc", "", 0);
        testLCS("", "def", 0);
        
        // 长字符串测试
        System.out.println("\n长字符串测试:");
        String longText1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String longText2 = "ABCDXYZLMNOPQRSTUVW";
        testLCS(longText1, longText2, 19);
        
        // 重复字符测试
        System.out.println("\n重复字符测试:");
        String repeat1 = "AAAAA";
        String repeat2 = "AA";
        testLCS(repeat1, repeat2, 2);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 生成长字符串
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        Random rand = new Random();
        
        for (int i = 0; i < 10000; i++) {
            char c = (char)('A' + rand.nextInt(26));
            sb1.append(c);
            if (rand.nextDouble() < 0.7) {
                sb2.append(c);
            } else {
                sb2.append((char)('A' + rand.nextInt(26)));
            }
        }
        
        String longText1 = sb1.toString();
        String longText2 = sb2.toString();
        
        System.out.println("字符串长度: " + longText1.length() + " 和 " + longText2.length());
        
        // 测试DP方法
        long start = System.currentTimeMillis();
        int dpResult = lcsDP(longText1, longText2);
        long end = System.currentTimeMillis();
        System.out.printf("DP方法: %d (耗时: %d ms)%n", dpResult, end - start);
        
        // 测试优化方法
        start = System.currentTimeMillis();
        int optResult = lcsOptimized(longText1, longText2);
        end = System.currentTimeMillis();
        System.out.printf("优化DP: %d (耗时: %d ms)%n", optResult, end - start);
        
        // 测试重建LCS（小规模）
        if (longText1.length() < 100) {
            start = System.currentTimeMillis();
            String lcs = buildLCS(longText1, longText2);
            end = System.currentTimeMillis();
            System.out.printf("LCS重建: \"%s\" (耗时: %d ms)%n", lcs, end - start);
        }
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 文本差异比较
        System.out.println("1. 文本差异比较:");
        String text1 = "Hello world!\nThis is a test.\nLCS algorithm is useful.";
        String text2 = "Hello there!\nThis is a test.\nLCS algorithm is powerful.";
        System.out.println("差异结果:\n" + textDiff(text1, text2));
        
        // 场景2: DNA序列比对
        System.out.println("\n2. DNA序列比对:");
        String dna1 = "GATCATGCTAGCTAGCTAGCT";
        String dna2 = "GATCCGTAGCTAGCTAGCT";
        System.out.println(dnaAlignment(dna1, dna2));
        
        // 场景3: 版本控制系统diff
        System.out.println("\n3. 版本控制系统diff:");
        String oldCode = "public class Hello {\n    public static void main(String[] args) {\n        System.out.println(\"Hello\");\n    }\n}";
        String newCode = "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World!\");\n    }\n}";
        System.out.println("代码变更:\n" + versionControlDiff(oldCode, newCode));
    }
}