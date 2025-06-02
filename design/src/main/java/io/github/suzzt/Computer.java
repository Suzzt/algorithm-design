package io.github.suzzt;

/**
 * 建造者模式（Builder Pattern）实现：复杂对象构造的优雅解决方案
 * 
 * <p><b>模式类型：</b>创建型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *   将复杂对象的<u>构造过程</u>与<u>最终表示</u>分离，使相同的构建过程可以创建不同的表示。
 *   通过一步步组装部件的方式构建最终对象，避免"构造器污染"和"setter泛滥"问题。
 * </p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 对象包含多个部件，且部件组合方式不同</li>
 *   <li>✅ 对象需要大量可配置参数（特别是可选参数）</li>
 *   <li>✅ 需要创建不可变对象（final字段）</li>
 *   <li>✅ 期望创建过程具有类似DSL的流畅接口</li>
 *   <li>❌ 对象结构简单，参数数量较少（直接使用构造函数更合适）</li>
 * </ul>
 * 
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>⭐ 解决可选参数问题：避免构造方法重载爆炸</li>
 *   <li>⭐ 保证对象创建一致性：通过build()方法做参数校验</li>
 *   <li>⭐ 链式调用提升可读性：创建流程清晰直观</li>
 *   <li>⭐ 支持创建不可变对象：避免线程安全问题</li>
 *   <li>⭐ 分离构造逻辑：创建逻辑可在子类改变</li>
 * </ul>
 * 
 * <p><b>经典应用案例：</b></p>
 * <ul>
 *   <li>JDK：StringBuilder、Stream API</li>
 *   <li>Spring：BeanDefinitionBuilder</li>
 *   <li>Lombok：@Builder注解</li>
 * </ul>
 */
public class Computer {
    
    /* ---------- 核心字段定义 (用final确保对象不可变) ---------- */
    private final String cpu;       // 必需参数：CPU型号
    private final String ram;       // 必需参数：内存大小
    private final String mainBoard; // 必需参数：主板型号
    
    private final String gpu;       // 可选参数：显卡型号
    private final String storage;   // 可选参数：存储配置
    private final String monitor;   // 可选参数：显示器信息
    private final String os;        // 可选参数：操作系统
    
    /**
     * 私有构造函数：只允许通过Builder创建实例
     * 
     * @param builder 建造者实例（包含所有配置参数）
     */
    private Computer(Builder builder) {
        // 核心必需组件
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.mainBoard = builder.mainBoard;
        
        // 可选扩展组件
        this.gpu = builder.gpu;
        this.storage = builder.storage;
        this.monitor = builder.monitor;
        this.os = builder.os;
    }

    /* ------------------- Getter方法 ------------------- */
    public String getCpu() { return cpu; }
    public String getRam() { return ram; }
    public String getMainBoard() { return mainBoard; }
    public String getGpu() { return gpu; }
    public String getStorage() { return storage; }
    public String getMonitor() { return monitor; }
    public String getOs() { return os; }

    /**
     * 重写toString输出完整配置信息
     * 
     * <p><b>设计亮点：</b>动态显示存在的配置项，忽略未配置项</p>
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== 电脑配置清单 =======================\n");
        sb.append("CPU型号: ").append(cpu).append("\n");
        sb.append("内存容量: ").append(ram).append("\n");
        sb.append("主板型号: ").append(mainBoard).append("\n");
        
        if (gpu != null) 
            sb.append("显卡配置: ").append(gpu).append("\n");
        if (storage != null) 
            sb.append("存储设备: ").append(storage).append("\n");
        if (monitor != null) 
            sb.append("显示设备: ").append(monitor).append("\n");
        if (os != null) 
            sb.append("操作系统: ").append(os).append("\n");
        
        return sb.toString();
    }

    // ███ 建造者核心：封装构造逻辑 ███████████████████████████████████████
    
    /**
     * 静态建造者类：负责逐步构建Computer对象
     * 
     * <p><b>设计哲学：</b>
     *   1. 分离构造步骤：通过链式方法逐步配置
     *   2. 隐藏构造细节：用户无需了解Computer内部结构
     *   3. 统一校验入口：参数校验集中在build()方法
     * </p>
     */
    public static class Builder {
        // 必需参数：必须在创建Builder时指定
        private final String cpu;
        private final String ram;
        private final String mainBoard;
        
        // 可选参数：提供默认值(可配置)
        private String gpu = null;      // 默认无独立显卡
        private String storage = null;  // 默认无存储
        private String monitor = null;  // 默认无显示器
        private String os = null;       // 默认无操作系统

        /**
         * 建造者构造函数：设置必需参数
         * 
         * @param cpu       必选 CPU型号
         * @param ram       必选 内存规格
         * @param mainBoard 必选 主板型号
         */
        public Builder(String cpu, String ram, String mainBoard) {
            this.cpu = cpu;
            this.ram = ram;
            this.mainBoard = mainBoard;
        }

        /**
         * 配置独立显卡：可选扩展部件
         * 
         * <p><b>链式关键：</b>返回this支持链式调用</p>
         * 
         * @param gpu 显卡型号（如"RTX 4090"）
         * @return 当前建造者实例
         */
        public Builder withGpu(String gpu) {
            this.gpu = gpu;
            return this;
        }

        /**
         * 配置存储设备：可选扩展部件
         * 
         * @param storage 存储描述（如"1TB SSD"）
         * @return 当前建造者实例
         */
        public Builder withStorage(String storage) {
            this.storage = storage;
            return this;
        }

        /**
         * 配置显示器：可选扩展部件
         * 
         * @param monitor 显示器描述（如"27寸4K"）
         * @return 当前建造者实例
         */
        public Builder withMonitor(String monitor) {
            this.monitor = monitor;
            return this;
        }

        /**
         * 配置操作系统：可选软件系统
         * 
         * @param os 操作系统（如"Windows 11"）
         * @return 当前建造者实例
         */
        public Builder withOs(String os) {
            this.os = os;
            return this;
        }

        /**
         * 最终构建方法：创建Computer实例
         * 
         * <p><b>核心作用：</b>
         *   1. 集中进行参数校验
         *   2. 实际创建目标对象
         *   3. 封装对象创建过程
         * </p>
         * 
         * @return 完全构建的Computer实例
         * @throws IllegalArgumentException 如果必需参数非法
         */
        public Computer build() {
            // >>> 参数校验：确保对象有效性 <<<
            if (cpu == null || cpu.isEmpty()) {
                throw new IllegalArgumentException("CPU型号不能为空");
            }
            if (ram == null || ram.isEmpty()) {
                throw new IllegalArgumentException("内存规格不能为空");
            }
            if (mainBoard == null || mainBoard.isEmpty()) {
                throw new IllegalArgumentException("主板型号不能为空");
            }
            
            // 创建并返回完整构建的Computer对象
            return new Computer(this);
        }
    }

    // ███ 客户端使用示例：展示多种配置方式 ███████████████████████████████
    
    public static void main(String[] args) {
        // 场景1：创建基本配置电脑（仅含必需部件）
        Computer basicComputer = new Computer.Builder("Intel i5", "16GB DDR4", "B760M")
                .build();
        System.out.println("【基础办公配置】" + basicComputer);
        
        // 场景2：创建游戏电脑（全参数配置）
        Computer gamingComputer = new Computer.Builder("AMD Ryzen 7 7800X3D", "32GB DDR5", "X670E")
                .withGpu("RTX 4090 24GB")
                .withStorage("2TB NVMe SSD")
                .withMonitor("27英寸 4K 144Hz HDR")
                .withOs("Windows 11 Pro")
                .build();
        System.out.println("【顶级游戏配置】" + gamingComputer);
        
        // 场景3：创建开发环境电脑（部分可选参数）
        Computer devComputer = new Computer.Builder("Apple M3 Pro", "36GB", "Apple Silicon")
                .withStorage("1TB SSD")
                .withOs("macOS Sonoma")
                .build();
        System.out.println("【开发环境配置】" + devComputer);
        
        // 场景4：测试异常情况（缺少必需参数）
        try {
            new Computer.Builder(null, "8GB", "A520").build();
        } catch (IllegalArgumentException e) {
            System.out.println("\n【参数校验测试】 ➤ " + e.getMessage());
        }
    }
}

/* 执行结果示例：
【基础办公配置】
=== 电脑配置清单 =======================
CPU型号: Intel i5
内存容量: 16GB DDR4
主板型号: B760M

【顶级游戏配置】
=== 电脑配置清单 =======================
CPU型号: AMD Ryzen 7 7800X3D
内存容量: 32GB DDR5
主板型号: X670E
显卡配置: RTX 4090 24GB
存储设备: 2TB NVMe SSD
显示设备: 27英寸 4K 144Hz HDR
操作系统: Windows 11 Pro

【开发环境配置】
=== 电脑配置清单 =======================
CPU型号: Apple M3 Pro
内存容量: 36GB
主板型号: Apple Silicon
存储设备: 1TB SSD
操作系统: macOS Sonoma

【参数校验测试】 ➤ CPU型号不能为空
*/

/**
 * 建造者模式结构解析（UML核心概念）：
 * 
 * ┌───────────────────┐       ┌─────────────┐
 * │    Product        │       │   Builder   │
 * ├───────────────────┤       ├─────────────┤
 * │ -partA: String   │<───┐  │ +buildPartA()│
 * │ -partB: String   │    │  │ +buildPartB()│
 * │ -partC: String   │    │  │ +getResult() │
 * ├───────────────────┤    │  └─────────────┘
 * │ +getters()        │    │          ▲
 * └───────────────────┘    │          │
 *                           │  ┌─────────────┐
 *                           └──┤ConcreteBuilder│
 *                              ├─────────────┤
 *                              │ +buildPartA()│
 *                              │ +buildPartB()│
 *                              └─────────────┘
 * 
 * 关键角色：
 * 1. Product（产品类）: 最终要构建的复杂对象（如Computer）
 * 2. Builder（抽象建造者）: 定义构建步骤的接口（本例中直接实现）
 * 3. ConcreteBuilder（具体建造者）: Builder实现类（本例的静态内部类）
 */