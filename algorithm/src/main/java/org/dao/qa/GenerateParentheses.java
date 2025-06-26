package main.java.org.dao.qa;

import java.util.*;

/**
 * 括号生成问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个整数n，生成所有由n对括号组成的有效组合。
 * 
 * <p><b>示例</b>:
 * 输入: n = 3
 * 输出: 
 * [
 *   "((()))",
 *   "(()())",
 *   "(())()",
 *   "()(())",
 *   "()()()"
 * ]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第22题)
 * 
 * <p><b>解题思路</b>:
 * 1. 回溯法: 通过递归构建可能的括号组合，使用开闭括号计数确保有效性
 * 2. 动态规划: 基于较小n的结果构建较大n的结果
 * 3. BFS/DFS: 广度优先或深度优先搜索所有可能组合
 * 
 * <p><b>时间复杂度</b>:
 *  所有方法: O(4^n/√n) - 卡特兰数复杂度
 * 
 * <p><b>应用场景</b>:
 * 1. 编译器语法分析
 * 2. 代码自动补全
 * 3. 数学表达式生成器
 * 4. DNA序列的碱基配对
 * 5. 游戏关卡的合法路径生成
 */

public class GenerateParentheses {

    // ========================= 解法1: 回溯法 =========================
    
    /**
     * 回溯法生成括号组合
     * 
     * @param n 括号对数
     * @return 所有有效的括号组合
     */
    public static List<String> generateBacktrack(int n) {
        List<String> result = new ArrayList<>();
        backtrack(result, "", 0, 0, n);
        return result;
    }
    
    private static void backtrack(List<String> result, String current, 
                                 int open, int close, int max) {
        // 当前字符串长度等于最大括号数
        if (current.length() == max * 2) {
            result.add(current);
            return;
        }
        
        // 可以添加开括号的条件
        if (open < max) {
            backtrack(result, current + "(", open + 1, close, max);
        }
        
        // 可以添加闭括号的条件
        if (close < open) {
            backtrack(result, current + ")", open, close + 1, max);
        }
    }
    
    // ========================= 解法2: 动态规划 =========================
    
    /**
     * 动态规划生成括号组合
     * 
     * @param n 括号对数
     * @return 所有有效的括号组合
     */
    public static List<String> generateDP(int n) {
        List<List<String>> dp = new ArrayList<>();
        
        // 初始化基础情况
        dp.add(new ArrayList<>(Collections.singletonList("")));
        
        for (int i = 1; i <= n; i++) {
            List<String> current = new ArrayList<>();
            
            for (int j = 0; j < i; j++) {
                // 所有内部可能组合
                for (String inside : dp.get(j)) {
                    // 所有尾部组合
                    for (String tail : dp.get(i - 1 - j)) {
                        // 组合成新的有效括号
                        current.add("(" + inside + ")" + tail);
                    }
                }
            }
            
            dp.add(current);
        }
        
        return dp.get(n);
    }
    
    // ========================= 解法3: BFS解法 =========================
    
    /**
     * BFS方法生成括号组合
     * 
     * @param n 括号对数
     * @return 所有有效的括号组合
     */
    public static List<String> generateBFS(int n) {
        class Node {
            String str;
            int open;
            int close;
            
            Node(String str, int open, int close) {
                this.str = str;
                this.open = open;
                this.close = close;
            }
        }
        
        List<String> result = new ArrayList<>();
        Queue<Node> queue = new LinkedList<>();
        queue.offer(new Node("", 0, 0));
        
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            
            // 完成状态
            if (node.open == n && node.close == n) {
                result.add(node.str);
                continue;
            }
            
            // 添加开括号
            if (node.open < n) {
                queue.offer(new Node(node.str + "(", node.open + 1, node.close));
            }
            
            // 添加闭括号
            if (node.close < node.open) {
                queue.offer(new Node(node.str + ")", node.open, node.close + 1));
            }
        }
        
        return result;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化括号生成过程
     * 
     * @param n 括号对数
     */
    public static void visualizeGeneration(int n) {
        System.out.println("\n括号生成可视化 (n=" + n + ")");
        System.out.println("决策树 (开括号: (, 闭括号: ))");
        
        visualizeBacktrack("", 0, 0, n, 0, new StringBuilder());
    }
    
    private static void visualizeBacktrack(String current, int open, int close, 
                                         int max, int depth, StringBuilder path) {
        // 缩进表示递归深度
        for (int i = 0; i < depth; i++) {
            System.out.print("|   ");
        }
        
        // 显示当前状态
        System.out.printf("L%d: \"%s\" (开=%d, 闭=%d) %s\n", 
                         depth, current, open, close, path.toString());
        
        // 达到最大长度
        if (current.length() == max * 2) {
            System.out.print(generateIndent(depth));
            System.out.println("√ 完成: \"" + current + "\"");
            return;
        }
        
        // 复制当前路径用于回溯
        int pathLen = path.length();
        
        // 尝试添加开括号
        if (open < max) {
            path.append(" → 加(");
            visualizeBacktrack(current + "(", open + 1, close, max, depth + 1, path);
            path.setLength(pathLen);
        }
        
        // 尝试添加闭括号
        if (close < open) {
            path.append(" → 加)");
            visualizeBacktrack(current + ")", open, close + 1, max, depth + 1, path);
            path.setLength(pathLen);
        }
    }
    
    // 生成缩进字符串
    private static String generateIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("|   ");
        }
        return sb.toString();
    }
    
    // ========================= 应用场景 =========================
    
    /**
     * 编程语言自动补全
     * 
     * @param partial 部分输入的括号字符串
     * @param maxPairs 最大括号对
     * @return 可能的补全方案
     */
    public static List<String> autoCompleteBrackets(String partial, int maxPairs) {
        // 统计已有括号数量
        int openCount = 0;
        int closeCount = 0;
        
        for (char c : partial.toCharArray()) {
            if (c == '(') openCount++;
            else if (c == ')') closeCount++;
        }
        
        // 补全可能的括号组合
        List<String> completions = new ArrayList<>();
        completeBacktrack(completions, partial, openCount, closeCount, maxPairs);
        return completions;
    }
    
    private static void completeBacktrack(List<String> result, String current, 
                                        int open, int close, int max) {
        // 有效结束条件
        if (current.length() >= max * 2 || (open == close && open == max)) {
            if (isValid(current)) {
                result.add(current);
            }
            return;
        }
        
        // 补全开括号
        if (open < max) {
            completeBacktrack(result, current + "(", open + 1, close, max);
        }
        
        // 补全闭括号
        if (close < open) {
            completeBacktrack(result, current + ")", open, close + 1, max);
        }
        
        // 补全缺失的闭括号
        if (close < open && current.length() < max * 2) {
            completeBacktrack(result, current + ")", open, close + 1, max);
        }
    }
    
    // 验证括号字符串有效性
    private static boolean isValid(String s) {
        int balance = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') balance++;
            else if (c == ')') balance--;
            
            if (balance < 0) return false;
        }
        return balance == 0;
    }
    
    /**
     * DNA碱基配对生成器
     * 
     * @param n 碱基对数量
     * @return 所有可能的碱基序列组合
     */
    public static List<String> generateDNAPairs(int n) {
        // 括号映射为DNA碱基: ( -> A(腺嘌呤), ) -> T(胸腺嘧啶)
        List<String> parenthesis = generateBacktrack(n);
        List<String> dnaSequences = new ArrayList<>();
        
        for (String seq : parenthesis) {
            // 随机替换为互补碱基对
            Random rand = new Random();
            StringBuilder sb = new StringBuilder();
            int atCount = 0;
            
            for (char c : seq.toCharArray()) {
                if (c == '(') {
                    sb.append('A');
                } else {
                    // 随机替换为T或U(尿嘧啶)
                    if (rand.nextDouble() < 0.3) {
                        sb.append('U');
                    } else {
                        sb.append('T');
                    }
                }
            }
            
            // 加入随机突变
            char[] dna = sb.toString().toCharArray();
            if (rand.nextDouble() < 0.2) {
                int idx = rand.nextInt(dna.length);
                if (dna[idx] == 'A') dna[idx] = 'G';
                else if (dna[idx] == 'T') dna[idx] = 'C';
            }
            
            dnaSequences.add(new String(dna));
        }
        
        return dnaSequences;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testComplexCases();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        
        // 测试 n=0
        System.out.println("n=0: " + generateBacktrack(0));
        
        // 测试 n=1
        System.out.println("n=1: " + generateBacktrack(1));
        
        // 测试 n=2
        List<String> n2 = Arrays.asList("(())", "()()");
        testForN(2, n2);
        
        // 测试 n=3
        List<String> n3 = Arrays.asList(
            "((()))", 
            "(()())", 
            "(())()", 
            "()(())", 
            "()()()"
        );
        testForN(3, n3);
    }
    
    private static void testForN(int n, List<String> expected) {
        System.out.println("\n测试 n=" + n);
        
        List<String> backtrack = generateBacktrack(n);
        List<String> dp = generateDP(n);
        List<String> bfs = generateBFS(n);
        
        System.out.println("回溯法结果: " + backtrack);
        System.out.println("动态规划结果: " + dp);
        System.out.println("BFS结果: " + bfs);
        
        boolean backtrackValid = new HashSet<>(backtrack).equals(new HashSet<>(expected));
        boolean dpValid = new HashSet<>(dp).equals(new HashSet<>(expected));
        boolean bfsValid = new HashSet<>(bfs).equals(new HashSet<>(expected));
        
        System.out.println("回溯法: " + (backtrackValid ? "✅" : "❌"));
        System.out.println("动态规划: " + (dpValid ? "✅" : "❌"));
        System.out.println("BFS: " + (bfsValid ? "✅" : "❌"));
        
        if (n <= 3) {
            visualizeGeneration(n);
        }
    }
    
    private static void testComplexCases() {
        System.out.println("\n====== 复杂测试 ======");
        
        int n = 4;
        System.out.println("n=" + n + " 结果数量: " + generateBacktrack(n).size());
        System.out.println("理论数量 (卡特兰数): " + catalanNumber(n));
        
        n = 5;
        System.out.println("n=" + n + " 结果数量: " + generateBacktrack(n).size());
        System.out.println("理论数量 (卡特兰数): " + catalanNumber(n));
    }
    
    // 计算卡特兰数
    private static int catalanNumber(int n) {
        long numerator = 1;
        long denominator = 1;
        
        for (int k = 2; k <= n; k++) {
            numerator *= (n + k);
            denominator *= k;
        }
        
        return (int)(numerator / denominator);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        int n = 12; // 测试较大n值
        
        long start, end;
        
        start = System.currentTimeMillis();
        List<String> result1 = generateBacktrack(n);
        end = System.currentTimeMillis();
        System.out.printf("回溯法: %d 个结果, 耗时: %d ms\n", result1.size(), end - start);
        
        start = System.currentTimeMillis();
        List<String> result2 = generateDP(n);
        end = System.currentTimeMillis();
        System.out.printf("动态规划: %d 个结果, 耗时: %d ms\n", result2.size(), end - start);
        
        start = System.currentTimeMillis();
        List<String> result3 = generateBFS(n);
        end = System.currentTimeMillis();
        System.out.printf("BFS: %d 个结果, 耗时: %d ms\n", result3.size(), end - start);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 测试括号自动补全
        System.out.println("自动补全测试: 输入 \"(()\"");
        List<String> completions = autoCompleteBrackets("(()", 3);
        System.out.println("补全方案: " + completions);
        
        // 测试DNA碱基生成
        System.out.println("\nDNA碱基对生成 (n=3):");
        List<String> dnaSequences = generateDNAPairs(3);
        Random rand = new Random();
        for (int i = 0; i < Math.min(5, dnaSequences.size()); i++) {
            String dna = dnaSequences.get(rand.nextInt(dnaSequences.size()));
            System.out.printf("DNA序列 %d: %s (长度: %d)\n", i+1, dna, dna.length());
        }
        
        // 输出完整的DNA结构
        System.out.println("\n完整的DNA双链结构:");
        printDNADoubleHelix("AATTCCGG");
    }
    
    private static void printDNADoubleHelix(String sequence) {
        if (sequence == null || sequence.isEmpty()) return;
        
        Map<Character, Character> complements = new HashMap<>();
        complements.put('A', 'T');
        complements.put('T', 'A');
        complements.put('C', 'G');
        complements.put('G', 'C');
        complements.put('U', 'A');
        
        // 生成互补序列
        StringBuilder complement = new StringBuilder();
        for (char c : sequence.toCharArray()) {
            complement.append(complements.getOrDefault(c, c));
        }
        
        // 打印双螺旋结构
        System.out.println("5'-" + sequence + "-3'");
        System.out.println("    " + generateBonds(sequence, complement.toString()));
        System.out.println("3'-" + complement.reverse().toString() + "-5'");
    }
    
    private static String generateBonds(String s1, String s2) {
        StringBuilder bonds = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            if ((s1.charAt(i) == 'A' && s2.charAt(i) == 'T') ||
                (s1.charAt(i) == 'T' && s2.charAt(i) == 'A')) {
                bonds.append("=");
            } else if ((s1.charAt(i) == 'C' && s2.charAt(i) == 'G') ||
                      (s1.charAt(i) == 'G' && s2.charAt(i) == 'C')) {
                bonds.append("≡");
            } else if (s1.charAt(i) == 'U' && s2.charAt(i) == 'A') {
                bonds.append("-");
            } else {
                bonds.append("?");
            }
        }
        return bonds.toString();
    }
}