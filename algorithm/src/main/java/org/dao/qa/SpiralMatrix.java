package main.java.org.dao.qa;

import java.util.*;

/**
 * 螺旋矩阵问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个 m x n 矩阵，按照螺旋顺序，返回矩阵中的所有元素。
 * 螺旋顺序是指从左到右、从上到下、从右到左、从下到上循环遍历。
 * 
 * <p><b>示例</b>:
 * 输入: matrix = [[1,2,3],[4,5,6],[7,8,9]]
 * 输出: [1,2,3,6,9,8,7,4,5]
 * 
 * 输入: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
 * 输出: [1,2,3,4,8,12,11,10,9,5,6,7]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第54题)
 * 
 * <p><b>解题思路</b>:
 * 1. 边界收缩法: 维护四个边界，按螺旋方向遍历并收缩边界
 * 2. 方向转换法: 使用方向数组，碰到边界或已访问元素时转向
 * 3. 递归分治法: 递归处理外圈和内圈
 * 4. 模拟转圈法: 模拟实际的转圈过程
 * 
 * <p><b>时间复杂度</b>:
 *  边界收缩法: O(m*n)
 *  方向转换法: O(m*n)
 *  递归分治法: O(m*n)
 *  模拟转圈法: O(m*n)
 * 
 * <p><b>空间复杂度</b>:
 *  边界收缩法: O(1)
 *  方向转换法: O(m*n) 访问标记数组
 *  递归分治法: O(min(m,n)) 递归栈
 *  模拟转圈法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 图像处理中的螺旋扫描
 * 2. 打印机螺旋打印模式
 * 3. 游戏地图的螺旋加载
 * 4. 数据可视化的螺旋布局
 * 5. 螺旋排序和数据重组
 */

public class SpiralMatrix {
    
    // ========================= 解法1: 边界收缩法 (推荐) =========================
    
    /**
     * 边界收缩法 - 维护四个边界指针
     * 
     * @param matrix 输入矩阵
     * @return 螺旋遍历结果
     */
    public static List<Integer> spiralOrderBoundary(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return result;
        }
        
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        
        while (top <= bottom && left <= right) {
            // 从左到右遍历上边界
            for (int col = left; col <= right; col++) {
                result.add(matrix[top][col]);
            }
            top++;
            
            // 从上到下遍历右边界
            for (int row = top; row <= bottom; row++) {
                result.add(matrix[row][right]);
            }
            right--;
            
            // 从右到左遍历下边界（如果还有行）
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    result.add(matrix[bottom][col]);
                }
                bottom--;
            }
            
            // 从下到上遍历左边界（如果还有列）
            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    result.add(matrix[row][left]);
                }
                left++;
            }
        }
        
        return result;
    }
    
    // ========================= 解法2: 方向转换法 =========================
    
    /**
     * 方向转换法 - 使用方向数组控制移动
     * 
     * @param matrix 输入矩阵
     * @return 螺旋遍历结果
     */
    public static List<Integer> spiralOrderDirection(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return result;
        }
        
        int m = matrix.length, n = matrix[0].length;
        boolean[][] visited = new boolean[m][n];
        
        // 方向数组：右、下、左、上
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int directionIndex = 0;
        
        int row = 0, col = 0;
        
        for (int i = 0; i < m * n; i++) {
            result.add(matrix[row][col]);
            visited[row][col] = true;
            
            // 计算下一个位置
            int nextRow = row + directions[directionIndex][0];
            int nextCol = col + directions[directionIndex][1];
            
            // 检查是否需要转向
            if (nextRow < 0 || nextRow >= m || nextCol < 0 || nextCol >= n || 
                visited[nextRow][nextCol]) {
                directionIndex = (directionIndex + 1) % 4;
                nextRow = row + directions[directionIndex][0];
                nextCol = col + directions[directionIndex][1];
            }
            
            row = nextRow;
            col = nextCol;
        }
        
        return result;
    }
    
    // ========================= 解法3: 递归分治法 =========================
    
    /**
     * 递归分治法 - 递归处理外层和内层
     * 
     * @param matrix 输入矩阵
     * @return 螺旋遍历结果
     */
    public static List<Integer> spiralOrderRecursive(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return result;
        }
        
        spiralHelper(matrix, 0, 0, matrix.length - 1, matrix[0].length - 1, result);
        
        return result;
    }
    
    private static void spiralHelper(int[][] matrix, int startRow, int startCol, 
                                   int endRow, int endCol, List<Integer> result) {
        if (startRow > endRow || startCol > endCol) {
            return;
        }
        
        // 只有一行
        if (startRow == endRow) {
            for (int col = startCol; col <= endCol; col++) {
                result.add(matrix[startRow][col]);
            }
            return;
        }
        
        // 只有一列
        if (startCol == endCol) {
            for (int row = startRow; row <= endRow; row++) {
                result.add(matrix[row][startCol]);
            }
            return;
        }
        
        // 遍历外层边界
        // 上边界：从左到右
        for (int col = startCol; col < endCol; col++) {
            result.add(matrix[startRow][col]);
        }
        
        // 右边界：从上到下
        for (int row = startRow; row < endRow; row++) {
            result.add(matrix[row][endCol]);
        }
        
        // 下边界：从右到左
        for (int col = endCol; col > startCol; col--) {
            result.add(matrix[endRow][col]);
        }
        
        // 左边界：从下到上
        for (int row = endRow; row > startRow; row--) {
            result.add(matrix[row][startCol]);
        }
        
        // 递归处理内层
        spiralHelper(matrix, startRow + 1, startCol + 1, endRow - 1, endCol - 1, result);
    }
    
    // ========================= 解法4: 模拟转圈法 =========================
    
    /**
     * 模拟转圈法 - 完全模拟螺旋转圈过程
     * 
     * @param matrix 输入矩阵
     * @return 螺旋遍历结果
     */
    public static List<Integer> spiralOrderSimulation(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return result;
        }
        
        int m = matrix.length, n = matrix[0].length;
        int totalElements = m * n;
        int processed = 0;
        
        int layer = 0;
        
        while (processed < totalElements) {
            // 计算当前层的边界
            int top = layer;
            int bottom = m - 1 - layer;
            int left = layer;
            int right = n - 1 - layer;
            
            // 如果只剩一行或一列
            if (top == bottom) {
                for (int col = left; col <= right && processed < totalElements; col++) {
                    result.add(matrix[top][col]);
                    processed++;
                }
                break;
            }
            
            if (left == right) {
                for (int row = top; row <= bottom && processed < totalElements; row++) {
                    result.add(matrix[row][left]);
                    processed++;
                }
                break;
            }
            
            // 遍历当前层的四条边
            // 上边：从左到右
            for (int col = left; col < right && processed < totalElements; col++) {
                result.add(matrix[top][col]);
                processed++;
            }
            
            // 右边：从上到下
            for (int row = top; row < bottom && processed < totalElements; row++) {
                result.add(matrix[row][right]);
                processed++;
            }
            
            // 下边：从右到左
            for (int col = right; col > left && processed < totalElements; col--) {
                result.add(matrix[bottom][col]);
                processed++;
            }
            
            // 左边：从下到上
            for (int row = bottom; row > top && processed < totalElements; row--) {
                result.add(matrix[row][left]);
                processed++;
            }
            
            layer++;
        }
        
        return result;
    }
    
    // ========================= 螺旋矩阵变体问题 =========================
    
    /**
     * 螺旋矩阵 II - 生成螺旋矩阵
     * 
     * @param n 矩阵大小
     * @return n x n的螺旋矩阵
     */
    public static int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        
        if (n <= 0) {
            return matrix;
        }
        
        int top = 0, bottom = n - 1;
        int left = 0, right = n - 1;
        int num = 1;
        
        while (top <= bottom && left <= right) {
            // 填充上边界
            for (int col = left; col <= right; col++) {
                matrix[top][col] = num++;
            }
            top++;
            
            // 填充右边界
            for (int row = top; row <= bottom; row++) {
                matrix[row][right] = num++;
            }
            right--;
            
            // 填充下边界
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    matrix[bottom][col] = num++;
                }
                bottom--;
            }
            
            // 填充左边界
            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    matrix[row][left] = num++;
                }
                left++;
            }
        }
        
        return matrix;
    }
    
    /**
     * 螺旋矩阵 III - 从指定位置开始的螺旋遍历
     * 
     * @param rows 矩阵行数
     * @param cols 矩阵列数
     * @param rStart 起始行
     * @param cStart 起始列
     * @return 螺旋遍历的坐标序列
     */
    public static int[][] spiralMatrixIII(int rows, int cols, int rStart, int cStart) {
        List<int[]> result = new ArrayList<>();
        
        // 方向：东、南、西、北
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int directionIndex = 0;
        
        int steps = 1;
        int r = rStart, c = cStart;
        
        result.add(new int[]{r, c});
        
        while (result.size() < rows * cols) {
            for (int i = 0; i < 2; i++) { // 每个步长重复两次
                for (int j = 0; j < steps; j++) {
                    r += directions[directionIndex][0];
                    c += directions[directionIndex][1];
                    
                    if (r >= 0 && r < rows && c >= 0 && c < cols) {
                        result.add(new int[]{r, c});
                    }
                }
                directionIndex = (directionIndex + 1) % 4;
            }
            steps++;
        }
        
        return result.toArray(new int[result.size()][]);
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 螺旋图像处理器
     */
    public static class SpiralImageProcessor {
        
        /**
         * 将图像按螺旋顺序转换为一维数组
         * 
         * @param image 二维图像数据
         * @return 螺旋排列的一维数组
         */
        public static int[] imageToSpiralArray(int[][] image) {
            if (image == null || image.length == 0) {
                return new int[0];
            }
            
            List<Integer> spiral = spiralOrderBoundary(image);
            return spiral.stream().mapToInt(Integer::intValue).toArray();
        }
        
        /**
         * 将一维螺旋数组还原为矩阵
         * 
         * @param spiralArray 螺旋排列的数组
         * @param rows 目标行数
         * @param cols 目标列数
         * @return 还原的二维矩阵
         */
        public static int[][] spiralArrayToMatrix(int[] spiralArray, int rows, int cols) {
            if (spiralArray == null || spiralArray.length != rows * cols) {
                throw new IllegalArgumentException("数组长度与矩阵尺寸不匹配");
            }
            
            int[][] matrix = new int[rows][cols];
            int index = 0;
            
            int top = 0, bottom = rows - 1;
            int left = 0, right = cols - 1;
            
            while (top <= bottom && left <= right && index < spiralArray.length) {
                // 填充上边界
                for (int col = left; col <= right && index < spiralArray.length; col++) {
                    matrix[top][col] = spiralArray[index++];
                }
                top++;
                
                // 填充右边界
                for (int row = top; row <= bottom && index < spiralArray.length; row++) {
                    matrix[row][right] = spiralArray[index++];
                }
                right--;
                
                // 填充下边界
                if (top <= bottom) {
                    for (int col = right; col >= left && index < spiralArray.length; col--) {
                        matrix[bottom][col] = spiralArray[index++];
                    }
                    bottom--;
                }
                
                // 填充左边界
                if (left <= right) {
                    for (int row = bottom; row >= top && index < spiralArray.length; row--) {
                        matrix[row][left] = spiralArray[index++];
                    }
                    left++;
                }
            }
            
            return matrix;
        }
        
        /**
         * 螺旋模糊处理
         * 
         * @param image 原始图像
         * @param blurRadius 模糊半径
         * @return 模糊处理后的图像
         */
        public static double[][] spiralBlur(int[][] image, int blurRadius) {
            if (image == null || image.length == 0) {
                return new double[0][0];
            }
            
            int rows = image.length;
            int cols = image[0].length;
            double[][] result = new double[rows][cols];
            
            // 获取螺旋遍历顺序
            List<Integer> spiralOrder = spiralOrderBoundary(image);
            
            // 按螺旋顺序应用模糊
            int index = 0;
            int top = 0, bottom = rows - 1;
            int left = 0, right = cols - 1;
            
            while (top <= bottom && left <= right) {
                // 处理上边界
                for (int col = left; col <= right; col++) {
                    result[top][col] = applySpiralBlur(spiralOrder, index, blurRadius);
                    index++;
                }
                top++;
                
                // 处理右边界
                for (int row = top; row <= bottom; row++) {
                    result[row][right] = applySpiralBlur(spiralOrder, index, blurRadius);
                    index++;
                }
                right--;
                
                // 处理下边界
                if (top <= bottom) {
                    for (int col = right; col >= left; col--) {
                        result[bottom][col] = applySpiralBlur(spiralOrder, index, blurRadius);
                        index++;
                    }
                    bottom--;
                }
                
                // 处理左边界
                if (left <= right) {
                    for (int row = bottom; row >= top; row--) {
                        result[row][left] = applySpiralBlur(spiralOrder, index, blurRadius);
                        index++;
                    }
                    left++;
                }
            }
            
            return result;
        }
        
        private static double applySpiralBlur(List<Integer> spiralOrder, int centerIndex, int radius) {
            double sum = 0;
            int count = 0;
            
            for (int i = Math.max(0, centerIndex - radius); 
                 i <= Math.min(spiralOrder.size() - 1, centerIndex + radius); i++) {
                sum += spiralOrder.get(i);
                count++;
            }
            
            return count > 0 ? sum / count : 0;
        }
    }
    
    /**
     * 螺旋数据布局器
     */
    public static class SpiralDataLayouter {
        
        /**
         * 创建螺旋数据布局
         * 
         * @param data 输入数据
         * @param rows 布局行数
         * @param cols 布局列数
         * @return 螺旋布局的数据
         */
        public static <T> T[][] createSpiralLayout(T[] data, int rows, int cols, Class<T> clazz) {
            if (data == null || data.length > rows * cols) {
                throw new IllegalArgumentException("数据量超过布局容量");
            }
            
            @SuppressWarnings("unchecked")
            T[][] layout = (T[][]) java.lang.reflect.Array.newInstance(clazz, rows, cols);
            
            int index = 0;
            int top = 0, bottom = rows - 1;
            int left = 0, right = cols - 1;
            
            while (top <= bottom && left <= right && index < data.length) {
                // 填充上边界
                for (int col = left; col <= right && index < data.length; col++) {
                    layout[top][col] = data[index++];
                }
                top++;
                
                // 填充右边界
                for (int row = top; row <= bottom && index < data.length; row++) {
                    layout[row][right] = data[index++];
                }
                right--;
                
                // 填充下边界
                if (top <= bottom) {
                    for (int col = right; col >= left && index < data.length; col--) {
                        layout[bottom][col] = data[index++];
                    }
                    bottom--;
                }
                
                // 填充左边界
                if (left <= right) {
                    for (int row = bottom; row >= top && index < data.length; row--) {
                        layout[row][left] = data[index++];
                    }
                    left++;
                }
            }
            
            return layout;
        }
        
        /**
         * 螺旋排序数据
         * 
         * @param matrix 输入矩阵
         * @param ascending 是否升序
         * @return 按螺旋顺序排序后的矩阵
         */
        public static int[][] spiralSort(int[][] matrix, boolean ascending) {
            if (matrix == null || matrix.length == 0) {
                return matrix;
            }
            
            // 获取所有元素
            List<Integer> elements = new ArrayList<>();
            for (int[] row : matrix) {
                for (int val : row) {
                    elements.add(val);
                }
            }
            
            // 排序
            if (ascending) {
                elements.sort(Integer::compareTo);
            } else {
                elements.sort(Collections.reverseOrder());
            }
            
            // 按螺旋顺序填充回矩阵
            int rows = matrix.length;
            int cols = matrix[0].length;
            int[][] result = new int[rows][cols];
            
            int index = 0;
            int top = 0, bottom = rows - 1;
            int left = 0, right = cols - 1;
            
            while (top <= bottom && left <= right && index < elements.size()) {
                // 填充上边界
                for (int col = left; col <= right && index < elements.size(); col++) {
                    result[top][col] = elements.get(index++);
                }
                top++;
                
                // 填充右边界
                for (int row = top; row <= bottom && index < elements.size(); row++) {
                    result[row][right] = elements.get(index++);
                }
                right--;
                
                // 填充下边界
                if (top <= bottom) {
                    for (int col = right; col >= left && index < elements.size(); col--) {
                        result[bottom][col] = elements.get(index++);
                    }
                    bottom--;
                }
                
                // 填充左边界
                if (left <= right) {
                    for (int row = bottom; row >= top && index < elements.size(); row--) {
                        result[row][left] = elements.get(index++);
                    }
                    left++;
                }
            }
            
            return result;
        }
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化螺旋遍历过程
     * 
     * @param matrix 输入矩阵
     */
    public static void visualizeSpiralTraversal(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            System.out.println("输入矩阵为空");
            return;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        
        System.out.println("\n螺旋遍历过程可视化:");
        System.out.println("原始矩阵:");
        printMatrix(matrix);
        
        boolean[][] visited = new boolean[m][n];
        List<Integer> result = new ArrayList<>();
        
        int top = 0, bottom = m - 1;
        int left = 0, right = n - 1;
        int step = 1;
        
        System.out.println("\n遍历过程:");
        
        while (top <= bottom && left <= right) {
            System.out.println("第 " + step + " 轮遍历:");
            System.out.printf("  边界: top=%d, bottom=%d, left=%d, right=%d\n", top, bottom, left, right);
            
            // 上边界：从左到右
            System.out.print("  上边界: ");
            for (int col = left; col <= right; col++) {
                result.add(matrix[top][col]);
                visited[top][col] = true;
                System.out.print(matrix[top][col] + " ");
            }
            System.out.println();
            top++;
            
            // 右边界：从上到下
            if (top <= bottom) {
                System.out.print("  右边界: ");
                for (int row = top; row <= bottom; row++) {
                    result.add(matrix[row][right]);
                    visited[row][right] = true;
                    System.out.print(matrix[row][right] + " ");
                }
                System.out.println();
            }
            right--;
            
            // 下边界：从右到左
            if (top <= bottom) {
                System.out.print("  下边界: ");
                for (int col = right; col >= left; col--) {
                    result.add(matrix[bottom][col]);
                    visited[bottom][col] = true;
                    System.out.print(matrix[bottom][col] + " ");
                }
                System.out.println();
            }
            bottom--;
            
            // 左边界：从下到上
            if (left <= right) {
                System.out.print("  左边界: ");
                for (int row = bottom; row >= top; row--) {
                    result.add(matrix[row][left]);
                    visited[row][left] = true;
                    System.out.print(matrix[row][left] + " ");
                }
                System.out.println();
            }
            left++;
            
            System.out.println("  当前访问状态:");
            printVisitedMatrix(visited);
            System.out.println();
            
            step++;
        }
        
        System.out.println("最终结果: " + result);
    }
    
    /**
     * 可视化螺旋矩阵生成过程
     * 
     * @param n 矩阵大小
     */
    public static void visualizeSpiralGeneration(int n) {
        if (n <= 0) {
            System.out.println("矩阵大小必须大于0");
            return;
        }
        
        System.out.println("\n螺旋矩阵生成过程可视化 (n = " + n + "):");
        
        int[][] matrix = new int[n][n];
        int top = 0, bottom = n - 1;
        int left = 0, right = n - 1;
        int num = 1;
        int step = 1;
        
        while (top <= bottom && left <= right) {
            System.out.println("第 " + step + " 轮填充:");
            System.out.printf("  边界: top=%d, bottom=%d, left=%d, right=%d\n", top, bottom, left, right);
            
            // 填充上边界
            System.out.print("  填充上边界: ");
            for (int col = left; col <= right; col++) {
                matrix[top][col] = num;
                System.out.print(num + " ");
                num++;
            }
            System.out.println();
            top++;
            
            // 填充右边界
            if (top <= bottom) {
                System.out.print("  填充右边界: ");
                for (int row = top; row <= bottom; row++) {
                    matrix[row][right] = num;
                    System.out.print(num + " ");
                    num++;
                }
                System.out.println();
            }
            right--;
            
            // 填充下边界
            if (top <= bottom) {
                System.out.print("  填充下边界: ");
                for (int col = right; col >= left; col--) {
                    matrix[bottom][col] = num;
                    System.out.print(num + " ");
                    num++;
                }
                System.out.println();
            }
            bottom--;
            
            // 填充左边界
            if (left <= right) {
                System.out.print("  填充左边界: ");
                for (int row = bottom; row >= top; row--) {
                    matrix[row][left] = num;
                    System.out.print(num + " ");
                    num++;
                }
                System.out.println();
            }
            left++;
            
            System.out.println("  当前矩阵状态:");
            printMatrix(matrix);
            System.out.println();
            
            step++;
        }
        
        System.out.println("最终螺旋矩阵:");
        printMatrix(matrix);
    }
    
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.printf("%4d", val);
            }
            System.out.println();
        }
    }
    
    private static void printVisitedMatrix(boolean[][] visited) {
        for (boolean[] row : visited) {
            for (boolean val : row) {
                System.out.printf("%4s", val ? "T" : "F");
            }
            System.out.println();
        }
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param rows 矩阵行数
     * @param cols 矩阵列数
     */
    public static void comparePerformance(int rows, int cols) {
        // 生成测试矩阵
        int[][] matrix = generateTestMatrix(rows, cols);
        
        long start, end;
        
        // 测试边界收缩法
        start = System.nanoTime();
        List<Integer> result1 = spiralOrderBoundary(matrix);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试方向转换法
        start = System.nanoTime();
        List<Integer> result2 = spiralOrderDirection(matrix);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试递归分治法
        start = System.nanoTime();
        List<Integer> result3 = spiralOrderRecursive(matrix);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试模拟转圈法
        start = System.nanoTime();
        List<Integer> result4 = spiralOrderSimulation(matrix);
        end = System.nanoTime();
        long time4 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("矩阵尺寸: %d x %d\n", rows, cols);
        System.out.println("===============================================");
        System.out.println("方法            | 时间(ms)   | 相对速度");
        System.out.println("----------------|------------|----------");
        System.out.printf("边界收缩法      | %.6f | 基准\n", time1 / 1_000_000.0);
        System.out.printf("方向转换法      | %.6f | %.2fx\n", time2 / 1_000_000.0, (double)time2/time1);
        System.out.printf("递归分治法      | %.6f | %.2fx\n", time3 / 1_000_000.0, (double)time3/time1);
        System.out.printf("模拟转圈法      | %.6f | %.2fx\n", time4 / 1_000_000.0, (double)time4/time1);
        System.out.println("===============================================");
        
        // 验证结果一致性
        boolean consistent = result1.equals(result2) && result2.equals(result3) && result3.equals(result4);
        System.out.println("结果一致性: " + (consistent ? "通过" : "失败"));
    }
    
    // 生成测试矩阵
    private static int[][] generateTestMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        int num = 1;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = num++;
            }
        }
        
        return matrix;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testBasicCases();
        testEdgeCases();
        testApplicationScenarios();
        testPerformance();
    }
    
    private static void testBasicCases() {
        System.out.println("====== 基础测试 ======");
        
        int[][] matrix1 = {{1,2,3},{4,5,6},{7,8,9}};
        testCase("示例测试1", matrix1, Arrays.asList(1,2,3,6,9,8,7,4,5));
        
        int[][] matrix2 = {{1,2,3,4},{5,6,7,8},{9,10,11,12}};
        testCase("示例测试2", matrix2, Arrays.asList(1,2,3,4,8,12,11,10,9,5,6,7));
        
        int[][] matrix3 = {{1}};
        testCase("单元素矩阵", matrix3, Arrays.asList(1));
        
        int[][] matrix4 = {{1,2},{3,4}};
        testCase("2x2矩阵", matrix4, Arrays.asList(1,2,4,3));
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        int[][] matrix1 = {{1,2,3}};
        testCase("单行矩阵", matrix1, Arrays.asList(1,2,3));
        
        int[][] matrix2 = {{1},{2},{3}};
        testCase("单列矩阵", matrix2, Arrays.asList(1,2,3));
        
        int[][] matrix3 = {};
        testCase("空矩阵", matrix3, Arrays.asList());
        
        int[][] matrix4 = {{1,2,3,4,5}};
        testCase("长条矩阵", matrix4, Arrays.asList(1,2,3,4,5));
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 螺旋矩阵生成
        System.out.println("\n螺旋矩阵生成测试:");
        int[][] generated = generateMatrix(4);
        System.out.println("生成的4x4螺旋矩阵:");
        printMatrix(generated);
        
        // 场景2: 螺旋矩阵III
        System.out.println("\n螺旋矩阵III测试:");
        int[][] positions = spiralMatrixIII(1, 4, 0, 0);
        System.out.println("从(0,0)开始的螺旋遍历坐标:");
        for (int[] pos : positions) {
            System.out.printf("(%d,%d) ", pos[0], pos[1]);
        }
        System.out.println();
        
        // 场景3: 图像处理
        System.out.println("\n图像处理测试:");
        int[][] image = {{1,2,3},{4,5,6},{7,8,9}};
        System.out.println("原始图像:");
        printMatrix(image);
        
        int[] spiralArray = SpiralImageProcessor.imageToSpiralArray(image);
        System.out.println("螺旋化的一维数组: " + Arrays.toString(spiralArray));
        
        int[][] restored = SpiralImageProcessor.spiralArrayToMatrix(spiralArray, 3, 3);
        System.out.println("还原的图像:");
        printMatrix(restored);
        
        // 场景4: 数据布局
        System.out.println("\n数据布局测试:");
        String[] data = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        String[][] layout = SpiralDataLayouter.createSpiralLayout(data, 3, 3, String.class);
        System.out.println("螺旋数据布局:");
        for (String[] row : layout) {
            System.out.println(Arrays.toString(row));
        }
        
        // 场景5: 螺旋排序
        System.out.println("\n螺旋排序测试:");
        int[][] unsorted = {{9,2,7},{4,1,8},{3,6,5}};
        System.out.println("原始矩阵:");
        printMatrix(unsorted);
        
        int[][] sorted = SpiralDataLayouter.spiralSort(unsorted, true);
        System.out.println("螺旋升序排序后:");
        printMatrix(sorted);
        
        // 可视化演示
        System.out.println("\n可视化演示:");
        int[][] demoMatrix = {{1,2,3},{4,5,6},{7,8,9}};
        visualizeSpiralTraversal(demoMatrix);
        visualizeSpiralGeneration(3);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        
        int[][] testSizes = {{10, 10}, {50, 20}, {100, 100}, {200, 150}};
        
        for (int[] size : testSizes) {
            comparePerformance(size[0], size[1]);
        }
    }
    
    private static void testCase(String name, int[][] matrix, List<Integer> expected) {
        System.out.printf("\n测试用例: %s\n", name);
        System.out.println("输入矩阵:");
        if (matrix.length > 0) {
            printMatrix(matrix);
        } else {
            System.out.println("空矩阵");
        }
        
        List<Integer> result1 = spiralOrderBoundary(matrix);
        List<Integer> result2 = spiralOrderDirection(matrix);
        List<Integer> result3 = spiralOrderRecursive(matrix);
        List<Integer> result4 = spiralOrderSimulation(matrix);
        
        System.out.println("边界收缩法结果: " + result1);
        System.out.println("方向转换法结果: " + result2);
        System.out.println("递归分治法结果: " + result3);
        System.out.println("模拟转圈法结果: " + result4);
        System.out.println("期望结果: " + expected);
        
        boolean isCorrect = result1.equals(expected) && result2.equals(expected) && 
                           result3.equals(expected) && result4.equals(expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小规模矩阵展示可视化
        if (matrix.length > 0 && matrix.length <= 5 && matrix[0].length <= 5) {
            visualizeSpiralTraversal(matrix);
        }
    }
}