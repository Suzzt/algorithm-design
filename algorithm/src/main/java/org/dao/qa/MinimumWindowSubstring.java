package main.java.org.dao.qa;

import java.util.*;

/**
 * 最小覆盖子串问题
 *
 * <p><b>问题描述</b>:
 * 给定一个字符串S和一个字符串T，找出S中最短的子串，使得T中所有字符都被包含（包括重复字符）。
 * 如果不存在，返回空字符串。
 *
 * <p><b>示例</b>:
 * 输入: S = "ADOBECODEBANC", T = "ABC"
 * 输出: "BANC"
 *
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第76题)
 *
 * <p><b>解题思路</b>:
 * 1. 滑动窗口法: 使用双指针维护窗口，时间复杂度O(n)
 * 2. 哈希表优化: 使用两个哈希表跟踪字符需求
 * 3. 字符计数法: 使用数组替代哈希表提高性能
 *
 * <p><b>时间复杂度</b>:
 * 所有方法: O(n)
 *
 * <p><b>空间复杂度</b>:
 * 滑动窗口法: O(1) 或 O(128) 字符集大小
 *
 * <p><b>应用场景</b>:
 * 1. 文本搜索与高亮
 * 2. DNA序列模式匹配
 * 3. 搜索引擎关键词提取
 * 4. 代码片段搜索
 * 5. 日志分析中的模式识别
 */

public class MinimumWindowSubstring {

    // ========================= 核心解法1: 滑动窗口法 =========================

    /**
     * 滑动窗口解法
     *
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    public static String minWindow(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        // 初始化需求表
        int[] need = new int[128];
        int needCount = t.length();
        for (char c : t.toCharArray()) {
            need[c]++;
        }

        int left = 0, right = 0;
        int minLen = Integer.MAX_VALUE;
        int start = 0;

        while (right < s.length()) {
            char rChar = s.charAt(right);
            // 如果当前字符是目标字符
            if (need[rChar] > 0) {
                needCount--;
            }
            need[rChar]--;
            right++;

            // 当窗口满足所有需求
            while (needCount == 0) {
                // 更新最小窗口
                if (right - left < minLen) {
                    minLen = right - left;
                    start = left;
                }

                char lChar = s.charAt(left);
                // 如果移出的是目标字符
                if (need[lChar] == 0) {
                    needCount++;
                }
                need[lChar]++;
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    // ========================= 核心解法2: 哈希表优化 =========================

    /**
     * 哈希表优化解法
     *
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    public static String minWindowHashMap(String s, String t) {
        if (s == null || t == null || s.length() < t.length()) return "";

        // 初始化需求表
        Map<Character, Integer> need = new HashMap<>();
        for (char c : t.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        int left = 0, right = 0;
        int minLen = Integer.MAX_VALUE;
        int start = 0;
        int needCount = t.length();

        while (right < s.length()) {
            char rChar = s.charAt(right);
            if (need.containsKey(rChar)) {
                if (need.get(rChar) > 0) {
                    needCount--;
                }
                need.put(rChar, need.get(rChar) - 1);
            }
            right++;

            while (needCount == 0) {
                if (right - left < minLen) {
                    minLen = right - left;
                    start = left;
                }

                char lChar = s.charAt(left);
                if (need.containsKey(lChar)) {
                    if (need.get(lChar) == 0) {
                        needCount++;
                    }
                    need.put(lChar, need.get(lChar) + 1);
                }
                left++;
            }
        }

        return minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
    }

    // ========================= 应用场景扩展 =========================

    /**
     * 文本搜索高亮器
     *
     * @param document 文档内容
     * @param keywords 关键词
     * @return 包含关键词的最短连续文本
     */
    public static String textSearchHighlighter(String document, String keywords) {
        return minWindow(document, keywords);
    }

    /**
     * DNA序列模式识别
     *
     * @param dnaSequence DNA序列
     * @param pattern     目标模式
     * @return 包含模式的最短序列
     */
    public static String dnaPatternFinder(String dnaSequence, String pattern) {
        return minWindow(dnaSequence, pattern);
    }

    /**
     * 搜索引擎关键词提取
     *
     * @param content  网页内容
     * @param keywords 关键词列表
     * @return 包含所有关键词的最短片段
     */
    public static String searchEngineSnippet(String content, List<String> keywords) {
        // 将关键词列表合并为字符串
        StringBuilder sb = new StringBuilder();
        for (String keyword : keywords) {
            sb.append(keyword);
        }
        return minWindow(content, sb.toString());
    }

    /**
     * 代码片段搜索
     *
     * @param code   源代码
     * @param tokens 关键标记
     * @return 包含所有标记的最短代码片段
     */
    public static String codeSnippetFinder(String code, Set<Character> tokens) {
        StringBuilder sb = new StringBuilder();
        for (Character token : tokens) {
            sb.append(token);
        }
        return minWindow(code, sb.toString());
    }

    /**
     * 日志模式识别器
     *
     * @param log     日志内容
     * @param pattern 模式字符
     * @return 包含模式的最短日志片段
     */
    public static String logPatternRecognizer(String log, String pattern) {
        return minWindow(log, pattern);
    }

    // ========================= 可视化工具 =========================

    /**
     * 可视化滑动窗口过程
     *
     * @param s 源字符串
     * @param t 目标字符串
     */
    public static void visualizeWindowProcess(String s, String t) {
        System.out.println("\n滑动窗口过程可视化:");
        System.out.println("源字符串: " + s);
        System.out.println("目标字符串: " + t);

        int[] need = new int[128];
        int needCount = t.length();
        for (char c : t.toCharArray()) {
            need[c]++;
        }

        int left = 0, right = 0;
        int minLen = Integer.MAX_VALUE;
        int start = 0;
        int step = 1;

        System.out.println("\n步骤 | 左指针 | 右指针 | 当前窗口 | 需求状态 | 满足需求");
        System.out.println("----|-------|-------|----------|----------|---------");

        while (right < s.length()) {
            char rChar = s.charAt(right);
            if (need[rChar] > 0) {
                needCount--;
            }
            need[rChar]--;
            right++;

            String window = s.substring(left, right);
            String needs = getNeedsStatus(need, t);
            String satisfied = needCount == 0 ? "是" : "否";

            System.out.printf("%2d  | %5d | %5d | %-8s | %-8s | %-7s%n",
                    step++, left, right - 1, window, needs, satisfied);

            while (needCount == 0) {
                if (right - left < minLen) {
                    minLen = right - left;
                    start = left;
                }

                char lChar = s.charAt(left);
                if (need[lChar] == 0) {
                    needCount++;
                }
                need[lChar]++;
                left++;

                window = s.substring(left, right);
                needs = getNeedsStatus(need, t);
                satisfied = needCount == 0 ? "是" : "否";

                System.out.printf("%2d  | %5d | %5d | %-8s | %-8s | %-7s%n",
                        step++, left, right - 1, window, needs, satisfied);
            }
        }

        String result = minLen == Integer.MAX_VALUE ? "" : s.substring(start, start + minLen);
        System.out.println("\n最小覆盖子串: " + result);
    }

    // 获取需求状态字符串
    private static String getNeedsStatus(int[] need, String t) {
        StringBuilder sb = new StringBuilder();
        for (char c : t.toCharArray()) {
            if (need[c] > 0) {
                sb.append(c).append(":").append(need[c]).append(" ");
            }
        }
        return sb.toString().trim();
    }

    /**
     * 可视化字符需求变化
     *
     * @param s 源字符串
     * @param t 目标字符串
     */
    public static void visualizeCharRequirements(String s, String t) {
        int[] need = new int[128];
        int needCount = t.length();
        for (char c : t.toCharArray()) {
            need[c]++;
        }

        int left = 0, right = 0;
        int step = 1;

        System.out.println("\n字符需求变化可视化:");
        System.out.println("步骤 | 操作 | 字符 | 需求变化 | 剩余需求");
        System.out.println("----|------|------|----------|---------");

        while (right < s.length()) {
            char rChar = s.charAt(right);
            int before = need[rChar];
            if (before > 0) {
                needCount--;
            }
            need[rChar]--;
            int after = need[rChar];

            System.out.printf("%2d  | 添加 |  %c   | %2d → %2d | %2d%n",
                    step++, rChar, before, after, needCount);
            right++;

            while (needCount == 0) {
                char lChar = s.charAt(left);
                before = need[lChar];
                if (before == 0) {
                    needCount++;
                }
                need[lChar]++;
                after = need[lChar];

                System.out.printf("%2d  | 移除 |  %c   | %2d → %2d | %2d%n",
                        step++, lChar, before, after, needCount);
                left++;
            }
        }
    }

    // ========================= 性能优化工具 =========================

    /**
     * 性能比较测试
     *
     * @param s 源字符串
     * @param t 目标字符串
     */
    public static void comparePerformance(String s, String t) {
        long start, end;

        // 数组计数法
        start = System.nanoTime();
        String result1 = minWindow(s, t);
        end = System.nanoTime();
        long time1 = end - start;

        // 哈希表法
        start = System.nanoTime();
        String result2 = minWindowHashMap(s, t);
        end = System.nanoTime();
        long time2 = end - start;

        System.out.println("\n性能比较:");
        System.out.println("源字符串长度: " + s.length());
        System.out.println("目标字符串长度: " + t.length());
        System.out.println("==================================");
        System.out.println("方法            | 时间(ns) | 结果");
        System.out.println("----------------|----------|----------");
        System.out.printf("数组计数法      | %8d | %s%n", time1, result1);
        System.out.printf("哈希表法        | %8d | %s%n", time2, result2);
        System.out.println("==================================");
    }

    /**
     * 大型数据集测试
     *
     * @param size        源字符串长度
     * @param patternSize 目标字符串长度
     */
    public static void testLargeDataset(int size, int patternSize) {
        // 生成随机字符串
        String s = generateRandomString(size);
        String t = generateRandomString(patternSize);

        long start = System.nanoTime();
        String result = minWindow(s, t);
        long time = System.nanoTime() - start;

        System.out.printf("\n大型数据集测试 (%d字符源串, %d字符目标串):%n", size, patternSize);
        System.out.println("执行时间: " + time / 1000 + " μs");
        System.out.println("结果长度: " + (result.isEmpty() ? "无匹配" : result.length()));
    }

    // 生成随机字符串
    private static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
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
        testCase("ADOBECODEBANC", "ABC", "BANC");
        testCase("a", "a", "a");
        testCase("a", "aa", "");
    }

    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("", "abc", "");
        testCase("abc", "", "");
        testCase("abc", "def", "");
        testCase("aaabbbccc", "abc", "abbbc");
    }

    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");

        // 小规模测试
        comparePerformance("ADOBECODEBANC", "ABC");

        // 中规模测试

        String s = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 100; i++) {
            s += s;
        }
        String t = "xyz";
        comparePerformance(s, t);

        // 大规模测试
        testLargeDataset(100000, 10);
    }

    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");

        // 场景1: 文本搜索高亮
        String document = "The quick brown fox jumps over the lazy dog";
        String keywords = "fox lazy";
        System.out.println("\n文本搜索高亮:");
        System.out.println("文档: " + document);
        System.out.println("关键词: " + keywords);
        System.out.println("最短片段: " + textSearchHighlighter(document, keywords));

        // 场景2: DNA序列模式识别
        String dna = "AGCTAGCTAGCTAGCT";
        String pattern = "TAGC";
        System.out.println("\nDNA模式识别:");
        System.out.println("DNA序列: " + dna);
        System.out.println("模式: " + pattern);
        System.out.println("匹配序列: " + dnaPatternFinder(dna, pattern));

        // 场景3: 搜索引擎片段提取
        String content = "Java is a high-level, class-based, object-oriented programming language";
        List<String> keywordsList = Arrays.asList("Java", "programming", "language");
        System.out.println("\n搜索引擎片段提取:");
        System.out.println("内容: " + content);
        System.out.println("关键词: " + keywordsList);
        System.out.println("摘要: " + searchEngineSnippet(content, keywordsList));

        // 场景4: 代码片段搜索
        String code = "public static void main(String[] args) { System.out.println(\"Hello\"); }";
        Set<Character> tokens = new HashSet<>(Arrays.asList('p', 'S', 'm'));
        System.out.println("\n代码片段搜索:");
        System.out.println("代码: " + code);
        System.out.println("标记: " + tokens);
        System.out.println("片段: " + codeSnippetFinder(code, tokens));

        // 场景5: 日志模式识别
        String log = "[ERROR] 2023-08-15: Database connection failed at 10:30:45";
        String logPattern = "ERROR Database";
        System.out.println("\n日志模式识别:");
        System.out.println("日志: " + log);
        System.out.println("模式: " + logPattern);
        System.out.println("匹配片段: " + logPatternRecognizer(log, logPattern));

        // 可视化过程
        visualizeWindowProcess("ADOBECODEBANC", "ABC");
        visualizeCharRequirements("ADOBECODEBANC", "ABC");
    }

    private static void testCase(String s, String t, String expected) {
        String result = minWindow(s, t);
        System.out.printf("\n测试: s=\"%s\", t=\"%s\"%n", s, t);
        System.out.println("预期结果: " + expected);
        System.out.println("实际结果: " + result);
        System.out.println("测试结果: " + (result.equals(expected) ? "通过" : "失败"));

        if (s.length() <= 50 && t.length() <= 10) {
            visualizeWindowProcess(s, t);
        }
    }
}