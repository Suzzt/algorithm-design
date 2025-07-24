package main.java.org.dao.qa;

import java.util.*;

/**
 * 最长连续序列问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个未排序的整数数组，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 要求时间复杂度为O(n)。
 * 
 * <p><b>示例</b>:
 * 输入: [100, 4, 200, 1, 3, 2]
 * 输出: 4
 * 解释: 最长连续序列是 [1, 2, 3, 4]，长度为4。
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第128题)
 * 
 * <p><b>解题思路</b>:
 * 1. 哈希表法: 使用HashSet存储所有数字，然后遍历数组，对每个数字检查其是否是一个连续序列的起点
 * 2. 并查集法: 将连续的数字合并到同一个集合，并记录集合的大小
 * 3. 排序法: 排序后遍历数组统计连续序列（不满足O(n)要求）
 * 
 * <p><b>时间复杂度</b>:
 *  哈希表法: O(n)
 *  并查集法: O(n)
 *  排序法: O(nlogn)
 * 
 * <p><b>空间复杂度</b>:
 *  所有方法: O(n)
 * 
 * <p><b>应用场景</b>:
 * 1. 用户行为分析（连续活跃天数）
 * 2. 金融交易连续记录检测
 * 3. 基因序列连续片段识别
 * 4. 日志分析中的连续事件检测
 * 5. 游戏成就系统（连续登录）
 */

public class LongestConsecutiveSequence {
    
    // ========================= 解法1: 哈希表法 =========================
    
    /**
     * 哈希表解法
     * 
     * @param nums 输入数组
     * @return 最长连续序列长度
     */
    public static int longestConsecutiveHashSet(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }
        
        int longestStreak = 0;
        
        for (int num : numSet) {
            // 只有当num是连续序列的起点时（即num-1不在集合中）
            if (!numSet.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;
                
                // 向后查找连续的数字
                while (numSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }
                
                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }
        
        return longestStreak;
    }
    
    // ========================= 解法2: 并查集法 =========================
    
    /**
     * 并查集解法
     * 
     * @param nums 输入数组
     * @return 最长连续序列长度
     */
    public static int longestConsecutiveUnionFind(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        Map<Integer, Integer> parent = new HashMap<>();
        Map<Integer, Integer> size = new HashMap<>();
        
        // 初始化并查集
        for (int num : nums) {
            parent.put(num, num);
            size.put(num, 1);
        }
        
        // 合并连续的数字
        for (int num : nums) {
            if (parent.containsKey(num - 1)) {
                union(num, num - 1, parent, size);
            }
            if (parent.containsKey(num + 1)) {
                union(num, num + 1, parent, size);
            }
        }
        
        // 找出最大集合大小
        return Collections.max(size.values());
    }
    
    // 并查集辅助方法：查找根节点
    private static int find(int x, Map<Integer, Integer> parent) {
        if (parent.get(x) != x) {
            parent.put(x, find(parent.get(x), parent));
        }
        return parent.get(x);
    }
    
    // 并查集辅助方法：合并两个集合
    private static void union(int x, int y, Map<Integer, Integer> parent, Map<Integer, Integer> size) {
        int rootX = find(x, parent);
        int rootY = find(y, parent);
        
        if (rootX == rootY) return;
        
        // 按秩合并
        if (size.get(rootX) < size.get(rootY)) {
            parent.put(rootX, rootY);
            size.put(rootY, size.get(rootY) + size.get(rootX));
        } else {
            parent.put(rootY, rootX);
            size.put(rootX, size.get(rootX) + size.get(rootY));
        }
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 用户连续活跃天数分析
     * 
     * @param activeDays 用户活跃日期（时间戳）
     * @return 最长连续活跃天数
     */
    public static int longestConsecutiveActiveDays(long[] activeDays) {
        // 将时间戳转换为天数（简化处理）
        int[] days = new int[activeDays.length];
        for (int i = 0; i < activeDays.length; i++) {
            days[i] = (int) (activeDays[i] / (24 * 60 * 60 * 1000)); // 转换为天数
        }
        return longestConsecutiveHashSet(days);
    }
    
    /**
     * 金融交易连续记录检测
     * 
     * @param transactionIds 交易ID数组
     * @return 最长连续交易记录长度
     */
    public static int detectConsecutiveTransactions(int[] transactionIds) {
        return longestConsecutiveUnionFind(transactionIds);
    }
    
    /**
     * 基因序列连续片段识别
     * 
     * @param genePositions 基因在染色体上的位置
     * @return 最长连续片段长度
     */
    public static int findLongestGeneSegment(int[] genePositions) {
        return longestConsecutiveHashSet(genePositions);
    }
    
    /**
     * 日志分析 - 连续事件检测
     * 
     * @param eventTimes 事件发生时间（时间戳）
     * @return 最长连续事件序列长度
     */
    public static int detectConsecutiveEvents(long[] eventTimes) {
        // 将时间戳转换为小时（简化）
        int[] hours = new int[eventTimes.length];
        for (int i = 0; i < eventTimes.length; i++) {
            hours[i] = (int) (eventTimes[i] / (60 * 60 * 1000));
        }
        return longestConsecutiveHashSet(hours);
    }
    
    /**
     * 游戏成就系统 - 连续登录天数
     * 
     * @param loginDays 登录日期（时间戳）
     * @return 最长连续登录天数
     */
    public static int longestConsecutiveLogin(long[] loginDays) {
        return longestConsecutiveActiveDays(loginDays);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化连续序列
     * 
     * @param nums 输入数组
     */
    public static void visualizeConsecutiveSequence(int[] nums) {
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }
        
        System.out.println("\n数组: " + Arrays.toString(nums));
        System.out.println("连续序列可视化:");
        
        int longest = 0;
        List<List<Integer>> sequences = new ArrayList<>();
        
        for (int num : numSet) {
            if (!numSet.contains(num - 1)) {
                List<Integer> sequence = new ArrayList<>();
                int current = num;
                sequence.add(current);
                
                while (numSet.contains(current + 1)) {
                    current++;
                    sequence.add(current);
                }
                
                sequences.add(sequence);
                longest = Math.max(longest, sequence.size());
            }
        }
        
        // 打印所有连续序列
        for (List<Integer> seq : sequences) {
            if (seq.size() == longest) {
                System.out.print("★ ");
            } else {
                System.out.print("  ");
            }
            System.out.println(seq);
        }
        
        System.out.println("最长连续序列长度: " + longest);
    }
    
    /**
     * 可视化并查集合并过程
     * 
     * @param nums 输入数组
     */
    public static void visualizeUnionFindProcess(int[] nums) {
        if (nums == null || nums.length == 0) return;
        
        Map<Integer, Integer> parent = new HashMap<>();
        Map<Integer, Integer> size = new HashMap<>();
        
        // 初始化并查集
        for (int num : nums) {
            parent.put(num, num);
            size.put(num, 1);
        }
        
        System.out.println("\n并查集初始化:");
        printUnionFindState(parent, size);
        
        // 合并连续的数字
        for (int num : nums) {
            if (parent.containsKey(num - 1)) {
                System.out.printf("\n合并 %d 和 %d:\n", num, num - 1);
                union(num, num - 1, parent, size);
                printUnionFindState(parent, size);
            }
            if (parent.containsKey(num + 1)) {
                System.out.printf("\n合并 %d 和 %d:\n", num, num + 1);
                union(num, num + 1, parent, size);
                printUnionFindState(parent, size);
            }
        }
        
        System.out.println("\n最终并查集状态:");
        printUnionFindState(parent, size);
    }
    
    // 打印并查集状态
    private static void printUnionFindState(Map<Integer, Integer> parent, Map<Integer, Integer> size) {
        System.out.println("数字 | 父节点 | 集合大小");
        System.out.println("----|--------|--------");
        for (int num : parent.keySet()) {
            System.out.printf("%3d | %6d | %6d\n", num, parent.get(num), size.get(num));
        }
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param size 数组大小
     */
    public static void comparePerformance(int size) {
        int[] nums = generateRandomArray(size);
        
        long start, end;
        
        // 测试哈希表法
        start = System.nanoTime();
        int result1 = longestConsecutiveHashSet(nums);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试并查集法
        start = System.nanoTime();
        int result2 = longestConsecutiveUnionFind(nums);
        end = System.nanoTime();
        long time2 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.println("数组大小: " + size);
        System.out.println("===============================");
        System.out.println("方法       | 时间(ns) | 结果");
        System.out.println("-----------|----------|------");
        System.out.printf("哈希表法   | %8d | %d\n", time1, result1);
        System.out.printf("并查集法   | %8d | %d\n", time2, result2);
        System.out.println("===============================");
    }
    
    // 生成随机数组（包含连续序列）
    private static int[] generateRandomArray(int size) {
        Random random = new Random();
        int[] nums = new int[size];
        
        // 生成一个连续序列
        int start = random.nextInt(1000);
        int length = random.nextInt(size / 2) + size / 2;
        
        for (int i = 0; i < length; i++) {
            if (i < size) {
                nums[i] = start + i;
            }
        }
        
        // 填充剩余位置
        for (int i = length; i < size; i++) {
            nums[i] = random.nextInt(10000);
        }
        
        // 打乱顺序
        for (int i = 0; i < size; i++) {
            int j = random.nextInt(size);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
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
        testCase("示例测试", new int[]{100, 4, 200, 1, 3, 2}, 4);
        testCase("连续序列在中间", new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}, 9);
        testCase("多个连续序列", new int[]{1, 2, 3, 10, 11, 12, 20, 21}, 3);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("空数组", new int[]{}, 0);
        testCase("单元素数组", new int[]{5}, 1);
        testCase("全相同元素", new int[]{7, 7, 7, 7}, 1);
        testCase("无连续序列", new int[]{1, 3, 5, 7, 9}, 1);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 用户连续活跃天数
        long[] activeDays = {
            1672531200000L, // 2023-01-01
            1672617600000L, // 2023-01-02
            1672704000000L, // 2023-01-03
            1672963200000L, // 2023-01-06
            1673049600000L, // 2023-01-07
            1673136000000L  // 2023-01-08
        };
        System.out.println("最长连续活跃天数: " + longestConsecutiveActiveDays(activeDays));
        
        // 场景2: 金融交易连续记录
        int[] transactionIds = {1001, 1002, 1003, 1005, 1006, 1008, 1009, 1010};
        System.out.println("最长连续交易记录: " + detectConsecutiveTransactions(transactionIds));
        
        // 场景3: 基因序列连续片段
        int[] genePositions = {100, 105, 101, 103, 102, 107, 106};
        System.out.println("基因序列最长连续片段: " + findLongestGeneSegment(genePositions));
        
        // 场景4: 连续事件检测
        long[] eventTimes = {
            1672531200000L, // 2023-01-01 00:00
            1672534800000L, // 2023-01-01 01:00
            1672538400000L, // 2023-01-01 02:00
            1672542000000L, // 2023-01-01 03:00
            1672624800000L  // 2023-01-02 02:00
        };
        System.out.println("最长连续事件序列(小时): " + detectConsecutiveEvents(eventTimes));
        
        // 场景5: 游戏连续登录
        long[] loginDays = {
            1672531200000L, // 2023-01-01
            1672617600000L, // 2023-01-02
            1672704000000L, // 2023-01-03
            1672790400000L, // 2023-01-04
            1672876800000L  // 2023-01-05
        };
        System.out.println("最长连续登录天数: " + longestConsecutiveLogin(loginDays));
        
        // 可视化测试
        System.out.println("\n可视化测试:");
        visualizeConsecutiveSequence(new int[]{100, 4, 200, 1, 3, 2});
        visualizeUnionFindProcess(new int[]{1, 2, 3, 5, 6, 8, 9});
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(1000);
        comparePerformance(10000);
        comparePerformance(100000);
    }
    
    private static void testCase(String name, int[] nums, int expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("数组: " + Arrays.toString(nums));
        
        int result1 = longestConsecutiveHashSet(nums);
        int result2 = longestConsecutiveUnionFind(nums);
        
        System.out.println("哈希表法结果: " + result1);
        System.out.println("并查集法结果: " + result2);
        
        boolean passed = (result1 == expected) && (result2 == expected);
        System.out.println("测试结果: " + (passed ? "通过" : "失败"));
        
        if (!passed) {
            System.out.println("预期结果: " + expected);
        }
        
        // 可视化小规模案例
        if (nums.length <= 20) {
            visualizeConsecutiveSequence(nums);
        }
    }
}