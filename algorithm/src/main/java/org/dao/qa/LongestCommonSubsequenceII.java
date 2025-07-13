package main.java.org.dao.qa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 最长公共子序列（LCS）综合解决方案
 * 
 * <p><b>本类包含的功能：</b>
 * 1. 基础LCS长度计算
 * 2. 空间优化版LCS计算
 * 3. 回溯获取最长公共子序列
 * 4. 文本相似度计算
 * 5. DNA序列比对可视化
 * 6. 文件差异比较（基于LCS）
 * 7. 代码剽窃检测
 * 
 * <p><b>应用场景：</b>
 * 1. 生物信息学中的DNA/RNA序列比对
 * 2. 文本相似度计算和文档比对
 * 3. 版本控制系统中的文件差异比较
 * 4. 代码剽窃检测
 * 5. 自然语言处理中的文本分析
 */
public class LongestCommonSubsequenceII {
    
    // ======================== 核心算法部分 ========================
    
    /**
     * 基础LCS长度计算（动态规划）
     * 
     * @param text1 文本1
     * @param text2 文本2
     * @return LCS的长度
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    public static int lcsLength(String text1, String text2) {
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
     * 空间优化版LCS计算
     * 
     * @param text1 文本1
     * @param text2 文本2
     * @return LCS的长度
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(min(m,n))
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
        int[] current = new int[n + 1];
        int[] previous = new int[n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    current[j] = previous[j - 1] + 1;
                } else {
                    current[j] = Math.max(previous[j], current[j - 1]);
                }
            }
            // 更新数组引用
            int[] temp = previous;
            previous = current;
            current = temp;
        }
        
        return previous[n];
    }
    
    /**
     * 回溯获取最长公共子序列
     * 
     * @param text1 文本1
     * @param text2 文本2
     * @return 一个最长公共子序列
     */
    public static String getLCS(String text1, String text2) {
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
        
        // 回溯构建LCS
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
    
    /**
     * 获取所有最长公共子序列
     * 
     * @param text1 文本1
     * @param text2 文本2
     * @return 所有最长公共子序列的列表
     */
    public static List<String> getAllLCS(String text1, String text2) {
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
        
        // 递归回溯获取所有LCS
        return backtrackAllLCS(dp, text1, text2, m, n);
    }
    
    private static List<String> backtrackAllLCS(int[][] dp, String text1, String text2, int i, int j) {
        List<String> results = new ArrayList<>();
        
        if (i == 0 || j == 0) {
            results.add("");
            return results;
        }
        
        if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
            List<String> lcsList = backtrackAllLCS(dp, text1, text2, i - 1, j - 1);
            for (String lcs : lcsList) {
                results.add(lcs + text1.charAt(i - 1));
            }
        } else {
            if (dp[i - 1][j] >= dp[i][j - 1]) {
                results.addAll(backtrackAllLCS(dp, text1, text2, i - 1, j));
            }
            
            if (dp[i][j - 1] >= dp[i - 1][j]) {
                results.addAll(backtrackAllLCS(dp, text1, text2, i, j - 1));
            }
        }
        
        return results;
    }
    
    // ======================== 应用场景部分 ========================
    
    /**
     * 计算文本相似度（基于LCS）
     * 
     * @param text1 文本1
     * @param text2 文本2
     * @return 相似度分数（0.0到1.0之间）
     */
    public static double textSimilarity(String text1, String text2) {
        if (text1.isEmpty() && text2.isEmpty()) {
            return 1.0;
        }
        
        int lcsLength = lcsOptimized(text1, text2);
        int maxLength = Math.max(text1.length(), text2.length());
        
        return (double) lcsLength / maxLength;
    }
    
    /**
     * DNA序列比对可视化
     * 
     * @param dna1 DNA序列1
     * @param dna2 DNA序列2
     * @return 比对结果字符串
     */
    public static String dnaAlignmentVisualization(String dna1, String dna2) {
        String lcs = getLCS(dna1, dna2);
        StringBuilder align1 = new StringBuilder();
        StringBuilder align2 = new StringBuilder();
        StringBuilder markers = new StringBuilder();
        
        int i = 0, j = 0, k = 0;
        
        while (k < lcs.length()) {
            char c = lcs.charAt(k);
            
            // 处理第一个序列
            while (i < dna1.length() && dna1.charAt(i) != c) {
                align1.append(dna1.charAt(i++));
                align2.append("-");
                markers.append(" ");
            }
            align1.append(dna1.charAt(i++));
            
            // 处理第二个序列
            while (j < dna2.length() && dna2.charAt(j) != c) {
                align2.append(dna2.charAt(j++));
                align1.append("-");
                markers.append(" ");
            }
            align2.append(dna2.charAt(j++));
            
            // 添加匹配标记
            markers.append("|");
            k++;
        }
        
        // 添加剩余字符
        while (i < dna1.length()) {
            align1.append(dna1.charAt(i++));
            align2.append("-");
            markers.append(" ");
        }
        
        while (j < dna2.length()) {
            align1.append("-");
            align2.append(dna2.charAt(j++));
            markers.append(" ");
        }
        
        return "序列1: " + align1.toString() + "\n" +
               "      " + markers.toString() + "\n" +
               "序列2: " + align2.toString() + "\n" +
               "说明: | 表示匹配位置，- 表示缺失或插入";
    }
    
    /**
     * 文件差异比较（基于LCS）
     * 
     * @param file1Lines 文件1内容（按行）
     * @param file2Lines 文件2内容（按行）
     * @return 差异比较结果
     */
    public static String fileDiffComparison(List<String> file1Lines, List<String> file2Lines) {
        List<String> common = getLineLCS(file1Lines, file2Lines);
        StringBuilder diff = new StringBuilder();
        int i = 0, j = 0, k = 0;
        
        diff.append("文件差异比较结果:\n");
        
        while (i < file1Lines.size() || j < file2Lines.size() || k < common.size()) {
            // 当两个文件同时匹配公共行
            if (k < common.size() && 
                i < file1Lines.size() && 
                file1Lines.get(i).equals(common.get(k)) && 
                j < file2Lines.size() && 
                file2Lines.get(j).equals(common.get(k))) {
                
                diff.append(" ").append(common.get(k)).append("\n");
                i++;
                j++;
                k++;
            } 
            // 文件1有额外行
            else if (k < common.size() && 
                     i < file1Lines.size() && 
                     file1Lines.get(i).equals(common.get(k)) == false) {
                
                diff.append("-").append(file1Lines.get(i++)).append("\n");
            } 
            // 文件2有额外行
            else if (k < common.size() && 
                     j < file2Lines.size() && 
                     file2Lines.get(j).equals(common.get(k)) == false) {
                
                diff.append("+").append(file2Lines.get(j++)).append("\n");
            } 
            // 处理剩余内容
            else if (i < file1Lines.size()) {
                diff.append("-").append(file1Lines.get(i++)).append("\n");
            } 
            else if (j < file2Lines.size()) {
                diff.append("+").append(file2Lines.get(j++)).append("\n");
            }
        }
        
        return diff.toString();
    }
    
    /**
     * 代码剽窃检测
     * 
     * @param code1 代码片段1
     * @param code2 代码片段2
     * @return 相似度报告
     */
    public static String plagiarismDetection(String code1, String code2) {
        // 预处理代码（移除注释和空格）
        String cleanCode1 = preprocessCode(code1);
        String cleanCode2 = preprocessCode(code2);
        
        // 计算相似度
        double similarity = textSimilarity(cleanCode1, cleanCode2);
        int commonLength = lcsOptimized(cleanCode1, cleanCode2);
        int minLength = Math.min(cleanCode1.length(), cleanCode2.length());
        
        // 生成报告
        StringBuilder report = new StringBuilder();
        report.append("代码剽窃检测报告\n");
        report.append("-----------------\n");
        report.append(String.format("原始代码长度: %d\n", code1.length()));
        report.append(String.format("对比代码长度: %d\n", code2.length()));
        report.append(String.format("公共子序列长度: %d\n", commonLength));
        report.append(String.format("相似度指数: %.2f%%\n", similarity * 100));
        report.append("\n评估: ");
        
        if (similarity > 0.9) {
            report.append("⚠️ 高概率存在代码剽窃（90%以上匹配）");
        } else if (similarity > 0.7) {
            report.append("⚠️ 中等概率存在代码剽窃（70-90%匹配）");
        } else if (similarity > 0.5) {
            report.append("⚠️ 低概率存在代码剽窃（50-70%匹配）");
        } else {
            report.append("✅ 代码差异显著，不太可能存在剽窃");
        }
        
        return report.toString();
    }
    
    // ======================== 辅助方法部分 ========================
    
    /**
     * 获取两个文件的最长公共行序列
     * 
     * @param lines1 文件1的行列表
     * @param lines2 文件2的行列表
     * @return 公共行序列
     */
    private static List<String> getLineLCS(List<String> lines1, List<String> lines2) {
        int m = lines1.size();
        int n = lines2.size();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (lines1.get(i - 1).equals(lines2.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        List<String> lcsLines = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 && j > 0) {
            if (lines1.get(i - 1).equals(lines2.get(j - 1))) {
                lcsLines.add(lines1.get(i - 1));
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
     * 预处理代码 - 移除注释和多余空格
     * 
     * @param code 原始代码
     * @return 处理后的代码
     */
    private static String preprocessCode(String code) {
        // 移除单行注释
        String cleanCode = code.replaceAll("//.*", "");
        // 移除多行注释
        cleanCode = cleanCode.replaceAll("/\\*.*?\\*/", "");
        // 移除多余空格和换行
        cleanCode = cleanCode.replaceAll("\\s+", " ");
        return cleanCode.trim();
    }
    
    // ======================== 测试方法 ========================
    
    public static void main(String[] args) {
        testBasicAlgorithms();
        testApplicationScenarios();
    }
    
    private static void testBasicAlgorithms() {
        System.out.println("====== 基本算法测试 ======");
        String text1 = "AGGTAB";
        String text2 = "GXTXAYB";
        
        // 基础LCS长度
        int basicLcs = lcsLength(text1, text2);
        System.out.printf("[基础算法] '%s' 和 '%s' 的LCS长度: %d\n", text1, text2, basicLcs);
        
        // 优化LCS长度
        int optLcs = lcsOptimized(text1, text2);
        System.out.printf("[优化算法] '%s' 和 '%s' 的LCS长度: %d\n", text1, text2, optLcs);
        
        // 获取LCS
        String lcs = getLCS(text1, text2);
        System.out.printf("LCS结果: '%s'\n", lcs);
        
        // 获取所有LCS
        List<String> allLcs = getAllLCS(text1, text2);
        System.out.println("所有可能的LCS:");
        for (String seq : allLcs) {
            System.out.println("  " + seq);
        }
        System.out.println();
    }
    
    private static void testApplicationScenarios() {
        System.out.println("====== 应用场景测试 ======");
        
        // 文本相似度计算
        System.out.println("\n[场景1] 文本相似度计算:");
        String s1 = "人类基因组测序计划";
        String s2 = "人类基因组测序项目";
        double similarity = textSimilarity(s1, s2);
        System.out.printf("文本1: '%s'\n文本2: '%s'\n相似度: %.2f%%\n", 
                          s1, s2, similarity * 100);
        
        // DNA序列比对可视化
        System.out.println("\n[场景2] DNA序列比对:");
        String dna1 = "ATGCTGAGCTAGCTAGCT";
        String dna2 = "ATGCGAGCTAGGTAGGT";
        System.out.println(dnaAlignmentVisualization(dna1, dna2));
        
        // 文件差异比较
        System.out.println("\n[场景3] 文件差异比较:");
        List<String> file1 = Arrays.asList(
            "public class Calculator {",
            "    public int add(int a, int b) {",
            "        return a + b;",
            "    }",
            "}"
        );
        List<String> file2 = Arrays.asList(
            "public class AdvancedCalculator {",
            "    public int add(int a, int b) {",
            "        return a + b;",
            "    }",
            "    ",
            "    public int subtract(int a, int b) {",
            "        return a - b;",
            "    }",
            "}"
        );
        System.out.println(fileDiffComparison(file1, file2));
        
        // 代码剽窃检测
        System.out.println("\n[场景4] 代码剽窃检测:");
        String code1 = "public class Sum {\n" +
                       "    public static void main(String[] args) {\n" +
                       "        int a = 5, b = 10;\n" +
                       "        int sum = a + b;\n" +
                       "        System.out.println(\"Sum is: \" + sum);\n" +
                       "    }\n" +
                       "}";
        
        String code2 = "public class Addition {\n" +
                       "    public static void main(String[] args) {\n" +
                       "        int num1 = 5;\n" +
                       "        int num2 = 10;\n" +
                       "        int total = num1 + num2;\n" +
                       "        System.out.println(\"Total: \" + total);\n" +
                       "    }\n" +
                       "}";
        
        System.out.println(plagiarismDetection(code1, code2));
    }
}