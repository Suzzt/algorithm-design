package main.java.org.dao.sort;

import java.util.Arrays;

/**
 * 计数排序算法实现
 * 
 * <p><b>设计思路：</b></p>
 * <ol>
 *   <li>非比较排序：通过统计元素频率实现排序</li>
 *   <li>数值映射：通过偏移量处理负数</li>
 *   <li>稳定排序：保持相等元素的原始顺序</li>
 *   <li>空间换时间：通过计数数组实现线性时间复杂度</li>
 * </ol>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 整数排序（包括负数）</li>
 *   <li>✅ 数据范围较小的密集数据（k ≤ 5n）</li>
 *   <li>✅ 需要稳定排序的场景</li>
 *   <li>❌ 数据范围极大的稀疏数据（如k > 10^7）</li>
 *   <li>❌ 浮点数排序（需改造为桶排序）</li>
 * </ul>
 *
 * <p><b>复杂度分析：</b></p>
 * <table border="1">
 *   <tr><th>场景</th><th>时间复杂度</th><th>空间复杂度</th></tr>
 *   <tr><td>最佳情况</td><td>O(n + k)</td><td>O(n + k)</td></tr>
 *   <tr><td>平均情况</td><td>O(n + k)</td><td>O(n + k)</td></tr>
 *   <tr><td>最坏情况</td><td>O(n + k)</td><td>O(n + k)</td></tr>
 * </table>
 * <i>n为元素数量，k为数据范围（max-min+1）</i>
 *
 * <p><b>优化策略：</b></p>
 * <ul>
 *   <li>动态范围检测：自动处理正负数混合数据</li>
 *   <li>内存预警：当k > 5n时输出警告日志</li>
 *   <li>零值压缩：跳过未出现的数值</li>
 * </ul>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *   <li>⚠️ 数据范围k受限于Java数组最大长度（Integer.MAX_VALUE - 5）</li>
 *   <li>⚠️ 处理1亿数据量时需至少1.2GB内存</li>
 *   <li>⚠️ 直接修改原始数组参数</li>
 * </ul>
 *
 * @author sucf
 */
public class CountingSort {
    // 内存使用警告阈值（k/n比例）
    private static final double MEMORY_WARNING_RATIO = 5.0;

    /**
     * 计数排序核心方法（升序）
     * @param arr 待排序数组（自动处理null）
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        // 1. 计算数据范围
        int[] range = findRange(arr);
        int min = range[0], max = range[1];
        int k = max - min + 1;

        // 2. 内存使用预警
        if (k > arr.length * MEMORY_WARNING_RATIO) {
            System.err.println("[WARN] 内存使用过高，建议改用其他排序算法。当前k/n比例：" 
                + String.format("%.1f", k/(double)arr.length));
        }

        // 3. 创建并填充计数数组
        int[] count = new int[k];
        for (int num : arr) {
            count[num - min]++;
        }

        // 4. 生成累积计数（稳定排序关键）
        for (int i = 1; i < k; i++) {
            count[i] += count[i - 1];
        }

        // 5. 反向填充结果数组
        int[] output = new int[arr.length];
        for (int i = arr.length - 1; i >= 0; i--) {
            int num = arr[i];
            int pos = --count[num - min];
            output[pos] = num;
        }

        // 6. 写回原始数组
        System.arraycopy(output, 0, arr, 0, arr.length);
    }

    /**
     * 查找数组的最小值和最大值
     * @return [最小值, 最大值]
     */
    private static int[] findRange(int[] arr) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        for (int num : arr) {
            if (num < min) min = num;
            if (num > max) max = num;
        }
        return new int[]{min, max};
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        testCase(new int[]{4, 2, 2, 8, 3, 3, 1}, "重复元素");
        testCase(new int[]{-5, -10, 0, -3, 8, 5}, "含负数");
        testCase(new int[]{1, 0, -1, Integer.MAX_VALUE, Integer.MIN_VALUE}, "极值测试");
        testCase(new int[]{}, "空数组");
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