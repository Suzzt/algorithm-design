package main.java.org.dao.qa;

import java.util.*;

/**
 * 最小覆盖子串解决方案
 * 
 * <p><b>问题描述：</b>
 * 给定一个字符串s和一个字符串t，找出s中涵盖t所有字符的最小子串。
 * 如果s中不存在涵盖t所有字符的子串，则返回空字符串。
 * 
 * <p><b>示例：</b>
 * 输入: s = "ADOBECODEBANC", t = "ABC"
 * 输出: "BANC"
 * 解释: 最小子串 "BANC" 包含字符串 t 的所有字符 'A'、'B' 和 'C'
 */
public class MinimumWindowSubstring {

    /**
     * 滑动窗口解法 - 时间复杂度O(s)
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    public static String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) {
            return "";
        }
        
        // 统计t中字符出现次数
        Map<Character, Integer> targetMap = new HashMap<>();
        for (char c : t.toCharArray()) {
            targetMap.put(c, targetMap.getOrDefault(c, 0) + 1);
        }
        
        // 窗口内字符计数
        Map<Character, Integer> windowMap = new HashMap<>();
        // 记录窗口中包含t中字符的个数（当某个字符在窗口中出现次数不小于t中该字符出现次数时，count加1）
        int count = 0;
        int left = 0, right = 0; // 窗口左右指针
        int minLen = Integer.MAX_VALUE; // 最小覆盖子串长度
        int start = 0; // 最小覆盖子串起始位置
        
        while (right < s.length()) {
            char rChar = s.charAt(right);
            right++;
            
            // 如果当前字符在t中，则更新窗口内该字符的计数
            if (targetMap.containsKey(rChar)) {
                windowMap.put(rChar, windowMap.getOrDefault(rChar, 0) + 1);
                // 如果窗口中该字符的数量等于t中该字符的数量，则count加1
                if (windowMap.get(rChar).equals(targetMap.get(rChar))) {
                    count++;
                }
            }
            
            // 当窗口中包含t中所有字符时，收缩窗口
            while (count == targetMap.size()) {
                // 更新最小覆盖子串
                if (right - left < minLen) {
                    minLen = right - left;
                    start = left;
                }
                
                char lChar = s.charAt(left);
                left++;
                
                // 如果左指针所指的字符在t中，则需要更新窗口计数
                if (targetMap.containsKey(lChar)) {
                    // 如果窗口中该字符的数量等于t中该字符的数量，则收缩窗口前count减1
                    if (windowMap.get(lChar).equals(targetMap.get(lChar))) {
                        count--;
                    }
                    windowMap.put(lChar, windowMap.get(lChar) - 1);
                }
            }
        }
        
        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    // ========================== 应用场景扩展 ==========================
    
    /**
     * 文档内容搜索高亮器
     * 
     * @param document 文档全文
     * @param keywords 关键词（连续字符串）
     * @return 包含所有关键词的最小连续片段（用于高亮显示）
     */
    public static String documentHighlighter(String document, String keywords) {
        return minWindow(document, keywords);
    }
    
    /**
     * 基因序列片段查找器
     * 
     * @param genome 基因序列
     * @param marker 目标基因片段
     * @return 包含目标基因片段的最短连续序列
     */
    public static String geneSequenceFinder(String genome, String marker) {
        return minWindow(genome, marker);
    }
    
    /**
     * 网络流量分析器（寻找包含特定特征串的最短流量片段）
     * 
     * @param traffic 流量数据串
     * @param pattern 特征模式串
     * @return 包含所有特征字符的最短流量片段
     */
    public static String trafficAnalyzer(String traffic, String pattern) {
        return minWindow(traffic, pattern);
    }
    
    /**
     * 多关键词搜索摘要生成器
     * 
     * @param text 源文本
     * @param keywords 关键词列表（以空格分隔）
     * @return 包含所有关键词的最小区间摘要
     */
    public static String generateSearchSnippet(String text, String keywords) {
        return minWindow(text, keywords.replace(" ", ""));
    }

    // ========================== 测试方法 ==========================
    
    public static void main(String[] args) {
        testAlgorithm();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testAlgorithm() {
        System.out.println("===== 算法测试 =====");
        String s = "ADOBECODEBANC";
        String t = "ABC";
        String result = minWindow(s, t);
        System.out.println("输入: s = \"" + s + "\", t = \"" + t + "\"");
        System.out.println("输出: \"" + result + "\"");
        
        // 更多测试用例
        String[][] testCases = {
            {"a", "a", "a"},
            {"a", "aa", ""},
            {"ab", "a", "a"},
            {"abc", "ac", "abc"}
        };
        
        for (String[] testCase : testCases) {
            String res = minWindow(testCase[0], testCase[1]);
            String expected = testCase[2];
            System.out.printf("输入: s='%s', t='%s' -> 输出: '%s' (%s)%n", 
                            testCase[0], testCase[1], res, 
                            res.equals(expected) ? "正确" : "错误: 预期 '" + expected + "'");
        }
    }
    
    private static void testPerformance() {
        System.out.println("\n===== 性能测试 =====");
        // 生成大型测试数据
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {  // 100万字符
            sBuilder.append((char)('a' + (int)(Math.random() * 26)));
        }
        String s = sBuilder.toString();
        String t = "abcdefghij";  // 10个字符
        
        long startTime = System.currentTimeMillis();
        String result = minWindow(s, t);
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("在100万字符中查找10字符所需时间: " + duration + "ms");
        System.out.println("找到的窗口长度: " + (result.length()));
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n===== 应用场景测试 =====");
        
        // 场景1: 文档高亮
        String document = "在一个黑暗的夜晚，有一个黑暗的身影出现在黑暗的角落。";
        String keywords = "黑暗身影";
        System.out.println("文档高亮器结果: " + documentHighlighter(document, keywords));
        
        // 场景2: 基因序列分析
        String genome = "GCATGCAGTCGATCAGTCGAGCTAGCTACGAT";
        String marker = "GCT";
        System.out.println("基因序列查找结果: " + geneSequenceFinder(genome, marker));
        
        // 场景3: 网络流量分析
        String traffic = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n...敏感数据...";
        String pattern = "敏感数据";
        System.out.println("网络流量分析结果: " + trafficAnalyzer(traffic, pattern));
        
        // 场景4: 搜索结果摘要
        String article = "人工智能是计算机科学的一个分支，旨在创造能够执行通常需要人类智能的任务的智能机器。";
        String searchTerms = "人工智能 任务";
        System.out.println("搜索摘要: " + generateSearchSnippet(article, searchTerms));
    }
}