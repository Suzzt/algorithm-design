package main.java.org.dao.qa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 正则表达式匹配
 *
 * <p><b>问题描述</b>:
 * 给定一个字符串s和一个字符模式p，实现支持 '.' 和 '*' 的正则表达式匹配。
 * '.' 匹配任意单个字符
 * '*' 匹配零个或多个前面的元素
 * 匹配应覆盖整个输入字符串（而非部分）
 *
 * <p><b>示例</b>:
 * 输入: s = "aa", p = "a"
 * 输出: false
 * <p>
 * 输入: s = "aa", p = "a*"
 * 输出: true
 * <p>
 * 输入: s = "ab", p = ".*"
 * 输出: true
 *
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第10题)
 *
 * <p><b>解题思路</b>:
 * 1. 递归回溯法: 直接但效率低
 * 2. 动态规划: 二维DP表记录匹配状态
 * 3. NFA自动机: 复杂但高效的解法
 *
 * <p><b>时间复杂度</b>:
 * 递归回溯: 最坏情况指数级
 * 动态规划: O(m×n)
 * NFA自动机: O(m×n)
 *
 * <p><b>空间复杂度</b>:
 * 递归回溯: O(m+n) 栈空间
 * 动态规划: O(m×n)
 * NFA自动机: O(m×n)
 *
 * <p><b>应用场景</b>:
 * 1. 文本编辑器查找替换
 * 2. 编译器词法分析
 * 3. 日志文件过滤系统
 * 4. 文件名匹配
 * 5. 用户输入验证
 */

public class RegularExpressionMatching {

    // ========================= 解法1: 动态规划 =========================

    /**
     * 动态规划解法
     *
     * @param s 输入字符串
     * @param p 正则表达式模式
     * @return 是否匹配
     */
    public static boolean isMatchDP(String s, String p) {
        int m = s.length();
        int n = p.length();

        // dp[i][j]: s的前i个字符和p的前j个字符是否匹配
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true; // 空字符串匹配空模式

        // 处理模式开头可能出现的 'a*' 'b*' 等可以匹配空串的情况
        for (int j = 2; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2]; // '*'匹配0次
            }
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char sc = s.charAt(i - 1);
                char pc = p.charAt(j - 1);

                if (pc == '.' || sc == pc) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pc == '*') {
                    // 前一个字符是'.'或匹配当前字符
                    if (p.charAt(j - 2) == '.' || p.charAt(j - 2) == sc) {
                        // 匹配0次 || 匹配1次 || 匹配多次
                        dp[i][j] = dp[i][j - 2] || dp[i][j - 1] || dp[i - 1][j];
                    } else {
                        // 只能匹配0次
                        dp[i][j] = dp[i][j - 2];
                    }
                }
            }
        }

        return dp[m][n];
    }

    // ========================= 解法2: 递归回溯 =========================

    /**
     * 递归回溯解法
     *
     * @param s 输入字符串
     * @param p 正则表达式模式
     * @return 是否匹配
     */
    public static boolean isMatchRecursive(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        boolean firstMatch = !s.isEmpty() &&
                (p.charAt(0) == '.' || p.charAt(0) == s.charAt(0));

        if (p.length() >= 2 && p.charAt(1) == '*') {
            // 匹配0次 || (匹配1次 && 递归匹配剩余字符)
            return isMatchRecursive(s, p.substring(2)) ||
                    (firstMatch && isMatchRecursive(s.substring(1), p));
        } else {
            return firstMatch && isMatchRecursive(s.substring(1), p.substring(1));
        }
    }

    // ========================= 解法3: 有限状态自动机 =========================

    // 状态定义
    static class State {
        Map<Character, State> transitions = new HashMap<>();
        boolean isFinal = false;
    }

    /**
     * 编译正则表达式为有限状态自动机
     *
     * @param pattern 正则表达式
     * @return 初始状态
     */
    public static State compilePattern(String pattern) {
        State start = new State();
        State current = start;

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            State nextState = new State();

            if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '*') {
                // 处理'*'模式
                State loopState = new State();
                State skipState = new State();

                // 自循环状态
                loopState.transitions.put(c, loopState);
                if (c == '.') {
                    // '.'匹配任意字符
                    loopState.transitions.put('*', loopState); // 通配任意字符
                }

                // 跳转到下一个状态
                loopState.transitions.put('>', skipState); // '>'表示状态转移

                // 跳过'*'匹配
                skipState.transitions.put('>', nextState);
                current.transitions.put('>', nextState); // 零次匹配
                current.transitions.put('?', loopState); // 进入循环匹配

                current = skipState;
                i++; // 跳过'*'
            } else {
                // 单个字符匹配
                current.transitions.put(c, nextState);
                if (c == '.') {
                    // '.'匹配任意字符
                    current.transitions.put('*', nextState);
                }
                current = nextState;
            }
        }

        current.isFinal = true;
        return start;
    }

    /**
     * 使用有限状态自动机进行匹配
     *
     * @param s     输入字符串
     * @param start 初始状态
     * @return 是否匹配
     */
    public static boolean simulateNFA(String s, State start) {
        Set<State> currentStates = new HashSet<>();
        addState(currentStates, start);

        for (char c : s.toCharArray()) {
            Set<State> nextStates = new HashSet<>();
            for (State state : currentStates) {
                // 处理'.'通配符
                State next = state.transitions.get(c);
                if (next != null) {
                    addState(nextStates, next);
                }

                // 处理通配符
                next = state.transitions.get('.');
                if (next != null) {
                    addState(nextStates, next);
                }

                // 处理任意字符匹配
                next = state.transitions.get('*');
                if (next != null) {
                    addState(nextStates, next);
                }
            }
            currentStates = nextStates;
        }

        for (State state : currentStates) {
            if (state.isFinal) return true;
        }
        return false;
    }

    // 添加状态（包括ε转移）
    private static void addState(Set<State> states, State state) {
        if (!states.contains(state)) {
            states.add(state);
            State next = state.transitions.get('>');
            if (next != null) {
                addState(states, next);
            }
        }
    }

    // ========================= 应用场景 =========================

    /**
     * 日志过滤系统
     *
     * @param log     日志条目
     * @param pattern 过滤模式
     * @return 是否匹配模式
     */
    public static boolean logFilter(String log, String pattern) {
        return isMatchDP(log, pattern);
    }

    /**
     * 文件名匹配器
     *
     * @param filename 文件名
     * @param pattern  匹配模式
     * @return 是否匹配
     */
    public static boolean fileMatcher(String filename, String pattern) {
        return isMatchDP(filename, pattern);
    }

    /**
     * 用户输入验证器
     *
     * @param input   用户输入
     * @param pattern 验证模式
     * @return 是否有效
     */
    public static boolean inputValidator(String input, String pattern) {
        return isMatchDP(input, pattern);
    }

    // ========================= 可视化工具 =========================

    /**
     * 可视化DP表
     *
     * @param s 字符串
     * @param p 模式
     */
    public static void visualizeDPTable(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;

        // 打印表头
        System.out.print("\nDP Table: \n       ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%3c", p.charAt(j));
        }
        System.out.println();

        // 初始化第一行（空字符串匹配）
        for (int j = 2; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2];
            }
        }

        // 填充DP表
        for (int i = 0; i <= m; i++) {
            if (i > 0) {
                System.out.printf("%3c |", s.charAt(i - 1));
            } else {
                System.out.print("    |");
            }

            for (int j = 0; j <= n; j++) {
                if (i == 0 && j == 0) {
                    dp[0][0] = true;
                } else if (i > 0 && j > 0) {
                    char sc = s.charAt(i - 1);
                    char pc = p.charAt(j - 1);

                    if (pc == '.' || sc == pc) {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else if (pc == '*') {
                        if (j > 1) {
                            if (p.charAt(j - 2) == '.' || p.charAt(j - 2) == sc) {
                                dp[i][j] = dp[i][j - 2] || dp[i][j - 1] || dp[i - 1][j];
                            } else {
                                dp[i][j] = dp[i][j - 2];
                            }
                        }
                    }
                }

                // 打印当前单元格
                if (j > 0) {
                    System.out.printf("%3s", dp[i][j] ? " T" : " F");
                }
            }
            System.out.println();
        }
    }

    /**
     * 可视化NFA状态转移
     *
     * @param pattern 正则模式
     */
    public static void visualizeNFA(String pattern) {
        State start = compilePattern(pattern);
        System.out.println("\nNFA for Pattern: " + pattern);
        visualizeState(start, new HashSet<>(), 0);
    }

    // 递归可视化状态机
    private static void visualizeState(State state, Set<State> visited, int level) {
        if (visited.contains(state)) return;
        visited.add(state);

        // 打印状态
        String prefix = "  " + level;
        System.out.println(prefix + "State[" + (state.isFinal ? "final" : "") + "]");

        // 打印转移
        for (Map.Entry<Character, State> entry : state.transitions.entrySet()) {
            char c = entry.getKey();
            State next = entry.getValue();
            System.out.printf(prefix + "  -- [%c] --> State\n", c);
            visualizeState(next, visited, level + 1);
        }
    }

    // ========================= 测试用例 =========================

    public static void main(String[] args) {
        testBasicCases();
        testComplexCases();
        testApplicationScenarios();
    }

    private static void testBasicCases() {
        System.out.println("====== 基本测试 ======");

        // 测试DP算法
        testCase("aa", "a", false); // 不匹配
        testCase("aa", "a*", true); // '*'匹配
        testCase("ab", ".*", true); // '.'通配

        // 测试递归算法
        testCaseRecursive("mississippi", "mis*is*p*.", false);
        testCaseRecursive("aab", "c*a*b", true);
    }

    private static void testComplexCases() {
        System.out.println("\n====== 复杂测试 ======");

        // 测试自动机
        testCaseNFA("aa", "a");
        testCaseNFA("aa", "a*");
        testCaseNFA("ab", ".*");

        // 复杂模式测试
        testCase("abc", "a***b******c", true);
        testCase("abc", ".*.*.*", true);
        testCase("abc", "a*ab*bc*c", true);
        testCase("aaa", "a*a", true);
    }

    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");

        // 场景1: 日志过滤
        String log1 = "2023-08-15 ERROR: Database connection failed";
        System.out.println("日志过滤: '" + log1 + "'");
        System.out.println("匹配 'ERROR:.*' -> " + logFilter(log1, "ERROR:.*"));
        System.out.println("匹配 'WARN:.*' -> " + logFilter(log1, "WARN:.*"));

        // 场景2: 文件名匹配
        String filename = "document_backup_2023-08-15.pdf";
        System.out.println("\n文件名匹配: '" + filename + "'");
        System.out.println("匹配 '*backup*.pdf' -> " +
                fileMatcher(filename, ".*backup.*\\.pdf"));
        System.out.println("匹配 '*.doc' -> " + fileMatcher(filename, ".*\\.doc"));

        // 场景3: 输入验证
        String email = "user@example.com";
        String emailPattern = "[a-z]+@[a-z]+\\.[a-z]{2,4}";
        System.out.println("\n邮箱验证: '" + email + "'");
        System.out.println("是否有效邮箱: " + inputValidator(email, emailPattern));

        // 可视化DP表
        visualizeDPTable("xaabyc", "xa*b.c");

        // 可视化NFA
        visualizeNFA("a*b.c");
    }

    // DP测试用例
    private static void testCase(String s, String p, boolean expected) {
        boolean result = isMatchDP(s, p);
        System.out.printf("DP: s='%-10s' p='%-10s' -> %-5s (期望: %s)%n",
                s, p, result, expected);
        if (result != expected) {
            System.err.println("错误: 结果不符合预期!");
        }
        visualizeDPTable(s, p);
    }

    // 递归测试用例
    private static void testCaseRecursive(String s, String p, boolean expected) {
        boolean result = isMatchRecursive(s, p);
        System.out.printf("递归: s='%-10s' p='%-10s' -> %-5s (期望: %s)%n",
                s, p, result, expected);
        if (result != expected) {
            System.err.println("错误: 结果不符合预期!");
        }
    }

    // NFA测试用例
    private static void testCaseNFA(String s, String p) {
        State nfa = compilePattern(p);
        boolean dpResult = isMatchDP(s, p);
        boolean nfaResult = simulateNFA(s, nfa);

        System.out.printf("NFA: s='%-5s' p='%-5s' -> DP:%-5s NFA:%-5s%n",
                s, p, dpResult, nfaResult);

        if (dpResult != nfaResult) {
            System.err.println("错误: DP和NFA结果不一致!");
            visualizeNFA(p);
        }
    }
}