package main.java.org.dao.qa;

import java.util.*;

/**
 * 旋转数组问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个数组，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。
 * 
 * <p><b>示例</b>:
 * 输入: nums = [1,2,3,4,5,6,7], k = 3
 * 输出: [5,6,7,1,2,3,4]
 * 解释: 向右轮转 3 步: [7,1,2,3,4,5,6] -> [6,7,1,2,3,4,5] -> [5,6,7,1,2,3,4]
 * 
 * 输入: nums = [-1,-100,3,99], k = 2
 * 输出: [3,99,-1,-100]
 * 解释: 向右轮转 2 步: [99,-1,-100,3] -> [3,99,-1,-100]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第189题)
 * 
 * <p><b>解题思路</b>:
 * 1. 额外数组法: 使用额外空间存储旋转后的结果
 * 2. 三次反转法: 通过三次数组反转实现旋转，空间复杂度O(1)
 * 3. 环形替换法: 按照环形结构逐个替换元素位置
 * 4. 分块旋转法: 将数组分为两部分，分别处理旋转
 * 
 * <p><b>时间复杂度</b>:
 *  额外数组法: O(n)
 *  三次反转法: O(n)
 *  环形替换法: O(n)
 *  分块旋转法: O(n)
 * 
 * <p><b>空间复杂度</b>:
 *  额外数组法: O(n)
 *  三次反转法: O(1)
 *  环形替换法: O(1)
 *  分块旋转法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 数据缓冲区管理
 * 2. 图像处理中的像素旋转
 * 3. 时间序列数据的周期性分析
 * 4. 游戏开发中的循环队列
 * 5. 密码学中的位移加密
 */

public class RotateArray {
    
    // ========================= 解法1: 额外数组法 =========================
    
    /**
     * 使用额外数组实现旋转
     * 
     * @param nums 输入数组
     * @param k 旋转步数
     */
    public static void rotateWithExtraArray(int[] nums, int k) {
        if (nums == null || nums.length <= 1 || k == 0) {
            return;
        }
        
        int n = nums.length;
        k = k % n; // 处理k大于数组长度的情况
        
        if (k == 0) {
            return;
        }
        
        int[] temp = new int[n];
        
        // 将旋转后的元素放到正确位置
        for (int i = 0; i < n; i++) {
            temp[(i + k) % n] = nums[i];
        }
        
        // 将结果复制回原数组
        System.arraycopy(temp, 0, nums, 0, n);
    }
    
    // ========================= 解法2: 三次反转法 (推荐) =========================
    
    /**
     * 三次反转实现旋转 - 空间复杂度O(1)
     * 思路：将数组分为两部分，先整体反转，再分别反转两部分
     * 
     * @param nums 输入数组
     * @param k 旋转步数
     */
    public static void rotateWithReverse(int[] nums, int k) {
        if (nums == null || nums.length <= 1 || k == 0) {
            return;
        }
        
        int n = nums.length;
        k = k % n;
        
        if (k == 0) {
            return;
        }
        
        // 第一步：反转整个数组
        reverse(nums, 0, n - 1);
        
        // 第二步：反转前k个元素
        reverse(nums, 0, k - 1);
        
        // 第三步：反转后n-k个元素
        reverse(nums, k, n - 1);
    }
    
    private static void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
            start++;
            end--;
        }
    }
    
    // ========================= 解法3: 环形替换法 =========================
    
    /**
     * 环形替换法 - 按照环形结构逐个替换
     * 
     * @param nums 输入数组
     * @param k 旋转步数
     */
    public static void rotateWithCyclicReplace(int[] nums, int k) {
        if (nums == null || nums.length <= 1 || k == 0) {
            return;
        }
        
        int n = nums.length;
        k = k % n;
        
        if (k == 0) {
            return;
        }
        
        int count = 0; // 已处理的元素个数
        
        for (int start = 0; count < n; start++) {
            int current = start;
            int prev = nums[start];
            
            do {
                int next = (current + k) % n;
                int temp = nums[next];
                nums[next] = prev;
                prev = temp;
                current = next;
                count++;
            } while (start != current);
        }
    }
    
    // ========================= 解法4: 分块旋转法 =========================
    
    /**
     * 分块旋转法 - 基于最大公约数的优化算法
     * 
     * @param nums 输入数组
     * @param k 旋转步数
     */
    public static void rotateWithBlockRotation(int[] nums, int k) {
        if (nums == null || nums.length <= 1 || k == 0) {
            return;
        }
        
        int n = nums.length;
        k = k % n;
        
        if (k == 0) {
            return;
        }
        
        int gcd = gcd(n, k);
        
        for (int i = 0; i < gcd; i++) {
            int temp = nums[i];
            int j = i;
            
            while (true) {
                int next = (j + k) % n;
                if (next == i) {
                    break;
                }
                nums[j] = nums[next];
                j = next;
            }
            nums[j] = temp;
        }
    }
    
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 循环缓冲区实现
     */
    public static class CircularBuffer<T> {
        private Object[] buffer;
        private int head;
        private int tail;
        private int size;
        private final int capacity;
        
        public CircularBuffer(int capacity) {
            this.capacity = capacity;
            this.buffer = new Object[capacity];
            this.head = 0;
            this.tail = 0;
            this.size = 0;
        }
        
        public void put(T item) {
            buffer[tail] = item;
            tail = (tail + 1) % capacity;
            
            if (size < capacity) {
                size++;
            } else {
                head = (head + 1) % capacity;
            }
        }
        
        @SuppressWarnings("unchecked")
        public T get() {
            if (size == 0) {
                return null;
            }
            
            T item = (T) buffer[head];
            buffer[head] = null;
            head = (head + 1) % capacity;
            size--;
            
            return item;
        }
        
        public void rotateRight(int k) {
            if (size == 0 || k == 0) {
                return;
            }
            
            k = k % size;
            if (k == 0) {
                return;
            }
            
            // 提取有效数据
            Object[] temp = new Object[size];
            for (int i = 0; i < size; i++) {
                temp[i] = buffer[(head + i) % capacity];
            }
            
            // 旋转数据
            Object[] rotated = new Object[size];
            for (int i = 0; i < size; i++) {
                rotated[(i + k) % size] = temp[i];
            }
            
            // 重新放入缓冲区
            head = 0;
            tail = size % capacity;
            for (int i = 0; i < size; i++) {
                buffer[i] = rotated[i];
            }
        }
        
        public List<T> toList() {
            List<T> result = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                @SuppressWarnings("unchecked")
                T item = (T) buffer[(head + i) % capacity];
                result.add(item);
            }
            return result;
        }
        
        public int size() {
            return size;
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        public boolean isFull() {
            return size == capacity;
        }
    }
    
    /**
     * 时间序列数据旋转分析器
     */
    public static class TimeSeriesRotator {
        
        /**
         * 时间序列向前预测（通过旋转模拟）
         * 
         * @param timeSeries 时间序列数据
         * @param steps 预测步数
         * @return 预测后的时间序列
         */
        public static double[] predictByRotation(double[] timeSeries, int steps) {
            if (timeSeries == null || timeSeries.length == 0) {
                return new double[0];
            }
            
            double[] result = timeSeries.clone();
            
            // 通过循环旋转模拟周期性预测
            for (int i = 0; i < steps; i++) {
                rotateDoubleArray(result, 1);
                
                // 添加一些随机波动（模拟真实预测的不确定性）
                for (int j = 0; j < result.length; j++) {
                    result[j] *= (0.95 + Math.random() * 0.1); // ±5%的波动
                }
            }
            
            return result;
        }
        
        /**
         * 找到时间序列的最佳旋转对齐
         * 
         * @param series1 时间序列1
         * @param series2 时间序列2
         * @return 最佳旋转步数
         */
        public static int findBestRotationAlignment(double[] series1, double[] series2) {
            if (series1 == null || series2 == null || 
                series1.length != series2.length || series1.length == 0) {
                return 0;
            }
            
            int n = series1.length;
            int bestRotation = 0;
            double bestCorrelation = Double.NEGATIVE_INFINITY;
            
            for (int k = 0; k < n; k++) {
                double correlation = calculateCorrelation(series1, series2, k);
                if (correlation > bestCorrelation) {
                    bestCorrelation = correlation;
                    bestRotation = k;
                }
            }
            
            return bestRotation;
        }
        
        private static void rotateDoubleArray(double[] arr, int k) {
            if (arr == null || arr.length <= 1 || k == 0) {
                return;
            }
            
            int n = arr.length;
            k = k % n;
            
            if (k == 0) {
                return;
            }
            
            // 使用三次反转法
            reverseDouble(arr, 0, n - 1);
            reverseDouble(arr, 0, k - 1);
            reverseDouble(arr, k, n - 1);
        }
        
        private static void reverseDouble(double[] arr, int start, int end) {
            while (start < end) {
                double temp = arr[start];
                arr[start] = arr[end];
                arr[end] = temp;
                start++;
                end--;
            }
        }
        
        private static double calculateCorrelation(double[] series1, double[] series2, int rotation) {
            int n = series1.length;
            double sum = 0.0;
            
            for (int i = 0; i < n; i++) {
                int rotatedIndex = (i + rotation) % n;
                sum += series1[i] * series2[rotatedIndex];
            }
            
            return sum / n;
        }
    }
    
    /**
     * 图像旋转处理器（简化版）
     */
    public static class ImageRotator {
        
        /**
         * 一维数组表示的图像进行旋转
         * 
         * @param image 图像数据（一维数组）
         * @param width 图像宽度
         * @param height 图像高度
         * @param clockwise 是否顺时针旋转90度
         * @return 旋转后的图像数据和新的宽高
         */
        public static RotationResult rotateImage90(int[] image, int width, int height, boolean clockwise) {
            if (image == null || image.length != width * height) {
                throw new IllegalArgumentException("图像数据不匹配宽高设置");
            }
            
            int[] rotated = new int[width * height];
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int oldIndex = i * width + j;
                    int newI, newJ;
                    
                    if (clockwise) {
                        newI = j;
                        newJ = height - 1 - i;
                    } else {
                        newI = width - 1 - j;
                        newJ = i;
                    }
                    
                    int newIndex = newI * height + newJ;
                    rotated[newIndex] = image[oldIndex];
                }
            }
            
            return new RotationResult(rotated, height, width); // 注意宽高交换
        }
        
        public static class RotationResult {
            public final int[] data;
            public final int width;
            public final int height;
            
            public RotationResult(int[] data, int width, int height) {
                this.data = data;
                this.width = width;
                this.height = height;
            }
            
            @Override
            public String toString() {
                return String.format("RotationResult{width=%d, height=%d, dataLength=%d}", 
                                   width, height, data.length);
            }
        }
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化三次反转的过程
     * 
     * @param nums 输入数组
     * @param k 旋转步数
     */
    public static void visualizeReverseProcess(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            System.out.println("输入数组为空");
            return;
        }
        
        int n = nums.length;
        k = k % n;
        
        System.out.println("\n三次反转过程可视化:");
        System.out.println("原数组: " + Arrays.toString(nums));
        System.out.println("旋转步数: " + k);
        
        int[] working = nums.clone();
        
        // 第一步：反转整个数组
        System.out.println("\n第一步：反转整个数组");
        System.out.println("反转前: " + Arrays.toString(working));
        reverse(working, 0, n - 1);
        System.out.println("反转后: " + Arrays.toString(working));
        
        // 第二步：反转前k个元素
        System.out.println("\n第二步：反转前" + k + "个元素");
        System.out.println("反转前: " + Arrays.toString(working));
        reverse(working, 0, k - 1);
        System.out.println("反转后: " + Arrays.toString(working));
        
        // 第三步：反转后n-k个元素
        System.out.println("\n第三步：反转后" + (n - k) + "个元素");
        System.out.println("反转前: " + Arrays.toString(working));
        reverse(working, k, n - 1);
        System.out.println("反转后: " + Arrays.toString(working));
        
        System.out.println("\n最终结果: " + Arrays.toString(working));
    }
    
    /**
     * 可视化环形替换的过程
     * 
     * @param nums 输入数组
     * @param k 旋转步数
     */
    public static void visualizeCyclicReplaceProcess(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            System.out.println("输入数组为空");
            return;
        }
        
        int n = nums.length;
        k = k % n;
        
        System.out.println("\n环形替换过程可视化:");
        System.out.println("原数组: " + Arrays.toString(nums));
        System.out.println("旋转步数: " + k);
        
        int[] working = nums.clone();
        int count = 0;
        
        System.out.println("\n替换过程:");
        System.out.println("步骤 | 起始位置 | 当前位置 | 目标位置 | 当前值 | 数组状态");
        System.out.println("-----|----------|----------|----------|--------|------------------");
        
        int step = 1;
        for (int start = 0; count < n; start++) {
            int current = start;
            int prev = working[start];
            
            do {
                int next = (current + k) % n;
                int temp = working[next];
                working[next] = prev;
                
                System.out.printf("%4d | %8d | %8d | %8d | %6d | %s\n", 
                                 step++, start, current, next, prev, Arrays.toString(working));
                
                prev = temp;
                current = next;
                count++;
            } while (start != current);
            
            if (count < n) {
                System.out.println("     | -------- 开始新的环 --------");
            }
        }
        
        System.out.println("\n最终结果: " + Arrays.toString(working));
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param size 数组大小
     * @param k 旋转步数
     */
    public static void comparePerformance(int size, int k) {
        // 生成测试数据
        int[] nums = generateTestArray(size);
        
        long start, end;
        
        // 测试额外数组法
        int[] nums1 = nums.clone();
        start = System.nanoTime();
        rotateWithExtraArray(nums1, k);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试三次反转法
        int[] nums2 = nums.clone();
        start = System.nanoTime();
        rotateWithReverse(nums2, k);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试环形替换法
        int[] nums3 = nums.clone();
        start = System.nanoTime();
        rotateWithCyclicReplace(nums3, k);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试分块旋转法
        int[] nums4 = nums.clone();
        start = System.nanoTime();
        rotateWithBlockRotation(nums4, k);
        end = System.nanoTime();
        long time4 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("数组大小: %d, 旋转步数: %d\n", size, k);
        System.out.println("===============================================");
        System.out.println("方法            | 时间(ms)   | 相对速度");
        System.out.println("----------------|------------|----------");
        System.out.printf("额外数组法      | %.6f | 基准\n", time1 / 1_000_000.0);
        System.out.printf("三次反转法      | %.6f | %.2fx\n", time2 / 1_000_000.0, (double)time2/time1);
        System.out.printf("环形替换法      | %.6f | %.2fx\n", time3 / 1_000_000.0, (double)time3/time1);
        System.out.printf("分块旋转法      | %.6f | %.2fx\n", time4 / 1_000_000.0, (double)time4/time1);
        System.out.println("===============================================");
        
        // 验证结果一致性
        boolean consistent = Arrays.equals(nums1, nums2) && Arrays.equals(nums2, nums3) && Arrays.equals(nums3, nums4);
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
    }
    
    // 生成测试数组
    private static int[] generateTestArray(int size) {
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = i + 1;
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
        testCase("示例测试1", new int[]{1,2,3,4,5,6,7}, 3, new int[]{5,6,7,1,2,3,4});
        testCase("示例测试2", new int[]{-1,-100,3,99}, 2, new int[]{3,99,-1,-100});
        testCase("单元素", new int[]{1}, 1, new int[]{1});
        testCase("两元素", new int[]{1,2}, 1, new int[]{2,1});
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("k为0", new int[]{1,2,3,4}, 0, new int[]{1,2,3,4});
        testCase("k等于数组长度", new int[]{1,2,3,4}, 4, new int[]{1,2,3,4});
        testCase("k大于数组长度", new int[]{1,2,3}, 5, new int[]{3,1,2});
        testCase("空数组", new int[]{}, 1, new int[]{});
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 循环缓冲区
        System.out.println("\n循环缓冲区测试:");
        CircularBuffer<Integer> buffer = new CircularBuffer<>(5);
        
        // 添加数据
        for (int i = 1; i <= 7; i++) {
            buffer.put(i);
            System.out.println("添加 " + i + " 后，缓冲区: " + buffer.toList());
        }
        
        // 旋转缓冲区
        buffer.rotateRight(2);
        System.out.println("向右旋转2位后: " + buffer.toList());
        
        // 场景2: 时间序列分析
        System.out.println("\n时间序列分析:");
        double[] timeSeries = {1.0, 1.5, 2.0, 1.8, 1.2, 0.8, 1.3, 1.7};
        System.out.println("原始时间序列: " + Arrays.toString(timeSeries));
        
        double[] predicted = TimeSeriesRotator.predictByRotation(timeSeries, 2);
        System.out.println("预测序列: " + Arrays.toString(predicted));
        
        // 序列对齐
        double[] series2 = {1.8, 1.2, 0.8, 1.3, 1.7, 1.0, 1.5, 2.0};
        int bestRotation = TimeSeriesRotator.findBestRotationAlignment(timeSeries, series2);
        System.out.println("最佳对齐旋转步数: " + bestRotation);
        
        // 场景3: 图像旋转
        System.out.println("\n图像旋转测试:");
        int[] image = {1, 2, 3, 4, 5, 6}; // 2x3的图像
        int width = 3, height = 2;
        
        System.out.println("原始图像 (" + width + "x" + height + "): " + Arrays.toString(image));
        
        ImageRotator.RotationResult result = ImageRotator.rotateImage90(image, width, height, true);
        System.out.println("顺时针旋转90度后 (" + result.width + "x" + result.height + "): " + 
                          Arrays.toString(result.data));
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        int[] demoArray = {1, 2, 3, 4, 5};
        visualizeReverseProcess(demoArray, 2);
        visualizeCyclicReplaceProcess(demoArray.clone(), 2);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        int[] sizes = {100, 1000, 10000};
        int[] rotations = {10, 100, 500};
        
        for (int size : sizes) {
            for (int k : rotations) {
                if (k < size) {
                    comparePerformance(size, k);
                }
            }
        }
    }
    
    private static void testCase(String name, int[] nums, int k, int[] expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("输入: " + Arrays.toString(nums) + ", k = " + k);
        
        int[] nums1 = nums.clone();
        int[] nums2 = nums.clone();
        int[] nums3 = nums.clone();
        int[] nums4 = nums.clone();
        
        if (nums1.length > 0) {
            rotateWithExtraArray(nums1, k);
            rotateWithReverse(nums2, k);
            rotateWithCyclicReplace(nums3, k);
            rotateWithBlockRotation(nums4, k);
        }
        
        System.out.println("额外数组法结果: " + Arrays.toString(nums1));
        System.out.println("三次反转法结果: " + Arrays.toString(nums2));
        System.out.println("环形替换法结果: " + Arrays.toString(nums3));
        System.out.println("分块旋转法结果: " + Arrays.toString(nums4));
        System.out.println("期望结果: " + Arrays.toString(expected));
        
        boolean isCorrect = Arrays.equals(nums1, expected) && Arrays.equals(nums2, expected) && 
                           Arrays.equals(nums3, expected) && Arrays.equals(nums4, expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小规模数组展示可视化
        if (nums.length <= 10 && nums.length > 0 && k > 0) {
            visualizeReverseProcess(nums.clone(), k);
        }
    }
}