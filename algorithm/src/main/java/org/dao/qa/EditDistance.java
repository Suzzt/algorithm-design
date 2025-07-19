package main.java.org.dao.qa;

import java.util.*;

/**
 * 编辑距离 (Edit Distance) 解决方案
 * 
 * <p><b>问题描述：</b>
 * 给定两个单词 word1 和 word2，计算将 word1 转换成 word2 所需的最少操作步数。
 * 操作包括：插入一个字符、删除一个字符、替换一个字符。
 * 
 * <p><b>示例：</b>
 * 输入: word1 = "horse", word2 = "ros"
 * 输出: 3
 * 解释: 
 *   horse → rorse (替换'h'为'r')
 *   rorse → rose (删除'r')
 *   rose → ros (删除'e')
 */
public class EditDistance {

    // ========================== 核心解法 ==========================
    
    /**
     * 动态规划解法 (时间复杂度O(mn)，空间复杂度O(mn))
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @return 最小编辑距离
     */
    public static int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        // dp[i][j]表示word1前i个字符转换为word2前j个字符的最小操作数
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // 删除所有字符
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // 插入所有字符
        }
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // 字符相同，无需操作
                } else {
                    // 取三种操作的最小值 + 1
                    dp[i][j] = 1 + Math.min(
                        Math.min(dp[i - 1][j],   // 删除
                                 dp[i][j - 1]), // 插入
                        dp[i - 1][j - 1]        // 替换
                    );
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 空间优化动态规划 (时间复杂度O(mn)，空间复杂度O(n))
     */
    public static int minDistanceOptimized(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];
        
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }
        
        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(
                        Math.min(prev[j],   // 删除
                                 curr[j - 1]), // 插入
                        prev[j - 1]           // 替换
                    );
                }
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }
        
        return prev[n];
    }
    
    // ========================== 进阶功能：获取操作序列 ==========================
    
    /**
     * 获取最小编辑距离的操作序列
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @return 操作步骤列表
     */
    public static List<String> getEditOperations(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化DP表
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        // 填充DP表并记录操作来源
        int[][] from = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                    from[i][j] = 0; // 无操作
                } else {
                    int delete = dp[i - 1][j];
                    int insert = dp[i][j - 1];
                    int replace = dp[i - 1][j - 1];
                    
                    if (delete <= insert && delete <= replace) {
                        dp[i][j] = delete + 1;
                        from[i][j] = 1; // 删除
                    } else if (insert <= delete && insert <= replace) {
                        dp[i][j] = insert + 1;
                        from[i][j] = 2; // 插入
                    } else {
                        dp[i][j] = replace + 1;
                        from[i][j] = 3; // 替换
                    }
                }
            }
        }
        
        // 回溯获取操作序列
        List<String> operations = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && from[i][j] == 0) {
                operations.add(0, "保留 '" + word1.charAt(i - 1) + "'");
                i--;
                j--;
            } else if (from[i][j] == 1) {
                operations.add(0, "删除 '" + word1.charAt(i - 1) + "'");
                i--;
            } else if (from[i][j] == 2) {
                operations.add(0, "插入 '" + word2.charAt(j - 1) + "'");
                j--;
            } else {
                operations.add(0, "替换 '" + word1.charAt(i - 1) + "' -> '" + word2.charAt(j - 1) + "'");
                i--;
                j--;
            }
        }
        
        return operations;
    }
    
    // ========================== 应用场景扩展 ==========================
    
    /**
     * DNA序列比对分析
     * 
     * @param seq1 DNA序列1
     * @param seq2 DNA序列2
     * @return 比对分析报告
     */
    public static String dnaSequenceAlignment(String seq1, String seq2) {
        int distance = minDistance(seq1, seq2);
        List<String> operations = getEditOperations(seq1, seq2);
        double similarity = 1 - (double)distance / Math.max(seq1.length(), seq2.length());
        
        StringBuilder report = new StringBuilder();
        report.append("DNA序列比对结果:\n");
        report.append(String.format("序列1: %s\n序列2: %s\n", seq1, seq2));
        report.append(String.format("编辑距离: %d\n", distance));
        report.append(String.format("相似度: %.2f%%\n", similarity * 100));
        report.append("操作步骤:\n");
        for (String op : operations) {
            report.append("- ").append(op).append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * 拼写纠错引擎
     * 
     * @param input 用户输入
     * @param dictionary 候选单词字典
     * @return 最可能的正确拼写
     */
    public static String spellCheck(String input, Set<String> dictionary) {
        int minDistance = Integer.MAX_VALUE;
        String bestMatch = input;
        
        for (String word : dictionary) {
            int dist = minDistanceOptimized(input, word);
            if (dist < minDistance) {
                minDistance = dist;
                bestMatch = word;
            }
        }
        
        return bestMatch + " (距离: " + minDistance + ")";
    }
    
    /**
     * 自然语言处理 - 句子相似度检测
     * 
     * @param sentence1 句子1
     * @param sentence2 句子2
     * @return 相似度分数 (0.0 - 1.0)
     */
    public static double sentenceSimilarity(String sentence1, String sentence2) {
        // 按空格分词
        String[] words1 = sentence1.split("\\s+");
        String[] words2 = sentence2.split("\\s+");
        
        // 构建DP矩阵
        int[][] dp = new int[words1.length + 1][words2.length + 1];
        
        // 初始化边界
        for (int i = 0; i <= words1.length; i++) dp[i][0] = i;
        for (int j = 0; j <= words2.length; j++) dp[0][j] = j;
        
        // 计算词级编辑距离
        for (int i = 1; i <= words1.length; i++) {
            for (int j = 1; j <= words2.length; j++) {
                if (words1[i - 1].equals(words2[j - 1])) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                        Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]
                    );
                }
            }
        }
        
        // 计算相似度
        int maxLen = Math.max(words1.length, words2.length);
        return 1.0 - (double)dp[words1.length][words2.length] / maxLen;
    }
    
    /**
     * 代码差异生成器
     * 
     * @param code1 旧版代码
     * @param code2 新版代码
     * @return 差异报告
     */
    public static String generateCodeDiff(String code1, String code2) {
        String[] lines1 = code1.split("\n");
        String[] lines2 = code2.split("\n");
        
        int[][] dp = new int[lines1.length + 1][lines2.length + 1];
        for (int i = 0; i <= lines1.length; i++) dp[i][0] = i;
        for (int j = 0; j <= lines2.length; j++) dp[0][j] = j;
        
        // 填充DP表
        for (int i = 1; i <= lines1.length; i++) {
            for (int j = 1; j <= lines2.length; j++) {
                if (lines1[i - 1].equals(lines2[j - 1])) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                        Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]
                    );
                }
            }
        }
        
        // 回溯生成差异
        StringBuilder diff = new StringBuilder();
        diff.append("代码变更摘要 (编辑距离: ").append(dp[lines1.length][lines2.length]).append("):\n");
        
        int i = lines1.length, j = lines2.length;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && lines1[i - 1].equals(lines2[j - 1])) {
                i--;
                j--;
            } else if (j > 0 && (i == 0 || dp[i][j] == dp[i][j - 1] + 1)) {
                diff.append("+ ").append(lines2[j - 1]).append("\n");
                j--;
            } else if (i > 0 && (j == 0 || dp[i][j] == dp[i - 1][j] + 1)) {
                diff.append("- ").append(lines1[i - 1]).append("\n");
                i--;
            } else {
                diff.append("  ").append(lines1[i - 1]).append("\n");
                diff.append("  ").append(lines2[j - 1]).append("\n");
                i--;
                j--;
            }
        }
        
        return diff.toString();
    }
    
    // ========================== 测试方法 ==========================
    
    public static void main(String[] args) {
        testAlgorithms();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testAlgorithms() {
        System.out.println("===== 算法测试 =====");
        
        String word1 = "horse";
        String word2 = "ros";
        int expected = 3;
        
        int resultDP = minDistance(word1, word2);
        int resultOpt = minDistanceOptimized(word1, word2);
        List<String> operations = getEditOperations(word1, word2);
        
        System.out.printf("动态规划结果: %d (预期: %d)\n", resultDP, expected);
        System.out.printf("优化算法结果: %d\n", resultOpt);
        System.out.println("操作序列:");
        operations.forEach(op -> System.out.println(" - " + op));
    }
    
    private static void testPerformance() {
        System.out.println("\n===== 性能测试 (单位: 微秒) =====");
        
        // 生成大型测试数据
        String longWord1 = generateString(1000); // 1000个字符
        String longWord2 = generateString(1000); // 1000个字符
        
        // 标准DP测试
        long start = System.nanoTime();
        minDistance(longWord1, longWord2);
        long timeDP = (System.nanoTime() - start) / 1000; // 微秒
        
        // 优化DP测试
        start = System.nanoTime();
        minDistanceOptimized(longWord1, longWord2);
        long timeOpt = (System.nanoTime() - start) / 1000;
        
        System.out.println("标准DP时间: " + timeDP + " μs");
        System.out.println("优化DP时间: " + timeOpt + " μs");
    }
    
    // 生成随机字符串
    private static String generateString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt((int)(Math.random() * chars.length())));
        }
        return sb.toString();
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n===== 应用场景测试 =====");
        
        // 场景1: DNA序列比对
        System.out.println("\n1. DNA序列比对:");
        String dna1 = "GATCACAGGTCTATCACCCTATTAACCACTCACGGGAGCTCTCCATGCAT";
        String dna2 = "GATCAACAGGTCTATCGCCCTATTGACCACTCACGGGAGCTCTACATGCA";
        System.out.println(dnaSequenceAlignment(dna1, dna2));
        
        // 场景2: 拼写纠错
        System.out.println("\n2. 拼写纠错引擎:");
        Set<String> dictionary = new HashSet<>(Arrays.asList("apple", "banana", "orange", "strawberry", "pineapple"));
        String input = "bannna";
        System.out.println("输入: " + input);
        System.out.println("建议: " + spellCheck(input, dictionary));
        
        // 场景3: 句子相似度检测
        System.out.println("\n3. 自然语言处理:");
        String s1 = "the quick brown fox jumps over the lazy dog";
        String s2 = "a quick brown fox leaped over the sleepy dog";
        double similarity = sentenceSimilarity(s1, s2);
        System.out.printf("句子1: '%s'\n句子2: '%s'\n相似度: %.2f%%\n", s1, s2, similarity * 100);
        
        // 场景4: 代码差异生成
        System.out.println("\n4. 代码变更追踪:");
        String oldCode = "public class Hello {\n    public static void main(String[] args) {\n        System.out.println(\"Hello\");\n    }\n}";
        String newCode = "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello World\");\n    }\n}";
        System.out.println(generateCodeDiff(oldCode, newCode));
    }
}