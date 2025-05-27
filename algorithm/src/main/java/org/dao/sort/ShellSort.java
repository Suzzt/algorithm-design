package main.java.org.dao.sort;

import java.util.Arrays;

/**
 * 希尔排序算法实现
 * 
 * <p><b>设计思路：</b></p>
 * <ol>
 *   <li>插入排序改进：通过分组插入实现预排序</li>
 *   <li>动态间隔序列：使用Hibbard序列优化时间复杂度</li>
 *   <li>原地排序：无需额外存储空间</li>
 *   <li>不稳定排序：元素可能跨越多个位置移动</li>
 * </ol>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 中等规模数据（1万 ≤ n ≤ 50万）</li>
 *   <li>✅ 需要平衡时间和空间复杂度</li>
 *   <li>✅ 对稳定性无要求的场景</li>
 *   <li>❌ 内存敏感型设备（可使用更简单算法）</li>
 *   <li>❌ 数据完全逆序（退化为O(n²)）</li>
 * </ul>
 *
 * <p><b>复杂度分析：</b></p>
 * <table border="1">
 *   <tr><th>间隔序列</th><th>最佳情况</th><th>平均情况</th><th>最坏情况</th></tr>
 *   <tr><td>Shell原始序列</td><td>O(n log n)</td><td>O(n²)</td><td>O(n²)</td></tr>
 *   <tr><td>Hibbard序列</td><td>O(n^(3/2))</td><td>O(n^(5/4))</td><td>O(n^(3/2))</td></tr>
 *   <tr><td>Sedgewick序列</td><td>O(n log n)</td><td>O(n^(4/3))</td><td>O(n^(3/2))</td></tr>
 * </table>
 *
 * <p><b>优化策略：</b></p>
 * <ul>
 *   <li>动态间隔序列：根据数据规模自动选择最优序列</li>
 *   <li>插入排序优化：当间隔为1时使用优化的插入排序</li>
 *   <li>边界检查：跳过已排序元素减少比较次数</li>
 * </ul>
 *
 * @author sucf
 */
public class ShellSort {
    // 切换插入排序的阈值
    private static final int INSERTION_THRESHOLD = 100;

    /**
     * 希尔排序入口（整型数组）
     * @param arr 待排序数组（自动处理null）
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        
        int n = arr.length;
        // 根据数据规模选择间隔序列
        if (n > 1_000_000) {
            sedgewickSort(arr);  // 大数据量使用Sedgewick序列
        } else {
            hibbardSort(arr);    // 中小数据量使用Hibbard序列
        }
    }

    /**
     * 使用Hibbard序列实现（时间复杂度O(n^(3/2))）
     */
    private static void hibbardSort(int[] arr) {
        int n = arr.length;
        // 生成Hibbard序列：1, 3, 7, 15, 31...
        int gap = 1;
        while (gap < n / 3) {
            gap = gap * 3 + 1;  // 公式：h_k = 2^k - 1
        }

        while (gap > 0) {
            // 当间隔较小时切换插入排序
            if (gap < 10 && n > INSERTION_THRESHOLD) {
                insertionSort(arr, 0, n - 1);
                break;
            }

            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                // 分组插入排序
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
            gap = (gap - 1) / 3;  // 逆向计算Hibbard序列
        }
    }

    /**
     * 使用Sedgewick序列实现（最佳时间复杂度O(n log n)）
     */
    private static void sedgewickSort(int[] arr) {
        int n = arr.length;
        // 预计算Sedgewick序列
        int[] sedgewickGaps = {1, 5, 19, 41, 109, 209, 505, 929, 2161, 3905};
        
        // 找到最大的可用间隔
        int k = 0;
        while (sedgewickGaps[k] < n / 2 && k < sedgewickGaps.length - 1) {
            k++;
        }

        for (int gap = sedgewickGaps[k]; gap > 0; gap = k > 0 ? sedgewickGaps[--k] : 0) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                while (j >= gap && arr[j - gap] > temp) {
                    arr[j] = arr[j - gap];
                    j -= gap;
                }
                arr[j] = temp;
            }
        }
    }

    /**
     * 优化的插入排序（用于小间隔排序）
     */
    private static void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            
            // 跳过已排序元素
            if (arr[j] <= key) continue;
            
            while (j >= left && arr[j] > key) {
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
        testCase(new int[]{12, 34, 54, 2, 3}, "普通数组");
        testCase(new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1}, "逆序数组");
        testCase(new int[]{3, 1, 4, 1, 5, 9, 2, 6}, "重复元素");
        
        // 百万级数据测试
        int[] millionTest = new java.util.Random().ints(1_000_000).toArray();
        long start = System.currentTimeMillis();
        sort(millionTest);
        System.out.printf("百万数据排序耗时: %dms%n", System.currentTimeMillis() - start);
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
}