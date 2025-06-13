package main.java.org.dao.qa;

import java.util.*;

/**
 * 单词接龙 II - 找到所有最短转换序列
 * 
 * <p><b>问题描述</b>:
 * 给定两个单词（beginWord 和 endWord）和一个字典 wordList，需要找出所有从 beginWord 到 endWord 的最短转换序列。
 * 转换规则:
 * 1. 每次转换只能改变一个字母
 * 2. 转换过程中的每个中间单词都必须是字典 wordList 中的单词
 * 
 * <p><b>问题难度</b>: 🔥🔥🔥 困难（LeetCode 第126题）
 * 
 * <p><b>算法思路</b>:
 * 采用双管齐下的搜索策略：
 * 1. <b>广度优先搜索(BFS)</b>：用于构建单词之间的层级关系和前驱映射
 *   - 从起始词开始逐层遍历
 *   - 记录每个单词到起始词的距离
 *   - 建立前驱映射：每个单词到它的上一个单词的映射
 * 2. <b>深度优先搜索(DFS)</b>：用于根据前驱映射回溯所有最短路径
 *   - 从结束词开始，反向通过前驱映射回溯到起始词
 *   - 收集所有完整的转换序列
 * 
 * <p><b>优化点</b>:
 * 1. <b>高效邻居生成</b>：通过字母替换而非全词典扫描
 * 2. <b>层级感知</b>：只考虑当前层的有效转换
 * 3. <b>双向搜索（可选）</b>：从起始词和结束词同时搜索（本文实现为单向）
 * 
 * <p><b>时间复杂度分析</b>:
 * 设 N = 单词数量, L = 单词长度, R = 结果路径数, P = 路径长度
 * 1. BFS阶段：O(N × L × 26) - 每个单词处理L个位置 × 26种变化
 * 2. DFS阶段：O(R × P) - 每条路径需要P步完成构建
 * 最坏情况：O(N × L × 26 + R × P)
 * 
 * <p><b>空间复杂度分析</b>:
 * 1. O(N × L) - 存储单词集合和前驱映射
 * 2. O(N) - BFS队列和距离映射
 * 3. O(R × P) - 存储所有结果路径
 * 
 * <p><b>适用场景</b>:
 * 1. 单词接龙类游戏实现
 * 2. 基因序列转换路径分析
 * 3. 网络路由中的多路径查找
 * 4. 社交网络中的最短关系链
 * 5. 密码破解中的多路径解谜
 * 
 * <p><b>示例测试</b>:
 * Input: 
 *   beginWord = "hit"
 *   endWord = "cog"
 *   wordList = ["hot","dot","dog","lot","log","cog"]
 * 
 * Output: 
 *   [
 *     ["hit","hot","dot","dog","cog"],
 *     ["hit","hot","lot","log","cog"]
 *   ]
 */

public class WordLadderII {
    
    // 主解题方法
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        // 将单词列表转换为集合提高查找效率
        Set<String> wordSet = new HashSet<>(wordList);
        // 存储最终结果
        List<List<String>> result = new ArrayList<>();
        
        // 检查结束词是否在字典中
        if (!wordSet.contains(endWord)) {
            return result;
        }
        
        // 前驱映射：记录每个单词可由哪些单词转换而来
        Map<String, List<String>> predecessors = new HashMap<>();
        // 每个单词到起始词的距离
        Map<String, Integer> distances = new HashMap<>();
        // BFS队列
        Queue<String> queue = new LinkedList<>();
        
        // 初始化数据结构
        initializeDataStructures(beginWord, wordSet, predecessors, distances, queue);
        
        // BFS遍历标记
        boolean foundEndWord = false;
        
        // 执行BFS构建前驱映射
        while (!queue.isEmpty() && !foundEndWord) {
            int levelSize = queue.size();
            // 处理当前层级的所有单词
            for (int i = 0; i < levelSize; i++) {
                String current = queue.poll();
                // 获取所有可能转换的邻居单词
                for (String neighbor : getNeighbors(current, wordSet)) {
                    // 处理新发现的邻居
                    processNeighbor(neighbor, current, endWord, distances, predecessors, queue);
                    // 检查是否找到结束词
                    if (neighbor.equals(endWord)) {
                        foundEndWord = true;
                    }
                }
            }
        }
        
        // 如果找到了结束词，则回溯所有路径
        if (foundEndWord) {
            List<String> path = new ArrayList<>();
            path.add(endWord);
            // DFS回溯构建所有最短路径
            backtrackPaths(endWord, beginWord, path, predecessors, distances, result);
        }
        
        return result;
    }

    // 初始化数据结构
    private void initializeDataStructures(String beginWord, Set<String> wordSet, 
                                         Map<String, List<String>> predecessors, 
                                         Map<String, Integer> distances, 
                                         Queue<String> queue) {
        // 设置起始词的距离为0
        distances.put(beginWord, 0);
        queue.add(beginWord);
        
        // 初始化所有单词的前驱列表
        for (String word : wordSet) {
            predecessors.put(word, new ArrayList<>());
        }
        predecessors.put(beginWord, new ArrayList<>());
    }

    // 处理邻居单词（核心BFS逻辑）
    private void processNeighbor(String neighbor, String current, String endWord,
                                Map<String, Integer> distances, 
                                Map<String, List<String>> predecessors, 
                                Queue<String> queue) {
        // 当前单词的距离（层级）
        int currentDistance = distances.get(current);
        
        if (!distances.containsKey(neighbor)) {
            // 首次发现该邻居
            distances.put(neighbor, currentDistance + 1);
            predecessors.get(neighbor).add(current);
            queue.add(neighbor);
        } else if (distances.get(neighbor) == currentDistance + 1) {
            // 不同路径在同一层级访问到同一单词
            predecessors.get(neighbor).add(current);
        }
    }

    // DFS回溯所有路径
    private void backtrackPaths(String current, String beginWord, 
                               List<String> currentPath, 
                               Map<String, List<String>> predecessors,
                               Map<String, Integer> distances,
                               List<List<String>> result) {
        // 回溯到起始词，完成一条路径
        if (current.equals(beginWord)) {
            result.add(new ArrayList<>(currentPath));
            return;
        }
        
        // 遍历所有可能的前驱单词
        for (String predecessor : predecessors.get(current)) {
            // 检查是否是有效前驱（层级差为1）
            if (distances.get(current) - 1 == distances.get(predecessor)) {
                // 添加到路径开头（保持顺序）
                currentPath.add(0, predecessor);
                // 继续回溯
                backtrackPaths(predecessor, beginWord, currentPath, predecessors, distances, result);
                // 回溯后移除
                currentPath.remove(0);
            }
        }
    }

    // 生成所有有效邻居单词（优化算法效率的关键）
    private List<String> getNeighbors(String word, Set<String> wordSet) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();
        
        // 遍历单词的每个字符位置
        for (int i = 0; i < word.length(); i++) {
            char originalChar = chars[i];
            // 尝试所有可能的字母替换
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == originalChar) continue; // 跳过原字母
                
                chars[i] = c;
                String newWord = new String(chars);
                
                // 检查新单词是否有效
                if (wordSet.contains(newWord)) {
                    neighbors.add(newWord);
                }
            }
            // 恢复原始字符
            chars[i] = originalChar;
        }
        
        return neighbors;
    }
    
    /**
     * 测试方法
     * 
     * <p><b>测试案例1</b>:
     * Input: 
     *   beginWord = "hit"
     *   endWord = "cog"
     *   wordList = ["hot","dot","dog","lot","log","cog"]
     * 
     * Output: 
     *   [
     *     ["hit","hot","dot","dog","cog"],
     *     ["hit","hot","lot","log","cog"]
     *   ]
     * 
     * <p><b>测试案例2</b>:
     * Input: 
     *   beginWord = "red"
     *   endWord = "tax"
     *   wordList = ["ted","tex","red","tax","tad","den","rex","pee"]
     * 
     * Output:
     *   [
     *     ["red","ted","tad","tax"],
     *     ["red","ted","tex","tax"],
     *     ["red","rex","tex","tax"]
     *   ]
     */
    public static void main(String[] args) {
        WordLadderII solution = new WordLadderII();
        
        // 测试案例1
        System.out.println("测试案例1:");
        String begin1 = "hit";
        String end1 = "cog";
        List<String> wordList1 = Arrays.asList("hot", "dot", "dog", "lot", "log", "cog");
        testAndPrint(solution, begin1, end1, wordList1);
        
        // 测试案例2
        System.out.println("\n测试案例2:");
        String begin2 = "red";
        String end2 = "tax";
        List<String> wordList2 = Arrays.asList("ted", "tex", "red", "tax", "tad", "den", "rex", "pee");
        testAndPrint(solution, begin2, end2, wordList2);
    }
    
    // 测试辅助方法
    private static void testAndPrint(WordLadderII solution, String begin, String end, List<String> wordList) {
        List<List<String>> paths = solution.findLadders(begin, end, wordList);
        
        System.out.println("从 '" + begin + "' 到 '" + end + "' 的最短路径:");
        if (paths.isEmpty()) {
            System.out.println("未找到路径");
        } else {
            for (List<String> path : paths) {
                System.out.println(String.join(" → ", path));
            }
        }
        System.out.println("找到 " + paths.size() + " 条路径");
    }
}