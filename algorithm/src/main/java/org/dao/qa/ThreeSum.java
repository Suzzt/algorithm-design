package main.java.org.dao.qa;

import java.util.*;

/**
 * 三数之和问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个整数数组 nums，找出所有满足 nums[i] + nums[j] + nums[k] == target 的三元组。
 * 答案中不能包含重复的三元组。
 * 
 * <p><b>示例</b>:
 * 输入: nums = [-1,0,1,2,-1,-4], target = 0
 * 输出: [[-1,-1,2], [-1,0,1]]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第15题)
 * 
 * <p><b>解题思路</b>:
 * 1. 双指针法: 排序数组，固定一个数，用双指针找另外两个数
 * 2. 哈希表法: 用哈希表存储补数，处理三数和问题
 * 3. 分治法: 将三数和问题转化为两数和问题
 * 
 * <p><b>时间复杂度</b>:
 *  双指针法: O(n²)
 *  哈希表法: O(n²)
 *  分治法: O(n²)
 * 
 * <p><b>空间复杂度</b>:
 *  所有方法: O(1) 或 O(n) 额外空间
 * 
 * <p><b>应用场景</b>:
 * 1. 金融数据分析和交易策略
 * 2. 组合优化问题求解
 * 3. 化学方程式配平
 * 4. 游戏开发中的组合算法
 * 5. 多因素数据分析
 */

public class ThreeSum {
    
    // ========================= 解法1: 双指针法 (推荐) =========================
    
    /**
     * 双指针解法
     * 
     * @param nums 输入数组
     * @param target 目标和
     * @return 所有满足条件的三元组
     */
    public static List<List<Integer>> threeSumTwoPointers(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) return result;
        
        Arrays.sort(nums);  // 排序是双指针法的前提
        
        for (int i = 0; i < nums.length - 2; i++) {
            // 跳过重复值
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            int left = i + 1;
            int right = nums.length - 1;
            int currentTarget = target - nums[i];
            
            while (left < right) {
                int sum = nums[left] + nums[right];
                
                if (sum == currentTarget) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // 跳过重复值
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    
                    left++;
                    right--;
                } else if (sum < currentTarget) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }
    
    // ========================= 解法2: 哈希表法 =========================
    
    /**
     * 哈希表解法
     * 
     * @param nums 输入数组
     * @param target 目标和
     * @return 所有满足条件的三元组
     */
    public static List<List<Integer>> threeSumHashMap(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) return result;
        
        Arrays.sort(nums);  // 仍然需要排序来避免重复
        
        for (int i = 0; i < nums.length - 2; i++) {
            // 跳过重复值
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            int currentTarget = target - nums[i];
            Map<Integer, Integer> map = new HashMap<>();
            
            for (int j = i + 1; j < nums.length; j++) {
                int complement = currentTarget - nums[j];
                
                if (map.containsKey(complement)) {
                    result.add(Arrays.asList(nums[i], complement, nums[j]));
                    
                    // 跳过重复值
                    while (j + 1 < nums.length && nums[j] == nums[j + 1]) j++;
                }
                map.put(nums[j], j);
            }
        }
        return result;
    }
    
    // ========================= 解法3: 分治法 =========================
    
    /**
     * 分治解法（转化为两数和）
     * 
     * @param nums 输入数组
     * @param target 目标和
     * @return 所有满足条件的三元组
     */
    public static List<List<Integer>> threeSumDivideConquer(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) return result;
        
        Arrays.sort(nums);  // 排序保证结果不重复
        
        for (int i = 0; i < nums.length - 2; i++) {
            // 跳过重复值
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            
            // 转化为两数和问题
            List<List<Integer>> twoSumResult = twoSum(nums, i + 1, target - nums[i]);
            
            for (List<Integer> list : twoSumResult) {
                List<Integer> triplet = new ArrayList<>();
                triplet.add(nums[i]);
                triplet.addAll(list);
                result.add(triplet);
            }
        }
        return result;
    }
    
    // 两数和子问题
    private static List<List<Integer>> twoSum(int[] nums, int start, int target) {
        List<List<Integer>> result = new ArrayList<>();
        int left = start;
        int right = nums.length - 1;
        
        while (left < right) {
            int sum = nums[left] + nums[right];
            
            if (sum == target) {
                result.add(Arrays.asList(nums[left], nums[right]));
                
                // 跳过重复值
                while (left < right && nums[left] == nums[left + 1]) left++;
                while (left < right && nums[right] == nums[right - 1]) right--;
                
                left++;
                right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return result;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 金融数据分析 - 查找资产组合
     * 
     * @param assets 资产收益率数组
     * @param targetReturn 目标收益率
     * @return 所有可能的资产组合
     */
    public static List<List<Integer>> findAssetCombinations(double[] assets, double targetReturn) {
        // 转换为整数问题（放大100倍避免浮点误差）
        int[] scaledAssets = new int[assets.length];
        for (int i = 0; i < assets.length; i++) {
            scaledAssets[i] = (int)(assets[i] * 100);
        }
        int scaledTarget = (int)(targetReturn * 100);
        
        List<List<Integer>> combinations = threeSumTwoPointers(scaledAssets, scaledTarget);
        
        // 转换回原始比例
        List<List<Integer>> result = new ArrayList<>();
        for (List<Integer> list : combinations) {
            List<Integer> scaled = new ArrayList<>();
            for (Integer num : list) {
                scaled.add(num / 100);
            }
            result.add(scaled);
        }
        return result;
    }
    
    /**
     * 化学方程式配平助手
     * 
     * @param elements 元素原子量数组
     * @param targetSum 目标原子量和
     * @return 所有可能的元素组合
     */
    public static List<List<Integer>> balanceChemicalEquation(int[] elements, int targetSum) {
        List<List<Integer>> result = threeSumDivideConquer(elements, targetSum);
        
        // 过滤无效组合（原子数为正整数）
        Iterator<List<Integer>> it = result.iterator();
        while (it.hasNext()) {
            List<Integer> triplet = it.next();
            for (int num : triplet) {
                if (num <= 0) {
                    it.remove();
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * 游戏开发 - 技能组合系统
     * 
     * @param skills 技能效果值数组
     * @param targetEffect 目标组合效果
     * @return 所有可能的技能组合
     */
    public static List<String> findSkillCombinations(Map<String, Integer> skills, int targetEffect) {
        // 提取技能值和名称
        List<String> skillNames = new ArrayList<>(skills.keySet());
        int[] skillValues = new int[skillNames.size()];
        for (int i = 0; i < skillNames.size(); i++) {
            skillValues[i] = skills.get(skillNames.get(i));
        }
        
        List<List<Integer>> indices = threeSumHashMap(skillValues, targetEffect);
        List<String> result = new ArrayList<>();
        
        // 转换索引为技能名组合
        for (List<Integer> triplet : indices) {
            Set<String> combination = new HashSet<>();
            for (int index : triplet) {
                combination.add(skillNames.get(index));
            }
            result.add(combination.toString());
        }
        return result;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化双指针过程
     * 
     * @param nums 输入数组
     * @param target 目标和
     */
    public static void visualizeTwoPointerProcess(int[] nums, int target) {
        System.out.println("\n双指针过程可视化:");
        System.out.println("数组: " + Arrays.toString(nums));
        System.out.println("目标: " + target);
        
        Arrays.sort(nums);
        System.out.println("排序后: " + Arrays.toString(nums));
        
        System.out.println("\n步骤 | 固定索引 | 左指针 | 右指针 | 当前和 | 操作");
        System.out.println("-----+----------+--------+--------+--------+----------------");
        
        int step = 1;
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                System.out.printf("%4d | %7d | 跳过重复 |\n", step++, i);
                continue;
            }
            
            int left = i + 1;
            int right = nums.length - 1;
            int currentTarget = target - nums[i];
            
            System.out.printf("%4d | %8d | %6d | %6d | %6d | 开始新循环\n", 
                             step++, i, left, right, currentTarget);
            
            while (left < right) {
                int sum = nums[left] + nums[right];
                
                if (sum == currentTarget) {
                    System.out.printf("%4d | %8d | %6d | %6d | %6d | 找到组合: [%d,%d,%d]\n", 
                                     step++, i, left, right, sum, 
                                     nums[i], nums[left], nums[right]);
                    
                    // 跳过重复值
                    while (left < right && nums[left] == nums[left + 1]) left++;
                    while (left < right && nums[right] == nums[right - 1]) right--;
                    
                    left++;
                    right--;
                } else if (sum < currentTarget) {
                    System.out.printf("%4d | %8d | %6d | %6d | %6d | 和太小，左指针右移\n", 
                                     step++, i, left, right, sum);
                    left++;
                } else {
                    System.out.printf("%4d | %8d | %6d | %6d | %6d | 和太大，右指针左移\n", 
                                     step++, i, left, right, sum);
                    right--;
                }
            }
        }
    }
    
    /**
     * 结果可视化打印
     * 
     * @param result 结果列表
     */
    public static void printResults(List<List<Integer>> result) {
        System.out.println("\n三数之和结果:");
        if (result.isEmpty()) {
            System.out.println("无满足条件的三元组");
            return;
        }
        
        for (List<Integer> triplet : result) {
            StringBuilder sb = new StringBuilder();
            for (int num : triplet) {
                sb.append(num).append(" + ");
            }
            sb.setLength(sb.length() - 3); // 移除最后的"+"
            int sum = triplet.stream().mapToInt(Integer::intValue).sum();
            
            System.out.printf("%s = %d\n", sb.toString(), sum);
        }
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param size 数组大小
     * @param range 数值范围
     */
    public static void comparePerformance(int size, int range) {
        // 生成测试数据
        int[] nums = generateRandomArray(size, -range, range);
        int target = new Random().nextInt(range * 2) - range;
        
        long start, end;
        
        // 测试双指针法
        start = System.nanoTime();
        threeSumTwoPointers(nums, target);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试哈希表法
        start = System.nanoTime();
        threeSumHashMap(nums, target);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试分治法
        start = System.nanoTime();
        threeSumDivideConquer(nums, target);
        end = System.nanoTime();
        long time3 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("数组大小: %d, 数值范围: [-%d, %d], 目标值: %d\n", size, range, range, target);
        System.out.println("=======================================");
        System.out.println("方法            | 时间(ms)   | 相对速度");
        System.out.println("----------------|------------|----------");
        System.out.printf("双指针法        | %.6f | 基准\n", time1 / 1_000_000.0);
        System.out.printf("哈希表法        | %.6f | %.2fx\n", time2 / 1_000_000.0, (double)time2/time1);
        System.out.printf("分治法          | %.6f | %.2fx\n", time3 / 1_000_000.0, (double)time3/time1);
        System.out.println("=======================================");
    }
    
    // 生成随机数组
    private static int[] generateRandomArray(int size, int min, int max) {
        Random rand = new Random();
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = rand.nextInt(max - min + 1) + min;
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
        testCase("示例测试", new int[]{-1, 0, 1, 2, -1, -4}, 0, 
                 Arrays.asList(Arrays.asList(-1, -1, 2), Arrays.asList(-1, 0, 1)));
        testCase("正数测试", new int[]{1, 2, 3, 4, 5}, 9, 
                 Arrays.asList(Arrays.asList(1, 3, 5), Arrays.asList(2, 3, 4)));
        testCase("负数测试", new int[]{-5, -4, -3, -2, -1}, -8, 
                 Arrays.asList(Arrays.asList(-5, -3, -1), Arrays.asList(-4, -3, -1)));
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("少于三个元素", new int[]{1, 2}, 3, Collections.emptyList());
        testCase("全零数组", new int[]{0, 0, 0, 0}, 0, Arrays.asList(Arrays.asList(0, 0, 0)));
        testCase("无解数组", new int[]{1, 2, 3, 4}, 100, Collections.emptyList());
        testCase("重复元素处理", new int[]{-1, -1, -1, 2, 2}, 0, Arrays.asList(Arrays.asList(-1, -1, 2)));
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 金融数据分析
        System.out.println("\n金融数据分析:");
        double[] assets = {0.05, 0.08, -0.02, 0.12, 0.03};
        double targetReturn = 0.15;
        System.out.printf("资产收益率: %s\n", Arrays.toString(assets));
        System.out.printf("目标组合收益率: %.2f\n", targetReturn);
        System.out.println("可行组合:");
        for (List<Integer> combination : findAssetCombinations(assets, targetReturn)) {
            System.out.println(combination);
        }
        
        // 场景2: 化学方程式配平
        System.out.println("\n化学方程式配平:");
        int[] atomicWeights = {16, 32, 1, 2}; // O, S, H, He 的原子量
        int targetWeight = 34; // H₂SO₄ = 2 * 1 + 32 + 4 * 16 = 98
        System.out.printf("原子量: %s\n", Arrays.toString(atomicWeights));
        System.out.printf("目标分子量: %d\n", targetWeight);
        System.out.println("可能的原子组合:");
        for (List<Integer> combination : balanceChemicalEquation(atomicWeights, targetWeight)) {
            System.out.println(combination);
        }
        
        // 场景3: 游戏技能组合
        System.out.println("\n游戏技能组合:");
        Map<String, Integer> skills = new HashMap<>();
        skills.put("火球术", 30);
        skills.put("冰箭术", 25);
        skills.put("雷电术", 35);
        skills.put("治疗术", 20);
        skills.put("护盾术", 15);
        int targetEffect = 70;
        System.out.printf("技能效果值: %s\n", skills);
        System.out.printf("目标组合效果: %d\n", targetEffect);
        System.out.println("有效组合:");
        for (String combination : findSkillCombinations(skills, targetEffect)) {
            System.out.println(combination);
        }
        
        // 可视化测试
        System.out.println("\n可视化过程:");
        visualizeTwoPointerProcess(new int[]{-1, 0, 1, 2, -1, -4}, 0);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100, 100);
        comparePerformance(1000, 1000);
        comparePerformance(5000, 5000);
    }
    
    private static void testCase(String name, int[] nums, int target, List<List<Integer>> expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("数组: " + Arrays.toString(nums));
        System.out.println("目标: " + target);
        
        List<List<Integer>> twoPointerResult = threeSumTwoPointers(nums, target);
        List<List<Integer>> hashMapResult = threeSumHashMap(nums, target);
        List<List<Integer>> divideResult = threeSumDivideConquer(nums, target);
        
        System.out.println("双指针法结果: " + twoPointerResult);
        System.out.println("哈希表法结果: " + hashMapResult);
        System.out.println("分治法结果: " + divideResult);
        
        // 验证结果一致性
        boolean isCorrect = compareResults(twoPointerResult, expected) &&
                            compareResults(hashMapResult, expected) &&
                            compareResults(divideResult, expected);
        
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 可视化小规模案例
        if (nums.length <= 20) {
            printResults(twoPointerResult);
            visualizeTwoPointerProcess(nums, target);
        }
    }
    
    // 比较结果是否相同（忽略顺序）
    private static boolean compareResults(List<List<Integer>> actual, List<List<Integer>> expected) {
        if (actual.size() != expected.size()) return false;
        
        Set<String> actualSet = new HashSet<>();
        for (List<Integer> triplet : actual) {
            Collections.sort(triplet);
            actualSet.add(triplet.toString());
        }
        
        Set<String> expectedSet = new HashSet<>();
        for (List<Integer> triplet : expected) {
            Collections.sort(triplet);
            expectedSet.add(triplet.toString());
        }
        
        return actualSet.equals(expectedSet);
    }
}