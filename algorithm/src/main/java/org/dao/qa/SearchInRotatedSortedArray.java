package main.java.org.dao.qa;

import java.util.*;

/**
 * 旋转排序数组中的搜索问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个升序排列的整数数组 nums，在某个点进行旋转，在这个旋转的有序数组中搜索目标值 target。
 * 如果存在返回其索引，否则返回 -1。要求时间复杂度为 O(log n)，不允许使用重复元素。
 * 
 * <p><b>示例</b>:
 * 输入: nums = [4,5,6,7,0,1,2], target = 0
 * 输出: 4
 * 解释: 原数组为 [0,1,2,4,5,6,7]，在索引3处旋转得到 [4,5,6,7,0,1,2]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第33题)
 * 
 * <p><b>解题思路</b>:
 * 1. 直接二分法: 判断有序部分，在有序部分中判断目标值位置，时间复杂度O(log n)
 * 2. 先找旋转点: 找到旋转点，然后在对应部分进行二分查找，时间复杂度O(log n)
 * 3. 分治递归法: 递归处理左右两部分，时间复杂度O(log n)
 * 4. 线性搜索法: 遍历整个数组，时间复杂度O(n)（不推荐）
 * 
 * <p><b>时间复杂度</b>:
 *  直接二分法: O(log n)
 *  先找旋转点: O(log n)
 *  分治递归法: O(log n)
 *  线性搜索法: O(n)
 * 
 * <p><b>空间复杂度</b>:
 *  直接二分法: O(1)
 *  先找旋转点: O(1)
 *  分治递归法: O(log n) 递归栈
 *  线性搜索法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 循环数组数据结构的搜索
 * 2. 数据库索引的旋转查找
 * 3. 分布式系统中的环形哈希查找
 * 4. 操作系统中的循环缓冲区
 * 5. 网络协议中的序列号搜索
 */

public class SearchInRotatedSortedArray {
    
    // 搜索结果类，包含详细信息
    public static class SearchResult {
        public int index;
        public boolean found;
        public int comparisons;
        public String searchPath;
        
        public SearchResult(int index, boolean found, int comparisons, String searchPath) {
            this.index = index;
            this.found = found;
            this.comparisons = comparisons;
            this.searchPath = searchPath;
        }
        
        @Override
        public String toString() {
            return String.format("Index: %d, Found: %s, Comparisons: %d, Path: %s", 
                               index, found, comparisons, searchPath);
        }
    }
    
    // ========================= 解法1: 直接二分法 (推荐) =========================
    
    /**
     * 直接二分查找法 - 最优解法
     * 核心思想：数组被分为两部分，其中一部分必定是有序的，
     * 先判断target是否在有序部分，决定搜索方向
     * 
     * @param nums 旋转排序数组
     * @param target 目标值
     * @return 目标值的索引，不存在返回-1
     */
    public static int searchDirectBinary(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        int left = 0, right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            }
            
            // 判断左半部分是否有序
            if (nums[left] <= nums[mid]) {
                // 左半部分有序，判断target是否在左半部分
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // 右半部分有序，判断target是否在右半部分
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * 直接二分查找法 - 带详细信息
     * 
     * @param nums 旋转排序数组
     * @param target 目标值
     * @return 包含详细搜索信息的结果
     */
    public static SearchResult searchDirectBinaryDetailed(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return new SearchResult(-1, false, 0, "数组为空");
        }
        
        int left = 0, right = nums.length - 1;
        int comparisons = 0;
        StringBuilder path = new StringBuilder();
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisons++;
            
            path.append(String.format("[%d,%d,mid=%d(%d)]", left, right, mid, nums[mid]));
            
            if (nums[mid] == target) {
                path.append(" -> 找到!");
                return new SearchResult(mid, true, comparisons, path.toString());
            }
            
            if (nums[left] <= nums[mid]) {
                // 左半部分有序
                if (nums[left] <= target && target < nums[mid]) {
                    path.append(" -> 左半有序,目标在左半");
                    right = mid - 1;
                } else {
                    path.append(" -> 左半有序,目标在右半");
                    left = mid + 1;
                }
            } else {
                // 右半部分有序
                if (nums[mid] < target && target <= nums[right]) {
                    path.append(" -> 右半有序,目标在右半");
                    left = mid + 1;
                } else {
                    path.append(" -> 右半有序,目标在左半");
                    right = mid - 1;
                }
            }
            
            if (left <= right) {
                path.append(" -> ");
            }
        }
        
        path.append(" -> 未找到");
        return new SearchResult(-1, false, comparisons, path.toString());
    }
    
    // ========================= 解法2: 先找旋转点再二分 =========================
    
    /**
     * 先找旋转点，再进行二分查找
     * 
     * @param nums 旋转排序数组
     * @param target 目标值
     * @return 目标值的索引，不存在返回-1
     */
    public static int searchFindPivotFirst(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        // 找到旋转点
        int pivot = findPivot(nums);
        
        // 判断在哪个部分搜索
        if (nums[pivot] <= target && target <= nums[nums.length - 1]) {
            // 在右半部分搜索
            return binarySearch(nums, pivot, nums.length - 1, target);
        } else {
            // 在左半部分搜索
            return binarySearch(nums, 0, pivot - 1, target);
        }
    }
    
    /**
     * 找到旋转点（最小元素的索引）
     * 
     * @param nums 旋转排序数组
     * @return 旋转点索引
     */
    private static int findPivot(int[] nums) {
        int left = 0, right = nums.length - 1;
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] > nums[right]) {
                // 旋转点在右半部分
                left = mid + 1;
            } else {
                // 旋转点在左半部分（包括mid）
                right = mid;
            }
        }
        
        return left;
    }
    
    /**
     * 标准二分查找
     * 
     * @param nums 数组
     * @param left 左边界
     * @param right 右边界
     * @param target 目标值
     * @return 目标值索引，不存在返回-1
     */
    private static int binarySearch(int[] nums, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
    
    // ========================= 解法3: 分治递归法 =========================
    
    /**
     * 分治递归法
     * 
     * @param nums 旋转排序数组
     * @param target 目标值
     * @return 目标值的索引，不存在返回-1
     */
    public static int searchDivideConquer(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        return searchDivideConquerHelper(nums, 0, nums.length - 1, target);
    }
    
    private static int searchDivideConquerHelper(int[] nums, int left, int right, int target) {
        if (left > right) {
            return -1;
        }
        
        int mid = left + (right - left) / 2;
        
        if (nums[mid] == target) {
            return mid;
        }
        
        // 左半部分有序
        if (nums[left] <= nums[mid]) {
            if (nums[left] <= target && target < nums[mid]) {
                return searchDivideConquerHelper(nums, left, mid - 1, target);
            } else {
                return searchDivideConquerHelper(nums, mid + 1, right, target);
            }
        } else {
            // 右半部分有序
            if (nums[mid] < target && target <= nums[right]) {
                return searchDivideConquerHelper(nums, mid + 1, right, target);
            } else {
                return searchDivideConquerHelper(nums, left, mid - 1, target);
            }
        }
    }
    
    // ========================= 解法4: 线性搜索法 =========================
    
    /**
     * 线性搜索法 - 最简单但效率最低
     * 
     * @param nums 旋转排序数组
     * @param target 目标值
     * @return 目标值的索引，不存在返回-1
     */
    public static int searchLinear(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                return i;
            }
        }
        
        return -1;
    }
    
    // ========================= 扩展问题：支持重复元素 =========================
    
    /**
     * 搜索旋转排序数组II - 支持重复元素
     * 当有重复元素时，最坏情况下退化为O(n)
     * 
     * @param nums 可能包含重复元素的旋转排序数组
     * @param target 目标值
     * @return 是否存在目标值
     */
    public static boolean searchWithDuplicates(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return false;
        }
        
        int left = 0, right = nums.length - 1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                return true;
            }
            
            // 处理重复元素：当nums[left] == nums[mid] == nums[right]时
            if (nums[left] == nums[mid] && nums[mid] == nums[right]) {
                left++;
                right--;
            } else if (nums[left] <= nums[mid]) {
                // 左半部分有序
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                // 右半部分有序
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return false;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 循环缓冲区搜索
     * 模拟操作系统中的循环缓冲区数据结构
     */
    public static class CircularBuffer<T> {
        private Object[] buffer;
        private int head;
        private int tail;
        private int size;
        private int capacity;
        
        public CircularBuffer(int capacity) {
            this.capacity = capacity;
            this.buffer = new Object[capacity];
            this.head = 0;
            this.tail = 0;
            this.size = 0;
        }
        
        public void add(T item) {
            buffer[tail] = item;
            tail = (tail + 1) % capacity;
            if (size < capacity) {
                size++;
            } else {
                head = (head + 1) % capacity;
            }
        }
        
        @SuppressWarnings("unchecked")
        public int search(T target) {
            if (size == 0) return -1;
            
            // 将循环缓冲区转换为线性数组进行搜索
            Integer[] linearArray = new Integer[size];
            for (int i = 0; i < size; i++) {
                linearArray[i] = (Integer) buffer[(head + i) % capacity];
            }
            
            // 这里假设缓冲区中的数据是有序的（可能因为旋转而不连续）
            for (int i = 0; i < linearArray.length; i++) {
                if (linearArray[i].equals(target)) {
                    return (head + i) % capacity; // 返回在原缓冲区中的实际位置
                }
            }
            
            return -1;
        }
        
        public int getSize() {
            return size;
        }
        
        @Override
        public String toString() {
            if (size == 0) return "[]";
            
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < size; i++) {
                sb.append(buffer[(head + i) % capacity]);
                if (i < size - 1) sb.append(", ");
            }
            sb.append("]");
            return sb.toString();
        }
    }
    
    /**
     * 分布式哈希环搜索
     * 模拟一致性哈希算法中的节点查找
     * 
     * @param hashRing 哈希环上的节点值（已排序但可能旋转）
     * @param targetHash 目标哈希值
     * @return 负责该哈希值的节点索引
     */
    public static int findNodeInHashRing(int[] hashRing, int targetHash) {
        if (hashRing == null || hashRing.length == 0) {
            return -1;
        }
        
        // 在哈希环中，寻找第一个大于等于目标哈希值的节点
        int left = 0, right = hashRing.length - 1;
        int result = 0; // 默认返回第一个节点
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (hashRing[mid] >= targetHash) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 时间序列数据搜索
     * 在旋转的时间序列数据中搜索特定时间点
     * 
     * @param timestamps 时间戳数组（可能因为数据轮转而旋转）
     * @param targetTime 目标时间戳
     * @return 目标时间戳的索引
     */
    public static int searchInTimeSeriesData(long[] timestamps, long targetTime) {
        if (timestamps == null || timestamps.length == 0) {
            return -1;
        }
        
        // 转换为int数组进行搜索（简化处理）
        int[] intTimestamps = new int[timestamps.length];
        for (int i = 0; i < timestamps.length; i++) {
            intTimestamps[i] = (int) (timestamps[i] % Integer.MAX_VALUE);
        }
        
        int intTarget = (int) (targetTime % Integer.MAX_VALUE);
        return searchDirectBinary(intTimestamps, intTarget);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化搜索过程
     * 
     * @param nums 旋转排序数组
     * @param target 目标值
     */
    public static void visualizeSearchProcess(int[] nums, int target) {
        System.out.println("\n旋转数组搜索过程可视化:");
        System.out.println("数组: " + Arrays.toString(nums));
        System.out.println("目标: " + target);
        
        if (nums == null || nums.length == 0) {
            System.out.println("数组为空，无法搜索");
            return;
        }
        
        int left = 0, right = nums.length - 1;
        int step = 1;
        
        System.out.println("\n步骤 | 左边界 | 右边界 | 中点 | 中点值 | 有序部分 | 目标范围 | 操作");
        System.out.println("-----|--------|--------|------|--------|----------|----------|----------");
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            String orderedPart;
            String targetRange;
            String operation;
            
            if (nums[mid] == target) {
                operation = "找到目标!";
                System.out.printf("%4d | %6d | %6d | %4d | %6d | %8s | %8s | %s\n",
                                 step, left, right, mid, nums[mid], "-", "-", operation);
                break;
            }
            
            if (nums[left] <= nums[mid]) {
                orderedPart = "左半部分";
                if (nums[left] <= target && target < nums[mid]) {
                    targetRange = "在左半";
                    operation = "搜索左半";
                    right = mid - 1;
                } else {
                    targetRange = "在右半";
                    operation = "搜索右半";
                    left = mid + 1;
                }
            } else {
                orderedPart = "右半部分";
                if (nums[mid] < target && target <= nums[right]) {
                    targetRange = "在右半";
                    operation = "搜索右半";
                    left = mid + 1;
                } else {
                    targetRange = "在左半";
                    operation = "搜索左半";
                    right = mid - 1;
                }
            }
            
            System.out.printf("%4d | %6d | %6d | %4d | %6d | %8s | %8s | %s\n",
                             step++, left, right, mid, nums[mid], orderedPart, targetRange, operation);
        }
        
        if (left > right) {
            System.out.println("搜索结束，未找到目标值");
        }
    }
    
    /**
     * 可视化旋转点查找过程
     * 
     * @param nums 旋转排序数组
     */
    public static void visualizeFindPivot(int[] nums) {
        System.out.println("\n查找旋转点过程可视化:");
        System.out.println("数组: " + Arrays.toString(nums));
        
        if (nums == null || nums.length == 0) {
            System.out.println("数组为空");
            return;
        }
        
        int left = 0, right = nums.length - 1;
        int step = 1;
        
        System.out.println("\n步骤 | 左边界 | 右边界 | 中点 | 中点值 | 右端值 | 比较结果 | 操作");
        System.out.println("-----|--------|--------|------|--------|--------|----------|----------");
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            String comparison;
            String operation;
            
            if (nums[mid] > nums[right]) {
                comparison = "mid > right";
                operation = "旋转点在右半";
                left = mid + 1;
            } else {
                comparison = "mid <= right";
                operation = "旋转点在左半";
                right = mid;
            }
            
            System.out.printf("%4d | %6d | %6d | %4d | %6d | %6d | %8s | %s\n",
                             step++, left, right, mid, nums[mid], nums[right], comparison, operation);
        }
        
        System.out.printf("\n旋转点索引: %d, 旋转点值: %d\n", left, nums[left]);
    }
    
    /**
     * 绘制数组旋转示意图
     * 
     * @param original 原始有序数组
     * @param rotated 旋转后的数组
     * @param pivot 旋转点
     */
    public static void drawRotationDiagram(int[] original, int[] rotated, int pivot) {
        System.out.println("\n数组旋转示意图:");
        System.out.println("原始数组: " + Arrays.toString(original));
        System.out.println("旋转数组: " + Arrays.toString(rotated));
        System.out.println("旋转点: " + pivot);
        
        System.out.println("\n旋转过程:");
        System.out.print("原始: ");
        for (int i = 0; i < original.length; i++) {
            System.out.printf("%3d", original[i]);
        }
        System.out.println();
        
        System.out.print("索引: ");
        for (int i = 0; i < original.length; i++) {
            System.out.printf("%3d", i);
        }
        System.out.println();
        
        System.out.print("旋转: ");
        for (int i = 0; i < rotated.length; i++) {
            if (i == pivot) {
                System.out.printf("%3s", "|" + rotated[i]);
            } else {
                System.out.printf("%3d", rotated[i]);
            }
        }
        System.out.println();
        // 修改这一行
        System.out.println("      " + repeatSpace(pivot * 3) + "^ 旋转点");
    }
    
    // 新增辅助方法用于生成重复空格
    private static String repeatSpace(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param size 数组大小
     * @param rotations 旋转次数
     */
    public static void comparePerformance(int size, int rotations) {
        // 生成测试数据
        int[] original = generateSortedArray(size);
        int[] rotated = rotateArray(original, rotations);
        int target = rotated[new Random().nextInt(size)]; // 随机选择一个存在的目标
        
        long start, end;
        
        // 测试直接二分法
        start = System.nanoTime();
        int result1 = searchDirectBinary(rotated, target);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试先找旋转点再二分
        start = System.nanoTime();
        int result2 = searchFindPivotFirst(rotated, target);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试分治递归法
        start = System.nanoTime();
        int result3 = searchDivideConquer(rotated, target);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试线性搜索法
        start = System.nanoTime();
        int result4 = searchLinear(rotated, target);
        end = System.nanoTime();
        long time4 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("数组大小: %d, 旋转次数: %d, 目标值: %d\n", size, rotations, target);
        System.out.println("===============================================");
        System.out.println("方法            | 时间(ms)   | 结果 | 相对速度");
        System.out.println("----------------|------------|------|----------");
        System.out.printf("直接二分法      | %.6f | %4d | 基准\n", time1 / 1_000_000.0, result1);
        System.out.printf("先找旋转点      | %.6f | %4d | %.2fx\n", time2 / 1_000_000.0, result2, (double)time2/time1);
        System.out.printf("分治递归法      | %.6f | %4d | %.2fx\n", time3 / 1_000_000.0, result3, (double)time3/time1);
        System.out.printf("线性搜索法      | %.6f | %4d | %.2fx\n", time4 / 1_000_000.0, result4, (double)time4/time1);
        System.out.println("===============================================");
        
        // 验证结果一致性
        boolean consistent = (result1 == result2) && (result2 == result3) && (result3 == result4);
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
    }
    
    // 生成有序数组
    private static int[] generateSortedArray(int size) {
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = i + 1;
        }
        return nums;
    }
    
    // 旋转数组
    private static int[] rotateArray(int[] nums, int k) {
        if (nums == null || nums.length <= 1) return nums;
        
        int n = nums.length;
        k = k % n; // 处理k大于数组长度的情况
        
        int[] rotated = new int[n];
        for (int i = 0; i < n; i++) {
            rotated[i] = nums[(i + n - k) % n];
        }
        
        return rotated;
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
        testCase("经典示例1", new int[]{4,5,6,7,0,1,2}, 0, 4);
        testCase("经典示例2", new int[]{4,5,6,7,0,1,2}, 3, -1);
        testCase("无旋转", new int[]{1,2,3,4,5}, 3, 2);
        testCase("完全旋转", new int[]{2,3,4,5,1}, 1, 4);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("单元素存在", new int[]{1}, 1, 0);
        testCase("单元素不存在", new int[]{1}, 0, -1);
        testCase("两元素", new int[]{3,1}, 1, 1);
        testCase("目标在边界", new int[]{4,5,6,7,0,1,2}, 4, 0);
        testCase("目标在边界", new int[]{4,5,6,7,0,1,2}, 2, 6);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 循环缓冲区
        System.out.println("\n循环缓冲区测试:");
        CircularBuffer<Integer> buffer = new CircularBuffer<Integer>(5);
        int[] testData = {10, 20, 30, 40, 50, 60, 70};
        
        for (int data : testData) {
            buffer.add(data);
            System.out.println("添加 " + data + " 后缓冲区状态: " + buffer.toString());
        }
        
        int searchTarget = 40;
        int position = buffer.search(searchTarget);
        System.out.println("搜索 " + searchTarget + " 的位置: " + position);
        
        // 场景2: 分布式哈希环
        System.out.println("\n分布式哈希环测试:");
        int[] hashRing = {100, 200, 300, 50}; // 旋转后的哈希环
        int targetHash = 150;
        int nodeIndex = findNodeInHashRing(hashRing, targetHash);
        System.out.println("哈希环: " + Arrays.toString(hashRing));
        System.out.println("目标哈希值 " + targetHash + " 分配到节点: " + nodeIndex);
        
        // 场景3: 时间序列数据
        System.out.println("\n时间序列数据测试:");
        long[] timestamps = {1000000003L, 1000000004L, 1000000005L, 1000000001L, 1000000002L};
        long targetTime = 1000000002L;
        int timeIndex = searchInTimeSeriesData(timestamps, targetTime);
        System.out.println("时间戳序列: " + Arrays.toString(timestamps));
        System.out.println("目标时间 " + targetTime + " 的索引: " + timeIndex);
        
        // 场景4: 重复元素搜索
        System.out.println("\n重复元素搜索测试:");
        int[] duplicates = {2,5,6,0,0,1,2};
        int dupTarget = 0;
        boolean found = searchWithDuplicates(duplicates, dupTarget);
        System.out.println("包含重复元素的数组: " + Arrays.toString(duplicates));
        System.out.println("搜索 " + dupTarget + " 结果: " + found);
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        int[] demoArray = {4,5,6,7,0,1,2};
        visualizeSearchProcess(demoArray, 0);
        visualizeFindPivot(demoArray);
        
        int[] original = {0,1,2,4,5,6,7};
        drawRotationDiagram(original, demoArray, 4);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100, 30);
        comparePerformance(1000, 300);
        comparePerformance(10000, 3000);
    }
    
    private static void testCase(String name, int[] nums, int target, int expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("数组: " + Arrays.toString(nums));
        System.out.println("目标: " + target);
        
        int result1 = searchDirectBinary(nums, target);
        SearchResult result2 = searchDirectBinaryDetailed(nums, target);
        int result3 = searchFindPivotFirst(nums, target);
        int result4 = searchDivideConquer(nums, target);
        int result5 = searchLinear(nums, target);
        
        System.out.println("直接二分法结果: " + result1);
        System.out.println("详细搜索结果: " + result2);
        System.out.println("先找旋转点结果: " + result3);
        System.out.println("分治递归法结果: " + result4);
        System.out.println("线性搜索法结果: " + result5);
        System.out.println("期望结果: " + expected);
        
        boolean isCorrect = (result1 == expected) && (result2.index == expected) && 
                           (result3 == expected) && (result4 == expected) && (result5 == expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小数组展示可视化
        if (nums.length <= 10) {
            visualizeSearchProcess(nums, target);
        }
    }
}