package main.java.org.dao.qa;

import java.util.*;

/**
 * 岛屿数量问题
 * 
 * <p><b>问题描述</b>:
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 此外，你可以假设该网格的四条边均被水包围。
 * 
 * <p><b>示例</b>:
 * 输入: grid = [
 *   ["1","1","1","1","0"],
 *   ["1","1","0","1","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","0","0","0"]
 * ]
 * 输出: 1
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第200题)
 * 
 * <p><b>解题思路</b>:
 * 1. DFS深度优先搜索: 遇到陆地就开始DFS，将连通的陆地全部标记为已访问
 * 2. BFS广度优先搜索: 使用队列进行层序遍历，标记连通区域
 * 3. 并查集法: 将相邻陆地合并到同一集合，最后统计集合数量
 * 4. 洪水填充法: 将找到的岛屿"淹没"，避免重复计算
 * 
 * <p><b>时间复杂度</b>:
 *  DFS: O(M×N) M为行数，N为列数
 *  BFS: O(M×N)
 *  并查集: O(M×N×α(M×N)) α为阿克曼函数的反函数
 *  洪水填充: O(M×N)
 * 
 * <p><b>空间复杂度</b>:
 *  DFS: O(M×N) 最坏情况下递归深度
 *  BFS: O(min(M,N)) 队列空间
 *  并查集: O(M×N)
 *  洪水填充: O(M×N)
 * 
 * <p><b>应用场景</b>:
 * 1. 地理信息系统(GIS)中的连通区域分析
 * 2. 图像处理中的连通组件检测
 * 3. 网络连通性分析
 * 4. 游戏地图中的区域划分
 * 5. 疫情传播路径分析
 */

public class NumberOfIslands {
    
    // ========================= 解法1: DFS深度优先搜索 (推荐) =========================
    
    /**
     * DFS解法
     * 
     * @param grid 二维网格
     * @return 岛屿数量
     */
    public static int numIslandsDFS(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private static void dfs(char[][] grid, int i, int j) {
        int m = grid.length;
        int n = grid[0].length;
        
        if (i < 0 || j < 0 || i >= m || j >= n || grid[i][j] == '0') {
            return;
        }
        
        grid[i][j] = '0'; // 标记为已访问
        
        // 向四个方向递归
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }
    
    // ========================= 解法2: BFS广度优先搜索 =========================
    
    /**
     * BFS解法
     * 
     * @param grid 二维网格
     * @return 岛屿数量
     */
    public static int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    
                    // BFS
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[]{i, j});
                    grid[i][j] = '0';
                    
                    while (!queue.isEmpty()) {
                        int[] current = queue.poll();
                        
                        for (int[] dir : directions) {
                            int x = current[0] + dir[0];
                            int y = current[1] + dir[1];
                            
                            if (x >= 0 && y >= 0 && x < m && y < n && grid[x][y] == '1') {
                                queue.offer(new int[]{x, y});
                                grid[x][y] = '0';
                            }
                        }
                    }
                }
            }
        }
        
        return count;
    }
    
    // ========================= 解法3: 并查集法 =========================
    
    /**
     * 并查集解法
     * 
     * @param grid 二维网格
     * @return 岛屿数量
     */
    public static int numIslandsUnionFind(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int m = grid.length;
        int n = grid[0].length;
        
        UnionFind uf = new UnionFind(grid);
        
        int[][] directions = {{1, 0}, {0, 1}};
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    for (int[] dir : directions) {
                        int x = i + dir[0];
                        int y = j + dir[1];
                        if (x < m && y < n && grid[x][y] == '1') {
                            uf.union(i * n + j, x * n + y);
                        }
                    }
                }
            }
        }
        
        return uf.getCount();
    }
    
    static class UnionFind {
        int[] parent;
        int[] rank;
        int count;
        
        public UnionFind(char[][] grid) {
            int m = grid.length;
            int n = grid[0].length;
            parent = new int[m * n];
            rank = new int[m * n];
            count = 0;
            
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == '1') {
                        parent[i * n + j] = i * n + j;
                        count++;
                    }
                    rank[i * n + j] = 0;
                }
            }
        }
        
        public int find(int i) {
            if (parent[i] != i) parent[i] = find(parent[i]);
            return parent[i];
        }
        
        public void union(int x, int y) {
            int rootx = find(x);
            int rooty = find(y);
            if (rootx != rooty) {
                if (rank[rootx] > rank[rooty]) {
                    parent[rooty] = rootx;
                } else if (rank[rootx] < rank[rooty]) {
                    parent[rootx] = rooty;
                } else {
                    parent[rooty] = rootx;
                    rank[rootx] += 1;
                }
                count--;
            }
        }
        
        public int getCount() {
            return count;
        }
    }
    
    // ========================= 解法4: 洪水填充法 =========================
    
    /**
     * 洪水填充解法
     * 
     * @param grid 二维网格
     * @return 岛屿数量
     */
    public static int numIslandsFloodFill(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;
        
        int m = grid.length;
        int n = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    floodFill(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private static void floodFill(char[][] grid, int i, int j) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{i, j});
        
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0], y = current[1];
            
            if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length || grid[x][y] == '0') {
                continue;
            }
            
            grid[x][y] = '0';
            
            for (int[] dir : directions) {
                stack.push(new int[]{x + dir[0], y + dir[1]});
            }
        }
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 地理信息系统 - 分析连通的陆地区域
     * 
     * @param elevationMap 高程图 (>0表示陆地)
     * @return 连通陆地区域信息
     */
    public static List<LandRegion> analyzeConnectedLandRegions(int[][] elevationMap) {
        if (elevationMap == null || elevationMap.length == 0) return new ArrayList<>();
        
        int m = elevationMap.length;
        int n = elevationMap[0].length;
        boolean[][] visited = new boolean[m][n];
        List<LandRegion> regions = new ArrayList<>();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (elevationMap[i][j] > 0 && !visited[i][j]) {
                    LandRegion region = exploreRegion(elevationMap, visited, i, j);
                    regions.add(region);
                }
            }
        }
        
        return regions;
    }
    
    static class LandRegion {
        int area;
        int minElevation;
        int maxElevation;
        double avgElevation;
        List<int[]> coordinates;
        
        LandRegion() {
            coordinates = new ArrayList<>();
            minElevation = Integer.MAX_VALUE;
            maxElevation = Integer.MIN_VALUE;
        }
        
        void addPoint(int x, int y, int elevation) {
            coordinates.add(new int[]{x, y});
            area++;
            minElevation = Math.min(minElevation, elevation);
            maxElevation = Math.max(maxElevation, elevation);
        }
        
        void calculateAverage(int totalElevation) {
            avgElevation = (double) totalElevation / area;
        }
        
        @Override
        public String toString() {
            return String.format("区域[面积:%d, 高程:%d-%d, 平均:%.1f]", 
                               area, minElevation, maxElevation, avgElevation);
        }
    }
    
    private static LandRegion exploreRegion(int[][] elevationMap, boolean[][] visited, int startI, int startJ) {
        LandRegion region = new LandRegion();
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startI, startJ});
        visited[startI][startJ] = true;
        
        int totalElevation = 0;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1];
            int elevation = elevationMap[x][y];
            
            region.addPoint(x, y, elevation);
            totalElevation += elevation;
            
            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];
                if (nx >= 0 && ny >= 0 && nx < elevationMap.length && ny < elevationMap[0].length &&
                    !visited[nx][ny] && elevationMap[nx][ny] > 0) {
                    visited[nx][ny] = true;
                    queue.offer(new int[]{nx, ny});
                }
            }
        }
        
        region.calculateAverage(totalElevation);
        return region;
    }
    
    /**
     * 图像处理 - 连通组件检测
     * 
     * @param binaryImage 二值图像 (1表示前景，0表示背景)
     * @return 连通组件信息
     */
    public static List<ConnectedComponent> detectConnectedComponents(int[][] binaryImage) {
        if (binaryImage == null || binaryImage.length == 0) return new ArrayList<>();
        
        int m = binaryImage.length;
        int n = binaryImage[0].length;
        boolean[][] visited = new boolean[m][n];
        List<ConnectedComponent> components = new ArrayList<>();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (binaryImage[i][j] == 1 && !visited[i][j]) {
                    ConnectedComponent component = findComponent(binaryImage, visited, i, j);
                    components.add(component);
                }
            }
        }
        
        return components;
    }
    
    static class ConnectedComponent {
        int area;
        int minX, maxX, minY, maxY;
        List<int[]> pixels;
        
        ConnectedComponent() {
            pixels = new ArrayList<>();
            minX = minY = Integer.MAX_VALUE;
            maxX = maxY = Integer.MIN_VALUE;
        }
        
        void addPixel(int x, int y) {
            pixels.add(new int[]{x, y});
            area++;
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }
        
        int getBoundingBoxArea() {
            return (maxX - minX + 1) * (maxY - minY + 1);
        }
        
        @Override
        public String toString() {
            return String.format("组件[面积:%d, 边界:(%d,%d)-(%d,%d)]", 
                               area, minX, minY, maxX, maxY);
        }
    }
    
    private static ConnectedComponent findComponent(int[][] image, boolean[][] visited, int startI, int startJ) {
        ConnectedComponent component = new ConnectedComponent();
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startI, startJ});
        
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0], y = current[1];
            
            if (x < 0 || y < 0 || x >= image.length || y >= image[0].length ||
                visited[x][y] || image[x][y] == 0) {
                continue;
            }
            
            visited[x][y] = true;
            component.addPixel(x, y);
            
            for (int[] dir : directions) {
                stack.push(new int[]{x + dir[0], y + dir[1]});
            }
        }
        
        return component;
    }
    
    /**
     * 网络连通性分析
     * 
     * @param networkMap 网络拓扑图 (1表示节点，0表示断开)
     * @return 网络分区信息
     */
    public static NetworkAnalysis analyzeNetworkConnectivity(int[][] networkMap) {
        if (networkMap == null || networkMap.length == 0) return new NetworkAnalysis();
        
        int m = networkMap.length;
        int n = networkMap[0].length;
        boolean[][] visited = new boolean[m][n];
        List<Integer> partitionSizes = new ArrayList<>();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (networkMap[i][j] == 1 && !visited[i][j]) {
                    int size = exploreNetworkPartition(networkMap, visited, i, j);
                    partitionSizes.add(size);
                }
            }
        }
        
        return new NetworkAnalysis(partitionSizes);
    }
    
    static class NetworkAnalysis {
        int totalPartitions;
        int largestPartition;
        int smallestPartition;
        double averagePartitionSize;
        List<Integer> partitionSizes;
        
        NetworkAnalysis() {
            partitionSizes = new ArrayList<>();
        }
        
        NetworkAnalysis(List<Integer> sizes) {
            this.partitionSizes = sizes;
            this.totalPartitions = sizes.size();
            
            if (!sizes.isEmpty()) {
                this.largestPartition = sizes.stream().mapToInt(Integer::intValue).max().orElse(0);
                this.smallestPartition = sizes.stream().mapToInt(Integer::intValue).min().orElse(0);
                this.averagePartitionSize = sizes.stream().mapToInt(Integer::intValue).average().orElse(0);
            }
        }
        
        @Override
        public String toString() {
            return String.format("网络分析[分区数:%d, 最大:%d, 最小:%d, 平均:%.1f]",
                               totalPartitions, largestPartition, smallestPartition, averagePartitionSize);
        }
    }
    
    private static int exploreNetworkPartition(int[][] networkMap, boolean[][] visited, int startI, int startJ) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startI, startJ});
        visited[startI][startJ] = true;
        
        int size = 0;
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1];
            size++;
            
            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];
                if (nx >= 0 && ny >= 0 && nx < networkMap.length && ny < networkMap[0].length &&
                    !visited[nx][ny] && networkMap[nx][ny] == 1) {
                    visited[nx][ny] = true;
                    queue.offer(new int[]{nx, ny});
                }
            }
        }
        
        return size;
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化网格和岛屿
     * 
     * @param grid 原始网格
     */
    public static void visualizeIslands(char[][] grid) {
        if (grid == null || grid.length == 0) return;
        
        System.out.println("\n岛屿可视化:");
        System.out.println("原始网格:");
        printGrid(grid);
        
        // 复制网格进行DFS标记
        char[][] copy = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        
        int count = markIslandsWithNumbers(copy);
        System.out.println("\n标记后的网格 (数字表示不同岛屿):");
        printGrid(copy);
        System.out.println("岛屿总数: " + count);
    }
    
    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
    private static int markIslandsWithNumbers(char[][] grid) {
        int count = 0;
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    markIsland(grid, i, j, (char)('A' + count - 1));
                }
            }
        }
        
        return count;
    }
    
    private static void markIsland(char[][] grid, int i, int j, char mark) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] != '1') {
            return;
        }
        
        grid[i][j] = mark;
        
        markIsland(grid, i + 1, j, mark);
        markIsland(grid, i - 1, j, mark);
        markIsland(grid, i, j + 1, mark);
        markIsland(grid, i, j - 1, mark);
    }
    
    /**
     * 可视化搜索过程
     * 
     * @param grid 网格
     */
    public static void visualizeSearchProcess(char[][] grid) {
        if (grid == null || grid.length == 0) return;
        
        System.out.println("\n搜索过程可视化:");
        
        char[][] copy = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        
        int count = 0;
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                if (copy[i][j] == '1') {
                    count++;
                    System.out.printf("\n发现第%d个岛屿，起点:(%d,%d)\n", count, i, j);
                    visualizeDFS(copy, i, j, count);
                    System.out.println("标记完成:");
                    printGrid(copy);
                }
            }
        }
    }
    
    private static void visualizeDFS(char[][] grid, int i, int j, int islandNum) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] != '1') {
            return;
        }
        
        grid[i][j] = (char)('0' + islandNum);
        System.out.printf("  标记位置(%d,%d)\n", i, j);
        
        visualizeDFS(grid, i + 1, j, islandNum);
        visualizeDFS(grid, i - 1, j, islandNum);
        visualizeDFS(grid, i, j + 1, islandNum);
        visualizeDFS(grid, i, j - 1, islandNum);
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param rows 行数
     * @param cols 列数
     * @param landRatio 陆地比例
     */
    public static void comparePerformance(int rows, int cols, double landRatio) {
        char[][] grid = generateRandomGrid(rows, cols, landRatio);
        
        long start, end;
        
        // 测试DFS
        char[][] grid1 = cloneGrid(grid);
        start = System.nanoTime();
        int result1 = numIslandsDFS(grid1);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试BFS
        char[][] grid2 = cloneGrid(grid);
        start = System.nanoTime();
        int result2 = numIslandsBFS(grid2);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试并查集
        char[][] grid3 = cloneGrid(grid);
        start = System.nanoTime();
        int result3 = numIslandsUnionFind(grid3);
        end = System.nanoTime();
        long time3 = end - start;
        
        // 测试洪水填充
        char[][] grid4 = cloneGrid(grid);
        start = System.nanoTime();
        int result4 = numIslandsFloodFill(grid4);
        end = System.nanoTime();
        long time4 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("网格大小: %d×%d, 陆地比例: %.1f%%\n", rows, cols, landRatio * 100);
        System.out.println("=========================================");
        System.out.println("方法       | 时间(ms)   | 结果 | 结果正确");
        System.out.println("-----------|------------|------|--------");
        System.out.printf("DFS        | %.6f | %4d | 基准\n", time1 / 1_000_000.0, result1);
        System.out.printf("BFS        | %.6f | %4d | %s\n", time2 / 1_000_000.0, result2, result1 == result2 ? "是" : "否");
        System.out.printf("并查集     | %.6f | %4d | %s\n", time3 / 1_000_000.0, result3, result1 == result3 ? "是" : "否");
        System.out.printf("洪水填充   | %.6f | %4d | %s\n", time4 / 1_000_000.0, result4, result1 == result4 ? "是" : "否");
        System.out.println("=========================================");
    }
    
    private static char[][] generateRandomGrid(int rows, int cols, double landRatio) {
        Random random = new Random();
        char[][] grid = new char[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = random.nextDouble() < landRatio ? '1' : '0';
            }
        }
        
        return grid;
    }
    
    private static char[][] cloneGrid(char[][] grid) {
        char[][] clone = new char[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            clone[i] = grid[i].clone();
        }
        return clone;
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
        
        char[][] grid1 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        testCase("示例1", grid1, 1);
        
        char[][] grid2 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        testCase("示例2", grid2, 3);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        char[][] empty = {};
        testCase("空网格", empty, 0);
        
        char[][] single = {{'1'}};
        testCase("单个陆地", single, 1);
        
        char[][] allWater = {{'0','0'},{'0','0'}};
        testCase("全是水", allWater, 0);
        
        char[][] allLand = {{'1','1'},{'1','1'}};
        testCase("全是陆地", allLand, 1);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 地理信息系统
        int[][] elevationMap = {
            {0, 0, 5, 3, 0},
            {0, 2, 8, 6, 0},
            {0, 0, 0, 0, 0},
            {4, 7, 0, 9, 2}
        };
        List<LandRegion> regions = analyzeConnectedLandRegions(elevationMap);
        System.out.println("\n地理信息系统分析:");
        System.out.println("高程图:");
        printElevationMap(elevationMap);
        System.out.println("连通区域分析:");
        for (int i = 0; i < regions.size(); i++) {
            System.out.println("  区域" + (i+1) + ": " + regions.get(i));
        }
        
        // 场景2: 图像处理
        int[][] binaryImage = {
            {1, 1, 0, 0, 1},
            {1, 0, 0, 1, 1},
            {0, 0, 0, 0, 0},
            {1, 0, 1, 1, 0}
        };
        List<ConnectedComponent> components = detectConnectedComponents(binaryImage);
        System.out.println("\n图像处理 - 连通组件检测:");
        System.out.println("二值图像:");
        printBinaryImage(binaryImage);
        System.out.println("连通组件:");
        for (int i = 0; i < components.size(); i++) {
            System.out.println("  组件" + (i+1) + ": " + components.get(i));
        }
        
        // 场景3: 网络连通性分析
        int[][] networkMap = {
            {1, 1, 0, 0, 1},
            {1, 0, 0, 0, 1},
            {0, 0, 1, 1, 0},
            {0, 0, 1, 0, 0}
        };
        NetworkAnalysis analysis = analyzeNetworkConnectivity(networkMap);
        System.out.println("\n网络连通性分析:");
        System.out.println("网络拓扑:");
        printBinaryImage(networkMap);
        System.out.println(analysis);
        
        // 可视化测试
        char[][] visualGrid = {
            {'1','1','0','0','1'},
            {'1','0','0','1','1'},
            {'0','0','0','0','0'},
            {'1','0','1','1','0'}
        };
        visualizeIslands(visualGrid);
        visualizeSearchProcess(visualGrid);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(10, 10, 0.3);
        comparePerformance(50, 50, 0.4);
        comparePerformance(100, 100, 0.3);
    }
    
    private static void testCase(String name, char[][] grid, int expected) {
        System.out.printf("\n测试用例: %s\n", name);
        
        if (grid.length == 0) {
            System.out.println("空网格");
        } else {
            System.out.println("网格:");
            printGrid(grid);
        }
        
        char[][] grid1 = cloneGrid(grid);
        char[][] grid2 = cloneGrid(grid);
        char[][] grid3 = cloneGrid(grid);
        char[][] grid4 = cloneGrid(grid);
        
        int result1 = numIslandsDFS(grid1);
        int result2 = numIslandsBFS(grid2);
        int result3 = numIslandsUnionFind(grid3);
        int result4 = numIslandsFloodFill(grid4);
        
        System.out.println("DFS结果: " + result1);
        System.out.println("BFS结果: " + result2);
        System.out.println("并查集结果: " + result3);
        System.out.println("洪水填充结果: " + result4);
        
        boolean passed = (result1 == expected) && (result2 == expected) && 
                        (result3 == expected) && (result4 == expected);
        System.out.println("测试结果: " + (passed ? "通过" : "失败"));
        
        if (!passed) {
            System.out.println("预期结果: " + expected);
        }
        
        // 可视化小规模案例
        if (grid.length > 0 && grid.length <= 8 && grid[0].length <= 8) {
            visualizeIslands(cloneGrid(grid));
        }
    }
    
    private static void printElevationMap(int[][] map) {
        for (int[] row : map) {
            for (int cell : row) {
                System.out.printf("%2d ", cell);
            }
            System.out.println();
        }
    }
    
    private static void printBinaryImage(int[][] image) {
        for (int[] row : image) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}