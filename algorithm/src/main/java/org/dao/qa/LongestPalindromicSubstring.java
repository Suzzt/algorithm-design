package main.java.org.dao.qa;

import java.util.*;

/**
 * 最长回文子串问题 (Longest Palindromic Substring)
 * 
 * <p><b>问题描述</b>:
 * 给定一个字符串s，找到s中最长的回文子串。回文子串是正着读和反着读都相同的字符串。
 * 
 * <p><b>示例</b>:
 * 输入: "babad"
 * 输出: "bab" 或 "aba"
 * 
 * 输入: "cbbd"
 * 输出: "bb"
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第5题)
 * 
 * <p><b>解题思路</b>:
 * 1. 中心扩展法: 从每个字符/字符间为中心向两边扩展，寻找最长回文
 * 2. 动态规划: 使用dp[i][j]记录i到j是否为回文，利用状态转移优化
 * 3. Manacher算法: 线性时间复杂度算法，添加分隔符预处理
 * 
 * <p><b>时间复杂度</b>:
 *  中心扩展法: O(n²)
 *  动态规划: O(n²)
 *  Manacher算法: O(n)
 * 
 * <p><b>应用场景</b>:
 * 1. DNA序列分析
 * 2. 文本编辑器的拼写检查
 * 3. 网络安全中的恶意代码检测
 * 4. 游戏中的名字生成
 * 5. 自然语言处理中的词法分析
 */

public class LongestPalindromicSubstring {

    // ========================= 解法1: 中心扩展法 =========================
    
    /**
     * 中心扩展法
     * 
     * @param s 输入字符串
     * @return 最长回文子串
     */
    public static String longestPalindromeCenter(String s) {
        if (s == null || s.length() < 1) return "";
        
        int start = 0, end = 0;
        
        for (int i = 0; i < s.length(); i++) {
            // 奇数长度回文
            int len1 = expandAroundCenter(s, i, i);
            // 偶数长度回文
            int len2 = expandAroundCenter(s, i, i + 1);
            
            int len = Math.max(len1, len2);
            
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        
        return s.substring(start, end + 1);
    }
    
    // 辅助方法：向两边扩展
    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }
    
    // ========================= 解法2: 动态规划 =========================
    
    /**
     * 动态规划解法
     * 
     * @param s 输入字符串
     * @return 最长回文子串
     */
    public static String longestPalindromeDP(String s) {
        int n = s.length();
        if (n <= 1) return s;
        
        boolean[][] dp = new boolean[n][n];
        
        // 所有单个字符都是回文
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }
        
        int maxLen = 1;
        int start = 0;
        
        // 检查长度为2的子串
        for (int i = 0; i < n - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                dp[i][i + 1] = true;
                start = i;
                maxLen = 2;
            }
        }
        
        // 检查长度大于2的子串
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i < n - len + 1; i++) {
                int j = i + len - 1;
                
                // 状态转移方程
                if (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    if (len > maxLen) {
                        start = i;
                        maxLen = len;
                    }
                }
            }
        }
        
        return s.substring(start, start + maxLen);
    }
    
    // ========================= 解法3: Manacher算法 =========================
    
    /**
     * Manacher算法 - 线性时间复杂度
     * 
     * @param s 输入字符串
     * @return 最长回文子串
     */
    public static String longestPalindromeManacher(String s) {
        if (s == null || s.length() == 0) return "";
        
        // 预处理字符串，添加分隔符
        char[] t = preprocess(s);
        int n = t.length;
        
        int[] p = new int[n]; // p[i]表示以i为中心的回文半径
        int center = 0, right = 0; // 当前最右回文的中心和右边界
        
        for (int i = 1; i < n - 1; i++) {
            // i关于center的对称点
            int mirror = 2 * center - i;
            
            // 利用已知信息快速初始化
            if (i < right) {
                p[i] = Math.min(right - i, p[mirror]);
            }
            
            // 尝试扩展
            while (t[i + (1 + p[i])] == t[i - (1 + p[i])]) {
                p[i]++;
            }
            
            // 更新最右回文信息
            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }
        }
        
        // 找出最长回文
        int maxLen = 0, centerIndex = 0;
        for (int i = 1; i < n - 1; i++) {
            if (p[i] > maxLen) {
                maxLen = p[i];
                centerIndex = i;
            }
        }
        
        // 计算原始字符串中的起始位置
        int startIndex = (centerIndex - 1 - maxLen) / 2;
        return s.substring(startIndex, startIndex + maxLen);
    }
    
    // 预处理字符串：添加分隔符
    private static char[] preprocess(String s) {
        int n = s.length();
        char[] t = new char[2 * n + 3];
        t[0] = '^';
        t[2 * n + 2] = '$';
        
        for (int i = 0; i < n; i++) {
            t[2 * i + 1] = '#';
            t[2 * i + 2] = s.charAt(i);
        }
        t[2 * n + 1] = '#';
        
        return t;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化中心扩展过程
     * 
     * @param s 输入字符串
     */
    public static void visualizeCenterExpansion(String s) {
        System.out.println("\n中心扩展过程可视化: \"" + s + "\"");
        System.out.println("索引: " + buildIndexString(s));
        System.out.println("字符串: " + toSpacedString(s));
        
        int maxStart = 0, maxEnd = 0;
        
        for (int center = 0; center < s.length(); center++) {
            System.out.println("\n中心位置: " + center + " (" + s.charAt(center) + ")");
            
            // 奇数长度扩展
            System.out.println("奇数长度扩展:");
            visualizeExpansion(s, center, center);
            
            // 偶数长度扩展
            System.out.println("偶数长度扩展:");
            if (center < s.length() - 1) {
                visualizeExpansion(s, center, center + 1);
            } else {
                System.out.println("  无法扩展到右侧（到达边界）");
            }
        }
        
        System.out.println("\n最终最长回文子串: " + 
                          (maxEnd > maxStart ? 
                           s.substring(maxStart, maxEnd + 1) : "无"));
    }
    
    // 构建索引字符串
    private static String buildIndexString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append(i).append(" ");
        }
        return sb.toString();
    }
    
    // 添加空格显示字符串
    private static String toSpacedString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(c).append(" ");
        }
        return sb.toString();
    }
    
    // 可视化单次扩展过程
    private static void visualizeExpansion(String s, int left, int right) {
        System.out.print("  扩展: ");
        StringBuilder expansion = new StringBuilder();
        
        // 记录初始回文范围
        int start = left, end = right;
        
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            if (left != right) {
                expansion.insert(0, s.charAt(left) + " ");
                expansion.append(s.charAt(right)).append(" ");
            } else {
                expansion.append(s.charAt(left)).append(" ");
            }
            
            left--;
            right++;
        }
        
        // 输出扩展结果
        System.out.println(expansion.toString());
        System.out.println("  范围: [" + (left + 1) + " - " + (right - 1) + 
                         "], 长度: " + (right - left - 1));
    }
    
    // ========================= 应用场景 =========================
    
    /**
     * DNA序列分析器
     * 
     * @param dnaSequence DNA序列
     * @return 最长回文片段
     */
    public static String analyzeDNA(String dnaSequence) {
        String longestPalindrome = longestPalindromeManacher(dnaSequence);
        System.out.println("\nDNA序列分析结果:");
        System.out.println("序列长度: " + dnaSequence.length());
        System.out.println("最长回文序列: " + longestPalindrome);
        System.out.println("长度: " + longestPalindrome.length());
        visualizeDNAPalindromes(dnaSequence, longestPalindrome);
        return longestPalindrome;
    }
    
    // 可视化DNA回文序列
    private static void visualizeDNAPalindromes(String dna, String palindrome) {
        System.out.println("\nDNA序列标记:");
        StringBuilder sb = new StringBuilder();
        int start = dna.indexOf(palindrome);
        
        if (start < 0) return;
        
        for (int i = 0; i < dna.length(); i++) {
            if (i >= start && i < start + palindrome.length()) {
                sb.append("[").append(dna.charAt(i)).append("]");
            } else {
                sb.append(" ").append(dna.charAt(i)).append(" ");
            }
        }
        System.out.println(sb.toString());
    }
    
    /**
     * 游戏名称生成器
     * 
     * @param seed 种子字符串
     * @param size 要生成的名称数量
     * @return 生成的名称列表
     */
    public static List<String> generateGameNames(String seed, int size) {
        List<String> names = new ArrayList<>();
        Random rand = new Random();
        
        for (int i = 0; i < size; i++) {
            // 随机选择一个回文中心
            int center = rand.nextInt(seed.length());
            int len = Math.min(center, seed.length() - center);
            
            // 随机扩展长度
            int expand = rand.nextInt(len) + 1;
            
            // 构造回文名称
            String leftPart = seed.substring(center - expand, center);
            String rightPart = new StringBuilder(leftPart).reverse().toString();
            String name = leftPart + seed.charAt(center) + rightPart;
            
            names.add(name);
        }
        
        System.out.println("\n生成的游戏名称: " + names);
        return names;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testComplexCases();
        testPerformance();
        testApplications();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        testLongestPalindrome("babad", Arrays.asList("bab", "aba"));
        testLongestPalindrome("cbbd", Collections.singletonList("bb"));
        testLongestPalindrome("a", Collections.singletonList("a"));
        testLongestPalindrome("ac", Arrays.asList("a", "c"));
        testLongestPalindrome("racecar", Collections.singletonList("racecar"));
    }
    
    private static void testComplexCases() {
        System.out.println("\n====== 复杂测试 ======");
        testLongestPalindrome("babadada", Collections.singletonList("adada"));
        testLongestPalindrome("abacab", Collections.singletonList("bacab"));
        testLongestPalindrome("caba", Collections.singletonList("aba"));
        testLongestPalindrome("a" + new String(new char[1000]).replace('\0', 'b') + "c", 
                            Collections.singletonList(new String(new char[1000]).replace('\0', 'b')));
    }
    
    private static void testLongestPalindrome(String s, List<String> expected) {
        System.out.println("\n输入: \"" + s + "\"");
        
        String centerRes = longestPalindromeCenter(s);
        String dpRes = longestPalindromeDP(s);
        String manacherRes = longestPalindromeManacher(s);
        
        System.out.println("中心扩展法: " + centerRes);
        System.out.println("动态规划: " + dpRes);
        System.out.println("Manacher算法: " + manacherRes);
        
        boolean centerPass = expected.contains(centerRes);
        boolean dpPass = expected.contains(dpRes);
        boolean manacherPass = expected.contains(manacherRes);
        
        System.out.println("中心扩展法: " + (centerPass ? "✅" : "❌"));
        System.out.println("动态规划: " + (dpPass ? "✅" : "❌"));
        System.out.println("Manacher算法: " + (manacherPass ? "✅" : "❌"));
        
        if (s.length() < 20) {
            visualizeCenterExpansion(s);
        }
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 生成长字符串：10000个字符
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            sb.append((char)('a' + rand.nextInt(26)));
        }
        String longStr = sb.toString();
        
        // 中心扩展法
        long start = System.currentTimeMillis();
        longestPalindromeCenter(longStr);
        System.out.println("中心扩展法: " + (System.currentTimeMillis() - start) + "ms");
        
        // 动态规划
        start = System.currentTimeMillis();
        longestPalindromeDP(longStr);
        System.out.println("动态规划: " + (System.currentTimeMillis() - start) + "ms");
        
        // Manacher算法
        start = System.currentTimeMillis();
        longestPalindromeManacher(longStr);
        System.out.println("Manacher算法: " + (System.currentTimeMillis() - start) + "ms");
    }
    
    private static void testApplications() {
        System.out.println("\n====== 应用场景测试 ======");
        System.out.println("应用1: DNA序列分析");
        analyzeDNA("GAATTCAGCGTACGATCGATCGATTGCTAGCTAGT");
        
        System.out.println("\n应用2: 游戏名称生成");
        generateGameNames("eldragon", 5);
    }
}