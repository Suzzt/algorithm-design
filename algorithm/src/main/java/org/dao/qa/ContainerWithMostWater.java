package main.java.org.dao.qa;

import java.util.Arrays;

/**
 * 盛水最多的容器解决方案
 * 
 * <p><b>问题描述：</b>
 * 给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * 返回容器可以储存的最大水量。
 * 
 * <p><b>示例：</b>
 * 输入: [1,8,6,2,5,4,8,3,7]
 * 输出: 49
 * 解释: 在此情况下，容器能够容纳水的最大值为 49（即 8 和 7 两条线，间隔为7，所以面积=7 * 7=49）
 */
public class ContainerWithMostWater {

    // ========================== 核心解法 ==========================
    
    /**
     * 双指针解法 (时间复杂度O(n)，空间复杂度O(1))
     * 
     * @param height 高度数组
     * @return 最大储水量
     */
    public static int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        
        while (left < right) {
            // 计算当前区域的面积
            int currentHeight = Math.min(height[left], height[right]);
            int currentWidth = right - left;
            int area = currentHeight * currentWidth;
            maxArea = Math.max(maxArea, area);
            
            // 移动指针
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
    
    // ========================== 应用场景扩展 ==========================
    
    /**
     * 城市雨水收集系统设计
     * 
     * @param buildingHeights 建筑物高度数组
     * @return 最大雨水收集量
     */
    public static int rainWaterHarvestingSystem(int[] buildingHeights) {
        return maxArea(buildingHeights);
    }
    
    /**
     * 农田灌溉规划系统
     * 
     * @param terrain 地形高度数组
     * @return 最大灌区容量
     */
    public static int farmlandIrrigationPlanning(int[] terrain) {
        return maxArea(terrain);
    }
    
    /**
     * 水库大坝位置选择
     * 
     * @param riverBanks 河岸高度剖面
     * @return 最佳水坝位置 [左岸位置, 右岸位置]
     */
    public static int[] reservoirDamPlacement(int[] riverBanks) {
        int maxArea = 0;
        int left = 0;
        int right = riverBanks.length - 1;
        int bestLeft = 0, bestRight = riverBanks.length - 1;
        
        while (left < right) {
            int currentHeight = Math.min(riverBanks[left], riverBanks[right]);
            int currentWidth = right - left;
            int area = currentHeight * currentWidth;
            
            if (area > maxArea) {
                maxArea = area;
                bestLeft = left;
                bestRight = right;
            }
            
            if (riverBanks[left] < riverBanks[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return new int[]{bestLeft, bestRight};
    }
    
    /**
     * 物流货物装载计算器
     * 
     * @param containerWalls 集装箱壁高数组
     * @return 最大装载容量
     */
    public static int cargoLoadingOptimizer(int[] containerWalls) {
        return maxArea(containerWalls);
    }
    
    // ========================== 测试方法 ==========================
    
    public static void main(String[] args) {
        testAlgorithm();
        testPerformance();
        testApplicationScenarios();
    }
    
    private static void testAlgorithm() {
        System.out.println("===== 算法测试 =====");
        int[] height = {1,8,6,2,5,4,8,3,7};
        int result = maxArea(height);
        System.out.println("输入: [1,8,6,2,5,4,8,3,7]");
        System.out.println("输出: " + result + " (预期: 49)");
        
        // 更多测试用例
        int[][] testCases = {
            {1,1},           // 预期: 1
            {4,3,2,1,4},     // 预期: 16
            {1,2,1},         // 预期: 2
            {1,8,100,2,100,4,8,3,7} // 预期: 400
        };
        
        for (int[] test : testCases) {
            int area = maxArea(test);
            System.out.printf("输入: %s → 输出: %d%n", 
                              Arrays.toString(test), area);
        }
    }
    
    private static void testPerformance() {
        System.out.println("\n===== 性能测试 =====");
        // 生成大型测试数据（100万个随机高度）
        int[] largeHeightArray = new int[1000000];
        for (int i = 0; i < largeHeightArray.length; i++) {
            largeHeightArray[i] = (int)(Math.random() * 1000);
        }
        
        long startTime = System.nanoTime();
        int result = maxArea(largeHeightArray);
        long duration = (System.nanoTime() - startTime) / 1000000; // 毫秒
        
        System.out.println("百万数据量用时: " + duration + "ms");
        System.out.println("最大面积: " + result);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n===== 应用场景测试 =====");
        
        // 场景1: 城市雨水收集系统
        System.out.println("1. 城市雨水收集系统:");
        int[] buildings = {2, 10, 5, 8, 7, 3, 12, 4};
        int maxWater = rainWaterHarvestingSystem(buildings);
        System.out.println("建筑物高度: " + Arrays.toString(buildings));
        System.out.println("最大收集水量: " + maxWater);
        
        // 场景2: 农田灌溉规划
        System.out.println("\n2. 农田灌溉规划:");
        int[] farmland = {5, 3, 8, 6, 4, 2, 7, 1};
        int irrigationCapacity = farmlandIrrigationPlanning(farmland);
        System.out.println("地形高度: " + Arrays.toString(farmland));
        System.out.println("最大灌区容量: " + irrigationCapacity);
        
        // 场景3: 水库大坝位置选择
        System.out.println("\n3. 水库大坝位置选择:");
        int[] riverBanks = {3, 8, 6, 7, 4, 5, 9, 2};
        int[] damPositions = reservoirDamPlacement(riverBanks);
        System.out.println("河岸高度: " + Arrays.toString(riverBanks));
        System.out.println("最佳水坝位置: 左岸" + damPositions[0] + ", 右岸" + damPositions[1]);
        System.out.println("最大水库容量: " + maxArea(riverBanks));
        
        // 场景4: 物流货物装载
        System.out.println("\n4. 物流货物装载:");
        int[] container = {5, 2, 10, 8, 6, 4};
        int maxCargo = cargoLoadingOptimizer(container);
        System.out.println("集装箱壁高: " + Arrays.toString(container));
        System.out.println("最大装载容量: " + maxCargo);
    }
}