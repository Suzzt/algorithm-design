package io.github.suzzt;

/**
 * 模板方法模式（Template Method Pattern）实现：文档处理流程
 * 
 * <p><b>模式类型：</b>行为型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *     定义一个操作中的算法骨架，将某些步骤延迟到子类中实现。模板方法使得子类可以不改变算法的结构即可
 *     重新定义该算法的某些特定步骤。该模式是实现"好莱坞原则"（"别调用我们，我们会调用你"）的经典方式。
 * </p>
 * 
 * <p><b>模式解决的问题：</b>
 *     当多个类有相似的算法流程，但某些步骤的具体实现不同时，模板方法模式可以避免代码重复。它通过将不变
 *     的行为移动到父类中，去除子类中的重复代码，同时允许子类实现特定步骤的个性化行为。
 * </p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 一次性实现算法的不变部分，将可变行为留给子类实现</li>
 *   <li>✅ 多个类有相同的方法流程，但部分步骤实现不同</li>
 *   <li>✅ 控制子类扩展点，避免子类改变整体流程结构</li>
 *   <li>✅ 需要在不改变算法结构的情况下重新定义算法某些步骤</li>
 *   <li>❌ 每个子类都需要完全不同的算法流程</li>
 * </ul>
 * 
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>⭐ 代码复用：将公共代码移到父类，避免重复</li>
 *   <li>⭐ 流程控制：父类控制整体流程，防止子类破坏流程</li>
 *   <li>⭐ 扩展性：通过钩子方法提供额外扩展点</li>
 *   <li>⭐ 开闭原则：新增实现只需添加子类，不修改父类</li>
 * </ul>
 * 
 * <p><b>关键组成部分：</b></p>
 * <ol>
 *   <li><b>抽象模板类</b>: 定义算法骨架和抽象方法</li>
 *   <li><b>具体实现类</b>: 实现父类中定义的可变方法</li>
 *   <li><b>模板方法</b>: 定义算法骨架的方法（通常为final）</li>
 *   <li><b>抽象方法</b>: 子类必须实现的具体步骤</li>
 *   <li><b>钩子方法</b>: 提供可选扩展点（默认空实现）</li>
 * </ol>
 * 
 * <p><b>好莱坞原则：</b>
 *     高层组件调用低层组件，低层组件从不调用高层组件。在模板方法中，父类控制整体流程，在需要时调用子类方法。
 * </p>
 * 
 * <p><b>典型应用：</b></p>
 * <ul>
 *   <li>Java I/O中的InputStream和OutputStream</li>
 *   <li>Servlet中的HttpServlet类</li>
 *   <li>Spring框架中的JdbcTemplate</li>
 *   <li>Android中的Activity生命周期管理</li>
 *   <li>游戏开发中的游戏主循环</li>
 * </ul>
 */

public class TemplateMethodPattern {

    // ====================== 抽象模板类 ======================
    
    /**
     * 文档处理器（抽象模板类）
     * 定义文档处理的标准流程
     */
    abstract static class DocumentProcessor {
        
        /**
         * 模板方法：定义文档处理流程（final防止子类覆盖）
         */
        public final void processDocument() {
            // 1. 打开文档
            openDocument();
            
            // 2. 验证文档格式（可选钩子）
            if (shouldValidate()) {
                validateDocument();
            }
            
            // 3. 读取文档内容
            String content = readContent();
            
            // 4. 处理文档内容（由子类实现）
            String processedContent = processContent(content);
            
            // 5. 保存处理结果
            saveDocument(processedContent);
            
            // 6. 关闭文档
            closeDocument();
            
            // 7. 后续处理（钩子）
            postProcessing();
        }
        
        // 具体步骤：打开文档（默认实现）
        private void openDocument() {
            System.out.println("📄 打开文档...");
        }
        
        // 钩子方法：验证文档（默认不执行）
        protected boolean shouldValidate() {
            return false;
        }
        
        // 钩子方法：验证文档（默认空实现）
        protected void validateDocument() {
            // 默认不执行任何操作
        }
        
        // 抽象方法：读取内容（必须由子类实现）
        protected abstract String readContent();
        
        // 抽象方法：处理内容（必须由子类实现）
        protected abstract String processContent(String content);
        
        // 具体步骤：保存文档（默认实现）
        private void saveDocument(String content) {
            System.out.println("💾 保存文档内容: " + content.substring(0, Math.min(20, content.length())) + "...");
        }
        
        // 具体步骤：关闭文档（默认实现）
        private void closeDocument() {
            System.out.println("🔒 关闭文档");
        }
        
        // 钩子方法：后续处理（默认空实现）
        protected void postProcessing() {
            // 默认不执行任何操作
        }
    }

    // ====================== 具体实现类 ======================
    
    /**
     * PDF文档处理器（具体实现类）
     */
    static class PdfProcessor extends DocumentProcessor {
        
        // 覆盖钩子方法：启用文档验证
        @Override
        protected boolean shouldValidate() {
            return true;
        }
        
        // 实现验证逻辑
        @Override
        protected void validateDocument() {
            System.out.println("🔍 验证PDF文档: 检查文件完整性...");
            System.out.println("    通过: PDF/A标准兼容验证");
        }
        
        @Override
        protected String readContent() {
            System.out.println("📖 读取PDF内容: 提取文本和图像元数据...");
            return "PDF文档内容: 这是从PDF中提取的文本内容";
        }
        
        @Override
        protected String processContent(String content) {
            System.out.println("⚙️ 处理PDF内容: 优化版式、压缩图像...");
            return "优化后的PDF内容";
        }
        
        // 覆盖钩子方法：添加水印作为后续处理
        @Override
        protected void postProcessing() {
            System.out.println("💧 PDF后续处理: 添加数字水印");
        }
    }
    
    /**
     * Word文档处理器（具体实现类）
     */
    static class WordProcessor extends DocumentProcessor {
        @Override
        protected String readContent() {
            System.out.println("📖 读取Word内容: 解析.docx格式...");
            return "Word文档内容: 这是从Word中提取的文本和格式";
        }
        
        @Override
        protected String processContent(String content) {
            System.out.println("⚙️ 处理Word内容: 清理格式、修复损坏链接...");
            return "标准化后的Word内容";
        }
        
        // 覆盖钩子方法：添加文件属性更新
        @Override
        protected void postProcessing() {
            System.out.println("🏷️ Word后续处理: 更新文档属性");
        }
    }
    
    /**
     * Markdown文档处理器（具体实现类）
     */
    static class MarkdownProcessor extends DocumentProcessor {
        // 覆盖钩子方法：启用文档验证
        @Override
        protected boolean shouldValidate() {
            return true;
        }
        
        // 实现验证逻辑
        @Override
        protected void validateDocument() {
            System.out.println("🔍 验证Markdown: 检查语法有效性...");
            System.out.println("    警告: 发现3处未关闭的代码块");
        }
        
        @Override
        protected String readContent() {
            System.out.println("📖 读取Markdown内容: 解析.md文件...");
            return "Markdown内容: # 标题\n正文内容...";
        }
        
        @Override
        protected String processContent(String content) {
            System.out.println("⚙️ 处理Markdown内容: 转换HTML标签、解析引用...");
            return "<h1>标题</h1><p>正文内容...</p>";
        }
        
        // 覆盖钩子方法：添加文件属性更新
        @Override
        protected void postProcessing() {
            System.out.println("📊 Markdown后续处理: 生成目录索引");
        }
    }

    // ====================== 客户端代码 ======================
    
    public static void main(String[] args) {
        System.out.println("================ 文档处理系统 ================");
        
        // 创建处理器实例
        DocumentProcessor pdfProcessor = new PdfProcessor();
        DocumentProcessor wordProcessor = new WordProcessor();
        DocumentProcessor markdownProcessor = new MarkdownProcessor();
        
        // 处理PDF文档
        System.out.println("\n>>> 处理PDF文档 <<<");
        pdfProcessor.processDocument();
        
        System.out.println("\n" + new String(new char[50]).replace("\0", "-"));

        // 处理Word文档
        System.out.println("\n>>> 处理Word文档 <<<");
        wordProcessor.processDocument();

        System.out.println("\n" + new String(new char[50]).replace("\0", "-"));
        
        // 处理Markdown文档
        System.out.println("\n>>> 处理Markdown文档 <<<");
        markdownProcessor.processDocument();
        
        // ====================== 模式优势总结 ======================
        System.out.println("\n" + new String(new char[60]).replace("\0", "-"));
        System.out.println("模板方法模式优势总结：");
        System.out.println("1. 流程一致性: 所有文档处理都遵循相同流程（打开->读取->处理->保存->关闭）");
        System.out.println("2. 代码复用: 公共步骤（打开/保存/关闭）在父类实现，避免重复");
        System.out.println("3. 扩展控制: 通过抽象方法约束子类必须实现特定步骤");
        System.out.println("4. 灵活性: 通过钩子方法（shouldValidate/postProcessing）提供额外扩展点");
        System.out.println("5. 流程保护: processDocument()方法为final，防止子类破坏流程");
        
        // ====================== 模式扩展演示 ======================
        System.out.println("\n扩展演示：添加HTML处理");
        
        // 创建HTML处理器
        DocumentProcessor htmlProcessor = new DocumentProcessor() {
            @Override
            protected String readContent() {
                System.out.println("📖 读取HTML内容: 解析DOM树...");
                return "<html><body>网页内容</body></html>";
            }
            
            @Override
            protected String processContent(String content) {
                System.out.println("⚙️ 处理HTML内容: 压缩代码、内联CSS...");
                return "<html><body style=\"margin:0\">优化后的网页内容</body></html>";
            }
        };
        
        // 处理HTML文档
        System.out.println("\n>>> 处理HTML文档 <<<");
        htmlProcessor.processDocument();
        
        System.out.println("\n说明：添加HTML处理只需实现特定方法，无需修改流程");
    }
}