package main.java.org.dao.qa;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 带权重编辑距离(Weighted Edit Distance)解决方案
 * 
 * <p><b>问题描述：</b>
 * 给定两个字符串s和t，以及各种编辑操作的权重（插入、删除、替换），
 * 计算将s转换为t的最小操作成本（总权重）。相比基础编辑距离，此问题中：
 * 1. 不同字符的替换操作可能有不同的权重
 * 2. 插入和删除操作的权重可能不同
 * 
 * <p><b>应用场景：</b>
 * 1. DNA序列突变分析（不同碱基替换成本不同）
 * 2. 拼写校正系统（不同字母混淆概率不同）
 * 3. OCR文本校正（不同形状字符的识别难度不同）
 * 4. 键盘输入错误校正（不同键位距离导致的输入错误权重不同）
 */
public class WeightedEditDistance {
    
    // 操作权重配置（可根据实际场景调整）
    static final int DEFAULT_INSERT_COST = 1;
    static final int DEFAULT_DELETE_COST = 1;
    static final int DEFAULT_SKIP_COST = 0;
    
    /**
     * 计算带权重编辑距离
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @param costMatrix 字符替换成本矩阵
     * @param insertCost 插入操作权重
     * @param deleteCost 删除操作权重
     * @return 最小操作成本
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    public static int weightedEditDistance(String s, String t, 
                                          int[][] costMatrix,
                                          int insertCost, 
                                          int deleteCost) {
        int m = s.length();
        int n = t.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化空字符串转换成本
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i * deleteCost;  // 删除所有字符的成本
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j * insertCost;  // 插入所有字符的成本
        }
        
        // 填充DP表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int charS = s.charAt(i - 1) - 'a';
                int charT = t.charAt(j - 1) - 'a';
                
                int replaceCost = costMatrix[charS][charT];
                
                // 计算三种操作的最小成本
                int delete = dp[i - 1][j] + deleteCost;
                int insert = dp[i][j - 1] + insertCost;
                int replace = dp[i - 1][j - 1] + replaceCost;
                
                dp[i][j] = Math.min(Math.min(delete, insert), replace);
            }
        }
        
        return dp[m][n];
    }
    
    /**
     * 生物信息学专用DNA序列变异成本计算
     * 
     * <p>DNA序列编辑距离的特殊规则：
     * 1. 嘌呤之间的替换(A↔G)成本较低(0.5)
     * 2. 嘧啶之间的替换(C↔T)成本较低(0.5)
     * 3. 跨类替换(A/G↔C/T)成本较高(1.5)
     * 
     * @param dna1 DNA序列1
     * @param dna2 DNA序列2
     * @return 最小变异成本
     */
    public static double dnaSequenceDistance(String dna1, String dna2) {
        // DNA碱基替换成本矩阵
        int[][] dnaCostMatrix = {
            // A   C   G   T
            { 0,  3,  1,  3 },  // A
            { 3,  0,  3,  1 },  // C
            { 1,  3,  0,  3 },  // G
            { 3,  1,  3,  0 }   // T
        };
        
        return weightedEditDistance(dna1.toLowerCase(), dna2.toLowerCase(),
                                    dnaCostMatrix, 2, 2);
    }
    
    /**
     * 键盘距离感知的拼写校正
     * 
     * <p>基于QWERTY键盘布局的键位距离计算替换成本：
     * 1. 相同键位：成本0
     * 2. 相邻键位：成本1
     * 3. 较远键位：成本2
     * 
     * @param input 用户输入
     * @param correct 正确拼写
     * @return 最小编辑成本
     */
    public static int keyboardAwareDistance(String input, String correct) {
        // 简化的QWERTY键盘键位映射
        char[][] keyboard = {
            {'q','w','e','r','t','y','u','i','o','p'},
            {'a','s','d','f','g','h','j','k','l'},
            {'z','x','c','v','b','n','m'}
        };
        
        int[][] keyboardCost = new int[26][26];
        
        // 初始化键盘距离成本矩阵
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                char c1 = (char)('a' + i);
                char c2 = (char)('a' + j);
                
                if (c1 == c2) {
                    keyboardCost[i][j] = 0;
                } else {
                    // 计算键位之间的物理距离
                    keyboardCost[i][j] = getKeyDistance(c1, c2, keyboard);
                }
            }
        }
        
        return weightedEditDistance(input, correct, keyboardCost, 1, 1);
    }
    
    // 计算键盘上两个键位的距离
    private static int getKeyDistance(char a, char b, char[][] keyboard) {
        int[] posA = findPosition(a, keyboard);
        int[] posB = findPosition(b, keyboard);
        
        if (posA == null || posB == null) return 2;
        
        // 欧几里得距离简化为曼哈顿距离
        int dist = Math.abs(posA[0] - posB[0]) + Math.abs(posA[1] - posB[1]);
        
        // 距离映射到成本：0->0, 1->1, >=2->2
        return Math.min(dist, 2);
    }
    
    // 查找键位在键盘布局中的位置
    private static int[] findPosition(char c, char[][] keyboard) {
        for (int r = 0; r < keyboard.length; r++) {
            for (int col = 0; col < keyboard[r].length; col++) {
                if (keyboard[r][col] == c) {
                    return new int[]{r, col};
                }
            }
        }
        return null;
    }
    
    // ========================= 测试用例 =========================
    public static void main(String[] args) {
        // 基础测试
        testGeneralCases();
        
        // DNA序列测试
        testDnaAlignment();
        
        // 键盘感知测试
        testKeyboardAwareCorrection();
    }
    
    private static void testGeneralCases() {
        System.out.println("====== 基本带权重编辑距离测试 ======");
        
        // 标准成本矩阵：相同字母为0，不同字母替换成本为1
        int[][] basicCostMatrix = new int[26][26];
        for (int i = 0; i < 26; i++) {
            Arrays.fill(basicCostMatrix[i], 1);
            basicCostMatrix[i][i] = 0;
        }
        
        String[][] testCases = {
            {"kitten", "sitting"}, // kit->sit
            {"intention", "execution"},
            {"sunday", "saturday"},
            {"test", "text"}
        };
        
        for (String[] test : testCases) {
            int distance = weightedEditDistance(test[0], test[1], 
                                               basicCostMatrix, 1, 1);
            System.out.printf("%s -> %s: %d\n", test[0], test[1], distance);
        }
    }
    
    private static void testDnaAlignment() {
        System.out.println("\n====== DNA序列变异成本测试 ======");
        
        String dnaSeq1 = "AGCTAGCT";
        String dnaSeq2 = "AGCCAGCT"; // 单点突变 (T->C)
        
        double cost = dnaSequenceDistance(dnaSeq1, dnaSeq2);
        System.out.printf("DNA序列1: %s\nDNA序列2: %s\n变异成本: %.1f\n", 
                         dnaSeq1, dnaSeq2, cost);
        
        dnaSeq1 = "AATGCCGAT";
        dnaSeq2 = "AAGGCCGAT"; // 嘌呤替换 (T->G，成本较低)
        cost = dnaSequenceDistance(dnaSeq1, dnaSeq2);
        System.out.printf("DNA序列1: %s\nDNA序列2: %s\n嘌呤替换成本: %.1f\n", 
                         dnaSeq1, dnaSeq2, cost);
    }
    
    private static void testKeyboardAwareCorrection() {
        System.out.println("\n====== 键盘感知拼写校正测试 ======");
        
        // 常见拼写错误
        String[][] commonMisspellings = {
            {"acress", "actress"},
            {"beleive", "believe"},
            {"collegue", "colleague"},
            {"definately", "definitely"},
            {"develope", "develop"}
        };
        
        for (String[] test : commonMisspellings) {
            int cost = keyboardAwareDistance(test[0], test[1]);
            System.out.printf("输入: %-12s 正确: %-12s 成本: %d\n", 
                            test[0], test[1], cost);
        }
        
        // 键盘距离测试
        String[][] keyboardTests = {
            {"qindow", "window"}, // q与w相邻
            {"hellp", "hello"},   // p与o相邻
            {"tezt", "test"},     // z与s较远
            {"kustard", "mustard"} // k与m较远
        };
        
        System.out.println("\n--- 键盘距离影响测试 ---");
        for (String[] test : keyboardTests) {
            int cost = keyboardAwareDistance(test[0], test[1]);
            System.out.printf("输入: %-8s 正确: %-8s 成本: %d\n", 
                            test[0], test[1], cost);
        }
    }
}