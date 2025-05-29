package main.java.org.dao.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 堆排序算法实现
 * 
 * <p><b>设计思路：</b></p>
 * <ol>
 *   <li>基于二叉堆数据结构：利用完全二叉树特性实现高效排序</li>
 *   <li>原地排序：通过元素交换避免额外空间消耗</li>
 *   <li>非稳定排序：交换操作可能破坏相等元素的原始顺序</li>
 *   <li>分治策略：构建堆 + 排序两阶段处理</li>
 * </ol>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 需要原地排序的大规模数据（内存敏感）</li>
 *   <li>✅ 实时系统（保证最坏情况O(n log n)）</li>
 *   <li>✅ 需要同时获取前K个最大值的场景</li>
 *   <li>❌ 需要稳定排序的场景</li>
 *   <li>❌ 数据量极小（n≤10）时效率不如插入排序</li>
 * </ul>
 *
 * <p><b>复杂度分析：</b></p>
 * <table border="1">
 *   <tr><th>场景</th><th>时间复杂度</th><th>空间复杂度</th></tr>
 *   <tr><td>最佳情况</td><td>O(n log n)</td><td>O(1)</td></tr>
 *   <tr><td>平均情况</td><td>O(n log n)</td><td>O(1)</td></tr>
 *   <tr><td>最坏情况</td><td>O(n log n)</td><td>O(1)</td></tr>
 * </table>
 *
 * <p><b>优化策略：</b></p>
 * <ul>
 *   <li>非递归堆调整：避免递归深度过大</li>
 *   <li>构建堆优化：从最后一个非叶子节点开始下沉</li>
 *   <li>小数组优化：当剩余元素较少时切换插入排序</li>
 * </ul>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *   <li>⚠️ 堆排序在最坏情况下性能优于快速排序</li>
 *   <li>⚠️ 处理大数组时CPU缓存命中率较低</li>
 *   <li>⚠️ JDK内置排序在递归深度过大时会切换到堆排序</li>
 * </ul>
 *
 * @author sucf
 */
public class HeapSort {
    // 切换插入排序的阈值
    private static final int INSERTION_THRESHOLD = 32;

    /**
     * 堆排序入口
     * @param arr 待排序数组（自动处理null）
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        int n = arr.length;

        // 1. 构建最大堆：从最后一个非叶子节点开始调整
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // 2. 逐个提取堆顶元素（最大值）并调整堆
        for (int i = n - 1; i > 0; i--) {
            // 将堆顶元素（最大值）与当前末尾元素交换
            swap(arr, 0, i);
            
            // 小数组优化：当剩余元素较少时使用插入排序
            if (i < INSERTION_THRESHOLD) {
                insertionSort(arr, i);
            } else {
                // 调整剩余元素使其满足最大堆性质
                heapify(arr, i, 0);
            }
        }
    }

    /**
     * 堆调整（非递归实现）
     * @param arr 堆数组
     * @param n   堆的大小
     * @param i   当前需要调整的节点索引
     */
    private static void heapify(int[] arr, int n, int i) {
        int current = i;
        while (current < n) {
            int largest = current; // 初始化最大值为当前节点
            int left = 2 * current + 1;
            int right = 2 * current + 2;

            // 比较左子节点
            if (left < n && arr[left] > arr[largest]) {
                largest = left;
            }
            // 比较右子节点
            if (right < n && arr[right] > arr[largest]) {
                largest = right;
            }

            // 如果最大值不是当前节点，则交换并继续调整
            if (largest != current) {
                swap(arr, current, largest);
                current = largest; // 继续调整子树
            } else {
                break; // 当前节点已满足堆性质，退出循环
            }
        }
    }

    /**
     * 插入排序（用于小数组优化）
     * @param arr 待排序数组
     * @param end 排序结束位置（不包含）
     */
    private static void insertionSort(int[] arr, int end) {
        for (int i = 1; i < end; i++) {
            int key = arr[i];
            int j = i - 1;
            
            // 移动比key大的元素
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * 交换数组元素
     */
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 泛型堆排序（支持自定义比较器）
     * @param arr        待排序数组
     * @param comparator 自定义比较器
     * @param <T>       泛型类型
     */
    public static <T> void sort(T[] arr, Comparator<? super T> comparator) {
        if (arr == null || comparator == null || arr.length <= 1) return;
        int n = arr.length;

        // 构建堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, comparator);
        }

        // 提取元素
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            heapify(arr, i, 0, comparator);
        }
    }

    private static <T> void heapify(T[] arr, int n, int i, Comparator<? super T> comparator) {
        int current = i;
        while (current < n) {
            int largest = current;
            int left = 2 * current + 1;
            int right = 2 * current + 2;

            if (left < n && comparator.compare(arr[left], arr[largest]) > 0) {
                largest = left;
            }
            if (right < n && comparator.compare(arr[right], arr[largest]) > 0) {
                largest = right;
            }

            if (largest != current) {
                swap(arr, current, largest);
                current = largest;
            } else {
                break;
            }
        }
    }

    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 性能测试方法
     */
    public static void main(String[] args) {
        testCase(new int[]{12, 11, 13, 5, 6, 7}, "普通数组");
        testCase(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1}, "逆序数组");
        testCase(new int[]{3, 1, 4, 1, 5, 9, 2, 6}, "重复元素");
        
        // 百万级数据测试
        int[] millionTest = new java.util.Random().ints(1_000_000).toArray();
        long start = System.currentTimeMillis();
        sort(millionTest);
        System.out.printf("百万数据排序耗时: %dms%n", System.currentTimeMillis() - start);
        
        // 对象排序测试
        Student[] students = {
            new Student("Alice", 88),
            new Student("Bob", 95),
            new Student("Charlie", 78)
        };
        sort(students, Comparator.comparingInt(Student::getScore));
        System.out.println("\n学生成绩排序:");
        Arrays.stream(students).forEach(System.out::println);
    }

    private static void testCase(int[] arr, String caseName) {
        System.out.println("测试案例: " + caseName);
        System.out.println("排序前: " + Arrays.toString(arr));
        sort(arr);
        System.out.println("排序后: " + Arrays.toString(arr));
        System.out.println("验证" + (isSorted(arr) ? "成功" : "失败"));
        System.out.println("----------------------");
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) return false;
        }
        return true;
    }

    /** 测试用学生对象 */
    static class Student {
        String name;
        int score;

        public Student(String name, int score) {
            this.name = name;
            this.score = score;
        }
        
        public int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return name + ": " + score;
        }
    }
}