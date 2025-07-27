package main.java.org.dao.qa;

import java.util.*;

/**
 * 下一个排列问题
 * 
 * <p><b>问题描述</b>:
 * 实现获取下一个排列的函数。算法需要将给定数字序列重新排列成字典序中下一个更大的排列。
 * 如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。
 * 必须原地修改，只允许使用额外常数空间。
 * 
 * <p><b>示例</b>:
 * 输入: nums = [1,2,3]
 * 输出: [1,3,2]
 * 输入: nums = [3,2,1]
 * 输出: [1,2,3]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第31题)
 * 
 * <p><b>解题思路</b>:
 * 1. 从右向左扫描：找到第一个小于右侧元素的索引i
 * 2. 再次从右向左：找到第一个大于nums[i]的元素索引j
 * 3. 交换nums[i]和nums[j]
 * 4. 反转索引i之后的所有元素
 * 
 * <p><b>时间复杂度</b>:
 *  O(n)
 * 
 * <p><b>空间复杂度</b>:
 *  O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 字典序排列生成器
 * 2. 密码学中的密钥序列生成
 * 3. 游戏中的关卡生成器
 * 4. 组合优化问题求解
 * 5. 数据压缩算法
 */

public class NextPermutation {
    
    // ========================= 核心算法 =========================
    
    /**
     * 计算下一个排列
     * 
     * @param nums 输入数组
     */
    public static void nextPermutation(int[] nums) {
        if (nums == null || nums.length <= 1) return;
        
        int i = nums.length - 2;
        // 1. 从右向左找第一个小于右邻的元素
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            i--;
        }
        
        // 如果找到递减点
        if (i >= 0) {
            int j = nums.length - 1;
            // 2. 从右向左找第一个大于nums[i]的元素
            while (j > i && nums[j] <= nums[i]) {
                j--;
            }
            // 3. 交换这两个元素
            swap(nums, i, j);
        }
        // 4. 反转i之后的序列
        reverse(nums, i + 1);
    }
    
    // ========================= 辅助方法 =========================
    
    // 交换数组中的两个元素
    private static void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    // 反转数组从指定位置开始到末尾的部分
    private static void reverse(int[] nums, int start) {
        int end = nums.length - 1;
        while (start < end) {
            swap(nums, start, end);
            start++;
            end--;
        }
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 字典序排列生成器
     * 
     * @param start 起始排列
     * @param count 生成排列数量
     * @return 排列列表
     */
    public static List<int[]> generatePermutations(int[] start, int count) {
        List<int[]> result = new ArrayList<>();
        int[] current = start.clone();
        result.add(current.clone());
        
        for (int i = 1; i < count; i++) {
            nextPermutation(current);
            result.add(current.clone());
        }
        return result;
    }
    
    /**
     * 密码学密钥序列生成器
     * 
     * @param baseKey 基础密钥
     * @param rounds 生成轮次
     * @return 密钥序列
     */
    public static List<int[]> generateKeySequence(int[] baseKey, int rounds) {
        return generatePermutations(baseKey, rounds);
    }
    
    /**
     * 游戏关卡生成器
     * 
     * @param elements 关卡元素
     * @param levelCount 关卡数量
     * @return 关卡列表
     */
    public static List<int[]> generateGameLevels(int[] elements, int levelCount) {
        List<int[]> levels = generatePermutations(elements, levelCount);
        Collections.shuffle(levels); // 随机打乱顺序
        return levels;
    }
    
    /**
     * 组合优化求解器 - 解决旅行商问题(TSP)
     * 
     * @param cities 城市数量
     * @return 城市访问序列
     */
    public static int[] tspSolution(int cities) {
        int[] path = new int[cities];
        for (int i = 0; i < cities; i++) {
            path[i] = i;
        }
        
        int[] bestPath = path.clone();
        int bestCost = Integer.MAX_VALUE;
        
        // 生成所有排列并评估（实际应用中会使用启发式方法）
        List<int[]> permutations = generatePermutations(path, factorial(cities));
        for (int[] permutation : permutations) {
            int cost = evaluatePath(permutation);
            if (cost < bestCost) {
                bestCost = cost;
                bestPath = permutation;
            }
        }
        
        return bestPath;
    }
    
    // 计算路径成本（模拟方法）
    private static int evaluatePath(int[] path) {
        int cost = 0;
        for (int i = 0; i < path.length - 1; i++) {
            cost += Math.abs(path[i] - path[i + 1]); // 使用简单的成本计算
        }
        return cost;
    }
    
    // 计算阶乘
    private static int factorial(int n) {
        return (n <= 1) ? 1 : n * factorial(n - 1);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化排列生成过程
     * 
     * @param start 起始排列
     * @param count 生成数量
     */
    public static void visualizePermutationProcess(int[] start, int count) {
        System.out.println("\n排列生成过程:");
        System.out.println("起始排列: " + Arrays.toString(start));
        
        int[] current = start.clone();
        
        for (int i = 1; i <= count; i++) {
            nextPermutation(current);
            System.out.printf("排列 %2d: %s%n", i, Arrays.toString(current));
        }
    }
    
    /**
     * 可视化下一个排列计算过程
     * 
     * @param nums 输入数组
     */
    public static void visualizeNextPermutation(int[] nums) {
        System.out.println("\n计算过程:");
        System.out.println("原始数组: " + Arrays.toString(nums));
        
        if (nums == null || nums.length <= 1) {
            System.out.println("数组为空或长度小于2，直接返回");
            return;
        }
        
        int i = nums.length - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            System.out.printf("索引 %d: %d >= %d, 继续向前%n", i, nums[i], nums[i + 1]);
            i--;
        }
        
        if (i >= 0) {
            System.out.printf("找到递减点: 索引 %d 值 %d%n", i, nums[i]);
            
            int j = nums.length - 1;
            while (j > i && nums[j] <= nums[i]) {
                System.out.printf("索引 %d: %d <= %d, 继续向前%n", j, nums[j], nums[i]);
                j--;
            }
            System.out.printf("找到交换点: 索引 %d 值 %d%n", j, nums[j]);
            
            swap(nums, i, j);
            System.out.println("交换后数组: " + Arrays.toString(nums));
        } else {
            System.out.println("整个数组是降序的，无下一个排列");
        }
        
        System.out.printf("反转位置: 从索引 %d 到结束%n", i + 1);
        reverse(nums, i + 1);
        System.out.println("最终结果: " + Arrays.toString(nums));
    }
    
    // ========================= 性能优化工具 =========================
    
    /**
     * 大型数据集测试
     * 
     * @param size 数组大小
     */
    public static void testLargeDataset(int size) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = i + 1;
        }
        
        long startTime = System.nanoTime();
        nextPermutation(data);
        long duration = System.nanoTime() - startTime;
        
        System.out.printf("\n大型数据集测试 (%d元素): 处理时间 %.3f ms%n", 
                          size, duration / 1_000_000.0);
    }
    
    /**
     * 性能比较（多次调用）
     * 
     * @param array 输入数组
     * @param iterations 迭代次数
     */
    public static void comparePerformance(int[] array, int iterations) {
        int[] copy1 = array.clone();
        int[] copy2 = array.clone();
        
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            nextPermutation(copy1);
        }
        long time1 = System.nanoTime() - start;
        
        // 洗牌方法比较
        start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            shuffleArray(copy2);
        }
        long time2 = System.nanoTime() - start;
        
        System.out.println("\n性能比较:");
        System.out.println("====================================");
        System.out.println("方法               | 时间 (ms)");
        System.out.println("-------------------|---------------");
        System.out.printf("下一个排列算法      | %.3f%n", time1 / 1_000_000.0);
        System.out.printf("随机洗牌算法        | %.3f%n", time2 / 1_000_000.0);
        System.out.println("====================================");
    }
    
    // Fisher-Yates洗牌算法
    private static void shuffleArray(int[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
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
        
        int[] test1 = {1, 2, 3};
        testCase("升序序列", test1, new int[]{1, 3, 2});
        
        int[] test2 = {3, 2, 1};
        testCase("降序序列", test2, new int[]{1, 2, 3});
        
        int[] test3 = {1, 1, 5};
        testCase("带重复元素", test3, new int[]{1, 5, 1});
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        int[] empty = {};
        testCase("空数组", empty, empty);
        
        int[] single = {5};
        testCase("单元素", single, single);
        
        int[] sameElements = {1, 1, 1};
        testCase("全相同元素", sameElements, sameElements);
        
        int[] alreadyMax = {5, 4, 3, 2, 1};
        testCase("已最大排列", alreadyMax, new int[]{1, 2, 3, 4, 5});
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 1. 字典序生成器
        int[] start = {1, 2, 3};
        System.out.println("\n字典序生成器 (10个排列):");
        visualizePermutationProcess(start, 10);
        
        // 2. 游戏关卡生成
        int[] elements = {1, 2, 3, 4};
        List<int[]> levels = generateGameLevels(elements, 6);
        System.out.println("\n游戏关卡生成:");
        for (int i = 0; i < levels.size(); i++) {
            System.out.println("关卡" + (i + 1) + ": " + Arrays.toString(levels.get(i)));
        }
        
        // 3. TSP问题求解（简化版）
        System.out.println("\n旅行商问题求解 (5个城市):");
        int[] solution = tspSolution(5);
        System.out.println("最优路径: " + Arrays.toString(solution));
        
        // 4. 可视化计算过程
        System.out.println("\n可视化计算:");
        visualizeNextPermutation(new int[]{1, 5, 8, 4, 2});
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        // 小规模测试
        int[] data = {1, 2, 3, 4, 5, 6};
        comparePerformance(data, 1000);
        
        // 大规模测试
        testLargeDataset(100000);
    }
    
    private static void testCase(String name, int[] input, int[] expected) {
        System.out.printf("\n测试用例: %s%n", name);
        System.out.println("输入: " + Arrays.toString(input));
        
        int[] original = input.clone();
        nextPermutation(input);
        System.out.println("输出: " + Arrays.toString(input));
        
        boolean passed = Arrays.equals(input, expected);
        System.out.println("测试结果: " + (passed ? "通过" : "失败"));
        
        if (!passed) {
            System.out.println("预期输出: " + Arrays.toString(expected));
        }
        
        // 可视化过程
        if (input.length <= 10) {
            visualizeNextPermutation(original);
        }
    }
}