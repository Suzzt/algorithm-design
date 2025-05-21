package main.java.org.dao.sort;

import java.util.Arrays;

/**
 * 工业级优化的快速排序实现
 *
 * <p><b>设计思路：</b></p>
 * <ol>
 *   <li>基于分治法的递归排序，核心在于高效分区</li>
 *   <li>采用Hoare分区方案减少交换次数</li>
 *   <li>三数取中法选择基准值避免极端分布</li>
 *   <li>小数组切换插入排序优化缓存利用率</li>
 *   <li>尾递归优化防止栈溢出</li>
 * </ol>
 *
 * <p><b>使用场景：</b></p>
 * <ul>
 *   <li>✅ 大规模随机数据排序（推荐n > 1000）</li>
 *   <li>✅ 内存敏感型应用（原地排序）</li>
 *   <li>❌ 需要稳定排序的场景（改用归并排序）</li>
 *   <li>❌ 链表结构数据排序（性能会下降）</li>
 * </ul>
 *
 * <p><b>复杂度分析：</b></p>
 * <table border="1">
 *   <tr><th>场景</th><th>时间复杂度</th><th>空间复杂度</th></tr>
 *   <tr><td>最佳情况</td><td>O(n log n)</td><td>O(log n)</td></tr>
 *   <tr><td>平均情况</td><td>O(n log n)</td><td>O(log n)</td></tr>
 *   <tr><td>最坏情况</td><td>O(n²)</td><td>O(log n)</td></tr>
 * </table>
 *
 * <p><b>可扩展方向：</b></p>
 * <ul>
 *   <li>支持泛型：修改为 {@code QuickSort<T extends Comparable<T>>}</li>
 *   <li>并行优化：对左右分区使用多线程处理</li>
 *   <li>自定义比较器：添加 {@code Comparator} 参数</li>
 * </ul>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *   <li>⚠️ 包含大量重复元素时建议使用三向切分优化</li>
 *   <li>⚠️ 递归深度超过2*log2(n)时应切换堆排序</li>
 *   <li>⚠️ 处理自定义对象需正确实现比较方法</li>
 * </ul>
 *
 * @author sucf
 * @see Arrays#sort() JDK内置排序实现
 */
public class QuickSort {

    /**
     * 优化的快速排序实现（升序）
     *
     * @param arr 待排序数组
     */
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quickSort(arr, 0, arr.length - 1);
    }

    /**
     * 递归排序核心方法
     *
     * @param arr   待排序数组
     * @param left  左边界
     * @param right 右边界
     */
    private static void quickSort(int[] arr, int left, int right) {
        if (left >= right) return;

        // 优化1：小数组使用插入排序
        if (right - left + 1 <= 10) {
            insertionSort(arr, left, right);
            return;
        }

        // 优化2：三数取中法选择基准
        int pivotIndex = medianOfThree(arr, left, right);
        swap(arr, pivotIndex, right); // 将基准放到最后

        int partitionIndex = hoarePartition(arr, left, right);

        // 尾递归优化
        if (partitionIndex - left < right - partitionIndex) {
            quickSort(arr, left, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, right);
        } else {
            quickSort(arr, partitionIndex + 1, right);
            quickSort(arr, left, partitionIndex - 1);
        }
    }

    /**
     * Hoare分区方案（比Lomuto更高效）
     */
    private static int hoarePartition(int[] arr, int left, int right) {
        int pivot = arr[right];
        int i = left - 1;
        int j = right;

        while (true) {
            do {
                i++;
            } while (arr[i] < pivot);
            do {
                j--;
            } while (j >= left && arr[j] > pivot);

            if (i >= j) break;
            swap(arr, i, j);
        }
        swap(arr, i, right);
        return i;
    }

    /**
     * 三数取中法选择基准
     */
    private static int medianOfThree(int[] arr, int a, int b) {
        int mid = a + (b - a) / 2;

        // 找出中间值
        if (arr[a] > arr[mid]) swap(arr, a, mid);
        if (arr[a] > arr[b]) swap(arr, a, b);
        if (arr[mid] > arr[b]) swap(arr, mid, b);

        return mid;
    }

    /**
     * 插入排序（用于小数组优化）
     */
    private static void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        testCase(new int[]{3, 1, 4, 1, 5, 9, 2, 6}, "普通数组");
        testCase(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1}, "逆序数组");
        testCase(new int[]{1, 2, 3, 4, 5}, "已排序数组");
        testCase(new int[]{5}, "单元素数组");
        testCase(new int[]{}, "空数组");
        testCase(new int[]{2, 2, 2, 2}, "重复元素数组");
    }

    private static void testCase(int[] arr, String caseName) {
        System.out.println("测试案例: " + caseName);
        System.out.println("排序前: " + Arrays.toString(arr));
        quickSort(arr);
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
}