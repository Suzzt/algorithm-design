package main.java.org.dao.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 插入排序算法实现
 * 
 * <p><b>设计思路：</b></p>
 * <ol>
 *   <li>构建有序序列：将首元素视为已排序序列</li>
 *   <li>逐项插入：从未排序部分取出元素，在已排序序列中找到合适位置插入</li>
 *   <li>移位优化：用赋值代替交换操作减少内存访问次数</li>
 *   <li>稳定排序：保持相等元素的原始相对顺序</li>
 * </ol>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 小规模数据排序（n ≤ 100）</li>
 *   <li>✅ 基本有序数据（效率接近O(n)）</li>
 *   <li>✅ 需要稳定排序的场景</li>
 *   <li>❌ 完全逆序数据（退化为O(n²)）</li>
 *   <li>❌ 内存敏感型场景（需要O(1)额外空间）</li>
 * </ul>
 *
 * <p><b>复杂度分析：</b></p>
 * <table border="1">
 *   <tr><th>场景</th><th>时间复杂度</th><th>空间复杂度</th></tr>
 *   <tr><td>最佳情况</td><td>O(n)</td><td>O(1)</td></tr>
 *   <tr><td>平均情况</td><td>O(n²)</td><td>O(1)</td></tr>
 *   <tr><td>最坏情况</td><td>O(n²)</td><td>O(1)</td></tr>
 * </table>
 *
 * <p><b>优化方向：</b></p>
 * <ul>
 *   <li>二分插入排序：使用二分查找优化插入位置查找</li>
 *   <li>希尔排序：基于插入排序的分组优化</li>
 *   <li>链表优化：直接修改指针避免元素移动</li>
 * </ul>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *   <li>⚠️ 数据逆序时比较次数达到最大值n(n-1)/2</li>
 *   <li>⚠️ 处理对象数组时需正确实现比较逻辑</li>
 *   <li>⚠️ 与快速排序结合使用可获得最佳性能</li>
 * </ul>
 *
 * @author sucf
 */
public class InsertionSort {
    // 启用二分查找优化的阈值
    private static final int BINARY_THRESHOLD = 50;

    /**
     * 优化的插入排序实现（整型数组）
     * @param arr 待排序数组，自动处理null和空数组
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        // 根据数据规模选择优化策略
        if (arr.length > BINARY_THRESHOLD) {
            binaryInsertionSort(arr);
        } else {
            standardInsertionSort(arr);
        }
    }

    /**
     * 标准插入排序（带移位优化）
     */
    private static void standardInsertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            
            // 后移元素找插入位置
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * 二分插入排序优化
     */
    private static void binaryInsertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int pos = binarySearch(arr, 0, i - 1, key);
            
            // 批量移动元素
            System.arraycopy(arr, pos, arr, pos + 1, i - pos);
            arr[pos] = key;
        }
    }

    /**
     * 二分查找插入位置
     */
    private static int binarySearch(int[] arr, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid + 1; // 保证稳定性
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    /**
     * 泛型插入排序（支持自定义比较器）
     * @param arr        待排序数组
     * @param comparator 自定义比较器
     * @param <T>       泛型类型
     */
    public static <T> void sort(T[] arr, Comparator<? super T> comparator) {
        if (arr == null || comparator == null || arr.length <= 1) return;

        for (int i = 1; i < arr.length; i++) {
            T key = arr[i];
            int j = i - 1;
            
            while (j >= 0 && comparator.compare(arr[j], key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * 性能测试方法
     */
    public static void main(String[] args) {
        // 测试整型排序
        int[] testArr = {5, 2, 4, 6, 1, 3};
        System.out.println("排序前: " + Arrays.toString(testArr));
        sort(testArr);
        System.out.println("排序后: " + Arrays.toString(testArr));

        // 测试对象排序
        Student[] students = {
            new Student("Bob", 88),
            new Student("Alice", 95),
            new Student("Charlie", 88)
        };
        sort(students, Comparator.comparing(Student::getScore).thenComparing(Student::getName));
        System.out.println("\n学生排序:");
        Arrays.stream(students).forEach(System.out::println);
    }

    /** 测试用学生对象 */
    static class Student {
        private String name;
        private int score;

        public Student(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() { return name; }
        public int getScore() { return score; }

        @Override
        public String toString() {
            return name + "(" + score + ")";
        }
    }
}