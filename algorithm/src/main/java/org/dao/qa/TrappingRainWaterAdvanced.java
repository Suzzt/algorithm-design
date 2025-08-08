package main.java.org.dao.qa;

import java.util.*;

/**
 * 接雨水问题
 * 
 * <p><b>问题描述</b>:
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，
 * 下雨之后能够接多少雨水。每个柱子的宽度为1。
 * 
 * <p><b>示例</b>:
 * 输入: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 * 解释: 上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，
 * 在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第42题)
 * 
 * <p><b>解题思路</b>:
 * 1. 双指针法: 从两端向中间移动，维护左右最大高度，时间O(n)，空间O(1)
 * 2. 动态规划法: 预计算每个位置的左右最大高度，时间O(n)，空间O(n)
 * 3. 单调栈法: 使用栈维护递减序列，遇到更高柱子时计算面积，时间O(n)，空间O(n)
 * 4. 暴力法: 对每个位置分别计算左右最大值，时间O(n²)，空间O(1)
 * 
 * <p><b>时间复杂度</b>:
 *  双指针法: O(n)
 *  动态规划法: O(n)
 *  单调栈法: O(n)
 *  暴力法: O(n²)
 * 
 * <p><b>空间复杂度</b>:
 *  双指针法: O(1)
 *  动态规划法: O(n)
 *  单调栈法: O(n)
 *  暴力法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 城市排水系统设计
 * 2. 建筑雨水收集系统
 * 3. 地形分析和水文建模
 * 4. 游戏开发中的物理模拟
 * 5. 图像处理中的凹凸检测
 */

public class TrappingRainWaterAdvanced {
    
    // 雨水计算结果类
    public static class RainWaterResult {
        public int totalWater;
        public int[] waterLevels;  // 每个位置的水位高度
        public int maxHeight;      // 最高柱子高度
        public String visualization; // 可视化字符串
        
        public RainWaterResult(int totalWater, int[] waterLevels, int maxHeight, String visualization) {
            this.totalWater = totalWater;
            this.waterLevels = waterLevels;
            this.maxHeight = maxHeight;
            this.visualization = visualization;
        }
        
        @Override
        public String toString() {
            return String.format("总雨水量: %d, 最高柱子: %d, 水位分布: %s", 
                               totalWater, maxHeight, Arrays.toString(waterLevels));
        }
    }
    
    // ========================= 解法1: 双指针法 (推荐) =========================
    
    /**
     * 双指针法 - 最优解法
     * 核心思想：从两端向中间移动，维护左右最大高度
     * 当左指针对应的高度小于右指针时，移动左指针（因为右边一定存在更高的柱子）
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水总量
     */
    public static int trapTwoPointers(int[] height) {
        if (height == null || height.length <= 2) {
            return 0;
        }
        
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int totalWater = 0;
        
        while (left < right) {
            if (height[left] < height[right]) {
                // 处理左边
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    totalWater += leftMax - height[left];
                }
                left++;
            } else {
                // 处理右边
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    totalWater += rightMax - height[right];
                }
                right--;
            }
        }
        
        return totalWater;
    }
    
    /**
     * 双指针法 - 带详细信息
     * 
     * @param height 柱子高度数组
     * @return 包含详细信息的结果
     */
    public static RainWaterResult trapTwoPointersDetailed(int[] height) {
        if (height == null || height.length <= 2) {
            return new RainWaterResult(0, new int[0], 0, "数组长度不足");
        }
        
        int n = height.length;
        int[] waterLevels = new int[n];
        int left = 0, right = n - 1;
        int leftMax = 0, rightMax = 0;
        int totalWater = 0;
        
        // 先复制原始高度
        for (int i = 0; i < n; i++) {
            waterLevels[i] = height[i];
        }
        
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    int water = leftMax - height[left];
                    totalWater += water;
                    waterLevels[left] = leftMax;  // 记录水位高度
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    int water = rightMax - height[right];
                    totalWater += water;
                    waterLevels[right] = rightMax;  // 记录水位高度
                }
                right--;
            }
        }
        
        int maxHeight = 0;
        for (int h : height) {
            maxHeight = Math.max(maxHeight, h);
        }
        
        String visualization = generateVisualization(height, waterLevels);
        
        return new RainWaterResult(totalWater, waterLevels, maxHeight, visualization);
    }
    
    // ========================= 解法2: 动态规划法 =========================
    
    /**
     * 动态规划法
     * 预计算每个位置的左侧最大高度和右侧最大高度
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水总量
     */
    public static int trapDynamicProgramming(int[] height) {
        if (height == null || height.length <= 2) {
            return 0;
        }
        
        int n = height.length;
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];
        
        // 计算左侧最大高度
        leftMax[0] = height[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], height[i]);
        }
        
        // 计算右侧最大高度
        rightMax[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], height[i]);
        }
        
        // 计算雨水
        int totalWater = 0;
        for (int i = 0; i < n; i++) {
            int waterLevel = Math.min(leftMax[i], rightMax[i]);
            if (waterLevel > height[i]) {
                totalWater += waterLevel - height[i];
            }
        }
        
        return totalWater;
    }
    
    // ========================= 解法3: 单调栈法 =========================
    
    /**
     * 单调栈法
     * 维护一个递减的单调栈，遇到更高的柱子时计算能接的雨水
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水总量
     */
    public static int trapMonotonicStack(int[] height) {
        if (height == null || height.length <= 2) {
            return 0;
        }
        
        Stack<Integer> stack = new Stack<Integer>();
        int totalWater = 0;
        
        for (int i = 0; i < height.length; i++) {
            // 当前柱子高度大于栈顶柱子时，可以形成凹槽
            while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                int bottom = stack.pop(); // 凹槽底部
                
                if (stack.isEmpty()) {
                    break; // 没有左边界，无法接水
                }
                
                int left = stack.peek(); // 左边界
                int right = i; // 右边界
                
                // 计算水的高度和宽度
                int waterHeight = Math.min(height[left], height[right]) - height[bottom];
                int waterWidth = right - left - 1;
                
                totalWater += waterHeight * waterWidth;
            }
            
            stack.push(i);
        }
        
        return totalWater;
    }
    
    /**
     * 单调栈法 - 带计算过程
     * 
     * @param height 柱子高度数组
     * @return 包含计算过程的详细信息
     */
    public static List<String> trapMonotonicStackWithProcess(int[] height) {
        List<String> process = new ArrayList<String>();
        
        if (height == null || height.length <= 2) {
            process.add("数组长度不足，无法接水");
            return process;
        }
        
        Stack<Integer> stack = new Stack<Integer>();
        int totalWater = 0;
        process.add("开始单调栈处理，栈维护递减序列");
        
        for (int i = 0; i < height.length; i++) {
            process.add(String.format("\n处理位置 %d，高度 %d", i, height[i]));
            
            while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                int bottom = stack.pop();
                process.add(String.format("  发现凹槽，底部位置 %d，高度 %d", bottom, height[bottom]));
                
                if (stack.isEmpty()) {
                    process.add("  缺少左边界，无法接水");
                    break;
                }
                
                int left = stack.peek();
                int right = i;
                
                int waterHeight = Math.min(height[left], height[right]) - height[bottom];
                int waterWidth = right - left - 1;
                int waterVolume = waterHeight * waterWidth;
                
                totalWater += waterVolume;
                
                process.add(String.format("  左边界位置 %d(高度%d)，右边界位置 %d(高度%d)", 
                                        left, height[left], right, height[right]));
                process.add(String.format("  水位高度 %d，宽度 %d，接水量 %d，累计接水量 %d", 
                                        waterHeight, waterWidth, waterVolume, totalWater));
            }
            
            stack.push(i);
            process.add(String.format("  位置 %d 入栈，当前栈: %s", i, stack.toString()));
        }
        
        process.add(String.format("\n最终接水总量: %d", totalWater));
        return process;
    }
    
    // ========================= 解法4: 暴力法 =========================
    
    /**
     * 暴力法 - 对每个位置分别计算左右最大值
     * 
     * @param height 柱子高度数组
     * @return 能接的雨水总量
     */
    public static int trapBruteForce(int[] height) {
        if (height == null || height.length <= 2) {
            return 0;
        }
        
        int totalWater = 0;
        
        for (int i = 1; i < height.length - 1; i++) {
            // 找左边最大值
            int leftMax = 0;
            for (int j = 0; j <= i; j++) {
                leftMax = Math.max(leftMax, height[j]);
            }
            
            // 找右边最大值
            int rightMax = 0;
            for (int j = i; j < height.length; j++) {
                rightMax = Math.max(rightMax, height[j]);
            }
            
            // 计算当前位置能接的水
            int waterLevel = Math.min(leftMax, rightMax);
            if (waterLevel > height[i]) {
                totalWater += waterLevel - height[i];
            }
        }
        
        return totalWater;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 城市排水系统设计
     * 计算不同降雨量下的积水情况
     * 
     * @param terrain 地形高度数组
     * @param rainfallIntensity 降雨强度（每单位时间的降雨量）
     * @param drainageCapacity 排水能力（每单位时间的排水量）
     * @return 积水分布情况
     */
    public static Map<String, Object> designDrainageSystem(int[] terrain, 
                                                          double rainfallIntensity, 
                                                          double drainageCapacity) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        if (terrain == null || terrain.length == 0) {
            result.put("error", "地形数据为空");
            return result;
        }
        
        // 基础积水计算
        RainWaterResult basicResult = trapTwoPointersDetailed(terrain);
        
        // 考虑降雨强度的调整
        double adjustmentFactor = rainfallIntensity / 10.0; // 标准降雨强度为10
        int adjustedWater = (int) (basicResult.totalWater * adjustmentFactor);
        
        // 考虑排水系统的影响
        double netWater = Math.max(0, adjustedWater - drainageCapacity * terrain.length);
        
        result.put("basicWaterVolume", basicResult.totalWater);
        result.put("adjustedWaterVolume", adjustedWater);
        result.put("netWaterVolume", netWater);
        result.put("rainfallIntensity", rainfallIntensity);
        result.put("drainageCapacity", drainageCapacity);
        result.put("terrainProfile", Arrays.toString(terrain));
        result.put("waterLevels", Arrays.toString(basicResult.waterLevels));
        result.put("riskLevel", netWater > adjustedWater * 0.8 ? "高风险" : 
                               netWater > adjustedWater * 0.5 ? "中风险" : "低风险");
        
        return result;
    }
    
    /**
     * 建筑雨水收集系统
     * 计算屋顶雨水收集量
     * 
     * @param roofProfile 屋顶轮廓高度
     * @param gutterCapacity 排水沟容量
     * @return 雨水收集分析结果
     */
    public static Map<String, Object> rainWaterHarvesting(int[] roofProfile, int gutterCapacity) {
        Map<String, Object> result = new HashMap<String, Object>();
        
        if (roofProfile == null || roofProfile.length == 0) {
            result.put("error", "屋顶轮廓数据为空");
            return result;
        }
        
        RainWaterResult harvestResult = trapTwoPointersDetailed(roofProfile);
        
        // 计算实际可收集量（考虑排水沟容量限制）
        int[] actualCollection = new int[roofProfile.length];
        int totalCollected = 0;
        
        for (int i = 0; i < roofProfile.length; i++) {
            int maxCollectable = harvestResult.waterLevels[i] - roofProfile[i];
            actualCollection[i] = Math.min(maxCollectable, gutterCapacity);
            totalCollected += actualCollection[i];
        }
        
        result.put("theoreticalCollection", harvestResult.totalWater);
        result.put("actualCollection", totalCollected);
        result.put("collectionEfficiency", totalCollected * 100.0 / harvestResult.totalWater);
        result.put("gutterUtilization", totalCollected * 100.0 / (gutterCapacity * roofProfile.length));
        result.put("roofProfile", Arrays.toString(roofProfile));
        result.put("actualCollectionProfile", Arrays.toString(actualCollection));
        
        return result;
    }
    
    /**
     * 游戏物理模拟 - 液体流动
     * 模拟液体在不平整表面上的分布
     * 
     * @param surface 表面高度数组
     * @param liquidVolume 液体总量
     * @return 液体分布结果
     */
    public static int[] simulateLiquidDistribution(int[] surface, int liquidVolume) {
        if (surface == null || surface.length == 0 || liquidVolume <= 0) {
            return new int[0];
        }
        
        int[] liquidLevels = new int[surface.length];
        
        // 先计算理论最大容量
        RainWaterResult capacity = trapTwoPointersDetailed(surface);
        
        if (liquidVolume <= capacity.totalWater) {
            // 液体量不足以填满所有凹槽，需要按比例分配
            double fillRatio = (double) liquidVolume / capacity.totalWater;
            
            for (int i = 0; i < surface.length; i++) {
                int theoreticalWater = capacity.waterLevels[i] - surface[i];
                liquidLevels[i] = surface[i] + (int) (theoreticalWater * fillRatio);
            }
        } else {
            // 液体量超过容量，所有凹槽都被填满
            for (int i = 0; i < surface.length; i++) {
                liquidLevels[i] = capacity.waterLevels[i];
            }
            
            // 剩余液体平均分布在表面
            int remainingLiquid = liquidVolume - capacity.totalWater;
            int averageIncrease = remainingLiquid / surface.length;
            
            for (int i = 0; i < surface.length; i++) {
                liquidLevels[i] += averageIncrease;
            }
        }
        
        return liquidLevels;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 生成ASCII可视化图
     * 
     * @param height 柱子高度数组
     * @param waterLevels 水位高度数组
     * @return ASCII艺术字符串
     */
    private static String generateVisualization(int[] height, int[] waterLevels) {
        if (height == null || height.length == 0) {
            return "无数据";
        }
        
        int maxHeight = 0;
        for (int i = 0; i < height.length; i++) {
            maxHeight = Math.max(maxHeight, Math.max(height[i], waterLevels[i]));
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        
        // 从上到下绘制
        for (int level = maxHeight; level > 0; level--) {
            for (int i = 0; i < height.length; i++) {
                if (height[i] >= level) {
                    sb.append("█"); // 柱子
                } else if (waterLevels[i] >= level) {
                    sb.append("~"); // 水
                } else {
                    sb.append(" "); // 空气
                }
            }
            sb.append("\n");
        }
        
        // 底部标尺
        for (int i = 0; i < height.length; i++) {
            sb.append("-");
        }
        sb.append("\n");
        
        // 索引
        for (int i = 0; i < height.length; i++) {
            sb.append(i % 10);
        }
        
        return sb.toString();
    }
    
    /**
     * 详细的可视化输出
     * 
     * @param height 柱子高度数组
     */
    public static void visualizeDetailed(int[] height) {
        System.out.println("\n接雨水详细可视化:");
        System.out.println("输入数组: " + Arrays.toString(height));
        
        if (height == null || height.length <= 2) {
            System.out.println("数组长度不足，无法接水");
            return;
        }
        
        RainWaterResult result = trapTwoPointersDetailed(height);
        
        System.out.println("总接水量: " + result.totalWater);
        System.out.println("水位分布: " + Arrays.toString(result.waterLevels));
        System.out.println("\n可视化图形:");
        System.out.println(result.visualization);
        
        System.out.println("\n图例:");
        System.out.println("█ - 柱子");
        System.out.println("~ - 雨水");
        System.out.println("  - 空气");
        
        // 显示每个位置的详细信息
        System.out.println("\n位置详情:");
        System.out.println("索引 | 柱高 | 水位 | 接水量");
        System.out.println("-----|------|------|-------");
        
        for (int i = 0; i < height.length; i++) {
            int water = result.waterLevels[i] - height[i];
            System.out.printf("%4d | %4d | %4d | %5d\n", i, height[i], result.waterLevels[i], water);
        }
    }
    
    /**
     * 可视化双指针过程
     * 
     * @param height 柱子高度数组
     */
    public static void visualizeTwoPointersProcess(int[] height) {
        System.out.println("\n双指针法过程可视化:");
        System.out.println("数组: " + Arrays.toString(height));
        
        if (height == null || height.length <= 2) {
            System.out.println("数组长度不足");
            return;
        }
        
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int totalWater = 0;
        int step = 1;
        
        System.out.println("\n步骤 | 左指针 | 右指针 | 左最大 | 右最大 | 当前操作 | 接水量 | 累计");
        System.out.println("-----|--------|--------|--------|--------|----------|--------|-------");
        
        while (left < right) {
            String operation;
            int currentWater = 0;
            
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                    operation = String.format("更新左最大至%d", leftMax);
                } else {
                    currentWater = leftMax - height[left];
                    totalWater += currentWater;
                    operation = String.format("左侧接水%d", currentWater);
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                    operation = String.format("更新右最大至%d", rightMax);
                } else {
                    currentWater = rightMax - height[right];
                    totalWater += currentWater;
                    operation = String.format("右侧接水%d", currentWater);
                }
                right--;
            }
            
            System.out.printf("%4d | %6d | %6d | %6d | %6d | %8s | %6d | %5d\n",
                             step++, left, right, leftMax, rightMax, operation, currentWater, totalWater);
        }
        
        System.out.println("\n最终接水总量: " + totalWater);
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param size 数组大小
     * @param maxHeight 最大柱子高度
     */
    public static void comparePerformance(int size, int maxHeight) {
        // 生成测试数据
        int[] height = generateRandomHeights(size, maxHeight);
        
        long start, end;
        
        // 测试双指针法
        start = System.nanoTime();
        int result1 = trapTwoPointers(height);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试动态规划法
        start = System.nanoTime();
        int result2 = trapDynamicProgramming(height);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试单调栈法
        start = System.nanoTime();
        int result3 = trapMonotonicStack(height);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 小数组才测试暴力法
        long time4 = -1;
        int result4 = -1;
        if (size <= 1000) {
            start = System.nanoTime();
            result4 = trapBruteForce(height);
            end = System.nanoTime();
            time4 = end - start;
        }
        
        System.out.println("\n性能比较:");
        System.out.printf("数组大小: %d, 最大高度: %d\n", size, maxHeight);
        System.out.println("===============================================");
        System.out.println("方法            | 时间(ms)   | 结果 | 相对速度");
        System.out.println("----------------|------------|------|----------");
        System.out.printf("双指针法        | %.6f | %4d | 基准\n", time1 / 1_000_000.0, result1);
        System.out.printf("动态规划法      | %.6f | %4d | %.2fx\n", time2 / 1_000_000.0, result2, (double)time2/time1);
        System.out.printf("单调栈法        | %.6f | %4d | %.2fx\n", time3 / 1_000_000.0, result3, (double)time3/time1);
        
        if (time4 > 0) {
            System.out.printf("暴力法          | %.6f | %4d | %.2fx\n", time4 / 1_000_000.0, result4, (double)time4/time1);
        }
        
        System.out.println("===============================================");
        
        // 验证结果一致性
        boolean consistent = (result1 == result2) && (result2 == result3) && 
                           (time4 <= 0 || result1 == result4);
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
    }
    
    // 生成随机高度数组
    private static int[] generateRandomHeights(int size, int maxHeight) {
        Random rand = new Random();
        int[] heights = new int[size];
        
        for (int i = 0; i < size; i++) {
            heights[i] = rand.nextInt(maxHeight + 1);
        }
        
        return heights;
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
        testCase("经典示例", new int[]{0,1,0,2,1,0,1,3,2,1,2,1}, 6);
        testCase("简单示例", new int[]{3,0,2,0,4}, 7);
        testCase("递增序列", new int[]{1,2,3,4,5}, 0);
        testCase("递减序列", new int[]{5,4,3,2,1}, 0);
        testCase("V字形", new int[]{3,0,3}, 3);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("空数组", new int[]{}, 0);
        testCase("单元素", new int[]{5}, 0);
        testCase("两元素", new int[]{3,1}, 0);
        testCase("全零", new int[]{0,0,0,0}, 0);
        testCase("相同高度", new int[]{3,3,3,3}, 0);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 城市排水系统
        System.out.println("\n城市排水系统设计:");
        int[] terrain = {2,0,2,1,0,1,3,1,0,2};
        double rainfall = 15.0;
        double drainage = 2.0;
        Map<String, Object> drainageResult = designDrainageSystem(terrain, rainfall, drainage);
        System.out.println("地形轮廓: " + Arrays.toString(terrain));
        System.out.println("排水系统分析: " + drainageResult);
        
        // 场景2: 建筑雨水收集
        System.out.println("\n建筑雨水收集系统:");
        int[] roof = {3,1,2,0,1,2,4,1,2};
        int gutterCap = 2;
        Map<String, Object> harvestResult = rainWaterHarvesting(roof, gutterCap);
        System.out.println("屋顶轮廓: " + Arrays.toString(roof));
        System.out.println("收集系统分析: " + harvestResult);
        
        // 场景3: 游戏液体模拟
        System.out.println("\n游戏液体物理模拟:");
        int[] surface = {4,1,3,0,2,1,4};
        int liquidVol = 10;
        int[] liquidDist = simulateLiquidDistribution(surface, liquidVol);
        System.out.println("表面轮廓: " + Arrays.toString(surface));
        System.out.println("液体总量: " + liquidVol);
        System.out.println("液体分布: " + Arrays.toString(liquidDist));
        
        // 场景4: 单调栈处理过程
        System.out.println("\n单调栈处理过程:");
        int[] stackDemo = {3,0,2,0,4};
        List<String> stackProcess = trapMonotonicStackWithProcess(stackDemo);
        for (String step : stackProcess) {
            System.out.println(step);
        }
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        int[] demoArray = {0,1,0,2,1,0,1,3,2,1,2,1};
        visualizeDetailed(demoArray);
        visualizeTwoPointersProcess(new int[]{3,0,2,0,4});
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100, 10);
        comparePerformance(1000, 20);
        comparePerformance(10000, 50);
    }
    
    private static void testCase(String name, int[] height, int expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("高度数组: " + Arrays.toString(height));
        
        int result1 = trapTwoPointers(height);
        int result2 = trapDynamicProgramming(height);
        int result3 = trapMonotonicStack(height);
        int result4 = trapBruteForce(height);
        
        System.out.println("双指针法结果: " + result1);
        System.out.println("动态规划法结果: " + result2);
        System.out.println("单调栈法结果: " + result3);
        System.out.println("暴力法结果: " + result4);
        System.out.println("期望结果: " + expected);
        
        boolean isCorrect = (result1 == expected) && (result2 == expected) && 
                           (result3 == expected) && (result4 == expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小数组展示可视化
        if (height.length <= 15 && height.length > 0) {
            visualizeDetailed(height);
        }
    }
}