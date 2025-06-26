package main.java.org.dao.qa;

import java.util.*;

/**
 * 全排列问题（包含重复元素）
 * 
 * <p><b>问题描述</b>:
 * 给定一个包含重复元素的序列，返回所有不重复的全排列。
 * 
 * <p><b>示例</b>:
 * 输入: [1,1,2]
 * 输出:
 * [
 *   [1,1,2],
 *   [1,2,1],
 *   [2,1,1]
 * ]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第47题)
 * 
 * <p><b>解题思路</b>:
 * 1. 回溯法基础框架
 * 2. 剪枝优化避免重复排列
 * 3. 三种去重策略:
 *    a. 排序+相同位置重复元素剪枝
 *    b. 频率计数法
 *    c. 迭代法
 * 4. 多种算法实现与性能比较
 * 
 * <p><b>时间复杂度</b>: O(n*n!) - n为序列长度
 * <p><b>空间复杂度</b>: O(n) - 递归栈深度
 * 
 * <p><b>应用场景</b>:
 * 1. 密码生成系统
 * 2. 游戏关卡设计
 * 3. DNA序列分析
 * 4. 实验设计组合优化
 */

public class PermutationsII {
    
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
            // 当前元素与前一个相同 且 前一个元素尚未使用（说明是同一层递归）
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
     */
    public static void visualizePermutations(int[] nums) {
        System.out.println("\n可视化排列生成过程: " + Arrays.toString(nums));
        System.out.println("决策树结构 ([]:选择, ():递归, X:剪枝):");
        visualizeBacktracking(new ArrayList<>(), nums, 
                             new boolean[nums.length], 0, new StringBuilder());
    }
    
    private static void visualizeBacktracking(List<Integer> current, int[] nums, 
                                             boolean[] used, int depth, StringBuilder path) {
        // 达到叶节点
        if (current.size() == nums.length) {
            System.out.println(path + " -> " + current);
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            
            // 剪枝判断
            if (i > 0 && nums[i] == nums[i-1] && !used[i-1]) {
                System.out.println(path + " X 剪枝: [" + nums[i] + "] 重复");
                continue;
            }
            
            // 记录当前状态
            int pathLength = path.length();
            path.append("[").append(nums[i]).append("] ");
            used[i] = true;
            current.add(nums[i]);
            
            // 递归探索
            visualizeBacktracking(current, nums, used, depth + 1, path);
            
            // 回溯
            path.setLength(pathLength);
            used[i] = false;
            current.remove(current.size() - 1);
        }
    }
    
    // ========================= 性能测试工具 =========================
    
    /**
     * 算法性能比较
     * 
     * @param nums 输入数组
     */
    public static void compareAlgorithms(int[] nums) {
        System.out.println("\n性能比较: " + Arrays.toString(nums));
        
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
    
    // ========================= 应用场景 =========================
    
    /**
     * 密码生成器
     * 
     * @param charset 可用字符集合
     * @param length 密码长度
     * @return 所有可能的密码
     */
    public static List<String> generatePasswords(Character[] charset, int length) {
        int[] nums = new int[charset.length];
        for (int i = 0; i < charset.length; i++) {
            nums[i] = charset[i]; // 使用字符的ASCII值
        }
        
        List<List<Integer>> permutations = permuteUniqueBacktracking(nums);
        List<String> passwords = new ArrayList<>();
        
        for (List<Integer> perm : permutations) {
            StringBuilder sb = new StringBuilder();
            for (int num : perm) {
                sb.append((char) num);
            }
            passwords.add(sb.toString());
        }
        
        System.out.println("生成密码数量: " + passwords.size());
        return passwords;
    }
    
    /**
     * 游戏关卡生成器
     * 
     * @param entities 关卡元素
     * @param size 关卡大小
     * @return 所有可能关卡组合
     */
    public static List<String> generateLevels(String[] entities, int size) {
        List<List<Integer>> permutations;
        if (size < 5) {
            int[] nums = new int[size];
            Arrays.fill(nums, 1);
            permutations = permuteUniqueBacktracking(nums);
        } else {
            // 大数据集使用频率法优化性能
            int[] nums = new int[size];
            Random rand = new Random();
            for (int i = 0; i < size; i++) {
                nums[i] = rand.nextInt(entities.length);
            }
            permutations = permuteUniqueFrequency(nums);
        }
        
        List<String> levels = new ArrayList<>();
        for (List<Integer> perm : permutations) {
            StringBuilder sb = new StringBuilder();
            for (int index : perm) {
                sb.append(entities[index % entities.length]).append(" ");
            }
            levels.add(sb.toString());
        }
        
        System.out.println("生成关卡数: " + levels.size());
        return levels;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        System.out.println("====== 全排列测试 ======");
        int[][] testCases = {
            {1, 1, 2},       // 基础重复
            {1, 2, 3},       // 无重复
            {2, 2, 2},       // 全相同
            {3, 3, 1, 1},    // 两组重复
            {1},             // 单个元素
            {}               // 空数组
        };
        
        for (int i = 0; i < testCases.length; i++) {
            System.out.println("\n测试用例 " + (i+1) + ": " + Arrays.toString(testCases[i]));
            
            // 三种解法
            List<List<Integer>> res1 = permuteUniqueBacktracking(testCases[i]);
            List<List<Integer>> res2 = permuteUniqueFrequency(testCases[i]);
            List<List<Integer>> res3 = permuteUniqueIterative(testCases[i]);
            
            System.out.println("回溯法: " + res1.size() + " 种排列");
            if (testCases[i].length <= 3) {
                System.out.println("排列结果: " + res1);
            }
            
            // 可视化过程
            if (testCases[i].length == 3) {
                visualizePermutations(testCases[i]);
            }
        }
        
        // 性能比较
        System.out.println("\n====== 性能比较 ======");
        compareAlgorithms(new int[]{1, 1, 2, 2});    // 中等规模
        compareAlgorithms(new int[]{1, 2, 3, 4, 5});  // 无重复元素
        compareAlgorithms(new int[]{1, 1, 2, 2, 3});  // 混合重复
        
        // 应用场景
        System.out.println("\n====== 应用场景 ======");
        System.out.println("1. 密码生成器:");
        Character[] charset = {'A', 'B', 'C', '1', '2'};
        List<String> passwords = generatePasswords(charset, 3);
        if (passwords.size() < 10) {
            System.out.println("生成的密码: " + passwords);
        }
        
        System.out.println("\n2. 游戏关卡生成器:");
        String[] entities = {"敌人", "道具", "障碍", "宝藏"};
        List<String> levels = generateLevels(entities, 4);
        if (levels.size() < 10) {
            System.out.println("生成的关卡: " + levels);
        }
    }
}