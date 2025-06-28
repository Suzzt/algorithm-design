package main.java.org.dao.qa;

import java.util.*;

/**
 * 编辑距离问题（Edit Distance / Levenshtein Distance）
 * 
 * <p><b>问题描述</b>：
 * 给定两个字符串word1和word2，计算将word1转换为word2所需的最小操作次数。
 * 操作包括：插入一个字符、删除一个字符、替换一个字符。
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 中等/困难 (LeetCode 72)
 * 
 * <p><b>应用场景</b>:
 * 1. 拼写检查和自动纠正
 * 2. DNA序列比对
 * 3. 自然语言处理（如机器翻译）
 * 4. 语音识别
 * 5. 文件差异比较
 * 
 * <p><b>示例</b>:
 * 输入: word1 = "intention", word2 = "execution"
 * 输出: 5
 * 解释: 
 *   intention → inention (删除 't')
 *   inention → enention (将 'i' 替换为 'e')
 *   enention → exention (将 'n' 替换为 'x')
 *   exention → exection (将 'n' 替换为 'c')
 *   exection → execution (插入 'u')
 * 
 * <p><b>算法形式化定义</b>:
 * 输入: 
 *   - 字符串 word1 = a[1...m]
 *   - 字符串 word2 = b[1...n]
 * 输出: 
 *   - 最小编辑距离: minDistance
 *   - 操作序列: operationSequence
 * 
 * <p><b>动态规划方程</b>:
 * 设 dp[i][j] 表示 word1[0..i-1] 和 word2[0..j-1] 的最小编辑距离
 * 
 * 当 i=0: dp[0][j] = j (全部插入)
 * 当 j=0: dp[i][0] = i (全部删除)
 * 当 i>0 且 j>0:
 *   dp[i][j] = {
 *        dp[i-1][j-1]                             if a[i-1] == b[j-1]
 *        min( dp[i-1][j] + 1,                      // 删除 a[i-1]
 *             dp[i][j-1] + 1,                      // 在a[i-1]后插入 b[j-1]
 *             dp[i-1][j-1] + 1 )                   // 将 a[i-1] 替换为 b[j-1]
 *   }
 */

public class EditDistance {
    
    // 操作类型枚举
    enum Operation {
        NONE,      // 无操作 (当字符匹配时)
        INSERT,    // 插入
        DELETE,    // 删除
        REPLACE,   // 替换
        MATCH      // 字符匹配 (仅用于可视化)
    }

    // 操作记录类
    static class EditStep {
        Operation op;
        char char1;     // word1中的字符（对于删除/替换）
        char char2;     // word2中的字符（对于插入/替换）
        int pos1;       // word1中的位置
        int pos2;       // word2中的位置
        
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
                case INSERT: return String.format("在第%d位插入 '%c'", pos2, char2);
                case DELETE: return String.format("删除第%d位的 '%c'", pos1, char1);
                case REPLACE: return String.format("将第%d位的 '%c' 替换为 '%c'", pos1, char1, char2);
                case MATCH: return String.format("匹配第%d位的 '%c'", pos1, char1);
                default: return "无操作";
            }
        }
    }

    /**
     * 计算最小编辑距离（基础DP）
     * 
     * @param word1 字符串1
     * @param word2 字符串2
     * @return 最小编辑距离
     */
    public static int minDistanceBasic(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        // dp[i][j] 表示 word1[0..i-1] 到 word2[0..j-1] 的最小编辑距离
        int[][] dp = new int[m+1][n+1];
        
        // 初始化
        for(int i = 0; i <= m; i++) dp[i][0] = i;
        for(int j = 0; j <= n; j++) dp[0][j] = j;
        
        // 填充DP表
        for(int i = 1; i <= m; i++) {
            for(int j = 1; j <= n; j++) {
                if(word1.charAt(i-1) == word2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1]; // 字符匹配，无需操作
                } else {
                    dp[i][j] = 1 + Math.min(
                        Math.min(dp[i-1][j],   // 删除
                                 dp[i][j-1]), // 插入
                        dp[i-1][j-1]            // 替换
                    );
                }
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 计算最小编辑距离（空间优化）
     * 
     * @param word1 字符串1
     * @param word2 字符串2
     * @return 最小编辑距离
     */
    public static int minDistanceOptimized(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        
        int[] dp = new int[n+1];
        
        // 初始化第一行
        for(int j = 0; j <= n; j++) dp[j] = j;
        
        for(int i = 1; i <= m; i++) {
            int prev = dp[0]; // 保存左上角的值
            dp[0] = i; // 当前行的第一个元素
            
            for(int j = 1; j <= n; j++) {
                int temp = dp[j]; // 保存当前值，供下一次迭代使用
                
                if(word1.charAt(i-1) == word2.charAt(j-1)) {
                    dp[j] = prev; // 使用左上角的值
                } else {
                    dp[j] = 1 + Math.min(Math.min(dp[j], dp[j-1]), prev);
                }
                
                prev = temp; // 更新左上角的值
            }
        }
        
        return dp[n];
    }
    
    /**
     * 计算最小编辑距离并返回操作序列
     * 
     * @param word1 字符串1
     * @param word2 字符串2
     * @return 编辑操作序列
     */
    public static LinkedList<EditStep> getEditSequence(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m+1][n+1];
        Operation[][] operations = new Operation[m+1][n+1];
        
        // 初始化
        for(int i = 0; i <= m; i++) {
            dp[i][0] = i;
            if(i > 0) operations[i][0] = Operation.DELETE;
        }
        for(int j = 0; j <= n; j++) {
            dp[0][j] = j;
            if(j > 0) operations[0][j] = Operation.INSERT;
        }
        
        // 填充DP表并记录操作
        for(int i = 1; i <= m; i++) {
            for(int j = 1; j <= n; j++) {
                char c1 = word1.charAt(i-1);
                char c2 = word2.charAt(j-1);
                
                if(c1 == c2) {
                    dp[i][j] = dp[i-1][j-1];
                    operations[i][j] = Operation.MATCH;
                } else {
                    int del = dp[i-1][j] + 1;
                    int ins = dp[i][j-1] + 1;
                    int rep = dp[i-1][j-1] + 1;
                    
                    if(del <= ins && del <= rep) {
                        dp[i][j] = del;
                        operations[i][j] = Operation.DELETE;
                    } else if(ins <= del && ins <= rep) {
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
        LinkedList<EditStep> sequence = new LinkedList<>();
        int i = m, j = n;
        while(i > 0 || j > 0) {
            Operation op = operations[i][j];
            char c1 = i > 0 ? word1.charAt(i-1) : ' ';
            char c2 = j > 0 ? word2.charAt(j-1) : ' ';
            
            if(op == null) {
                // 处理边界情况
                if(i > 0 && j == 0) op = Operation.DELETE;
                else if(j > 0 && i == 0) op = Operation.INSERT;
                else op = Operation.REPLACE; // 默认处理
            }
            
            switch(op) {
                case MATCH:
                    sequence.addFirst(new EditStep(op, c1, c2, i, j));
                    i--; j--;
                    break;
                case REPLACE:
                    sequence.addFirst(new EditStep(op, c1, c2, i, j));
                    i--; j--;
                    break;
                case INSERT:
                    sequence.addFirst(new EditStep(op, ' ', c2, 0, j));
                    j--;
                    break;
                case DELETE:
                    sequence.addFirst(new EditStep(op, c1, ' ', i, 0));
                    i--;
                    break;
            }
        }
        
        return sequence;
    }
    
    /**
     * 可视化编辑距离矩阵
     * 
     * @param word1 字符串1
     * @param word2 字符串2
     */
    public static void visualizeEditMatrix(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m+1][n+1];
        
        // 初始化
        for(int i = 0; i <= m; i++) dp[i][0] = i;
        for(int j = 0; j <= n; j++) dp[0][j] = j;
        
        // 填充DP表
        for(int i = 1; i <= m; i++) {
            for(int j = 1; j <= n; j++) {
                if(word1.charAt(i-1) == word2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = 1 + Math.min(
                        Math.min(dp[i-1][j], dp[i][j-1]),
                        dp[i-1][j-1]
                    );
                }
            }
        }
        
        // 打印矩阵
        System.out.println("\n编辑距离矩阵:");
        System.out.print("      ");
        for(char c : word2.toCharArray()) System.out.printf("   %c", c);
        System.out.println();
        
        for(int i = 0; i <= m; i++) {
            if(i == 0) System.out.print("  ");
            else System.out.printf("%c ", word1.charAt(i-1));
            
            for(int j = 0; j <= n; j++) {
                System.out.printf("%3d", dp[i][j]);
            }
            System.out.println();
        }
        
        // 打印编辑距离
        System.out.printf("\n最小编辑距离: %d\n", dp[m][n]);
    }
    
    /**
     * 可视化编辑操作序列
     * 
     * @param word1 原字符串
     * @param word2 目标字符串
     */
    public static void visualizeEditSequence(String word1, String word2) {
        LinkedList<EditStep> sequence = getEditSequence(word1, word2);
        StringBuilder current = new StringBuilder(word1);
        int offset = 0; // 处理删除/插入后的位置偏移
        
        System.out.println("\n编辑操作序列:");
        System.out.printf("原始: \"%s\"\n", word1);
        
        int stepNum = 1;
        for(EditStep step : sequence) {
            // 调整位置，考虑到之前的插入/删除操作
            int adjustedPos = step.op == Operation.INSERT 
                ? step.pos2 - 1 + offset
                : step.pos1 - 1 + offset;
            
            // 执行操作可视化
            switch(step.op) {
                case MATCH:
                    System.out.printf("%2d. 匹配: %s (无需操作)\n", 
                                     stepNum, step);
                    break;
                    
                case REPLACE:
                    System.out.printf("%2d. %s\n", stepNum, step);
                    current.setCharAt(adjustedPos, step.char2);
                    System.out.printf("    当前: \"%s\"\n", current);
                    break;
                    
                case INSERT:
                    System.out.printf("%2d. %s\n", stepNum, step);
                    if(adjustedPos >= current.length()) {
                        current.append(step.char2);
                    } else {
                        current.insert(adjustedPos, step.char2);
                    }
                    offset++; // 插入后后续位置增加
                    System.out.printf("    当前: \"%s\"\n", current);
                    break;
                    
                case DELETE:
                    System.out.printf("%2d. %s\n", stepNum, step);
                    current.deleteCharAt(adjustedPos);
                    offset--; // 删除后后续位置减少
                    System.out.printf("    当前: \"%s\"\n", current);
                    break;
            }
            
            stepNum++;
        }
        
        System.out.println("最终达到: \"" + word2 + "\"");
    }
    
    // ================== 应用场景实现 ==================
    
    /**
     * 拼写检查与修正建议
     * 
     * @param word 输入的单词
     * @param dictionary 词典
     * @param maxSuggestions 最大建议数
     * @return 修正建议列表
     */
    public static List<String> spellCheck(String word, List<String> dictionary, int maxSuggestions) {
        // 计算单词与词典中每个单词的编辑距离
        List<String> suggestions = new ArrayList<>();
        PriorityQueue<DictEntry> queue = new PriorityQueue<>(Comparator.comparingInt(e -> e.distance));
        
        for(String dictWord : dictionary) {
            int dist = minDistanceOptimized(word, dictWord);
            queue.add(new DictEntry(dictWord, dist));
        }
        
        // 获取前几个建议
        for(int i = 0; i < maxSuggestions && !queue.isEmpty(); i++) {
            suggestions.add(queue.poll().word);
        }
        
        return suggestions;
    }
    
    static class DictEntry {
        String word;
        int distance;
        
        public DictEntry(String word, int distance) {
            this.word = word;
            this.distance = distance;
        }
    }
    
    /**
     * DNA序列比对可视化
     * 
     * @param seq1 DNA序列1
     * @param seq2 DNA序列2
     */
    public static void visualizeDnaAlignment(String seq1, String seq2) {
        LinkedList<EditStep> operations = getEditSequence(seq1, seq2);
        StringBuilder aligned1 = new StringBuilder();
        StringBuilder aligned2 = new StringBuilder();
        StringBuilder symbols = new StringBuilder();
        
        int idx1 = 0, idx2 = 0;
        
        for(EditStep op : operations) {
            switch(op.op) {
                case MATCH:
                    aligned1.append(seq1.charAt(idx1));
                    aligned2.append(seq2.charAt(idx2));
                    symbols.append('|');
                    idx1++;
                    idx2++;
                    break;
                    
                case REPLACE:
                    aligned1.append(seq1.charAt(idx1));
                    aligned2.append(seq2.charAt(idx2));
                    symbols.append('*');
                    idx1++;
                    idx2++;
                    break;
                    
                case INSERT:
                    aligned1.append('-');
                    aligned2.append(seq2.charAt(idx2));
                    symbols.append(' ');
                    idx2++;
                    break;
                    
                case DELETE:
                    aligned1.append(seq1.charAt(idx1));
                    aligned2.append('-');
                    symbols.append(' ');
                    idx1++;
                    break;
            }
        }
        
        System.out.println("\nDNA序列比对:");
        System.out.println(aligned1);
        System.out.println(symbols);
        System.out.println(aligned2);
        System.out.println("说明: | 表示匹配, * 表示替换, - 表示插入/删除的空白");
    }
    
    /**
     * 文件差异比较
     * 
     * @param file1 文件内容1（按行）
     * @param file2 文件内容2（按行）
     * @return 差异结果
     */
    public static String fileDiff(List<String> file1, List<String> file2) {
        StringBuilder diff = new StringBuilder();
        int[][] dp = new int[file1.size() + 1][file2.size() + 1];
        Operation[][] operations = new Operation[file1.size() + 1][file2.size() + 1];
        
        // 初始化
        for(int i = 0; i <= file1.size(); i++) {
            dp[i][0] = i;
            operations[i][0] = Operation.DELETE;
        }
        for(int j = 0; j <= file2.size(); j++) {
            dp[0][j] = j;
            operations[0][j] = Operation.INSERT;
        }
        
        // 填充DP表
        for(int i = 1; i <= file1.size(); i++) {
            for(int j = 1; j <= file2.size(); j++) {
                if(file1.get(i-1).equals(file2.get(j-1))) {
                    dp[i][j] = dp[i-1][j-1];
                    operations[i][j] = Operation.MATCH;
                } else {
                    int del = dp[i-1][j] + 1;
                    int ins = dp[i][j-1] + 1;
                    int rep = dp[i-1][j-1] + 1;
                    
                    if(del <= ins && del <= rep) {
                        dp[i][j] = del;
                        operations[i][j] = Operation.DELETE;
                    } else if(ins <= del && ins <= rep) {
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
        int i = file1.size(), j = file2.size();
        LinkedList<String> diffLines = new LinkedList<>();
        
        while(i > 0 || j > 0) {
            Operation op = operations[i][j];
            
            if(op == null) {
                // 边界处理
                if(i > 0) op = Operation.DELETE;
                else op = Operation.INSERT;
            }
            
            switch(op) {
                case MATCH:
                    diffLines.addFirst("    " + file1.get(i-1));
                    i--; j--;
                    break;
                    
                case REPLACE:
                    diffLines.addFirst(String.format("修改: -%s", file1.get(i-1)));
                    diffLines.addFirst(String.format("      +%s", file2.get(j-1)));
                    i--; j--;
                    break;
                    
                case INSERT:
                    diffLines.addFirst(String.format("添加: +%s", file2.get(j-1)));
                    j--;
                    break;
                    
                case DELETE:
                    diffLines.addFirst(String.format("删除: -%s", file1.get(i-1)));
                    i--;
                    break;
            }
        }
        
        // 构建差异结果
        diff.append("文件差异比较结果:\n");
        for(String line : diffLines) {
            diff.append(line).append("\n");
        }
        
        return diff.toString();
    }
    
    // ================== 测试用例 ==================
    
    public static void main(String[] args) {
        testBasicCases();
        testEdgeCases();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基本测试 ======");
        String word1 = "intention";
        String word2 = "execution";
        
        int basicDist = minDistanceBasic(word1, word2);
        int optimizedDist = minDistanceOptimized(word1, word2);
        
        System.out.printf("'%s' -> '%s'\n", word1, word2);
        System.out.println("基础DP距离: " + basicDist);
        System.out.println("优化DP距离: " + optimizedDist);
        
        // 可视化
        visualizeEditMatrix(word1, word2);
        visualizeEditSequence(word1, word2);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        // 空字符串测试
        System.out.println("空字符串测试:");
        System.out.printf("'' -> 'test': %d\n", minDistanceOptimized("", "test"));
        System.out.printf("'algorithm' -> '': %d\n", minDistanceOptimized("algorithm", ""));
        
        // 相同字符串
        System.out.println("\n相同字符串测试:");
        System.out.printf("'identical' -> 'identical': %d\n", 
                          minDistanceOptimized("identical", "identical"));
        
        // 一个字符差异
        System.out.println("\n单字符差异测试:");
        System.out.printf("'abcd' -> 'abed': %d\n", 
                          minDistanceOptimized("abcd", "abed"));
        
        // 长字符串测试
        System.out.println("\n长字符串测试:");
        String s1 = "supercalifragilisticexpialidocious";
        String s2 = "supercalifragilisticexpialibxious";
        int dist = minDistanceOptimized(s1, s2);
        System.out.printf("%s -> %s\n距离: %d\n", s1, s2, dist);
        
        // 获取操作序列
        LinkedList<EditStep> sequence = getEditSequence(s1, s2);
        System.out.println("操作数: " + sequence.size());
        System.out.println("前5个操作:");
        sequence.stream().limit(5).forEach(System.out::println);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 生成长字符串
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        Random rand = new Random();
        for(int i = 0; i < 10000; i++) {
            char c = (char)('a' + rand.nextInt(26));
            sb1.append(c);
            if(rand.nextDouble() > 0.1) {
                sb2.append(c);
            } else {
                sb2.append((char)('a' + rand.nextInt(26)));
            }
        }
        String longWord1 = sb1.toString();
        String longWord2 = sb2.toString();
        
        System.out.println("字符串长度: " + longWord1.length());
        
        // 空间优化DP
        long start = System.currentTimeMillis();
        int distOptimized = minDistanceOptimized(longWord1, longWord2);
        long end = System.currentTimeMillis();
        System.out.printf("优化DP: %d (耗时: %d ms)\n", distOptimized, end - start);
        
        // 基本DP (仅适用于较短字符串)
        if(longWord1.length() < 500) {
            start = System.currentTimeMillis();
            int distBasic = minDistanceBasic(longWord1, longWord2);
            end = System.currentTimeMillis();
            System.out.printf("基本DP: %d (耗时: %d ms)\n", distBasic, end - start);
        }
        
        // 操作序列计算
        if(longWord1.length() < 100) {
            start = System.currentTimeMillis();
            getEditSequence(longWord1, longWord2);
            end = System.currentTimeMillis();
            System.out.println("操作序列计算耗时: " + (end - start) + " ms");
        }
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 拼写检查
        System.out.println("1. 拼写检查:");
        List<String> dictionary = Arrays.asList(
            "algorithm", "datastructure", "computer", "science", 
            "programming", "java", "python", "javascript"
        );
        List<String> suggestions = spellCheck("algoritm", dictionary, 3);
        System.out.println("对于 'algoritm' 的建议: " + suggestions);
        
        // 场景2: DNA序列比对
        System.out.println("\n2. DNA序列比对:");
        String dna1 = "GATCACAGTACCAGAT";
        String dna2 = "GATCAAGTAACAGAT";
        visualizeDnaAlignment(dna1, dna2);
        
        // 场景3: 文件差异比较
        System.out.println("\n3. 文件差异比较:");
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