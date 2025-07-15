package main.java.org.dao.qa;

/**
 * 最长回文子串解决方案
 * 
 * <p><b>问题描述：</b>
 * 给定一个字符串s，找出s中最长的回文子串。回文串是正序和倒序都相同的字符串。
 * 
 * <p><b>示例：</b>
 * 输入: "babad"
 * 输出: "bab" 或 "aba"
 */
public class LongestPalindromicSubstring {

    // ========================== 核心解法 ==========================
    
    /**
     * 扩展中心法 (时间复杂度O(n²)，空间复杂度O(1))
     * 
     * @param s 输入字符串
     * @return 最长回文子串
     */
    public static String longestPalindromeExpand(String s) {
        if (s == null || s.length() < 1) return "";
        
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            // 处理奇数长度回文 (aba型)
            int len1 = expandAroundCenter(s, i, i);
            // 处理偶数长度回文 (abba型)
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substring(start, end + 1);
    }
    
    private static int expandAroundCenter(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }
    
    /**
     * 动态规划法 (时间复杂度O(n²)，空间复杂度O(n²))
     * 
     * @param s 输入字符串
     * @return 最长回文子串
     */
    public static String longestPalindromeDP(String s) {
        if (s == null || s.length() < 2) return s;
        
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int maxLength = 1;
        int start = 0;
        
        // 所有单个字符都是回文
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }
        
        // 检查长度为2的回文
        for (int i = 0; i < n - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                dp[i][i + 1] = true;
                start = i;
                maxLength = 2;
            }
        }
        
        // 检查长度大于2的回文
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1; // 结束位置
                
                // 当前子串是回文的条件: 首尾字符相同且内部子串是回文
                if (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    if (len > maxLength) {
                        start = i;
                        maxLength = len;
                    }
                }
            }
        }
        
        return s.substring(start, start + maxLength);
    }
    
    // ========================== Manacher算法 (优化实现) ==========================
    
    /**
     * Manacher算法 (时间复杂度O(n)，空间复杂度O(n))
     * 
     * @param s 输入字符串
     * @return 最长回文子串
     */
    public static String longestPalindromeManacher(String s) {
        if (s == null || s.length() < 1) return "";
        
        // 预处理字符串，添加分隔符
        String T = preprocess(s);
        int n = T.length();
        int[] P = new int[n]; // 回文半径数组
        int center = 0, right = 0; // 当前回文中心和右边界
        
        for (int i = 1; i < n - 1; i++) {
            // i关于center的对称点
            int mirror = 2 * center - i;
            
            // 利用已知信息初始化P[i]
            if (i < right) {
                P[i] = Math.min(right - i, P[mirror]);
            }
            
            // 尝试扩展回文半径
            while (T.charAt(i + P[i] + 1) == T.charAt(i - P[i] - 1)) {
                P[i]++;
            }
            
            // 更新中心和右边界
            if (i + P[i] > right) {
                center = i;
                right = i + P[i];
            }
        }
        
        // 找出最长回文子串
        int maxCenter = 0;
        for (int i = 1; i < n - 1; i++) {
            if (P[i] > P[maxCenter]) {
                maxCenter = i;
            }
        }
        
        // 移除分隔符，返回原字符串中的回文子串
        int start = (maxCenter - P[maxCenter]) / 2;
        return s.substring(start, start + P[maxCenter]);
    }
    
    // 字符串预处理：添加特殊字符分隔符
    private static String preprocess(String s) {
        if (s.length() == 0) return "^$";
        StringBuilder sb = new StringBuilder("^");
        for (int i = 0; i < s.length(); i++) {
            sb.append("#").append(s.charAt(i));
        }
        sb.append("#$");
        return sb.toString();
    }
    
    // ========================== 应用场景扩展 ==========================
    
    /**
     * 基因序列回文分析 (检测回文片段)
     * 
     * @param gene 基因序列
     * @return 最长回文片段及其位置
     */
    public static String genePalindromeAnalysis(String gene) {
        String palindrome = longestPalindromeExpand(gene);
        int start = gene.indexOf(palindrome);
        int end = start + palindrome.length() - 1;
        return String.format("基因位置: %d-%d, 长度: %d, 片段: %s", 
                            start, end, palindrome.length(), palindrome);
    }
    
    /**
     * DNA回文结构检测器
     * 
     * @param dna DNA序列 (只包含A,T,C,G)
     * @return 最长互补回文序列
     */
    public static String dnaComplementaryPalindrome(String dna) {
        // 转换到互补序列表示
        StringBuilder complementary = new StringBuilder();
        for (char c : dna.toCharArray()) {
            switch (c) {
                case 'A': complementary.append('T'); break;
                case 'T': complementary.append('A'); break;
                case 'C': complementary.append('G'); break;
                case 'G': complementary.append('C'); break;
            }
        }
        String compStr = complementary.toString();
        
        // 在原始DNA和其互补序列中查找回文
        String palindrome1 = longestPalindromeDP(dna);
        String palindrome2 = longestPalindromeDP(compStr);
        
        return palindrome1.length() > palindrome2.length() ? 
               palindrome1 : palindrome2;
    }
    
    /**
     * 代码安全性扫描 (检测潜在恶意代码模式)
     * 
     * @param code 源代码字符串
     * @param minLength 最小回文长度阈值
     * @return 检测到的可疑代码片段
     */
    public static String securityCodeScanner(String code, int minLength) {
        String result = "未检测到可疑回文片段";
        
        // 查找长回文片段
        String pal = longestPalindromeManacher(code);
        if (pal.length() >= minLength) {
            int start = code.indexOf(pal);
            result = String.format("检测到可疑回文代码(长度 %d): 位置 %d\n...%s...", 
                                  pal.length(), start, 
                                  pal.substring(0, Math.min(20, pal.length())));
        }
        
        return result;
    }
    
    /**
     * 网络钓鱼检测器 (检查相似回文域名)
     * 
     * @param domain 域名
     * @return 潜在钓鱼域名报告
     */
    public static String phishingDomainDetector(String domain) {
        // 移除顶级域名后缀
        String domainCore = domain.split("\\.")[0];
        String palindrome = longestPalindromeExpand(domainCore);
        
        // 回文部分在域名核心中占比超过50%认为可疑
        double ratio = (double) palindrome.length() / domainCore.length();
        if (ratio > 0.5 && palindrome.length() >= 5) {
            return String.format("潜在钓鱼域名: %s (回文占比 %.2f%%, 片段: %s)", 
                                domain, ratio * 100, palindrome);
        }
        return domain + " 安全";
    }
    
    // ========================== 性能测试方法 ==========================
    
    public static void main(String[] args) {
        testAlgorithms();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testAlgorithms() {
        System.out.println("===== 算法正确性测试 =====");
        
        String[] testCases = {
            "babad", // 预期 "bab" 或 "aba"
            "cbbd",  // 预期 "bb"
            "a",     // 预期 "a"
            "ac",    // 预期 "a" 或 "c"
            "racecar", // 完整回文
            "forgeeksskeegfor" // 长回文
        };
        
        for (String s : testCases) {
            String dp = longestPalindromeDP(s);
            String expand = longestPalindromeExpand(s);
            String manacher = longestPalindromeManacher(s);
            
            System.out.printf("输入: %-15s | DP: %-10s | 扩展中心: %-10s | Manacher: %-10s\n",
                             "\"" + s + "\"", "\"" + dp + "\"", 
                             "\"" + expand + "\"", "\"" + manacher + "\"");
        }
    }
    
    private static void testPerformance() {
        System.out.println("\n===== 性能测试 (单位: 微秒) =====");
        
        // 生成长测试字符串
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append((char)('a' + Math.random() * 26));
        }
        String longString = sb.toString();
        
        // 测试DP算法
        long start = System.nanoTime();
        longestPalindromeDP(longString);
        long timeDP = (System.nanoTime() - start) / 1000;
        
        // 测试扩展中心法
        start = System.nanoTime();
        longestPalindromeExpand(longString);
        long timeExpand = (System.nanoTime() - start) / 1000;
        
        // 测试Manacher算法
        start = System.nanoTime();
        longestPalindromeManacher(longString);
        long timeManacher = (System.nanoTime() - start) / 1000;
        
        System.out.printf("动态规划: %d μs\n扩展中心法: %d μs\nManacher算法: %d μs\n", 
                        timeDP, timeExpand, timeManacher);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n===== 应用场景测试 =====");
        
        // 场景1: 基因序列回文分析
        System.out.println("\n1. 基因序列回文分析:");
        String gene = "ACGTATACGCGCTATACGT"; // 包含"TATACGTAT"回文
        System.out.println(genePalindromeAnalysis(gene));
        
        // 场景2: DNA回文结构检测
        System.out.println("\n2. DNA互补回文检测:");
        String dna = "ATCGATTAATCG"; // "ATTA"在互补序列中是"TAAT"
        System.out.println("检测结果: " + dnaComplementaryPalindrome(dna));
        
        // 场景3: 代码安全性扫描
        System.out.println("\n3. 恶意代码检测:");
        String safeCode = "public void testMethod() {}";
        String maliciousCode = "x='suspiciousssuoispus'; doSomethingBad();";
        System.out.println("安全代码扫描: " + securityCodeScanner(safeCode, 5));
        System.out.println("可疑代码扫描: " + securityCodeScanner(maliciousCode, 5));
        
        // 场景4: 网络钓鱼检测
        System.out.println("\n4. 钓鱼域名检测:");
        String legitDomain = "google.com";
        String phishingDomain = "paypal-login-security.com";
        System.out.println("合法域名: " + phishingDomainDetector(legitDomain));
        System.out.println("可疑域名: " + phishingDomainDetector(phishingDomain));
    }
}