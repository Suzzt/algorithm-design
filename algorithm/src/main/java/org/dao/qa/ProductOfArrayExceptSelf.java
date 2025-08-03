package main.java.org.dao.qa;

import java.util.*;

/**
 * 除自身以外数组的乘积问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个整数数组 nums，返回数组 answer，其中 answer[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积。
 * 题目数据保证数组 nums 之中任意元素的全部前缀元素和后缀元素的乘积都在 32 位整数范围内。
 * 要求：不能使用除法，且算法的时间复杂度为 O(n)。
 * 
 * <p><b>示例</b>:
 * 输入: nums = [1,2,3,4]
 * 输出: [24,12,8,6]
 * 解释: 对于索引0：2*3*4=24，对于索引1：1*3*4=12，以此类推
 * 
 * 输入: nums = [-1,1,0,-3,3]
 * 输出: [0,0,9,0,0]
 * 解释: 由于数组中包含0，除了0位置外其他位置结果都是0
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第238题)
 * 
 * <p><b>解题思路</b>:
 * 1. 前缀积 + 后缀积: 两次遍历，分别计算左右两侧的乘积
 * 2. 空间优化法: 使用输出数组存储前缀积，再从右遍历计算后缀积
 * 3. 分治法: 递归计算左右两部分的乘积
 * 4. 特殊处理法: 针对包含0的情况进行优化
 * 
 * <p><b>时间复杂度</b>:
 *  前缀积解法: O(n)
 *  空间优化解法: O(n)
 *  分治法: O(n log n)
 *  特殊处理法: O(n)
 * 
 * <p><b>空间复杂度</b>:
 *  前缀积解法: O(n)
 *  空间优化解法: O(1) 不算输出数组
 *  分治法: O(log n) 递归栈
 *  特殊处理法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 股票交易策略分析
 * 2. 统计学数据处理
 * 3. 矩阵运算优化
 * 4. 概率计算和风险评估
 * 5. 游戏积分系统设计
 */

public class ProductOfArrayExceptSelf {
    
    // ========================= 解法1: 前缀积 + 后缀积 =========================
    
    /**
     * 前缀积和后缀积解法
     * 
     * @param nums 输入数组
     * @return 除自身以外的乘积数组
     */
    public static int[] productExceptSelfPrefixSuffix(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] leftProducts = new int[n];   // 前缀积数组
        int[] rightProducts = new int[n];  // 后缀积数组
        int[] result = new int[n];
        
        // 计算前缀积
        leftProducts[0] = 1;
        for (int i = 1; i < n; i++) {
            leftProducts[i] = leftProducts[i - 1] * nums[i - 1];
        }
        
        // 计算后缀积
        rightProducts[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            rightProducts[i] = rightProducts[i + 1] * nums[i + 1];
        }
        
        // 计算最终结果
        for (int i = 0; i < n; i++) {
            result[i] = leftProducts[i] * rightProducts[i];
        }
        
        return result;
    }
    
    // ========================= 解法2: 空间优化解法 (推荐) =========================
    
    /**
     * 空间优化解法 - 使用输出数组存储前缀积
     * 
     * @param nums 输入数组
     * @return 除自身以外的乘积数组
     */
    public static int[] productExceptSelfOptimized(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n];
        
        // 第一次遍历：计算前缀积存储在result中
        result[0] = 1;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }
        
        // 第二次遍历：从右到左计算后缀积并直接更新result
        int rightProduct = 1;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= rightProduct;
            rightProduct *= nums[i];
        }
        
        return result;
    }
    
    // ========================= 解法3: 分治法 =========================
    
    /**
     * 分治解法
     * 
     * @param nums 输入数组
     * @return 除自身以外的乘积数组
     */
    public static int[] productExceptSelfDivideConquer(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n];
        
        divideConquerHelper(nums, result, 0, n - 1);
        
        return result;
    }
    
    private static int divideConquerHelper(int[] nums, int[] result, int left, int right) {
        if (left == right) {
            result[left] = 1;
            return nums[left];
        }
        
        int mid = left + (right - left) / 2;
        
        // 递归计算左右两部分
        int leftProduct = divideConquerHelper(nums, result, left, mid);
        int rightProduct = divideConquerHelper(nums, result, mid + 1, right);
        
        // 更新结果数组
        for (int i = left; i <= mid; i++) {
            result[i] *= rightProduct;
        }
        for (int i = mid + 1; i <= right; i++) {
            result[i] *= leftProduct;
        }
        
        return leftProduct * rightProduct;
    }
    
    // ========================= 解法4: 特殊处理法 =========================
    
    /**
     * 特殊处理解法 - 针对包含0的情况优化
     * 
     * @param nums 输入数组
     * @return 除自身以外的乘积数组
     */
    public static int[] productExceptSelfSpecialCase(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n];
        
        // 统计0的个数和非0元素的乘积
        int zeroCount = 0;
        int nonZeroProduct = 1;
        int zeroIndex = -1;
        
        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                zeroCount++;
                zeroIndex = i;
            } else {
                nonZeroProduct *= nums[i];
            }
        }
        
        // 根据0的个数进行不同处理
        if (zeroCount > 1) {
            // 多个0，所有位置结果都是0
            Arrays.fill(result, 0);
        } else if (zeroCount == 1) {
            // 一个0，只有0的位置结果是非0元素的乘积，其他位置都是0
            Arrays.fill(result, 0);
            result[zeroIndex] = nonZeroProduct;
        } else {
            // 没有0，正常计算
            for (int i = 0; i < n; i++) {
                result[i] = nonZeroProduct / nums[i];
            }
        }
        
        return result;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 股票交易策略 - 计算除当日外其他日期收益率乘积
     * 
     * @param dailyReturns 每日收益率数组
     * @return 除当日外的累积收益率
     */
    public static double[] calculateOtherDaysReturn(double[] dailyReturns) {
        if (dailyReturns == null || dailyReturns.length == 0) {
            return new double[0];
        }
        
        int n = dailyReturns.length;
        double[] result = new double[n];
        
        // 计算前缀乘积
        result[0] = 1.0;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * dailyReturns[i - 1];
        }
        
        // 计算后缀乘积并更新结果
        double rightProduct = 1.0;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= rightProduct;
            rightProduct *= dailyReturns[i];
        }
        
        return result;
    }
    
    /**
     * 概率计算 - 计算除当前事件外其他事件同时发生的概率
     * 
     * @param probabilities 各事件发生概率
     * @return 除当前事件外其他事件同时发生的概率
     */
    public static double[] calculateOtherEventsProbability(double[] probabilities) {
        if (probabilities == null || probabilities.length == 0) {
            return new double[0];
        }
        
        // 检查概率值的合法性
        for (double prob : probabilities) {
            if (prob < 0.0 || prob > 1.0) {
                throw new IllegalArgumentException("概率值必须在[0,1]范围内");
            }
        }
        
        int n = probabilities.length;
        double[] result = new double[n];
        
        // 使用优化的乘积计算方法
        result[0] = 1.0;
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * probabilities[i - 1];
        }
        
        double rightProduct = 1.0;
        for (int i = n - 1; i >= 0; i--) {
            result[i] *= rightProduct;
            rightProduct *= probabilities[i];
        }
        
        return result;
    }
    
    /**
     * 游戏积分系统 - 计算除当前关卡外其他关卡积分乘积
     */
    public static class GameScoreCalculator {
        private int[] levelScores;
        private int[] productCache;
        private boolean cacheValid;
        
        public GameScoreCalculator(int[] levelScores) {
            this.levelScores = levelScores.clone();
            this.productCache = null;
            this.cacheValid = false;
        }
        
        public int[] getOtherLevelsProduct() {
            if (!cacheValid) {
                productCache = productExceptSelfOptimized(levelScores);
                cacheValid = true;
            }
            return productCache.clone();
        }
        
        public void updateLevelScore(int level, int newScore) {
            if (level >= 0 && level < levelScores.length) {
                levelScores[level] = newScore;
                cacheValid = false; // 缓存失效
            }
        }
        
        public int getTotalScoreExceptLevel(int level) {
            if (level < 0 || level >= levelScores.length) {
                return 0;
            }
            
            int[] products = getOtherLevelsProduct();
            return products[level];
        }
        
        public Map<String, Object> getGameStatistics() {
            Map<String, Object> stats = new HashMap<>();
            int[] otherProducts = getOtherLevelsProduct();
            
            int totalProduct = 1;
            for (int score : levelScores) {
                totalProduct *= score;
            }
            
            int maxOtherProduct = Arrays.stream(otherProducts).max().orElse(0);
            int minOtherProduct = Arrays.stream(otherProducts).min().orElse(0);
            
            stats.put("levelScores", levelScores.clone());
            stats.put("otherLevelsProducts", otherProducts);
            stats.put("totalProduct", totalProduct);
            stats.put("maxOtherProduct", maxOtherProduct);
            stats.put("minOtherProduct", minOtherProduct);
            
            return stats;
        }
    }
    
    /**
     * 矩阵行列乘积计算器
     */
    public static class MatrixProductCalculator {
        
        /**
         * 计算矩阵每行除当前列外其他列的乘积
         * 
         * @param matrix 输入矩阵
         * @return 每行除当前列外的乘积矩阵
         */
        public static int[][] calculateRowProductsExceptColumn(int[][] matrix) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                return new int[0][0];
            }
            
            int rows = matrix.length;
            int cols = matrix[0].length;
            int[][] result = new int[rows][cols];
            
            for (int i = 0; i < rows; i++) {
                result[i] = productExceptSelfOptimized(matrix[i]);
            }
            
            return result;
        }
        
        /**
         * 计算矩阵每列除当前行外其他行的乘积
         * 
         * @param matrix 输入矩阵
         * @return 每列除当前行外的乘积矩阵
         */
        public static int[][] calculateColumnProductsExceptRow(int[][] matrix) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                return new int[0][0];
            }
            
            int rows = matrix.length;
            int cols = matrix[0].length;
            int[][] result = new int[rows][cols];
            
            for (int j = 0; j < cols; j++) {
                // 提取第j列
                int[] column = new int[rows];
                for (int i = 0; i < rows; i++) {
                    column[i] = matrix[i][j];
                }
                
                // 计算除当前行外的乘积
                int[] columnProducts = productExceptSelfOptimized(column);
                
                // 填充结果矩阵的第j列
                for (int i = 0; i < rows; i++) {
                    result[i][j] = columnProducts[i];
                }
            }
            
            return result;
        }
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化前缀积和后缀积的计算过程
     * 
     * @param nums 输入数组
     */
    public static void visualizePrefixSuffixProcess(int[] nums) {
        if (nums == null || nums.length == 0) {
            System.out.println("输入数组为空");
            return;
        }
        
        System.out.println("\n前缀积和后缀积计算过程可视化:");
        System.out.println("输入数组: " + Arrays.toString(nums));
        
        int n = nums.length;
        int[] leftProducts = new int[n];
        int[] rightProducts = new int[n];
        
        // 计算并展示前缀积
        System.out.println("\n前缀积计算过程:");
        System.out.println("索引 | 原数组 | 前缀积 | 计算过程");
        System.out.println("-----|--------|--------|------------------");
        
        leftProducts[0] = 1;
        System.out.printf("%4d | %6d | %6d | %s\n", 0, nums[0], leftProducts[0], "初始值");
        
        for (int i = 1; i < n; i++) {
            leftProducts[i] = leftProducts[i - 1] * nums[i - 1];
            System.out.printf("%4d | %6d | %6d | %d * %d\n", 
                             i, nums[i], leftProducts[i], leftProducts[i - 1], nums[i - 1]);
        }
        
        // 计算并展示后缀积
        System.out.println("\n后缀积计算过程:");
        System.out.println("索引 | 原数组 | 后缀积 | 计算过程");
        System.out.println("-----|--------|--------|------------------");
        
        rightProducts[n - 1] = 1;
        System.out.printf("%4d | %6d | %6d | %s\n", n - 1, nums[n - 1], rightProducts[n - 1], "初始值");
        
        for (int i = n - 2; i >= 0; i--) {
            rightProducts[i] = rightProducts[i + 1] * nums[i + 1];
            System.out.printf("%4d | %6d | %6d | %d * %d\n", 
                             i, nums[i], rightProducts[i], rightProducts[i + 1], nums[i + 1]);
        }
        
        // 计算最终结果
        System.out.println("\n最终结果计算:");
        System.out.println("索引 | 前缀积 | 后缀积 | 最终结果 | 计算过程");
        System.out.println("-----|--------|--------|----------|------------------");
        
        for (int i = 0; i < n; i++) {
            int result = leftProducts[i] * rightProducts[i];
            System.out.printf("%4d | %6d | %6d | %8d | %d * %d\n", 
                             i, leftProducts[i], rightProducts[i], result, 
                             leftProducts[i], rightProducts[i]);
        }
    }
    
    /**
     * 可视化空间优化算法的过程
     * 
     * @param nums 输入数组
     */
    public static void visualizeOptimizedProcess(int[] nums) {
        if (nums == null || nums.length == 0) {
            System.out.println("输入数组为空");
            return;
        }
        
        System.out.println("\n空间优化算法过程可视化:");
        System.out.println("输入数组: " + Arrays.toString(nums));
        
        int n = nums.length;
        int[] result = new int[n];
        
        // 第一次遍历：前缀积
        System.out.println("\n第一次遍历 - 计算前缀积:");
        System.out.println("索引 | 原数组 | 结果数组 | 计算过程");
        System.out.println("-----|--------|----------|------------------");
        
        result[0] = 1;
        System.out.printf("%4d | %6d | %8d | %s\n", 0, nums[0], result[0], "初始值");
        
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
            System.out.printf("%4d | %6d | %8d | %d * %d\n", 
                             i, nums[i], result[i], result[i - 1], nums[i - 1]);
        }
        
        // 第二次遍历：计算后缀积并更新结果
        System.out.println("\n第二次遍历 - 计算后缀积并更新结果:");
        System.out.println("索引 | 原数组 | 前状态 | 后缀积 | 最终结果 | 计算过程");
        System.out.println("-----|--------|--------|--------|----------|------------------");
        
        int rightProduct = 1;
        for (int i = n - 1; i >= 0; i--) {
            int prevResult = result[i];
            result[i] *= rightProduct;
            System.out.printf("%4d | %6d | %6d | %6d | %8d | %d * %d\n", 
                             i, nums[i], prevResult, rightProduct, result[i], 
                             prevResult, rightProduct);
            rightProduct *= nums[i];
        }
        
        System.out.println("\n最终结果: " + Arrays.toString(result));
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param nums 测试数组
     */
    public static void comparePerformance(int[] nums) {
        System.out.println("\n性能比较:");
        System.out.println("数组长度: " + nums.length);
        System.out.println("===============================================");
        
        long start, end;
        
        // 测试前缀积+后缀积解法
        start = System.nanoTime();
        int[] result1 = productExceptSelfPrefixSuffix(nums);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试空间优化解法
        start = System.nanoTime();
        int[] result2 = productExceptSelfOptimized(nums);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试分治解法
        start = System.nanoTime();
        int[] result3 = productExceptSelfDivideConquer(nums);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试特殊处理解法（如果没有超出int范围）
        long time4 = 0;
        int[] result4 = null;
        boolean canUseDivision = true;
        
        try {
            // 检查是否会溢出
            long product = 1;
            int zeroCount = 0;
            for (int num : nums) {
                if (num == 0) {
                    zeroCount++;
                } else {
                    if (Math.abs(product) > Integer.MAX_VALUE / Math.abs(num)) {
                        canUseDivision = false;
                        break;
                    }
                    product *= num;
                }
            }
            
            if (canUseDivision && zeroCount <= 1) {
                start = System.nanoTime();
                result4 = productExceptSelfSpecialCase(nums);
                end = System.nanoTime();
                time4 = end - start;
            }
        } catch (Exception e) {
            canUseDivision = false;
        }
        
        System.out.println("方法                | 时间(ms)   | 相对速度");
        System.out.println("-------------------|------------|----------");
        System.out.printf("前缀积+后缀积      | %.6f | 基准\n", time1 / 1_000_000.0);
        System.out.printf("空间优化解法       | %.6f | %.2fx\n", time2 / 1_000_000.0, (double)time2/time1);
        System.out.printf("分治解法           | %.6f | %.2fx\n", time3 / 1_000_000.0, (double)time3/time1);
        
        if (canUseDivision && result4 != null) {
            System.out.printf("特殊处理解法       | %.6f | %.2fx\n", time4 / 1_000_000.0, (double)time4/time1);
        } else {
            System.out.println("特殊处理解法       | 跳过(溢出风险或多个0)");
        }
        
        System.out.println("===============================================");
        
        // 验证结果一致性
        boolean consistent = Arrays.equals(result1, result2) && Arrays.equals(result2, result3);
        if (result4 != null) {
            consistent = consistent && Arrays.equals(result3, result4);
        }
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
    }
    
    // 生成测试数组
    private static int[] generateTestArray(int size, int range, boolean includeZero) {
        Random rand = new Random();
        int[] nums = new int[size];
        
        for (int i = 0; i < size; i++) {
            nums[i] = rand.nextInt(range * 2) - range;
            // 避免生成0（除非特意要包含）
            if (!includeZero && nums[i] == 0) {
                nums[i] = 1;
            }
        }
        
        // 如果需要包含0，随机选择位置放置0
        if (includeZero && rand.nextBoolean()) {
            int zeroPos = rand.nextInt(size);
            nums[zeroPos] = 0;
        }
        
        return nums;
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
        testCase("示例测试1", new int[]{1,2,3,4}, new int[]{24,12,8,6});
        testCase("示例测试2", new int[]{-1,1,0,-3,3}, new int[]{0,0,9,0,0});
        testCase("两元素数组", new int[]{2,3}, new int[]{3,2});
        testCase("包含负数", new int[]{-2,1,-3,4}, new int[]{-12,24,-8,6});
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("单元素数组", new int[]{5}, new int[]{1});
        testCase("包含一个0", new int[]{1,0,3,4}, new int[]{0,12,0,0});
        testCase("包含多个0", new int[]{1,0,3,0}, new int[]{0,0,0,0});
        testCase("全1数组", new int[]{1,1,1,1}, new int[]{1,1,1,1});
        testCase("大数值", new int[]{10,20,30}, new int[]{600,300,200});
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 股票收益率分析
        System.out.println("\n股票收益率分析:");
        double[] dailyReturns = {1.05, 0.98, 1.12, 0.95, 1.08};
        double[] otherDaysReturn = calculateOtherDaysReturn(dailyReturns);
        System.out.println("每日收益率: " + Arrays.toString(dailyReturns));
        System.out.println("除当日外累积收益率: " + Arrays.toString(otherDaysReturn));
        
        // 场景2: 概率计算
        System.out.println("\n概率计算:");
        double[] probabilities = {0.8, 0.6, 0.9, 0.7};
        double[] otherEventsProb = calculateOtherEventsProbability(probabilities);
        System.out.println("各事件概率: " + Arrays.toString(probabilities));
        System.out.println("除当前事件外其他事件同时发生概率: " + Arrays.toString(otherEventsProb));
        
        // 场景3: 游戏积分系统
        System.out.println("\n游戏积分系统:");
        int[] levelScores = {100, 200, 150, 300, 250};
        GameScoreCalculator calculator = new GameScoreCalculator(levelScores);
        
        System.out.println("各关卡积分: " + Arrays.toString(levelScores));
        System.out.println("除当前关卡外其他关卡积分乘积: " + Arrays.toString(calculator.getOtherLevelsProduct()));
        
        // 更新关卡积分并测试缓存
        calculator.updateLevelScore(2, 180);
        System.out.println("更新第3关卡积分后:");
        System.out.println("第1关卡除外的总积分: " + calculator.getTotalScoreExceptLevel(0));
        
        Map<String, Object> gameStats = calculator.getGameStatistics();
        System.out.println("游戏统计信息: " + gameStats);
        
        // 场景4: 矩阵乘积计算
        System.out.println("\n矩阵乘积计算:");
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        
        System.out.println("原矩阵:");
        printMatrix(matrix);
        
        int[][] rowProducts = MatrixProductCalculator.calculateRowProductsExceptColumn(matrix);
        System.out.println("每行除当前列外的乘积:");
        printMatrix(rowProducts);
        
        int[][] colProducts = MatrixProductCalculator.calculateColumnProductsExceptRow(matrix);
        System.out.println("每列除当前行外的乘积:");
        printMatrix(colProducts);
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        int[] demoArray = {1, 2, 3, 4};
        visualizePrefixSuffixProcess(demoArray);
        visualizeOptimizedProcess(demoArray);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 不同大小和特征的测试用例
        int[][] testCases = {
            generateTestArray(100, 10, false),      // 小数组，无0
            generateTestArray(100, 10, true),       // 小数组，可能有0
            generateTestArray(1000, 100, false),    // 中等数组，无0
            generateTestArray(1000, 100, true),     // 中等数组，可能有0
            generateTestArray(10000, 1000, false)   // 大数组，无0
        };
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n测试用例 " + (i + 1) + ":");
            comparePerformance(testCases[i]);
        }
    }
    
    private static void testCase(String name, int[] nums, int[] expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("输入: " + Arrays.toString(nums));
        
        int[] result1 = productExceptSelfPrefixSuffix(nums);
        int[] result2 = productExceptSelfOptimized(nums);
        int[] result3 = productExceptSelfDivideConquer(nums);
        
        System.out.println("前缀积+后缀积结果: " + Arrays.toString(result1));
        System.out.println("空间优化结果: " + Arrays.toString(result2));
        System.out.println("分治解法结果: " + Arrays.toString(result3));
        System.out.println("期望结果: " + Arrays.toString(expected));
        
        boolean isCorrect = Arrays.equals(result1, expected) && 
                           Arrays.equals(result2, expected) && 
                           Arrays.equals(result3, expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小规模数组展示可视化
        if (nums.length <= 8) {
            visualizePrefixSuffixProcess(nums);
        }
    }
    
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}