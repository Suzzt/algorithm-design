package main.java.org.dao.qa;

import java.util.*;

/**
 * 最小窗口子序列问题
 * 
 * <p><b>问题描述</b>:
 * 给定字符串S和T，在S中找到最短的子串，使得T是此子串的子序列。
 * 如果有多个结果，返回起始索引最小的子串。
 * 
 * <p><b>示例</b>:
 * 输入: S = "abcdebdde", T = "bde"
 * 输出: "bcde" (因为T是"bcde"的子序列)
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第727题)
 * 
 * <p><b>解题思路</b>:
 * 1. 动态规划法: 使用DP表记录匹配状态
 * 2. 双指针滑动窗口法: 高效寻找最短窗口
 * 3. 状态机优化法: 构建子序列状态机
 * 
 * <p><b>时间复杂度</b>:
 *  动态规划: O(m×n)
 *  双指针法: O(n)
 *  状态机法: O(n)
 * 
 * <p><b>空间复杂度</b>:
 *  动态规划: O(m×n)
 *  双指针法: O(1)
 *  状态机法: O(m)
 * 
 * <p><b>应用场景</b>:
 * 1. 文本编辑器中的序列搜索
 * 2. DNA序列中的模式识别
 * 3. 代码分析中的API调用序列检测
 * 4. 日志分析中的事件序列匹配
 * 5. 语音识别中的音素序列匹配
 */

public class MinimumWindowSubsequence {
    
    // ========================= 解法1: 动态规划 =========================
    
    /**
     * 动态规划解法
     * 
     * @param S 源字符串
     * @param T 目标序列
     * @return 最小窗口子串
     */
    public static String minWindowSubsequenceDP(String S, String T) {
        int m = S.length(), n = T.length();
        // dp[i][j]: 在S的前i个字符中，匹配T的前j个字符的最小起始位置
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化
        for (int i = 0; i <= m; i++) {
            Arrays.fill(dp[i], -1);
            dp[i][0] = i; // 空序列匹配任意位置
        }
        
        int minLen = Integer.MAX_VALUE;
        int start = -1;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (S.charAt(i - 1) == T.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
            
            // 完全匹配时更新最小窗口
            if (dp[i][n] != -1) {
                int len = i - dp[i][n];
                if (len < minLen) {
                    minLen = len;
                    start = dp[i][n];
                }
            }
        }
        
        return start == -1 ? "" : S.substring(start, start + minLen);
    }
    
    // ========================= 解法2: 双指针滑动窗口 =========================
    
    /**
     * 双指针滑动窗口解法
     * 
     * @param S 源字符串
     * @param T 目标序列
     * @return 最小窗口子串
     */
    public static String minWindowSubsequenceTwoPointers(String S, String T) {
        int n = S.length(), m = T.length();
        int minLen = Integer.MAX_VALUE;
        int start = -1;
        
        int i = 0, j = 0;
        while (i < n) {
            // 匹配T的字符
            if (S.charAt(i) == T.charAt(j)) {
                j++;
                // 完全匹配T
                if (j == m) {
                    int end = i; // 窗口结束位置
                    j--; // 回溯到T的最后一个字符
                    int k = i; // 从后向前匹配
                    
                    // 从后向前寻找最短窗口
                    while (j >= 0) {
                        if (S.charAt(k) == T.charAt(j)) {
                            j--;
                        }
                        k--;
                    }
                    k++; // 窗口起始位置
                    
                    // 更新最小窗口
                    int len = end - k + 1;
                    if (len < minLen) {
                        minLen = len;
                        start = k;
                    }
                    
                    j = 0; // 重置T指针
                    i = k; // 从下一个位置开始
                }
            }
            i++;
        }
        
        return start == -1 ? "" : S.substring(start, start + minLen);
    }
    
    // ========================= 解法3: 状态机优化法 =========================
    
    /**
     * 状态机优化解法
     * 
     * @param S 源字符串
     * @param T 目标序列
     * @return 最小窗口子串
     */
    public static String minWindowSubsequenceStateMachine(String S, String T) {
        // 构建状态转移表
        int[] next = new int[26]; // 每个字符的下一个出现位置
        Arrays.fill(next, -1);
        int[] last = new int[T.length()]; // 每个状态的上一个状态
        
        // 预处理T的状态转移
        for (int i = T.length() - 1; i >= 0; i--) {
            int c = T.charAt(i) - 'a';
            last[i] = next[c];
            next[c] = i;
        }
        
        int minLen = Integer.MAX_VALUE;
        int start = -1;
        int[] state = new int[S.length()]; // 记录每个位置的状态
        
        // 初始化状态
        for (int i = 0; i < S.length(); i++) {
            int c = S.charAt(i) - 'a';
            state[i] = (next[c] == 0) ? 1 : -1; // 如果匹配T的第一个字符
        }
        
        for (int i = 0; i < S.length(); i++) {
            if (state[i] == -1) continue;
            
            int currentState = state[i];
            int j = i;
            
            // 状态转移
            while (j < S.length() && currentState < T.length()) {
                char c = S.charAt(j);
                if (c == T.charAt(currentState)) {
                    currentState++;
                }
                j++;
            }
            
            // 完全匹配
            if (currentState == T.length()) {
                int len = j - i;
                if (len < minLen) {
                    minLen = len;
                    start = i;
                }
            }
        }
        
        return start == -1 ? "" : S.substring(start, start + minLen);
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 代码API调用序列检测
     * 
     * @param code 代码字符串
     * @param apiSequence API调用序列
     * @return 包含API序列的最短代码段
     */
    public static String detectAPISequence(String code, String apiSequence) {
        return minWindowSubsequenceTwoPointers(code, apiSequence);
    }
    
    /**
     * DNA序列模式识别
     * 
     * @param dna DNA序列
     * @param pattern 目标模式
     * @return 包含模式的最短序列
     */
    public static String findDNASubsequence(String dna, String pattern) {
        return minWindowSubsequenceStateMachine(dna, pattern);
    }
    
    /**
     * 日志事件序列分析
     * 
     * @param log 日志内容
     * @param eventSequence 事件序列
     * @return 包含事件序列的最短日志段
     */
    public static String analyzeEventSequence(String log, String eventSequence) {
        return minWindowSubsequenceDP(log, eventSequence);
    }
    
    /**
     * 语音识别音素匹配
     * 
     * @param phonemes 音素序列
     * @param targetSequence 目标音素序列
     * @return 包含目标序列的最短音素段
     */
    public static String matchPhonemeSequence(String phonemes, String targetSequence) {
        return minWindowSubsequenceTwoPointers(phonemes, targetSequence);
    }
    
    /**
     * 网络数据包序列检测
     * 
     * @param packets 数据包序列
     * @param signature 攻击特征序列
     * @return 包含特征序列的最短包序列
     */
    public static String detectAttackSignature(String packets, String signature) {
        return minWindowSubsequenceStateMachine(packets, signature);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化动态规划表
     * 
     * @param S 源字符串
     * @param T 目标序列
     */
    public static void visualizeDPTable(String S, String T) {
        int m = S.length(), n = T.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化
        for (int i = 0; i <= m; i++) {
            Arrays.fill(dp[i], -1);
            dp[i][0] = i;
        }
        
        System.out.println("\n动态规划表:");
        System.out.print("    ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%3c", T.charAt(j));
        }

        for (int i = 1; i <= m; i++) {
            System.out.printf("%c | ", S.charAt(i - 1));
            for (int j = 1; j <= n; j++) {
                if (S.charAt(i - 1) == T.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
                System.out.printf("%3d", dp[i][j]);
            }
            System.out.println();
        }
    }
    
    /**
     * 可视化双指针匹配过程
     * 
     * @param S 源字符串
     * @param T 目标序列
     */
    public static void visualizeTwoPointersProcess(String S, String T) {
        int n = S.length(), m = T.length();
        int minLen = Integer.MAX_VALUE;
        int start = -1;
        
        int i = 0, j = 0;
        int step = 1;
        
        System.out.println("\n双指针匹配过程:");
        System.out.println("步骤 | S指针 | T指针 | 当前字符 | 匹配状态");
        System.out.println("----|-------|-------|----------|----------");
        
        while (i < n) {
            char sChar = S.charAt(i);
            char tChar = j < m ? T.charAt(j) : ' ';
            String status = j < m ? "匹配中" : "完成匹配";
            
            System.out.printf("%4d | %5d | %5d | %8c | %-8s%n", 
                             step++, i, j, sChar, status);
            
            if (j < m && sChar == tChar) {
                j++;
                if (j == m) {
                    int end = i;
                    j--;
                    int k = i;
                    
                    // 回溯过程
                    System.out.println("  >> 开始回溯寻找最短窗口");
                    while (j >= 0) {
                        System.out.printf("      回溯: k=%d, T[%d]=%c, S[%d]=%c%n", 
                                         k, j, T.charAt(j), k, S.charAt(k));
                        if (S.charAt(k) == T.charAt(j)) {
                            j--;
                        }
                        k--;
                    }
                    k++;
                    
                    int len = end - k + 1;
                    if (len < minLen) {
                        minLen = len;
                        start = k;
                    }
                    
                    System.out.printf("  >> 找到窗口: [%d, %d] = %s%n", 
                                     k, end, S.substring(k, end + 1));
                    
                    j = 0;
                    i = k;
                }
            }
            i++;
        }
        
        if (start != -1) {
            System.out.println("最小窗口: " + S.substring(start, start + minLen));
        } else {
            System.out.println("未找到匹配窗口");
        }
    }
    
    /**
     * 可视化状态机转移
     * 
     * @param T 目标序列
     */
    public static void visualizeStateMachine(String T) {
        int[] next = new int[26];
        Arrays.fill(next, -1);
        int[] last = new int[T.length()];
        
        System.out.println("\n状态机构建:");
        System.out.println("状态 | 字符 | 下一个状态");
        System.out.println("----|------|----------");
        
        for (int i = T.length() - 1; i >= 0; i--) {
            int c = T.charAt(i) - 'a';
            last[i] = next[c];
            next[c] = i;
            
            System.out.printf("%4d | %4c | %d%n", 
                             i, T.charAt(i), last[i]);
        }
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param sSize 源字符串大小
     * @param tSize 目标序列大小
     */
    public static void comparePerformance(int sSize, int tSize) {
        // 生成测试数据
        String S = generateRandomString(sSize);
        String T = generateRandomString(tSize);
        
        long start, end;
        
        // 测试动态规划
        start = System.nanoTime();
        String dpResult = minWindowSubsequenceDP(S, T);
        end = System.nanoTime();
        long dpTime = end - start;
        
        // 测试双指针法
        start = System.nanoTime();
        String twoPointersResult = minWindowSubsequenceTwoPointers(S, T);
        end = System.nanoTime();
        long twoPointersTime = end - start;
        
        // 测试状态机法
        start = System.nanoTime();
        String stateMachineResult = minWindowSubsequenceStateMachine(S, T);
        end = System.nanoTime();
        long stateMachineTime = end - start;
        
        System.out.println("\n性能比较:");
        System.out.println("源字符串大小: " + sSize + ", 目标序列大小: " + tSize);
        System.out.println("===============================================");
        System.out.println("方法            | 时间(ns) | 结果长度");
        System.out.println("----------------|----------|----------");
        System.out.printf("动态规划        | %8d | %d%n", dpTime, dpResult.length());
        System.out.printf("双指针法        | %8d | %d%n", twoPointersTime, twoPointersResult.length());
        System.out.printf("状态机法        | %8d | %d%n", stateMachineTime, stateMachineResult.length());
        System.out.println("===============================================");
    }
    
    // 生成随机字符串
    private static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char)('a' + random.nextInt(26)));
        }
        return sb.toString();
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testEdgeCases();
        testApplicationScenarios();
        testPerformance();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        testCase("示例1", "abcdebdde", "bde", "bcde");
        testCase("示例2", "jmeksksrxcy", "srxc", "ksrx");
        testCase("示例3", "ffynmlzesdshlvugsigobutgaetsngl", "sig", "igsig");
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("相同字符串", "abc", "abc", "abc");
        testCase("目标为空", "abc", "", "");
        testCase("源为空", "", "abc", "");
        testCase("无匹配", "abcdef", "xyz", "");
        testCase("目标比源长", "abc", "abcdef", "");
        testCase("重复字符", "aabbcc", "abc", "abbcc");
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 代码API调用序列检测
        String code = "open();read();process();close();";
        String apiSequence = "read;process";
        System.out.println("API调用序列检测: " + detectAPISequence(code, apiSequence));
        
        // 场景2: DNA序列模式识别
        String dna = "ATGCGATACGCTAGCTAGCTAGCT";
        String pattern = "GATAC";
        System.out.println("DNA模式识别: " + findDNASubsequence(dna, pattern));
        
        // 场景3: 日志事件序列分析
        String log = "[INFO]Start;[DEBUG]Processing;[ERROR]Failed;[INFO]End";
        String eventSequence = "Processing;Failed";
        System.out.println("事件序列分析: " + analyzeEventSequence(log, eventSequence));
        
        // 场景4: 语音识别音素匹配
        String phonemes = "p,t,k,a,e,i,o,u";
        String targetSequence = "k,a";
        System.out.println("音素序列匹配: " + matchPhonemeSequence(phonemes, targetSequence));
        
        // 场景5: 网络攻击特征检测
        String packets = "SYN,ACK,SYN,ACK,FIN";
        String signature = "SYN,ACK,FIN";
        System.out.println("攻击特征检测: " + detectAttackSignature(packets, signature));
        
        // 可视化测试
        System.out.println("\n可视化测试:");
        visualizeDPTable("abcdebdde", "bde");
        visualizeTwoPointersProcess("abcdebdde", "bde");
        visualizeStateMachine("bde");
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100, 5);
        comparePerformance(1000, 10);
        comparePerformance(10000, 20);
    }
    
    private static void testCase(String name, String S, String T, String expected) {
        String dp = minWindowSubsequenceDP(S, T);
        String twoPointers = minWindowSubsequenceTwoPointers(S, T);
        String stateMachine = minWindowSubsequenceStateMachine(S, T);
        
        System.out.printf("\n测试: %s%n", name);
        System.out.println("源字符串: " + S);
        System.out.println("目标序列: " + T);
        System.out.println("动态规划结果: " + dp);
        System.out.println("双指针法结果: " + twoPointers);
        System.out.println("状态机法结果: " + stateMachine);
        
        boolean passed = expected.equals(dp) && expected.equals(twoPointers) && expected.equals(stateMachine);
        System.out.println("测试结果: " + (passed ? "通过" : "失败"));
        
        if (!passed) {
            System.out.println("预期结果: " + expected);
        }
        
        // 可视化小规模案例
        if (S.length() <= 20 && T.length() <= 5) {
            visualizeDPTable(S, T);
            visualizeTwoPointersProcess(S, T);
        }
    }
}