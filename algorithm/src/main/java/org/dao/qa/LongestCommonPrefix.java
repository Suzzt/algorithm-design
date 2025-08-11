package main.java.org.dao.qa;

import java.util.*;

/**
 * 最长公共前缀问题
 * 
 * <p><b>问题描述</b>:
 * 编写一个函数来查找字符串数组中的最长公共前缀。
 * 如果不存在公共前缀，返回空字符串 ""。
 * 
 * <p><b>示例</b>:
 * 输入: strs = ["flower","flow","flight"]
 * 输出: "fl"
 * 
 * <p><b>问题难度</b>: 🔥 简单 (LeetCode 第14题)
 * 
 * <p><b>解题思路</b>:
 * 1. 垂直扫描法: 逐列比较所有字符串的对应字符
 * 2. 水平扫描法: 逐个比较字符串，不断缩短公共前缀
 * 3. 分治法: 递归地将问题分解为子问题
 * 4. 字典树法: 构建前缀树，找到所有字符串的公共路径
 * 5. 二分查找法: 在可能的前缀长度上进行二分搜索
 * 
 * <p><b>时间复杂度</b>:
 *  垂直扫描法: O(S) S为所有字符串字符总数
 *  水平扫描法: O(S)
 *  分治法: O(S)
 *  字典树法: O(S)
 *  二分查找法: O(S*log(minLen))
 * 
 * <p><b>空间复杂度</b>:
 *  垂直扫描法: O(1)
 *  水平扫描法: O(1)
 *  分治法: O(m*log(m)) m为字符串数量
 *  字典树法: O(S)
 *  二分查找法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 文件路径公共目录提取
 * 2. URL地址前缀分析
 * 3. 自动补全系统
 * 4. 数据库索引优化
 * 5. 文本相似度分析
 */

public class LongestCommonPrefix {
    
    // ========================= 解法1: 垂直扫描法 (推荐) =========================
    
    /**
     * 垂直扫描解法
     * 
     * @param strs 字符串数组
     * @return 最长公共前缀
     */
    public static String longestCommonPrefixVertical(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        
        // 逐列比较
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c) {
                    return strs[0].substring(0, i);
                }
            }
        }
        
        return strs[0];
    }
    
    // ========================= 解法2: 水平扫描法 =========================
    
    /**
     * 水平扫描解法
     * 
     * @param strs 字符串数组
     * @return 最长公共前缀
     */
    public static String longestCommonPrefixHorizontal(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        }
        
        return prefix;
    }
    
    // ========================= 解法3: 分治法 =========================
    
    /**
     * 分治解法
     * 
     * @param strs 字符串数组
     * @return 最长公共前缀
     */
    public static String longestCommonPrefixDivideConquer(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        return longestCommonPrefixHelper(strs, 0, strs.length - 1);
    }
    
    private static String longestCommonPrefixHelper(String[] strs, int left, int right) {
        if (left == right) {
            return strs[left];
        }
        
        int mid = (left + right) / 2;
        String lcpLeft = longestCommonPrefixHelper(strs, left, mid);
        String lcpRight = longestCommonPrefixHelper(strs, mid + 1, right);
        
        return commonPrefix(lcpLeft, lcpRight);
    }
    
    private static String commonPrefix(String left, String right) {
        int min = Math.min(left.length(), right.length());
        for (int i = 0; i < min; i++) {
            if (left.charAt(i) != right.charAt(i)) {
                return left.substring(0, i);
            }
        }
        return left.substring(0, min);
    }
    
    // ========================= 解法4: 字典树法 =========================
    
    /**
     * 字典树解法
     * 
     * @param strs 字符串数组
     * @return 最长公共前缀
     */
    public static String longestCommonPrefixTrie(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        
        TrieNode root = buildTrie(strs);
        return findLongestCommonPrefix(root, strs.length);
    }
    
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        int count = 0;
    }
    
    private static TrieNode buildTrie(String[] strs) {
        TrieNode root = new TrieNode();
        
        for (String str : strs) {
            TrieNode current = root;
            for (char c : str.toCharArray()) {
                current.children.putIfAbsent(c, new TrieNode());
                current = current.children.get(c);
                current.count++;
            }
        }
        
        return root;
    }
    
    private static String findLongestCommonPrefix(TrieNode root, int totalStrings) {
        StringBuilder prefix = new StringBuilder();
        TrieNode current = root;
        
        while (current.children.size() == 1) {
            Map.Entry<Character, TrieNode> entry = current.children.entrySet().iterator().next();
            char c = entry.getKey();
            TrieNode next = entry.getValue();
            
            if (next.count == totalStrings) {
                prefix.append(c);
                current = next;
            } else {
                break;
            }
        }
        
        return prefix.toString();
    }
    
    // ========================= 解法5: 二分查找法 =========================
    
    /**
     * 二分查找解法
     * 
     * @param strs 字符串数组
     * @return 最长公共前缀
     */
    public static String longestCommonPrefixBinarySearch(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        
        int minLen = Arrays.stream(strs).mapToInt(String::length).min().orElse(0);
        
        int low = 1, high = minLen;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (isCommonPrefix(strs, middle)) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        
        return strs[0].substring(0, (low + high) / 2);
    }
    
    private static boolean isCommonPrefix(String[] strs, int len) {
        String str1 = strs[0].substring(0, len);
        for (int i = 1; i < strs.length; i++) {
            if (!strs[i].startsWith(str1)) {
                return false;
            }
        }
        return true;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 文件路径公共目录提取
     * 
     * @param filePaths 文件路径数组
     * @return 公共目录路径
     */
    public static String extractCommonDirectory(String[] filePaths) {
        if (filePaths == null || filePaths.length == 0) return "";
        
        // 统一路径分隔符
        String[] normalizedPaths = new String[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            normalizedPaths[i] = filePaths[i].replace('\\', '/');
        }
        
        String commonPrefix = longestCommonPrefixVertical(normalizedPaths);
        
        // 确保结果以完整的目录名结束
        int lastSlash = commonPrefix.lastIndexOf('/');
        if (lastSlash == -1) return "";
        return commonPrefix.substring(0, lastSlash + 1);
    }
    
    /**
     * URL地址前缀分析
     * 
     * @param urls URL数组
     * @return URL前缀分析结果
     */
    public static UrlPrefixAnalysis analyzeUrlPrefix(String[] urls) {
        if (urls == null || urls.length == 0) {
            return new UrlPrefixAnalysis("", "", "", "");
        }
        
        String commonPrefix = longestCommonPrefixVertical(urls);
        
        // 分析前缀结构
        String protocol = "";
        String domain = "";
        String path = "";
        
        if (commonPrefix.contains("://")) {
            int protocolEnd = commonPrefix.indexOf("://");
            protocol = commonPrefix.substring(0, protocolEnd);
            
            String remaining = commonPrefix.substring(protocolEnd + 3);
            int pathStart = remaining.indexOf('/');
            
            if (pathStart == -1) {
                domain = remaining;
            } else {
                domain = remaining.substring(0, pathStart);
                path = remaining.substring(pathStart);
            }
        }
        
        return new UrlPrefixAnalysis(commonPrefix, protocol, domain, path);
    }
    
    static class UrlPrefixAnalysis {
        String fullPrefix;
        String protocol;
        String domain;
        String path;
        
        UrlPrefixAnalysis(String fullPrefix, String protocol, String domain, String path) {
            this.fullPrefix = fullPrefix;
            this.protocol = protocol;
            this.domain = domain;
            this.path = path;
        }

        @Override
        public String toString() {
            return String.format("URL前缀分析[完整:%s, 协议:%s, 域名:%s, 路径:%s]",
                               fullPrefix, protocol, domain, path);
        }
    }
    
    /**
     * 自动补全系统
     * 
     * @param dictionary 词典
     * @param input 用户输入
     * @return 补全建议
     */
    public static List<String> autoComplete(String[] dictionary, String input) {
        if (dictionary == null || input == null) return new ArrayList<>();
        
        List<String> candidates = new ArrayList<>();
        for (String word : dictionary) {
            if (word.startsWith(input)) {
                candidates.add(word);
            }
        }
        
        if (candidates.isEmpty()) return candidates;
        
        // 找到所有候选词的公共前缀作为自动补全
        String commonPrefix = longestCommonPrefixVertical(candidates.toArray(new String[0]));
        
        // 如果公共前缀比输入长，则提供补全建议
        if (commonPrefix.length() > input.length()) {
            List<String> suggestions = new ArrayList<>();
            suggestions.add(commonPrefix);
            
            // 添加最多5个候选词
            for (int i = 0; i < Math.min(5, candidates.size()); i++) {
                if (!suggestions.contains(candidates.get(i))) {
                    suggestions.add(candidates.get(i));
                }
            }
            return suggestions;
        }
        
        return candidates.subList(0, Math.min(5, candidates.size()));
    }
    
    /**
     * 数据库索引优化分析
     * 
     * @param indexKeys 索引键数组
     * @return 索引优化建议
     */
    public static IndexOptimization analyzeIndexOptimization(String[] indexKeys) {
        if (indexKeys == null || indexKeys.length == 0) {
            return new IndexOptimization("", 0, 0, "无数据");
        }
        
        String commonPrefix = longestCommonPrefixVertical(indexKeys);
        int prefixLength = commonPrefix.length();
        
        // 计算压缩比例
        int totalOriginalLength = Arrays.stream(indexKeys).mapToInt(String::length).sum();
        int totalCompressedLength = indexKeys.length * prefixLength + 
                                   Arrays.stream(indexKeys).mapToInt(s -> s.length() - prefixLength).sum();
        
        double compressionRatio = totalOriginalLength > 0 ? 
                                 (1.0 - (double)totalCompressedLength / totalOriginalLength) * 100 : 0;
        
        String recommendation;
        if (prefixLength > 3 && compressionRatio > 20) {
            recommendation = "建议使用前缀压缩索引";
        } else if (prefixLength > 0) {
            recommendation = "可考虑前缀优化";
        } else {
            recommendation = "前缀优化效果不明显";
        }
        
        return new IndexOptimization(commonPrefix, prefixLength, compressionRatio, recommendation);
    }
    
    static class IndexOptimization {
        String commonPrefix;
        int prefixLength;
        double compressionRatio;
        String recommendation;
        
        IndexOptimization(String commonPrefix, int prefixLength, double compressionRatio, String recommendation) {
            this.commonPrefix = commonPrefix;
            this.prefixLength = prefixLength;
            this.compressionRatio = compressionRatio;
            this.recommendation = recommendation;
        }
        
        @Override
        public String toString() {
            return String.format("索引优化[前缀:'%s', 长度:%d, 压缩率:%.1f%%, 建议:%s]",
                               commonPrefix, prefixLength, compressionRatio, recommendation);
        }
    }
    
    /**
     * 文本相似度分析
     * 
     * @param texts 文本数组
     * @return 相似度分析结果
     */
    public static TextSimilarityAnalysis analyzeTextSimilarity(String[] texts) {
        if (texts == null || texts.length == 0) {
            return new TextSimilarityAnalysis("", 0, 0, "无数据");
        }
        
        String commonPrefix = longestCommonPrefixVertical(texts);
        int prefixLength = commonPrefix.length();
        
        // 计算平均文本长度
        double avgLength = Arrays.stream(texts).mapToInt(String::length).average().orElse(0);
        
        // 计算相似度分数
        double similarityScore = avgLength > 0 ? (prefixLength / avgLength) * 100 : 0;
        
        String category;
        if (similarityScore > 80) {
            category = "高度相似";
        } else if (similarityScore > 50) {
            category = "中等相似";
        } else if (similarityScore > 20) {
            category = "低度相似";
        } else {
            category = "几乎无相似性";
        }
        
        return new TextSimilarityAnalysis(commonPrefix, prefixLength, similarityScore, category);
    }
    
    static class TextSimilarityAnalysis {
        String commonPrefix;
        int prefixLength;
        double similarityScore;
        String category;
        
        TextSimilarityAnalysis(String commonPrefix, int prefixLength, double similarityScore, String category) {
            this.commonPrefix = commonPrefix;
            this.prefixLength = prefixLength;
            this.similarityScore = similarityScore;
            this.category = category;
        }
        
        @Override
        public String toString() {
            return String.format("文本相似度[前缀:'%s', 长度:%d, 分数:%.1f%%, 类别:%s]",
                               commonPrefix, prefixLength, similarityScore, category);
        }
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化前缀查找过程
     * 
     * @param strs 字符串数组
     */
    public static void visualizePrefixFinding(String[] strs) {
        if (strs == null || strs.length == 0) {
            System.out.println("空字符串数组");
            return;
        }
        
        System.out.println("\n最长公共前缀查找过程:");
        System.out.println("输入字符串:");
        for (int i = 0; i < strs.length; i++) {
            System.out.printf("  [%d]: \"%s\"%n", i, strs[i]);
        }
        
        System.out.println("\n垂直扫描过程:");
        System.out.print("位置: ");
        for (int i = 0; i < strs[0].length(); i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println();
        
        for (int i = 0; i < strs.length; i++) {
            System.out.printf("串%d:  ", i);
            for (int j = 0; j < strs[i].length(); j++) {
                System.out.printf("%2c ", strs[i].charAt(j));
            }
            System.out.println();
        }
        
        System.out.println("\n逐列比较:");
        int prefixLen = 0;
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            boolean allMatch = true;
            
            System.out.printf("位置%d, 字符'%c': ", i, c);
            
            for (int j = 1; j < strs.length; j++) {
                if (i >= strs[j].length() || strs[j].charAt(i) != c) {
                    allMatch = false;
                    System.out.printf("串%d不匹配 ", j);
                    break;
                }
            }
            
            if (allMatch) {
                System.out.println("全部匹配 ✓");
                prefixLen = i + 1;
            } else {
                System.out.println("不匹配 ✗");
                break;
            }
        }
        
        String result = strs[0].substring(0, prefixLen);
        System.out.println("\n结果: \"" + result + "\"");
    }
    
    /**
     * 可视化字典树构建过程
     * 
     * @param strs 字符串数组
     */
    public static void visualizeTrieBuilding(String[] strs) {
        if (strs == null || strs.length == 0) return;
        
        System.out.println("\n字典树构建过程:");
        
        TrieNode root = new TrieNode();
        
        for (int strIndex = 0; strIndex < strs.length; strIndex++) {
            String str = strs[strIndex];
            System.out.printf("\n插入字符串 \"%s\":%n", str);
            
            TrieNode current = root;
            StringBuilder path = new StringBuilder();
            
            for (char c : str.toCharArray()) {
                current.children.putIfAbsent(c, new TrieNode());
                current = current.children.get(c);
                current.count++;
                path.append(c);
                
                System.out.printf("  路径: \"%s\", 计数: %d%n", path.toString(), current.count);
            }
        }
        
        System.out.println("\n查找公共前缀:");
        StringBuilder prefix = new StringBuilder();
        TrieNode current = root;
        
        while (current.children.size() == 1) {
            Map.Entry<Character, TrieNode> entry = current.children.entrySet().iterator().next();
            char c = entry.getKey();
            TrieNode next = entry.getValue();
            
            System.out.printf("  检查字符'%c', 计数:%d, 总串数:%d%n", c, next.count, strs.length);
            
            if (next.count == strs.length) {
                prefix.append(c);
                current = next;
                System.out.printf("  添加到前缀: \"%s\"%n", prefix.toString());
            } else {
                System.out.println("  计数不匹配，停止");
                break;
            }
        }
        
        System.out.println("\n最终前缀: \"" + prefix.toString() + "\"");
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param stringCount 字符串数量
     * @param avgLength 平均长度
     * @param prefixLength 公共前缀长度
     */
    public static void comparePerformance(int stringCount, int avgLength, int prefixLength) {
        String[] strs = generateTestStrings(stringCount, avgLength, prefixLength);
        
        long start, end;
        
        // 测试垂直扫描法
        start = System.nanoTime();
        String result1 = longestCommonPrefixVertical(strs.clone());
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试水平扫描法
        start = System.nanoTime();
        String result2 = longestCommonPrefixHorizontal(strs.clone());
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试分治法
        start = System.nanoTime();
        String result3 = longestCommonPrefixDivideConquer(strs.clone());
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试字典树法
        start = System.nanoTime();
        String result4 = longestCommonPrefixTrie(strs.clone());
        end = System.nanoTime();
        long time4 = end - start;
        
        // 测试二分查找法
        start = System.nanoTime();
        String result5 = longestCommonPrefixBinarySearch(strs.clone());
        end = System.nanoTime();
        long time5 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("字符串数量: %d, 平均长度: %d, 期望前缀长度: %d%n", 
                          stringCount, avgLength, prefixLength);
        System.out.println("============================================");
        System.out.println("方法         | 时间(ms)   | 结果长度 | 结果正确");
        System.out.println("-------------|------------|----------|--------");
        System.out.printf("垂直扫描     | %.6f | %8d | 基准%n", time1 / 1_000_000.0, result1.length());
        System.out.printf("水平扫描     | %.6f | %8d | %s%n", time2 / 1_000_000.0, result2.length(), result1.equals(result2) ? "是" : "否");
        System.out.printf("分治法       | %.6f | %8d | %s%n", time3 / 1_000_000.0, result3.length(), result1.equals(result3) ? "是" : "否");
        System.out.printf("字典树       | %.6f | %8d | %s%n", time4 / 1_000_000.0, result4.length(), result1.equals(result4) ? "是" : "否");
        System.out.printf("二分查找     | %.6f | %8d | %s%n", time5 / 1_000_000.0, result5.length(), result1.equals(result5) ? "是" : "否");
        System.out.println("============================================");
    }
    
    private static String[] generateTestStrings(int count, int avgLength, int prefixLength) {
        Random random = new Random();
        String[] strs = new String[count];
        
        // 生成公共前缀
        StringBuilder commonPrefix = new StringBuilder();
        for (int i = 0; i < prefixLength; i++) {
            commonPrefix.append((char)('a' + random.nextInt(26)));
        }
        
        // 生成字符串
        for (int i = 0; i < count; i++) {
            StringBuilder sb = new StringBuilder(commonPrefix);
            int remainingLength = avgLength - prefixLength + random.nextInt(10) - 5;
            remainingLength = Math.max(0, remainingLength);
            
            for (int j = 0; j < remainingLength; j++) {
                sb.append((char)('a' + random.nextInt(26)));
            }
            
            strs[i] = sb.toString();
        }
        
        return strs;
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
        testCase("示例1", new String[]{"flower","flow","flight"}, "fl");
        testCase("示例2", new String[]{"dog","racecar","car"}, "");
        testCase("相同字符串", new String[]{"test","test","test"}, "test");
        testCase("包含关系", new String[]{"abc","ab","abcd"}, "ab");
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("空数组", new String[]{}, "");
        testCase("单个字符串", new String[]{"alone"}, "alone");
        testCase("空字符串", new String[]{"","abc"}, "");
        testCase("一个字符", new String[]{"a","a","a"}, "a");
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 文件路径公共目录
        String[] filePaths = {
            "/home/user/documents/file1.txt",
            "/home/user/documents/file2.txt",
            "/home/user/documents/subfolder/file3.txt"
        };
        String commonDir = extractCommonDirectory(filePaths);
        System.out.println("\n文件路径分析:");
        System.out.println("文件路径:");
        for (String path : filePaths) {
            System.out.println("  " + path);
        }
        System.out.println("公共目录: " + commonDir);
        
        // 场景2: URL前缀分析
        String[] urls = {
            "https://www.example.com/api/v1/users",
            "https://www.example.com/api/v1/orders",
            "https://www.example.com/api/v1/products"
        };
        UrlPrefixAnalysis urlAnalysis = analyzeUrlPrefix(urls);
        System.out.println("\nURL前缀分析:");
        System.out.println("URL列表:");
        for (String url : urls) {
            System.out.println("  " + url);
        }
        System.out.println(urlAnalysis);
        
        // 场景3: 自动补全
        String[] dictionary = {"apple", "application", "apply", "appreciate", "approach"};
        String input = "app";
        List<String> suggestions = autoComplete(dictionary, input);
        System.out.println("\n自动补全测试:");
        System.out.println("词典: " + Arrays.toString(dictionary));
        System.out.println("输入: " + input);
        System.out.println("建议: " + suggestions);
        
        // 场景4: 索引优化
        String[] indexKeys = {
            "user_profile_001",
            "user_profile_002", 
            "user_profile_003",
            "user_profile_004"
        };
        IndexOptimization indexOpt = analyzeIndexOptimization(indexKeys);
        System.out.println("\n数据库索引优化:");
        System.out.println("索引键: " + Arrays.toString(indexKeys));
        System.out.println(indexOpt);
        
        // 场景5: 文本相似度
        String[] texts = {
            "Hello world, this is a test",
            "Hello world, this is another test",
            "Hello world, this is yet another test"
        };
        TextSimilarityAnalysis textSim = analyzeTextSimilarity(texts);
        System.out.println("\n文本相似度分析:");
        System.out.println("文本列表:");
        for (String text : texts) {
            System.out.println("  \"" + text + "\"");
        }
        System.out.println(textSim);
        
        // 可视化测试
        System.out.println("\n可视化测试:");
        visualizePrefixFinding(new String[]{"flower","flow","flight"});
        visualizeTrieBuilding(new String[]{"flower","flow","flight"});
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(10, 20, 5);
        comparePerformance(100, 50, 10);
        comparePerformance(1000, 100, 20);
    }
    
    private static void testCase(String name, String[] strs, String expected) {
        System.out.printf("\n测试用例: %s%n", name);
        if (strs.length == 0) {
            System.out.println("输入: 空数组");
        } else {
            System.out.println("输入: " + Arrays.toString(strs));
        }
        
        String result1 = longestCommonPrefixVertical(strs.clone());
        String result2 = longestCommonPrefixHorizontal(strs.clone());
        String result3 = longestCommonPrefixDivideConquer(strs.clone());
        String result4 = longestCommonPrefixTrie(strs.clone());
        String result5 = longestCommonPrefixBinarySearch(strs.clone());
        
        System.out.println("垂直扫描结果: \"" + result1 + "\"");
        System.out.println("水平扫描结果: \"" + result2 + "\"");
        System.out.println("分治法结果: \"" + result3 + "\"");
        System.out.println("字典树结果: \"" + result4 + "\"");
        System.out.println("二分查找结果: \"" + result5 + "\"");
        
        boolean passed = result1.equals(expected) && result2.equals(expected) && 
                        result3.equals(expected) && result4.equals(expected) && result5.equals(expected);
        System.out.println("测试结果: " + (passed ? "通过" : "失败"));
        
        if (!passed) {
            System.out.println("预期结果: \"" + expected + "\"");
        }
        
        // 可视化小规模案例
        if (strs.length > 0 && strs.length <= 5 && 
            Arrays.stream(strs).mapToInt(String::length).max().orElse(0) <= 20) {
            visualizePrefixFinding(strs);
        }
    }
}