package main.java.org.dao.qa;

import java.util.*;

/**
 * 通配符匹配问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个字符串s和一个字符模式p，实现支持'?'和'*'的通配符匹配。
 * '?' 可以匹配任何单个字符。
 * '*' 可以匹配任意字符串（包括空字符串）。
 * 匹配应覆盖整个输入字符串（而非部分）
 * 
 * <p><b>示例</b>:
 * 输入: s = "adceb", p = "*a*b"
 * 输出: true
 * 解释: 第一个'*'匹配空字符串，第二个'*'匹配"dce"
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第44题)
 * 
 * <p><b>解题思路</b>:
 * 1. 动态规划法: 二维DP表记录匹配状态
 * 2. 双指针回溯法: 使用指针和回溯处理'*'
 * 3. 有限状态机法: 将模式转换为状态转移图
 * 
 * <p><b>时间复杂度</b>:
 *  动态规划: O(m×n)
 *  双指针回溯: O(m+n) 最坏O(m×n)
 *  状态机: O(m+n)
 * 
 * <p><b>空间复杂度</b>:
 *  动态规划: O(m×n)
 *  双指针回溯: O(1)
 *  状态机: O(m)
 * 
 * <p><b>应用场景</b>:
 * 1. 文件系统路径匹配
 * 2. 数据库查询模式匹配
 * 3. 搜索引擎关键词扩展
 * 4. 日志文件过滤
 * 5. 命令行工具参数解析
 */

public class WildcardMatching {
    
    // ========================= 解法1: 动态规划 =========================
    
    /**
     * 动态规划解法
     * 
     * @param s 输入字符串
     * @param p 模式字符串
     * @return 是否匹配
     */
    public static boolean isMatchDP(String s, String p) {
        int m = s.length();
        int n = p.length();
        
        // dp[i][j]: s的前i个字符和p的前j个字符是否匹配
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true; // 空字符串匹配空模式
        
        // 处理模式开头的连续'*'
        for (int j = 1; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 1];
            }
        }
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char sc = s.charAt(i - 1);
                char pc = p.charAt(j - 1);
                
                if (pc == '?' || sc == pc) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pc == '*') {
                    // '*'匹配0个字符 || '*'匹配多个字符
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                }
            }
        }
        
        return dp[m][n];
    }
    
    // ========================= 解法2: 双指针回溯法 =========================
    
    /**
     * 双指针回溯法 - 空间最优解
     * 
     * @param s 输入字符串
     * @param p 模式字符串
     * @return 是否匹配
     */
    public static boolean isMatchTwoPointers(String s, String p) {
        int i = 0; // s的索引
        int j = 0; // p的索引
        int starIdx = -1; // 最近一个'*'的位置
        int match = -1;    // 匹配s的起始位置
        
        while (i < s.length()) {
            if (j < p.length() && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
                // 字符匹配或'?'通配
                i++;
                j++;
            } else if (j < p.length() && p.charAt(j) == '*') {
                // 遇到'*'，记录位置用于回溯
                starIdx = j;
                match = i;
                j++; // 先尝试匹配0个字符
            } else if (starIdx != -1) {
                // 使用'*'匹配一个或多个字符
                j = starIdx + 1;
                match++;
                i = match;
            } else {
                return false;
            }
        }
        
        // 处理模式末尾的'*'
        while (j < p.length() && p.charAt(j) == '*') {
            j++;
        }
        
        return j == p.length();
    }
    
    // ========================= 解法3: 有限状态机法 =========================
    
    /**
     * 有限状态机解法
     * 
     * @param s 输入字符串
     * @param p 模式字符串
     * @return 是否匹配
     */
    public static boolean isMatchFSM(String s, String p) {
        // 压缩模式中的连续'*'
        StringBuilder compressed = new StringBuilder();
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) != '*' || (i > 0 && p.charAt(i - 1) != '*')) {
                compressed.append(p.charAt(i));
            }
        }
        p = compressed.toString();
        
        // 构建状态转移表
        List<Map<Character, Integer>> states = new ArrayList<>();
        states.add(new HashMap<>()); // 初始状态
        
        int state = 0;
        for (char c : p.toCharArray()) {
            if (c == '*') {
                // '*'可以匹配任意字符，包括空串
                states.get(state).put('*', state); // 自循环
            } else {
                int nextState = state + 1;
                if (nextState >= states.size()) {
                    states.add(new HashMap<>());
                }
                states.get(state).put(c, nextState);
                state = nextState;
            }
        }
        
        // 模拟状态机
        Set<Integer> currentStates = new HashSet<>();
        currentStates.add(0);
        
        for (char c : s.toCharArray()) {
            Set<Integer> nextStates = new HashSet<>();
            
            for (int st : currentStates) {
                Map<Character, Integer> transitions = states.get(st);
                
                // 处理通配符'?'和实际字符
                if (transitions.containsKey(c)) {
                    nextStates.add(transitions.get(c));
                }
                if (transitions.containsKey('?')) {
                    nextStates.add(transitions.get('?'));
                }
                if (transitions.containsKey('*')) {
                    nextStates.add(transitions.get('*'));
                }
            }
            
            if (nextStates.isEmpty()) return false;
            currentStates = nextStates;
        }
        
        // 检查是否到达接受状态
        return currentStates.contains(state);
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 文件系统匹配器
     * 
     * @param filename 文件名
     * @param pattern 匹配模式
     * @return 是否匹配
     */
    public static boolean fileMatcher(String filename, String pattern) {
        return isMatchTwoPointers(filename, pattern);
    }
    
    /**
     * 数据库查询过滤
     * 
     * @param query 查询字符串
     * @param filter 过滤模式
     * @return 是否匹配
     */
    public static boolean queryFilter(String query, String filter) {
        return isMatchFSM(query, filter);
    }
    
    /**
     * 日志文件搜索器
     * 
     * @param log 日志条目
     * @param searchPattern 搜索模式
     * @return 是否匹配
     */
    public static boolean logSearcher(String log, String searchPattern) {
        return isMatchDP(log, searchPattern);
    }
    
    /**
     * 命令行参数解析器
     * 
     * @param arg 命令行参数
     * @param paramPattern 参数模式
     * @return 是否匹配
     */
    public static boolean parseCommandParam(String arg, String paramPattern) {
        return isMatchTwoPointers(arg, paramPattern);
    }
    
    /**
     * 搜索引擎关键词扩展
     * 
     * @param keyword 用户关键词
     * @param expansionPattern 扩展模式
     * @return 是否匹配
     */
    public static boolean keywordExpansion(String keyword, String expansionPattern) {
        return isMatchFSM(keyword, expansionPattern);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化DP表
     * 
     * @param s 输入字符串
     * @param p 模式字符串
     */
    public static void visualizeDPTable(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        
        // 初始化第一行
        for (int j = 1; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 1];
            }
        }
        
        System.out.println("\nDP表可视化:");
        System.out.println("模式: " + p);
        System.out.println("字符串: " + s);
        System.out.println();
        
        // 打印表头
        System.out.print("      ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%3c", p.charAt(j));
        }

        // 在原有代码处修改
        System.out.println("\n    +" + repeatString("---", n));
        
        for (int i = 0; i <= m; i++) {
            if (i > 0) {
                System.out.printf("%3c |", s.charAt(i - 1));
            } else {
                System.out.print("     |");
            }
            
            for (int j = 0; j <= n; j++) {
                // 计算DP值
                if (i > 0 && j > 0) {
                    char sc = s.charAt(i - 1);
                    char pc = p.charAt(j - 1);
                    
                    if (pc == '?' || sc == pc) {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else if (pc == '*') {
                        dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                    }
                }
                
                // 打印单元格
                if (j > 0) {
                    System.out.printf("%3s", dp[i][j] ? " T" : " F");
                }
            }
            System.out.println();
        }
    }

    private static String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    /**
     * 可视化双指针匹配过程
     * 
     * @param s 输入字符串
     * @param p 模式字符串
     */
    public static void visualizePointers(String s, String p) {
        int i = 0, j = 0;
        int starIdx = -1, match = -1;
        int step = 1;
        
        System.out.println("\n双指针匹配过程:");
        System.out.printf("%5s | %4s | %4s | %6s | %6s | %-15s%n", 
                         "步骤", "s指针", "p指针", "starIdx", "match", "操作");
        System.out.println("------+------+------+--------+--------+----------------");
        
        while (i < s.length()) {
            if (j < p.length() && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
                System.out.printf("%5d | %4d | %4d | %6d | %6d | 匹配字符 %c%n", 
                                 step++, i, j, starIdx, match, s.charAt(i));
                i++;
                j++;
            } else if (j < p.length() && p.charAt(j) == '*') {
                System.out.printf("%5d | %4d | %4d | %6d | %6d | 记录*位置%n", 
                                 step++, i, j, j, match);
                starIdx = j;
                match = i;
                j++;
            } else if (starIdx != -1) {
                System.out.printf("%5d | %4d | %4d | %6d | %6d | 回溯匹配*%n", 
                                 step++, i, j, starIdx, match);
                j = starIdx + 1;
                match++;
                i = match;
            } else {
                System.out.printf("%5d | %4d | %4d | %6d | %6d | 匹配失败%n", 
                                 step++, i, j, starIdx, match);
                break;
            }
        }
        
        while (j < p.length() && p.charAt(j) == '*') {
            System.out.printf("%5d | %4d | %4d | %6d | %6d | 跳过末尾*%n", 
                             step++, i, j, starIdx, match);
            j++;
        }
        
        System.out.println("最终状态: " + (j == p.length() && i == s.length()));
    }
    
    /**
     * 可视化状态机
     * 
     * @param p 模式字符串
     */
    public static void visualizeStateMachine(String p) {
        // 压缩模式中的连续'*'
        StringBuilder compressed = new StringBuilder();
        for (int i = 0; i < p.length(); i++) {
            if (p.charAt(i) != '*' || (i > 0 && p.charAt(i - 1) != '*')) {
                compressed.append(p.charAt(i));
            }
        }
        p = compressed.toString();
        
        // 构建状态转移表
        List<Map<Character, Integer>> states = new ArrayList<>();
        states.add(new HashMap<>()); // 初始状态
        
        int state = 0;
        System.out.println("\n状态机构建:");
        System.out.println("模式: " + p);
        System.out.println("状态 | 转移");
        System.out.println("----|------------------");
        
        for (char c : p.toCharArray()) {
            Map<Character, Integer> transitions = states.get(state);
            if (c == '*') {
                transitions.put('*', state);
                System.out.printf("%4d | * -> %d (自循环)%n", state, state);
            } else {
                int nextState = state + 1;
                if (nextState >= states.size()) {
                    states.add(new HashMap<>());
                }
                transitions.put(c, nextState);
                System.out.printf("%4d | %c -> %d%n", state, c, nextState);
                state = nextState;
            }
        }
        
        // 添加通配符'?'转移
        for (int i = 0; i < states.size(); i++) {
            Map<Character, Integer> transitions = states.get(i);
            for (int j = i; j < states.size(); j++) {
                if (transitions.containsKey('*')) {
                    transitions.put('?', transitions.get('*'));
                }
            }
        }
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param sSize 字符串大小
     * @param pSize 模式大小
     */
    public static void comparePerformance(int sSize, int pSize) {
        String s = generateRandomString(sSize);
        String p = generatePattern(pSize);
        
        long start, end;
        
        // 测试动态规划
        start = System.nanoTime();
        boolean dpResult = isMatchDP(s, p);
        end = System.nanoTime();
        long dpTime = end - start;
        
        // 测试双指针
        start = System.nanoTime();
        boolean ptrResult = isMatchTwoPointers(s, p);
        end = System.nanoTime();
        long ptrTime = end - start;
        
        // 测试状态机
        start = System.nanoTime();
        boolean fsmResult = isMatchFSM(s, p);
        end = System.nanoTime();
        long fsmTime = end - start;
        
        System.out.println("\n性能比较:");
        System.out.println("字符串大小: " + sSize + ", 模式大小: " + pSize);
        System.out.println("===============================================");
        System.out.println("方法            | 时间(ns) | 结果");
        System.out.println("----------------|----------|--------");
        System.out.printf("动态规划        | %8d | %b%n", dpTime, dpResult);
        System.out.printf("双指针回溯      | %8d | %b%n", ptrTime, ptrResult);
        System.out.printf("有限状态机      | %8d | %b%n", fsmTime, fsmResult);
        System.out.println("===============================================");
    }
    
    // 生成随机字符串
    private static String generateRandomString(int size) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append((char)('a' + random.nextInt(26)));
        }
        return sb.toString();
    }
    
    // 生成随机模式
    private static String generatePattern(int size) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        char[] options = {'a', 'b', '?', '*'};
        
        for (int i = 0; i < size; i++) {
            char c = options[random.nextInt(options.length)];
            sb.append(c);
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
        testCase("示例1", "aa", "a", false);
        testCase("示例2", "aa", "*", true);
        testCase("示例3", "cb", "?a", false);
        testCase("示例4", "adceb", "*a*b", true);
        testCase("示例5", "acdcb", "a*c?b", false);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("空字符串", "", "", true);
        testCase("空模式", "abc", "", false);
        testCase("空字符串带星", "", "*", true);
        testCase("空字符串带问号", "", "?", false);
        testCase("全星号", "abcdef", "******", true);
        testCase("连续星号", "abc", "a**b**c", true);
        testCase("星号开头", "abc", "*abc", true);
        testCase("星号结尾", "abc", "abc*", true);
        testCase("复杂模式", "abcababcd", "a*b?*d", true);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 文件匹配
        System.out.println("文件匹配:");
        System.out.println("file.txt 匹配 *.txt: " + fileMatcher("file.txt", "*.txt"));
        System.out.println("document.docx 匹配 *.doc: " + fileMatcher("document.docx", "*.doc"));
        System.out.println("backup2023.tar.gz 匹配 backup*.gz: " + fileMatcher("backup2023.tar.gz", "backup*.gz"));
        
        // 场景2: 日志过滤
        System.out.println("\n日志过滤:");
        System.out.println("[ERROR] 匹配 [*]: " + logSearcher("[ERROR]", "[*]"));
        System.out.println("User login 匹配 User*: " + logSearcher("User login", "User*"));
        System.out.println("Data export failed 匹配 *fail*: " + logSearcher("Data export failed", "*fail*"));
        
        // 场景3: 数据库查询
        System.out.println("\n数据库查询:");
        System.out.println("SELECT * 匹配 SELECT?*: " + queryFilter("SELECT *", "SELECT?*"));
        System.out.println("WHERE id=10 匹配 WHERE*id=*: " + queryFilter("WHERE id=10", "WHERE*id=*"));
        
        // 场景4: 命令行参数
        System.out.println("\n命令行参数:");
        System.out.println("-output=file 匹配 -output*: " + parseCommandParam("-output=file", "-output*"));
        System.out.println("--verbose --debug 匹配 --*debug: " + parseCommandParam("--verbose --debug", "--*debug"));
        
        // 场景5: 关键词扩展
        System.out.println("\n关键词扩展:");
        System.out.println("dat 匹配 dat*: " + keywordExpansion("dat", "dat*"));
        System.out.println("computer 匹配 *comp*: " + keywordExpansion("computer", "*comp*"));
        
        // 可视化测试
        System.out.println("\n可视化测试:");
        visualizeDPTable("adceb", "*a*b");
        visualizePointers("adceb", "*a*b");
        visualizeStateMachine("*a*b");
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100, 10);
        comparePerformance(1000, 20);
        comparePerformance(10000, 50);
    }
    
    private static void testCase(String name, String s, String p, boolean expected) {
        boolean dp = isMatchDP(s, p);
        boolean ptr = isMatchTwoPointers(s, p);
        boolean fsm = isMatchFSM(s, p);
        
        System.out.printf("\n测试: %s\n", name);
        System.out.println("字符串: " + s);
        System.out.println("模式: " + p);
        System.out.println("动态规划结果: " + dp);
        System.out.println("双指针结果: " + ptr);
        System.out.println("状态机结果: " + fsm);
        
        if (dp != expected || ptr != expected || fsm != expected) {
            System.err.println("错误: 结果不符合预期!");
        }
        
        // 可视化小规模案例
        if (s.length() < 20 && p.length() < 10) {
            visualizeDPTable(s, p);
            visualizePointers(s, p);
        }
    }
}