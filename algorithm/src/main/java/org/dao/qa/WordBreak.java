package main.java.org.dao.qa;

import java.util.*;

/**
 * Word Break（单词拆分）问题
 * 
 * <p><b>问题描述：</b>
 * 给定一个非空字符串s和一个包含非空单词列表的字典wordDict，
 * 判断s是否可以拆分成一个或多个字典中的单词。同一个单词可以被重复使用多次。
 * 
 * <p><b>示例：</b>
 * 输入: s = "leetcode", wordDict = ["leet", "code"]
 * 输出: true
 * 解释: "leetcode" 可以拆分为 "leet" 和 "code"
 * 
 * <p><b>解题思路：</b>
 * 使用动态规划(dp[i]表示s的前i个字符是否可拆分)和字典树优化查找效率
 */
public class WordBreak {
    
    // ========================== 字典树节点 ==========================
    private static class TrieNode {
        boolean isWordEnd;
        TrieNode[] children;
        
        public TrieNode() {
            isWordEnd = false;
            children = new TrieNode[26];
        }
    }
    
    // ========================== 核心解法 ==========================
    
    /**
     * 动态规划解法 (时间复杂度O(n²))
     * 
     * @param s 目标字符串
     * @param wordDict 单词字典
     * @return 是否可拆分
     */
    public static boolean wordBreakDP(String s, List<String> wordDict) {
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;  // 空字符串初始为true
        
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordDict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;  // 找到一种拆分方式即可
                }
            }
        }
        return dp[n];
    }
    
    /**
     * 基于字典树的优化解法 (时间复杂度O(n²)但平均更优)
     * 
     * @param s 目标字符串
     * @param wordDict 单词字典
     * @return 是否可拆分
     */
    public static boolean wordBreakTrie(String s, List<String> wordDict) {
        TrieNode root = buildTrie(wordDict);
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        
        for (int i = 0; i < n; i++) {
            if (!dp[i]) continue;  // 只有当前位置可拆分才继续
            
            TrieNode node = root;
            int j = i;
            while (j < n && node.children[s.charAt(j) - 'a'] != null) {
                node = node.children[s.charAt(j) - 'a'];
                j++;
                if (node.isWordEnd) {
                    dp[j] = true;
                }
            }
        }
        return dp[n];
    }
    
    // 构建字典树
    private static TrieNode buildTrie(List<String> wordDict) {
        TrieNode root = new TrieNode();
        for (String word : wordDict) {
            insertWord(root, word);
        }
        return root;
    }
    
    // 向字典树插入单词
    private static void insertWord(TrieNode root, String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isWordEnd = true;
    }
    
    // ========================== 应用场景扩展 ==========================
    
    /**
     * 获取所有可能的拆分方案
     * 
     * @param s 目标字符串
     * @param wordDict 单词字典
     * @return 所有可能的单词拆分序列
     */
    public static List<List<String>> findAllBreakSolutions(String s, List<String> wordDict) {
        Map<Integer, List<List<String>>> memo = new HashMap<>();
        return dfs(s, wordDict, 0, memo);
    }
    
    private static List<List<String>> dfs(String s, List<String> wordDict, 
                                         int start, Map<Integer, List<List<String>>> memo) {
        if (memo.containsKey(start)) {
            return memo.get(start);
        }
        
        List<List<String>> solutions = new ArrayList<>();
        if (start == s.length()) {
            solutions.add(new ArrayList<>());  // 添加空列表表示结束
            return solutions;
        }
        
        for (int end = start + 1; end <= s.length(); end++) {
            String word = s.substring(start, end);
            if (wordDict.contains(word)) {
                List<List<String>> nextSolutions = dfs(s, wordDict, end, memo);
                for (List<String> solution : nextSolutions) {
                    List<String> newSolution = new ArrayList<>(solution);
                    newSolution.add(0, word);
                    solutions.add(newSolution);
                }
            }
        }
        
        memo.put(start, solutions);
        return solutions;
    }
    
    /**
     * 编译器代码格式化 (自动添加空格)
     * 
     * @param code 未分隔的代码字符串
     * @param keywords 编程语言关键字列表
     * @return 格式化后的代码
     */
    public static String codeFormatter(String code, List<String> keywords) {
        TrieNode root = buildTrie(keywords);
        int n = code.length();
        boolean[] dp = new boolean[n + 1];
        int[] backPtr = new int[n + 1];  // 回溯路径
        
        Arrays.fill(backPtr, -1);
        dp[0] = true;
        
        // 构建动态规划表
        for (int i = 0; i < n; i++) {
            if (!dp[i]) continue;
            
            TrieNode node = root;
            int j = i;
            while (j < n && node.children[code.charAt(j) - 'a'] != null) {
                node = node.children[code.charAt(j) - 'a'];
                j++;
                if (node.isWordEnd) {
                    dp[j] = true;
                    backPtr[j] = i;  // 记录断点位置
                }
            }
        }
        
        // 回溯添加空格
        if (!dp[n]) return code;  // 无法格式化返回原代码
        
        StringBuilder formatted = new StringBuilder();
        int i = n;
        while (i > 0) {
            int start = backPtr[i];
            formatted.insert(0, " " + code.substring(start, i));
            i = start;
        }
        return formatted.toString().trim();
    }
    
    /**
     * 文本分词系统 (基于最大匹配算法)
     * 
     * @param text 待分词文本
     * @param dictionary 词典
     * @return 分词结果
     */
    public static List<String> textSegmentation(String text, List<String> dictionary) {
        List<List<String>> allSolutions = findAllBreakSolutions(text, dictionary);
        if (allSolutions.isEmpty()) return Collections.singletonList(text);
        
        // 选择使用最多单词的方案
        return allSolutions.stream()
            .min(Comparator.comparingInt(List::size))
            .orElse(Collections.singletonList(text));
    }
    
    // ========================== 测试方法 ==========================
    
    public static void main(String[] args) {
        testAlgorithms();
        testApplicationScenarios();
    }
    
    private static void testAlgorithms() {
        System.out.println("===== 算法测试 =====");
        String s = "catsanddog";
        List<String> wordDict = Arrays.asList("cat", "cats", "and", "sand", "dog");
        
        System.out.println("测试用例: " + s);
        System.out.println("词典: " + wordDict);
        
        boolean dpResult = wordBreakDP(s, wordDict);
        boolean trieResult = wordBreakTrie(s, wordDict);
        
        System.out.printf("动态规划结果: %b\n", dpResult);
        System.out.printf("字典树优化结果: %b\n", trieResult);
        System.out.println("所有拆分方案:");
        findAllBreakSolutions(s, wordDict).forEach(System.out::println);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n===== 应用场景测试 =====");
        
        // 场景1: 编译器代码格式化
        String code = "publicstaticvoidmainstringargs";
        List<String> javaKeywords = Arrays.asList(
            "public", "static", "void", "main", "string", "args"
        );
        
        System.out.println("代码格式化测试:");
        System.out.println("原始代码: " + code);
        String formattedCode = codeFormatter(code, javaKeywords);
        System.out.println("格式化后: " + formattedCode);
        
        // 场景2: 文本分词系统
        String text = "中国人工智能产业发展迅速";
        List<String> dict = Arrays.asList(
            "中国", "人工", "人工智能", "产业", "发展", "迅速"
        );
        
        System.out.println("\n中文分词测试:");
        System.out.println("原始文本: " + text);
        List<String> segments = textSegmentation(text, dict);
        System.out.println("分词结果: " + segments);
    }
}