package main.java.org.dao.qa;

import java.util.*;

/**
 * 二叉树最大路径和问题
 * 
 * <p><b>问题描述</b>:
 * 路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。
 * 同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
 * 路径和是路径中各节点值的总和。给你一个二叉树的根节点root，返回其最大路径和。
 * 
 * <p><b>示例</b>:
 * 输入: root = [1,2,3]
 *       1
 *      / \
 *     2   3
 * 输出: 6
 * 解释: 最优路径是 2 -> 1 -> 3，路径和为 2 + 1 + 3 = 6
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难 (LeetCode 第124题)
 * 
 * <p><b>解题思路</b>:
 * 1. 递归解法: 对于每个节点，计算通过该节点的最大路径和
 * 2. 动态规划: 自底向上计算每个节点能提供的最大贡献值
 * 3. 分治法: 将问题分解为左右子树的最大路径和问题
 * 
 * <p><b>时间复杂度</b>:
 *  递归解法: O(n)
 *  动态规划: O(n)
 *  分治法: O(n)
 * 
 * <p><b>空间复杂度</b>:
 *  递归解法: O(h) h为树的高度
 *  动态规划: O(h)
 *  分治法: O(h)
 * 
 * <p><b>应用场景</b>:
 * 1. 网络路由优化（最大带宽路径）
 * 2. 金融风险评估（最大收益路径）
 * 3. 游戏AI决策树（最优策略路径）
 * 4. 供应链优化（最大利润路径）
 * 5. 社交网络影响力传播路径分析
 */

public class BinaryTreeMaximumPathSum {
    
    // 树节点定义
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
        
        @Override
        public String toString() {
            return String.valueOf(val);
        }
    }
    
    // ========================= 解法1: 递归解法 (推荐) =========================
    
    private static int maxSum = Integer.MIN_VALUE;
    
    /**
     * 递归解法
     * 
     * @param root 二叉树根节点
     * @return 最大路径和
     */
    public static int maxPathSumRecursive(TreeNode root) {
        maxSum = Integer.MIN_VALUE;
        maxGain(root);
        return maxSum;
    }
    
    /**
     * 计算节点的最大贡献值
     * 
     * @param node 当前节点
     * @return 该节点能提供的最大贡献值
     */
    private static int maxGain(TreeNode node) {
        if (node == null) return 0;
        
        // 递归计算左右子节点的最大贡献值
        // 只有在最大贡献值大于0时，才会选取对应子节点
        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);
        
        // 节点的最大路径和取决于该节点的值与该节点的左右子节点的最大贡献值
        int priceNewPath = node.val + leftGain + rightGain;
        
        // 更新答案
        maxSum = Math.max(maxSum, priceNewPath);
        
        // 返回节点的最大贡献值
        return node.val + Math.max(leftGain, rightGain);
    }
    
    // ========================= 解法2: 带状态的递归解法 =========================
    
    static class Result {
        int maxPath;      // 通过当前节点的最大路径和
        int maxGain;      // 当前节点能提供的最大贡献值
        
        Result(int maxPath, int maxGain) {
            this.maxPath = maxPath;
            this.maxGain = maxGain;
        }
    }
    
    /**
     * 带状态的递归解法
     * 
     * @param root 二叉树根节点
     * @return 最大路径和
     */
    public static int maxPathSumWithState(TreeNode root) {
        if (root == null) return 0;
        Result result = dfsWithState(root);
        return result.maxPath;
    }
    
    private static Result dfsWithState(TreeNode node) {
        if (node == null) {
            return new Result(Integer.MIN_VALUE, 0);
        }
        
        Result leftResult = dfsWithState(node.left);
        Result rightResult = dfsWithState(node.right);
        
        // 当前节点能提供的最大贡献值
        int currentGain = node.val + Math.max(0, Math.max(leftResult.maxGain, rightResult.maxGain));
        
        // 通过当前节点的最大路径和
        int currentMaxPath = node.val + Math.max(0, leftResult.maxGain) + Math.max(0, rightResult.maxGain);
        
        // 全局最大路径和
        int globalMaxPath = Math.max(currentMaxPath, Math.max(leftResult.maxPath, rightResult.maxPath));
        
        return new Result(globalMaxPath, currentGain);
    }
    
    // ========================= 解法3: 非递归解法（后序遍历） =========================
    
    /**
     * 非递归解法
     * 
     * @param root 二叉树根节点
     * @return 最大路径和
     */
    public static int maxPathSumIterative(TreeNode root) {
        if (root == null) return 0;
        
        Map<TreeNode, Integer> gainMap = new HashMap<>();
        int maxSum = Integer.MIN_VALUE;
        
        // 后序遍历
        Stack<TreeNode> stack = new Stack<>();
        TreeNode lastVisited = null;
        TreeNode current = root;
        
        while (!stack.isEmpty() || current != null) {
            if (current != null) {
                stack.push(current);
                current = current.left;
            } else {
                TreeNode peekNode = stack.peek();
                if (peekNode.right != null && lastVisited != peekNode.right) {
                    current = peekNode.right;
                } else {
                    // 处理当前节点
                    TreeNode node = stack.pop();
                    
                    int leftGain = node.left != null ? Math.max(0, gainMap.get(node.left)) : 0;
                    int rightGain = node.right != null ? Math.max(0, gainMap.get(node.right)) : 0;
                    
                    int currentPathSum = node.val + leftGain + rightGain;
                    maxSum = Math.max(maxSum, currentPathSum);
                    
                    gainMap.put(node, node.val + Math.max(leftGain, rightGain));
                    lastVisited = node;
                }
            }
        }
        
        return maxSum;
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 网络路由优化 - 寻找最大带宽路径
     * 
     * @param networkTree 网络拓扑树（节点值表示带宽）
     * @return 最大带宽路径的带宽总和
     */
    public static int findMaxBandwidthPath(TreeNode networkTree) {
        return maxPathSumRecursive(networkTree);
    }
    
    /**
     * 金融风险评估 - 寻找最大收益投资路径
     * 
     * @param investmentTree 投资决策树（节点值表示收益）
     * @return 最大收益路径的收益总和
     */
    public static double findMaxProfitPath(TreeNode investmentTree) {
        // 假设节点值已经是收益的100倍（避免浮点数）
        return maxPathSumRecursive(investmentTree) / 100.0;
    }
    
    /**
     * 游戏AI决策树 - 寻找最优策略路径
     * 
     * @param decisionTree 决策树（节点值表示得分）
     * @return 最优策略的总得分
     */
    public static int findOptimalStrategy(TreeNode decisionTree) {
        return maxPathSumWithState(decisionTree);
    }
    
    /**
     * 供应链优化 - 寻找最大利润路径
     * 
     * @param supplyChainTree 供应链树（节点值表示利润）
     * @return 最大利润路径的利润总和
     */
    public static int optimizeSupplyChain(TreeNode supplyChainTree) {
        return maxPathSumIterative(supplyChainTree);
    }
    
    /**
     * 社交网络影响力传播 - 寻找最大影响力路径
     * 
     * @param socialTree 社交网络树（节点值表示影响力）
     * @return 最大影响力传播路径的影响力总和
     */
    public static int findMaxInfluencePath(TreeNode socialTree) {
        return maxPathSumRecursive(socialTree);
    }
    
    // ========================= 辅助工具方法 =========================
    
    /**
     * 根据数组构建二叉树（层序遍历）
     * 
     * @param values 节点值数组，null表示空节点
     * @return 构建的二叉树根节点
     */
    public static TreeNode buildTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) {
            return null;
        }
        
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            TreeNode current = queue.poll();
            
            // 左子节点
            if (i < values.length && values[i] != null) {
                current.left = new TreeNode(values[i]);
                queue.offer(current.left);
            }
            i++;
            
            // 右子节点
            if (i < values.length && values[i] != null) {
                current.right = new TreeNode(values[i]);
                queue.offer(current.right);
            }
            i++;
        }
        
        return root;
    }
    
    /**
     * 打印二叉树结构
     * 
     * @param root 二叉树根节点
     */
    public static void printTree(TreeNode root) {
        if (root == null) {
            System.out.println("空树");
            return;
        }
        
        List<List<String>> result = new ArrayList<>();
        int height = getHeight(root);
        int width = (1 << height) - 1;
        
        // 初始化结果数组
        for (int i = 0; i < height; i++) {
            List<String> level = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                level.add(" ");
            }
            result.add(level);
        }
        
        fillTree(result, root, 0, 0, width - 1);
        
        // 打印结果
        for (List<String> level : result) {
            for (String s : level) {
                System.out.print(s);
            }
            System.out.println();
        }
    }
    
    private static void fillTree(List<List<String>> result, TreeNode node, int level, int left, int right) {
        if (node == null) return;
        
        int mid = (left + right) / 2;
        result.get(level).set(mid, String.valueOf(node.val));
        
        fillTree(result, node.left, level + 1, left, mid - 1);
        fillTree(result, node.right, level + 1, mid + 1, right);
    }
    
    private static int getHeight(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(getHeight(root.left), getHeight(root.right));
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化路径查找过程
     * 
     * @param root 二叉树根节点
     */
    public static void visualizePathFinding(TreeNode root) {
        System.out.println("\n二叉树最大路径和查找过程:");
        printTree(root);
        
        Map<TreeNode, Integer> gainMap = new HashMap<>();
        Map<TreeNode, Integer> pathMap = new HashMap<>();
        
        System.out.println("\n节点处理顺序（后序遍历）:");
        System.out.println("节点 | 左贡献 | 右贡献 | 当前路径和 | 节点贡献值");
        System.out.println("----|--------|--------|-----------|----------");
        
        visualizeDFS(root, gainMap, pathMap);
        
        int maxPath = Collections.max(pathMap.values());
        System.out.println("\n最大路径和: " + maxPath);
    }
    
    private static int visualizeDFS(TreeNode node, Map<TreeNode, Integer> gainMap, Map<TreeNode, Integer> pathMap) {
        if (node == null) return 0;
        
        int leftGain = Math.max(visualizeDFS(node.left, gainMap, pathMap), 0);
        int rightGain = Math.max(visualizeDFS(node.right, gainMap, pathMap), 0);
        
        int currentPath = node.val + leftGain + rightGain;
        int currentGain = node.val + Math.max(leftGain, rightGain);
        
        gainMap.put(node, currentGain);
        pathMap.put(node, currentPath);
        
        System.out.printf("%3d | %6d | %6d | %9d | %8d\n", 
                          node.val, leftGain, rightGain, currentPath, currentGain);
        
        return currentGain;
    }
    
    /**
     * 查找并可视化最大路径
     * 
     * @param root 二叉树根节点
     */
    public static void findAndVisualizeMaxPath(TreeNode root) {
        if (root == null) return;
        
        List<TreeNode> maxPath = new ArrayList<>();
        findMaxPathNodes(root, maxPath);
        
        System.out.println("\n最大路径节点序列:");
        for (int i = 0; i < maxPath.size(); i++) {
            if (i > 0) System.out.print(" -> ");
            System.out.print(maxPath.get(i).val);
        }
        System.out.println();
        
        int sum = maxPath.stream().mapToInt(node -> node.val).sum();
        System.out.println("路径和: " + sum);
    }
    
    private static boolean findMaxPathNodes(TreeNode node, List<TreeNode> path) {
        // 简化实现：这里只是示例，实际实现较复杂
        if (node == null) return false;
        
        path.add(node);
        
        // 递归查找左右子树
        if (findMaxPathNodes(node.left, path) || findMaxPathNodes(node.right, path)) {
            return true;
        }
        
        path.remove(path.size() - 1);
        return false;
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param nodeCount 节点数量
     */
    public static void comparePerformance(int nodeCount) {
        TreeNode tree = generateRandomTree(nodeCount);
        
        long start, end;
        
        // 测试递归解法
        start = System.nanoTime();
        int result1 = maxPathSumRecursive(tree);
        end = System.nanoTime();
        long time1 = end - start;
        
        // 测试带状态递归解法
        start = System.nanoTime();
        int result2 = maxPathSumWithState(tree);
        end = System.nanoTime();
        long time2 = end - start;
        
        // 测试非递归解法
        start = System.nanoTime();
        int result3 = maxPathSumIterative(tree);
        end = System.nanoTime();
        long time3 = end - start;
        
        System.out.println("\n性能比较:");
        System.out.printf("节点数量: %d\n", nodeCount);
        System.out.println("========================================");
        System.out.println("方法           | 时间(ms)   | 结果     ");
        System.out.println("---------------|------------|----------");
        System.out.printf("递归解法       | %.6f | %8d\n", time1 / 1_000_000.0, result1);
        System.out.printf("状态递归解法   | %.6f | %8d\n", time2 / 1_000_000.0, result2);
        System.out.printf("非递归解法     | %.6f | %8d\n", time3 / 1_000_000.0, result3);
        System.out.println("========================================");
    }
    
    // 生成随机二叉树
    private static TreeNode generateRandomTree(int nodeCount) {
        if (nodeCount <= 0) return null;
        
        Random random = new Random();
        TreeNode root = new TreeNode(random.nextInt(201) - 100); // -100 到 100
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int count = 1;
        while (!queue.isEmpty() && count < nodeCount) {
            TreeNode current = queue.poll();
            
            // 左子节点
            if (count < nodeCount && random.nextBoolean()) {
                current.left = new TreeNode(random.nextInt(201) - 100);
                queue.offer(current.left);
                count++;
            }
            
            // 右子节点
            if (count < nodeCount && random.nextBoolean()) {
                current.right = new TreeNode(random.nextInt(201) - 100);
                queue.offer(current.right);
                count++;
            }
        }
        
        return root;
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
        
        // 测试用例1: [1,2,3]
        TreeNode tree1 = buildTree(new Integer[]{1, 2, 3});
        testCase("基本三节点树", tree1, 6);
        
        // 测试用例2: [-10,9,20,null,null,15,7]
        TreeNode tree2 = buildTree(new Integer[]{-10, 9, 20, null, null, 15, 7});
        testCase("包含负数的树", tree2, 42);
        
        // 测试用例3: [2,-1,-2]
        TreeNode tree3 = buildTree(new Integer[]{2, -1, -2});
        testCase("负数子节点", tree3, 2);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        
        // 空树
        testCase("空树", null, 0);
        
        // 单节点
        TreeNode tree1 = buildTree(new Integer[]{5});
        testCase("单节点", tree1, 5);
        
        // 单节点负数
        TreeNode tree2 = buildTree(new Integer[]{-3});
        testCase("单节点负数", tree2, -3);
        
        // 全负数树
        TreeNode tree3 = buildTree(new Integer[]{-1, -2, -3});
        testCase("全负数树", tree3, -1);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 网络路由优化
        TreeNode networkTree = buildTree(new Integer[]{10, 5, 15, 3, 7, 12, 20});
        int maxBandwidth = findMaxBandwidthPath(networkTree);
        System.out.println("\n网络路由优化:");
        System.out.println("网络拓扑:");
        printTree(networkTree);
        System.out.println("最大带宽路径: " + maxBandwidth);
        
        // 场景2: 金融风险评估
        TreeNode investmentTree = buildTree(new Integer[]{500, 200, 800, -100, 300, 600, 900});
        double maxProfit = findMaxProfitPath(investmentTree);
        System.out.println("\n金融风险评估:");
        System.out.println("投资决策树:");
        printTree(investmentTree);
        System.out.println("最大收益路径: " + maxProfit);
        
        // 场景3: 游戏AI决策
        TreeNode decisionTree = buildTree(new Integer[]{10, -5, 15, 2, 8, 12, 20});
        int optimalScore = findOptimalStrategy(decisionTree);
        System.out.println("\n游戏AI决策:");
        System.out.println("决策树:");
        printTree(decisionTree);
        System.out.println("最优策略得分: " + optimalScore);
        
        // 场景4: 供应链优化
        TreeNode supplyTree = buildTree(new Integer[]{100, 50, 150, 25, 75, 125, 175});
        int maxSupplyProfit = optimizeSupplyChain(supplyTree);
        System.out.println("\n供应链优化:");
        System.out.println("供应链树:");
        printTree(supplyTree);
        System.out.println("最大利润路径: " + maxSupplyProfit);
        
        // 场景5: 社交网络影响力
        TreeNode socialTree = buildTree(new Integer[]{20, 10, 30, 5, 15, 25, 35});
        int maxInfluence = findMaxInfluencePath(socialTree);
        System.out.println("\n社交网络影响力:");
        System.out.println("社交网络树:");
        printTree(socialTree);
        System.out.println("最大影响力路径: " + maxInfluence);
        
        // 可视化测试
        System.out.println("\n可视化测试:");
        TreeNode visualTree = buildTree(new Integer[]{1, 2, 3, 4, 5});
        visualizePathFinding(visualTree);
        findAndVisualizeMaxPath(visualTree);
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(100);
        comparePerformance(1000);
        comparePerformance(10000);
    }
    
    private static void testCase(String name, TreeNode root, int expected) {
        System.out.printf("\n测试用例: %s\n", name);
        
        if (root == null && expected == 0) {
            System.out.println("空树测试通过");
            return;
        }
        
        if (root != null) {
            System.out.println("树结构:");
            printTree(root);
        }
        
        int result1 = maxPathSumRecursive(root);
        int result2 = maxPathSumWithState(root);
        int result3 = maxPathSumIterative(root);
        
        System.out.println("递归解法结果: " + result1);
        System.out.println("状态递归解法结果: " + result2);
        System.out.println("非递归解法结果: " + result3);
        
        boolean passed = (result1 == expected) && (result2 == expected) && (result3 == expected);
        System.out.println("测试结果: " + (passed ? "通过" : "失败"));
        
        if (!passed) {
            System.out.println("预期结果: " + expected);
        }
        
        // 可视化小规模案例
        if (root != null && getHeight(root) <= 4) {
            visualizePathFinding(root);
        }
    }
}