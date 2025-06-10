package io.github.suzzt; /**
 * 生成器模式（Builder Pattern）实现：复杂对象创建系统
 * 
 * <p><b>模式类型：</b>创建型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *     将复杂对象的构建过程与其表示分离，使得同样的构建过程可以创建不同的表示。
 *     生成器模式通过分步构建和最终统一组装的方式，解决了包含多个参数的复杂对象的构造问题。
 * </p>
 * 
 * <p><b>模式解决的问题：</b>
 *     当对象的构建过程复杂（需要多个步骤），且需要灵活控制构建过程和最终结果时，
 *     传统的构造方法和setter方法难以清晰地表达构建意图。
 * </p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 构造过程需要多个步骤的复杂对象</li>
 *   <li>✅ 需要创建同一产品族的不同配置对象</li>
 *   <li>✅ 对象的构造过程需要分步骤进行</li>
 *   <li>✅ 需要避免使用过长的构造函数参数列表</li>
 *   <li>❌ 构造过程简单的对象（使用构造函数或静态工厂方法）</li>
 * </ul>
 * 
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>⭐ 封装性：封装复杂对象的构建过程</li>
 *   <li>⭐ 灵活性：可灵活组合不同的构建过程</li>
 *   <li>⭐ 代码可读性：链式调用使代码清晰易懂</li>
 *   <li>⭐ 解耦：构建过程与对象表示分离</li>
 *   <li>⭐ 多态性：不同生成器可以创建不同表示</li>
 * </ul>
 */

import java.util.*;
import java.util.stream.Collectors;

public class BuilderPattern {

    // ====================== 产品类 ======================
    
    /**
     * 计算机产品类
     */
    static class Computer {
        private final String cpu;           // CPU型号
        private final int ram;              // 内存大小（GB）
        private final int ssd;              // SSD容量（GB）
        private final String gpu;           // GPU型号
        private List<String> extras;  // 额外组件
        private final String os;            // 操作系统
        private final boolean overclock;    // 是否超频
        private final String cooling;       // 散热系统
        private final String psu;            // 电源规格
        
        // 私有构造函数，只能通过生成器创建
        private Computer(Builder builder) {
            this.cpu = builder.cpu;
            this.ram = builder.ram;
            this.ssd = builder.ssd;
            this.gpu = builder.gpu;
            this.extras = builder.extras;
            this.os = builder.os;
            this.overclock = builder.overclock;
            this.cooling = builder.cooling;
            this.psu = builder.psu;
        }
        
        /**
         * 显示计算机配置
         */
        public void displaySpecs() {
            System.out.println("\n======= 计算机配置详情 =======");
            System.out.println("CPU: " + cpu);
            System.out.println("内存: " + ram + "GB");
            System.out.println("SSD: " + ssd + "GB");
            System.out.println("GPU: " + (gpu == null ? "集成显卡" : gpu));
            System.out.println("操作系统: " + os);
            System.out.println("散热系统: " + cooling);
            System.out.println("电源: " + psu);
            System.out.println("超频模式: " + (overclock ? "开启" : "关闭"));
            
            if (extras != null && !extras.isEmpty()) {
                System.out.println("额外组件: " + 
                    extras.stream().collect(Collectors.joining(", ")));
            }
            System.out.println("===========================\n");
        }
        
        // 静态内部生成器类
        public static class Builder {
            // 必选参数
            private final String cpu;
            private final int ram;
            
            // 可选参数（使用合理的默认值）
            private int ssd = 512;
            private String gpu = null;
            private List<String> extras = new ArrayList<>();
            private String os = "Windows 11 Home";
            private boolean overclock = false;
            private String cooling = "风冷";
            private String psu = "550W Gold";
            
            /**
             * 构造器（必选参数）
             */
            public Builder(String cpu, int ram) {
                if (cpu == null || cpu.isEmpty()) {
                    throw new IllegalArgumentException("CPU不能为空");
                }
                if (ram <= 0) {
                    throw new IllegalArgumentException("内存大小必须大于0");
                }
                this.cpu = cpu;
                this.ram = ram;
            }
            
            /**
             * 设置SSD容量
             */
            public Builder ssd(int ssd) {
                if (ssd <= 0) {
                    throw new IllegalArgumentException("SSD容量必须大于0");
                }
                this.ssd = ssd;
                return this;
            }
            
            /**
             * 设置独立显卡
             */
            public Builder gpu(String gpu) {
                this.gpu = gpu;
                return this;
            }
            
            /**
             * 添加额外组件
             */
            public Builder addExtra(String extra) {
                if (extra != null && !extra.isEmpty()) {
                    this.extras.add(extra);
                }
                return this;
            }
            
            /**
             * 设置操作系统
             */
            public Builder os(String os) {
                if (os != null && !os.isEmpty()) {
                    this.os = os;
                }
                return this;
            }
            
            /**
             * 开启超频模式
             */
            public Builder enableOverclock() {
                this.overclock = true;
                return this;
            }
            
            /**
             * 设置液冷系统
             */
            public Builder liquidCooling() {
                this.cooling = "液冷";
                return this;
            }
            
            /**
             * 设置大功率电源
             */
            public Builder highPowerPsu() {
                this.psu = "850W Platinum";
                return this;
            }
            
            /**
             * 构建计算机对象
             */
            public Computer build() {
                return new Computer(this);
            }
        }
    }

    // ====================== 抽象生成器接口 ======================
    
    /**
     * 计算机生成器接口（用于不同类型计算机）
     */
    interface ComputerBuilder {
        void buildCpu();
        void buildRam();
        void buildSsd();
        void buildGpu();
        void buildExtras();
        void buildOs();
        void buildOverclock();
        Computer getComputer();
    }

    // ====================== 具体生成器实现 ======================
    
    /**
     * 游戏电脑生成器
     */
    static class GamingComputerBuilder implements ComputerBuilder {
        private final Computer computer;
        
        public GamingComputerBuilder() {
            // 创建构建器并设置初始值
            Computer.Builder builder = new Computer.Builder("Intel i9-13900K", 32);
            builder.enableOverclock().liquidCooling().highPowerPsu();
            this.computer = builder.build();
        }
        
        @Override
        public void buildCpu() {
            // CPU已在构造器中设置
        }
        
        @Override
        public void buildRam() {
            // RAM已在构造器中设置
        }
        
        @Override
        public void buildSsd() {
            // 重新设置SSD容量
            Computer.Builder builder = new Computer.Builder("", 0)
                .ssd(2000);
            this.computer.extras.add("2TB NVMe SSD");
        }
        
        @Override
        public void buildGpu() {
            Computer.Builder builder = new Computer.Builder("", 0)
                .gpu("NVIDIA RTX 4090");
            this.computer.extras.add("NVIDIA RTX 4090 显卡");
        }
        
        @Override
        public void buildExtras() {
            Computer.Builder builder = new Computer.Builder("", 0)
                .addExtra("RGB灯效系统")
                .addExtra("机械键盘")
                .addExtra("游戏鼠标")
                .addExtra("曲面电竞显示器");
            this.computer.extras.addAll(builder.extras);
        }
        
        @Override
        public void buildOs() {
            Computer.Builder builder = new Computer.Builder("", 0)
                .os("Windows 11 Pro");
        }
        
        @Override
        public void buildOverclock() {
            // 已在构造器中设置
        }
        
        @Override
        public Computer getComputer() {
            return computer;
        }
    }
    
    /**
     * 办公电脑生成器
     */
    static class OfficeComputerBuilder implements ComputerBuilder {
        private final Computer computer;
        
        public OfficeComputerBuilder() {
            // 创建构建器并设置初始值
            Computer.Builder builder = new Computer.Builder("Intel i5-12400", 16);
            this.computer = builder.build();
        }
        
        @Override
        public void buildCpu() {
            // 已在构造器中设置
        }
        
        @Override
        public void buildRam() {
            // 已在构造器中设置
        }
        
        @Override
        public void buildSsd() {
            Computer.Builder builder = new Computer.Builder("", 0)
                .ssd(1000);
            this.computer.extras.add("1TB NVMe SSD");
        }
        
        @Override
        public void buildGpu() {
            // 办公电脑通常不需要独立显卡
        }
        
        @Override
        public void buildExtras() {
            Computer.Builder builder = new Computer.Builder("", 0)
                .addExtra("多显示器支持")
                .addExtra("外接键盘鼠标");
            this.computer.extras.addAll(builder.extras);
        }
        
        @Override
        public void buildOs() {
            Computer.Builder builder = new Computer.Builder("", 0)
                .os("Windows 11 Pro");
        }
        
        @Override
        public void buildOverclock() {
            // 办公电脑不需要超频
        }
        
        @Override
        public Computer getComputer() {
            return computer;
        }
    }

    // ====================== 导演类 ======================
    
    /**
     * 电脑装配总监
     */
    static class ComputerDirector {
        private final ComputerBuilder builder;
        
        public ComputerDirector(ComputerBuilder builder) {
            this.builder = builder;
        }
        
        /**
         * 构造完整的计算机对象
         */
        public Computer constructComputer() {
            System.out.println("开始组装计算机...");
            builder.buildCpu();
            builder.buildRam();
            builder.buildSsd();
            builder.buildGpu();
            builder.buildExtras();
            builder.buildOs();
            builder.buildOverclock();
            System.out.println("计算机组装完成！");
            return builder.getComputer();
        }
        
        /**
         * 构造基础版计算机
         */
        public Computer constructBasicComputer() {
            System.out.println("开始组装基础计算机...");
            builder.buildCpu();
            builder.buildRam();
            builder.buildSsd();
            System.out.println("基础计算机组装完成！");
            return builder.getComputer();
        }
    }

    // ====================== 客户端代码 ======================
    
    public static void main(String[] args) {
        System.out.println("============= 生成器模式演示 =============");
        
        // 1. 使用链式构造器模式（标准实现）
        System.out.println("\n>>> 构建高端游戏电脑（链式构造器）");
        Computer gamingComputer = new Computer.Builder("AMD Ryzen 9 7950X", 64)
            .ssd(4000)
            .gpu("NVIDIA RTX 4090")
            .addExtra("RGB液冷系统")
            .addExtra("机械键盘")
            .addExtra("电竞鼠标")
            .addExtra("4K显示器")
            .enableOverclock()
            .liquidCooling()
            .highPowerPsu()
            .os("Windows 11 Pro")
            .build();
        
        gamingComputer.displaySpecs();
        
        // 2. 使用链式构造器模式创建办公电脑
        System.out.println("\n>>> 构建经济型办公电脑（链式构造器）");
        Computer officeComputer = new Computer.Builder("Intel i3-12100", 8)
            .ssd(512)
            .addExtra("键盘鼠标套装")
            .build();
        
        officeComputer.displaySpecs();
        
        // 3. 使用生成器模式+导演类模式
        System.out.println("\n>>> 使用生成器模式+导演类构建电脑");
        
        // 构建游戏电脑
        ComputerBuilder gamingBuilder = new GamingComputerBuilder();
        ComputerDirector gamingDirector = new ComputerDirector(gamingBuilder);
        Computer customGamingComputer = gamingDirector.constructComputer();
        customGamingComputer.displaySpecs();
        
        // 构建办公电脑
        ComputerBuilder officeBuilder = new OfficeComputerBuilder();
        ComputerDirector officeDirector = new ComputerDirector(officeBuilder);
        Computer customOfficeComputer = officeDirector.constructBasicComputer();
        customOfficeComputer.displaySpecs();
        
        // 4. 批量构建不同配置的电脑
        System.out.println("\n>>> 批量构建不同配置电脑");
        List<Computer> computerBatch = Arrays.asList(
            new Computer.Builder("Intel i7-12700K", 32)
                .ssd(2000)
                .gpu("NVIDIA RTX 3080")
                .enableOverclock()
                .build(),
                
            new Computer.Builder("AMD Ryzen 7 5800X", 16)
                .ssd(1000)
                .gpu("AMD RX 6800 XT")
                .addExtra("WiFi6网卡")
                .build(),
                
            new Computer.Builder("Apple M1 Max", 64)
                .ssd(4000)
                .os("macOS Ventura")
                .addExtra("5K显示器")
                .build()
        );
        
        System.out.printf("\n批量构建了 %d 台电脑\n", computerBatch.size());
        for (int i = 0; i < computerBatch.size(); i++) {
            System.out.printf("\n电脑 #%d 配置:", i + 1);
            computerBatch.get(i).displaySpecs();
        }
        
        // 5. 性能测试：生成器模式 vs 传统构造方法
        System.out.println("\n>>> 性能测试（生成器 vs 传统构造）");
        long startTime, endTime;
        
        // 测试生成器模式
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            new Computer.Builder("Intel i5-12600K", 16)
                .ssd(1000)
                .gpu("RTX 3060")
                .build();
        }
        endTime = System.nanoTime();
        System.out.printf("生成器模式耗时: %d 微秒\n", (endTime - startTime) / 1000);
        
        // 测试传统构造方法（模拟多个setter调用）
        startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            Computer comp = new Computer.Builder("Intel i5-12600K", 16).build();
            comp.extras = new ArrayList<>();
            comp.extras.add("RTX 3060");
            // 其他属性的模拟设置...
        }
        endTime = System.nanoTime();
        System.out.printf("传统构造方法耗时: %d 微秒\n", (endTime - startTime) / 1000);
        
        // 6. 使用场景示例：电商配置器
        System.out.println("\n>>> 使用场景：电商电脑配置器");
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n欢迎使用电脑配置器！");
        System.out.println("请输入您想要的CPU型号: ");
        String cpu = scanner.nextLine();
        
        System.out.println("请输入内存大小(GB): ");
        int ram = scanner.nextInt();
        scanner.nextLine(); // 清除换行符
        
        Computer.Builder customBuilder = new Computer.Builder(cpu, ram);
        
        System.out.println("是否需要独立显卡？(y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.println("请输入显卡型号: ");
            customBuilder.gpu(scanner.nextLine());
        }
        
        System.out.println("请选择SSD容量(512/1024/2048): ");
        customBuilder.ssd(scanner.nextInt());
        scanner.nextLine(); // 清除换行符
        
        System.out.println("需要哪些额外配件？(逗号分隔，无则跳过): ");
        String extras = scanner.nextLine();
        if (!extras.isEmpty()) {
            String[] extraArray = extras.split(",");
            for (String extra : extraArray) {
                customBuilder.addExtra(extra.trim());
            }
        }
        
        System.out.println("需要安装操作系统吗？(y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.println("请输入操作系统名称: ");
            customBuilder.os(scanner.nextLine());
        }
        
        System.out.println("需要开启超频模式吗？(y/n): ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            customBuilder.enableOverclock();
        }
        
        Computer customComputer = customBuilder.build();
        System.out.println("\n您的电脑配置如下：");
        customComputer.displaySpecs();
        
        // 模式优势总结
        System.out.println("生成器模式优势总结：");
        System.out.println("1. 灵活构建：可轻松创建同一产品的不同表现形式");
        System.out.println("2. 代码可读性：链式调用清晰表达构建意图");
        System.out.println("3. 过程控制：分离构建过程和最终产品表示");
        System.out.println("4. 不变性：构建完成的对象不可变，线程安全");
        System.out.println("5. 参数验证：可在设置时验证参数有效性");
    }
}