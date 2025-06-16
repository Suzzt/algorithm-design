package main.java.org.dao.qa;

/**
 * 接雨水问题 - 计算地形可以容纳的雨水量
 * 
 * <p><b>问题描述</b>:
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子下雨之后能接多少雨水。
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第42题)
 * 
 * <p><b>示例</b>:
 * 输入: [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 * 解释: 蓝色部分表示雨水
 * 
 * <p><b>解题思路</b>:
 * 1. 动态规划(DP)解法: 
 *    - 计算每个位置的左侧最高柱子和右侧最高柱子
 *    - 每个位置可接雨水 = min(左侧最高, 右侧最高) - 当前高度
 * 2. 双指针解法(优化空间):
 *    - 使用两个指针从两边向中间移动
 *    - 实时维护左右两侧的最大值
 *    - 每次处理较低一侧的指针
 * 
 * <p><b>时间复杂度</b>: 
 *   DP解法: O(n) 
 *   双指针解法: O(n)
 *   
 * <p><b>空间复杂度</b>:
 *   DP解法: O(n) 
 *   双指针解法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 建筑与城市规划(雨水收集系统设计)
 * 2. 地理信息系统(GIS)地形分析
 * 3. 游戏开发中的地形模拟
 * 4. 水利工程设计
 * 5. 环境保护(洪水预测)
 */

public class TrappingRainWater {

    /**
     * 动态规划解法
     */
    public static int trapDP(int[] height) {
        if (height == null || height.length == 0) return 0;
        
        int n = height.length;
        int[] leftMax = new int[n];   // 存储每个位置左侧的最大高度
        int[] rightMax = new int[n];  // 存储每个位置右侧的最大高度
        
        // 从左向右计算左侧最大值
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }
        
        // 从右向左计算右侧最大值
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }
        
        // 计算总雨水量
        int water = 0;
        for (int i = 0; i < n; i++) {
            int minHeight = Math.min(leftMax[i], rightMax[i]);
            water += minHeight - height[i];
        }
        
        return water;
    }
    
    /**
     * 双指针解法(空间优化)
     */
    public static int trapTwoPointers(int[] height) {
        if (height == null || height.length == 0) return 0;
        
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int water = 0;
        
        while (left < right) {
            // 处理左侧指针
            if (height[left] <= height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left]; // 更新左侧最大值
                } else {
                    water += leftMax - height[left]; // 累加雨水
                }
                left++;
            } 
            // 处理右侧指针
            else {
                if (height[right] >= rightMax) {
                    rightMax = height[right]; // 更新右侧最大值
                } else {
                    water += rightMax - height[right]; // 累加雨水
                }
                right--;
            }
        }
        
        return water;
    }
    
    /**
     * 可视化地形和雨水分布
     */
    public static void visualizeTerrain(int[] height) {
        int max = 0;
        for (int h : height) {
            max = Math.max(max, h);
        }
        
        for (int level = max; level >= 0; level--) {
            for (int i = 0; i < height.length; i++) {
                if (height[i] >= level) {
                    System.out.print("██"); // 柱子
                } else if (level <= Math.min(
                    getLeftMax(height, i), 
                    getRightMax(height, i))
                ) {
                    System.out.print("⛆⛆"); // 雨水
                } else {
                    System.out.print("  "); // 空位
                }
            }
            System.out.println();
        }
    }
    
    private static int getLeftMax(int[] height, int index) {
        int max = 0;
        for (int i = 0; i < index; i++) {
            max = Math.max(max, height[i]);
        }
        return max;
    }
    
    private static int getRightMax(int[] height, int index) {
        int max = 0;
        for (int i = index + 1; i < height.length; i++) {
            max = Math.max(max, height[i]);
        }
        return max;
    }
    
    /**
     * 测试用例和主函数
     */
    public static void main(String[] args) {
        int[][] testCases = {
            {0,1,0,2,1,0,1,3,2,1,2,1}, // 6
            {4,2,0,3,2,5},              // 9
            {5,4,1,2},                  // 1
            {0,0,0,0},                  // 0
            {7,0,5,0,4},                // 9
            {1,2,3,4,5,4,3,2,1},       // 0
            {5,0,6,0,3,0,4},           // 14
            {6,4,2,0,5,3,1,7}          // 17
        };
        
        int[] expected = {6, 9, 1, 0, 9, 0, 14, 17};
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n====== 测试" + (i+1) + " ======");
            System.out.println("输入地形: " + arrayToString(testCases[i]));
            
            // 两种解法验证
            int resultDP = trapDP(testCases[i]);
            int resultTP = trapTwoPointers(testCases[i]);
            
            System.out.println("DP解法结果: " + resultDP);
            System.out.println("双指针解法结果: " + resultTP);
            System.out.println("预期结果: " + expected[i]);
            
            boolean pass = (resultDP == expected[i]) && (resultTP == expected[i]);
            System.out.println("状态: " + (pass ? "通过 ✅" : "失败 ❌"));
            
            // 可视化地形
            System.out.println("\n地形可视化:");
            visualizeTerrain(testCases[i]);
        }
    }
    
    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}