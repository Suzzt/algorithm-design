package main.java.org.dao.qa;

import java.util.Stack;

/**
 * 最长有效括号问题 - 找到最长的有效括号子串长度
 * 
 * <p><b>问题描述</b>:
 * 给定一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第32题)
 * 
 * <p><b>解题思路</b>:
 * 1. <b>动态规划解法</b>:
 *    - 定义 dp[i] 表示以第i个字符结尾的最长有效括号长度
 *    - 处理 '()' 和 '))' 两种情况
 * 2. <b>栈解法</b>:
 *    - 使用栈记录括号的索引位置
 *    - 维护当前有效括号子串的起始位置
 * 3. <b>双指针解法</b>:
 *    - 左右扫描两次字符串
 *    - 无需额外空间
 * 
 * <p><b>时间复杂度</b>:
 *   - 动态规划: O(n)
 *   - 栈解法: O(n)
 *   - 双指针: O(n)
 * 
 * <p><b>空间复杂度</b>:
 *   - 动态规划: O(n)
 *   - 栈解法: O(n)
 *   - 双指针: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 代码编辑器语法检测
 * 2. 编译器语法解析
 * 3. 字符串格式验证器
 * 4. 数据压缩算法
 * 5. 数学表达式计算
 */

public class LongestValidParentheses {

    /**
     * 动态规划解法
     */
    public static int dpSolution(String s) {
        if (s == null || s.length() == 0) return 0;
        
        int n = s.length();
        int maxLen = 0;
        int[] dp = new int[n]; // dp[i]表示以s.charAt(i)结尾的最长有效括号长度
        
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                // 情况1: "()"
                if (s.charAt(i-1) == '(') {
                    dp[i] = (i >= 2 ? dp[i-2] : 0) + 2;
                } 
                // 情况2: "))"
                else if (i - dp[i-1] > 0 && s.charAt(i - dp[i-1] - 1) == '(') {
                    dp[i] = dp[i-1] + 2 + ((i - dp[i-1] - 2) >= 0 ? dp[i - dp[i-1] - 2] : 0);
                }
                maxLen = Math.max(maxLen, dp[i]);
            }
        }
        return maxLen;
    }
    
    /**
     * 栈解法
     */
    public static int stackSolution(String s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1); // 哨兵节点
        int maxLen = 0;
        
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop(); // 弹出匹配的'('或哨兵节点
                if (stack.isEmpty()) {
                    // 无效括号，重置起始位置
                    stack.push(i);
                } else {
                    // 计算当前有效长度
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        return maxLen;
    }
    
    /**
     * 双指针解法（空间优化）
     */
    public static int twoPointersSolution(String s) {
        int left = 0, right = 0;
        int maxLen = 0;
        
        // 从左向右扫描
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }
            
            if (left == right) {
                maxLen = Math.max(maxLen, left * 2);
            } else if (right > left) {
                left = right = 0;
            }
        }
        
        // 从右向左扫描处理左括号多于右括号的情况
        left = right = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }
            
            if (left == right) {
                maxLen = Math.max(maxLen, left * 2);
            } else if (left > right) {
                left = right = 0;
            }
        }
        
        return maxLen;
    }
    
    /**
     * 可视化括号匹配
     */
    public static void visualizeMatching(String s) {
        if (s.isEmpty()) return;
        
        Stack<Integer> stack = new Stack<>();
        boolean[] valid = new boolean[s.length()]; // 标记有效括号位置
        
        // 标记所有有效括号
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else if (!stack.isEmpty()) {
                valid[i] = true;
                valid[stack.pop()] = true;
            }
        }
        
        // 可视化字符串下方的有效性标记
        System.out.println("字符串: " + s);
        
        // 第一行: 原始字符串
        for (char c : s.toCharArray()) {
            System.out.print(c + " ");
        }
        System.out.println();
        
        // 第二行: 有效性标记
        for (int i = 0; i < s.length(); i++) {
            System.out.print(valid[i] ? "√ " : "× ");
        }
        System.out.println();
        
        // 第三行: 最长有效括号子串标记
        int maxLen = dpSolution(s);
        int start = -1;
        for (int i = 0; i <= s.length() - maxLen; i++) {
            if (dpSolution(s.substring(i, i + maxLen)) == maxLen) {
                start = i;
                break;
            }
        }
        
        for (int i = 0; i < s.length(); i++) {
            if (start <= i && i < start + maxLen) {
                System.out.print("⎺ ");
            } else {
                System.out.print("  ");
            }
        }
        System.out.println("\n长度: " + maxLen);
    }
    
    /**
     * 测试用例和主函数
     */
    public static void main(String[] args) {
        String[] testCases = {
            "(()",          // 2
            ")()())",       // 4
            "()(()",        // 2
            "()(())",       // 6
            ")(())))(())())", // 6
            "(",            // 0
            "",             // 0
            "((()()()))",   // 10
            "()()())(()",   // 6
            ")(())(()()))"  // 10
        };
        
        int[] expected = {2, 4, 2, 6, 6, 0, 0, 10, 6, 10};
        
        for (int i = 0; i < testCases.length; i++) {
            String s = testCases[i];
            System.out.println("\n====== 测试" + (i+1) + " ======");
            System.out.println("输入: \"" + s + "\"");
            
            // 三种解法验证
            int dpResult = dpSolution(s);
            int stackResult = stackSolution(s);
            int pointersResult = twoPointersSolution(s);
            
            System.out.println("动态规划结果: " + dpResult);
            System.out.println("栈解法结果: " + stackResult);
            System.out.println("双指针结果: " + pointersResult);
            System.out.println("预期结果: " + expected[i]);
            
            boolean pass = dpResult == expected[i] && 
                          stackResult == expected[i] &&
                          pointersResult == expected[i];
            System.out.println("状态: " + (pass ? "通过 ✅" : "失败 ❌"));
            
            // 可视化匹配结果
            System.out.println("\n括号匹配可视化:");
            visualizeMatching(s);
        }
    }
}