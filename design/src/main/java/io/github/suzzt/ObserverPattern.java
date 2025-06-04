package io.github.suzzt; /**
 * 观察者模式（Observer Pattern）实现：新闻订阅系统
 * 
 * <p><b>模式类型：</b>行为型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *     定义对象间的一种一对多的依赖关系，当一个对象（主题）的状态发生改变时，
 *     所有依赖于它的对象（观察者）都自动收到通知并被更新。这种模式实现了
 *     主题和观察者之间的松耦合，使它们可以独立改变和复用。
 * </p>
 * 
 * <p><b>模式解决的问题：</b>
 *     在软件系统中，一个对象的改变需要同时影响其他对象，但又不知道有多少对象需要改变，
 *     或者这些对象的具体类型是什么。观察者模式提供了一种避免组件之间紧密耦合的设计方案。
 * </p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 一个对象的改变需要改变其他多个对象，但不知道具体有多少对象需要改变</li>
 *   <li>✅ 当一个抽象模型有两个方面，其中一个方面依赖于另一个方面，将两者封装在独立对象中以各自独立地改变和复用</li>
 *   <li>✅ 需要建立对象间的触发机制（如事件驱动系统）</li>
 *   <li>✅ 需要实现广播通信机制（一对多的消息传递）</li>
 *   <li>❌ 当一个对象的改变需要更新其他对象，而这些对象之间存在复杂的依赖关系时</li>
 * </ul>
 * 
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>⭐ 主题和观察者松耦合：主题只知道观察者实现了某个接口，不知道观察者的具体类</li>
 *   <li>⭐ 遵循开闭原则：可以新增观察者而无须修改主题代码</li>
 *   <li>⭐ 支持广播通信：主题状态变化时自动通知所有订阅的观察者</li>
 *   <li>⭐ 灵活性高：可以在运行时动态添加或移除观察者</li>
 * </ul>
 * 
 * <p><b>模式组成部分：</b></p>
 * <ol>
 *   <li><b>Subject（主题）</b>: 维护观察者列表，提供添加、删除和通知观察者的方法</li>
 *   <li><b>Observer（观察者）</b>: 定义更新接口，用于接收主题的通知</li>
 *   <li><b>ConcreteSubject（具体主题）</b>: 存储状态，当状态改变时通知所有观察者</li>
 *   <li><b>ConcreteObserver（具体观察者）</b>: 实现更新接口，保持状态与主题的一致性</li>
 * </ol>
 * 
 * <p><b>设计构思流程：</b></p>
 * <ol>
 *   <li>识别问题域中变化的一方（主题）和依赖变化的多方（观察者）</li>
 *   <li>定义主题接口（Subject），包含注册、移除和通知方法</li>
 *   <li>定义观察者接口（Observer），包含更新方法</li>
 *   <li>创建具体主题类，实现主题接口并管理观察者列表</li>
 *   <li>创建具体观察者类，实现观察者接口并定义接收通知后的行为</li>
 *   <li>客户端创建主题和观察者实例，并将观察者注册到主题</li>
 *   <li>当主题状态改变时，自动通知所有已注册的观察者</li>
 * </ol>
 * 
 * <p><b>经典案例：</b></p>
 * <ul>
 *   <li>GUI事件处理（按钮点击事件）</li>
 *   <li>消息队列/发布-订阅系统</li>
 *   <li>模型-视图-控制器(MVC)架构中的模型和视图关系</li>
 *   <li>实时数据监控系统（如股票行情）</li>
 *   <li>日志系统和系统监控</li>
 * </ul>
 */

import java.util.ArrayList;
import java.util.List;

public class ObserverPattern {

    // 1. 主题接口：定义主题的基本操作
    interface NewsSubject {
        void registerObserver(NewsObserver observer);
        void removeObserver(NewsObserver observer);
        void notifyObservers();
    }

    // 2. 观察者接口：定义更新接口
    interface NewsObserver {
        void update(String latestNews);
    }

    // 3. 具体主题类：新闻发布系统
    static class NewsPublisher implements NewsSubject {
        private final List<NewsObserver> observers = new ArrayList<>();
        private String latestNews;
        private boolean newsChanged;
        
        // 设置最新新闻（会触发通知）
        public void setLatestNews(String news) {
            this.latestNews = news;
            newsChanged = true;
            notifyObservers(); // 自动通知所有观察者
            newsChanged = false;
        }
        
        // 注册观察者
        @Override
        public void registerObserver(NewsObserver observer) {
            if (!observers.contains(observer)) {
                observers.add(observer);
                System.out.println("✅ 已订阅: " + getObserverName(observer));
            }
        }
        
        // 移除观察者
        @Override
        public void removeObserver(NewsObserver observer) {
            if (observers.remove(observer)) {
                System.out.println("❌ 已取消订阅: " + getObserverName(observer));
            }
        }
        
        // 通知所有观察者
        @Override
        public void notifyObservers() {
            if (!newsChanged) return;
            
            System.out.println("\n====== 有新新闻发布 =====");
            for (NewsObserver observer : observers) {
                observer.update(latestNews);
            }
            System.out.println("===== 新闻推送完毕 =====");
        }
        
        // 辅助方法：获取观察者名称
        private String getObserverName(NewsObserver observer) {
            if (observer instanceof EmailSubscriber) {
                return ((EmailSubscriber) observer).getEmail();
            } else if (observer instanceof MobileApp) {
                return ((MobileApp) observer).getAppName();
            }
            return "匿名订阅者";
        }
    }

    // 4. 具体观察者类：邮件订阅者
    static class EmailSubscriber implements NewsObserver {
        private final String email;
        private String lastReceivedNews;
        
        public EmailSubscriber(String email) {
            this.email = email;
        }
        
        @Override
        public void update(String latestNews) {
            lastReceivedNews = latestNews;
            System.out.println("[邮件] 发送新闻到 " + email + ": " + latestNews);
        }
        
        public String getEmail() {
            return email;
        }
        
        public String getLastReceivedNews() {
            return lastReceivedNews;
        }
    }

    // 5. 具体观察者类：移动应用
    static class MobileApp implements NewsObserver {
        private final String appName;
        private String pushNotification;
        
        public MobileApp(String appName) {
            this.appName = appName;
        }
        
        @Override
        public void update(String latestNews) {
            pushNotification = latestNews;
            System.out.println("[推送] " + appName + " 收到新闻推送: " + latestNews);
        }
        
        public String getAppName() {
            return appName;
        }
        
        public String getPushNotification() {
            return pushNotification;
        }
    }
    
    // 6. 具体观察者类：打印版订阅
    static class PrintEdition implements NewsObserver {
        @Override
        public void update(String latestNews) {
            System.out.println("[打印版] 准备在明天的头版刊登: " + latestNews);
        }
    }

    // 主方法：演示观察者模式
    public static void main(String[] args) {
        // ====================== 创建主题 ======================
        NewsPublisher publisher = new NewsPublisher();
        
        // ====================== 创建观察者 ======================
        NewsObserver john = new EmailSubscriber("john@example.com");
        NewsObserver emily = new EmailSubscriber("emily@example.com");
        NewsObserver iosApp = new MobileApp("iPhone新闻客户端");
        NewsObserver androidApp = new MobileApp("Android新闻推送");
        NewsObserver printEdition = new PrintEdition();
        
        // ====================== 注册观察者 ======================
        publisher.registerObserver(john);
        publisher.registerObserver(emily);
        publisher.registerObserver(iosApp);
        publisher.registerObserver(androidApp);
        publisher.registerObserver(printEdition);
        
        // ====================== 发布第一条新闻 ======================
        System.out.println("\n>>> 发布第一条重要新闻");
        publisher.setLatestNews("全球科技峰会将在下月召开，AI领域将有重大突破");
        
        // ====================== 移除一个观察者 ======================
        publisher.removeObserver(emily);
        
        // ====================== 发布第二条新闻 ======================
        System.out.println("\n>>> 发布第二条紧急新闻");
        publisher.setLatestNews("台风预警：本周末将有强台风登陆沿海城市");
        
        // ====================== 动态添加新观察者 ======================
        System.out.println("\n>>> 添加紧急新闻监控系统");
        NewsObserver breakingNewsMonitor = new NewsObserver() {
            @Override
            public void update(String latestNews) {
                if (latestNews.contains("紧急") || latestNews.contains("预警")) {
                    System.out.println("⚠️【紧急新闻监控系统】检测到重要新闻: " + latestNews);
                }
            }
        };
        publisher.registerObserver(breakingNewsMonitor);
        
        // ====================== 发布第三条新闻 ======================
        System.out.println("\n>>> 发布第三条普通新闻");
        publisher.setLatestNews("本市新建图书馆将于下周开放");
        
        // ====================== 发布第四条紧急新闻 ======================
        System.out.println("\n>>> 发布第四条紧急新闻");
        publisher.setLatestNews("紧急：地铁三号线因设备故障暂停运营");
        
        // ====================== 模式优势演示 ======================
        System.out.println("\n================= 观察者模式优势总结 =================");
        System.out.println("1. 松耦合：新增和移除观察者不影响主题核心功能");
        System.out.println("2. 开闭原则：添加新观察者类型(如紧急新闻监控系统)不需修改主题代码");
        System.out.println("3. 广播通信：主题发布新闻时自动通知所有订阅者");
        System.out.println("4. 动态性：运行时动态添加/移除观察者");
        
        // ====================== 实际应用扩展 ======================
        System.out.println("\n实际应用扩展思路：");
        System.out.println("● 可按新闻类别实现主题分组（科技类、体育类等）");
        System.out.println("● 可加入新闻过滤系统，让用户只接收感兴趣的新闻");
        System.out.println("● 可实现优先级系统，重要新闻优先推送");
    }
}