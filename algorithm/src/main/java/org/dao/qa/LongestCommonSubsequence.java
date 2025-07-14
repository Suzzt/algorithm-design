package main.java.org.dao.qa;

/**
 * 最长公共子序列 (LCS) 解决方案
 * 
 * <p><b>问题描述：</b>
 * 给定两个字符串 text1 和 text2，返回它们的最长公共子序列的长度。
 * 子序列是通过删除原始字符串中一些字符而不改变剩余字符相对位置形成的新字符串。
 * 
 * <p><b>示例：</b>
 * 输入: text1 = "abcde", text2 = "ace"
 * 输出: 3
 * 解释: "ace" 是最长公共子序列
 */
public class LongestCommonSubsequence {

    // ========================== 核心解法 ==========================
    
    /**
     * 动态规划解法
     * 
     * @param text1 第一个字符串
     * @param text2 第二个字符串
     * @return LCS长度
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
    
    /**
     * 空间优化动态规划 (O(min(m,n))空间)
     */
    public static int lcsOptimizedDP(String text1, String text2) {
        if (text1.length() < text2.length()) {
            return lcsOptimizedDP(text2, text1); // 确保text2是较短的
        }
        
        int m = text1.length();
        int n = text2.length();
        int[] dp = new int[n + 1];
        
        for (int i = 1; i <= m; i++) {
            int prev = 0;
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[j] = prev + 1;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                prev = temp;
            }
        }
        
        return dp[n];
    }
    
    // ========================== 进阶功能：获取具体序列 ==========================
    
    /**
     * 获取最长公共子序列的字符串
     */
    public static String getLCSSequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 构建DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        // 回溯获取LCS
        StringBuilder sb = new StringBuilder();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                sb.append(text1.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] > dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        
        return sb.reverse().toString();
    }
    
    // ========================== 应用场景扩展 ==========================
    
    /**
     * DNA序列相似度计算
     * 
     * @param dna1 第一条DNA序列
     * @param dna2 第二条DNA序列
     * @return 相似度分数和最长公共子序列
     */
    public static String dnaSimilarityAnalysis(String dna1, String dna2) {
        int lcs = lcsOptimizedDP(dna1, dna2);
        String sequence = getLCSSequence(dna1, dna2);
        double similarity = (2.0 * lcs) / (dna1.length() + dna2.length());
        
        return String.format("相似度: %.2f%%, LCS长度: %d, 公共序列: %s", 
            similarity * 100, lcs, sequence);
    }
    
    /**
     * 代码差异分析
     * 
     * @param code1 旧版本代码
     * @param code2 新版本代码
     * @return 最大公共部分和差异分析
     */
    public static String codeDiffAnalysis(String code1, String code2) {
        String commonSeq = getLCSSequence(code1, code2);
        int commonLength = commonSeq.length();
        int diff1 = code1.length() - commonLength;
        int diff2 = code2.length() - commonLength;
        
        return String.format("公共核心代码长度: %d\n" +
                            "旧版本差异: %d 字符\n" +
                            "新版本差异: %d 字符\n" +
                            "公共代码片段: %s...",
                            commonLength, diff1, diff2, 
                            commonSeq.substring(0, Math.min(20, commonSeq.length())));
    }
    
    // ========================== 测试方法 ==========================
    
    public static void main(String[] args) {
        testBasicLCS();
        testRealWorldApplications();
    }
    
    private static void testBasicLCS() {
        System.out.println("===== 基础功能测试 =====");
        
        String text1 = "abcde";
        String text2 = "ace";
        
        int dpResult = lcsDP(text1, text2);
        int optResult = lcsOptimizedDP(text1, text2);
        String sequence = getLCSSequence(text1, text2);
        
        System.out.println("标准DP结果: " + dpResult);
        System.out.println("优化DP结果: " + optResult);
        System.out.println("LCS序列: \"" + sequence + "\"");
    }
    
    private static void testRealWorldApplications() {
        System.out.println("\n===== 实际应用测试 =====");
        
        // DNA相似度分析
        System.out.println("\n1. DNA序列相似度分析:");
        String dna1 = "AGCATGCTGCACTCGCGAGATAC";
        String dna2 = "TAGCTGATCGCGACTACGAGCAT";
        System.out.println(dnaSimilarityAnalysis(dna1, dna2));
        
        // 代码差异分析
        System.out.println("\n2. 代码差异分析:");
        String codeOld = "public class HelloWorld {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        System.out.println(\"Hello\");\n" +
                        "    }\n" +
                        "}";
        
        String codeNew = "public class HelloEarth {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        System.out.println(\"Hello\");\n" +
                        "        System.out.println(\"World\");\n" +
                        "    }\n" +
                        "}";
        
        System.out.println(codeDiffAnalysis(codeOld, codeNew));
    }
}