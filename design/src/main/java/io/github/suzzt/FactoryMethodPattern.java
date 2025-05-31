package io.github.suzzt;

/**
 * 工厂方法模式（Factory Method Pattern）
 * 
 * <p><b>模式类型：</b>创建型设计模式</p>
 * 
 * <p><b>核心思想：</b>定义一个创建对象的接口，但让子类决定实例化哪一个类。工厂方法使一个类的实例化延迟到其子类。</p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 需要避免客户端代码与具体产品类耦合</li>
 *   <li>✅ 需要在运行时决定创建哪个产品对象</li>
 *   <li>✅ 需要为不同环境提供不同产品实现</li>
 *   <li>✅ 需要支持产品扩展而不修改客户端代码</li>
 *   <li>❌ 创建逻辑简单且无需变化时（可直接使用new）</li>
 *   <li>❌ 系统需要大量产品种类且变化频繁时（考虑抽象工厂）</li>
 * </ul>
 * 
 * <p><b>主要组件：</b></p>
 * <table border="1">
 *   <tr><th>组件</th><th>角色</th><th>示例</th></tr>
 *   <tr><td>Product</td><td>产品接口</td><td>Document</td></tr>
 *   <tr><td>ConcreteProduct</td><td>具体产品实现</td><td>PdfDocument, WordDocument</td></tr>
 *   <tr><td>Creator</td><td>工厂方法声明</td><td>Application</td></tr>
 *   <tr><td>ConcreteCreator</td><td>具体工厂实现</td><td>WordApplication, PdfApplication</td></tr>
 * </table>
 * 
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>⭐ 解耦调用者与具体产品实现</li>
 *   <li>⭐ 支持"开闭原则"（扩展新产品时无需修改现有代码）</li>
 *   <li>⭐ 易于集成依赖注入框架</li>
 *   <li>⭐ 允许在运行时更改应用行为</li>
 * </ul>
 * 
 * <p><b>实现技巧：</b></p>
 * <ol>
 *   <li>优先使用抽象类型声明而非具体实现</li>
 *   <li>利用多态机制动态决定产品类型</li>
 *   <li>考虑使用泛型增强类型安全</li>
 *   <li>可通过静态工厂方法简化创建过程</li>
 * </ol>
 */
public class FactoryMethodPattern {

    // 1. 抽象产品接口 - Document
    interface Document {
        void open();
        void save();
        void close();
    }
    
    // 2.1 具体产品实现 - PDF文档
    static class PdfDocument implements Document {
        @Override
        public void open() {
            System.out.println("正在打开PDF文档...");
            System.out.println("显示PDF工具栏");
        }
        
        @Override
        public void save() {
            System.out.println("保存为PDF格式，支持压缩和加密");
        }
        
        @Override
        public void close() {
            System.out.println("关闭PDF文档，释放资源");
        }
        
        // PDF特有功能
        public void addDigitalSignature() {
            System.out.println("添加数字签名到PDF");
        }
    }
    
    // 2.2 具体产品实现 - Word文档
    static class WordDocument implements Document {
        @Override
        public void open() {
            System.out.println("正在打开Word文档...");
            System.out.println("显示Word编辑器");
        }
        
        @Override
        public void save() {
            System.out.println("保存为DOCX格式，包含版本历史");
        }
        
        @Override
        public void close() {
            System.out.println("关闭Word文档，自动保存恢复文件");
        }
        
        // Word特有功能
        public void checkGrammar() {
            System.out.println("进行语法和拼写检查");
        }
    }
    
    // 3.1 抽象创建者 - Application
    static abstract class Application {
        // 工厂方法 - 由子类实现具体逻辑
        public abstract Document createDocument();
        
        // 文档处理流程（标准操作）
        public void processDocument() {
            Document doc = createDocument(); // 调用工厂方法
            doc.open();
            
            // 业务逻辑...
            System.out.println("用户编辑文档中...");
            
            doc.save();
            doc.close();
        }
    }
    
    // 3.2 具体创建者 - PDF应用
    static class PdfApplication extends Application {
        @Override
        public Document createDocument() {
            System.out.println("PDF应用创建PDF文档实例");
            PdfDocument doc = new PdfDocument();
            doc.addDigitalSignature(); // 添加PDF特有功能
            return doc;
        }
    }
    
    // 3.3 具体创建者 - Word应用
    static class WordApplication extends Application {
        @Override
        public Document createDocument() {
            System.out.println("Word应用创建Word文档实例");
            WordDocument doc = new WordDocument();
            doc.checkGrammar(); // 添加Word特有功能
            return doc;
        }
    }
    
    // 4. 客户端代码
    public static void main(String[] args) {
        System.out.println("=== 工厂方法模式演示 ===");
        
        // 基于用户选择创建不同应用
        System.out.println("\n创建PDF应用...");
        Application pdfApp = new PdfApplication();
        pdfApp.processDocument();
        
        System.out.println("\n创建Word应用...");
        Application wordApp = new WordApplication();
        wordApp.processDocument();
        
        // 运行时切换应用类型
        System.out.println("\n动态切换应用类型...");
        Application currentApp = createApplicationByOS();
        currentApp.processDocument();
    }
    
    // 根据操作系统选择应用类型（模拟策略）
    private static Application createApplicationByOS() {
        String os = System.getProperty("os.name", "").toLowerCase();
        
        if (os.contains("mac")) {
            System.out.println("检测到macOS系统，使用PDF应用");
            return new PdfApplication();
        } else {
            System.out.println("检测到Windows系统，使用Word应用");
            return new WordApplication();
        }
    }
}