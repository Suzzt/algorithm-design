package main.java.org.dao.qa;

import java.util.*;

/**
 * 最小覆盖子串问题 - 找到包含目标字符串所有字符的最短子串
 * 
 * <p><b>问题描述</b>:
 * 给定一个字符串 S 和一个字符串 T，在 S 中找出包含 T 所有字符的最小子串。如果不存在，返回空字符串。
 * 
 * <p><b>示例</b>:
 * 输入: S = "ADOBECODEBANC", T = "ABC"
 * 输出: "BANC"
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第76题)
 * 
 * <p><b>解题思路</b>:
 * 1. 滑动窗口技术:
 *    - 使用左右指针定义窗口的边界
 *    - 使用哈希表记录目标字符串中每个字符的出现频率
 *    - 扩展右指针直到窗口包含所有目标字符
 *    - 收缩左指针寻找最小窗口
 * 2. 优化技巧:
 *    - 使用频率计数判断窗口是否满足条件
 *    - 在满足条件时更新最小窗口
 * 
 * <p><b>时间复杂度</b>: O(|S| + |T|) - 线性时间
 * <p><b>空间复杂度</b>: O(1) - 字符频率数组大小固定为128
 * 
 * <p><b>应用场景</b>:
 * 1. DNA序列分析
 * 2. 文档内容搜索
 * 3. 广告关键词匹配
 * 4. 实时日志分析
 * 5. 数据挖掘中的特征提取
 */

public class MinimumWindowSubstring {
    
    /**
     * 滑动窗口解法
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最小覆盖子串
     */
    public static String minWindow(String s, String t) {
        // 处理特殊情况
        if (s == null || t == null || s.length() == 0 || t.length() == 0 ||
            s.length() < t.length()) {
            return "";
        }
        
        // 初始化频率数组
        int[] freq = new int[128];
        for (char c : t.toCharArray()) {
            freq[c]++;
        }
        
        int left = 0, right = 0;             // 滑动窗口边界
        int minLeft = 0;                     // 最小窗口左边界
        int minLen = Integer.MAX_VALUE;      // 最小窗口长度
        int required = t.length();            // 需要匹配的字符数量
        
        // 滑动窗口遍历
        while (right < s.length()) {
            char rightChar = s.charAt(right);
            
            // 遇到目标字符，减少需求计数
            if (freq[rightChar] > 0) {
                required--;
            }
            
            // 减少该字符的频数（非目标字符会变成负数）
            freq[rightChar]--;
            right++;
            
            // 当窗口包含所有目标字符时
            while (required == 0) {
                // 更新最小窗口
                if (right - left < minLen) {
                    minLeft = left;
                    minLen = right - left;
                }
                
                // 移动左指针
                char leftChar = s.charAt(left);
                freq[leftChar]++;
                
                // 如果左指针移动使得某个目标字符不再满足要求
                if (freq[leftChar] > 0) {
                    required++;
                }
                left++;
            }
        }
        
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minLeft, minLeft + minLen);
    }
    
    /**
     * 可视化滑动窗口过程
     * 
     * @param s 源字符串
     * @param t 目标字符串
     */
    public static void visualizeWindow(String s, String t) {
        System.out.println("\n字符串: " + s);
        System.out.println("目标: " + t);
        System.out.println("可视化滑动窗口过程:");
        
        int[] freq = new int[128];
        for (char c : t.toCharArray()) {
            freq[c]++;
        }
        
        int left = 0, right = 0;
        int required = t.length();
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : t.toCharArray()) {
            freqMap.putIfAbsent(c, 0);
            freqMap.put(c, freqMap.get(c) + 1);
        }
        
        while (right < s.length()) {
            System.out.println("\n右指针位置: " + right);
            char rightChar = s.charAt(right);
            
            // 显示当前字符
            System.out.println("当前字符: " + rightChar);
            
            if (freq[rightChar] > 0) {
                required--;
            }
            freq[rightChar]--;
            
            // 显示频率变化
            System.out.print("剩余需求: ");
            for (char c : freqMap.keySet()) {
                System.out.print(c + ":" + Math.max(freq[c], 0) + " ");
            }
            System.out.println("\n未满足需求数: " + required);
            
            right++;
            
            // 显示窗口状态
            System.out.println("当前窗口: [" + s.substring(left, right) + "]");
            
            while (required == 0) {
                System.out.println("满足条件! 最小窗口长度: " + (right - left));
                
                char leftChar = s.charAt(left);
                freq[leftChar]++;
                
                if (freq[leftChar] > 0) {
                    required++;
                    System.out.println("移动左指针: " + left + " -> " + (left+1));
                    System.out.println("需求增加: " + leftChar);
                }
                
                left++;
            }
        }
        
        String result = minWindow(s, t);
        System.out.println("\n最小覆盖子串: " + result);
    }
    
    /**
     * 扩展应用：包含目标字符串所有字符但顺序不要求的序列
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 最短满足子串
     */
    public static String minWindowExtended(String s, String t) {
        // 与原始方法相同，但输出扩展信息
        String result = minWindow(s, t);
        System.out.println("\n应用扩展：包含顺序不要求的序列");
        System.out.println("输入: S=\"" + s + "\", T=\"" + t + "\"");
        System.out.println("结果: \"" + result + "\"");
        System.out.println("分析: 虽然目标字符要求顺序 " + t + 
                         "，但实际顺序为 " + (result.isEmpty() ? "N/A" : result));
        return result;
    }
    
    /**
     * 测试用例和主函数
     */
    public static void main(String[] args) {
        // 基础测试
        testBasicCases();
        
        // 扩展功能测试
        testExtendedCases();
        
        // 性能测试
        testPerformance();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        String[][] testCases = {
            {"ADOBECODEBANC", "ABC"},  // "BANC"
            {"a", "a"},                // "a"
            {"a", "aa"},               // ""
            {"ab", "a"},               // "a"
            {"ab", "b"},               // "b"
            {"abc", "ac"},             // "abc"
            {"abracadabra", "abc"},   // "brac"
            {"helloworld", "lol"},    // "llo"
            {"timetopractice", "toc"}, // "topractice"
            {"zoomlazaro", "oo"}       // "oo"
        };
        
        String[] expected = {
            "BANC", "a", "", "a", "b", "abc", "brac", "worl", "topractice", "oo"
        };
        
        for (int i = 0; i < testCases.length; i++) {
            String s = testCases[i][0];
            String t = testCases[i][1];
            String result = minWindow(s, t);
            
            System.out.printf("\n测试%d: S=\"%s\", T=\"%s\"", i+1, s, t);
            System.out.printf("\n结果: \"%s\"", result);
            System.out.printf("\n预期: \"%s\"", expected[i]);
            System.out.printf("\n状态: %s\n", result.equals(expected[i]) ? "✅" : "❌");
            
            // 特殊用例可视化
            if (i == 0 || i == 6) {
                visualizeWindow(s, t);
            }
        }
    }
    
    private static void testExtendedCases() {
        System.out.println("\n====== 扩展应用测试 ======");
        // 包含顺序不要求的序列
        minWindowExtended("ADOBECODEBANC", "CAB");
        
        // 空目标测试
        minWindowExtended("abc", "");
        
        // 多重复字符
        minWindowExtended("aaabbbccc", "abbc");
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 生成长字符串
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 1_000_000; i++) {
            sb.append((char)('A' + rand.nextInt(26)));
        }
        String longS = sb.toString();
        String longT = "ABCDEFG";
        
        // Unicode测试字符串
        String unicodeS = "中文测试字符串包含各种字符ξηθλμνξ日本語";
        String unicodeT = "字符ξ";
        
        // 性能测试
        long startTime;
        int iterations = 10;
        
        // 长字符串测试
        startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            minWindow(longS, longT);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("百万字符处理时间: " + (endTime - startTime) + "ms (10次平均)");
        
        // Unicode字符测试
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            minWindow(unicodeS, unicodeT);
        }
        endTime = System.currentTimeMillis();
        System.out.println("Unicode处理时间: " + (endTime - startTime) + "ms (10000次平均)");
        
        // 结果显示
        String result = minWindow(longS, longT);
        System.out.println("\n长字符串示例结果长度: " + result.length());
    }
}