package main.java.org.dao.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 选择排序算法实现
 *
 * <p><b>设计思路：</b></p>
 * <ol>
 *   <li>分治思想：将数组分为已排序和未排序两部分</li>
 *   <li>贪心策略：每次选择未排序序列中的最小元素</li>
 *   <li>原地排序：通过元素交换避免额外空间消耗</li>
 *   <li>非稳定排序：交换操作可能破坏相等元素的原始顺序</li>
 * </ol>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 教学演示基础排序原理</li>
 *   <li>✅ 小规模数据排序（n ≤ 1000）</li>
 *   <li>✅ 内存敏感型设备开发</li>
 *   <li>❌ 需要稳定排序的场景</li>
 *   <li>❌ 数据量超过1万的排序任务</li>
 * </ul>
 *
 * <p><b>复杂度分析：</b></p>
 * <table border="1">
 *   <tr><th>场景</th><th>时间复杂度</th><th>空间复杂度</th></tr>
 *   <tr><td>最佳情况</td><td>O(n²)</td><td>O(1)</td></tr>
 *   <tr><td>平均情况</td><td>O(n²)</td><td>O(1)</td></tr>
 *   <tr><td>最坏情况</td><td>O(n²)</td><td>O(1)</td></tr>
 * </table>
 *
 * <p><b>优化方向：</b></p>
 * <ul>
 *   <li>双向选择排序（同时找最大最小值）</li>
 *   <li>链表结构优化（减少交换次数）</li>
 *   <li>并行化改造（分块查找极值）</li>
 * </ul>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *   <li>⚠️ 10万数据规模时性能比快速排序慢100倍以上</li>
 *   <li>⚠️ 对已排序数组仍需进行n(n-1)/2次比较</li>
 *   <li>⚠️ 处理对象数组时需自定义Comparator</li>
 * </ul>
 *
 * @author sucf
 */
public class SelectionSort {

    // 启用优化的数据量阈值
    private static final int OPTIMIZATION_THRESHOLD = 1000;

    /**
     * 选择排序核心方法（整型数组）
     * @param arr 待排序数组，自动处理null和空数组
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        int n = arr.length;
        // 根据数据规模选择优化策略
        boolean useOptimized = n > OPTIMIZATION_THRESHOLD;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            // 查找未排序部分的最小值索引
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // 避免不必要的交换
            if (minIndex != i) {
                swap(arr, i, minIndex);
            }

            // 大规模数据时添加进度提示
            if (useOptimized && (i % 100 == 0)) {
                System.out.printf("处理进度: %.1f%%%n", (i * 100.0 / n));
            }
        }
    }

    /**
     * 泛型选择排序（支持自定义比较器）
     * @param arr        待排序数组
     * @param comparator 自定义比较器
     * @param <T>       泛型类型
     */
    public static <T> void sort(T[] arr, Comparator<? super T> comparator) {
        if (arr == null || comparator == null || arr.length <= 1) return;

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(arr[j], arr[minIndex]) < 0) {
                    minIndex = j;
                }
            }

            if (minIndex != i) {
                swap(arr, i, minIndex);
            }
        }
    }

    // 私有方法：交换数组元素
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
        // 测试整型排序
        int[] testArr = {64, 25, 12, 22, 11};
        System.out.println("排序前: " + Arrays.toString(testArr));
        sort(testArr);
        System.out.println("排序后: " + Arrays.toString(testArr));

        // 测试对象排序
        Student[] students = {
                new Student("Alice", 88),
                new Student("Bob", 95),
                new Student("Charlie", 78)
        };
        sort(students, Comparator.comparingInt(s -> s.score));
        System.out.println("\n学生成绩排序:");
        Arrays.stream(students).forEach(System.out::println);
    }

    /** 测试用学生对象 */
    static class Student {
        String name;
        int score;

        public Student(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return name + ": " + score;
        }
    }
}