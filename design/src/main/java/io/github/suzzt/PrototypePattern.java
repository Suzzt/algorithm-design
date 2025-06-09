package io.github.suzzt;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 原型模式（Prototype Pattern）实现：高效对象复制系统
 * 
 * <p><b>模式类型：</b>创建型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *     使用原型实例指定创建对象的种类，并通过拷贝这些原型创建新的对象。
 *     原型模式通过复制已有的对象而不是创建新实例来提高性能，特别适用于
 *     创建成本较高的对象或需要动态加载类的场景。
 * </p>
 * 
 * <p><b>模式解决的问题：</b>
 *     当创建对象的成本较高（如需要大量计算或IO操作）时，或当系统需要独立于创建、
 *     组成和表示方式时，原型模式提供了一种高效的解决方案。
 * </p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 创建对象成本高，需要高效的对象创建机制</li>
 *   <li>✅ 需要独立于系统的创建过程</li>
 *   <li>✅ 需要动态加载类或运行时动态创建对象</li>
 *   <li>✅ 需要避免使用工厂类的层次结构</li>
 *   <li>❌ 类中字段包含循环引用或复杂关系</li>
 * </ul>
 * 
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>⭐ 性能提升：直接复制对象比新建对象更高效</li>
 *   <li>⭐ 灵活性：运行时动态添加和删除产品类</li>
 *   <li>⭐ 简化创建过程：避免工厂类的复杂层次结构</li>
 *   <li>⭐ 动态配置：可以在运行时改变值来创建新对象</li>
 * </ul>
 * 
 * <p><b>关键组成部分：</b></p>
 * <ol>
 *   <li><b>Prototype（原型接口）</b>: 声明克隆方法的接口</li>
 *   <li><b>ConcretePrototype（具体原型）</b>: 实现克隆方法的具体类</li>
 *   <li><b>Client（客户端）</b>: 通过克隆原型创建新对象</li>
 * </ol>
 * 
 * <p><b>深浅拷贝问题：</b></p>
 * <ul>
 *   <li>浅拷贝：仅复制对象本身（基本类型和引用），不复制引用指向的对象</li>
 *   <li>深拷贝：复制对象本身及所有引用指向的对象</li>
 *   <li>本实现展示了两种拷贝方式</li>
 * </ul>
 * 
 * <p><b>典型应用：</b></p>
 * <ul>
 *   <li>图形编辑器中的对象复制</li>
 *   <li>高性能游戏引擎中的对象池</li>
 *   <li>数据库连接池中的连接对象</li>
 *   <li>文档模板的批量生成</li>
 *   <li>配置对象的多次复用</li>
 * </ul>
 */
public class PrototypePattern {

    // ====================== 原型接口 ======================
    
    /**
     * 原型接口
     */
    interface Prototype extends Cloneable {
        Prototype clonePrototype(); // 克隆方法
        void display();             // 显示对象信息
    }

    // ====================== 具体原型 - 复杂对象 ======================
    
    /**
     * 高性能游戏角色（展示深拷贝与浅拷贝问题）
     */
    static class GameCharacter implements Prototype {
        private String name;
        private int level;
        private double health;
        private Map<String, Integer> skills;
        private Equipment equipment;
        
        public GameCharacter(String name, int level, double health) {
            this.name = name;
            this.level = level;
            this.health = health;
            this.skills = new HashMap<>();
            this.equipment = new Equipment();
            
            // 模拟创建高成本操作
            System.out.println("⏳ 创建游戏角色 - 加载资源中...");
            simulateCostlyCreation();
            System.out.println("✅ 游戏角色创建完成: " + name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private void simulateCostlyCreation() {
            try {
                // 模拟资源加载时间（500毫秒）
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        public void addSkill(String skill, int level) {
            skills.put(skill, level);
        }
        
        public void setEquipment(Equipment equipment) {
            this.equipment = equipment;
        }

        public Equipment getEquipment() {
            return equipment;
        }

        @Override
        public Prototype clonePrototype() {
            System.out.println("⚡ 复制游戏角色: " + name);
            
            // 创建基本拷贝
            GameCharacter clone = new GameCharacter();
            clone.name = this.name;
            clone.level = this.level;
            clone.health = this.health;
            
            // 浅拷贝 - 共享skills和equipment引用
            clone.skills = this.skills;
            clone.equipment = this.equipment;
            
            return clone;
        }
        
        // 深拷贝方法
        public Prototype deepClone() {
            System.out.println("⚡ 深拷贝游戏角色: " + name);
            
            // 创建基本拷贝
            GameCharacter clone = new GameCharacter();
            clone.name = this.name;
            clone.level = this.level;
            clone.health = this.health;
            
            // 深拷贝 - 创建新集合
            clone.skills = new HashMap<>(this.skills);
            
            // 深拷贝 - 创建新Equipment对象
            clone.equipment = new Equipment();
            clone.equipment.setWeapon(this.equipment.getWeapon());
            clone.equipment.setArmor(this.equipment.getArmor());
            clone.equipment.setAccessory(this.equipment.getAccessory());
            
            return clone;
        }
        
        // 私有构造函数，专用于克隆
        private GameCharacter() {
            // 空的构造函数用于克隆，避免高成本创建
        }
        
        public void setHealth(double health) {
            this.health = health;
        }
        
        public void updateSkill(String skill, int level) {
            skills.put(skill, level);
        }
        
        @Override
        public void display() {
            System.out.printf("角色: %s (Lv.%d) HP: %.1f\n", name, level, health);
            System.out.println("技能: " + skills);
            System.out.println("装备: " + equipment);
            System.out.println();
        }
    }
    
    /**
     * 装备类
     */
    static class Equipment {
        private String weapon;
        private String armor;
        private String accessory;
        
        public Equipment() {
            this.weapon = "新手剑";
            this.armor = "布衣";
            this.accessory = "无";
        }
        
        public void setWeapon(String weapon) {
            this.weapon = weapon;
        }
        
        public void setArmor(String armor) {
            this.armor = armor;
        }
        
        public void setAccessory(String accessory) {
            this.accessory = accessory;
        }
        
        public String getWeapon() {
            return weapon;
        }
        
        public String getArmor() {
            return armor;
        }
        
        public String getAccessory() {
            return accessory;
        }
        
        @Override
        public String toString() {
            return "武器: " + weapon + ", 防具: " + armor + ", 饰品: " + accessory;
        }
    }

    // ====================== 具体原型 - 简单对象 ======================
    
    /**
     * 文档模板类（浅拷贝示例）
     */
    static class DocumentTemplate implements Prototype {
        private String title;
        private String content;
        private Date createdDate;
        private List<String> tags;
        
        public DocumentTemplate(String title, String content) {
            this.title = title;
            this.content = content;
            this.createdDate = new Date();
            this.tags = new ArrayList<>();
            System.out.println("📝 创建新文档模板: " + title);
        }
        
        @Override
        public Prototype clonePrototype() {
            System.out.println("📋 复制文档模板: " + title);
            DocumentTemplate clone = new DocumentTemplate();
            clone.title = this.title + " (副本)";
            clone.content = this.content;
            
            // 浅拷贝 - 共享日期和标签引用
            clone.createdDate = this.createdDate;
            clone.tags = this.tags;
            
            return clone;
        }
        
        // 私有构造函数，专用于克隆
        private DocumentTemplate() {}
        
        public void setTitle(String title) {
            this.title = title;
        }
        
        public void addTag(String tag) {
            tags.add(tag);
        }
        
        public void updateCreatedDate() {
            this.createdDate = new Date();
        }
        
        @Override
        public void display() {
            SimpleDateFormat sdf = new SimpleDateFormat();
            System.out.println("标题: " + title);
            System.out.println("内容: " + content.substring(0, Math.min(20, content.length())) + "...");
            System.out.println("创建时间: " + sdf.format(createdDate));
            System.out.println("标签: " + tags);
            System.out.println();
        }
        
        // 深拷贝方法
        public Prototype deepClone() {
            System.out.println("📋 深拷贝文档模板: " + title);
            DocumentTemplate clone = new DocumentTemplate();
            clone.title = this.title + " (深拷贝副本)";
            clone.content = this.content;
            
            // 深拷贝 - 创建新日期对象
            clone.createdDate = (Date) this.createdDate.clone();
            
            // 深拷贝 - 创建新标签列表
            clone.tags = new ArrayList<>(this.tags);
            
            return clone;
        }
    }

    // ====================== 原型管理器 ======================
    
    /**
     * 原型管理器（存储和获取原型）
     */
    static class PrototypeManager {
        private final Map<String, Prototype> prototypes = new HashMap<>();
        
        public void registerPrototype(String key, Prototype prototype) {
            prototypes.put(key, prototype);
        }
        
        public Prototype getPrototype(String key) {
            Prototype prototype = prototypes.get(key);
            if (prototype != null) {
                return prototype.clonePrototype();
            }
            return null;
        }
        
        // 添加所有原型的方法
        public void loadPrototypes() {
            // 注册游戏角色原型
            GameCharacter warrior = new GameCharacter("战士", 1, 100.0);
            warrior.addSkill("重击", 1);
            registerPrototype("warrior", warrior);
            
            GameCharacter mage = new GameCharacter("法师", 1, 80.0);
            mage.addSkill("火球术", 1);
            registerPrototype("mage", mage);
            
            // 注册文档模板原型
            DocumentTemplate report = new DocumentTemplate("月度报告", "本月的销售业绩...");
            report.addTag("报告");
            report.addTag("财务");
            registerPrototype("report", report);
            
            DocumentTemplate contract = new DocumentTemplate("合同模板", "本协议由以下双方签订...");
            contract.addTag("合同");
            contract.addTag("法律");
            registerPrototype("contract", contract);
        }
    }

    // ====================== 客户端代码 ======================
    
    public static void main(String[] args) {
        System.out.println("============= 原型模式演示系统 =============");
        
        // 1. 创建原型管理器并加载原型
        PrototypeManager manager = new PrototypeManager();
        manager.loadPrototypes();
        System.out.println("\n✅ 原型加载完成\n");
        
        // 2. 通过原型管理器获取对象
        System.out.println(">>> 通过原型创建新对象:");
        Prototype warrior = manager.getPrototype("warrior");
        Prototype mage = manager.getPrototype("mage");
        
        if (warrior != null && mage != null) {
            System.out.println("战士角色克隆完成");
            warrior.display();
            
            System.out.println("法师角色克隆完成");
            mage.display();
        }
        
        // 3. 展示浅拷贝与深拷贝的区别
        System.out.println("\n>>> 浅拷贝与深拷贝区别:");
        
        // 获取文档模板原型
        DocumentTemplate reportTemplate = (DocumentTemplate) manager.getPrototype("report");
        reportTemplate.display();
        
        // 浅拷贝复制
        DocumentTemplate shallowCopy = (DocumentTemplate) reportTemplate.clonePrototype();
        shallowCopy.setTitle("浅拷贝报告");
        shallowCopy.addTag("新标签");
        
        // 深拷贝复制
        DocumentTemplate deepCopy = (DocumentTemplate) reportTemplate.deepClone();
        deepCopy.setTitle("深拷贝报告");
        deepCopy.addTag("深拷贝标签");
        deepCopy.updateCreatedDate();
        
        System.out.println("原始模板:");
        reportTemplate.display();
        
        System.out.println("浅拷贝副本:");
        shallowCopy.display();
        
        System.out.println("深拷贝副本:");
        deepCopy.display();
        
        // 4. 创建多个游戏角色实例
        System.out.println("\n>>> 批量创建游戏角色实例:");
        long startTime = System.nanoTime();
        
        List<Prototype> warriors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            warriors.add(manager.getPrototype("warrior"));
        }
        
        List<Prototype> mages = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mages.add(manager.getPrototype("mage"));
        }
        
        long endTime = System.nanoTime();
        System.out.printf("创建 %d 个角色耗时: %d 微秒\n", 
                warriors.size() + mages.size(), 
                (endTime - startTime) / 1000);
        
        // 5. 展示深拷贝在复杂对象中的应用
        System.out.println("\n>>> 深拷贝在复杂对象中的应用:");
        GameCharacter original = (GameCharacter) manager.getPrototype("warrior");
        original.setEquipment(new Equipment());
        original.getEquipment().setWeapon("巨人之剑");
        original.getEquipment().setArmor("骑士铠甲");
        original.setHealth(150.0);
        
        // 深拷贝
        GameCharacter deepCopyWarrior = (GameCharacter) original.deepClone();
        deepCopyWarrior.setName("精英战士");
        deepCopyWarrior.getEquipment().setWeapon("火焰之剑");
        deepCopyWarrior.setHealth(200.0);
        
        System.out.println("原始战士:");
        original.display();
        
        System.out.println("深拷贝战士:");
        deepCopyWarrior.display();
        
        // 6. 性能对比：使用new创建对象 vs 使用原型复制
        System.out.println("\n>>> 性能对比 (新建对象 vs 原型复制):");
        
        // 传统方式创建对象
        long newStart = System.nanoTime();
        for (int i = 0; i < 5; i++) {
            GameCharacter newWarrior = new GameCharacter("战士", 1, 100.0);
        }
        long newEnd = System.nanoTime();
        
        // 原型方式创建对象
        long prototypeStart = System.nanoTime();
        for (int i = 0; i < 5; i++) {
            Prototype protoWarrior = manager.getPrototype("warrior");
        }
        long prototypeEnd = System.nanoTime();
        
        System.out.printf("传统创建方式耗时: %d 微秒\n", (newEnd - newStart) / 1000);
        System.out.printf("原型创建方式耗时: %d 微秒\n", (prototypeEnd - prototypeStart) / 1000);
        
        // 模式优势总结
        String separator = String.join("", Collections.nCopies(70, "="));
        System.out.println("\n" + separator);
        System.out.println("原型模式优势总结：");
        System.out.println("1. 性能提升：避免重复的高成本初始化操作");
        System.out.println("2. 灵活扩展：可在运行时动态添加原型");
        System.out.println("3. 简化结构：减少子类数量，避免工厂类层次结构");
        System.out.println("4. 配置复用：创建预配置对象的副本");
        
        // 模式扩展演示
        System.out.println("\n扩展演示：动态添加新原型");
        
        // 创建并注册新原型
        DocumentTemplate newTemplate = new DocumentTemplate("项目计划", "项目目标：...");
        newTemplate.addTag("项目");
        newTemplate.addTag("计划");
        manager.registerPrototype("project_plan", newTemplate);
        
        // 使用新原型
        DocumentTemplate projectPlan = (DocumentTemplate) manager.getPrototype("project_plan");
        projectPlan.setTitle("新产品发布计划");
        System.out.println("\n使用动态添加的原型创建文档:");
        projectPlan.display();
    }
    
    // 辅助类
    static class SimpleDateFormat {
        private final java.text.SimpleDateFormat sdf = 
            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public String format(Date date) {
            return sdf.format(date);
        }
    }
}