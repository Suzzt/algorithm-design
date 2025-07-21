package main.java.org.dao.qa;

import java.util.*;

/**
 * 省份数量问题
 * 
 * <p><b>问题描述</b>:
 * 给定一个n x n的矩阵isConnected，其中isConnected[i][j] = 1表示第i个城市和第j个城市直接相连，0表示没有直接相连。
 * 省份是一组直接或间接相连的城市，组内不含其他没有相连的城市。
 * 求矩阵中省份的总数。
 * 
 * <p><b>示例</b>:
 * 输入: isConnected = [[1,1,0],
 *                     [1,1,0],
 *                     [0,0,1]]
 * 输出: 2
 * 解释: 城市0和1相连，形成第一个省份；城市2独自形成第二个省份。
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第547题)
 * 
 * <p><b>解题思路</b>:
 * 1. 并查集(Union-Find): 高效处理连通性问题
 * 2. 深度优先搜索(DFS): 通过递归或栈实现图遍历
 * 3. 广度优先搜索(BFS): 通过队列实现图遍历
 * 
 * <p><b>时间复杂度</b>:
 *  并查集: O(n²) (使用路径压缩和按秩合并优化后接近O(n))
 *  DFS/BFS: O(n²)
 * 
 * <p><b>空间复杂度</b>:
 *  并查集: O(n)
 *  DFS/BFS: O(n)
 * 
 * <p><b>应用场景</b>:
 * 1. 社交网络中的朋友圈分析
 * 2. 计算机网络中的集群检测
 * 3. 电商平台的关联用户识别
 * 4. 交通网络中的连通区域分析
 * 5. 图像处理中的连通像素区域
 */

public class NumberOfProvinces {
    
    // ========================= 解法1: 并查集 =========================
    
    /**
     * 并查集解法
     * 
     * @param isConnected 城市连接矩阵
     * @return 省份数量
     */
    public static int findCircleNumUnionFind(int[][] isConnected) {
        int n = isConnected.length;
        UnionFind uf = new UnionFind(n);
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    uf.union(i, j);
                }
            }
        }
        
        return uf.getCount();
    }
    
    // 并查集数据结构
    static class UnionFind {
        private int[] parent;
        private int[] rank;
        private int count;
        
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            count = n;
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // 路径压缩
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX == rootY) return;
            
            // 按秩合并
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            count--;
        }
        
        public int getCount() {
            return count;
        }
    }
    
    // ========================= 解法2: 深度优先搜索 =========================
    
    /**
     * DFS解法
     * 
     * @param isConnected 城市连接矩阵
     * @return 省份数量
     */
    public static int findCircleNumDFS(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int count = 0;
        
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(isConnected, visited, i);
                count++;
            }
        }
        
        return count;
    }
    
    private static void dfs(int[][] isConnected, boolean[] visited, int i) {
        visited[i] = true;
        for (int j = 0; j < isConnected.length; j++) {
            if (isConnected[i][j] == 1 && !visited[j]) {
                dfs(isConnected, visited, j);
            }
        }
    }
    
    // ========================= 解法3: 广度优先搜索 =========================
    
    /**
     * BFS解法
     * 
     * @param isConnected 城市连接矩阵
     * @return 省份数量
     */
    public static int findCircleNumBFS(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int count = 0;
        
        Queue<Integer> queue = new LinkedList<>();
        
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                queue.offer(i);
                visited[i] = true;
                count++;
                
                while (!queue.isEmpty()) {
                    int city = queue.poll();
                    for (int j = 0; j < n; j++) {
                        if (isConnected[city][j] == 1 && !visited[j]) {
                            queue.offer(j);
                            visited[j] = true;
                        }
                    }
                }
            }
        }
        
        return count;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 社交网络分析 - 找出朋友圈数量
     * 
     * @param relationships 用户关系矩阵
     * @return 朋友圈数量
     */
    public static int findFriendCircles(int[][] relationships) {
        return findCircleNumUnionFind(relationships);
    }
    
    /**
     * 网络集群分析 - 检测服务器集群数量
     * 
     * @param connections 服务器连接矩阵
     * @return 独立集群数量
     */
    public static int countServerClusters(int[][] connections) {
        return findCircleNumDFS(connections);
    }
    
    /**
     * 电子商务关联用户识别
     * 
     * @param userBehaviors 用户行为相似度矩阵
     * @return 用户群组数量
     */
    public static int identifyUserGroups(double[][] userBehaviors) {
        // 将相似度矩阵转换为二进制连接矩阵
        int n = userBehaviors.length;
        int[][] connections = new int[n][n];
        double threshold = 0.7; // 相似度阈值
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                connections[i][j] = (userBehaviors[i][j] > threshold) ? 1 : 0;
            }
        }
        
        return findCircleNumBFS(connections);
    }
    
    /**
     * 交通网络规划 - 计算城市集群
     * 
     * @param roadNetwork 城市道路连接矩阵
     * @return 城市集群数量
     */
    public static int countCityClusters(int[][] roadNetwork) {
        return findCircleNumUnionFind(roadNetwork);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化城市连接图
     * 
     * @param isConnected 连接矩阵
     * @param provinces 省份分组结果
     */
    public static void visualizeCities(int[][] isConnected, List<List<Integer>> provinces) {
        int n = isConnected.length;
        System.out.println("\n城市连接图:");
        
        // 打印城市连接矩阵
        System.out.println("连接矩阵:");
        System.out.print("   ");
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d ", i);
        }

        for (int i = 0; i < n; i++) {
            System.out.printf("%d | ", i);
            for (int j = 0; j < n; j++) {
                System.out.printf("%2d ", isConnected[i][j]);
            }
            System.out.println();
        }
        
        // 打印省份分组
        System.out.println("\n省份分组:");
        for (int i = 0; i < provinces.size(); i++) {
            System.out.printf("省份 %d: ", i + 1);
            for (int city : provinces.get(i)) {
                System.out.printf("%d ", city);
            }
            System.out.println();
        }
        
        // 可视化连通性
        System.out.println("\n连通性结构:");
        visualizeProvinces(isConnected, provinces);
    }
    
    // 可视化省份连通关系
    private static void visualizeProvinces(int[][] isConnected, List<List<Integer>> provinces) {
        for (List<Integer> group : provinces) {
            if (group.size() == 1) {
                System.out.printf("孤立城市: %d\n", group.get(0));
            } else {
                System.out.printf("省份: [%d]", group.get(0));
                for (int i = 1; i < group.size(); i++) {
                    System.out.printf(" ↔ [%d]", group.get(i));
                }
                System.out.println();
            }
        }
    }
    
    /**
     * 获取省份分组详情
     * 
     * @param isConnected 连接矩阵
     * @return 省份分组列表
     */
    public static List<List<Integer>> getProvincesGroup(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        List<List<Integer>> provinces = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                List<Integer> group = new ArrayList<>();
                dfsGroup(isConnected, visited, i, group);
                provinces.add(group);
            }
        }
        
        return provinces;
    }
    
    private static void dfsGroup(int[][] isConnected, boolean[] visited, int i, List<Integer> group) {
        visited[i] = true;
        group.add(i);
        for (int j = 0; j < isConnected.length; j++) {
            if (isConnected[i][j] == 1 && !visited[j]) {
                dfsGroup(isConnected, visited, j, group);
            }
        }
    }
    
    // ========================= 性能比较 =========================
    
    /**
     * 比较不同算法的性能
     * 
     * @param size 测试矩阵大小
     */
    public static void compareAlgorithms(int size) {
        // 生成随机城市连接矩阵
        int[][] matrix = generateConnectionMatrix(size);
        
        // 测试并查集
        long start = System.nanoTime();
        int ufResult = findCircleNumUnionFind(matrix);
        long ufTime = System.nanoTime() - start;
        
        // 测试DFS
        start = System.nanoTime();
        int dfsResult = findCircleNumDFS(matrix);
        long dfsTime = System.nanoTime() - start;
        
        // 测试BFS
        start = System.nanoTime();
        int bfsResult = findCircleNumBFS(matrix);
        long bfsTime = System.nanoTime() - start;
        
        System.out.printf("%n性能比较 (矩阵大小: %d x %d):%n", size, size);
        System.out.println("============================================");
        System.out.printf("算法       | 结果 | 耗时(微秒)%n");
        System.out.println("-----------+------+-------------");
        System.out.printf("并查集     | %4d | %11d%n", ufResult, ufTime / 1000);
        System.out.printf("深度优先搜索 | %4d | %11d%n", dfsResult, dfsTime / 1000);
        System.out.printf("广度优先搜索 | %4d | %11d%n", bfsResult, bfsTime / 1000);
        System.out.println("============================================");
    }
    
    // 生成随机连接矩阵
    private static int[][] generateConnectionMatrix(int n) {
        int[][] matrix = new int[n][n];
        Random random = new Random();
        
        // 确保对角线为1（每个城市与自身相连）
        for (int i = 0; i < n; i++) {
            matrix[i][i] = 1;
        }
        
        // 随机生成连接关系
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (random.nextDouble() > 0.7) { // 30%的概率相连
                    matrix[i][j] = 1;
                    matrix[j][i] = 1;
                }
            }
        }
        
        return matrix;
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        // 基础测试用例
        int[][] testCase1 = {
            {1, 1, 0},
            {1, 1, 0},
            {0, 0, 1}
        };
        
        int[][] testCase2 = {
            {1, 0, 0, 1},
            {0, 1, 1, 0},
            {0, 1, 1, 1},
            {1, 0, 1, 1}
        };
        
        // 大型测试矩阵
        int[][] largeMatrix = generateConnectionMatrix(50);
        
        testAlgorithms("基础测试1", testCase1, 2);
        testAlgorithms("基础测试2", testCase2, 1);
        testAlgorithms("大型矩阵测试", largeMatrix, -1); // -1表示不验证结果
        
        // 性能比较
        compareAlgorithms(100);
        compareAlgorithms(1000);
        
        // 应用场景测试
        testApplicationScenarios();
    }
    
    private static void testAlgorithms(String name, int[][] matrix, int expected) {
        System.out.println("\n====== " + name + " ======");
        
        List<List<Integer>> groups = getProvincesGroup(matrix);
        visualizeCities(matrix, groups);
        
        int ufResult = findCircleNumUnionFind(matrix);
        int dfsResult = findCircleNumDFS(matrix);
        int bfsResult = findCircleNumBFS(matrix);
        
        System.out.println("\n算法结果:");
        System.out.printf("并查集: %d\n", ufResult);
        System.out.printf("深度优先搜索: %d\n", dfsResult);
        System.out.printf("广度优先搜索: %d\n", bfsResult);
        
        if (expected != -1 && ufResult != expected) {
            System.err.println("错误: 结果不符合预期!");
        }
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 社交网络朋友圈
        int[][] socialNetwork = {
            {1, 1, 0, 0, 0},
            {1, 1, 0, 0, 0},
            {0, 0, 1, 1, 1},
            {0, 0, 1, 1, 0},
            {0, 0, 1, 0, 1}
        };
        int circles = findFriendCircles(socialNetwork);
        System.out.println("社交网络朋友圈数量: " + circles);
        
        // 场景2: 服务器集群
        int[][] serverConnections = {
            {1, 0, 1, 0},
            {0, 1, 0, 1},
            {1, 0, 1, 0},
            {0, 1, 0, 1}
        };
        int clusters = countServerClusters(serverConnections);
        System.out.println("服务器集群数量: " + clusters);
    }
}