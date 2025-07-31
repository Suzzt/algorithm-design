package main.java.org.dao.qa;

import java.util.*;

/**
 * 无重复字符的最长子串问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个字符串 s，请找出其中不含有重复字符的最长子串的长度。
 * 
 * <p><b>示例</b>:
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第3题)
 * 
 * <p><b>解题思路</b>:
 * 1. 滑动窗口法: 维护一个窗口，当遇到重复字符时收缩窗口，时间复杂度O(n)
 * 2. 哈希表优化法: 记录字符最后出现位置，直接跳跃，时间复杂度O(n)
 * 3. 集合判重法: 使用HashSet判断重复，暴力枚举，时间复杂度O(n²)
 * 4. 数组优化法: 对于ASCII字符集，使用数组替代HashMap，提升常数效率
 * 
 * <p><b>时间复杂度</b>:
 *  滑动窗口法: O(n)
 *  哈希表优化法: O(n)
 *  集合判重法: O(n²)
 *  数组优化法: O(n)
 * 
 * <p><b>空间复杂度</b>:
 *  滑动窗口法: O(min(m,n))，m是字符集大小
 *  哈希表优化法: O(min(m,n))
 *  集合判重法: O(min(m,n))
 *  数组优化法: O(m)
 * 
 * <p><b>应用场景</b>:
 * 1. 数据去重和窗口分析
 * 2. 网络协议中的滑动窗口
 * 3. 字符串处理和模式匹配
 * 4. 数据流处理中的唯一性检测
 * 5. 缓存系统的LRU实现
 */

public class LongestSubstringWithoutRepeatingCharacters {
    
    // ========================= 解法1: 滑动窗口法 (推荐) =========================
    
    /**
     * 滑动窗口解法 - 双指针技术
     * 
     * @param s 输入字符串
     * @return 最长无重复子串的长度
     */
    public static int lengthOfLongestSubstringSlidingWindow(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        Set<Character> window = new HashSet<>();
        int left = 0, right = 0;
        int maxLength = 0;
        
        while (right < s.length()) {
            char rightChar = s.charAt(right);
            
            // 如果窗口中存在重复字符，收缩左边界
            while (window.contains(rightChar)) {
                window.remove(s.charAt(left));
                left++;
            }
            
            // 扩展窗口
            window.add(rightChar);
            maxLength = Math.max(maxLength, right - left + 1);
            right++;
        }
        
        return maxLength;
    }
    
    // ========================= 解法2: 哈希表优化法 =========================
    
    /**
     * 哈希表优化解法 - 记录字符最后出现位置
     * 
     * @param s 输入字符串
     * @return 最长无重复子串的长度
     */
    public static int lengthOfLongestSubstringHashMap(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        Map<Character, Integer> charIndexMap = new HashMap<>();
        int left = 0;
        int maxLength = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            // 如果字符已存在且在当前窗口内，移动左边界
            if (charIndexMap.containsKey(rightChar)) {
                left = Math.max(left, charIndexMap.get(rightChar) + 1);
            }
            
            // 更新字符位置和最大长度
            charIndexMap.put(rightChar, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    // ========================= 解法3: 集合判重法 =========================
    
    /**
     * 集合判重解法 - 暴力枚举所有子串
     * 
     * @param s 输入字符串
     * @return 最长无重复子串的长度
     */
    public static int lengthOfLongestSubstringBruteForce(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int maxLength = 0;
        
        for (int i = 0; i < s.length(); i++) {
            Set<Character> seen = new HashSet<>();
            
            for (int j = i; j < s.length(); j++) {
                char c = s.charAt(j);
                
                if (seen.contains(c)) {
                    break;
                }
                
                seen.add(c);
                maxLength = Math.max(maxLength, j - i + 1);
            }
        }
        
        return maxLength;
    }
    
    // ========================= 解法4: 数组优化法 =========================
    
    /**
     * 数组优化解法 - 针对ASCII字符集
     * 
     * @param s 输入字符串
     * @return 最长无重复子串的长度
     */
    public static int lengthOfLongestSubstringArray(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // ASCII字符集有128个字符
        int[] charIndex = new int[128];
        Arrays.fill(charIndex, -1);
        
        int left = 0;
        int maxLength = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            // 如果字符已存在且在当前窗口内，移动左边界
            if (charIndex[rightChar] >= left) {
                left = charIndex[rightChar] + 1;
            }
            
            // 更新字符位置和最大长度
            charIndex[rightChar] = right;
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 获取所有最长无重复子串
     * 
     * @param s 输入字符串
     * @return 所有最长无重复子串的列表
     */
    public static List<String> getAllLongestSubstrings(String s) {
        if (s == null || s.length() == 0) {
            return new ArrayList<>();
        }
        
        List<String> result = new ArrayList<>();
        int maxLength = lengthOfLongestSubstringSlidingWindow(s);
        
        Map<Character, Integer> charIndexMap = new HashMap<>();
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            if (charIndexMap.containsKey(rightChar)) {
                left = Math.max(left, charIndexMap.get(rightChar) + 1);
            }
            
            charIndexMap.put(rightChar, right);
            
            // 如果当前子串长度等于最大长度，添加到结果中
            if (right - left + 1 == maxLength) {
                String substring = s.substring(left, right + 1);
                if (!result.contains(substring)) {
                    result.add(substring);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 数据流中的最长无重复子串 - 支持动态添加字符
     */
    public static class LongestSubstringInStream {
        private StringBuilder stream;
        private Map<Character, Integer> charIndexMap;
        private int currentMaxLength;
        private int windowStart;
        
        public LongestSubstringInStream() {
            this.stream = new StringBuilder();
            this.charIndexMap = new HashMap<>();
            this.currentMaxLength = 0;
            this.windowStart = 0;
        }
        
        public int addChar(char c) {
            int currentPos = stream.length();
            stream.append(c);
            
            // 如果字符已存在且在当前窗口内，更新窗口起始位置
            if (charIndexMap.containsKey(c) && charIndexMap.get(c) >= windowStart) {
                windowStart = charIndexMap.get(c) + 1;
            }
            
            charIndexMap.put(c, currentPos);
            currentMaxLength = Math.max(currentMaxLength, currentPos - windowStart + 1);
            
            return currentMaxLength;
        }
        
        public String getCurrentStream() {
            return stream.toString();
        }
        
        public int getCurrentMaxLength() {
            return currentMaxLength;
        }
    }
    
    /**
     * 窗口大小限制的最长无重复子串
     * 
     * @param s 输入字符串
     * @param maxWindowSize 最大窗口大小
     * @return 在窗口大小限制下的最长无重复子串长度
     */
    public static int lengthOfLongestSubstringWithLimit(String s, int maxWindowSize) {
        if (s == null || s.length() == 0 || maxWindowSize <= 0) {
            return 0;
        }
        
        Set<Character> window = new HashSet<>();
        int left = 0, right = 0;
        int maxLength = 0;
        
        while (right < s.length()) {
            char rightChar = s.charAt(right);
            
            // 如果窗口中存在重复字符或窗口大小超限，收缩左边界
            while (window.contains(rightChar) || right - left + 1 > maxWindowSize) {
                window.remove(s.charAt(left));
                left++;
            }
            
            window.add(rightChar);
            maxLength = Math.max(maxLength, right - left + 1);
            right++;
        }
        
        return maxLength;
    }
    
    /**
     * 字符频率统计的最长子串 - 允许最多k个重复字符
     * 
     * @param s 输入字符串
     * @param k 允许的最大重复字符数
     * @return 最长子串长度
     */
    public static int lengthOfLongestSubstringWithKRepeats(String s, int k) {
        if (s == null || s.length() == 0 || k < 0) {
            return 0;
        }
        
        Map<Character, Integer> charCount = new HashMap<>();
        int left = 0;
        int maxLength = 0;
        int duplicates = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            charCount.put(rightChar, charCount.getOrDefault(rightChar, 0) + 1);
            
            // 如果字符出现次数从1变为2，增加重复计数
            if (charCount.get(rightChar) == 2) {
                duplicates++;
            }
            
            // 如果重复字符数超过k，收缩窗口
            while (duplicates > k) {
                char leftChar = s.charAt(left);
                charCount.put(leftChar, charCount.get(leftChar) - 1);
                
                if (charCount.get(leftChar) == 1) {
                    duplicates--;
                }
                if (charCount.get(leftChar) == 0) {
                    charCount.remove(leftChar);
                }
                
                left++;
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化滑动窗口过程
     * 
     * @param s 输入字符串
     */
    public static void visualizeSlidingWindow(String s) {
        if (s == null || s.length() == 0) {
            System.out.println("输入字符串为空");
            return;
        }
        
        System.out.println("\n滑动窗口过程可视化:");
        System.out.println("输入字符串: \"" + s + "\"");
        System.out.println("字符索引:   " + getIndexString(s.length()));
        
        Set<Character> window = new HashSet<>();
        int left = 0, right = 0;
        int maxLength = 0;
        String maxSubstring = "";
        
        System.out.println("\n步骤 | 左指针 | 右指针 | 当前字符 | 窗口状态 | 窗口长度 | 最大长度");
        System.out.println("-----|--------|--------|----------|----------|----------|----------");
        
        int step = 1;
        while (right < s.length()) {
            char rightChar = s.charAt(right);
            
            // 如果窗口中存在重复字符，收缩左边界
            while (window.contains(rightChar)) {
                window.remove(s.charAt(left));
                left++;
            }
            
            window.add(rightChar);
            int currentLength = right - left + 1;
            
            if (currentLength > maxLength) {
                maxLength = currentLength;
                maxSubstring = s.substring(left, right + 1);
            }
            
            System.out.printf("%4d | %6d | %6d | %8c | %8s | %8d | %8d\n", 
                             step, left, right, rightChar, 
                             getWindowVisualization(s, left, right),
                             currentLength, maxLength);
            
            right++;
            step++;
        }
        
        System.out.println("\n结果:");
        System.out.println("最长无重复子串: \"" + maxSubstring + "\"");
        System.out.println("最大长度: " + maxLength);
    }
    
    private static String getIndexString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%2d", i));
        }
        return sb.toString();
    }
    
    private static String getWindowVisualization(String s, int left, int right) {
        if (right - left + 1 > 8) {
            return s.substring(left, left + 3) + ".." + s.substring(right - 1, right + 1);
        }
        return s.substring(left, right + 1);
    }
    
    /**
     * 可视化哈希表优化过程
     * 
     * @param s 输入字符串
     */
    public static void visualizeHashMapOptimization(String s) {
        if (s == null || s.length() == 0) {
            System.out.println("输入字符串为空");
            return;
        }
        
        System.out.println("\n哈希表优化过程可视化:");
        System.out.println("输入字符串: \"" + s + "\"");
        
        Map<Character, Integer> charIndexMap = new HashMap<>();
        int left = 0;
        int maxLength = 0;
        
        System.out.println("\n步骤 | 当前字符 | 字符位置 | 左指针 | 窗口长度 | 最大长度 | 哈希表状态");
        System.out.println("-----|----------|----------|--------|----------|----------|------------------");
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            int oldLeft = left;
            
            if (charIndexMap.containsKey(rightChar)) {
                left = Math.max(left, charIndexMap.get(rightChar) + 1);
            }
            
            charIndexMap.put(rightChar, right);
            int currentLength = right - left + 1;
            maxLength = Math.max(maxLength, currentLength);
            
            System.out.printf("%4d | %8c | %8d | %6d | %8d | %8d | %s\n", 
                             right + 1, rightChar, right, left,
                             currentLength, maxLength, 
                             formatHashMap(charIndexMap));
            
            if (left != oldLeft) {
                System.out.printf("%4s | %8s | %8s | %6s | %8s | %8s | 检测到重复，左指针跳跃\n", 
                                 "", "", "", "", "", "");
            }
        }
        
        System.out.println("\n最长无重复子串长度: " + maxLength);
    }
    
    private static String formatHashMap(Map<Character, Integer> map) {
        if (map.size() > 5) {
            return "{" + map.size() + " entries}";
        }
        
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append(entry.getKey()).append(":").append(entry.getValue());
            first = false;
        }
        sb.append("}");
        return sb.toString();
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
        
        // 测试滑动窗口法
        start = System.nanoTime();
        int result1 = lengthOfLongestSubstringSlidingWindow(testString);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试哈希表优化法
        start = System.nanoTime();
        int result2 = lengthOfLongestSubstringHashMap(testString);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试数组优化法
        start = System.nanoTime();
        int result3 = lengthOfLongestSubstringArray(testString);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试暴力法（仅对短字符串）
        long time4 = 0;
        int result4 = -1;
        if (testString.length() <= 1000) {
            start = System.nanoTime();
            result4 = lengthOfLongestSubstringBruteForce(testString);
            end = System.nanoTime();
            time4 = end - start;
        }
        
        System.out.println("方法            | 时间(ms)   | 结果 | 相对速度");
        System.out.println("----------------|------------|------|----------");
        System.out.printf("滑动窗口法      | %.6f | %4d | 基准\n", time1 / 1_000_000.0, result1);
        System.out.printf("哈希表优化法    | %.6f | %4d | %.2fx\n", time2 / 1_000_000.0, result2, (double)time2/time1);
        System.out.printf("数组优化法      | %.6f | %4d | %.2fx\n", time3 / 1_000_000.0, result3, (double)time3/time1);
        
        if (result4 != -1) {
            System.out.printf("暴力法          | %.6f | %4d | %.2fx\n", time4 / 1_000_000.0, result4, (double)time4/time1);
        } else {
            System.out.println("暴力法          | 跳过(字符串太长)");
        }
        
        System.out.println("===============================================");
        
        // 验证结果一致性
        boolean consistent = (result1 == result2) && (result2 == result3) && 
                           (result4 == -1 || result3 == result4);
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
    }
    
    // 生成测试字符串
    private static String generateTestString(int length, int charRange) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + rand.nextInt(charRange));
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
        testCase("示例测试1", "abcabcbb", 3);
        testCase("示例测试2", "bbbbb", 1);
        testCase("示例测试3", "pwwkew", 3);
        testCase("示例测试4", "dvdf", 3);
        testCase("示例测试5", "", 0);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("单字符", "a", 1);
        testCase("两字符相同", "aa", 1);
        testCase("两字符不同", "ab", 2);
        testCase("全字符不同", "abcdef", 6);
        testCase("特殊字符", "a!@#$%^&*()", 11);
        testCase("数字字符", "0123456789", 10);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 数据流处理
        System.out.println("\n数据流处理:");
        LongestSubstringInStream stream = new LongestSubstringInStream();
        String testData = "abcabcbb";
        for (char c : testData.toCharArray()) {
            int length = stream.addChar(c);
            System.out.printf("添加 '%c' -> 当前最长长度: %d, 当前流: \"%s\"\n", 
                             c, length, stream.getCurrentStream());
        }
        
        // 场景2: 获取所有最长子串
        System.out.println("\n所有最长无重复子串:");
        String testStr = "pwwkew";
        List<String> allLongest = getAllLongestSubstrings(testStr);
        System.out.println("字符串: \"" + testStr + "\"");
        System.out.println("所有最长子串: " + allLongest);
        
        // 场景3: 窗口大小限制
        System.out.println("\n窗口大小限制测试:");
        String limitTestStr = "abcdef";
        for (int limit = 1; limit <= 6; limit++) {
            int result = lengthOfLongestSubstringWithLimit(limitTestStr, limit);
            System.out.printf("字符串: \"%s\", 窗口限制: %d, 结果: %d\n", 
                             limitTestStr, limit, result);
        }
        
        // 场景4: 允许k个重复字符
        System.out.println("\n允许重复字符测试:");
        String repeatTestStr = "aabbcc";
        for (int k = 0; k <= 3; k++) {
            int result = lengthOfLongestSubstringWithKRepeats(repeatTestStr, k);
            System.out.printf("字符串: \"%s\", 允许重复数: %d, 结果: %d\n", 
                             repeatTestStr, k, result);
        }
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        visualizeSlidingWindow("abcabcbb");
        visualizeHashMapOptimization("pwwkew");
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 不同长度和字符集的测试
        String[] testCases = {
            generateTestString(100, 10),    // 短字符串，小字符集
            generateTestString(1000, 26),   // 中等字符串，标准字母
            generateTestString(10000, 5),   // 长字符串，小字符集
            generateTestString(10000, 26)   // 长字符串，标准字母
        };
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n测试用例 " + (i + 1) + ":");
            comparePerformance(testCases[i]);
        }
    }
    
    private static void testCase(String name, String s, int expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("输入: \"" + s + "\"");
        
        int result1 = lengthOfLongestSubstringSlidingWindow(s);
        int result2 = lengthOfLongestSubstringHashMap(s);
        int result3 = lengthOfLongestSubstringArray(s);
        int result4 = lengthOfLongestSubstringBruteForce(s);
        
        System.out.println("滑动窗口法结果: " + result1);
        System.out.println("哈希表优化法结果: " + result2);
        System.out.println("数组优化法结果: " + result3);
        System.out.println("暴力法结果: " + result4);
        System.out.println("期望结果: " + expected);
        
        boolean isCorrect = (result1 == expected) && (result2 == expected) && 
                           (result3 == expected) && (result4 == expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小规模字符串展示可视化
        if (s.length() <= 15 && s.length() > 0) {
            visualizeSlidingWindow(s);
        }
    }
}