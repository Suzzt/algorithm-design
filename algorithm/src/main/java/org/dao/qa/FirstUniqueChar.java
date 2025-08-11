package main.java.org.dao.qa;

import java.util.*;

/**
 * 字符串中第一个不重复的字符
 *
 * <p><b>问题描述</b>:
 * 给定一个字符串，找到它的第一个不重复字符，并返回它的索引。如果不存在，则返回 -1。
 *
 * <p><b>示例</b>:
 * 输入: "leetcode"
 * 输出: 0
 *
 * 输入: "loveleetcode"
 * 输出: 2
 *
 * 输入: "aabb"
 * 输出: -1
 *
 * <p><b>问题难度</b>: 🔥 简单（LeetCode 第387题）
 *
 * <p><b>解题思路</b>:
 * 我们可以使用一个哈希表来记录每个字符出现的次数，并在第二次遍历时找到第一个只出现一次的字符。
 *
 * 步骤如下:
 * 1. 使用 `Map<Character, Integer>` 记录每个字符的出现次数。
 * 2. 再次遍历字符串，找到第一个出现次数为 1 的字符并返回其索引。
 * 3. 如果不存在，返回 -1。
 *
 * <p><b>时间复杂度</b>: O(n)
 * <p><b>空间复杂度</b>: O(1)，因为英文字母数量是常数级 26。
 */
public class FirstUniqueChar {

    /**
     * 查找字符串中第一个不重复的字符索引
     *
     * @param s 输入字符串
     * @return 第一个不重复字符的索引，若无返回 -1
     */
    public static int firstUniqChar(String s) {
        if (s == null || s.isEmpty()) return -1;

        // 第一次遍历：统计每个字符出现次数
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : s.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        // 第二次遍历：找出第一个只出现一次的字符
        for (int i = 0; i < s.length(); i++) {
            if (freqMap.get(s.charAt(i)) == 1) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 测试用例
     */
    public static void main(String[] args) {
        test("leetcode", 0);
        test("loveleetcode", 2);
        test("aabb", -1);
        test("xxyz", 2);
        test("", -1);
    }

    /**
     * 简易测试方法
     *
     * @param input 输入字符串
     * @param expected 预期输出
     */
    private static void test(String input, int expected) {
        int result = firstUniqChar(input);
        System.out.printf("输入: \"%s\"，输出: %d，预期: %d，测试结果: %s%n",
                input, result, expected, result == expected ? "✅" : "❌");
    }
}
