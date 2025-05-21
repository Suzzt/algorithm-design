package main.java.org.dao.sort;

import java.util.Arrays;

/**
 * 工业级优化的冒泡排序实现
 *
 * <p><b>设计思路：</b></p>
 * <ol>
 *   <li>通过相邻元素比较交换，逐步将最大/小元素"冒泡"到序列末端</li>
 *   <li>双重循环结构：外层控制遍历轮次，内层执行元素比较</li>
 *   <li>提前终止优化：检测到已有序时立即结束排序</li>
 *   <li>边界收缩优化：每轮减少末尾已排序区的比较次数</li>
 * </ol>
 *
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 教学演示排序算法基本原理</li>
 *   <li>✅ 小规模数据排序（n ≤ 100）</li>
 *   <li>✅ 基本有序数据的微调（交换次数少）</li>
 *   <li>❌ 大规模随机数据排序（时间复杂度高）</li>
 *   <li>❌ 内存敏感型场景（无优化空间）</li>
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
 * <p><b>可扩展方向：</b></p>
 * <ul>
 *   <li>鸡尾酒排序优化：双向交替遍历（适合钟形分布数据）</li>
 *   <li>并行化改造：分块比较交换（需处理数据依赖）</li>
 *   <li>泛型支持：实现Comparable接口版本</li>
 * </ul>
 *
 * <p><b>注意事项：</b></p>
 * <ul>
 *   <li>⚠️ 10万数据规模时性能比快速排序慢1000倍以上</li>
 *   <li>⚠️ 保持排序稳定性（相等元素不交换）</li>
 *   <li>⚠️ 提前终止优化对完全随机数据无效</li>
 * </ul>
 *
 * @author sucf
 */
public class BubbleSort {

    /**
     * 优化的冒泡排序实现
     *
     * @param arr       要排序的整型数组
     * @param ascending 排序方向：true为升序，false为降序
     */
    public static void bubbleSort(int[] arr, boolean ascending) {
        if (arr == null || arr.length <= 1) {
            return; // 无需排序
        }

        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            // 每次遍历减少比较范围（已排序部分无需再比较）
            for (int j = 0; j < n - 1 - i; j++) {
                // 根据排序方向进行条件判断
                boolean shouldSwap = ascending ?
                        (arr[j] > arr[j + 1]) :
                        (arr[j] < arr[j + 1]);

                if (shouldSwap) {
                    // 交换元素
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            // 提前终止优化：如果本轮没有交换，说明已排序完成
            if (!swapped) break;
        }
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        int[] testArray = {64, 34, 25, 12, 22, 11, 90};
        int[] emptyArray = {};
        int[] singleElementArray = {5};
        int[] alreadySorted = {1, 2, 3, 4, 5};

        System.out.println("原始数组：" + Arrays.toString(testArray));

        // 升序排序
        bubbleSort(testArray, true);
        System.out.println("升序排序：" + Arrays.toString(testArray));

        // 降序排序
        bubbleSort(testArray, false);
        System.out.println("降序排序：" + Arrays.toString(testArray));

        // 边界测试
        bubbleSort(emptyArray, true);
        System.out.println("空数组测试：" + Arrays.toString(emptyArray));

        bubbleSort(singleElementArray, true);
        System.out.println("单元素数组：" + Arrays.toString(singleElementArray));

        bubbleSort(alreadySorted, true);
        System.out.println("已排序数组：" + Arrays.toString(alreadySorted));
    }
}