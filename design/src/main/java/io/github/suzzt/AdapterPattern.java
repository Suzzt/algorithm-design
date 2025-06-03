package io.github.suzzt;

/**
 * 适配器模式（Adapter Pattern）实现：连接不兼容接口的桥梁
 * 
 * <p><b>模式类型：</b>结构型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *   作为<u>接口转换器</u>，允许原本接口不兼容的类可以协同工作。适配器模式通过包装对象的方式，
 *   将目标接口转换为客户端所期望的接口，实现不同系统或组件之间的无缝对接。
 * </p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 需要使用现有类，但其接口与系统不匹配</li>
 *   <li>✅ 需要集成多个第三方库但接口差异大</li>
 *   <li>✅ 系统需复用旧系统功能但不想修改原有代码</li>
 *   <li>✅ 为不同数据格式提供统一接口（如JSON/XML适配）</li>
 *   <li>❌ 接口设计初期应避免使用（提前规划更好）</li>
 * </ul>
 * 
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>⭐ <b>解耦性</b>：分离接口定义与具体实现</li>
 *   <li>⭐ <b>复用性</b>：复用已有功能避免重写</li>
 *   <li>⭐ <b>灵活性</b>：增加适配器即可支持新接口</li>
 *   <li>⭐ <b>开闭原则</b>：不修改现有代码扩展功能</li>
 * </ul>
 * 
 * <p><b>经典应用案例：</b></p>
 * <ul>
 *   <li>JDK I/O：InputStreamReader适配字节流到字符流</li>
 *   <li>Spring MVC：HandlerAdapter适配各种控制器接口</li>
 *   <li>日志框架：适配不同日志API（SLF4J）</li>
 * </ul>
 */
public class AdapterPattern {

    // ===================== 1. 目标接口（客户端期望） =====================
    /**
     * 文件阅读器接口（目标接口）
     * <p>客户端期望的统一文件操作接口</p>
     */
    interface FileReader {
        void openFile(String path);
        void readContent();
        void closeFile();
    }

    // ===================== 2. 适配者类（已有但不兼容） =====================
    
    /**
     * JSON解析库（适配者1）
     * <p>已有但不兼容FileReader接口的第三方库</p>
     */
    static class JsonParser {
        public void loadJsonFile(String filePath) {
            System.out.println("加载JSON文件: " + filePath);
        }
        
        public void parseJsonData() {
            System.out.println("解析JSON数据结构");
        }
        
        public void releaseJsonResources() {
            System.out.println("释放JSON解析资源");
        }
    }

    /**
     * XML解析库（适配者2）
     * <p>已有但不兼容FileReader接口的第三方库</p>
     */
    static class XmlReader {
        public void openXmlDocument(String path) {
            System.out.println("打开XML文档: " + path);
        }
        
        public void traverseXmlNodes() {
            System.out.println("遍历XML节点树");
        }
        
        public void closeXmlDocument() {
            System.out.println("关闭XML文档句柄");
        }
    }

    // ===================== 3. 适配器实现 =====================
    
    /**
     * JSON适配器（对象适配器）
     * <p>包装JsonParser并实现目标FileReader接口</p>
     */
    static class JsonFileAdapter implements FileReader {
        private final JsonParser jsonParser;
        
        public JsonFileAdapter(JsonParser jsonParser) {
            this.jsonParser = jsonParser;
        }
        
        @Override
        public void openFile(String path) {
            jsonParser.loadJsonFile(path);
        }
        
        @Override
        public void readContent() {
            jsonParser.parseJsonData();
        }
        
        @Override
        public void closeFile() {
            jsonParser.releaseJsonResources();
        }
    }
    
    /**
     * XML适配器（类适配器）
     * <p>继承XmlReader并实现目标FileReader接口</p>
     */
    static class XmlFileAdapter extends XmlReader implements FileReader {
        @Override
        public void openFile(String path) {
            openXmlDocument(path);
        }
        
        @Override
        public void readContent() {
            traverseXmlNodes();
        }
        
        @Override
        public void closeFile() {
            closeXmlDocument();
        }
    }
    
    /**
     * 复合适配器（支持多种格式）
     * <p>根据文件类型自动选择适配器</p>
     */
    static class UniversalFileAdapter implements FileReader {
        private final FileReader adaptedReader;
        
        public UniversalFileAdapter(String filePath) {
            if (filePath.endsWith(".json")) {
                this.adaptedReader = new JsonFileAdapter(new JsonParser());
            } else if (filePath.endsWith(".xml")) {
                this.adaptedReader = new XmlFileAdapter();
            } else {
                throw new UnsupportedOperationException("不支持的格式: " + filePath);
            }
        }
        
        @Override
        public void openFile(String path) {
            adaptedReader.openFile(path);
        }
        
        @Override
        public void readContent() {
            adaptedReader.readContent();
        }
        
        @Override
        public void closeFile() {
            adaptedReader.closeFile();
        }
    }
    
    // ===================== 4. 客户端使用 =====================
    public static void main(String[] args) {
        System.out.println("===== 适配器模式演示：统一文件阅读器 =====");
        
        // 场景1：使用JSON适配器
        System.out.println("\n--- 处理JSON文件 ---");
        FileReader jsonReader = new JsonFileAdapter(new JsonParser());
        processFile(jsonReader, "config.json");
        
        // 场景2：使用XML适配器
        System.out.println("\n--- 处理XML文件 ---");
        FileReader xmlReader = new XmlFileAdapter();
        processFile(xmlReader, "data.xml");
        
        // 场景3：使用复合适配器
        System.out.println("\n--- 自动适配处理 ---");
        processFile(new UniversalFileAdapter("settings.json"), "settings.json");
        processFile(new UniversalFileAdapter("template.xml"), "template.xml");
        
        // 场景4：测试不支持格式
        try {
            new UniversalFileAdapter("image.jpg").openFile("image.jpg");
        } catch (Exception e) {
            System.out.println("\n错误测试: " + e.getMessage());
        }
    }
    
    /**
     * 统一文件处理流程（客户端代码）
     * 
     * <p><b>设计亮点：</b>
     *   客户端仅依赖FileReader接口，无需知道具体实现
     * </p>
     * 
     * @param reader 文件阅读器（通过适配器注入）
     * @param path 文件路径
     */
    private static void processFile(FileReader reader, String path) {
        System.out.println("\n开始处理文件: " + path);
        reader.openFile(path);
        reader.readContent();
        reader.closeFile();
    }
}

/* 执行结果：
===== 适配器模式演示：统一文件阅读器 =====

--- 处理JSON文件 ---

开始处理文件: config.json
加载JSON文件: config.json
解析JSON数据结构
释放JSON解析资源

--- 处理XML文件 ---

开始处理文件: data.xml
打开XML文档: data.xml
遍历XML节点树
关闭XML文档句柄

--- 自动适配处理 ---

开始处理文件: settings.json
加载JSON文件: settings.json
解析JSON数据结构
释放JSON解析资源

开始处理文件: template.xml
打开XML文档: template.xml
遍历XML节点树
关闭XML文档句柄

错误测试: 不支持的格式: image.jpg
*/