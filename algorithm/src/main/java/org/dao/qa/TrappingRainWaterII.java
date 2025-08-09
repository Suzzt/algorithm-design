package main.java.org.dao.qa;

import java.util.*;

/**
 * 接雨水 II 问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个 m x n 的二维高度图，计算下雨后能够陷住多少雨水。
 * 雨水会从边界流出，只有被四周包围且高度较低的区域才能存水。
 * 
 * <p><b>示例</b>:
 * 输入: heightMap = [[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]]
 * 输出: 4
 * 解释: 下雨后，内部低洼区域可以存储雨水
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第407题)
 * 
 * <p><b>解题思路</b>:
 * 1. 优先队列法: 从边界开始，用最小堆维护当前最低边界点
 * 2. Dijkstra算法: 转化为求每个点到边界的最短"高度路径"
 * 3. 并查集法: 按高度排序后用并查集维护连通性
 * 
 * <p><b>时间复杂度</b>:
 *  优先队列法: O(mn log(mn))
 *  Dijkstra算法: O(mn log(mn))
 *  并查集法: O(mn log(mn))
 * 
 * <p><b>空间复杂度</b>:
 *  优先队列法: O(mn)
 *  Dijkstra算法: O(mn)
 *  并查集法: O(mn)
 * 
 * <p><b>应用场景</b>:
 * 1. 地形分析和水文建模
 * 2. 城市排水系统设计
 * 3. 游戏地形生成算法
 * 4. 图像处理中的区域填充
 * 5. 三维建模中的体积计算
 */

public class TrappingRainWaterII {
    
    // 四个方向：上右下左
    private static final int[][] DIRECTIONS = {{-1,0}, {0,1}, {1,0}, {0,-1}};
    
    // ========================= 解法1: 优先队列法 (推荐) =========================
    
    /**
     * 优先队列解法
     * 
     * @param heightMap 二维高度图
     * @return 能陷住的雨水总量
     */
    public static int trapRainWaterPriorityQueue(int[][] heightMap) {
        if (heightMap == null || heightMap.length == 0 || heightMap[0].length == 0) {
            return 0;
        }
        
        int m = heightMap.length;
        int n = heightMap[0].length;
        
        if (m <= 2 || n <= 2) {
            return 0; // 边界太小，无法存水
        }
        
        // 最小堆，存储边界点
        PriorityQueue<Cell> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.height, b.height));
        boolean[][] visited = new boolean[m][n];
        
        // 将边界点加入优先队列
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (isBoundary(i, j, m, n)) {
                    pq.offer(new Cell(i, j, heightMap[i][j]));
                    visited[i][j] = true;
                }
            }
        }
        
        int waterTrapped = 0;
        
        while (!pq.isEmpty()) {
            Cell current = pq.poll();
            
            // 检查四个方向的邻居
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];
                
                if (isValidCell(newRow, newCol, m, n) && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    
                    // 计算当前位置能存储的水量
                    int cellHeight = heightMap[newRow][newCol];
                    int waterLevel = Math.max(current.height, cellHeight);
                    waterTrapped += Math.max(0, waterLevel - cellHeight);
                    
                    pq.offer(new Cell(newRow, newCol, waterLevel));
                }
            }
        }
        
        return waterTrapped;
    }
    
    // 单元格类
    static class Cell {
        int row, col, height;
        
        Cell(int row, int col, int height) {
            this.row = row;
            this.col = col;
            this.height = height;
        }
        
        @Override
        public String toString() {
            return String.format("(%d,%d,h=%d)", row, col, height);
        }
    }
    
    // 检查是否为边界点
    private static boolean isBoundary(int row, int col, int m, int n) {
        return row == 0 || row == m - 1 || col == 0 || col == n - 1;
    }
    
    // 检查坐标是否有效
    private static boolean isValidCell(int row, int col, int m, int n) {
        return row >= 0 && row < m && col >= 0 && col < n;
    }
    
    // ========================= 解法2: Dijkstra算法 =========================
    
    /**
     * Dijkstra算法解法 - 求每个点到边界的最短"高度路径"
     * 
     * @param heightMap 二维高度图
     * @return 能陷住的雨水总量
     */
    public static int trapRainWaterDijkstra(int[][] heightMap) {
        if (heightMap == null || heightMap.length == 0 || heightMap[0].length == 0) {
            return 0;
        }
        
        int m = heightMap.length;
        int n = heightMap[0].length;
        
        if (m <= 2 || n <= 2) {
            return 0;
        }
        
        // 距离数组，存储每个点到边界的最小"高度距离"
        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        
        PriorityQueue<Cell> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.height, b.height));
        
        // 初始化边界点
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (isBoundary(i, j, m, n)) {
                    dist[i][j] = heightMap[i][j];
                    pq.offer(new Cell(i, j, heightMap[i][j]));
                }
            }
        }
        
        // Dijkstra算法
        while (!pq.isEmpty()) {
            Cell current = pq.poll();
            
            if (current.height > dist[current.row][current.col]) {
                continue;
            }
            
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];
                
                if (isValidCell(newRow, newCol, m, n)) {
                    // 新的"高度距离"是当前距离和新位置高度的最大值
                    int newDist = Math.max(dist[current.row][current.col], heightMap[newRow][newCol]);
                    
                    if (newDist < dist[newRow][newCol]) {
                        dist[newRow][newCol] = newDist;
                        pq.offer(new Cell(newRow, newCol, newDist));
                    }
                }
            }
        }
        
        // 计算总的雨水量
        int waterTrapped = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                waterTrapped += Math.max(0, dist[i][j] - heightMap[i][j]);
            }
        }
        
        return waterTrapped;
    }
    
    // ========================= 解法3: 并查集法 =========================
    
    /**
     * 并查集解法 - 按高度排序后处理连通性
     * 
     * @param heightMap 二维高度图
     * @return 能陷住的雨水总量
     */
    public static int trapRainWaterUnionFind(int[][] heightMap) {
        if (heightMap == null || heightMap.length == 0 || heightMap[0].length == 0) {
            return 0;
        }
        
        int m = heightMap.length;
        int n = heightMap[0].length;
        
        if (m <= 2 || n <= 2) {
            return 0;
        }
        
        // 创建所有位置的列表并按高度排序
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                positions.add(new Position(i, j, heightMap[i][j]));
            }
        }
        
        positions.sort((a, b) -> Integer.compare(a.height, b.height));
        
        UnionFind uf = new UnionFind(m * n);
        boolean[][] processed = new boolean[m][n];
        int[] waterLevel = new int[m * n]; // 每个连通分量的水位
        
        // 初始化边界点的水位
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (isBoundary(i, j, m, n)) {
                    waterLevel[i * n + j] = heightMap[i][j];
                } else {
                    waterLevel[i * n + j] = Integer.MAX_VALUE;
                }
            }
        }
        
        int waterTrapped = 0;
        
        // 按高度从低到高处理每个位置
        for (Position pos : positions) {
            int row = pos.row;
            int col = pos.col;
            int height = pos.height;
            
            processed[row][col] = true;
            int currentIndex = row * n + col;
            
            // 检查相邻的已处理位置
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (isValidCell(newRow, newCol, m, n) && processed[newRow][newCol]) {
                    int neighborIndex = newRow * n + newCol;
                    
                    // 合并连通分量
                    int root1 = uf.find(currentIndex);
                    int root2 = uf.find(neighborIndex);
                    
                    if (root1 != root2) {
                        uf.union(currentIndex, neighborIndex);
                        int newRoot = uf.find(currentIndex);
                        // 更新连通分量的水位（取较小值，因为水会流向较低处）
                        waterLevel[newRoot] = Math.min(waterLevel[root1], waterLevel[root2]);
                    }
                }
            }
            
            // 计算当前位置的蓄水量
            int root = uf.find(currentIndex);
            int currentWaterLevel = Math.max(waterLevel[root], height);
            waterLevel[root] = currentWaterLevel;
            waterTrapped += Math.max(0, currentWaterLevel - height);
        }
        
        return waterTrapped;
    }
    
    // 位置类
    static class Position {
        int row, col, height;
        
        Position(int row, int col, int height) {
            this.row = row;
            this.col = col;
            this.height = height;
        }
    }
    
    // 并查集类
    static class UnionFind {
        int[] parent;
        int[] rank;
        
        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 地形分析 - 计算流域面积和蓄水能力
     * 
     * @param elevation 地形高程数据
     * @return 流域分析结果
     */
    public static WatershedAnalysis analyzeWatershed(int[][] elevation) {
        if (elevation == null || elevation.length == 0) {
            return new WatershedAnalysis(0, 0, new ArrayList<>());
        }
        
        int m = elevation.length;
        int n = elevation[0].length;
        
        // 计算总蓄水量
        int totalWater = trapRainWaterPriorityQueue(elevation);
        
        // 识别蓄水区域
        List<WaterPool> waterPools = identifyWaterPools(elevation);
        
        // 计算流域面积（能够蓄水的区域）
        int watershedArea = 0;
        for (WaterPool pool : waterPools) {
            watershedArea += pool.area;
        }
        
        return new WatershedAnalysis(totalWater, watershedArea, waterPools);
    }
    
    // 识别蓄水池
    private static List<WaterPool> identifyWaterPools(int[][] elevation) {
        int m = elevation.length;
        int n = elevation[0].length;
        
        // 计算每个点的水位
        int[][] waterLevel = calculateWaterLevel(elevation);
        boolean[][] visited = new boolean[m][n];
        List<WaterPool> pools = new ArrayList<>();
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (!visited[i][j] && waterLevel[i][j] > elevation[i][j]) {
                    WaterPool pool = explorePool(elevation, waterLevel, visited, i, j);
                    if (pool.area > 0) {
                        pools.add(pool);
                    }
                }
            }
        }
        
        return pools;
    }
    
    // 计算水位分布
    private static int[][] calculateWaterLevel(int[][] elevation) {
        int m = elevation.length;
        int n = elevation[0].length;
        int[][] waterLevel = new int[m][n];
        
        for (int i = 0; i < m; i++) {
            Arrays.fill(waterLevel[i], Integer.MAX_VALUE);
        }
        
        PriorityQueue<Cell> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.height, b.height));
        
        // 初始化边界
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (isBoundary(i, j, m, n)) {
                    waterLevel[i][j] = elevation[i][j];
                    pq.offer(new Cell(i, j, elevation[i][j]));
                }
            }
        }
        
        // 计算内部点的水位
        while (!pq.isEmpty()) {
            Cell current = pq.poll();
            
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];
                
                if (isValidCell(newRow, newCol, m, n)) {
                    int newLevel = Math.max(waterLevel[current.row][current.col], elevation[newRow][newCol]);
                    if (newLevel < waterLevel[newRow][newCol]) {
                        waterLevel[newRow][newCol] = newLevel;
                        pq.offer(new Cell(newRow, newCol, newLevel));
                    }
                }
            }
        }
        
        return waterLevel;
    }
    
    // 探索蓄水池
    private static WaterPool explorePool(int[][] elevation, int[][] waterLevel, 
                                         boolean[][] visited, int startRow, int startCol) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        
        int area = 0;
        int totalWater = 0;
        int minElevation = elevation[startRow][startCol];
        int maxElevation = elevation[startRow][startCol];
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            
            area++;
            totalWater += waterLevel[row][col] - elevation[row][col];
            minElevation = Math.min(minElevation, elevation[row][col]);
            maxElevation = Math.max(maxElevation, elevation[row][col]);
            
            for (int[] dir : DIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                if (isValidCell(newRow, newCol, elevation.length, elevation[0].length) 
                    && !visited[newRow][newCol] 
                    && waterLevel[newRow][newCol] > elevation[newRow][newCol]) {
                    
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol});
                }
            }
        }
        
        return new WaterPool(area, totalWater, minElevation, maxElevation);
    }
    
    // 流域分析结果类
    static class WatershedAnalysis {
        int totalWaterCapacity;
        int watershedArea;
        List<WaterPool> waterPools;
        
        WatershedAnalysis(int totalWater, int area, List<WaterPool> pools) {
            this.totalWaterCapacity = totalWater;
            this.watershedArea = area;
            this.waterPools = pools;
        }
        
        @Override
        public String toString() {
            return String.format("流域分析 - 总蓄水量: %d, 流域面积: %d, 蓄水池数量: %d", 
                               totalWaterCapacity, watershedArea, waterPools.size());
        }
    }
    
    // 蓄水池类
    static class WaterPool {
        int area;
        int waterVolume;
        int minElevation;
        int maxElevation;
        
        WaterPool(int area, int water, int minElev, int maxElev) {
            this.area = area;
            this.waterVolume = water;
            this.minElevation = minElev;
            this.maxElevation = maxElev;
        }
        
        @Override
        public String toString() {
            return String.format("蓄水池[面积:%d, 蓄水量:%d, 高程范围:%d-%d]", 
                               area, waterVolume, minElevation, maxElevation);
        }
    }
    
    /**
     * 城市排水系统设计
     * 
     * @param cityElevation 城市地形高程
     * @param drainageCapacity 排水系统容量
     * @return 排水系统效果评估
     */
    public static DrainageSystem designDrainageSystem(int[][] cityElevation, int drainageCapacity) {
        WatershedAnalysis analysis = analyzeWatershed(cityElevation);
        
        boolean isAdequate = analysis.totalWaterCapacity <= drainageCapacity;
        int deficit = Math.max(0, analysis.totalWaterCapacity - drainageCapacity);
        
        // 识别需要重点关注的积水区域
        List<WaterPool> criticalAreas = new ArrayList<>();
        for (WaterPool pool : analysis.waterPools) {
            if (pool.waterVolume > drainageCapacity * 0.1) { // 超过总容量10%的区域
                criticalAreas.add(pool);
            }
        }
        
        return new DrainageSystem(drainageCapacity, analysis.totalWaterCapacity, 
                                  isAdequate, deficit, criticalAreas);
    }
    
    // 排水系统类
    static class DrainageSystem {
        int capacity;
        int requiredCapacity;
        boolean isAdequate;
        int deficit;
        List<WaterPool> criticalAreas;
        
        DrainageSystem(int capacity, int required, boolean adequate, int deficit, List<WaterPool> critical) {
            this.capacity = capacity;
            this.requiredCapacity = required;
            this.isAdequate = adequate;
            this.deficit = deficit;
            this.criticalAreas = critical;
        }
        
        @Override
        public String toString() {
            return String.format("排水系统 - 容量:%d, 需求:%d, 充足:%s, 缺口:%d, 关键区域:%d个", 
                               capacity, requiredCapacity, isAdequate ? "是" : "否", 
                               deficit, criticalAreas.size());
        }
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化二维地形和积水情况
     * 
     * @param heightMap 高度图
     */
    public static void visualizeWaterTrapping(int[][] heightMap) {
        if (heightMap == null || heightMap.length == 0) {
            System.out.println("空地形图");
            return;
        }
        
        int m = heightMap.length;
        int n = heightMap[0].length;
        
        System.out.println("\n二维接雨水可视化:");
        System.out.println("原始地形:");
        printHeightMap(heightMap);
        
        // 计算水位
        int[][] waterLevel = calculateWaterLevel(heightMap);
        
        System.out.println("\n水位分布:");
        printHeightMap(waterLevel);
        
        System.out.println("\n积水情况 (W=有水, .=无水):");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (waterLevel[i][j] > heightMap[i][j]) {
                    System.out.print("W ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
        
        // 统计信息
        int totalWater = trapRainWaterPriorityQueue(heightMap);
        System.out.printf("\n总积水量: %d\n", totalWater);
        
        WatershedAnalysis analysis = analyzeWatershed(heightMap);
        System.out.println(analysis);
        for (WaterPool pool : analysis.waterPools) {
            System.out.println("  " + pool);
        }
    }
    
    /**
     * 打印高度图
     * 
     * @param heightMap 高度图
     */
    private static void printHeightMap(int[][] heightMap) {
        for (int[] row : heightMap) {
            for (int height : row) {
                System.out.printf("%3d ", height == Integer.MAX_VALUE ? 999 : height);
            }
            System.out.println();
        }
    }
    
    /**
     * 算法过程可视化
     * 
     * @param heightMap 高度图
     */
    public static void visualizeAlgorithmProcess(int[][] heightMap) {
        System.out.println("\n优先队列算法过程可视化:");
        
        int m = heightMap.length;
        int n = heightMap[0].length;
        
        PriorityQueue<Cell> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.height, b.height));
        boolean[][] visited = new boolean[m][n];
        
        // 初始化边界
        System.out.println("步骤1: 初始化边界点");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (isBoundary(i, j, m, n)) {
                    pq.offer(new Cell(i, j, heightMap[i][j]));
                    visited[i][j] = true;
                    System.out.printf("  边界点: (%d,%d) 高度=%d\n", i, j, heightMap[i][j]);
                }
            }
        }
        
        System.out.println("\n步骤2: 从最低边界开始扩展");
        int step = 1;
        int totalWater = 0;
        
        while (!pq.isEmpty() && step <= 10) { // 限制显示步数
            Cell current = pq.poll();
            System.out.printf("\n第%d步: 处理点(%d,%d) 高度=%d\n", 
                             step, current.row, current.col, current.height);
            
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];
                
                if (isValidCell(newRow, newCol, m, n) && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    
                    int cellHeight = heightMap[newRow][newCol];
                    int waterLevel = Math.max(current.height, cellHeight);
                    int water = Math.max(0, waterLevel - cellHeight);
                    totalWater += water;
                    
                    System.out.printf("  邻居(%d,%d): 地面高度=%d, 水位=%d, 积水=%d\n", 
                                     newRow, newCol, cellHeight, waterLevel, water);
                    
                    pq.offer(new Cell(newRow, newCol, waterLevel));
                }
            }
            step++;
        }
        
        if (!pq.isEmpty()) {
            System.out.println("... (省略剩余步骤)");
        }
        
        System.out.printf("\n当前总积水量: %d\n", totalWater);
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param rows 行数
     * @param cols 列数
     */
    public static void comparePerformance(int rows, int cols) {
        // 生成测试数据
        int[][] heightMap = generateRandomHeightMap(rows, cols, 20);
        
        long start, end;
        
        // 测试优先队列法
        start = System.nanoTime();
        int result1 = trapRainWaterPriorityQueue(copyArray(heightMap));
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试Dijkstra算法
        start = System.nanoTime();
        int result2 = trapRainWaterDijkstra(copyArray(heightMap));
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试并查集法
        start = System.nanoTime();
        int result3 = trapRainWaterUnionFind(copyArray(heightMap));
        end = System.nanoTime();
        long time3 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("地形大小: %dx%d = %d个点\n", rows, cols, rows * cols);
        System.out.println("=======================================");
        System.out.println("方法            | 时间(ms)   | 结果  | 相对速度");
        System.out.println("----------------|------------|-------|----------");
        System.out.printf("优先队列法      | %.6f | %5d | 基准\n", time1 / 1_000_000.0, result1);
        System.out.printf("Dijkstra算法    | %.6f | %5d | %.2fx\n", time2 / 1_000_000.0, result2, (double)time2/time1);
        System.out.printf("并查集法        | %.6f | %5d | %.2fx\n", time3 / 1_000_000.0, result3, (double)time3/time1);
        System.out.println("=======================================");
        
        // 验证结果一致性
        if (result1 == result2 && result2 == result3) {
            System.out.println("✓ 所有算法结果一致");
        } else {
            System.out.println("✗ 算法结果不一致，请检查实现");
        }
    }
    
    // 生成随机高度图
    private static int[][] generateRandomHeightMap(int rows, int cols, int maxHeight) {
        Random rand = new Random();
        int[][] heightMap = new int[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                heightMap[i][j] = rand.nextInt(maxHeight) + 1;
            }
        }
        
        return heightMap;
    }
    
    // 复制二维数组
    private static int[][] copyArray(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
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
        
        testCase("示例测试1", new int[][]{{1,4,3,1,3,2},{3,2,1,3,2,4},{2,3,3,2,3,1}}, 4);
        testCase("示例测试2", new int[][]{{3,3,3,3,3},{3,2,2,2,3},{3,2,1,2,3},{3,2,2,2,3},{3,3,3,3,3}}, 10);
        testCase("山峰测试", new int[][]{{1,2,3,2,1},{2,3,4,3,2},{3,4,5,4,3},{2,3,4,3,2},{1,2,3,2,1}}, 0);
        testCase("盆地测试", new int[][]{{5,5,5,5,5},{5,1,1,1,5},{5,1,0,1,5},{5,1,1,1,5},{5,5,5,5,5}}, 16);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        testCase("空数组", new int[][]{}, 0);
        testCase("单行", new int[][]{{1,2,3,4,5}}, 0);
        testCase("单列", new int[][]{{1},{2},{3},{4},{5}}, 0);
        testCase("2x2数组", new int[][]{{1,2},{3,4}}, 0);
        testCase("平坦地形", new int[][]{{5,5,5},{5,5,5},{5,5,5}}, 0);
        testCase("阶梯地形", new int[][]{{1,2,3},{4,5,6},{7,8,9}}, 0);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 地形分析
        System.out.println("\n地形分析:");
        int[][] terrain = {{5,4,3,4,5},{4,2,1,2,4},{3,1,0,1,3},{4,2,1,2,4},{5,4,3,4,5}};
        System.out.println("地形数据:");
        printHeightMap(terrain);
        
        WatershedAnalysis analysis = analyzeWatershed(terrain);
        System.out.println(analysis);
        for (WaterPool pool : analysis.waterPools) {
            System.out.println("  " + pool);
        }
        
        // 场景2: 城市排水系统
        System.out.println("\n城市排水系统设计:");
        int[][] cityTerrain = {{10,9,8,9,10},{9,7,6,7,9},{8,6,4,6,8},{9,7,6,7,9},{10,9,8,9,10}};
        DrainageSystem drainage = designDrainageSystem(cityTerrain, 15);
        System.out.println(drainage);
        for (WaterPool area : drainage.criticalAreas) {
            System.out.println("  关键积水区: " + area);
        }
        
        // 场景3: 可视化演示
        System.out.println("\n可视化演示:");
        int[][] demo = {{3,3,3,3,3},{3,2,2,2,3},{3,2,1,2,3},{3,2,2,2,3},{3,3,3,3,3}};
        visualizeWaterTrapping(demo);
        visualizeAlgorithmProcess(demo);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(10, 10);
        comparePerformance(20, 20);
        comparePerformance(50, 50);
    }
    
    private static void testCase(String name, int[][] heightMap, int expected) {
        System.out.printf("\n测试用例: %s\n", name);
        
        if (heightMap.length > 0) {
            System.out.println("地形:");
            printHeightMap(heightMap);
        }
        
        int pqResult = trapRainWaterPriorityQueue(heightMap);
        int dijkstraResult = trapRainWaterDijkstra(heightMap);
        int ufResult = trapRainWaterUnionFind(heightMap);
        
        System.out.printf("优先队列法结果: %d\n", pqResult);
        System.out.printf("Dijkstra法结果: %d\n", dijkstraResult);
        System.out.printf("并查集法结果: %d\n", ufResult);
        System.out.printf("期望结果: %d\n", expected);
        
        boolean isCorrect = (pqResult == expected) && (dijkstraResult == expected) && (ufResult == expected);
        System.out.println("测试结果: " + (isCorrect ? "通过" : "失败"));
        
        // 小规模案例的可视化
        if (heightMap.length > 0 && heightMap.length <= 6 && heightMap[0].length <= 6) {
            visualizeWaterTrapping(heightMap);
        }
    }
}