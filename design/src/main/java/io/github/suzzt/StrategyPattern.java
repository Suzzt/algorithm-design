package io.github.suzzt;
/**
 * 策略模式（Strategy Pattern）实现：支付系统设计
 * 
 * <p><b>模式类型：</b>行为型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *     定义一系列算法，将每个算法封装起来，并且使它们可以互相替换。
 *     策略模式让算法的变化独立于使用算法的客户，使得：
 *     1. 算法可以独立变化
 *     2. 客户端可以根据需求动态切换算法
 *     3. 避免使用多重条件判断语句
 * </p>
 * 
 * <p><b>模式解决的问题：</b>
 *     当一个系统需要多种算法实现同一种功能，且需要在运行时动态切换算法时，
 *     使用策略模式可以避免将算法硬编码在客户端代码中，提高系统的灵活性和可扩展性。
 * </p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 一个系统需要多种算法变体（如不同支付方式、不同排序算法等）</li>
 *   <li>✅ 需要封装算法中与数据结构相关的复杂细节</li>
 *   <li>✅ 避免暴露复杂、与算法相关的数据结构给客户端</li>
 *   <li>✅ 需要动态在多种算法中选择一种</li>
 *   <li>❌ 算法很少变化，没有扩展需求</li>
 * </ul>
 * 
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>⭐ 算法自由切换：运行时动态改变对象使用的算法</li>
 *   <li>⭐ 避免多重条件判断：消除大量的if-else或switch-case语句</li>
 *   <li>⭐ 符合开闭原则：增加新策略无需修改上下文类</li>
 *   <li>⭐ 提高代码复用性：相同算法可以被多个上下文环境复用</li>
 * </ul>
 * 
 * <p><b>模式组成部分：</b></p>
 * <ol>
 *   <li><b>Strategy（策略）</b>: 定义所有支持的算法的公共接口</li>
 *   <li><b>ConcreteStrategy（具体策略）</b>: 实现策略接口的具体算法类</li>
 *   <li><b>Context（上下文）</b>: 持有策略对象的引用，提供设置策略和执行算法的方法</li>
 * </ol>
 * 
 * <p><b>设计构思流程：</b></p>
 * <ol>
 *   <li>识别应用中需要变化的算法部分（如支付系统中的支付方式）</li>
 *   <li>定义策略接口，包含算法执行方法（如executePayment）</li>
 *   <li>创建具体策略类实现策略接口（如CreditCard、PayPal等）</li>
 *   <li>创建上下文类，持有策略对象并提供设置和执行方法</li>
 *   <li>客户端根据需要选择具体策略并传递给上下文对象</li>
 *   <li>上下文对象执行策略算法，无需知道具体实现细节</li>
 * </ol>
 * 
 * <p><b>经典案例：</b></p>
 * <ul>
 *   <li>支付系统中的多种支付方式选择</li>
 *   <li>电子商务中的不同促销策略（满减、折扣、赠品等）</li>
 *   <li>游戏中的不同角色行为（攻击、防御、治疗等）</li>
 *   <li>压缩工具中的不同压缩算法（ZIP、RAR、7z等）</li>
 *   <li>排序算法选择（快速排序、归并排序、冒泡排序等）</li>
 * </ul>
 */

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StrategyPattern {

    // 1. 策略接口：定义支付算法的公共接口
    interface PaymentStrategy {
        boolean executePayment(double amount);
        String getStrategyName();
    }

    // 2. 具体策略类：信用卡支付
    static class CreditCardPayment implements PaymentStrategy {
        private final String cardNumber;
        private final String expiryDate;
        private final String cvv;
        
        public CreditCardPayment(String cardNumber, String expiryDate, String cvv) {
            this.cardNumber = cardNumber;
            this.expiryDate = expiryDate;
            this.cvv = cvv;
        }
        
        @Override
        public boolean executePayment(double amount) {
            System.out.println("💳 信用卡支付: ¥" + formatAmount(amount) + 
                    " | 卡号: " + maskCardNumber() + " | 有效期: " + expiryDate);
            // 模拟支付处理
            System.out.println("  正在处理信用卡支付...");
            // 假设所有信用卡支付都成功
            return true;
        }
        
        private String maskCardNumber() {
            return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
        }
        
        @Override
        public String getStrategyName() {
            return "信用卡";
        }
    }

    // 3. 具体策略类：支付宝支付
    static class AlipayPayment implements PaymentStrategy {
        private final String account;
        
        public AlipayPayment(String account) {
            this.account = account;
        }
        
        @Override
        public boolean executePayment(double amount) {
            System.out.println("🔵 支付宝支付: ¥" + formatAmount(amount) + " | 账号: " + account);
            // 模拟支付宝支付
            System.out.println("  向支付宝发起支付请求...");
            System.out.println("  请打开支付宝App确认支付");
            // 假设所有支付宝支付都成功
            return true;
        }
        
        @Override
        public String getStrategyName() {
            return "支付宝";
        }
    }

    // 4. 具体策略类：微信支付
    static class WechatPayment implements PaymentStrategy {
        private final String openId;
        
        public WechatPayment(String openId) {
            this.openId = openId;
        }
        
        @Override
        public boolean executePayment(double amount) {
            System.out.println("🟢 微信支付: ¥" + formatAmount(amount) + " | OpenID: " + openId);
            // 模拟微信支付
            System.out.println("  正在生成微信支付二维码...");
            System.out.println("  请使用微信扫码支付");
            // 假设所有微信支付都成功
            return true;
        }
        
        @Override
        public String getStrategyName() {
            return "微信支付";
        }
    }
    
    // 5. 具体策略类：PayPal支付
    static class PayPalPayment implements PaymentStrategy {
        private final String email;
        
        public PayPalPayment(String email) {
            this.email = email;
        }
        
        @Override
        public boolean executePayment(double amount) {
            double usdAmount = amount * 0.14; // 汇率转换
            System.out.println("🔶 PayPal支付: $" + formatAmount(usdAmount) + " | 账号: " + email);
            // 模拟PayPal支付
            System.out.println("  跳转到PayPal支付页面...");
            System.out.println("  正在处理国际支付...");
            // 假设所有PayPal支付都成功
            return true;
        }
        
        @Override
        public String getStrategyName() {
            return "PayPal";
        }
    }
    
    // 6. 具体策略类：加密货币支付
    static class CryptoPayment implements PaymentStrategy {
        private final String walletAddress;
        private final String coinType;
        
        public CryptoPayment(String walletAddress, String coinType) {
            this.walletAddress = walletAddress;
            this.coinType = coinType;
        }
        
        @Override
        public boolean executePayment(double amount) {
            System.out.println("🔷 加密货币支付: " + coinType + " | 钱包地址: " + walletAddress);
            // 模拟加密货币支付
            System.out.println("  正在计算所需加密货币数量...");
            System.out.println("  请发送加密货币到指定地址");
            // 假设所有加密货币支付都成功
            return true;
        }
        
        @Override
        public String getStrategyName() {
            return coinType + "支付";
        }
    }

    // 7. 上下文类：购物车
    static class ShoppingCart {
        private PaymentStrategy paymentStrategy;
        private final Map<String, Double> items = new HashMap<>();
        
        public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
            this.paymentStrategy = paymentStrategy;
            System.out.println("\n[已设置支付方式]: " + paymentStrategy.getStrategyName());
        }
        
        public void addItem(String name, double price) {
            items.put(name, price);
        }
        
        public double calculateTotal() {
            return items.values().stream().mapToDouble(Double::doubleValue).sum();
        }
        
        public boolean checkout() {
            double total = calculateTotal();
            
            if (paymentStrategy == null) {
                System.out.println("错误：请先设置支付方式");
                return false;
            }
            
            System.out.println("\n========== 订单结算 ==========");
            System.out.println("商品清单:");
            items.forEach((item, price) -> 
                System.out.printf("  %-15s ¥%s\n", item, formatAmount(price)));
            
            System.out.printf("\n总金额: ¥%s\n", formatAmount(total));
            System.out.println("使用支付方式: " + paymentStrategy.getStrategyName());
            
            return paymentStrategy.executePayment(total);
        }
    }

    // 辅助方法：格式化金额
    private static String formatAmount(double amount) {
        return new DecimalFormat("#,##0.00").format(amount);
    }

    // 主方法：演示策略模式
    public static void main(String[] args) {
        // ====================== 准备支付策略 ======================
        PaymentStrategy[] strategies = {
            new CreditCardPayment("1234567890123456", "12/25", "123"),
            new AlipayPayment("user@example.com"),
            new WechatPayment("wxid_1234567890"),
            new PayPalPayment("paypal@example.com"),
            new CryptoPayment("0x742d35Cc6634C0532925a3b844Bc454e4438f44e", "ETH")
        };
        
        // ====================== 创建购物车 ======================
        ShoppingCart cart = new ShoppingCart();
        cart.addItem("无线耳机", 899.00);
        cart.addItem("手机壳", 99.90);
        cart.addItem("贴膜", 29.90);
        
        // ====================== 模拟用户选择支付方式 ======================
        System.out.println("请选择支付方式:");
        for (int i = 0; i < strategies.length; i++) {
            System.out.printf("%d. %s\n", i + 1, strategies[i].getStrategyName());
        }
        System.out.println("0. 退出");
        
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n请输入支付方式编号: ");
            int choice = scanner.nextInt();
            
            if (choice == 0) {
                System.out.println("感谢光临!");
                break;
            }
            
            if (choice > 0 && choice <= strategies.length) {
                // 设置支付策略
                cart.setPaymentStrategy(strategies[choice - 1]);
                
                // 执行支付
                boolean success = cart.checkout();
                
                if (success) {
                    System.out.println("✅ 支付成功! 感谢您的购买!");
                    System.out.println("=====================================");
                } else {
                    System.out.println("❌ 支付失败，请重试或选择其他支付方式");
                }
            } else {
                System.out.println("无效选择，请重新输入");
            }
        }
        scanner.close();
        
        // ====================== 模式优势总结 ======================
        System.out.println("\n================= 策略模式优势总结 =================");
        System.out.println("1. 动态切换策略: 运行时轻松切换不同支付算法");
        System.out.println("2. 避免条件判断: 消除了复杂的if-else/switch支付选择逻辑");
        System.out.println("3. 开闭原则: 新增支付方式(如数字人民币)不影响现有代码");
        System.out.println("4. 算法分离: 将支付细节封装在具体策略类中");
        
        // ====================== 实际应用扩展 ======================
        System.out.println("\n实际应用扩展思路:");
        System.out.println("● 支付策略组合: 优惠券+支付方式组合策略");
        System.out.println("● 策略工厂: 根据配置自动创建支付策略");
        System.out.println("● 策略统计: 记录不同支付方式的使用频率");
    }
}