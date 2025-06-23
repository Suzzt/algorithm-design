package main.java.org.dao.qa;

import java.util.*;

/**
 * 接雨水问题 (Trapping Rain Water)
 * 
 * <p><b>问题描述</b>:
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * 
 * <p><b>示例</b>:
 * 输入: [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 * 解释: 上述高度图上由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示，
 *       在该情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第42题)
 * 
 * <p><b>解题思路</b>:
 * 1. 双指针法（最优解）: 左右指针向中间移动，同时记录左右最大高度，计算每个位置的蓄水量
 * 2. 动态规划: 分别计算每个位置的左右最大高度，然后计算总蓄水量
 * 3. 单调栈: 使用栈存储索引，计算凹槽的蓄水量
 * 
 * <p><b>时间复杂度</b>: O(n) - n为柱子数量
 * <p><b>空间复杂度</b>: O(1) - 双指针法；O(n) - 动态规划/单调栈
 * 
 * <p><b>应用场景</b>:
 * 1. 城市排水系统设计
 * 2. 地形蓄水分析
 * 3. 建筑规划中的雨水收集系统
 * 4. 游戏地形生成（如Minecraft中的雨水收集）
 * 5. 地理信息系统(GIS)分析
 */

public class TrappingRainWater {

    // ========================= 解法1: 双指针法 =========================
    
    /**
     * 双指针法 - 空间复杂度O(1)的最优解
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水量
     */
    public static int trapTwoPointers(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }
        
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int water = 0;
        
        while (left < right) {
            // 更新左右最大高度
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            
            // 总是移动高度较小一侧的指针
            if (height[left] < height[right]) {
                // 左指针位置的蓄水量取决于leftMax
                water += leftMax - height[left];
                left++;
            } else {
                // 右指针位置的蓄水量取决于rightMax
                water += rightMax - height[right];
                right--;
            }
        }
        
        return water;
    }
    
    // ========================= 解法2: 动态规划 =========================
    
    /**
     * 动态规划解法 - 更容易理解
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水量
     */
    public static int trapDynamicProgramming(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }
        
        int n = height.length;
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];
        
        // 从左向右计算左侧最大值
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i-1], height[i]);
        }
        
        // 从右向左计算右侧最大值
        rightMax[n-1] = height[n-1];
        for (int i = n-2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i+1], height[i]);
        }
        
        // 计算每个位置的蓄水量
        int water = 0;
        for (int i = 0; i < n; i++) {
            water += Math.min(leftMax[i], rightMax[i]) - height[i];
        }
        
        return water;
    }
    
    // ========================= 解法3: 单调栈 =========================
    
    /**
     * 单调栈解法 - 另一种解题思路
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水量
     */
    public static int trapMonotonicStack(int[] height) {
        if (height == null || height.length < 3) {
            return 0;
        }
        
        int water = 0;
        int current = 0;
        Deque<Integer> stack = new ArrayDeque<>(); // 存储柱子的索引
        
        while (current < height.length) {
            // 当当前柱子高度大于栈顶柱子高度时
            while (!stack.isEmpty() && height[current] > height[stack.peek()]) {
                int bottom = stack.pop(); // 凹槽底部位置
                
                if (stack.isEmpty()) {
                    break;
                }
                
                // 计算凹槽的宽度和高度
                int leftBound = stack.peek();
                int width = current - leftBound - 1;
                int boundedHeight = Math.min(height[current], height[leftBound]) - height[bottom];
                
                // 计算当前凹槽的蓄水量
                water += width * boundedHeight;
            }
            
            stack.push(current++);
        }
        
        return water;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化雨水分布
     * 
     * @param height 柱子高度数组
     */
    public static void visualizeWater(int[] height) {
        if (height == null || height.length == 0) {
            System.out.println("无数据可展示");
            return;
        }
        
        // 找出最大高度
        int maxHeight = Arrays.stream(height).max().getAsInt();
        
        System.out.println("\n雨水分布图:");
        for (int h = maxHeight; h > 0; h--) {
            for (int i = 0; i < height.length; i++) {
                if (height[i] >= h) {
                    System.out.print("■ "); // 柱子部分
                } else {
                    // 判断当前位置是否有雨水
                    boolean hasWater = isWaterPosition(height, i, h);
                    System.out.print(hasWater ? "~ " : "  ");
                }
            }
            System.out.println();
        }
        
        // 打印底部参考线
        for (int i = 0; i < height.length; i++) {
            System.out.print("--");
        }
        System.out.println("\n");
    }
    
    // 辅助方法：判断特定高度是否可能存储雨水
    private static boolean isWaterPosition(int[] height, int index, int currentHeight) {
        // 检查左侧是否有更高的柱子
        boolean hasLeftHigher = false;
        for (int left = index - 1; left >= 0; left--) {
            if (height[left] > currentHeight) {
                hasLeftHigher = true;
                break;
            }
        }
        
        // 检查右侧是否有更高的柱子
        boolean hasRightHigher = false;
        for (int right = index + 1; right < height.length; right++) {
            if (height[right] > currentHeight) {
                hasRightHigher = true;
                break;
            }
        }
        
        return hasLeftHigher && hasRightHigher;
    }
    
    // ========================= 应用场景 =========================
    
    /**
     * 城市雨水排放规划系统
     * 
     * @param height 地形高度图
     * @return 推荐的集水池位置
     */
    public static List<Integer> recommendWaterStorage(int[] height) {
        int water = trapTwoPointers(height);
        System.out.println("预计可收集雨水总量: " + water + " 单位");
        
        // 分析最佳集水位置
        List<Integer> positions = new ArrayList<>();
        int n = height.length;
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];
        
        // 计算左侧最大高度
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i-1], height[i]);
        }
        
        // 计算右侧最大高度
        rightMax[n-1] = height[n-1];
        for (int i = n-2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i+1], height[i]);
        }
        
        // 找出集水深度最大的位置
        int maxDepth = 0;
        for (int i = 1; i < n-1; i++) {
            int depth = Math.min(leftMax[i], rightMax[i]) - height[i];
            if (depth > maxDepth) {
                maxDepth = depth;
                positions.clear();
                positions.add(i);
            } else if (depth == maxDepth) {
                positions.add(i);
            }
        }
        
        System.out.println("推荐集水池位置: " + positions);
        return positions;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testComplexCases();
        testPerformance();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        int[] height1 = {0,1,0,2,1,0,1,3,2,1,2,1}; // 标准示例 输出6
        int[] height2 = {4,2,0,3,2,5}; // 输出9
        
        System.out.println("测试1: 标准示例");
        testTrap(height1, 6);
        
        System.out.println("测试2: 凹型结构");
        testTrap(height2, 9);
        
        System.out.println("测试3: 单调递增");
        testTrap(new int[]{1,2,3,4,5}, 0);
        
        System.out.println("测试4: 单调递减");
        testTrap(new int[]{5,4,3,2,1}, 0);
        
        System.out.println("测试5: 三角型");
        testTrap(new int[]{1,0,2,0,3,0,2,0,1}, 8);
    }
    
    private static void testComplexCases() {
        System.out.println("\n====== 复杂场景测试 ======");
        int[] height1 = {5,4,3,2,1,2,3,4,5}; // 金字塔型 输出16
        int[] height2 = {3,0,0,2,0,4}; // 多个凹槽 输出10
        int[] height3 = {0,1,2,0,2,1,0}; // 波浪型 输出2
        
        System.out.println("测试1: 金字塔型");
        testTrap(height1, 16);
        
        System.out.println("测试2: 多个凹槽");
        testTrap(height2, 10);
        
        System.out.println("测试3: 波浪型");
        testTrap(height3, 2);
    }
    
    private static void testTrap(int[] height, int expected) {
        int result1 = trapTwoPointers(height);
        int result2 = trapDynamicProgramming(height);
        int result3 = trapMonotonicStack(height);
        
        System.out.println("输入: " + Arrays.toString(height));
        System.out.println("双指针法: " + result1 + 
                         " | 动态规划: " + result2 + 
                         " | 单调栈: " + result3);
        
        boolean pass = result1 == expected && result2 == expected && result3 == expected;
        System.out.println("状态: " + (pass ? "通过 ✅" : "失败 ❌"));
        
        if (height.length < 20) {
            visualizeWater(height);
            recommendWaterStorage(height);
        }
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        Random rand = new Random();
        
        // 生成大型测试数据
        int[] largeData = new int[1000000];
        for (int i = 0; i < largeData.length; i++) {
            largeData[i] = rand.nextInt(100);
        }
        
        // 双指针法测试
        long start = System.currentTimeMillis();
        int result1 = trapTwoPointers(largeData);
        long time1 = System.currentTimeMillis() - start;
        
        // 动态规划测试
        start = System.currentTimeMillis();
        int result2 = trapDynamicProgramming(largeData);
        long time2 = System.currentTimeMillis() - start;
        
        // 单调栈测试
        start = System.currentTimeMillis();
        int result3 = trapMonotonicStack(largeData);
        long time3 = System.currentTimeMillis() - start;
        
        System.out.println("测试数据长度: " + largeData.length);
        System.out.println("双指针法结果: " + result1 + " | 耗时: " + time1 + "ms");
        System.out.println("动态规划结果: " + result2 + " | 耗时: " + time2 + "ms");
        System.out.println("单调栈结果: " + result3 + " | 耗时: " + time3 + "ms");
    }
}