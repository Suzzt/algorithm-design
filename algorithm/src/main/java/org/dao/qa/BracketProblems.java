package main.java.org.dao.qa;

import java.util.*;

/**
 * 括号匹配问题综合解决方案 (支持JDK 1.8)
 * 
 * <p><b>本类包含的经典括号问题：</b>
 * 1. 有效的括号 (LeetCode 20)
 * 2. 平衡括号字符串的最小插入次数 (LeetCode 1541)
 * 3. 括号生成 (LeetCode 22)
 * 4. 最长有效括号 (LeetCode 32)
 * 5. 括号的分数 (LeetCode 856)
 * 6. 检查括号字符串的有效性（通配符版）(LeetCode 678)
 * 7. 有效括号的嵌套深度 (LeetCode 1111)
 * 
 * <p><b>使用说明：</b>
 * 1. 每个问题都有独立的解法方法
 * 2. 每个方法都有详细的解题思路注释
 * 3. 包含测试用例和输出验证
 * 4. 使用JDK 1.8特性
 */
public class BracketProblems {

    // ============================= 问题1: 有效的括号 =============================
    /**
     * 问题描述：给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效
     * 
     * 解题思路：使用栈数据结构进行括号匹配
     * 1. 遍历字符串中的每个字符
     * 2. 遇到左括号时压入栈中
     * 3. 遇到右括号时，检查栈顶的左括号是否匹配
     * 4. 最后检查栈是否为空
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static boolean isValidParentheses(String s) {
        Deque<Character> stack = new ArrayDeque<>();  // 使用双端队列实现栈
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);  // 左括号入栈
            } else {
                if (stack.isEmpty()) return false;  // 缺少匹配的左括号
                char top = stack.pop();
                // 检查括号类型是否匹配
                if ((c == ')' && top != '(') || 
                    (c == ']' && top != '[') || 
                    (c == '}' && top != '{')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();  // 检查是否所有括号都匹配完成
    }
    
    // =================== 问题2: 平衡括号字符串的最小插入次数 =====================
    /**
     * 问题描述：计算插入字符使括号平衡的最小次数
     * 
     * 解题思路：遍历字符串维护左括号数量
     * 1. 遇到 '('：左括号计数+1
     * 2. 遇到 ')'：检查是否连续两个右括号
     *    a. 若是则正常处理
     *    b. 若只有1个，需要插入1个右括号
     * 3. 最终处理剩余的左括号
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int minInsertionsToBalance(String s) {
        int insertions = 0;  // 需要插入的总次数
        int leftCount = 0;   // 当前左括号数量
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c == '(') {
                leftCount++;
                i++;
            } else {
                // 检查是否连续两个右括号
                if (i + 1 < s.length() && s.charAt(i + 1) == ')') {
                    i += 2;  // 跳过两个连续右括号
                } else {
                    insertions++;  // 插入一个右括号
                    i++;
                }
                
                if (leftCount > 0) {
                    leftCount--;  // 消耗一个左括号
                } else {
                    insertions++;  // 缺少左括号，插入一个左括号
                }
            }
        }
        insertions += 2 * leftCount;  // 每个剩余左括号需要两个右括号匹配
        return insertions;
    }
    
    // ============================== 问题3: 括号生成 =============================
    /**
     * 问题描述：生成所有可能的并且有效的括号组合
     * 
     * 解题思路：回溯算法
     * 1. 维护两个计数器：当前已使用的左括号数量和右括号数量
     * 2. 添加左括号条件：左括号数量小于n
     * 3. 添加右括号条件：右括号数量小于左括号数量
     * 4. 达到目标长度时保存结果
     * 
     * 时间复杂度：O(4^n/√n) - 卡特兰数复杂度
     * 空间复杂度：O(n)
     */
    public static List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, new StringBuilder(), 0, 0, n);
        return result;
    }
    
    private static void backtrack(List<String> result, StringBuilder sb, 
                                 int open, int close, int max) {
        if (sb.length() == 2 * max) {
            result.add(sb.toString());
            return;
        }
        
        // 添加左括号
        if (open < max) {
            sb.append('(');
            backtrack(result, sb, open + 1, close, max);
            sb.deleteCharAt(sb.length() - 1);  // 回溯
        }
        
        // 添加右括号
        if (close < open) {
            sb.append(')');
            backtrack(result, sb, open, close + 1, max);
            sb.deleteCharAt(sb.length() - 1);  // 回溯
        }
    }
    
    // ============================= 问题4: 最长有效括号 ===========================
    /**
     * 问题描述：找出最长有效括号子串的长度
     * 
     * 解题思路：动态规划
     * 1. dp[i] 表示以位置i结尾的最长有效括号长度
     * 2. 处理两种模式：
     *    a. 简单模式：以"()"结束
     *    b. 嵌套模式：以"))"结束，但包含嵌套的有效括号
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static int longestValidParentheses(String s) {
        int n = s.length();
        int[] dp = new int[n];  // dp数组
        int maxLen = 0;
        
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                // 模式1："()"
                if (s.charAt(i-1) == '(') {
                    dp[i] = (i >= 2 ? dp[i-2] : 0) + 2;
                } 
                // 模式2："))"
                else if (i - dp[i-1] > 0 && s.charAt(i - dp[i-1] - 1) == '(') {
                    dp[i] = dp[i-1] + 2;
                    if (i - dp[i-1] - 2 >= 0) {
                        dp[i] += dp[i - dp[i-1] - 2];
                    }
                }
                maxLen = Math.max(maxLen, dp[i]);
            }
        }
        return maxLen;
    }
    
    // ============================= 问题5: 括号的分数 =============================
    /**
     * 问题描述：计算平衡括号字符串的分数
     * 
     * 解题思路：使用栈
     * 1. 遇到左括号压入0
     * 2. 遇到右括号：
     *    a. 栈顶为0：弹出0压入1
     *    b. 栈顶为数字：累加数字直到遇到0，然后弹出0压入2倍累加值
     * 3. 最后求和栈中所有数字
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static int scoreOfParentheses(String s) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);  // 初始分数为0
        
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(0);  // 新括号组的开始
            } else {
                int top = stack.pop();
                if (top == 0) {
                    // 遇到"()"，加分1
                    stack.push(stack.pop() + 1);
                } else {
                    // 内部有括号组，加分=2×内部分数
                    int innerScore = top;
                    while (stack.peek() != 0) {
                        innerScore += stack.pop();
                    }
                    stack.pop();  // 弹出0
                    stack.push(stack.pop() + 2 * innerScore);
                }
            }
        }
        return stack.peek();
    }
    
    // ============ 问题6: 检查括号字符串的有效性（通配符版） ================
    /**
     * 问题描述：给定包含 '('、')' 和 '*' 的字符串，'*' 可以被视为 '('、')' 或空字符
     * 
     * 解题思路：贪心算法
     * 1. 维护两个变量：当前可能的最小开放括号数和最大开放括号数
     * 2. 遍历字符串时更新：
     *    '('：min++, max++
     *    ')'：min--, max--
     *    '*'：min--, max++
     * 3. 规则：max不能为负，最后min必须为0
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static boolean checkValidWithWildcard(String s) {
        int minOpen = 0;  // 最小可能开放括号数
        int maxOpen = 0;  // 最大可能开放括号数
        
        for (char c : s.toCharArray()) {
            if (c == '(') {
                minOpen++;
                maxOpen++;
            } else if (c == ')') {
                minOpen = Math.max(minOpen - 1, 0);
                maxOpen--;
                if (maxOpen < 0) return false;  // 无效字符串
            } else {  // '*' 通配符
                minOpen = Math.max(minOpen - 1, 0);
                maxOpen++;
            }
        }
        return minOpen == 0;  // 所有开放括号都可以关闭
    }
    
    // =================== 问题7: 有效括号的嵌套深度 ======================
    /**
     * 问题描述：将有效括号字符串拆分为两个新字符串，使最大嵌套深度最小化
     * 
     * 解题思路：
     * 1. 计算字符串的最大深度
     * 2. 深度超过最大值一半的括号分配给不同组
     * 3. 结果数组：0表示分配给第一组，1表示分配给第二组
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n) 存储结果
     */
    public static int[] maxDepthAfterSplit(String seq) {
        int depth = 0, maxDepth = 0;
        for (char c : seq.toCharArray()) {
            depth += (c == '(') ? 1 : -1;
            maxDepth = Math.max(maxDepth, depth);
        }
        
        int[] result = new int[seq.length()];
        depth = 0;
        int half = maxDepth / 2;  // 深度分配阈值
        
        for (int i = 0; i < seq.length(); i++) {
            if (seq.charAt(i) == '(') {
                depth++;
                result[i] = (depth > half) ? 1 : 0;  // 过深部分分配1
            } else {
                result[i] = (depth > half) ? 1 : 0;
                depth--;
            }
        }
        return result;
    }
    
    // ============================= 测试方法 =============================
    
    public static void main(String[] args) {
        testProblem1();  // 测试有效的括号
        testProblem2();  // 测试平衡括号的最小插入次数
        testProblem3();  // 测试括号生成
        testProblem4();  // 测试最长有效括号
        testProblem5();  // 测试括号的分数
        testProblem6();  // 测试通配符匹配
        testProblem7();  // 测试嵌套深度
    }
    
    private static void testProblem1() {
        System.out.println("====== 问题1: 有效的括号测试 ======");
        String[] testCases = {"()", "()[]{}", "(]", "([)]", "{[]}"};
        boolean[] expected = {true, true, false, false, true};
        
        for (int i = 0; i < testCases.length; i++) {
            boolean result = isValidParentheses(testCases[i]);
            System.out.println(testCases[i] + " → " + result + 
                             " 状态: " + (result == expected[i] ? "✅" : "❌"));
        }
        System.out.println();
    }
    
    private static void testProblem2() {
        System.out.println("====== 问题2: 平衡括号的最小插入次数 ======");
        String[] testCases = {"(()))", "())", "))", "(()()", "(((((("};
        int[] expected = {1, 0, 3, 1, 12};
        
        for (int i = 0; i < testCases.length; i++) {
            int result = minInsertionsToBalance(testCases[i]);
            System.out.println(testCases[i] + " → " + result + 
                             " 状态: " + (result == expected[i] ? "✅" : "❌"));
        }
        System.out.println();
    }
    
    private static void testProblem3() {
        System.out.println("====== 问题3: 括号生成测试 (n=3) ======");
        List<String> results = generateParenthesis(3);
        System.out.println("生成结果: " + results);
        System.out.println("数量: " + results.size() + " 状态: " + 
                         (results.size() == 5 ? "✅" : "❌"));
        System.out.println();
    }
    
    private static void testProblem4() {
        System.out.println("====== 问题4: 最长有效括号测试 ======");
        String[] testCases = {"(()", ")()())", "()(()", "()(())", ""};
        int[] expected = {2, 4, 2, 6, 0};
        
        for (int i = 0; i < testCases.length; i++) {
            int result = longestValidParentheses(testCases[i]);
            System.out.println(testCases[i] + " → " + result + 
                             " 状态: " + (result == expected[i] ? "✅" : "❌"));
        }
        System.out.println();
    }
    
    private static void testProblem5() {
        System.out.println("====== 问题5: 括号的分数测试 ======");
        String[] testCases = {"()", "()()", "(())", "(()(()))", "()(())"};
        int[] expected = {1, 2, 2, 6, 3};
        
        for (int i = 0; i < testCases.length; i++) {
            int result = scoreOfParentheses(testCases[i]);
            System.out.println(testCases[i] + " → " + result + 
                             " 状态: " + (result == expected[i] ? "✅" : "❌"));
        }
        System.out.println();
    }
    
    private static void testProblem6() {
        System.out.println("====== 问题6: 通配符括号有效性测试 ======");
        String[] testCases = {"()", "(*)", "(*))", ")*(", "(((***"};
        boolean[] expected = {true, true, true, false, true};
        
        for (int i = 0; i < testCases.length; i++) {
            boolean result = checkValidWithWildcard(testCases[i]);
            System.out.println(testCases[i] + " → " + result + 
                             " 状态: " + (result == expected[i] ? "✅" : "❌"));
        }
        System.out.println();
    }
    
    private static void testProblem7() {
        System.out.println("====== 问题7: 有效括号的嵌套深度测试 ======");
        String testCase = "(()())";
        int[] result = maxDepthAfterSplit(testCase);
        System.out.println("输入: " + testCase);
        System.out.print("结果: [");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + (i < result.length - 1 ? ", " : ""));
        }
        System.out.println("]");
        
        // 验证结果：总和应为半开半闭
        int sum = Arrays.stream(result).sum();
        System.out.println("分配状态: " + (sum == 3 ? "✅" : "❌"));
    }
}