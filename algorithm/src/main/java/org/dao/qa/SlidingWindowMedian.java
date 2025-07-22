package main.java.org.dao.qa;

import java.util.*;

/**
 * 滑动窗口中位数问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个数组nums，有一个大小为k的滑动窗口从数组最左侧移动到最右侧。
 * 我们只能看到滑动窗口中的k个数字。窗口每次向右移动一位。
 * 要求返回每个窗口的中位数所组成的新数组。
 * 
 * <p><b>示例</b>:
 * 输入: nums = [1,3,-1,-3,5,3,6,7], k = 3
 * 输出: [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第480题)
 * 
 * <p><b>解题思路</b>:
 * 1. 双堆法: 使用一个大顶堆和小顶堆维护当前窗口的数值分布
 * 2. 平衡因子: 保持两个堆的大小平衡以确保高效获取中位数
 * 3. 延迟删除: 优化删除操作的性能
 * 4. 红黑树: 使用TreeSet作为替代解法
 * 
 * <p><b>时间复杂度</b>:
 *  双堆法: O(nlogk)
 *  红黑树法: O(nlogk)
 * 
 * <p><b>空间复杂度</b>:
 *  所有方法: O(k)
 * 
 * <p><b>应用场景</b>:
 * 1. 金融数据实时分析
 * 2. 网络流量监控
 * 3. 传感器数据处理
 * 4. 股票价格分析
 * 5. 实时视频处理
 */

public class SlidingWindowMedian {
    
    // ========================= 核心解法1: 双堆法 =========================
    
    /**
     * 双堆法解决方案（优先推荐）
     * 
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 每个窗口的中位数数组
     */
    public static double[] medianSlidingWindow(int[] nums, int k) {
        // 初始化双堆
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        Map<Integer, Integer> deferred = new HashMap<>();
        
        double[] result = new double[nums.length - k + 1];
        int balance = 0; // 平衡因子：正数表示maxHeap需调整，负数表示minHeap需调整
        
        // 初始化第一个窗口
        for (int i = 0; i < k; i++) {
            addNum(nums[i], minHeap, maxHeap);
        }
        rebalanceHeaps(minHeap, maxHeap, deferred);
        result[0] = getMedian(minHeap, maxHeap, k);
        
        // 滑动窗口处理后续元素
        for (int i = k; i < nums.length; i++) {
            int remove = nums[i - k];  // 移出窗口的元素
            int add = nums[i];          // 加入窗口的元素
            
            // 处理移除元素
            balance += removeNum(remove, minHeap, maxHeap, deferred);
            
            // 添加新元素
            balance += addNum(add, minHeap, maxHeap);
            
            // 平衡堆并清理延迟删除元素
            rebalanceHeaps(minHeap, maxHeap, deferred);
            cleanHeaps(minHeap, maxHeap, deferred);
            
            // 获取当前窗口中位数
            result[i - k + 1] = getMedian(minHeap, maxHeap, k);
        }
        
        return result;
    }
    
    // 添加元素到合适的堆
    private static int addNum(int num, PriorityQueue<Integer> minHeap, PriorityQueue<Integer> maxHeap) {
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.offer(num);
            return 1; // 添加到大顶堆
        } else {
            minHeap.offer(num);
            return -1; // 添加到小顶堆
        }
    }
    
    // 标记元素为延迟删除
    private static int removeNum(int num, PriorityQueue<Integer> minHeap, PriorityQueue<Integer> maxHeap, 
                                Map<Integer, Integer> deferred) {
        if (!maxHeap.isEmpty() && num <= maxHeap.peek()) {
            deferred.put(num, deferred.getOrDefault(num, 0) + 1);
            return -1; // 从大顶堆移除
        } else {
            deferred.put(num, deferred.getOrDefault(num, 0) + 1);
            return 1; // 从小顶堆移除
        }
    }
    
    // 重新平衡两个堆的大小
    private static void rebalanceHeaps(PriorityQueue<Integer> minHeap, PriorityQueue<Integer> maxHeap, 
                                     Map<Integer, Integer> deferred) {
        // 保证大顶堆大小 >= 小顶堆大小
        while (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
        while (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        }
    }
    
    // 清理堆顶部的延迟删除元素
    private static void cleanHeaps(PriorityQueue<Integer> minHeap, PriorityQueue<Integer> maxHeap, 
                                  Map<Integer, Integer> deferred) {
        // 清理大顶堆
        while (!maxHeap.isEmpty() && deferred.getOrDefault(maxHeap.peek(), 0) > 0) {
            int num = maxHeap.poll();
            deferred.put(num, deferred.get(num) - 1);
        }
        
        // 清理小顶堆
        while (!minHeap.isEmpty() && deferred.getOrDefault(minHeap.peek(), 0) > 0) {
            int num = minHeap.poll();
            deferred.put(num, deferred.get(num) - 1);
        }
    }
    
    // 获取当前中位数
    private static double getMedian(PriorityQueue<Integer> minHeap, PriorityQueue<Integer> maxHeap, int k) {
        if (k % 2 == 1) {
            return (double) maxHeap.peek();
        } else {
            return ((double) maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }
    
    // ========================= 核心解法2: 红黑树法 =========================
    
    /**
     * 红黑树（TreeSet）解法
     * 
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 每个窗口的中位数数组
     */
    public static double[] medianSlidingWindowTreeSet(int[] nums, int k) {
        // 创建TreeSet，使用自定义比较器处理相同值的情况
        Comparator<int[]> cmp = (a, b) -> a[0] != b[0] ? 
            Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]);
        TreeSet<int[]> set = new TreeSet<>(cmp);
        
        double[] result = new double[nums.length - k + 1];
        
        // 初始化窗口
        for (int i = 0; i < k; i++) {
            set.add(new int[]{nums[i], i});
        }
        
        // 获取第一个窗口的中位数
        result[0] = getMedianFromSet(set, k);
        
        // 滑动窗口
        for (int i = k; i < nums.length; i++) {
            // 移除窗口外的元素
            set.remove(new int[]{nums[i - k], i - k});
            // 添加新元素
            set.add(new int[]{nums[i], i});
            // 计算当前中位数
            result[i - k + 1] = getMedianFromSet(set, k);
        }
        
        return result;
    }
    
    // 从TreeSet获取中位数
    private static double getMedianFromSet(TreeSet<int[]> set, int k) {
        int mid = k / 2;
        
        // 创建一个拷贝并找到中间的k个元素
        List<int[]> list = new ArrayList<>(set);
        Collections.sort(list, (a, b) -> Integer.compare(a[0], b[0]));
        
        if (k % 2 == 1) {
            return (double) list.get(mid)[0];
        } else {
            return ((double) list.get(mid - 1)[0] + list.get(mid)[0]) / 2.0;
        }
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 金融时间序列分析 - 计算滚动中位数
     * 
     * @param prices 每日股票价格
     * @param windowSize 分析窗口大小
     * @return 滚动中位数序列
     */
    public static double[] financialAnalysis(double[] prices, int windowSize) {
        int n = prices.length;
        int[] pricesInt = new int[n];
        for (int i = 0; i < n; i++) {
            pricesInt[i] = (int) (prices[i] * 100); // 转换为整数避免精度问题
        }
        double[] medians = medianSlidingWindow(pricesInt, windowSize);
        
        // 转换回double
        for (int i = 0; i < medians.length; i++) {
            medians[i] = medians[i] / 100.0;
        }
        return medians;
    }
    
    /**
     * 网络流量监控 - 检测流量异常
     * 
     * @param traffic 每分钟流量数据
     * @param windowSize 监控窗口大小
     * @return 异常标记序列（超过中位数2倍为异常）
     */
    public static boolean[] detectTrafficAnomalies(int[] traffic, int windowSize) {
        double[] medians = medianSlidingWindow(traffic, windowSize);
        boolean[] anomalies = new boolean[medians.length];
        
        for (int i = 0; i < medians.length; i++) {
            // 当前窗口实际数据点
            int current = traffic[i + windowSize - 1];
            // 标记超过中位数2倍的为异常
            anomalies[i] = current > 2 * medians[i];
        }
        
        return anomalies;
    }
    
    /**
     * 实时数据流处理器（模拟）
     * 
     * @param stream 数据流（模拟）
     * @param windowSize 窗口大小
     * @param sampleInterval 采样时间间隔
     */
    public static void realtimeStreamProcessing(int[] stream, int windowSize, int sampleInterval) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        Map<Integer, Integer> deferred = new HashMap<>();
        
        System.out.println("\n实时数据流处理开始...");
        System.out.println("时间\t当前值\t中位数\t异常状态");
        System.out.println("----\t------\t------\t--------");
        
        int time = 0;
        for (int i = 0; i < stream.length; i++) {
            time += sampleInterval;
            int value = stream[i];
            
            // 添加新值
            int balance = addNum(value, minHeap, maxHeap);
            
            // 当窗口填满后处理移除元素
            if (i >= windowSize) {
                balance += removeNum(stream[i - windowSize], minHeap, maxHeap, deferred);
            }
            
            // 窗口填满后开始输出
            if (i >= windowSize - 1) {
                rebalanceHeaps(minHeap, maxHeap, deferred);
                cleanHeaps(minHeap, maxHeap, deferred);
                
                double median = getMedian(minHeap, maxHeap, windowSize);
                boolean isAnomaly = value > 2 * median;
                
                System.out.printf("%4d\t%6d\t%6.2f\t%s%n", 
                                time, value, median, isAnomaly ? "ALERT" : "Normal");
            }
            
            // 模拟处理时延
            try {
                Thread.sleep(sampleInterval * 100L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化滑动窗口处理过程
     * 
     * @param nums 输入数组
     * @param k 窗口大小
     */
    public static void visualizeProcess(int[] nums, int k) {
        double[] medians = medianSlidingWindow(nums, k);
        
        System.out.println("\n滑动窗口处理可视化 (k=" + k + ")");
        System.out.println("索引 | 当前窗口值                 | 中位数 | 窗口状态");
        System.out.println("-----|--------------------------|--------|---------");
        
        for (int i = 0; i < medians.length; i++) {
            // 构建窗口值显示
            StringBuilder window = new StringBuilder();
            window.append("[");
            for (int j = i; j < i + k; j++) {
                if (j > i) window.append(",");
                window.append(nums[j]);
            }
            window.append("]");
            
            // 打印带颜色标记的中位数位置
            System.out.printf("%4d | %-24s | %6.2f | %s%n", 
                             i + k - 1, window.toString(), medians[i], 
                             getWindowVisualization(nums, i, k, medians[i]));
        }
    }
    
    // 获取窗口可视化状态
    private static String getWindowVisualization(int[] nums, int start, int k, double median) {
        String[] window = new String[k];
        int medianPos = -1;
        double minDiff = Double.MAX_VALUE;
        
        // 找到最接近中位数的位置
        for (int i = 0; i < k; i++) {
            int value = nums[start + i];
            window[i] = String.valueOf(value);
            
            double diff = Math.abs(value - median);
            if (diff < minDiff) {
                minDiff = diff;
                medianPos = i;
            }
        }
        
        // 标记中位数位置
        window[medianPos] = "\u001B[32m" + window[medianPos] + "\u001B[0m";
        return "[" + String.join(",", window) + "]";
    }
    
    // ========================= 性能测试与比较 =========================
    
    public static void comparePerformance() {
        int size = 100000;
        int k = 500;
        int[] largeData = new int[size];
        Random random = new Random();
        
        // 生成大型随机数组
        for (int i = 0; i < size; i++) {
            largeData[i] = random.nextInt(10000);
        }
        
        // 测试双堆法性能
        long start = System.currentTimeMillis();
        double[] result1 = medianSlidingWindow(largeData, k);
        long time1 = System.currentTimeMillis() - start;
        
        // 测试TreeSet法性能
        start = System.currentTimeMillis();
        double[] result2 = medianSlidingWindowTreeSet(largeData, k);
        long time2 = System.currentTimeMillis() - start;
        
        // 验证结果一致性
        boolean consistent = true;
        for (int i = 0; i < result1.length; i++) {
            if (Math.abs(result1[i] - result2[i]) > 0.001) {
                consistent = false;
                break;
            }
        }
        
        System.out.println("\n性能比较测试:");
        System.out.println("数据规模: " + size + "个元素, 窗口大小: " + k);
        System.out.println("----------------------------------");
        System.out.printf("双堆法耗时: %d ms\n", time1);
        System.out.printf("红黑树法耗时: %d ms\n", time2);
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
        System.out.println("----------------------------------");
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testEdgeCases();
        testApplicationScenarios();
        comparePerformance();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        
        double[] expected = {1.0, -1.0, -1.0, 3.0, 5.0, 6.0};
        double[] result = medianSlidingWindow(nums, k);
        
        System.out.println("输入: " + Arrays.toString(nums) + ", k = " + k);
        System.out.println("预期结果: " + Arrays.toString(expected));
        System.out.println("计算结果: " + Arrays.toString(result));
        System.out.println("测试结果: " + (Arrays.equals(result, expected) ? "通过" : "失败"));
        
        // 可视化处理过程
        visualizeProcess(nums, k);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        // 测试1: 窗口大小为1
        int[] nums1 = {5, 2, 8, 1, 9};
        int k1 = 1;
        double[] result1 = medianSlidingWindow(nums1, k1);
        System.out.println("窗口大小1: " + Arrays.toString(result1));
        
        // 测试2: 窗口大小等于数组长度
        int[] nums2 = {1, 2, 3, 4, 5};
        int k2 = 5;
        double[] result2 = medianSlidingWindow(nums2, k2);
        System.out.println("全窗口: " + Arrays.toString(result2));
        
        // 测试3: 含重复元素
        int[] nums3 = {1, 1, 1, 1, 2, 3, 2, 1};
        int k3 = 4;
        double[] result3 = medianSlidingWindow(nums3, k3);
        System.out.println("重复元素: " + Arrays.toString(result3));
        
        // 测试4: 负数与大数混合
        int[] nums4 = {-100, 100, 50, -50, 0};
        int k4 = 3;
        double[] result4 = medianSlidingWindow(nums4, k4);
        System.out.println("混合数: " + Arrays.toString(result4));
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 金融数据分析
        double[] prices = {100.5, 101.2, 100.8, 102.3, 103.5, 102.1, 100.2, 99.8};
        System.out.println("\n金融数据滚动中位数:");
        double[] priceMedians = financialAnalysis(prices, 3);
        System.out.println(Arrays.toString(priceMedians));
        
        // 场景2: 网络流量监控
        int[] traffic = {100, 120, 80, 200, 150, 90, 300, 110};
        System.out.println("\n网络流量异常检测:");
        boolean[] anomalies = detectTrafficAnomalies(traffic, 3);
        for (int i = 0; i < anomalies.length; i++) {
            System.out.printf("窗口%d: %s, 当前值=%d%n", i+1, anomalies[i] ? "异常" : "正常", traffic[i+2]);
        }
        
        // 场景3: 实时数据流处理
        System.out.println("\n实时数据流监控:");
        int[] stream = {10, 15, 20, 10, 5, 12, 18, 25, 30, 22, 18};
        realtimeStreamProcessing(stream, 3, 1);
    }
}