package main.java.org.dao.qa;

import java.util.*;

/**
 * 最长有效括号问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个只包含 '(' 和 ')' 的字符串，找出最长的包含有效括号的子串的长度。
 * 
 * <p><b>示例</b>:
 * 输入: "(()"
 * 输出: 2
 * 解释: 最长有效括号子串为 "()"
 * 
 * 输入: ")()())"
 * 输出: 4
 * 解释: 最长有效括号子串为 "()()"
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第32题)
 * 
 * <p><b>解题思路</b>:
 * 1. 动态规划法: 使用dp数组记录以每个位置结尾的最长有效括号长度
 * 2. 栈解法: 利用栈匹配括号并计算长度
 * 3. 双指针法: 左右扫描两次计算最大长度
 * 
 * <p><b>时间复杂度</b>:
 *  所有方法: O(n)
 * 
 * <p><b>空间复杂度</b>:
 *  动态规划: O(n)
 *  栈解法: O(n)
 *  双指针法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 编译器语法检查
 * 2. 代码编辑器自动补全
 * 3. 数学表达式验证
 * 4. 配置文件语法分析
 * 5. 数据格式校验
 */

public class LongestValidParentheses {
    
    // ========================= 解法1: 动态规划 =========================
    
    /**
     * 动态规划解法
     * 
     * @param s 输入字符串
     * @return 最长有效括号长度
     */
    public static int longestValidParenthesesDP(String s) {
        int n = s.length();
        if (n < 2) return 0;
        
        int[] dp = new int[n]; // dp[i]表示以i结尾的最长有效括号长度
        int maxLen = 0;
        
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                if (s.charAt(i - 1) == '(') {
                    // 情况1: 直接匹配 "()"
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                } else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                    // 情况2: 嵌套匹配 "((...))"
                    dp[i] = dp[i - 1] + 
                            ((i - dp[i - 1] >= 2) ? dp[i - dp[i - 1] - 2] : 0) + 2;
                }
                maxLen = Math.max(maxLen, dp[i]);
            }
        }
        return maxLen;
    }
    
    // ========================= 解法2: 栈解法 =========================
    
    /**
     * 栈解法
     * 
     * @param s 输入字符串
     * @return 最长有效括号长度
     */
    public static int longestValidParenthesesStack(String s) {
        int maxLen = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1); // 初始化栈底索引
        
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i); // 更新基准点
                } else {
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        return maxLen;
    }
    
    // ========================= 解法3: 双指针法 =========================
    
    /**
     * 双指针法 - 空间最优
     * 
     * @param s 输入字符串
     * @return 最长有效括号长度
     */
    public static int longestValidParenthesesTwoPointers(String s) {
        int left = 0, right = 0;
        int maxLen = 0;
        
        // 从左向右扫描
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }
            
            if (left == right) {
                maxLen = Math.max(maxLen, 2 * right);
            } else if (right > left) {
                left = right = 0;
            }
        }
        
        // 从右向左扫描
        left = right = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }
            
            if (left == right) {
                maxLen = Math.max(maxLen, 2 * left);
            } else if (left > right) {
                left = right = 0;
            }
        }
        
        return maxLen;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 代码编辑器语法检查器
     * 
     * @param code 代码片段
     * @return 最长有效括号长度
     */
    public static int codeSyntaxChecker(String code) {
        return longestValidParenthesesDP(code);
    }
    
    /**
     * 配置文件验证器
     * 
     * @param config 配置文件内容
     * @return 是否括号匹配
     */
    public static boolean validateConfigFile(String config) {
        int maxLen = longestValidParenthesesStack(config);
        return maxLen == config.length();
    }
    
    /**
     * 数学表达式解析器
     * 
     * @param expression 数学表达式
     * @return 表达式有效性分数
     */
    public static int mathExpressionParser(String expression) {
        int maxLen = longestValidParenthesesTwoPointers(expression);
        return maxLen * 100 / expression.length();
    }
    
    /**
     * 数据格式校验器
     * 
     * @param data 数据字符串
     * @return 数据格式是否有效
     */
    public static boolean validateDataFormat(String data) {
        return longestValidParenthesesStack(data) == data.length();
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化括号匹配过程
     * 
     * @param s 输入字符串
     */
    public static void visualizeParenthesesMatching(String s) {
        System.out.println("\n括号匹配可视化: " + s);
        System.out.println("索引: " + getIndexString(s));
        System.out.println("字符: " + s);
        
        // 动态规划可视化
        visualizeDPProcess(s);
        
        // 栈解法可视化
        visualizeStackProcess(s);
    }
    
    // 生成索引字符串
    private static String getIndexString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(i % 10); // 只显示个位数
        }
        return sb.toString();
    }
    
    // 可视化动态规划过程
    private static void visualizeDPProcess(String s) {
        System.out.println("\n动态规划过程:");
        int n = s.length();
        int[] dp = new int[n];
        
        System.out.println("i | char | dp[i] | 解释");
        System.out.println("--|------|-------|----------------");
        
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                if (s.charAt(i - 1) == '(') {
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                    System.out.printf("%d | %c    | %5d | 匹配'()'%n", i, s.charAt(i), dp[i]);
                } else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                    dp[i] = dp[i - 1] + ((i - dp[i - 1] >= 2) ? dp[i - dp[i - 1] - 2] : 0) + 2;
                    System.out.printf("%d | %c    | %5d | 嵌套匹配 (位置%d)%n", 
                                     i, s.charAt(i), dp[i], i - dp[i - 1] - 1);
                }
            } else {
                System.out.printf("%d | %c    | %5d | 无匹配%n", i, s.charAt(i), dp[i]);
            }
        }
    }
    
    // 可视化栈匹配过程
    private static void visualizeStackProcess(String s) {
        System.out.println("\n栈匹配过程:");
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(-1);
        int maxLen = 0;
        
        System.out.println("i | char | 栈状态         | 当前长度 | 最大长度");
        System.out.println("--|------|---------------|---------|---------");
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(i);
                System.out.printf("%d | %c    | %-13s | %7d | %7d%n", 
                                 i, c, stackToString(stack), 0, maxLen);
            } else {
                stack.pop();
                if (stack.isEmpty()) {
                    stack.push(i);
                    System.out.printf("%d | %c    | %-13s | %7d | %7d%n", 
                                     i, c, stackToString(stack), 0, maxLen);
                } else {
                    int len = i - stack.peek();
                    maxLen = Math.max(maxLen, len);
                    System.out.printf("%d | %c    | %-13s | %7d | %7d%n", 
                                     i, c, stackToString(stack), len, maxLen);
                }
            }
        }
    }
    
    // 栈转字符串
    private static String stackToString(Deque<Integer> stack) {
        List<Integer> list = new ArrayList<>(stack);
        Collections.reverse(list);
        return list.toString();
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param s 测试字符串
     */
    public static void comparePerformance(String s) {
        long start, end;
        
        // 动态规划
        start = System.nanoTime();
        int dpResult = longestValidParenthesesDP(s);
        end = System.nanoTime();
        long dpTime = end - start;
        
        // 栈解法
        start = System.nanoTime();
        int stackResult = longestValidParenthesesStack(s);
        end = System.nanoTime();
        long stackTime = end - start;
        
        // 双指针法
        start = System.nanoTime();
        int twoPointersResult = longestValidParenthesesTwoPointers(s);
        end = System.nanoTime();
        long twoPointersTime = end - start;
        
        System.out.println("\n性能比较 (字符串长度: " + s.length() + "):");
        System.out.println("===============================================");
        System.out.println("方法            | 结果 | 时间(ns) | 相对速度");
        System.out.println("----------------|------|----------|----------");
        System.out.printf("动态规划        | %4d | %8d | 基准%n", dpResult, dpTime);
        System.out.printf("栈解法          | %4d | %8d | %.2fx%n", stackResult, stackTime, (double)dpTime/stackTime);
        System.out.printf("双指针法        | %4d | %8d | %.2fx%n", twoPointersResult, twoPointersTime, (double)dpTime/twoPointersTime);
        System.out.println("===============================================");
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
        testCase("(()", 2);
        testCase(")()())", 4);
        testCase("()(())", 6);
        testCase("()(()", 2);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("", 0);
        testCase("(", 0);
        testCase(")", 0);
        testCase("()", 2);
        testCase("((((()))))", 10);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 生成长测试字符串
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            sb.append(random.nextBoolean() ? '(' : ')');
        }
        String longStr = sb.toString();
        
        comparePerformance(longStr);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 代码语法检查
        String codeSnippet = "if (x > 0) { System.out.println(\"Positive\"); } else { System.out.println(\"Negative\"); }";
        System.out.println("代码语法检查结果: " + codeSyntaxChecker(codeSnippet));
        
        // 场景2: 配置文件验证
        String config = "((config (setting value)) (params (a b c)))";
        System.out.println("配置文件验证: " + validateConfigFile(config));
        
        // 场景3: 数学表达式解析
        String mathExpr = "((a+b)*(c-d))/(e+f)";
        System.out.println("数学表达式有效性: " + mathExpressionParser(mathExpr) + "%");
        
        // 场景4: 数据格式校验
        String data = "((()))(())";
        System.out.println("数据格式校验: " + validateDataFormat(data));
    }
    
    private static void testCase(String s, int expected) {
        int dp = longestValidParenthesesDP(s);
        int stack = longestValidParenthesesStack(s);
        int twoPointers = longestValidParenthesesTwoPointers(s);
        
        System.out.printf("\n测试: \"%s\"%n", s);
        System.out.println("动态规划结果: " + dp);
        System.out.println("栈解法结果: " + stack);
        System.out.println("双指针法结果: " + twoPointers);
        
        if (dp != expected || stack != expected || twoPointers != expected) {
            System.err.println("错误: 结果不符合预期!");
        }
        
        // 可视化过程
        if (s.length() <= 20) {
            visualizeParenthesesMatching(s);
        }
    }
}