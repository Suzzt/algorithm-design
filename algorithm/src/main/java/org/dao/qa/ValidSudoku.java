package main.java.org.dao.qa;

import java.util.*;

/**
 * 有效的数独问题
 * 
 * <p><b>问题描述</b>:
 * 判断一个9×9的数独是否有效。只需要根据以下规则验证已填入的数字是否有效：
 * 1. 每行必须包含1-9的数字且不重复
 * 2. 每列必须包含1-9的数字且不重复
 * 3. 每个3×3的子网格必须包含1-9的数字且不重复
 * 
 * <p><b>示例</b>:
 * 输入:
 * [
 *   ["5","3",".",".","7",".",".",".","."],
 *   ["6",".",".","1","9","5",".",".","."],
 *   [".","9","8",".",".",".",".","6","."],
 *   ["8",".",".",".","6",".",".",".","3"],
 *   ["4",".",".","8",".","3",".",".","1"],
 *   ["7",".",".",".","2",".",".",".","6"],
 *   [".","6",".",".",".",".","2","8","."],
 *   [".",".",".","4","1","9",".",".","5"],
 *   [".",".",".",".","8",".",".","7","9"]
 * ]
 * 输出: true
 * 
 * <p><b>问题难度</b>: 🔥 中等 (LeetCode 第36题)
 * 
 * <p><b>解题思路</b>:
 * 1. 三次遍历法: 分别检查行、列和子网格
 * 2. 一次遍历法: 使用哈希表同时记录行、列和子网格
 * 3. 位运算法: 使用位掩码优化空间
 * 
 * <p><b>时间复杂度</b>:
 *  所有方法: O(1) (固定81个单元格)
 * 
 * <p><b>空间复杂度</b>:
 *  三次遍历法: O(n)
 *  一次遍历法: O(n)
 *  位运算法: O(1)
 * 
 * <p><b>应用场景</b>:
 * 1. 数独游戏验证器
 * 2. 数独解题器
 * 3. 数据完整性检查
 * 4. 约束满足问题求解
 * 5. 数独生成器
 */

public class ValidSudoku {
    
    // ========================= 解法1: 三次遍历法 =========================
    
    /**
     * 三次遍历法 - 直观解法
     * 
     * @param board 数独棋盘
     * @return 是否有效
     */
    public static boolean isValidSudokuThreePass(char[][] board) {
        // 检查所有行
        for (int i = 0; i < 9; i++) {
            if (!isValidUnit(board[i])) {
                return false;
            }
        }
        
        // 检查所有列
        for (int j = 0; j < 9; j++) {
            char[] column = new char[9];
            for (int i = 0; i < 9; i++) {
                column[i] = board[i][j];
            }
            if (!isValidUnit(column)) {
                return false;
            }
        }
        
        // 检查所有3x3子网格
        for (int block = 0; block < 9; block++) {
            char[] subgrid = new char[9];
            int rowStart = (block / 3) * 3;
            int colStart = (block % 3) * 3;
            int idx = 0;
            for (int i = rowStart; i < rowStart + 3; i++) {
                for (int j = colStart; j < colStart + 3; j++) {
                    subgrid[idx++] = board[i][j];
                }
            }
            if (!isValidUnit(subgrid)) {
                return false;
            }
        }
        
        return true;
    }
    
    // 检查一个单元（行、列或子网格）是否有效
    private static boolean isValidUnit(char[] unit) {
        Set<Character> seen = new HashSet<>();
        for (char c : unit) {
            if (c != '.') {
                if (seen.contains(c)) {
                    return false;
                }
                seen.add(c);
            }
        }
        return true;
    }
    
    // ========================= 解法2: 一次遍历法 =========================
    
    /**
     * 一次遍历法 - 高效解法
     * 
     * @param board 数独棋盘
     * @return 是否有效
     */
    public static boolean isValidSudokuOnePass(char[][] board) {
        // 使用三个数组记录每行、每列、每个子网格的数字出现情况
        boolean[][] rowSeen = new boolean[9][9];
        boolean[][] colSeen = new boolean[9][9];
        boolean[][] boxSeen = new boolean[9][9];
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c != '.') {
                    int num = c - '1'; // 转换为0-8的索引
                    int boxIndex = (i / 3) * 3 + j / 3;
                    
                    // 检查是否重复
                    if (rowSeen[i][num] || colSeen[j][num] || boxSeen[boxIndex][num]) {
                        return false;
                    }
                    
                    // 标记为已出现
                    rowSeen[i][num] = true;
                    colSeen[j][num] = true;
                    boxSeen[boxIndex][num] = true;
                }
            }
        }
        
        return true;
    }
    
    // ========================= 解法3: 位运算法 =========================
    
    /**
     * 位运算法 - 空间最优解
     * 
     * @param board 数独棋盘
     * @return 是否有效
     */
    public static boolean isValidSudokuBitwise(char[][] board) {
        int[] rows = new int[9];
        int[] cols = new int[9];
        int[] boxes = new int[9];
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c != '.') {
                    int num = 1 << (c - '1'); // 位掩码
                    int boxIndex = (i / 3) * 3 + j / 3;
                    
                    // 检查是否重复
                    if ((rows[i] & num) != 0 || 
                        (cols[j] & num) != 0 || 
                        (boxes[boxIndex] & num) != 0) {
                        return false;
                    }
                    
                    // 标记为已出现
                    rows[i] |= num;
                    cols[j] |= num;
                    boxes[boxIndex] |= num;
                }
            }
        }
        
        return true;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 数独游戏验证器
     * 
     * @param board 数独棋盘
     * @return 是否有效
     */
    public static boolean sudokuValidator(char[][] board) {
        return isValidSudokuOnePass(board);
    }
    
    /**
     * 数独解题器前置检查
     * 
     * @param board 数独棋盘
     * @return 是否可解
     */
    public static boolean sudokuSolverPrecheck(char[][] board) {
        return isValidSudokuBitwise(board);
    }
    
    /**
     * 数据完整性检查器
     * 
     * @param data 9x9数据矩阵
     * @return 是否满足数独约束
     */
    public static boolean dataIntegrityChecker(int[][] data) {
        char[][] charBoard = new char[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                charBoard[i][j] = data[i][j] == 0 ? '.' : (char)('0' + data[i][j]);
            }
        }
        return isValidSudokuOnePass(charBoard);
    }
    
    /**
     * 数独生成器辅助工具
     * 
     * @param board 数独棋盘
     * @param row 行
     * @param col 列
     * @param num 要填入的数字
     * @return 填入后是否有效
     */
    public static boolean isValidPlacement(char[][] board, int row, int col, char num) {
        // 检查行
        for (int j = 0; j < 9; j++) {
            if (board[row][j] == num) return false;
        }
        
        // 检查列
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) return false;
        }
        
        // 检查3x3子网格
        int boxRowStart = (row / 3) * 3;
        int boxColStart = (col / 3) * 3;
        for (int i = boxRowStart; i < boxRowStart + 3; i++) {
            for (int j = boxColStart; j < boxColStart + 3; j++) {
                if (board[i][j] == num) return false;
            }
        }
        
        return true;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化数独棋盘
     * 
     * @param board 数独棋盘
     */
    public static void visualizeSudoku(char[][] board) {
        System.out.println("\n数独棋盘:");
        System.out.println("  +-------+-------+-------+");
        for (int i = 0; i < 9; i++) {
            System.out.print((i + 1) + " | ");
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] == '.' ? "  " : board[i][j] + " ");
                if (j % 3 == 2) System.out.print("| ");
            }
            System.out.println();
            if (i % 3 == 2) {
                System.out.println("  +-------+-------+-------+");
            }
        }
        System.out.println("    a b c   d e f   g h i");
    }
    
    /**
     * 可视化冲突检测
     * 
     * @param board 数独棋盘
     */
    public static void visualizeConflicts(char[][] board) {
        boolean[][] conflicts = new boolean[9][9];
        boolean[][] rowSeen = new boolean[9][9];
        boolean[][] colSeen = new boolean[9][9];
        boolean[][] boxSeen = new boolean[9][9];
        
        // 第一次遍历：检测冲突
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c != '.') {
                    int num = c - '1';
                    int boxIndex = (i / 3) * 3 + j / 3;
                    
                    if (rowSeen[i][num] || colSeen[j][num] || boxSeen[boxIndex][num]) {
                        conflicts[i][j] = true;
                    }
                    
                    rowSeen[i][num] = true;
                    colSeen[j][num] = true;
                    boxSeen[boxIndex][num] = true;
                }
            }
        }
        
        // 第二次遍历：标记冲突
        System.out.println("\n冲突检测:");
        System.out.println("  +-------+-------+-------+");
        for (int i = 0; i < 9; i++) {
            System.out.print((i + 1) + " | ");
            for (int j = 0; j < 9; j++) {
                if (conflicts[i][j]) {
                    System.out.print("\u001B[31m" + board[i][j] + "\u001B[0m ");
                } else {
                    System.out.print(board[i][j] == '.' ? "  " : board[i][j] + " ");
                }
                if (j % 3 == 2) System.out.print("| ");
            }
            System.out.println();
            if (i % 3 == 2) {
                System.out.println("  +-------+-------+-------+");
            }
        }
        System.out.println("    a b c   d e f   g h i");
    }
    
    /**
     * 可视化位掩码状态
     * 
     * @param board 数独棋盘
     */
    public static void visualizeBitwiseState(char[][] board) {
        int[] rows = new int[9];
        int[] cols = new int[9];
        int[] boxes = new int[9];
        
        System.out.println("\n位掩码状态:");
        System.out.println("行 | 位掩码 | 二进制");
        System.out.println("---+--------+------------------");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                char c = board[i][j];
                if (c != '.') {
                    int num = 1 << (c - '1');
                    int boxIndex = (i / 3) * 3 + j / 3;
                    rows[i] |= num;
                    cols[j] |= num;
                    boxes[boxIndex] |= num;
                }
            }
            System.out.printf("%2d | %6d | %s%n", i + 1, rows[i], toBinaryString(rows[i]));
        }
        
        System.out.println("\n列 | 位掩码 | 二进制");
        System.out.println("---+--------+------------------");
        for (int j = 0; j < 9; j++) {
            System.out.printf("%2d | %6d | %s%n", j + 1, cols[j], toBinaryString(cols[j]));
        }
        
        System.out.println("\n子网格 | 位掩码 | 二进制");
        System.out.println("------+--------+------------------");
        for (int b = 0; b < 9; b++) {
            System.out.printf("%5d | %6d | %s%n", b + 1, boxes[b], toBinaryString(boxes[b]));
        }
    }
    
    // 将整数转换为9位二进制字符串
    private static String toBinaryString(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 8; i >= 0; i--) {
            sb.append((n & (1 << i)) != 0 ? '1' : '0');
        }
        return sb.toString();
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     */
    public static void comparePerformance() {
        char[][] board = generateSudokuBoard();
        
        long start, end;
        
        // 测试三次遍历法
        start = System.nanoTime();
        boolean result1 = isValidSudokuThreePass(board);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试一次遍历法
        start = System.nanoTime();
        boolean result2 = isValidSudokuOnePass(board);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试位运算法
        start = System.nanoTime();
        boolean result3 = isValidSudokuBitwise(board);
        end = System.nanoTime();
        long time3 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.println("============================================");
        System.out.println("方法            | 时间(ns) | 结果");
        System.out.println("----------------|----------|--------");
        System.out.printf("三次遍历法      | %8d | %b%n", time1, result1);
        System.out.printf("一次遍历法      | %8d | %b%n", time2, result2);
        System.out.printf("位运算法        | %8d | %b%n", time3, result3);
        System.out.println("============================================");
    }
    
    // 生成数独棋盘
    private static char[][] generateSudokuBoard() {
        char[][] board = new char[9][9];
        Random random = new Random();
        
        // 填充一些随机数字
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (random.nextDouble() > 0.7) {
                    board[i][j] = (char)('1' + random.nextInt(9));
                } else {
                    board[i][j] = '.';
                }
            }
        }
        return board;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        testValidCases();
        testInvalidCases();
        testApplicationScenarios();
        testPerformance();
    }
    
    private static void testValidCases() {
        System.out.println("====== 有效数独测试 ======");
        testCase("示例数独", new char[][]{
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        }, true);
        
        testCase("空棋盘", new char[9][9], true);
    }
    
    private static void testInvalidCases() {
        System.out.println("\n====== 无效数独测试 ======");
        testCase("行重复", new char[][]{
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','5'} // 最后一行有两个5
        }, false);
        
        testCase("列重复", new char[][]{
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'5','.','.','.','8','.','.','7','9'} // 第一列有两个5
        }, false);
        
        testCase("子网格重复", new char[][]{
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','8','7','9'} // 右下角有两个8
        }, false);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 数独游戏验证
        char[][] gameBoard = {
            {'5','3','4','6','7','8','9','1','2'},
            {'6','7','2','1','9','5','3','4','8'},
            {'1','9','8','3','4','2','5','6','7'},
            {'8','5','9','7','6','1','4','2','3'},
            {'4','2','6','8','5','3','7','9','1'},
            {'7','1','3','9','2','4','8','5','6'},
            {'9','6','1','5','3','7','2','8','4'},
            {'2','8','7','4','1','9','6','3','5'},
            {'3','4','5','2','8','6','1','7','9'}
        };
        System.out.println("完整数独验证: " + sudokuValidator(gameBoard));
        
        // 场景2: 数独解题器前置检查
        char[][] partialBoard = {
            {'5','3','.','.','7','.','.','.','.'},
            {'6','.','.','1','9','5','.','.','.'},
            {'.','9','8','.','.','.','.','6','.'},
            {'8','.','.','.','6','.','.','.','3'},
            {'4','.','.','8','.','3','.','.','1'},
            {'7','.','.','.','2','.','.','.','6'},
            {'.','6','.','.','.','.','2','8','.'},
            {'.','.','.','4','1','9','.','.','5'},
            {'.','.','.','.','8','.','.','7','9'}
        };
        System.out.println("部分数独验证: " + sudokuSolverPrecheck(partialBoard));
        
        // 场景3: 数据完整性检查
        int[][] dataMatrix = {
            {5,3,0,0,7,0,0,0,0},
            {6,0,0,1,9,5,0,0,0},
            {0,9,8,0,0,0,0,6,0},
            {8,0,0,0,6,0,0,0,3},
            {4,0,0,8,0,3,0,0,1},
            {7,0,0,0,2,0,0,0,6},
            {0,6,0,0,0,0,2,8,0},
            {0,0,0,4,1,9,0,0,5},
            {0,0,0,0,8,0,0,7,9}
        };
        System.out.println("数据完整性检查: " + dataIntegrityChecker(dataMatrix));
        
        // 场景4: 数独生成器辅助
        char[][] generatedBoard = generateSudokuBoard();
        System.out.println("随机生成数独有效性: " + sudokuValidator(generatedBoard));
        
        // 可视化测试
        System.out.println("\n可视化测试:");
        visualizeSudoku(partialBoard);
        visualizeConflicts(partialBoard);
        visualizeBitwiseState(partialBoard);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance();
    }
    
    private static void testCase(String name, char[][] board, boolean expected) {
        boolean result1 = isValidSudokuThreePass(board);
        boolean result2 = isValidSudokuOnePass(board);
        boolean result3 = isValidSudokuBitwise(board);
        
        System.out.printf("\n测试: %s%n", name);
        System.out.println("三次遍历法结果: " + result1);
        System.out.println("一次遍历法结果: " + result2);
        System.out.println("位运算法结果: " + result3);
        
        boolean passed = (result1 == expected) && 
                         (result2 == expected) && 
                         (result3 == expected);
        System.out.println("测试结果: " + (passed ? "通过" : "失败"));
        
        // 可视化小规模案例
        if (board.length <= 9) {
            visualizeSudoku(board);
        }
    }
}