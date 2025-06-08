package io.github.suzzt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 访问者模式（Visitor Pattern）实现：编译器抽象语法树处理（Java 8 兼容版）
 *
 * <p><b>模式类型：</b>行为型设计模式</p>
 *
 * <p><b>核心思想：</b>
 *     将算法与其作用的对象分离，通过定义新的访问者来扩展功能而无需修改已有类结构。
 *     访问者模式允许在不修改类的前提下为类添加新的操作，适用于结构稳定的复杂对象结构。
 * </p>
 *
 * <p><b>模式解决的问题：</b>
 *     当一个复杂对象结构包含多种类型的元素，且需要对这些元素进行多种不相关的操作时，
 *     若将所有操作都直接添加在元素类中会导致类过于庞大，访问者模式通过双分派机制
 *     将操作封装到访问者中，实现操作与对象结构的解耦。
 * </p>
 */
public class VisitorPattern {

    // ====================== 访问者接口 ======================

    /**
     * AST节点访问者接口
     */
    interface ASTVisitor {
        void visit(AssignmentNode node);
        void visit(ExpressionNode node);
        void visit(VariableNode node);
        void visit(PrintNode node);
        void visit(IfNode node);
        void visit(LoopNode node);
        void visit(FunctionNode node);
    }

    // ====================== 具体访问者 ======================

    /**
     * 语义检查访问者
     */
    static class SemanticChecker implements ASTVisitor {
        @Override
        public void visit(AssignmentNode node) {
            System.out.printf("[语义检查] 赋值语句: %s = %s\n",
                    node.getVariableName(), node.getExpression());
        }

        @Override
        public void visit(ExpressionNode node) {
            System.out.printf("[语义检查] 表达式: %s\n", node.getExpression());
        }

        @Override
        public void visit(VariableNode node) {
            System.out.printf("[语义检查] 变量使用: %s\n", node.getVariableName());
        }

        @Override
        public void visit(PrintNode node) {
            System.out.printf("[语义检查] 打印语句: %s\n", node.getExpression());
        }

        @Override
        public void visit(IfNode node) {
            System.out.printf("[语义检查] 条件语句: if (%s)\n", node.getCondition());
        }

        @Override
        public void visit(LoopNode node) {
            System.out.printf("[语义检查] 循环语句: while (%s)\n", node.getCondition());
        }

        @Override
        public void visit(FunctionNode node) {
            System.out.printf("[语义检查] 函数定义: %s(%s)\n",
                    node.getFunctionName(),
                    String.join(", ", node.getParameters()));
        }
    }

    /**
     * 代码生成访问者
     */
    static class CodeGenerator implements ASTVisitor {
        private int indentLevel = 0;

        // Java 8兼容的缩进生成方法
        private String getIndent() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < indentLevel; i++) {
                sb.append("  ");
            }
            return sb.toString();
        }

        @Override
        public void visit(AssignmentNode node) {
            System.out.printf("%s%s = %s;\n",
                    getIndent(), node.getVariableName(), generateExpression(node.getExpression()));
        }

        private String generateExpression(ExpressionNode expr) {
            // 简化的表达式生成
            return expr.getExpression().replace(" * ", "*")
                    .replace(" / ", "/")
                    .replace(" + ", "+")
                    .replace(" - ", "-");
        }

        @Override
        public void visit(ExpressionNode node) {
            // 表达式节点本身不会被独立生成代码
        }

        @Override
        public void visit(VariableNode node) {
            // 变量节点本身不会被独立生成代码
        }

        @Override
        public void visit(PrintNode node) {
            System.out.printf("%sprint(%s);\n",
                    getIndent(), generateExpression(node.getExpression()));
        }

        @Override
        public void visit(IfNode node) {
            System.out.printf("%sif (%s) {\n",
                    getIndent(), generateExpression(node.getCondition()));
            indentLevel++;
            for (ASTNode stmt : node.getBody()) {
                stmt.accept(this);
            }
            indentLevel--;
            System.out.printf("%s}\n", getIndent());
        }

        @Override
        public void visit(LoopNode node) {
            System.out.printf("%swhile (%s) {\n",
                    getIndent(), generateExpression(node.getCondition()));
            indentLevel++;
            for (ASTNode stmt : node.getBody()) {
                stmt.accept(this);
            }
            indentLevel--;
            System.out.printf("%s}\n", getIndent());
        }

        @Override
        public void visit(FunctionNode node) {
            System.out.printf("%sfunction %s(%s) {\n",
                    getIndent(), node.getFunctionName(), String.join(", ", node.getParameters()));
            indentLevel++;
            for (ASTNode stmt : node.getBody()) {
                stmt.accept(this);
            }
            indentLevel--;
            System.out.printf("%s}\n", getIndent());
        }
    }

    // ====================== 元素接口 ======================

    /**
     * AST节点接口（元素）
     */
    interface ASTNode {
        void accept(ASTVisitor visitor);
        int getLine();
        int getColumn();
    }

    // ====================== 具体元素 ======================

    /**
     * 变量节点
     */
    static class VariableNode implements ASTNode {
        private final String variableName;
        private final int line;
        private final int column;

        public VariableNode(String variableName, int line, int column) {
            this.variableName = variableName;
            this.line = line;
            this.column = column;
        }

        public String getVariableName() {
            return variableName;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * 表达式节点
     */
    static class ExpressionNode implements ASTNode {
        private final String expression;
        private final int line;
        private final int column;

        public ExpressionNode(String expression, int line, int column) {
            this.expression = expression;
            this.line = line;
            this.column = column;
        }

        public String getExpression() {
            return expression;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * 赋值语句节点
     */
    static class AssignmentNode implements ASTNode {
        private final VariableNode variable;
        private final ExpressionNode expression;
        private final int line;
        private final int column;

        public AssignmentNode(VariableNode variable, ExpressionNode expression, int line, int column) {
            this.variable = variable;
            this.expression = expression;
            this.line = line;
            this.column = column;
        }

        public String getVariableName() {
            return variable.getVariableName();
        }

        public ExpressionNode getExpression() {
            return expression;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * 打印语句节点
     */
    static class PrintNode implements ASTNode {
        private final ExpressionNode expression;
        private final int line;
        private final int column;

        public PrintNode(ExpressionNode expression, int line, int column) {
            this.expression = expression;
            this.line = line;
            this.column = column;
        }

        public ExpressionNode getExpression() {
            return expression;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * 条件语句节点
     */
    static class IfNode implements ASTNode {
        private final ExpressionNode condition;
        private final List<ASTNode> body;
        private final int line;
        private final int column;

        public IfNode(ExpressionNode condition, List<ASTNode> body, int line, int column) {
            this.condition = condition;
            this.body = new ArrayList<>(body);
            this.line = line;
            this.column = column;
        }

        public ExpressionNode getCondition() {
            return condition;
        }

        public List<ASTNode> getBody() {
            return body;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
            for (ASTNode node : body) {
                node.accept(visitor);
            }
        }
    }

    /**
     * 循环语句节点
     */
    static class LoopNode implements ASTNode {
        private final ExpressionNode condition;
        private final List<ASTNode> body;
        private final int line;
        private final int column;

        public LoopNode(ExpressionNode condition, List<ASTNode> body, int line, int column) {
            this.condition = condition;
            this.body = new ArrayList<>(body);
            this.line = line;
            this.column = column;
        }

        public ExpressionNode getCondition() {
            return condition;
        }

        public List<ASTNode> getBody() {
            return body;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
            for (ASTNode node : body) {
                node.accept(visitor);
            }
        }
    }

    /**
     * 函数定义节点
     */
    static class FunctionNode implements ASTNode {
        private final String functionName;
        private final List<String> parameters;
        private final List<ASTNode> body;
        private final int line;
        private final int column;

        public FunctionNode(String functionName, List<String> parameters, List<ASTNode> body, int line, int column) {
            this.functionName = functionName;
            this.parameters = new ArrayList<>(parameters);
            this.body = new ArrayList<>(body);
            this.line = line;
            this.column = column;
        }

        public String getFunctionName() {
            return functionName;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public List<ASTNode> getBody() {
            return body;
        }

        @Override
        public int getLine() {
            return line;
        }

        @Override
        public int getColumn() {
            return column;
        }

        @Override
        public void accept(ASTVisitor visitor) {
            visitor.visit(this);
            for (ASTNode node : body) {
                node.accept(visitor);
            }
        }
    }

    // ====================== 对象结构 ======================

    /**
     * AST（抽象语法树）结构
     */
    static class ASTStructure {
        private final List<ASTNode> nodes = new ArrayList<>();

        public void addNode(ASTNode node) {
            nodes.add(node);
        }

        public void accept(ASTVisitor visitor) {
            for (ASTNode node : nodes) {
                node.accept(visitor);
            }
        }
    }

    // ====================== 客户端代码 ======================

    public static void main(String[] args) {
        System.out.println("=========== 编译器抽象语法树系统 ===========");

        // 1. 构建AST
        ASTStructure ast = new ASTStructure();

        // 添加一个函数定义
        List<ASTNode> factorialBody = new ArrayList<>();

        // 添加赋值语句
        factorialBody.add(new AssignmentNode(
                new VariableNode("result", 6, 7),
                new ExpressionNode("1", 6, 15),
                6, 3
        ));

        // 添加if语句
        List<ASTNode> ifBody = new ArrayList<>();
        ifBody.add(new AssignmentNode(
                new VariableNode("result", 6, 7),
                new ExpressionNode("1", 6, 15),
                6, 3
        ));

        factorialBody.add(new IfNode(
                new ExpressionNode("n <= 1", 5, 5),
                ifBody,
                5, 3
        ));

        factorialBody.add(new AssignmentNode(
                new VariableNode("result", 8, 7),
                new ExpressionNode("n * factorial(n - 1)", 8, 15),
                8, 3
        ));
        factorialBody.add(new PrintNode(
                new ExpressionNode("\"Result: \" + result", 9, 9),
                9, 3
        ));

        ast.addNode(new FunctionNode(
                "factorial",
                Arrays.asList("n"),
                factorialBody,
                4, 1
        ));

        // 添加主程序
        List<ASTNode> mainProgram = new ArrayList<>();
        mainProgram.add(new AssignmentNode(
                new VariableNode("num", 12, 5),
                new ExpressionNode("5", 12, 11),
                12, 1
        ));
        mainProgram.add(new AssignmentNode(
                new VariableNode("result", 13, 5),
                new ExpressionNode("factorial(num)", 13, 13),
                13, 1
        ));
        mainProgram.add(new PrintNode(
                new ExpressionNode("\"Factorial of \" + num + \" is \" + result", 14, 9),
                14, 1
        ));

        ast.addNode(new FunctionNode(
                "main",
                new ArrayList<>(),
                mainProgram,
                11, 1
        ));

        // 2. 使用不同访问者处理AST

        // 执行语义检查
        System.out.println("\n>>> 语义检查 <<<");
        ast.accept(new SemanticChecker());

        System.out.println("\n>>> 代码生成 (伪代码) <<<");
        ast.accept(new CodeGenerator());

        // ====================== 模式优势演示 ======================
        String separator = String.join("", Collections.nCopies(70, "="));
        System.out.println("\n" + separator);
        System.out.println("访问者模式优势总结：");
        System.out.println("1. 开闭原则: 添加新操作无需修改AST节点类");
        System.out.println("2. 单一职责: 每个访问者专注于一种操作");
        System.out.println("3. 算法聚合: 相关操作集中在一个地方");

        // ====================== 模式扩展演示 ======================
        System.out.println("\n扩展演示：添加新操作");

        // 创建一个新的访问者：调试信息访问者
        ASTVisitor debugVisitor = new DebugVisitor();

        System.out.println("\n>>> 调试信息 <<<");
        ast.accept(debugVisitor);
    }

    /**
     * 调试信息访问者（演示扩展）
     */
    static class DebugVisitor implements ASTVisitor {
        @Override
        public void visit(AssignmentNode node) {
            System.out.printf("[调试] 赋值节点 [行号:%d, 列号:%d]\n",
                    node.getLine(), node.getColumn());
        }

        @Override
        public void visit(ExpressionNode node) {
            System.out.printf("[调试] 表达式节点 [行号:%d, 列号:%d]\n",
                    node.getLine(), node.getColumn());
        }

        @Override
        public void visit(VariableNode node) {
            System.out.printf("[调试] 变量节点 '%s' [行号:%d, 列号:%d]\n",
                    node.getVariableName(), node.getLine(), node.getColumn());
        }

        @Override
        public void visit(PrintNode node) {
            System.out.printf("[调试] 打印节点 [行号:%d, 列号:%d]\n",
                    node.getLine(), node.getColumn());
        }

        @Override
        public void visit(IfNode node) {
            System.out.printf("[调试] 条件节点 [行号:%d, 列号:%d]\n",
                    node.getLine(), node.getColumn());
        }

        @Override
        public void visit(LoopNode node) {
            System.out.printf("[调试] 循环节点 [行号:%d, 列号:%d]\n",
                    node.getLine(), node.getColumn());
        }

        @Override
        public void visit(FunctionNode node) {
            System.out.printf("[调试] 函数节点 '%s' [行号:%d, 列号:%d]\n",
                    node.getFunctionName(), node.getLine(), node.getColumn());
        }
    }
}