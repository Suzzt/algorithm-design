package main.java.org.dao.qa;

import java.util.*;

/**
 * 有效括号高级版问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个包含括号字符的字符串，判断字符串是否有效。有效字符串需满足：
 * 1. 左括号必须用相同类型的右括号闭合
 * 2. 左括号必须以正确的顺序闭合
 * 3. 支持多种括号类型：()、[]、{}、<>
 * 
 * <p><b>示例</b>:
 * 输入: s = "()[]{}"
 * 输出: true
 * 
 * 输入: s = "([)]"
 * 输出: false
 * 
 * 输入: s = "{[]}"
 * 输出: true
 * 
 * <p><b>问题难度</b>: 🔥 简单 (LeetCode 第20题扩展版)
 * 
 * <p><b>解题思路</b>:
 * 1. 栈解法: 使用栈存储左括号，遇到右括号时匹配栈顶元素
 * 2. 计数器解法: 对于单一括号类型，使用计数器即可
 * 3. 递归解法: 递归处理嵌套结构
 * 4. 状态机解法: 使用有限状态机处理复杂括号规则
 * 
 * <p><b>时间复杂度</b>:
 *  栈解法: O(n)
 *  计数器解法: O(n) 
 *  递归解法: O(n)
 *  状态机解法: O(n)
 * 
 * <p><b>空间复杂度</b>:
 *  栈解法: O(n) 最坏情况
 *  计数器解法: O(1)
 *  递归解法: O(n) 递归栈
 *  状态机解法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 编译器语法分析
 * 2. 数学表达式求值
 * 3. HTML/XML标签验证
 * 4. 代码编辑器括号匹配
 * 5. 配置文件格式检查
 */

public class ValidParenthesesAdvanced {
    
    // ========================= 解法1: 栈解法 (推荐) =========================
    
    /**
     * 使用栈判断括号是否有效
     * 
     * @param s 输入字符串
     * @return 是否为有效括号字符串
     */
    public static boolean isValidStack(String s) {
        if (s == null || s.length() % 2 != 0) {
            return false;
        }
        
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> pairs = new HashMap<>();
        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');
        pairs.put('>', '<');
        
        for (char c : s.toCharArray()) {
            if (pairs.containsValue(c)) {
                // 左括号，入栈
                stack.push(c);
            } else if (pairs.containsKey(c)) {
                // 右括号，检查匹配
                if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                    return false;
                }
            }
            // 其他字符忽略
        }
        
        return stack.isEmpty();
    }
    
    /**
     * 优化的栈解法 - 直接使用字符判断
     * 
     * @param s 输入字符串
     * @return 是否为有效括号字符串
     */
    public static boolean isValidStackOptimized(String s) {
        if (s == null || s.length() % 2 != 0) {
            return false;
        }
        
        Stack<Character> stack = new Stack<>();
        
        for (char c : s.toCharArray()) {
            switch (c) {
                case '(':
                case '[':
                case '{':
                case '<':
                    stack.push(c);
                    break;
                case ')':
                    if (stack.isEmpty() || stack.pop() != '(') return false;
                    break;
                case ']':
                    if (stack.isEmpty() || stack.pop() != '[') return false;
                    break;
                case '}':
                    if (stack.isEmpty() || stack.pop() != '{') return false;
                    break;
                case '>':
                    if (stack.isEmpty() || stack.pop() != '<') return false;
                    break;
                default:
                    // 忽略其他字符
                    break;
            }
        }
        
        return stack.isEmpty();
    }
    
    // ========================= 解法2: 计数器解法 =========================
    
    /**
     * 计数器解法 - 仅适用于单一类型括号
     * 
     * @param s 输入字符串
     * @param leftChar 左括号字符
     * @param rightChar 右括号字符
     * @return 是否为有效括号字符串
     */
    public static boolean isValidCounter(String s, char leftChar, char rightChar) {
        if (s == null) {
            return false;
        }
        
        int count = 0;
        
        for (char c : s.toCharArray()) {
            if (c == leftChar) {
                count++;
            } else if (c == rightChar) {
                count--;
                if (count < 0) {
                    return false; // 右括号过多
                }
            }
        }
        
        return count == 0;
    }
    
    /**
     * 多类型括号计数器解法
     * 
     * @param s 输入字符串
     * @return 是否为有效括号字符串
     */
    public static boolean isValidMultiCounter(String s) {
        if (s == null || s.length() % 2 != 0) {
            return false;
        }
        
        int round = 0, square = 0, curly = 0, angle = 0;
        
        for (char c : s.toCharArray()) {
            switch (c) {
                case '(': round++; break;
                case ')': 
                    round--; 
                    if (round < 0) return false;
                    break;
                case '[': square++; break;
                case ']': 
                    square--; 
                    if (square < 0) return false;
                    break;
                case '{': curly++; break;
                case '}': 
                    curly--; 
                    if (curly < 0) return false;
                    break;
                case '<': angle++; break;
                case '>': 
                    angle--; 
                    if (angle < 0) return false;
                    break;
            }
        }
        
        return round == 0 && square == 0 && curly == 0 && angle == 0;
    }
    
    // ========================= 解法3: 递归解法 =========================
    
    /**
     * 递归解法
     * 
     * @param s 输入字符串
     * @return 是否为有效括号字符串
     */
    public static boolean isValidRecursive(String s) {
        if (s == null) {
            return false;
        }
        
        return isValidRecursiveHelper(s.toCharArray(), 0, new int[]{0});
    }
    
    private static boolean isValidRecursiveHelper(char[] chars, int index, int[] pos) {
        if (pos[0] >= chars.length) {
            return true;
        }
        
        char c = chars[pos[0]];
        pos[0]++;
        
        if (isLeftBracket(c)) {
            // 找到对应的右括号
            char matchingRight = getMatchingRight(c);
            return isValidRecursiveHelper(chars, index + 1, pos) && 
                   pos[0] <= chars.length && 
                   chars[pos[0] - 1] == matchingRight;
        } else if (isRightBracket(c)) {
            return true; // 由上层递归处理
        } else {
            return isValidRecursiveHelper(chars, index + 1, pos);
        }
    }
    
    private static boolean isLeftBracket(char c) {
        return c == '(' || c == '[' || c == '{' || c == '<';
    }
    
    private static boolean isRightBracket(char c) {
        return c == ')' || c == ']' || c == '}' || c == '>';
    }
    
    private static char getMatchingRight(char left) {
        switch (left) {
            case '(': return ')';
            case '[': return ']';
            case '{': return '}';
            case '<': return '>';
            default: return '\0';
        }
    }
    
    // ========================= 解法4: 状态机解法 =========================
    
    /**
     * 状态机解法
     */
    public static class BracketStateMachine {
        private Stack<Character> stack;
        private boolean isValid;
        
        public BracketStateMachine() {
            this.stack = new Stack<>();
            this.isValid = true;
        }
        
        public void processChar(char c) {
            if (!isValid) return;
            
            switch (c) {
                case '(':
                case '[':
                case '{':
                case '<':
                    stack.push(c);
                    break;
                case ')':
                case ']':
                case '}':
                case '>':
                    if (stack.isEmpty() || !isMatching(stack.pop(), c)) {
                        isValid = false;
                    }
                    break;
                default:
                    // 忽略其他字符
                    break;
            }
        }
        
        private boolean isMatching(char left, char right) {
            return (left == '(' && right == ')') ||
                   (left == '[' && right == ']') ||
                   (left == '{' && right == '}') ||
                   (left == '<' && right == '>');
        }
        
        public boolean isValid() {
            return isValid && stack.isEmpty();
        }
        
        public void reset() {
            stack.clear();
            isValid = true;
        }
    }
    
    public static boolean isValidStateMachine(String s) {
        if (s == null) {
            return false;
        }
        
        BracketStateMachine machine = new BracketStateMachine();
        
        for (char c : s.toCharArray()) {
            machine.processChar(c);
        }
        
        return machine.isValid();
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 表达式求值中的括号验证
     * 
     * @param expression 数学表达式
     * @return 括号是否有效
     */
    public static boolean validateMathExpression(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }
        
        Stack<Character> stack = new Stack<>();
        
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
        }
        
        return stack.isEmpty();
    }
    
    /**
     * HTML标签验证
     * 
     * @param html HTML字符串
     * @return 是否为有效HTML
     */
    public static boolean validateHTMLTags(String html) {
        if (html == null) {
            return false;
        }
        
        Stack<String> tagStack = new Stack<>();
        int i = 0;
        
        while (i < html.length()) {
            if (html.charAt(i) == '<') {
                int j = i + 1;
                while (j < html.length() && html.charAt(j) != '>') {
                    j++;
                }
                
                if (j >= html.length()) {
                    return false; // 未闭合的标签
                }
                
                String tag = html.substring(i + 1, j);
                
                if (tag.startsWith("/")) {
                    // 结束标签
                    String tagName = tag.substring(1);
                    if (tagStack.isEmpty() || !tagStack.pop().equals(tagName)) {
                        return false;
                    }
                } else if (!tag.endsWith("/")) {
                    // 开始标签（非自闭合）
                    String tagName = tag.split("\\s")[0]; // 去除属性
                    tagStack.push(tagName);
                }
                
                i = j + 1;
            } else {
                i++;
            }
        }
        
        return tagStack.isEmpty();
    }
    
    /**
     * 代码括号匹配检查器
     */
    public static class CodeBracketChecker {
        private Map<Character, Character> brackets;
        private Stack<BracketInfo> bracketStack;
        
        private static class BracketInfo {
            char bracket;
            int line;
            int column;
            
            BracketInfo(char bracket, int line, int column) {
                this.bracket = bracket;
                this.line = line;
                this.column = column;
            }
        }
        
        public CodeBracketChecker() {
            brackets = new HashMap<>();
            brackets.put(')', '(');
            brackets.put(']', '[');
            brackets.put('}', '{');
            
            bracketStack = new Stack<>();
        }
        
        public List<String> checkCode(String[] codeLines) {
            List<String> errors = new ArrayList<>();
            bracketStack.clear();
            
            for (int line = 0; line < codeLines.length; line++) {
                String codeLine = codeLines[line];
                
                for (int col = 0; col < codeLine.length(); col++) {
                    char c = codeLine.charAt(col);
                    
                    if (brackets.containsValue(c)) {
                        // 左括号
                        bracketStack.push(new BracketInfo(c, line + 1, col + 1));
                    } else if (brackets.containsKey(c)) {
                        // 右括号
                        if (bracketStack.isEmpty()) {
                            errors.add(String.format("第%d行第%d列: 多余的右括号 '%c'", 
                                                    line + 1, col + 1, c));
                        } else {
                            BracketInfo left = bracketStack.pop();
                            if (left.bracket != brackets.get(c)) {
                                errors.add(String.format("第%d行第%d列: 括号不匹配，期望 '%c'，实际 '%c'", 
                                                        line + 1, col + 1, 
                                                        getMatchingRight(left.bracket), c));
                            }
                        }
                    }
                }
            }
            
            // 检查未闭合的左括号
            while (!bracketStack.isEmpty()) {
                BracketInfo left = bracketStack.pop();
                errors.add(String.format("第%d行第%d列: 未闭合的左括号 '%c'", 
                                        left.line, left.column, left.bracket));
            }
            
            return errors;
        }
    }
    
    /**
     * 配置文件格式检查
     * 
     * @param configContent 配置文件内容
     * @return 检查结果
     */
    public static Map<String, Object> validateConfigFormat(String configContent) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        if (configContent == null || configContent.trim().isEmpty()) {
            errors.add("配置文件内容为空");
            result.put("valid", false);
            result.put("errors", errors);
            return result;
        }
        
        // 检查JSON格式括号
        boolean jsonBracketsValid = isValidStack(configContent);
        if (!jsonBracketsValid) {
            errors.add("JSON格式括号不匹配");
        }
        
        // 检查括号数量统计
        Map<Character, Integer> bracketCount = new HashMap<>();
        for (char c : configContent.toCharArray()) {
            if ("()[]{}".indexOf(c) != -1) {
                bracketCount.put(c, bracketCount.getOrDefault(c, 0) + 1);
            }
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        result.put("bracketCount", bracketCount);
        result.put("jsonBracketsValid", jsonBracketsValid);
        
        return result;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化栈处理过程
     * 
     * @param s 输入字符串
     */
    public static void visualizeStackProcess(String s) {
        if (s == null) {
            System.out.println("输入字符串为空");
            return;
        }
        
        System.out.println("\n栈处理过程可视化:");
        System.out.println("输入字符串: \"" + s + "\"");
        
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> pairs = new HashMap<>();
        pairs.put(')', '(');
        pairs.put(']', '[');
        pairs.put('}', '{');
        pairs.put('>', '<');
        
        System.out.println("\n步骤 | 当前字符 | 操作 | 栈状态 | 结果");
        System.out.println("-----|----------|------|--------|------");
        
        boolean isValid = true;
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            String operation;
            String stackState;
            String stepResult = "继续";
            
            if (pairs.containsValue(c)) {
                // 左括号
                stack.push(c);
                operation = "入栈";
            } else if (pairs.containsKey(c)) {
                // 右括号
                if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                    operation = "出栈失败";
                    stepResult = "无效";
                    isValid = false;
                } else {
                    operation = "出栈匹配";
                }
            } else {
                operation = "忽略";
            }
            
            stackState = stack.toString().replaceAll("[\\[\\],]", "").trim();
            if (stackState.isEmpty()) {
                stackState = "空";
            }
            
            System.out.printf("%4d | %8c | %4s | %6s | %4s\n", 
                             i + 1, c, operation, stackState, stepResult);
            
            if (!isValid) {
                break;
            }
        }
        
        boolean finalResult = isValid && stack.isEmpty();
        System.out.println("\n最终结果: " + (finalResult ? "有效" : "无效"));
        
        if (!stack.isEmpty()) {
            System.out.println("未匹配的左括号: " + stack);
        }
    }
    
    /**
     * 可视化计数器处理过程
     * 
     * @param s 输入字符串
     */
    public static void visualizeCounterProcess(String s) {
        if (s == null) {
            System.out.println("输入字符串为空");
            return;
        }
        
        System.out.println("\n计数器处理过程可视化:");
        System.out.println("输入字符串: \"" + s + "\"");
        
        int round = 0, square = 0, curly = 0, angle = 0;
        
        System.out.println("\n步骤 | 字符 | () | [] | {} | <> | 状态");
        System.out.println("-----|------|----|----|----|----|------");
        
        boolean isValid = true;
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            String status = "正常";
            
            switch (c) {
                case '(': round++; break;
                case ')': 
                    round--; 
                    if (round < 0) {
                        status = "错误";
                        isValid = false;
                    }
                    break;
                case '[': square++; break;
                case ']': 
                    square--; 
                    if (square < 0) {
                        status = "错误";
                        isValid = false;
                    }
                    break;
                case '{': curly++; break;
                case '}': 
                    curly--; 
                    if (curly < 0) {
                        status = "错误";
                        isValid = false;
                    }
                    break;
                case '<': angle++; break;
                case '>': 
                    angle--; 
                    if (angle < 0) {
                        status = "错误";
                        isValid = false;
                    }
                    break;
                default:
                    status = "忽略";
                    break;
            }
            
            System.out.printf("%4d | %4c | %2d | %2d | %2d | %2d | %4s\n", 
                             i + 1, c, round, square, curly, angle, status);
            
            if (!isValid) {
                break;
            }
        }
        
        boolean finalResult = isValid && round == 0 && square == 0 && curly == 0 && angle == 0;
        System.out.println("\n最终结果: " + (finalResult ? "有效" : "无效"));
        
        if (round != 0 || square != 0 || curly != 0 || angle != 0) {
            System.out.printf("未匹配计数: () = %d, [] = %d, {} = %d, <> = %d\n", 
                             round, square, curly, angle);
        }
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param testString 测试字符串
     */
    public static void comparePerformance(String testString) {
        System.out.println("\n性能比较:");
        System.out.println("测试字符串长度: " + testString.length());
        System.out.println("===============================================");
        
        long start, end;
        
        // 测试栈解法
        start = System.nanoTime();
        boolean result1 = isValidStack(testString);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试优化栈解法
        start = System.nanoTime();
        boolean result2 = isValidStackOptimized(testString);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试多计数器解法
        start = System.nanoTime();
        boolean result3 = isValidMultiCounter(testString);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试状态机解法
        start = System.nanoTime();
        boolean result4 = isValidStateMachine(testString);
        end = System.nanoTime();
        long time4 = end - start;
        
        System.out.println("方法            | 时间(ms)   | 结果 | 相对速度");
        System.out.println("----------------|------------|------|----------");
        System.out.printf("栈解法          | %.6f | %4s | 基准\n", time1 / 1_000_000.0, result1);
        System.out.printf("优化栈解法      | %.6f | %4s | %.2fx\n", time2 / 1_000_000.0, result2, (double)time2/time1);
        System.out.printf("多计数器解法    | %.6f | %4s | %.2fx\n", time3 / 1_000_000.0, result3, (double)time3/time1);
        System.out.printf("状态机解法      | %.6f | %4s | %.2fx\n", time4 / 1_000_000.0, result4, (double)time4/time1);
        System.out.println("===============================================");
        
        // 验证结果一致性
        boolean consistent = (result1 == result2) && (result2 == result3) && (result3 == result4);
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
    }
    
    // 生成测试字符串
    private static String generateTestString(int length, boolean valid) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        
        char[] leftBrackets = {'(', '[', '{', '<'};
        char[] rightBrackets = {')', ']', '}', '>'};
        
        if (valid) {
            Stack<Character> stack = new Stack<>();
            
            for (int i = 0; i < length; i++) {
                if (stack.isEmpty() || rand.nextBoolean()) {
                    // 添加左括号
                    char left = leftBrackets[rand.nextInt(leftBrackets.length)];
                    sb.append(left);
                    stack.push(left);
                } else {
                    // 添加匹配的右括号
                    char left = stack.pop();
                    char right = getMatchingRight(left);
                    sb.append(right);
                }
            }
            
            // 闭合剩余的左括号
            while (!stack.isEmpty()) {
                char left = stack.pop();
                sb.append(getMatchingRight(left));
            }
        } else {
            // 生成无效字符串
            for (int i = 0; i < length; i++) {
                if (rand.nextBoolean()) {
                    sb.append(leftBrackets[rand.nextInt(leftBrackets.length)]);
                } else {
                    sb.append(rightBrackets[rand.nextInt(rightBrackets.length)]);
                }
            }
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
        testCase("示例测试1", "()", true);
        testCase("示例测试2", "()[]{}", true);
        testCase("示例测试3", "(]", false);
        testCase("示例测试4", "([)]", false);
        testCase("示例测试5", "{[]}", true);
        testCase("包含尖括号", "<{[()]}>", true);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("空字符串", "", true);
        testCase("单左括号", "(", false);
        testCase("单右括号", ")", false);
        testCase("奇数长度", "(()", false);
        testCase("全左括号", "((((", false);
        testCase("全右括号", "))))", false);
        testCase("包含其他字符", "a(b)c[d]e{f}g", true);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 数学表达式验证
        System.out.println("\n数学表达式验证:");
        String[] mathExpressions = {
            "3 + (4 * 5)",
            "((1 + 2) * 3",
            "(a + b) * (c - d)",
            "sin(cos(x))"
        };
        
        for (String expr : mathExpressions) {
            boolean valid = validateMathExpression(expr);
            System.out.printf("表达式: \"%s\" -> %s\n", expr, valid ? "有效" : "无效");
        }
        
        // 场景2: HTML标签验证
        System.out.println("\nHTML标签验证:");
        String[] htmlStrings = {
            "<div><p>Hello</p></div>",
            "<div><p>Hello</div></p>",
            "<img src='test.jpg'/>",
            "<div><span>Test</span>"
        };
        
        for (String html : htmlStrings) {
            boolean valid = validateHTMLTags(html);
            System.out.printf("HTML: \"%s\" -> %s\n", html, valid ? "有效" : "无效");
        }
        
        // 场景3: 代码括号检查
        System.out.println("\n代码括号检查:");
        String[] codeLines = {
            "public void test() {",
            "    if (condition) {",
            "        array[index] = value;",
            "    // 缺少右括号",
            "}"
        };
        
        CodeBracketChecker checker = new CodeBracketChecker();
        List<String> errors = checker.checkCode(codeLines);
        
        System.out.println("代码片段:");
        for (int i = 0; i < codeLines.length; i++) {
            System.out.printf("%2d: %s\n", i + 1, codeLines[i]);
        }
        
        if (errors.isEmpty()) {
            System.out.println("括号匹配正确");
        } else {
            System.out.println("发现错误:");
            for (String error : errors) {
                System.out.println("  " + error);
            }
        }
        
        // 场景4: 配置文件验证
        System.out.println("\n配置文件验证:");
        String configContent = "{\"name\": \"test\", \"values\": [1, 2, 3]}";
        Map<String, Object> configResult = validateConfigFormat(configContent);
        System.out.println("配置内容: " + configContent);
        System.out.println("验证结果: " + configResult);
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        visualizeStackProcess("([{}])");
        visualizeCounterProcess("([{}])");
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 不同长度和复杂度的测试
        String[] testCases = {
            generateTestString(50, true),     // 短字符串，有效
            generateTestString(50, false),    // 短字符串，无效
            generateTestString(500, true),    // 中等字符串，有效
            generateTestString(500, false),   // 中等字符串，无效
            generateTestString(5000, true),   // 长字符串，有效
        };
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n测试用例 " + (i + 1) + ":");
            comparePerformance(testCases[i]);
        }
    }
    
    private static void testCase(String name, String s, boolean expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("输入: \"" + s + "\"");
        
        boolean result1 = isValidStack(s);
        boolean result2 = isValidStackOptimized(s);
        boolean result3 = isValidMultiCounter(s);
        boolean result4 = isValidStateMachine(s);
        
        System.out.println("栈解法结果: " + result1);
        System.out.println("优化栈解法结果: " + result2);
        System.out.println("多计数器解法结果: " + result3);
        System.out.println("状态机解法结果: " + result4);
        System.out.println("期望结果: " + expected);
        
        boolean isCorrect = (result1 == expected) && (result2 == expected) && 
                           (result3 == expected) && (result4 == expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小规模字符串展示可视化
        if (s.length() <= 20 && s.length() > 0) {
            visualizeStackProcess(s);
        }
    }
}