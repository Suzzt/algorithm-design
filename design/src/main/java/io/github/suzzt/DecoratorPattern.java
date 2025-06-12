package io.github.suzzt; /**
 * 装饰器模式 (Decorator Pattern) 实现：高级披萨订制系统
 * 
 * <p><b>模式类型：</b>结构型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *     动态地为对象添加新的功能，同时保持类方法签名的完整性。
 *     通过创建装饰器类来包装原有对象，提供额外的功能，而不修改其结构。
 * </p>
 * 
 * <p><b>模式解决的问题：</b>
 *     当需要给对象动态添加功能，但又不能或不希望使用继承时（避免类爆炸），
 *     装饰器模式提供了一种灵活的替代方案，支持运行时添加职责。
 * </p>
 */

import java.util.ArrayList;
import java.util.List;

public class DecoratorPattern {

    public static void main(String[] args) {
        System.out.println("============= 高级披萨订制系统 =============\n");
        
        // 1. 创建基础披萨
        Pizza basicPizza = new BasicPizza();
        System.out.println("基础披萨: " + basicPizza.getDescription() + 
                          " | 价格: $" + basicPizza.cost());
        
        // 2. 创建装饰器添加配料
        Pizza cheesePizza = new CheeseDecorator(basicPizza);
        System.out.println("\n芝士披萨: " + cheesePizza.getDescription() + 
                          " | 价格: $" + cheesePizza.cost());
        
        Pizza pepperoniPizza = new PepperoniDecorator(cheesePizza);
        System.out.println("\n芝士辣香肠披萨: " + pepperoniPizza.getDescription() + 
                          " | 价格: $" + pepperoniPizza.cost());
        
        Pizza deluxePizza = new OliveDecorator(
            new MushroomDecorator(
                new PepperoniDecorator(
                    new CheeseDecorator(basicPizza)
                )
            )
        );
        System.out.println("\n豪华披萨: " + deluxePizza.getDescription() + 
                          " | 价格: $" + deluxePizza.cost());
        
        // 3. 创建特殊披萨
        Pizza hawaiianPizza = new PineappleDecorator(
            new HamDecorator(
                new CheeseDecorator(basicPizza)
            )
        );
        System.out.println("\n夏威夷披萨: " + hawaiianPizza.getDescription() + 
                          " | 价格: $" + hawaiianPizza.cost());
        
        Pizza vegetarianPizza = new OliveDecorator(
            new MushroomDecorator(
                new BellPepperDecorator(
                    new CheeseDecorator(basicPizza)
                )
            )
        );
        System.out.println("\n素食披萨: " + vegetarianPizza.getDescription() + 
                          " | 价格: $" + vegetarianPizza.cost());
        
        // 4. 创建自定义披萨构建器
        System.out.println("\n>>> 自定义披萨构建器 <<<");
        PizzaBuilder pizzaBuilder = new PizzaBuilder(new BasicPizza())
            .addCheese()
            .addPepperoni()
            .addMushroom()
            .addExtraCheese()
            .addOlive();
        
        Pizza customPizza = pizzaBuilder.build();
        System.out.println("自定义披萨: " + customPizza.getDescription() + 
                          " | 价格: $" + customPizza.cost());
        
        // 5. 打印销售票据
        System.out.println("\n>>> 销售票据 <<<");
        Order order = new Order();
        order.addItem(new PizzaWithDescription(basicPizza));
        order.addItem(new PizzaWithDescription(pepperoniPizza));
        order.addItem(new PizzaWithDescription(hawaiianPizza));
        order.addItem(new PizzaWithDescription(customPizza));
        order.addItem(new Drink("可乐", 2.99));
        order.addItem(new Drink("啤酒", 4.99));
        order.printReceipt();
        
        // 模式优势总结
        System.out.println("装饰器模式优势总结：");
        System.out.println("1. 扩展灵活性：动态添加新功能而不修改原有对象");
        System.out.println("2. 避免类爆炸：通过组合替代继承减少子类数量");
        System.out.println("3. 符合开闭原则：对扩展开放，对修改关闭");
        System.out.println("4. 功能分离：装饰类专注单一功能，符合单一职责原则");
        
        // 模式应用场景
        System.out.println("装饰器模式实际应用场景：");
        System.out.println("① 披萨/咖啡/食品定制系统（如本示例）");
        System.out.println("② Java I/O 流处理（如BufferedInputStream）");
        System.out.println("③ GUI界面组件增强");
        System.out.println("④ Web请求处理链");
        System.out.println("⑤ 文本格式化系统");
    }
}

// ====================== 抽象组件 ======================

interface Pizza {
    String getDescription();
    double cost();
}

// ====================== 具体组件 - 基础披萨 ======================

class BasicPizza implements Pizza {
    @Override
    public String getDescription() {
        return "经典披萨底";
    }
    
    @Override
    public double cost() {
        return 8.99;
    }
}

class ThinCrustPizza implements Pizza {
    @Override
    public String getDescription() {
        return "薄脆披萨底";
    }
    
    @Override
    public double cost() {
        return 9.99;
    }
}

class GlutenFreePizza implements Pizza {
    @Override
    public String getDescription() {
        return "无麸质披萨底";
    }
    
    @Override
    public double cost() {
        return 12.99;
    }
}

// ====================== 抽象装饰器 ======================

abstract class PizzaDecorator implements Pizza {
    protected Pizza decoratedPizza;
    
    public PizzaDecorator(Pizza decoratedPizza) {
        this.decoratedPizza = decoratedPizza;
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription();
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost();
    }
}

// ====================== 具体装饰器 - 配料 ======================

class CheeseDecorator extends PizzaDecorator {
    public CheeseDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription() + ", 芝士";
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() + 2.49;
    }
}

class ExtraCheeseDecorator extends PizzaDecorator {
    public ExtraCheeseDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription() + ", 双倍芝士";
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() + 3.99;
    }
}

class PepperoniDecorator extends PizzaDecorator {
    public PepperoniDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription() + ", 辣香肠";
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() + 3.49;
    }
}

class MushroomDecorator extends PizzaDecorator {
    public MushroomDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription() + ", 蘑菇";
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() + 1.99;
    }
}

class OliveDecorator extends PizzaDecorator {
    public OliveDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription() + ", 橄榄";
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() + 1.99;
    }
}

class HamDecorator extends PizzaDecorator {
    public HamDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription() + ", 火腿";
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() + 2.99;
    }
}

class PineappleDecorator extends PizzaDecorator {
    public PineappleDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription() + ", 菠萝";
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() + 2.49;
    }
}

class BellPepperDecorator extends PizzaDecorator {
    public BellPepperDecorator(Pizza decoratedPizza) {
        super(decoratedPizza);
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription() + ", 彩椒";
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() + 1.49;
    }
}

// ====================== 披萨构建器（简化装饰器使用）======================

class PizzaBuilder {
    private Pizza pizza;
    
    public PizzaBuilder(Pizza basePizza) {
        this.pizza = basePizza;
    }
    
    public PizzaBuilder addCheese() {
        pizza = new CheeseDecorator(pizza);
        return this;
    }
    
    public PizzaBuilder addExtraCheese() {
        pizza = new ExtraCheeseDecorator(pizza);
        return this;
    }
    
    public PizzaBuilder addPepperoni() {
        pizza = new PepperoniDecorator(pizza);
        return this;
    }
    
    public PizzaBuilder addMushroom() {
        pizza = new MushroomDecorator(pizza);
        return this;
    }
    
    public PizzaBuilder addOlive() {
        pizza = new OliveDecorator(pizza);
        return this;
    }
    
    public PizzaBuilder addHam() {
        pizza = new HamDecorator(pizza);
        return this;
    }
    
    public PizzaBuilder addPineapple() {
        pizza = new PineappleDecorator(pizza);
        return this;
    }
    
    public PizzaBuilder addBellPepper() {
        pizza = new BellPepperDecorator(pizza);
        return this;
    }
    
    public Pizza build() {
        return pizza;
    }
}

// ====================== 订单处理系统（可选扩展）======================

interface OrderItem {
    String getDescription();
    double getPrice();
}

class PizzaWithDescription implements OrderItem {
    private Pizza pizza;
    
    public PizzaWithDescription(Pizza pizza) {
        this.pizza = pizza;
    }
    
    @Override
    public String getDescription() {
        return pizza.getDescription();
    }
    
    @Override
    public double getPrice() {
        return pizza.cost();
    }
}

class Drink implements OrderItem {
    private String name;
    private double price;
    
    public Drink(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    @Override
    public String getDescription() {
        return name;
    }
    
    @Override
    public double getPrice() {
        return price;
    }
}

class Order {
    private List<OrderItem> items = new ArrayList<>();
    
    public void addItem(OrderItem item) {
        items.add(item);
    }
    
    public double getTotal() {
        return items.stream().mapToDouble(OrderItem::getPrice).sum();
    }
    
    public void printReceipt() {
        System.out.println("\n================ 销售票据 ================");
        System.out.println("商品名称                     价格");
        System.out.println("----------------------------------------");
        
        items.forEach(item -> {
            System.out.printf("%-28s $%.2f\n", 
                item.getDescription(), item.getPrice());
        });
        
        System.out.println("----------------------------------------");
        System.out.printf("%-28s $%.2f\n", "总计:", getTotal());
        System.out.printf("%-28s $%.2f\n", "税 (8.5%):", getTotal() * 0.085);
        System.out.printf("%-28s $%.2f\n", "应付金额:", getTotal() * 1.085);
        System.out.println("========================================");
    }
}

// ====================== 披萨尺寸装饰器（可选扩展）======================

class SizeDecorator extends PizzaDecorator {
    private String size;
    private double sizeFactor;
    
    public SizeDecorator(Pizza decoratedPizza, String size, double sizeFactor) {
        super(decoratedPizza);
        this.size = size;
        this.sizeFactor = sizeFactor;
    }
    
    @Override
    public String getDescription() {
        return size + "份量 " + decoratedPizza.getDescription();
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() * sizeFactor;
    }
}

// ====================== 特价装饰器（可选扩展）======================

class DiscountDecorator extends PizzaDecorator {
    private double discountRate;
    private String promotionName;
    
    public DiscountDecorator(Pizza decoratedPizza, double discountRate, String promotionName) {
        super(decoratedPizza);
        this.discountRate = discountRate;
        this.promotionName = promotionName;
    }
    
    @Override
    public String getDescription() {
        return decoratedPizza.getDescription() + " (促销: " + promotionName + ")";
    }
    
    @Override
    public double cost() {
        return decoratedPizza.cost() * (1 - discountRate);
    }
}