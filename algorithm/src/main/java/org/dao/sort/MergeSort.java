package main.java.org.dao.sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 归并排序算法实现
 * 
 * <p><b>设计思路：</b></p>
 * <ol>
 *   <li>分治策略：将数组递归拆分为最小单元</li>
 *   <li>有序合并：将两个有序数组合并为更大有序数组</li>
 *   <li>稳定排序：保持相等元素的原始相对顺序</li>
 *   <li>外排序：适合处理磁盘/网络等外部存储数据</li>
 * </ol>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 大规模数据排序（内存不足时使用外排序）</li>
 *   <li>✅ 需要稳定排序的场景</li>
 *   <li>✅ 链表结构排序（天然适合归并策略）</li>
 *   <li>❌ 内存敏感型场景（需要O(n)额外空间）</li>
 *   <li>❌ 实时性要求高的场景</li>
 * </ul>
 *
 * <p><b>复杂度分析：</b></p>
 * <table border="1">
 *   <tr><th>场景</th><th>时间复杂度</th><th>空间复杂度</th></tr>
 *   <tr><td>最佳情况</td><td>O(n log n)</td><td>O(n)</td></tr>
 *   <tr><td>平均情况</td><td>O(n log n)</td><td>O(n)</td></tr>
 *   <tr><td>最坏情况</td><td>O(n log n)</td><td>O(n)</td></tr>
 * </table>
 *
 * <p><b>优化方向：</b></p>
 * <ul>
 *   <li>TimSort优化：JDK内置的混合排序算法</li>
 *   <li>原地归并：减少内存占用（复杂度上升）</li>
 *   <li>多路归并：提升外排序性能</li>
 * </ul>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *   <li>⚠️ 递归深度过大会导致栈溢出（建议设置阈值）</li>
 *   <li>⚠️ 处理大数组时需考虑内存分配次数</li>
 *   <li>⚠️ 建议设置合并阈值（40-60）切换插入排序</li>
 * </ul>
 *
 * @author sucf
 */
public class MergeSort {
    private static final int INSERTION_THRESHOLD = 40;

    /**
     * 归并排序入口
     * @param arr 待排序数组（自动处理null）
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        int[] temp = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1, temp);
    }

    /**
     * 递归分治方法
     */
    private static void mergeSort(int[] arr, int left, int right, int[] temp) {
        if (right - left <= INSERTION_THRESHOLD) {
            insertionSort(arr, left, right);
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid, temp);
        mergeSort(arr, mid + 1, right, temp);
        merge(arr, left, mid, right, temp);
    }

    /**
     * 合并两个有序区间
     */
    private static void merge(int[] arr, int left, int mid, int right, int[] temp) {
        System.arraycopy(arr, left, temp, left, right - left + 1);
        
        int i = left, j = mid + 1;
        int k = left;
        
        while (i <= mid && j <= right) {
            arr[k++] = (temp[i] <= temp[j]) ? temp[i++] : temp[j++];
        }
        while (i <= mid) arr[k++] = temp[i++];
        while (j <= right) arr[k++] = temp[j++];
    }

    /**
     * 插入排序（优化小数组）
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

    /**
     * 泛型归并排序（支持自定义比较器）
     */
    public static <T> void sort(T[] arr, Comparator<? super T> comparator) {
        // 实现类似上述逻辑，需要创建临时数组
    }

    public static void main(String[] args) {
        int[] arr = {12, 11, 13, 5, 6, 7};
        System.out.println("原始数组: " + Arrays.toString(arr));
        sort(arr);
        System.out.println("排序结果: " + Arrays.toString(arr));

        int[] millionTest = new java.util.Random().ints(1_000_000).toArray();
        long start = System.currentTimeMillis();
        sort(millionTest);
        System.out.printf("百万数据排序耗时: %dms%n", System.currentTimeMillis() - start);
    }
}