package main.java.org.dao.qa;

import java.util.*;

/**
 * 合并区间问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个区间的集合，合并所有重叠的区间。
 * 区间 [i,j] 和 [k,l] 重叠当且仅当 i <= l 且 k <= j。
 * 
 * <p><b>示例</b>:
 * 输入: [[1,3],[2,6],[8,10],[15,18]]
 * 输出: [[1,6],[8,10],[15,18]]
 * 解释: 区间 [1,3] 和 [2,6] 重叠，将它们合并为 [1,6]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第56题)
 * 
 * <p><b>解题思路</b>:
 * 1. 排序贪心法: 按起始位置排序，依次合并重叠区间
 * 2. 分治法: 递归合并左右两部分的区间
 * 3. 栈辅助法: 用栈维护已合并的区间
 * 
 * <p><b>时间复杂度</b>:
 *  排序贪心法: O(n log n)
 *  分治法: O(n log n)
 *  栈辅助法: O(n log n)
 * 
 * <p><b>空间复杂度</b>:
 *  排序贪心法: O(1) 额外空间
 *  分治法: O(log n) 递归栈空间
 *  栈辅助法: O(n) 栈空间
 * 
 * <p><b>应用场景</b>:
 * 1. 会议室安排和时间冲突检测
 * 2. 数据库区间合并优化
 * 3. 网络资源分配管理
 * 4. 内存管理中的碎片整理
 * 5. 日程安排系统
 */

public class MergeIntervals {
    
    // ========================= 解法1: 排序贪心法 (推荐) =========================
    
    /**
     * 排序贪心解法
     * 
     * @param intervals 区间数组，每个区间用两元素数组表示[start, end]
     * @return 合并后的区间数组
     */
    public static int[][] mergeGreedy(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        
        // 按区间起始位置排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        List<int[]> merged = new ArrayList<>();
        int[] currentInterval = intervals[0];
        merged.add(currentInterval);
        
        for (int i = 1; i < intervals.length; i++) {
            int[] nextInterval = intervals[i];
            
            // 检查是否重叠：当前区间的结束位置 >= 下一个区间的开始位置
            if (currentInterval[1] >= nextInterval[0]) {
                // 合并区间：更新当前区间的结束位置
                currentInterval[1] = Math.max(currentInterval[1], nextInterval[1]);
            } else {
                // 不重叠，添加新区间
                currentInterval = nextInterval;
                merged.add(currentInterval);
            }
        }
        
        return merged.toArray(new int[merged.size()][]);
    }
    
    // ========================= 解法2: 分治法 =========================
    
    /**
     * 分治解法
     * 
     * @param intervals 区间数组
     * @return 合并后的区间数组
     */
    public static int[][] mergeDivideConquer(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        
        // 先排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        List<int[]> result = mergeDivideConquerHelper(
            Arrays.asList(intervals), 0, intervals.length - 1);
        
        return result.toArray(new int[result.size()][]);
    }
    
    private static List<int[]> mergeDivideConquerHelper(List<int[]> intervals, int left, int right) {
        if (left == right) {
            return Arrays.asList(intervals.get(left));
        }
        
        int mid = left + (right - left) / 2;
        List<int[]> leftMerged = mergeDivideConquerHelper(intervals, left, mid);
        List<int[]> rightMerged = mergeDivideConquerHelper(intervals, mid + 1, right);
        
        return mergeTwoSortedIntervals(leftMerged, rightMerged);
    }
    
    // 合并两个已排序的区间列表
    private static List<int[]> mergeTwoSortedIntervals(List<int[]> left, List<int[]> right) {
        List<int[]> result = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < left.size() && j < right.size()) {
            int[] leftInterval = left.get(i);
            int[] rightInterval = right.get(j);
            
            if (leftInterval[0] <= rightInterval[0]) {
                result.add(leftInterval);
                i++;
            } else {
                result.add(rightInterval);
                j++;
            }
        }
        
        // 添加剩余区间
        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }
        
        // 对结果进行最终合并
        return finalMerge(result);
    }
    
    // 对已排序区间进行最终合并
    private static List<int[]> finalMerge(List<int[]> intervals) {
        if (intervals.isEmpty()) return intervals;
        
        List<int[]> merged = new ArrayList<>();
        int[] current = intervals.get(0);
        merged.add(current);
        
        for (int i = 1; i < intervals.size(); i++) {
            int[] next = intervals.get(i);
            if (current[1] >= next[0]) {
                current[1] = Math.max(current[1], next[1]);
            } else {
                current = next;
                merged.add(current);
            }
        }
        
        return merged;
    }
    
    // ========================= 解法3: 栈辅助法 =========================
    
    /**
     * 栈辅助解法
     * 
     * @param intervals 区间数组
     * @return 合并后的区间数组
     */
    public static int[][] mergeWithStack(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        
        // 按区间起始位置排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        
        Stack<int[]> stack = new Stack<>();
        stack.push(intervals[0]);
        
        for (int i = 1; i < intervals.length; i++) {
            int[] current = intervals[i];
            int[] top = stack.peek();
            
            // 如果当前区间与栈顶区间重叠
            if (top[1] >= current[0]) {
                // 合并区间：更新栈顶区间的结束位置
                top[1] = Math.max(top[1], current[1]);
            } else {
                // 不重叠，将当前区间入栈
                stack.push(current);
            }
        }
        
        // 将栈中的区间转换为数组
        List<int[]> result = new ArrayList<>(stack);
        return result.toArray(new int[result.size()][]);
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 会议室调度系统
     * 
     * @param meetings 会议时间数组 [[开始时间, 结束时间, 会议ID]]
     * @return 合并后的会议安排
     */
    public static List<Meeting> scheduleMeetings(int[][] meetings) {
        if (meetings == null || meetings.length == 0) {
            return new ArrayList<>();
        }
        
        // 转换为Meeting对象并排序
        List<Meeting> meetingList = new ArrayList<>();
        for (int i = 0; i < meetings.length; i++) {
            meetingList.add(new Meeting(meetings[i][0], meetings[i][1], i));
        }
        
        meetingList.sort((a, b) -> Integer.compare(a.start, b.start));
        
        List<Meeting> merged = new ArrayList<>();
        Meeting current = meetingList.get(0);
        merged.add(current);
        
        for (int i = 1; i < meetingList.size(); i++) {
            Meeting next = meetingList.get(i);
            
            if (current.end >= next.start) {
                // 合并会议时间
                current.end = Math.max(current.end, next.end);
                current.addConflictMeeting(next.id);
            } else {
                current = new Meeting(next.start, next.end, next.id);
                merged.add(current);
            }
        }
        
        return merged;
    }
    
    // 会议类
    static class Meeting {
        int start, end, id;
        List<Integer> conflictIds;
        
        Meeting(int start, int end, int id) {
            this.start = start;
            this.end = end;
            this.id = id;
            this.conflictIds = new ArrayList<>();
            this.conflictIds.add(id);
        }
        
        void addConflictMeeting(int conflictId) {
            this.conflictIds.add(conflictId);
        }
        
        @Override
        public String toString() {
            return String.format("会议时间[%d-%d], 涉及会议ID: %s", 
                               start, end, conflictIds);
        }
    }
    
    /**
     * 内存碎片整理
     * 
     * @param memoryBlocks 内存块数组 [[起始地址, 结束地址]]
     * @return 整理后的连续内存块
     */
    public static int[][] defragmentMemory(int[][] memoryBlocks) {
        System.out.println("内存碎片整理前:");
        printIntervals(memoryBlocks);
        
        int[][] merged = mergeGreedy(memoryBlocks);
        
        System.out.println("内存碎片整理后:");
        printIntervals(merged);
        
        // 计算整理效果
        int originalBlocks = memoryBlocks.length;
        int mergedBlocks = merged.length;
        int savedBlocks = originalBlocks - mergedBlocks;
        
        System.out.printf("整理效果: 原有%d个内存块，整理后%d个，节省%d个碎片\n", 
                         originalBlocks, mergedBlocks, savedBlocks);
        
        return merged;
    }
    
    /**
     * 网络带宽分配
     * 
     * @param bandwidthRequests 带宽请求 [[开始时间, 结束时间, 带宽需求]]
     * @return 优化后的带宽分配方案
     */
    public static List<BandwidthAllocation> optimizeBandwidth(int[][] bandwidthRequests) {
        List<BandwidthAllocation> allocations = new ArrayList<>();
        
        // 转换为BandwidthAllocation对象
        for (int i = 0; i < bandwidthRequests.length; i++) {
            allocations.add(new BandwidthAllocation(
                bandwidthRequests[i][0], 
                bandwidthRequests[i][1], 
                bandwidthRequests[i][2],
                i
            ));
        }
        
        // 按时间排序
        allocations.sort((a, b) -> Integer.compare(a.startTime, b.startTime));
        
        List<BandwidthAllocation> optimized = new ArrayList<>();
        BandwidthAllocation current = allocations.get(0);
        optimized.add(current);
        
        for (int i = 1; i < allocations.size(); i++) {
            BandwidthAllocation next = allocations.get(i);
            
            // 时间重叠且带宽需求相同，可以合并
            if (current.endTime >= next.startTime && current.bandwidth == next.bandwidth) {
                current.endTime = Math.max(current.endTime, next.endTime);
                current.addRequest(next.requestId);
            } else {
                current = new BandwidthAllocation(
                    next.startTime, next.endTime, next.bandwidth, next.requestId);
                optimized.add(current);
            }
        }
        
        return optimized;
    }
    
    // 带宽分配类
    static class BandwidthAllocation {
        int startTime, endTime, bandwidth, requestId;
        List<Integer> mergedRequests;
        
        BandwidthAllocation(int start, int end, int bandwidth, int id) {
            this.startTime = start;
            this.endTime = end;
            this.bandwidth = bandwidth;
            this.requestId = id;
            this.mergedRequests = new ArrayList<>();
            this.mergedRequests.add(id);
        }
        
        void addRequest(int id) {
            this.mergedRequests.add(id);
        }
        
        @Override
        public String toString() {
            return String.format("时间段[%d-%d], 带宽%dMbps, 合并请求: %s", 
                               startTime, endTime, bandwidth, mergedRequests);
        }
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化合并过程
     * 
     * @param intervals 原始区间数组
     */
    public static void visualizeMergeProcess(int[][] intervals) {
        System.out.println("\n区间合并过程可视化:");
        System.out.println("原始区间: " + Arrays.deepToString(intervals));
        
        if (intervals == null || intervals.length <= 1) {
            System.out.println("无需合并");
            return;
        }
        
        // 排序
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        System.out.println("排序后: " + Arrays.deepToString(intervals));
        
        System.out.println("\n步骤 | 当前区间 | 下一区间 | 操作 | 结果");
        System.out.println("-----|----------|----------|------|----------");
        
        List<int[]> merged = new ArrayList<>();
        int[] current = intervals[0];
        merged.add(current);
        
        System.out.printf("%4d | [%d,%d]   | %-8s | 初始 | %s\n", 
                         1, current[0], current[1], "-", formatIntervals(merged));
        
        for (int i = 1; i < intervals.length; i++) {
            int[] next = intervals[i];
            
            if (current[1] >= next[0]) {
                int oldEnd = current[1];
                current[1] = Math.max(current[1], next[1]);
                System.out.printf("%4d | [%d,%d]   | [%d,%d]   | 合并 | %s\n", 
                                 i + 1, current[0], oldEnd, next[0], next[1], 
                                 formatIntervals(merged));
            } else {
                current = next;
                merged.add(current);
                System.out.printf("%4d | [%d,%d]   | [%d,%d]   | 新增 | %s\n", 
                                 i + 1, merged.get(merged.size()-2)[0], 
                                 merged.get(merged.size()-2)[1], 
                                 next[0], next[1], formatIntervals(merged));
            }
        }
        
        System.out.println("\n最终结果: " + formatIntervals(merged));
    }
    
    /**
     * 打印区间数组
     * 
     * @param intervals 区间数组
     */
    public static void printIntervals(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            System.out.println("[]");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < intervals.length; i++) {
            sb.append("[").append(intervals[i][0]).append(",").append(intervals[i][1]).append("]");
            if (i < intervals.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        System.out.println(sb.toString());
    }
    
    // 格式化区间列表为字符串
    private static String formatIntervals(List<int[]> intervals) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < intervals.size(); i++) {
            int[] interval = intervals.get(i);
            sb.append("[").append(interval[0]).append(",").append(interval[1]).append("]");
            if (i < intervals.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param size 区间数量
     * @param range 区间范围
     */
    public static void comparePerformance(int size, int range) {
        // 生成测试数据
        int[][] intervals = generateRandomIntervals(size, range);
        
        long start, end;
        
        // 测试排序贪心法
        start = System.nanoTime();
        mergeGreedy(Arrays.copyOf(intervals, intervals.length));
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试分治法
        start = System.nanoTime();
        mergeDivideConquer(Arrays.copyOf(intervals, intervals.length));
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试栈辅助法
        start = System.nanoTime();
        mergeWithStack(Arrays.copyOf(intervals, intervals.length));
        end = System.nanoTime();
        long time3 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("区间数量: %d, 范围: [0, %d]\n", size, range);
        System.out.println("=======================================");
        System.out.println("方法            | 时间(ms)   | 相对速度");
        System.out.println("----------------|------------|----------");
        System.out.printf("排序贪心法      | %.6f | 基准\n", time1 / 1_000_000.0);
        System.out.printf("分治法          | %.6f | %.2fx\n", time2 / 1_000_000.0, (double)time2/time1);
        System.out.printf("栈辅助法        | %.6f | %.2fx\n", time3 / 1_000_000.0, (double)time3/time1);
        System.out.println("=======================================");
    }
    
    // 生成随机区间数组
    private static int[][] generateRandomIntervals(int size, int range) {
        Random rand = new Random();
        int[][] intervals = new int[size][2];
        
        for (int i = 0; i < size; i++) {
            int start = rand.nextInt(range);
            int end = start + rand.nextInt(range / 4 + 1); // 确保end >= start
            intervals[i][0] = start;
            intervals[i][1] = end;
        }
        
        return intervals;
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
        
        testCase("示例测试1", new int[][]{{1,3},{2,6},{8,10},{15,18}}, 
                 new int[][]{{1,6},{8,10},{15,18}});
        testCase("示例测试2", new int[][]{{1,4},{4,5}}, 
                 new int[][]{{1,5}});
        testCase("完全重叠", new int[][]{{1,4},{2,3}}, 
                 new int[][]{{1,4}});
        testCase("相邻区间", new int[][]{{1,3},{4,6}}, 
                 new int[][]{{1,3},{4,6}});
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        testCase("空数组", new int[][]{}, new int[][]{});
        testCase("单个区间", new int[][]{{1,4}}, new int[][]{{1,4}});
        testCase("两个不重叠", new int[][]{{1,2},{3,4}}, new int[][]{{1,2},{3,4}});
        testCase("多个相同区间", new int[][]{{1,3},{1,3},{1,3}}, new int[][]{{1,3}});
        testCase("逆序输入", new int[][]{{4,6},{1,3},{2,5}}, new int[][]{{1,6}});
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 会议室调度
        System.out.println("\n会议室调度:");
        int[][] meetings = {{9,10},{10,11},{11,12},{13,14},{14,15}};
        System.out.println("会议安排: " + Arrays.deepToString(meetings));
        List<Meeting> scheduled = scheduleMeetings(meetings);
        for (Meeting meeting : scheduled) {
            System.out.println(meeting);
        }
        
        // 场景2: 内存碎片整理
        System.out.println("\n内存碎片整理:");
        int[][] memoryBlocks = {{100,200},{150,300},{400,500},{450,600}};
        defragmentMemory(memoryBlocks);
        
        // 场景3: 网络带宽分配
        System.out.println("\n网络带宽分配:");
        int[][] bandwidthRequests = {{1,3,100},{2,4,100},{5,7,200},{6,8,200}};
        System.out.println("带宽请求: [[时间段,带宽需求]]");
        for (int[] request : bandwidthRequests) {
            System.out.printf("  [%d-%d, %dMbps]\n", request[0], request[1], request[2]);
        }
        List<BandwidthAllocation> optimized = optimizeBandwidth(bandwidthRequests);
        System.out.println("优化后分配:");
        for (BandwidthAllocation allocation : optimized) {
            System.out.println("  " + allocation);
        }
        
        // 可视化测试
        System.out.println("\n可视化过程:");
        visualizeMergeProcess(new int[][]{{1,3},{2,6},{8,10},{15,18}});
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100, 1000);
        comparePerformance(1000, 10000);
        comparePerformance(5000, 50000);
    }
    
    private static void testCase(String name, int[][] intervals, int[][] expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("输入: " + Arrays.deepToString(intervals));
        
        int[][] greedyResult = mergeGreedy(copyArray(intervals));
        int[][] divideResult = mergeDivideConquer(copyArray(intervals));
        int[][] stackResult = mergeWithStack(copyArray(intervals));
        
        System.out.println("排序贪心法结果: " + Arrays.deepToString(greedyResult));
        System.out.println("分治法结果: " + Arrays.deepToString(divideResult));
        System.out.println("栈辅助法结果: " + Arrays.deepToString(stackResult));
        System.out.println("期望结果: " + Arrays.deepToString(expected));
        
        // 验证结果一致性
        boolean isCorrect = Arrays.deepEquals(greedyResult, expected) &&
                            Arrays.deepEquals(divideResult, expected) &&
                            Arrays.deepEquals(stackResult, expected);
        
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 可视化小规模案例
        if (intervals.length <= 10 && intervals.length > 0) {
            visualizeMergeProcess(copyArray(intervals));
        }
    }
    
    // 复制二维数组
    private static int[][] copyArray(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }
}