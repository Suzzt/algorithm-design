package main.java.org.dao.qa;

import java.util.*;

/**
 * 最小基因变化问题
 * 
 * <p><b>问题描述</b>:
 * 给定两个基因序列start和end，以及一个基因库bank，找出从start变化到end所需的最小变化次数。
 * 每次变化只能改变一个碱基，且变化后的基因必须在基因库中。
 * 如果无法完成变化，返回-1。
 * 
 * <p><b>示例</b>:
 * 输入: start = "AACCGGTT", end = "AACCGGTA", bank = ["AACCGGTA"]
 * 输出: 1
 * 
 * <p><b>问题难度</b>: 🔥🔥 中等 (LeetCode 第433题)
 * 
 * <p><b>解题思路</b>:
 * 1. 广度优先搜索(BFS): 将基因变化视为图遍历
 * 2. 双向BFS: 从起点和终点同时搜索
 * 3. 遗传算法启发式搜索: 使用启发式函数优化搜索
 * 
 * <p><b>时间复杂度</b>:
 *  BFS: O(N×L) N为基因库大小，L为基因长度
 *  双向BFS: O(N×L)
 *  遗传算法: O(N×L×G) G为代数
 * 
 * <p><b>空间复杂度</b>:
 *  BFS: O(N)
 *  双向BFS: O(N)
 *  遗传算法: O(P) P为种群大小
 * 
 * <p><b>应用场景</b>:
 * 1. 基因工程中的基因编辑
 * 2. 生物信息学中的基因演化分析
 * 3. 药物研发中的分子结构优化
 * 4. 密码学中的相似密钥搜索
 * 5. 自动代码修复系统
 */

public class MinimumGeneticMutation {
    
    // 基因碱基类型
    private static final char[] BASES = {'A', 'C', 'G', 'T'};
    
    // ========================= 解法1: 广度优先搜索 =========================
    
    /**
     * 广度优先搜索解法
     * 
     * @param start 起始基因
     * @param end 目标基因
     * @param bank 基因库
     * @return 最小变化次数
     */
    public static int minMutationBFS(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (!bankSet.contains(end)) return -1;
        
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(start);
        visited.add(start);
        
        int steps = 0;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String current = queue.poll();
                if (current.equals(end)) return steps;
                
                // 生成所有可能的变化
                for (String neighbor : getNeighbors(current, bankSet)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.offer(neighbor);
                    }
                }
            }
            steps++;
        }
        
        return -1;
    }
    
    // 获取所有有效邻居基因
    private static List<String> getNeighbors(String gene, Set<String> bankSet) {
        List<String> neighbors = new ArrayList<>();
        char[] geneArr = gene.toCharArray();
        
        for (int i = 0; i < geneArr.length; i++) {
            char original = geneArr[i];
            for (char base : BASES) {
                if (base != original) {
                    geneArr[i] = base;
                    String newGene = new String(geneArr);
                    if (bankSet.contains(newGene)) {
                        neighbors.add(newGene);
                    }
                }
            }
            geneArr[i] = original;
        }
        return neighbors;
    }
    
    // ========================= 解法2: 双向BFS =========================
    
    /**
     * 双向广度优先搜索解法
     * 
     * @param start 起始基因
     * @param end 目标基因
     * @param bank 基因库
     * @return 最小变化次数
     */
    public static int minMutationBidirectionalBFS(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (!bankSet.contains(end)) return -1;
        
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        Set<String> visited = new HashSet<>();
        
        beginSet.add(start);
        endSet.add(end);
        visited.add(start);
        visited.add(end);
        
        int steps = 0;
        
        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            // 总是从较小的集合开始扩展
            if (beginSet.size() > endSet.size()) {
                Set<String> temp = beginSet;
                beginSet = endSet;
                endSet = temp;
            }
            
            Set<String> nextLevel = new HashSet<>();
            for (String gene : beginSet) {
                for (String neighbor : getNeighbors(gene, bankSet)) {
                    if (endSet.contains(neighbor)) {
                        return steps + 1;
                    }
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        nextLevel.add(neighbor);
                    }
                }
            }
            beginSet = nextLevel;
            steps++;
        }
        
        return -1;
    }
    
    // ========================= 解法3: 遗传算法启发式搜索 =========================
    
    /**
     * 遗传算法启发式解法
     * 
     * @param start 起始基因
     * @param end 目标基因
     * @param bank 基因库
     * @return 最小变化次数
     */
    public static int minMutationGeneticAlgorithm(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (!bankSet.contains(end)) return -1;
        
        // 初始化种群
        List<String> population = new ArrayList<>();
        population.add(start);
        
        // 适应度函数：计算与目标的相似度
        Map<String, Integer> fitness = new HashMap<>();
        fitness.put(start, fitness(start, end));
        
        int generation = 0;
        int maxGenerations = 1000; // 最大迭代次数
        
        while (generation < maxGenerations) {
            List<String> newPopulation = new ArrayList<>();
            
            for (String gene : population) {
                if (gene.equals(end)) return generation;
                
                // 变异操作
                for (String neighbor : getNeighbors(gene, bankSet)) {
                    if (!fitness.containsKey(neighbor)) {
                        int fit = fitness(neighbor, end);
                        fitness.put(neighbor, fit);
                        newPopulation.add(neighbor);
                    }
                }
            }
            
            // 选择最优个体
            population = selectBest(newPopulation, fitness, 5);
            generation++;
        }
        
        return -1;
    }
    
    // 计算适应度（与目标的相似度）
    private static int fitness(String gene, String target) {
        int diff = 0;
        for (int i = 0; i < gene.length(); i++) {
            if (gene.charAt(i) != target.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }
    
    // 选择适应度最高的个体
    private static List<String> selectBest(List<String> population, Map<String, Integer> fitness, int size) {
        population.sort((g1, g2) -> fitness.get(g1) - fitness.get(g2));
        return population.subList(0, Math.min(size, population.size()));
    }
    
    // ========================= 应用场景扩展 =========================
    
    /**
     * 基因编辑距离计算器
     * 
     * @param startGene 起始基因
     * @param targetGene 目标基因
     * @param geneBank 基因库
     * @return 最小编辑距离
     */
    public static int geneEditingDistance(String startGene, String targetGene, String[] geneBank) {
        return minMutationBidirectionalBFS(startGene, targetGene, geneBank);
    }
    
    /**
     * 分子结构优化器
     * 
     * @param startMolecule 起始分子结构
     * @param targetMolecule 目标分子结构
     * @param validStructures 有效结构库
     * @return 最小转换步骤
     */
    public static int moleculeOptimizer(String startMolecule, String targetMolecule, String[] validStructures) {
        return minMutationBFS(startMolecule, targetMolecule, validStructures);
    }
    
    /**
     * 密码学密钥演化器
     * 
     * @param startKey 起始密钥
     * @param targetKey 目标密钥
     * @param validKeys 有效密钥库
     * @return 最小演化步骤
     */
    public static int keyEvolution(String startKey, String targetKey, String[] validKeys) {
        return minMutationGeneticAlgorithm(startKey, targetKey, validKeys);
    }
    
    /**
     * 自动代码修复系统
     * 
     * @param buggyCode 有bug的代码
     * @param fixedCode 修复后的代码
     * @param validCodeSnippets 有效代码片段
     * @return 最小修复步骤
     */
    public static int autoCodeFixer(String buggyCode, String fixedCode, String[] validCodeSnippets) {
        return minMutationBFS(buggyCode, fixedCode, validCodeSnippets);
    }
    
    // ========================= 可视化工具 =========================
    
    /**
     * 可视化基因变化路径
     * 
     * @param start 起始基因
     * @param end 目标基因
     * @param bank 基因库
     */
    public static void visualizeMutationPath(String start, String end, String[] bank) {
        Map<String, String> path = findMutationPath(start, end, bank);
        if (path == null) {
            System.out.println("无有效路径");
            return;
        }
        
        System.out.println("\n基因变化路径:");
        List<String> steps = new ArrayList<>();
        String current = end;
        while (!current.equals(start)) {
            steps.add(current);
            current = path.get(current);
        }
        steps.add(start);
        Collections.reverse(steps);
        
        for (int i = 0; i < steps.size(); i++) {
            System.out.printf("步骤 %d: %s%n", i, steps.get(i));
        }
    }
    
    // 查找基因变化路径
    private static Map<String, String> findMutationPath(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (!bankSet.contains(end)) return null;
        
        Queue<String> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(start);
        visited.add(start);
        parentMap.put(start, null);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) return parentMap;
            
            for (String neighbor : getNeighbors(current, bankSet)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }
        return null;
    }
    
    /**
     * 可视化遗传算法进化过程
     * 
     * @param start 起始基因
     * @param end 目标基因
     * @param bank 基因库
     */
    public static void visualizeGeneticEvolution(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (!bankSet.contains(end)) return;
        
        // 初始化种群
        List<String> population = new ArrayList<>();
        population.add(start);
        
        // 适应度函数
        Map<String, Integer> fitness = new HashMap<>();
        fitness.put(start, fitness(start, end));
        
        int generation = 0;
        int maxGenerations = 20; // 最大迭代次数
        
        System.out.println("\n遗传算法进化过程:");
        System.out.println("代数 | 种群 | 最佳适应度");
        System.out.println("----|------|-----------");
        
        while (generation < maxGenerations) {
            // 打印当前代
            String bestGene = population.get(0);
            int bestFitness = fitness.get(bestGene);
            System.out.printf("%2d  | %4d | %s (%d)%n", 
                             generation, population.size(), bestGene, bestFitness);
            
            // 终止条件
            if (bestGene.equals(end)) break;
            
            List<String> newPopulation = new ArrayList<>();
            for (String gene : population) {
                for (String neighbor : getNeighbors(gene, bankSet)) {
                    if (!fitness.containsKey(neighbor)) {
                        int fit = fitness(neighbor, end);
                        fitness.put(neighbor, fit);
                        newPopulation.add(neighbor);
                    }
                }
            }
            
            // 选择最优个体
            population = selectBest(newPopulation, fitness, 5);
            generation++;
        }
    }
    
    /**
     * 可视化双向BFS搜索过程
     * 
     * @param start 起始基因
     * @param end 目标基因
     * @param bank 基因库
     */
    public static void visualizeBidirectionalBFS(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (!bankSet.contains(end)) return;
        
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        Set<String> visited = new HashSet<>();
        
        beginSet.add(start);
        endSet.add(end);
        visited.add(start);
        visited.add(end);
        
        int steps = 0;
        
        System.out.println("\n双向BFS搜索过程:");
        System.out.println("步骤 | 起始集合 | 结束集合 | 交集");
        System.out.println("----|----------|----------|----------");
        
        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            // 打印当前状态
            System.out.printf("%2d  | %-8s | %-8s | %-8s%n", 
                             steps, beginSet, endSet, getIntersection(beginSet, endSet));
            
            if (beginSet.size() > endSet.size()) {
                Set<String> temp = beginSet;
                beginSet = endSet;
                endSet = temp;
            }
            
            Set<String> nextLevel = new HashSet<>();
            for (String gene : beginSet) {
                for (String neighbor : getNeighbors(gene, bankSet)) {
                    if (endSet.contains(neighbor)) {
                        System.out.println("找到交集: " + neighbor);
                        return;
                    }
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        nextLevel.add(neighbor);
                    }
                }
            }
            beginSet = nextLevel;
            steps++;
        }
        System.out.println("未找到路径");
    }
    
    // 获取两个集合的交集
    private static Set<String> getIntersection(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection;
    }
    
    // ========================= 性能分析工具 =========================
    
    /**
     * 性能比较测试
     * 
     * @param geneLength 基因长度
     * @param bankSize 基因库大小
     */
    public static void comparePerformance(int geneLength, int bankSize) {
        // 生成测试数据
        String start = generateRandomGene(geneLength);
        String end = generateRandomGene(geneLength);
        String[] bank = generateGeneBank(geneLength, bankSize, start, end);
        
        long startTime, endTime;
        
        // 测试BFS
        startTime = System.nanoTime();
        int bfsResult = minMutationBFS(start, end, bank);
        endTime = System.nanoTime();
        long bfsTime = endTime - startTime;
        
        // 测试双向BFS
        startTime = System.nanoTime();
        int biBfsResult = minMutationBidirectionalBFS(start, end, bank);
        endTime = System.nanoTime();
        long biBfsTime = endTime - startTime;
        
        // 测试遗传算法
        startTime = System.nanoTime();
        int gaResult = minMutationGeneticAlgorithm(start, end, bank);
        endTime = System.nanoTime();
        long gaTime = endTime - startTime;
        
        System.out.println("\n性能比较:");
        System.out.println("基因长度: " + geneLength + ", 基因库大小: " + bankSize);
        System.out.println("===============================================");
        System.out.println("方法            | 时间(ns) | 结果");
        System.out.println("----------------|----------|--------");
        System.out.printf("BFS            | %8d | %d%n", bfsTime, bfsResult);
        System.out.printf("双向BFS         | %8d | %d%n", biBfsTime, biBfsResult);
        System.out.printf("遗传算法        | %8d | %d%n", gaTime, gaResult);
        System.out.println("===============================================");
    }
    
    // 生成随机基因
    private static String generateRandomGene(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(BASES[random.nextInt(BASES.length)]);
        }
        return sb.toString();
    }
    
    // 生成基因库
    private static String[] generateGeneBank(int geneLength, int size, String start, String end) {
        Set<String> bank = new HashSet<>();
        bank.add(start);
        bank.add(end);
        
        Random random = new Random();
        while (bank.size() < size) {
            StringBuilder gene = new StringBuilder();
            for (int i = 0; i < geneLength; i++) {
                gene.append(BASES[random.nextInt(BASES.length)]);
            }
            bank.add(gene.toString());
        }
        
        return bank.toArray(new String[0]);
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
        testCase("示例1", "AACCGGTT", "AACCGGTA", new String[]{"AACCGGTA"}, 1);
        testCase("示例2", "AACCGGTT", "AAACGGTA", new String[]{"AACCGGTA", "AACCGCTA", "AAACGGTA", "AACCCGTA", "AACCTGTA"}, 2);
        testCase("示例3", "AAAAACCC", "AACCCCCC", new String[]{"AAAACCCC", "AAACCCCC", "AACCCCCC"}, 3);
    }
    
    private static void testEdgeCases() {
        System.out.println("\n====== 边界测试 ======");
        testCase("相同基因", "AACCGGTT", "AACCGGTT", new String[]{"AACCGGTT"}, 0);
        testCase("无有效路径", "AACCGGTT", "AACCGGTA", new String[]{}, -1);
        testCase("目标不在库中", "AACCGGTT", "AACCGGTA", new String[]{"AACCGGTC"}, -1);
        testCase("长基因序列", "AAAAA", "TTTTT", 
                new String[]{"AAAAT", "AAATT", "AATTT", "ATTTT", "TTTTT"}, 4);
    }
    
    private static void testApplicationScenarios() {
        System.out.println("\n====== 应用场景测试 ======");
        
        // 场景1: 基因编辑
        String startGene = "ATGCTAGCTAG";
        String targetGene = "ATGCTAGCTAA";
        String[] geneBank = {"ATGCTAGCTAG", "ATGCTAGCTTG", "ATGCTAGCTAA"};
        System.out.println("基因编辑距离: " + geneEditingDistance(startGene, targetGene, geneBank));
        
        // 场景2: 分子结构优化
        String startMolecule = "C6H12O6";
        String targetMolecule = "C6H12O5";
        String[] validStructures = {"C6H12O6", "C6H12O5", "C6H12O4"};
        System.out.println("分子结构优化步骤: " + moleculeOptimizer(startMolecule, targetMolecule, validStructures));
        
        // 场景3: 密钥演化
        String startKey = "SECRETKEY";
        String targetKey = "SECUREKEY";
        String[] validKeys = {"SECRETKEY", "SECRETKEZ", "SECUREKEY"};
        System.out.println("密钥演化步骤: " + keyEvolution(startKey, targetKey, validKeys));
        
        // 场景4: 代码修复
        String buggyCode = "if(a=b){return true;}";
        String fixedCode = "if(a==b){return true;}";
        String[] validCodeSnippets = {"if(a=b){return true;}", "if(a==b){return true;}", "if(a===b){return true;}"};
        System.out.println("代码修复步骤: " + autoCodeFixer(buggyCode, fixedCode, validCodeSnippets));
        
        // 可视化测试
        System.out.println("\n可视化测试:");
        visualizeMutationPath("AACCGGTT", "AAACGGTA", new String[]{"AACCGGTA", "AACCGCTA", "AAACGGTA", "AACCCGTA", "AACCTGTA"});
        visualizeGeneticEvolution("AAAA", "TTTT", new String[]{"AAAT", "AATT", "ATTT", "TTTT"});
        visualizeBidirectionalBFS("AACCGGTT", "AAACGGTA", new String[]{"AACCGGTA", "AACCGCTA", "AAACGGTA", "AACCCGTA", "AACCTGTA"});
    }
    
    private static void testPerformance() {
        System.out.println("\n====== 性能测试 ======");
        comparePerformance(8, 100);
        comparePerformance(12, 1000);
        comparePerformance(16, 10000);
    }
    
    private static void testCase(String name, String start, String end, String[] bank, int expected) {
        int bfs = minMutationBFS(start, end, bank);
        int biBfs = minMutationBidirectionalBFS(start, end, bank);
        int ga = minMutationGeneticAlgorithm(start, end, bank);
        
        System.out.printf("\n测试: %s%n", name);
        System.out.println("起始基因: " + start);
        System.out.println("目标基因: " + end);
        System.out.println("基因库: " + Arrays.toString(bank));
        System.out.println("BFS结果: " + bfs);
        System.out.println("双向BFS结果: " + biBfs);
        System.out.println("遗传算法结果: " + ga);
        
        boolean passed = (bfs == expected) && (biBfs == expected) && (ga == expected);
        System.out.println("测试结果: " + (passed ? "通过" : "失败"));
        
        if (!passed) {
            System.out.println("预期结果: " + expected);
        }
        
        // 可视化小规模案例
        if (bank.length <= 10) {
            visualizeMutationPath(start, end, bank);
        }
    }
}