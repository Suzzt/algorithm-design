package main.java.org.dao.qa;

import java.util.*;

/**
 * N-Queens (N皇后问题) 解决方案
 * 
 * <p><b>问题描述：</b>
 * 在一个N×N的棋盘上放置N个皇后，使得每个皇后都不同行、不同列，也不能同一条对角线（包括主对角线和反对角线）
 * 
 * <p><b>输出要求：</b>
 * 返回所有解决方案，每个解决方案使用棋盘格式表示，其中'Q'表示皇后，'.'表示空位
 * 
 * <p><b>示例 (N=4)：</b>
 * [
 *  [".Q..", 
 *   "...Q",
 *   "Q...", 
 *   "..Q."],
 * 
 *  ["..Q.", 
 *   "Q...",
 *   "...Q", 
 *   ".Q.."]
 * ]
 * 
 * <p><b>解题思路：</b>
 * 使用回溯算法逐行放置皇后，同时记录已占用的列和两个对角线
 * 
 * <p><b>应用场景：</b>
 * 1. 组合数学中的经典问题
 * 2. 并行计算和分布式系统的调度问题
 * 3. 人工智能中的约束满足问题
 * 4. 芯片设计和电路板布局
 * 
 * <p><b>时间复杂度：</b> O(N!) (最优情况下)
 */
public class NQueens {

    /**
     * 解决N皇后问题主方法
     * 
     * @param n 棋盘大小（n×n）
     * @return 所有解决方案的列表
     */
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> solutions = new ArrayList<>();
        // 初始化棋盘
        char[][] board = new char[n][n];
        for (char[] row : board) {
            Arrays.fill(row, '.');
        }
        
        // 回溯法求解
        backtrack(board, 0, new HashSet<>(), new HashSet<>(), new HashSet<>(), solutions);
        return solutions;
    }
    
    /**
     * 回溯算法核心实现
     * 
     * @param board 当前棋盘状态
     * @param row 当前处理的行
     * @param columns 已占用的列集合
     * @param diagonals1 主对角线(左上->右下)集合
     * @param diagonals2 副对角线(右上->左下)集合
     * @param solutions 存储所有解决方案
     */
    private static void backtrack(char[][] board, int row, 
                                 Set<Integer> columns, 
                                 Set<Integer> diagonals1, 
                                 Set<Integer> diagonals2,
                                 List<List<String>> solutions) {
        int n = board.length;
        // 终止条件：已经放置所有皇后
        if (row == n) {
            solutions.add(createSolution(board));
            return;
        }
        
        // 尝试在当前行的每一列放置皇后
        for (int col = 0; col < n; col++) {
            int diag1 = row - col;  // 主对角线标识
            int diag2 = row + col;  // 副对角线标识
            
            // 检查当前位置是否安全
            if (columns.contains(col) || diagonals1.contains(diag1) || diagonals2.contains(diag2)) {
                continue;
            }
            
            // 放置皇后
            board[row][col] = 'Q';
            columns.add(col);
            diagonals1.add(diag1);
            diagonals2.add(diag2);
            
            // 递归到下一行
            backtrack(board, row + 1, columns, diagonals1, diagonals2, solutions);
            
            // 回溯：移除皇后
            board[row][col] = '.';
            columns.remove(col);
            diagonals1.remove(diag1);
            diagonals2.remove(diag2);
        }
    }
    
    /**
     * 将当前棋盘状态转换为解决方案格式
     */
    private static List<String> createSolution(char[][] board) {
        List<String> solution = new ArrayList<>();
        for (char[] row : board) {
            solution.add(new String(row));
        }
        return solution;
    }
    
    // =========================== 应用场景 ===========================
    
    /**
     * 打印所有解决方案的可视化棋盘
     * 
     * @param solutions 所有解决方案
     */
    public static void visualizeSolutions(List<List<String>> solutions) {
        System.out.println("\n所有解决方案：");
        for (int i = 0; i < solutions.size(); i++) {
            System.out.println("方案 #" + (i + 1) + ":");
            List<String> solution = solutions.get(i);
            for (String row : solution) {
                for (char c : row.toCharArray()) {
                    System.out.print(c + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    
    /**
     * 生成棋盘布局统计数据
     * 
     * @param n 皇后数量
     * @return 解决方案数量统计报告
     */
    public static String generateStatistics(int n) {
        StringBuilder stats = new StringBuilder();
        long startTime = System.currentTimeMillis();
        List<List<String>> solutions = solveNQueens(n);
        long endTime = System.currentTimeMillis();
        
        stats.append("N皇后问题统计报告 (N=").append(n).append(")\n");
        stats.append("=================================\n");
        stats.append("解决方案总数: ").append(solutions.size()).append("\n");
        stats.append("计算时间: ").append(endTime - startTime).append("ms\n\n");
        
        if (n <= 6) {
            stats.append("所有解决方案可视化:\n");
            for (int i = 0; i < solutions.size(); i++) {
                stats.append("方案 #").append(i + 1).append(":\n");
                for (String row : solutions.get(i)) {
                    stats.append(row).append("\n");
                }
                stats.append("\n");
            }
        }
        
        if (n > 1) {
            stats.append("对称性分析:\n");
            stats.append("- 旋转对称方案: ").append(countSymmetricSolutions(solutions)).append("\n");
            stats.append("- 轴对称方案: ").append(countAxisSymmetricSolutions(solutions)).append("\n");
        }
        
        return stats.toString();
    }
    
    /**
     * 计算对称解决方案数量
     */
    private static int countSymmetricSolutions(List<List<String>> solutions) {
        Set<String> uniqueSolutions = new HashSet<>();
        for (List<String> solution : solutions) {
            uniqueSolutions.add(solution.toString());
        }
        return solutions.size() - uniqueSolutions.size();
    }
    
    /**
     * 计算轴对称解决方案数量
     */
    private static int countAxisSymmetricSolutions(List<List<String>> solutions) {
        int count = 0;
        for (List<String> solution : solutions) {
            if (isAxisSymmetric(solution)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * 检查解决方案是否轴对称
     */
    private static boolean isAxisSymmetric(List<String> board) {
        int n = board.size();
        for (int i = 0; i < n; i++) {
            if (!board.get(i).equals(board.get(n - 1 - i))) {
                return false;
            }
        }
        return true;
    }
    
    // =========================== 测试方法 ===========================
    
    public static void main(String[] args) {
        int[] testCases = {4, 5, 6};
        
        for (int n : testCases) {
            System.out.println("===== 测试 N = " + n + " =====");
            long startTime = System.currentTimeMillis();
            List<List<String>> solutions = solveNQueens(n);
            long endTime = System.currentTimeMillis();
            
            System.out.println("解决方案数量: " + solutions.size());
            System.out.println("计算时间: " + (endTime - startTime) + "ms");
            
            if (n <= 6) {
                visualizeSolutions(solutions);
            }
            
            // 生成统计报告
            if (n >= 4) {
                System.out.println(generateStatistics(n));
            }
        }
    }
}