package main.java.org.dao.qa;

import java.util.*;

/**
 * 编辑距离（Edit Distance）综合解决方案
 * 
 * <p><b>本类包含的功能：</b>
 * 1. 基础编辑距离计算 (LeetCode 72)
 * 2. 空间优化版编辑距离计算
 * 3. 编辑操作序列回溯
 * 4. 拼写纠错建议
 * 5. DNA序列比对可视化
 * 6. 文件差异比较
 * 
 * <p><b>应用场景：</b>
 * 1. 拼写检查与自动修正
 * 2. 生物信息学中的DNA序列比对
 * 3. 自然语言处理中的文本相似度计算
 * 4. 版本控制系统中的文件差异比较
 * 5. 数据清洗中的记录匹配
 */
public class EditDistance {

    // ========================== 核心算法部分 ==========================
    
    /**
     * 编辑距离操作类型枚举
     */
    public enum Operation {
        MATCH,      // 字符匹配
        REPLACE,    // 替换操作
        INSERT,     // 插入操作
        DELETE      // 删除操作
    }
    
    /**
     * 编辑操作步骤记录
     */
    public static class EditStep {
        public Operation op;
        public char char1;     // 源字符串字符
        public char char2;     // 目标字符串字符
        public int pos1;        // 源字符串位置
        public int pos2;        // 目标字符串位置
        
        public EditStep(Operation op, char c1, char c2, int pos1, int pos2) {
            this.op = op;
            this.char1 = c1;
            this.char2 = c2;
            this.pos1 = pos1;
            this.pos2 = pos2;
        }
        
        @Override
        public String toString() {
            switch(op) {
                case INSERT: 
                    return String.format("在位置 %d 插入 '%c'", pos2, char2);
                case DELETE: 
                    return String.format("删除位置 %d 的 '%c'", pos1, char1);
                case REPLACE: 
                    return String.format("将位置 %d 的 '%c' 替换为 '%c'", pos1, char1, char2);
                case MATCH: 
                    return String.format("匹配位置 %d 的 '%c'", pos1, char1);
                default: 
                    return "未知操作";
            }
        }
    }
    
    /**
     * 计算基础编辑距离（动态规划）
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @return 最小编辑距离
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    public static int minDistanceBasic(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化第一行和第一列
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + min(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 空间优化版编辑距离计算
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @return 最小编辑距离
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(min(m,n))
     */
    public static int minDistanceOptimized(String word1, String word2) {
        // 确保word2是较短的字符串
        if (word1.length() < word2.length()) {
            String temp = word1;
            word1 = word2;
            word2 = temp;
        }
        
        int m = word1.length();
        int n = word2.length();
        int[] dp = new int[n + 1];
        
        // 初始化第一行
        for (int j = 0; j <= n; j++) dp[j] = j;
        
        for (int i = 1; i <= m; i++) {
            int prev = dp[0]; // 保存左上角的值
            dp[0] = i; // 当前行的第一个元素
            
            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // 保存当前值
                
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[j] = prev;
                } else {
                    dp[j] = 1 + min(dp[j], dp[j - 1], prev);
                }
                
                prev = temp; // 更新左上角值
            }
        }
        
        return dp[n];
    }
    
    /**
     * 获取编辑操作序列
     * 
     * @param word1 源字符串
     * @param word2 目标字符串
     * @return 编辑操作序列
     */
    public static List<EditStep> getEditSequence(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        Operation[][] operations = new Operation[m + 1][n + 1];
        
        // 初始化
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            if (i > 0) operations[i][0] = Operation.DELETE;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            if (j > 0) operations[0][j] = Operation.INSERT;
        }
        
        // 填充DP表并记录操作
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char c1 = word1.charAt(i - 1);
                char c2 = word2.charAt(j - 1);
                
                if (c1 == c2) {
                    dp[i][j] = dp[i - 1][j - 1];
                    operations[i][j] = Operation.MATCH;
                } else {
                    int del = dp[i - 1][j] + 1;
                    int ins = dp[i][j - 1] + 1;
                    int rep = dp[i - 1][j - 1] + 1;
                    
                    if (del <= ins && del <= rep) {
                        dp[i][j] = del;
                        operations[i][j] = Operation.DELETE;
                    } else if (ins <= del && ins <= rep) {
                        dp[i][j] = ins;
                        operations[i][j] = Operation.INSERT;
                    } else {
                        dp[i][j] = rep;
                        operations[i][j] = Operation.REPLACE;
                    }
                }
            }
        }
        
        // 回溯操作序列
        List<EditStep> sequence = new ArrayList<>();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            Operation op = operations[i][j];
            char c1 = (i > 0) ? word1.charAt(i - 1) : ' ';
            char c2 = (j > 0) ? word2.charAt(j - 1) : ' ';
            
            if (op == null) {
                // 处理边界情况
                if (i > 0 && j == 0) op = Operation.DELETE;
                else if (j > 0 && i == 0) op = Operation.INSERT;
                else op = Operation.REPLACE;
            }
            
            EditStep step = new EditStep(op, c1, c2, i, j);
            sequence.add(0, step); // 添加到序列开头
            
            switch (op) {
                case MATCH:
                case REPLACE:
                    i--;
                    j--;
                    break;
                case INSERT:
                    j--;
                    break;
                case DELETE:
                    i--;
                    break;
            }
        }
        
        return sequence;
    }
    
    // ========================== 应用场景实现部分 ==========================
    
    /**
     * 拼写纠错建议
     * 
     * @param word 输入单词
     * @param dictionary 词典
     * @param maxSuggestions 最大建议数
     * @return 纠错建议列表
     */
    public static List<String> spellCheck(String word, List<String> dictionary, int maxSuggestions) {
        // 计算单词与词典中每个单词的编辑距离
        PriorityQueue<DictEntry> queue = new PriorityQueue<>(
            Comparator.comparingInt(e -> e.distance)
        );
        
        for (String dictWord : dictionary) {
            int dist = minDistanceOptimized(word, dictWord);
            queue.add(new DictEntry(dictWord, dist));
        }
        
        // 获取前几个建议
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < maxSuggestions && !queue.isEmpty(); i++) {
            suggestions.add(queue.poll().word);
        }
        
        return suggestions;
    }
    
    /**
     * DNA序列比对可视化
     * 
     * @param seq1 DNA序列1
     * @param seq2 DNA序列2
     * @return 比对结果字符串
     */
    public static String visualizeDnaAlignment(String seq1, String seq2) {
        List<EditStep> operations = getEditSequence(seq1, seq2);
        StringBuilder align1 = new StringBuilder();  // 序列1的比对结果
        StringBuilder align2 = new StringBuilder();  // 序列2的比对结果
        StringBuilder markers = new StringBuilder(); // 比对标记
        
        int idx1 = 0, idx2 = 0;
        
        for (EditStep step : operations) {
            switch (step.op) {
                case MATCH:
                    align1.append(seq1.charAt(idx1));
                    align2.append(seq2.charAt(idx2));
                    markers.append('|');
                    idx1++;
                    idx2++;
                    break;
                    
                case REPLACE:
                    align1.append(seq1.charAt(idx1));
                    align2.append(seq2.charAt(idx2));
                    markers.append('*');
                    idx1++;
                    idx2++;
                    break;
                    
                case INSERT:
                    align1.append('-');
                    align2.append(seq2.charAt(idx2));
                    markers.append(' ');
                    idx2++;
                    break;
                    
                case DELETE:
                    align1.append(seq1.charAt(idx1));
                    align2.append('-');
                    markers.append(' ');
                    idx1++;
                    break;
            }
        }
        
        // 添加剩余字符
        while (idx1 < seq1.length()) {
            align1.append(seq1.charAt(idx1));
            align2.append('-');
            markers.append(' ');
            idx1++;
        }
        
        while (idx2 < seq2.length()) {
            align1.append('-');
            align2.append(seq2.charAt(idx2));
            markers.append(' ');
            idx2++;
        }
        
        return "序列1: " + align1.toString() + "\n" +
               "      " + markers.toString() + "\n" +
               "序列2: " + align2.toString() + "\n" +
               "说明: | 表示匹配, * 表示替换, - 表示插入/删除的空白";
    }
    
    /**
     * 文件差异比较
     * 
     * @param file1 文件内容1（按行）
     * @param file2 文件内容2（按行）
     * @return 差异比较结果
     */
    public static String fileDiff(List<String> file1, List<String> file2) {
        int m = file1.size();
        int n = file2.size();
        int[][] dp = new int[m + 1][n + 1];
        Operation[][] operations = new Operation[m + 1][n + 1];
        
        // 初始化
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
            operations[i][0] = Operation.DELETE;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
            operations[0][j] = Operation.INSERT;
        }
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (file1.get(i - 1).equals(file2.get(j - 1))) {
                    dp[i][j] = dp[i - 1][j - 1];
                    operations[i][j] = Operation.MATCH;
                } else {
                    int del = dp[i - 1][j] + 1;
                    int ins = dp[i][j - 1] + 1;
                    int rep = dp[i - 1][j - 1] + 1;
                    
                    if (del <= ins && del <= rep) {
                        dp[i][j] = del;
                        operations[i][j] = Operation.DELETE;
                    } else if (ins <= del && ins <= rep) {
                        dp[i][j] = ins;
                        operations[i][j] = Operation.INSERT;
                    } else {
                        dp[i][j] = rep;
                        operations[i][j] = Operation.REPLACE;
                    }
                }
            }
        }
        
        // 回溯差异
        StringBuilder diff = new StringBuilder();
        int i = m, j = n;
        List<String> diffLines = new ArrayList<>();
        
        while (i > 0 || j > 0) {
            Operation op = operations[i][j];
            String line1 = (i > 0) ? file1.get(i - 1) : "";
            String line2 = (j > 0) ? file2.get(j - 1) : "";
            
            switch (op) {
                case MATCH:
                    diffLines.add("   " + line1);
                    i--;
                    j--;
                    break;
                    
                case REPLACE:
                    diffLines.add("-  " + line1);
                    diffLines.add("+  " + line2);
                    i--;
                    j--;
                    break;
                    
                case INSERT:
                    diffLines.add("+  " + line2);
                    j--;
                    break;
                    
                case DELETE:
                    diffLines.add("-  " + line1);
                    i--;
                    break;
            }
        }
        
        // 反转并构建结果
        Collections.reverse(diffLines);
        diff.append("文件差异比较结果:\n");
        for (String line : diffLines) {
            diff.append(line).append("\n");
        }
        
        return diff.toString();
    }
    
    // ========================== 辅助类与方法 ==========================
    
    /**
     * 词典条目辅助类
     */
    private static class DictEntry {
        String word;
        int distance;
        
        public DictEntry(String word, int distance) {
            this.word = word;
            this.distance = distance;
        }
    }
    
    // 三数取最小值
    private static int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }
    
    // ========================== 测试方法 ==========================
    
    public static void main(String[] args) {
        testBasicAlgorithms();
        testApplicationScenarios();
    }
    
    private static void testBasicAlgorithms() {
        System.out.println("====== 基本算法测试 ======");
        String word1 = "intention";
        String word2 = "execution";
        
        // 基础DP测试
        int basicDist = minDistanceBasic(word1, word2);
        System.out.printf("[基础DP] '%s' → '%s' 距离: %d\n", word1, word2, basicDist);
        
        // 优化DP测试
        int optDist = minDistanceOptimized(word1, word2);
        System.out.printf("[优化DP] '%s' → '%s' 距离: %d\n", word1, word2, optDist);
        
        // 操作序列测试
        List<EditStep> sequence = getEditSequence(word1, word2);
        System.out.println("\n编辑操作序列:");
        for (EditStep step : sequence) {
            System.out.println("  " + step);
        }
        System.out.println();
    }
    
    private static void testApplicationScenarios() {
        System.out.println("====== 应用场景测试 ======");
        
        // 拼写纠错测试
        System.out.println("\n[场景1] 拼写纠错:");
        List<String> dictionary = Arrays.asList(
            "algorithm", "datastructure", "computer", "science", 
            "programming", "java", "python", "javascript"
        );
        List<String> suggestions = spellCheck("algoritm", dictionary, 3);
        System.out.println("'algoritm' 的纠错建议: " + suggestions);
        
        // DNA序列比对测试
        System.out.println("\n[场景2] DNA序列比对:");
        String dna1 = "GATCACAGTACCAGAT";
        String dna2 = "GATCAAGTAACAGAT";
        System.out.println(visualizeDnaAlignment(dna1, dna2));
        
        // 文件差异比较测试
        System.out.println("\n[场景3] 文件差异比较:");
        List<String> file1 = Arrays.asList(
            "public class HelloWorld {",
            "    public static void main(String[] args) {",
            "        System.out.println(\"Hello, world!\");",
            "    }",
            "}"
        );
        List<String> file2 = Arrays.asList(
            "public class Greeting {",
            "    public static void main(String[] args) {",
            "        System.out.println(\"Hello, Java!\");",
            "        System.out.println(\"How are you?\");",
            "    }",
            "}"
        );
        System.out.println(fileDiff(file1, file2));
    }
}