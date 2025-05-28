package main.java.org.dao.sort;

import java.util.Arrays;

/**
 * 基数排序算法实现
 * 
 * <p><b>设计亮点：</b></p>
 * <ol>
 *   <li>支持正负数混合排序</li>
 *   <li>自动检测数据范围优化性能</li>
 *   <li>内存预分配减少GC压力</li>
 *   <li>兼容JDK风格API设计</li>
 * </ol>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 固定长度的数字（身份证号、电话号码）</li>
 *   <li>✅ 取值范围较大的整数（10^18以内）</li>
 *   <li>✅ 需要稳定排序的数值数据</li>
 *   <li>❌ 浮点数排序（需特殊处理）</li>
 *   <li>❌ 非数值数据（字符串需改造）</li>
 * </ul>
 *
 * <p><b>复杂度分析：</b></p>
 * <table border="1">
 *   <tr><th>数据特征</th><th>时间复杂度</th><th>空间复杂度</th></tr>
 *   <tr><td>d位数字</td><td>O(d*(n+k))</td><td>O(n+k)</td></tr>
 *   <tr><td>32位整数</td><td>O(32n) → O(n)</td><td>O(n)</td></tr>
 * </table>
 * <i>k为基数大小（通常取256）</i>
 */
public class RadixSort {
    private static final int BITS = 8;    // 每次处理的比特数
    private static final int RADIX = 1 << BITS; // 基数大小（256）
    private static final int MASK = RADIX - 1;  // 位掩码（0xFF）

    /**
     * 基数排序入口
     * @param arr 待排序数组（自动处理负数）
     */
    public static void sort(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        
        // 1. 数据预处理：处理负数
        int min = Arrays.stream(arr).min().getAsInt();
        int max = Arrays.stream(arr).max().getAsInt();
        boolean hasNegative = min < 0;
        
        // 负数处理：偏移转正数
        if (hasNegative) {
            int offset = -min;
            for (int i = 0; i < arr.length; i++) {
                arr[i] += offset;
            }
            max += offset;
        }

        // 2. 计算最大位数
        int digits = (int)(Math.log(max)/Math.log(RADIX)) + 1;
        
        // 3. 执行多轮计数排序
        int[] output = new int[arr.length];
        int[] count = new int[RADIX];
        
        for (int d = 0; d < digits; d++) {
            Arrays.fill(count, 0);
            
            // 统计频率
            int shift = d * BITS;
            for (int num : arr) {
                int digit = (num >> shift) & MASK;
                count[digit]++;
            }
            
            // 计算前缀和
            for (int i = 1; i < RADIX; i++) {
                count[i] += count[i - 1];
            }
            
            // 反向填充保证稳定性
            for (int i = arr.length - 1; i >= 0; i--) {
                int digit = (arr[i] >> shift) & MASK;
                output[--count[digit]] = arr[i];
            }
            
            // 交换数组引用
            int[] temp = arr;
            arr = output;
            output = temp;
        }

        // 4. 恢复负数数据
        if (hasNegative) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] -= (-min);
            }
        }
    }

    public static void main(String[] args) {
        testCase(new int[]{170, 45, 75, 90, 802, 24, 2, 66}, "普通整数");
        testCase(new int[]{-3, -100, 0, 5, -10, 7}, "含负数");
        testCase(new int[]{999999999, 1000000000, 123456789}, "大数测试");
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