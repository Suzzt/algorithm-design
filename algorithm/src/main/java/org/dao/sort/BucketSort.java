package main.java.org.dao.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 自适应桶排序算法实现
 * 
 * <p><b>设计思路：</b></p>
 * <ol>
 *   <li>基于分散-排序-收集的分治策略，核心在于合理的数据分桶</li>
 *   <li>动态计算桶数量：根据数据范围和分布自动调整</li>
 *   <li>混合排序策略：大桶使用快速排序，小桶使用插入排序</li>
 *   <li>支持正负数和浮点数（通过标准化处理）</li>
 * </ol>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 均匀分布的浮点数据（最佳性能）</li>
 *   <li>✅ 数据范围已知的整数排序</li>
 *   <li>❌ 数据分布极度不均匀的情况</li>
 *   <li>❌ 需要稳定排序的场景（改用归并排序）</li>
 * </ul>
 *
 * <p><b>复杂度分析：</b></p>
 * <table border="1">
 *   <tr><th>场景</th><th>时间复杂度</th><th>空间复杂度</th></tr>
 *   <tr><td>最佳情况</td><td>O(n)</td><td>O(n+k)</td></tr>
 *   <tr><td>平均情况</td><td>O(n + n²/k + k)</td><td>O(n+k)</td></tr>
 *   <tr><td>最坏情况</td><td>O(n²)</td><td>O(n)</td></tr>
 * </table>
 * <i>k表示桶的数量，当k≈n时达到线性时间复杂度</i>
 *
 * <p><b>可扩展方向：</b></p>
 * <ul>
 *   <li>并行处理：不同桶的排序可以多线程执行</li>
 *   <li>分布式版本：将桶分布到不同节点处理</li>
 *   <li>自动检测数据分布（直方图分析）</li>
 * </ul>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *   <li>⚠️ 输入数据应避免极端离群值（会大幅降低性能）</li>
 *   <li>⚠️ 处理负数时需要特殊偏移处理</li>
 *   <li>⚠️ 内存敏感场景需限制最大桶数量</li>
 * </ul>
 *
 * @author sucf
 * @see Arrays#sort() JDK内置排序实现
 */
public class BucketSort {
    // 禁止实例化
    private BucketSort() {}

    // 默认桶数量计算因子
    private static final double BUCKET_FACTOR = 0.2;
    
    /**
     * 桶排序入口方法（升序）
     * @param arr 待排序数组（支持负数）
     * @throws IllegalArgumentException 输入数组为null时抛出
     */
    public static void sort(double[] arr) {
        if (arr == null) throw new IllegalArgumentException("输入数组不能为null");
        if (arr.length <= 1) return;

        // 1. 数据标准化处理
        double[] normalized = normalize(arr);
        
        // 2. 动态计算桶数量
        int bucketCount = calculateBucketCount(arr.length);
        
        // 3. 初始化桶
        List<List<Double>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }

        // 4. 数据分桶
        for (double num : normalized) {
            int bucketIndex = (int) (num * bucketCount);
            buckets.get(bucketIndex).add(num);
        }

        // 5. 各桶分别排序
        for (List<Double> bucket : buckets) {
            hybridSort(bucket);
        }

        // 6. 合并结果并还原
        int index = 0;
        for (List<Double> bucket : buckets) {
            for (double num : bucket) {
                arr[index++] = denormalize(num, getMin(arr), getMax(arr));
            }
        }
    }

    /**
     * 数据标准化到[0,1)范围（兼容负数处理）
     */
    private static double[] normalize(double[] arr) {
        double min = getMin(arr);
        double max = getMax(arr);
        double range = max - min;

        double[] normalized = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            normalized[i] = (arr[i] - min) / range;
        }
        return normalized;
    }

    /**
     * 动态计算桶数量（基于数据量和分布）
     */
    private static int calculateBucketCount(int n) {
        int calculated = (int) (Math.sqrt(n) * BUCKET_FACTOR);
        return Math.max(10, Math.min(calculated, 1000));
    }

    /**
     * 混合排序策略（自动选择排序算法）
     */
    private static void hybridSort(List<Double> list) {
        if (list.size() <= 16) {
            insertionSort(list);
        } else {
            Collections.sort(list);
        }
    }

    /**
     * 插入排序（用于小数据集）
     */
    private static void insertionSort(List<Double> list) {
        for (int i = 1; i < list.size(); i++) {
            double key = list.get(i);
            int j = i - 1;
            
            while (j >= 0 && list.get(j) > key) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    // 实用工具方法
    private static double getMax(double[] arr) {
        double max = Double.NEGATIVE_INFINITY;
        for (double num : arr) {
            if (num > max) max = num;
        }
        return max;
    }

    private static double getMin(double[] arr) {
        double min = Double.POSITIVE_INFINITY;
        for (double num : arr) {
            if (num < min) min = num;
        }
        return min;
    }

    private static double denormalize(double val, double min, double max) {
        return val * (max - min) + min;
    }

    /**
     * 验证方法
     */
    public static void main(String[] args) {
        testCase(new double[]{0.5, 0.2, 0.9, 0.1}, "均匀分布浮点数");
        testCase(new double[]{-3.1, 5.2, -1.8, 0.0}, "含负数数据");
        testCase(new double[]{1.0, 1.0, 1.0, 1.0}, "全重复数据");
        testCase(new double[]{}, "空数组");
    }

    private static void testCase(double[] arr, String caseName) {
        System.out.println("测试案例: " + caseName);
        System.out.println("排序前: " + Arrays.toString(arr));
        sort(arr);
        System.out.println("排序后: " + Arrays.toString(arr));
        System.out.println("验证" + (isSorted(arr) ? "成功" : "失败"));
        System.out.println("----------------------");
    }

    private static boolean isSorted(double[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) return false;
        }
        return true;
    }
}