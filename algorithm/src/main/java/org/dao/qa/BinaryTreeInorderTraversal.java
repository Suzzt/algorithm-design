package main.java.org.dao.qa;

import java.util.*;

/**
 * 二叉树中序遍历（迭代法）
 * 
 * <p><b>问题描述</b>:
 * 给定一个二叉树的根节点，返回其中序遍历的节点值序列。要求使用迭代法而非递归实现。
 * 
 * <p><b>示例</b>:
 * 输入:
 *    1
 *     \
 *      2
 *     /
 *    3
 * 输出: [1,3,2]
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第94题)
 * 
 * <p><b>解题思路</b>:
 * 1. 栈迭代法: 使用栈显式管理遍历过程
 * 2. 标记法: 使用标记记录节点访问状态
 * 3. 线索二叉树法: 空间复杂度O(1)的Morris遍历算法
 * 
 * <p><b>时间复杂度</b>:
 *  所有方法: O(n) - n为节点数
 * 
 * <p><b>空间复杂度</b>:
 *  栈迭代法: O(h) - h为树高度
 *  标记法: O(n) - 节点数
 *  Morris法: O(1) - 仅使用常数空间
 * 
 * <p><b>应用场景</b>:
 * 1. 表达式树求值
 * 2. 二叉搜索树的有序遍历
 * 3. 语法树处理
 * 4. 文件系统目录遍历
 * 5. 编译器抽象语法树处理
 */

public class BinaryTreeInorderTraversal {

    // 二叉树节点定义
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int val) {
            this.val = val;
        }
    }
    
    // ========================= 解法1: 栈迭代法 =========================
    
    /**
     * 栈迭代法 - 标准方法
     * 
     * @param root 二叉树根节点
     * @return 中序遍历序列
     */
    public static List<Integer> inorderStack(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        
        while (curr != null || !stack.isEmpty()) {
            // 遍历左子树到最左节点
            while (curr != null) {
                stack.push(curr);
                curr = curr.left;
            }
            
            // 访问节点
            curr = stack.pop();
            result.add(curr.val);
            
            // 处理右子树
            curr = curr.right;
        }
        
        return result;
    }
    
    // ========================= 解法2: 标记法 =========================
    
    /**
     * 标记法 - 易于理解
     * 
     * @param root 二叉树根节点
     * @return 中序遍历序列
     */
    public static List<Integer> inorderColorMethod(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        
        // 使用一个状态栈：0表示未访问，1表示已访问
        Stack<Pair> stack = new Stack<>();
        stack.push(new Pair(root, Color.WHITE));
        
        while (!stack.isEmpty()) {
            Pair pair = stack.pop();
            TreeNode node = pair.node;
            
            if (pair.color == Color.WHITE) {
                // 未访问节点，按照反序压入栈（右-根-左）
                if (node.right != null) {
                    stack.push(new Pair(node.right, Color.WHITE));
                }
                // 重新压入当前节点（作为已访问节点）
                stack.push(new Pair(node, Color.GRAY));
                if (node.left != null) {
                    stack.push(new Pair(node.left, Color.WHITE));
                }
            } else {
                // 已访问节点，添加到结果集
                result.add(node.val);
            }
        }
        
        return result;
    }
    
    // 定义访问状态颜色
    enum Color {
        WHITE, // 未访问
        GRAY   // 已访问
    }
    
    // 节点-状态对
    static class Pair {
        TreeNode node;
        Color color;
        
        Pair(TreeNode node, Color color) {
            this.node = node;
            this.color = color;
        }
    }
    
    // ========================= 解法3: Morris遍历 =========================
    
    /**
     * Morris遍历法 - 空间复杂度O(1)
     * 
     * @param root 二叉树根节点
     * @return 中序遍历序列
     */
    public static List<Integer> inorderMorris(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode curr = root;
        
        while (curr != null) {
            // 左子树为空，访问当前节点并转向右子树
            if (curr.left == null) {
                result.add(curr.val);
                curr = curr.right;
            } else {
                // 找到左子树的最右节点
                TreeNode prev = curr.left;
                while (prev.right != null && prev.right != curr) {
                    prev = prev.right;
                }
                
                // 建立线索
                if (prev.right == null) {
                    // 第一次到达，建立线索
                    prev.right = curr;
                    curr = curr.left;
                } else {
                    // 第二次到达，撤销线索并访问
                    prev.right = null;
                    result.add(curr.val);
                    curr = curr.right;
                }
            }
        }
        
        return result;
    }
    
    // ========================= 工具方法 =========================
    
    /**
     * 创建二叉树
     * 
     * @param values 节点值数组（按层序排列，-1表示空节点）
     * @return 根节点
     */
    public static TreeNode createTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) 
            return null;
        
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        
        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            TreeNode curr = queue.poll();
            
            // 左子节点
            if (i < values.length && values[i] != null) {
                curr.left = new TreeNode(values[i]);
                queue.add(curr.left);
            }
            i++;
            
            // 右子节点
            if (i < values.length && values[i] != null) {
                curr.right = new TreeNode(values[i]);
                queue.add(curr.right);
            }
            i++;
        }
        
        return root;
    }
    
    /**
     * 可视化中序遍历过程
     * 
     * @param root 二叉树根节点
     */
    public static void visualizeTraversal(TreeNode root) {
        if (root == null) {
            System.out.println("空树");
            return;
        }
        
        System.out.println("\n二叉树结构:");
        printTree(root, 0);
        
        System.out.println("\n中序遍历过程:");
        visualizeInorderStack(root);
        visualizeInorderMorris(root);
    }
    
    // 打印二叉树
    private static void printTree(TreeNode node, int level) {
        if (node == null) return;
        
        // 打印右子树
        printTree(node.right, level + 1);
        
        // 打印当前节点
        for (int i = 0; i < level; i++) 
            System.out.print("   ");
        System.out.println(node.val);
        
        // 打印左子树
        printTree(node.left, level + 1);
    }
    
    // 可视化栈迭代法过程
    private static void visualizeInorderStack(TreeNode root) {
        System.out.println("\n栈迭代法过程:");
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode curr = root;
        int step = 1;
        
        while (curr != null || !stack.isEmpty()) {
            System.out.printf("第%d步:\n", step++);
            
            if (curr != null) {
                System.out.printf("  当前节点: %d（左子节点入栈）\n", curr.val);
                stack.push(curr);
                curr = curr.left;
            } else {
                curr = stack.pop();
                System.out.printf("  弹出节点: %d（添加到结果）\n", curr.val);
                result.add(curr.val);
                curr = curr.right;
                if (curr != null) {
                    System.out.printf("  处理右子节点: %d\n", curr.val);
                }
            }
            
            // 打印当前栈状态
            System.out.print("  栈状态: ");
            for (TreeNode n : stack) {
                System.out.print(n.val + " ");
            }
            System.out.println("\n  当前结果: " + result);
        }
    }
    
    // 可视化Morris遍历过程
    private static void visualizeInorderMorris(TreeNode root) {
        System.out.println("\nMorris遍历过程:");
        List<Integer> result = new ArrayList<>();
        TreeNode curr = root;
        int step = 1;
        
        while (curr != null) {
            System.out.printf("第%d步: 当前节点 - %d\n", step++, curr.val);
            
            if (curr.left == null) {
                System.out.printf("  左子节点为空，访问节点 %d 并转向右子树\n", curr.val);
                result.add(curr.val);
                curr = curr.right;
            } else {
                TreeNode prev = curr.left;
                System.out.printf("  左子节点存在: %d, 开始寻找前驱节点\n", prev.val);
                
                // 寻找前驱节点
                int searchSteps = 1;
                while (prev.right != null && prev.right != curr) {
                    prev = prev.right;
                    System.out.printf("    第%d步搜索: 移动到节点 %d\n", searchSteps++, prev.val);
                }
                
                if (prev.right == null) {
                    System.out.printf("  建立线索: %d -> %d\n", prev.val, curr.val);
                    prev.right = curr;
                    System.out.printf("  移动到左子节点: %d\n", curr.left.val);
                    curr = curr.left;
                } else {
                    System.out.printf("  发现线索: %d -> %d（已存在线索）\n", prev.val, curr.val);
                    System.out.printf("  撤销线索并访问节点 %d\n", curr.val);
                    prev.right = null;
                    result.add(curr.val);
                    System.out.printf("  转向右子节点: %s\n", 
                                     (curr.right != null ? String.valueOf(curr.right.val) : "null"));
                    curr = curr.right;
                }
            }
            
            System.out.println("  当前结果: " + result);
        }
    }
    
    // ========================= 应用场景 =========================
    
    /**
     * 二叉搜索树验证
     * 
     * @param root 二叉树根节点
     * @return 是否为二叉搜索树
     */
    public static boolean isValidBST(TreeNode root) {
        List<Integer> inorder = inorderStack(root);
        for (int i = 1; i < inorder.size(); i++) {
            if (inorder.get(i) <= inorder.get(i - 1)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 表达式树求值
     * 
     * @param root 表达式树的根节点
     * @return 表达式计算结果
     */
    public static int evaluateExpressionTree(TreeNode root) {
        if (root == null) return 0;
        if (root.left == null && root.right == null) 
            return root.val;
        
        // 使用中序遍历获取表达式（后缀表达式）
        List<Integer> values = inorderStack(root);
        Stack<Integer> stack = new Stack<>();
        
        for (int val : values) {
            if (val >= 0) {
                // 数字直接入栈
                stack.push(val);
            } else {
                // 运算符：执行计算
                int b = stack.pop();
                int a = stack.pop();
                int result = applyOperator(a, b, (char) val);
                stack.push(result);
            }
        }
        
        return stack.isEmpty() ? 0 : stack.pop();
    }
    
    // 应用算术运算符
    private static int applyOperator(int a, int b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
            default: return 0;
        }
    }
    
    /**
     * 文件系统目录树遍历
     * 
     * @param root 目录树根节点
     * @return 中序遍历的目录列表
     */
    public static List<String> traverseDirectory(TreeNode root) {
        List<Integer> inorder = inorderMorris(root);
        List<String> result = new ArrayList<>();
        for (int val : inorder) {
            result.add(getDirectoryName(val));
        }
        return result;
    }
    
    // 获取目录名（根据节点值映射）
    private static String getDirectoryName(int code) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "/");
        map.put(2, "/usr");
        map.put(3, "/usr/bin");
        map.put(4, "/usr/lib");
        map.put(5, "/etc");
        map.put(6, "/etc/passwd");
        map.put(7, "/home");
        return map.getOrDefault(code, "未知目录(" + code + ")");
    }
    
    // ========================= 测试用例 =========================
    
    public static void main(String[] args) {
        // 测试用例1: 标准测试
        testBasicTree();
        
        // 测试用例2: 大型树测试
        testLargeTree();
        
        // 测试用例3: 应用场景测试
        testApplicationScenarios();
    }
    
    private static void testBasicTree() {
        System.out.println("====== 基础测试 ======");
        /*
         * 创建测试树:
         *    1
         *     \
         *      2
         *     /
         *    3
         */
        Integer[] values = {1, null, 2, null, null, 3, null};
        TreeNode root = createTree(values);
        
        List<Integer> stackResult = inorderStack(root);
        List<Integer> colorResult = inorderColorMethod(root);
        List<Integer> morrisResult = inorderMorris(root);
        
        System.out.println("栈迭代法结果: " + stackResult);
        System.out.println("标记法结果:   " + colorResult);
        System.out.println("Morris遍历结果: " + morrisResult);
        
        // 可视化遍历过程
        visualizeTraversal(root);
    }
    
    private static void testLargeTree() {
        System.out.println("\n====== 大型树测试 ======");
        // 创建完全二叉树: 1-15
        Integer[] fullTree = new Integer[15];
        for (int i = 0; i < fullTree.length; i++) {
            fullTree[i] = i + 1;
        }
        TreeNode largeTree = createTree(fullTree);
        
        // 性能测试
        long start, end;
        
        start = System.nanoTime();
        List<Integer> stackResult = inorderStack(largeTree);
        end = System.nanoTime();
        System.out.println("栈迭代法用时: " + (end - start) / 1000 + "微秒");
        
        start = System.nanoTime();
        List<Integer> colorResult = inorderColorMethod(largeTree);
        end = System.nanoTime();
        System.out.println("标记法用时:   " + (end - start) / 1000 + "微秒");
        
        start = System.nanoTime();
        List<Integer> morrisResult = inorderMorris(largeTree);
        end = System.nanoTime();
        System.out.println("Morris法用时: " + (end - start) / 1000 + "微秒");
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 应用1: 验证二叉搜索树
        Integer[] bstValues = {5, 3, 8, 1, 4, 7, 9}; // 有效BST
        TreeNode bstRoot = createTree(bstValues);
        System.out.println("二叉搜索树验证: " + isValidBST(bstRoot) + " (预期: true)");
        
        Integer[] nonBstValues = {5, 3, 8, 1, 6, 7, 9}; // 无效BST（6在左子树但大于5）
        TreeNode nonBstRoot = createTree(nonBstValues);
        System.out.println("非二叉搜索树验证: " + isValidBST(nonBstRoot) + " (预期: false)");
        
        // 应用2: 表达式树求值
        TreeNode exprRoot = new TreeNode('-');
        exprRoot.left = new TreeNode('*');
        exprRoot.right = new TreeNode(10);
        exprRoot.left.left = new TreeNode(3);
        exprRoot.left.right = new TreeNode(4);
        
        System.out.println("\n表达式树求值:");
        visualizeTraversal(exprRoot);
        int result = evaluateExpressionTree(exprRoot);
        System.out.println("表达式求值结果: 3 * 4 - 10 = " + result);
        
        // 应用3: 文件系统目录树遍历
        Integer[] dirTreeValues = {1, 2, 5, 3, 4, 6, 7};
        TreeNode dirRoot = createTree(dirTreeValues);
        /*
         * 目录树结构:
         *       1 (/)
         *     /   \
         *    2    5
         *   / \  / \
         *  3  4 6  7
         * 
         * 实际目录映射:
         *  1: /, 2: /usr, 3: /usr/bin, 4: /usr/lib
         *  5: /etc, 6: /etc/passwd, 7: /home
         * 
         * 中序遍历: 3,2,4,1,6,5,7
         * 实际路径: /usr/bin, /usr, /usr/lib, /, /etc/passwd, /etc, /home
         */
        List<String> dirs = traverseDirectory(dirRoot);
        System.out.println("\n文件系统目录遍历:");
        System.out.println("预期顺序: [/usr/bin, /usr, /usr/lib, /, /etc/passwd, /etc, /home]");
        System.out.println("实际顺序: " + dirs);
    }
}