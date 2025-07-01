package main.java.org.dao.qa;

import java.util.*;
import java.util.stream.*;

public class UnifiedLISProblems {
    public static void main(String[] args) {
        testProblem1();
        testProblem2();
        testProblem3();
        testProblem4();
        testProblem5();
    }

    // 问题1：非连续数字串的最长递增子序列
    static String stringLIS(String s) {
        if (s == null || s.isEmpty()) return "";
        int[] nums = s.chars().map(c -> c - '0').toArray();
        int n = nums.length;
        int[] tails = new int[n], indices = new int[n], prev = new int[n];
        Arrays.fill(prev, -1);
        int len = 0;
        
        for (int i = 0; i < n; i++) {
            int left = 0, right = len;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < nums[i]) left = mid + 1;
                else right = mid;
            }
            tails[left] = nums[i];
            indices[left] = i;
            if (left > 0) prev[i] = indices[left - 1];
            if (left == len) len++;
        }
        
        StringBuilder sb = new StringBuilder();
        for (int cur = indices[len - 1]; cur != -1; cur = prev[cur]) {
            sb.insert(0, nums[cur]);
        }
        return sb.toString();
    }

    // 问题2：二维网格最长递增路径
    static int gridLIS(int[][] matrix) {
        if (matrix.length == 0) return 0;
        int m = matrix.length, n = matrix[0].length, max = 1;
        int[][] memo = new int[m][n];
        int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                max = Math.max(max, gridDfs(matrix, i, j, memo, dirs));
            }
        }
        return max;
    }
    
    private static int gridDfs(int[][] matrix, int i, int j, int[][] memo, int[][] dirs) {
        if (memo[i][j] != 0) return memo[i][j];
        int max = 1;
        for (int[] d : dirs) {
            int x = i + d[0], y = j + d[1];
            if (x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length && 
                matrix[x][y] > matrix[i][j]) {
                max = Math.max(max, 1 + gridDfs(matrix, x, y, memo, dirs));
            }
        }
        memo[i][j] = max;
        return max;
    }

    // 问题3：产品版本依赖分析
    static class Version {
        String id;
        List<String> deps;
        Version(String id, List<String> deps) { this.id = id; this.deps = deps; }
    }
    
    static int versionChain(List<Version> versions) {
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> memo = new HashMap<>();
        for (Version v : versions) graph.put(v.id, v.deps);
        return graph.keySet().stream().map(id -> versionDfs(graph, memo, id)).max(Integer::compare).orElse(0);
    }
    
    private static int versionDfs(Map<String, List<String>> graph, Map<String, Integer> memo, String ver) {
        if (memo.containsKey(ver)) return memo.get(ver);
        int max = 1;
        for (String dep : graph.getOrDefault(ver, Collections.emptyList())) {
            max = Math.max(max, 1 + versionDfs(graph, memo, dep));
        }
        memo.put(ver, max);
        return max;
    }

    // 问题4：动态时间序列分割
    static int temperatureRise(int[] temps) {
        if (temps.length == 0) return 0;
        int max = 1, curr = 1;
        for (int i = 1; i < temps.length; i++) {
            curr = temps[i] > temps[i-1] ? curr + 1 : 1;
            max = Math.max(max, curr);
        }
        return max;
    }

    // 问题5：音乐旋律相似度检测
    static int melodySimilarity(int[] a, int[] b) {
        int m = a.length, n = b.length, max = 0;
        int[] dp = new int[n+1];
        Arrays.fill(dp, 0);
        
        for (int i = 0; i < m; i++) {
            int curr = 0;
            for (int j = 0; j < n; j++) {
                int prev = dp[j];
                if (a[i] == b[j]) {
                    dp[j] = Math.max(dp[j], curr + 1);
                    max = Math.max(max, dp[j]);
                }
                if (a[i] > b[j] && prev > curr) curr = prev;
            }
        }
        return max;
    }

    // 测试方法
    static void testProblem1() {
        System.out.println("\n=== 问题1测试 ===");
        System.out.println("输入: \"12134\" → 输出: " + stringLIS("12134"));      // 1234
        System.out.println("输入: \"987654\" → 输出: " + stringLIS("987654"));    // 9
        System.out.println("输入: \"192837465\" → 输出: " + stringLIS("192837465")); // 12345
    }

    static void testProblem2() {
        System.out.println("\n=== 问题2测试 ===");
        int[][] grid = {{9,9,4}, {6,6,8}, {2,1,1}};
        System.out.println("网格: \n" + Arrays.deepToString(grid));
        System.out.println("最长路径: " + gridLIS(grid));  // 4
    }

    static void testProblem3() {
        System.out.println("\n=== 问题3测试 ===");
        List<Version> versions = Arrays.asList(
            new Version("A", Arrays.asList("B", "D")),
            new Version("B", Arrays.asList("C")),
            new Version("C", Collections.emptyList()),
            new Version("D", Arrays.asList("C"))
        );
        System.out.println("最长依赖链: " + versionChain(versions)); // 3
    }

    static void testProblem4() {
        System.out.println("\n=== 问题4测试 ===");
        int[] temps = {23, 24, 25, 23, 24, 25, 26};
        System.out.println("温度序列: " + Arrays.toString(temps));
        System.out.println("最长上升时段: " + temperatureRise(temps)); // 4
    }

    static void testProblem5() {
        System.out.println("\n=== 问题5测试 ===");
        int[] melodyA = {60, 62, 64, 65, 67};
        int[] melodyB = {62, 64, 65, 67, 69};
        System.out.println("旋律A: " + Arrays.toString(melodyA));
        System.out.println("旋律B: " + Arrays.toString(melodyB));
        System.out.println("最长公共递增序列: " + melodySimilarity(melodyA, melodyB)); // 4
    }
}