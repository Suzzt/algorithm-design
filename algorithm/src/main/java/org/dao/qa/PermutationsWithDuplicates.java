package main.java.org.dao.qa;

import java.util.*;

/**
 * 全排列问题（包含重复元素）深度解析
 * 
 * <p><b>问题描述</b>:
 * 给定一个包含重复数字的序列，返回所有不重复的全排列。
 * 
 * <p><b>示例</b>:
 * 输入: [1,1,2]
 * 输出: [[1,1,2], [1,2,1], [2,1,1]]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第47题)
 * 
 * <p><b>解题思路</b>:
 * 1. 回溯法基础框架
 * 2. 剪枝优化避免重复排列
 * 3. 三种去重策略分析：
 *    a. 结果集去重（效率低）
 *    b. 排序+相同位置重复元素剪枝
 *    c. 频率计数法
 * 4. 迭代法实现
 * 
 * <p><b>时间复杂度</b>: O(n*n!) - n为序列长度
 * <p><b>空间复杂度</b>: O(n) - 回溯递归栈深度
 * 
 * <p><b>应用场景</b>:
 * 1. 密码破解中的组合生成
 * 2. 游戏中的关卡设计
 * 3. 数据加密算法
 * 4. 生物信息学的序列分析
 * 5. 组合优化问题
 */

public class PermutationsWithDuplicates {

    // ========================= 解法1: 回溯法 + 剪枝 =========================
    /**
     * 基于排序和剪枝的回溯解法
     * 
     * @param nums 输入数组
     * @return 所有不重复全排列
     */
    public static List<List<Integer>> permuteUniqueBacktracking(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();
        Arrays.sort(nums);  // 关键：先排序使相同元素相邻
        backtrack(results, new ArrayList<>(), nums, new boolean[nums.length]);
        return results;
    }
    
    private static void backtrack(List<List<Integer>> results, List<Integer> current, 
                                 int[] nums, boolean[] used) {
        // 终止条件：当前排列长度等于输入长度
        if (current.size() == nums.length) {
            results.add(new ArrayList<>(current));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            // 剪枝条件1：当前元素已使用
            if (used[i]) continue;
            
            // 剪枝条件2：避免重复排列
            // a. 当前元素与前一个相同
            // b. 前一个元素尚未使用（说明是同一层递归）
            if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) {
                continue;
            }
            
            // 选择当前元素
            used[i] = true;
            current.add(nums[i]);
            
            // 递归探索
            backtrack(results, current, nums, used);
            
            // 撤销选择
            used[i] = false;
            current.remove(current.size() - 1);
        }
    }
    
    // ========================= 解法2: 频率计数法 =========================
    /**
     * 基于频率计数的回溯解法
     * 
     * @param nums 输入数组
     * @return 所有不重复全排列
     */
    public static List<List<Integer>> permuteUniqueFrequency(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();
        Map<Integer, Integer> freqMap = new HashMap<>();
        
        // 初始化频率计数
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        backtrackFrequency(results, new ArrayList<>(), freqMap, nums.length);
        return results;
    }
    
    private static void backtrackFrequency(List<List<Integer>> results, List<Integer> current, 
                                          Map<Integer, Integer> freqMap, int total) {
        // 终止条件：当前排列长度等于总长度
        if (current.size() == total) {
            results.add(new ArrayList<>(current));
            return;
        }
        
        // 遍历所有唯一键（避免重复处理相同元素）
        for (int num : freqMap.keySet()) {
            int count = freqMap.get(num);
            if (count == 0) continue;  // 当前元素已用完
            
            // 选择当前元素
            freqMap.put(num, count - 1);
            current.add(num);
            
            // 递归探索
            backtrackFrequency(results, current, freqMap, total);
            
            // 撤销选择
            current.remove(current.size() - 1);
            freqMap.put(num, count);
        }
    }
    
    // ========================= 解法3: 迭代法 =========================
    /**
     * 基于插入的迭代解法
     * 
     * @param nums 输入数组
     * @return 所有不重复全排列
     */
    public static List<List<Integer>> permuteUniqueIterative(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();
        results.add(new ArrayList<>());  // 初始空排列
        
        for (int num : nums) {
            Set<List<Integer>> currentSet = new LinkedHashSet<>();
            
            // 遍历现有所有排列
            for (List<Integer> perm : results) {
                // 尝试在所有位置插入新元素
                for (int i = 0; i <= perm.size(); i++) {
                    // 避免重复插入：相同元素仅插入到左侧最后一个相同元素之后
                    if (i > 0 && perm.get(i - 1) == num) {
                        continue;
                    }
                    
                    List<Integer> newPerm = new ArrayList<>(perm);
                    newPerm.add(i, num);
                    currentSet.add(newPerm);
                }
            }
            results = new ArrayList<>(currentSet);
        }
        return results;
    }
    
    // ========================= 可视化工具 =========================
    /**
     * 可视化排列生成过程
     * 
     * @param nums 输入数组
     * @param algorithm 1=回溯法, 2=频率法, 3=迭代法
     */
    public static void visualizePermutations(int[] nums, int algorithm) {
        System.out.println("输入数组: " + Arrays.toString(nums));
        System.out.println("算法: " + 
                          (algorithm == 1 ? "回溯法+剪枝" : 
                           algorithm == 2 ? "频率计数法" : "迭代法"));
        System.out.println("排列生成过程:");
        
        // 跟踪递归状态树
        if (algorithm == 1) {
            visualizeBacktracking(nums);
        } else if (algorithm == 2) {
            visualizeFrequency(nums);
        } else {
            visualizeIterative(nums);
        }
    }
    
    private static void visualizeBacktracking(int[] nums) {
        Arrays.sort(nums);
        System.out.println("决策树（左括号表示进入递归，右括号表示回溯）:");
        visualizeBacktrackingRecursive(new ArrayList<>(), nums, 
                                      new boolean[nums.length], 0, new StringBuilder());
    }
    
    private static void visualizeBacktrackingRecursive(List<Integer> current, int[] nums, 
                                                     boolean[] used, int depth, StringBuilder tree) {
        // 达到叶节点
        if (current.size() == nums.length) {
            System.out.println(tree + " -> " + current);
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) {
                // 显示剪枝分支
                System.out.println(tree + " X 剪枝: [" + nums[i] + "] 与 [前元素] 重复");
                continue;
            }
            
            // 记录当前状态
            int treeLength = tree.length();
            tree.append("(").append(nums[i]).append(" ");
            used[i] = true;
            current.add(nums[i]);
            
            // 递归探索
            visualizeBacktrackingRecursive(current, nums, used, depth + 1, tree);
            
            // 回溯
            tree.setLength(treeLength);
            used[i] = false;
            current.remove(current.size() - 1);
        }
    }
    
    private static void visualizeFrequency(int[] nums) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        System.out.println("频率表: " + freqMap);
        visualizeFrequencyRecursive(new ArrayList<>(), freqMap, 
                                   nums.length, 0, new StringBuilder());
    }
    
    private static void visualizeFrequencyRecursive(List<Integer> current, 
                                                   Map<Integer, Integer> freqMap, 
                                                   int total, int depth, StringBuilder path) {
        // 达到叶节点
        if (current.size() == total) {
            System.out.println(path + " -> " + current);
            return;
        }
        
        for (int num : freqMap.keySet()) {
            int count = freqMap.get(num);
            if (count == 0) {
                // 显示跳过情况
                System.out.println(path + " X 跳过: [" + num + "] 数量不足");
                continue;
            }
            
            // 记录当前状态
            int pathLength = path.length();
            path.append("(").append(num).append(" ");
            freqMap.put(num, count - 1);
            current.add(num);
            
            // 递归探索
            visualizeFrequencyRecursive(current, freqMap, total, depth + 1, path);
            
            // 回溯
            path.setLength(pathLength);
            freqMap.put(num, count);
            current.remove(current.size() - 1);
        }
    }
    
    private static void visualizeIterative(int[] nums) {
        List<List<Integer>> results = new ArrayList<>();
        results.add(new ArrayList<>());
        System.out.println("起始: 空列表");
        
        for (int num : nums) {
            System.out.println("\n处理元素: " + num);
            Set<List<Integer>> currentSet = new LinkedHashSet<>();
            
            for (List<Integer> perm : results) {
                System.out.println("当前排列: " + perm);
                
                for (int i = 0; i <= perm.size(); i++) {
                    if (i > 0 && perm.get(i - 1) == num) {
                        System.out.println("  跳过位置 " + i + " (避免重复)");
                        continue;
                    }
                    
                    List<Integer> newPerm = new ArrayList<>(perm);
                    newPerm.add(i, num);
                    System.out.println("  位置 " + i + ": " + newPerm);
                    currentSet.add(newPerm);
                }
            }
            results = new ArrayList<>(currentSet);
        }
    }
    
    // ========================= 测试方法 =========================
    public static void main(String[] args) {
        // 测试用例集
        int[][] testCases = {
            {1, 1, 2},       // 基础重复
            {1, 2, 3},       // 无重复
            {2, 2, 2},       // 全相同
            {3, 3, 1, 1},    // 两组重复
            {1},             // 单个元素
            {}               // 空数组
        };
        
        // 运行测试
        runTestSuite(testCases);
        
        // 算法比较
        compareAlgorithms(new int[]{1, 1, 2});
        compareAlgorithms(new int[]{1, 2, 2, 3});
        
        // 可视化演示
        System.out.println("\n===== 可视化演示 =====");
        visualizePermutations(new int[]{1, 1, 2}, 1);  // 回溯法
        visualizePermutations(new int[]{1, 2, 2}, 2);   // 频率法
        visualizePermutations(new int[]{2, 1, 2}, 3);   // 迭代法
        
        // 性能测试
        performanceTest();
    }
    
    private static void runTestSuite(int[][] testCases) {
        System.out.println("====== 测试套件 ======");
        
        for (int i = 0; i < testCases.length; i++) {
            int[] nums = testCases[i];
            System.out.println("\n测试 " + (i+1) + ": " + Arrays.toString(nums));
            
            // 三种解法
            List<List<Integer>> result1 = permuteUniqueBacktracking(nums);
            List<List<Integer>> result2 = permuteUniqueFrequency(nums);
            List<List<Integer>> result3 = permuteUniqueIterative(nums);
            
            // 验证结果一致性
            boolean pass12 = areResultEqual(result1, result2);
            boolean pass13 = areResultEqual(result1, result3);
            
            System.out.println("回溯法: " + result1.size() + " 种排列");
            System.out.println("频率法: " + result2.size() + " 种排列 " + (pass12 ? "✅" : "❌"));
            System.out.println("迭代法: " + result3.size() + " 种排列 " + (pass13 ? "✅" : "❌"));
            
            // 打印结果（避免大量输出）
            if (nums.length <= 3) {
                System.out.println("排列结果: " + result1);
            } else {
                System.out.println("排列结果数量: " + result1.size());
            }
        }
    }
    
    private static boolean areResultEqual(List<List<Integer>> list1, List<List<Integer>> list2) {
        if (list1.size() != list2.size()) return false;
        
        Set<List<Integer>> set1 = new HashSet<>(list1);
        Set<List<Integer>> set2 = new HashSet<>(list2);
        
        return set1.equals(set2);
    }
    
    private static void compareAlgorithms(int[] nums) {
        System.out.println("\n===== 算法比较: " + Arrays.toString(nums) + " =====");
        
        long start, end;
        
        // 回溯法性能
        start = System.nanoTime();
        List<List<Integer>> res1 = permuteUniqueBacktracking(nums);
        end = System.nanoTime();
        System.out.printf("回溯法: %d ns (%d 种排列)", (end - start), res1.size());
        
        // 频率法性能
        start = System.nanoTime();
        List<List<Integer>> res2 = permuteUniqueFrequency(nums);
        end = System.nanoTime();
        System.out.printf(" | 频率法: %d ns (%d 种排列)", (end - start), res2.size());
        
        // 迭代法性能
        start = System.nanoTime();
        List<List<Integer>> res3 = permuteUniqueIterative(nums);
        end = System.nanoTime();
        System.out.printf(" | 迭代法: %d ns (%d 种排列)\n", (end - start), res3.size());
    }
    
    private static void performanceTest() {
        System.out.println("\n===== 性能测试 =====");
        
        // 生成不同规模测试数据
        int[] small = generateArray(8, 5);    // 8个元素，5种不同值
        int[] medium = generateArray(10, 4);   // 10个元素，4种不同值
        int[] large = generateArray(12, 6);    // 12个元素，6种不同值
        
        System.out.println("小数据集 (8元素): ");
        compareAlgorithms(small);
        
        System.out.println("中数据集 (10元素): ");
        compareAlgorithms(medium);
        
        System.out.println("大数据集 (12元素): ");
        compareAlgorithms(large);
    }
    
    private static int[] generateArray(int size, int distinct) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(distinct);  // 生成0到distinct-1的随机数
        }
        return arr;
    }
}