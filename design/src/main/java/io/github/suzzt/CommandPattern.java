package io.github.suzzt; /**
 * 命令模式（Command Pattern）实现：智能家居控制系统
 * 
 * <p><b>模式类型：</b>行为型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *     将请求封装成对象，从而使你可以用不同的请求对客户进行参数化、对请求排队或记录请求日志，
 *     以及支持可撤销的操作。命令模式将"发出请求的对象"和"接收与执行请求的对象"解耦。
 * </p>
 * 
 * <p><b>模式解决的问题：</b>
 *     当系统需要将请求的发送者和接收者解耦时，或者当系统需要支持命令的撤销/重做、
 *     排队执行和记录日志等功能时，命令模式提供了一种优雅的解决方案。
 * </p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 需要将操作请求与操作执行分离的系统</li>
 *   <li>✅ 需要支持可撤销/重做操作的系统</li>
 *   <li>✅ 需要实现任务队列或日志记录的系统</li>
 *   <li>✅ 需要支持宏命令（组合命令）的系统</li>
 *   <li>✅ 需要支持事务处理的系统</li>
 * </ul>
 */

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class CommandPattern {

    // ====================== 接收者类 (设备) ======================
    
    /**
     * 智能灯 - 命令接收者
     */
    static class SmartLight {
        private String location;
        private int brightness;
        private boolean isOn;
        
        public SmartLight(String location) {
            this.location = location;
            this.brightness = 50;
            this.isOn = false;
        }
        
        public void turnOn() {
            isOn = true;
            System.out.println(location + "的灯打开了");
        }
        
        public void turnOff() {
            isOn = false;
            System.out.println(location + "的灯关闭了");
        }
        
        public void setBrightness(int level) {
            if (level < 0) level = 0;
            if (level > 100) level = 100;
            brightness = level;
            System.out.println(location + "的灯亮度设置为: " + level + "%");
        }
        
        public void increaseBrightness() {
            setBrightness(brightness + 10);
        }
        
        public void decreaseBrightness() {
            setBrightness(brightness - 10);
        }
        
        public int getBrightness() {
            return brightness;
        }
        
        public boolean isOn() {
            return isOn;
        }
        
        public String getStatus() {
            return location + "的灯: " + (isOn ? "开启 (" + brightness + "%)" : "关闭");
        }
    }
    
    /**
     * 智能恒温器 - 命令接收者
     */
    static class Thermostat {
        private String location;
        private double temperature;
        private boolean isOn;
        
        public Thermostat(String location) {
            this.location = location;
            this.temperature = 22.0;
            this.isOn = true;
        }
        
        public void turnOn() {
            isOn = true;
            System.out.println(location + "的恒温器开启了");
        }
        
        public void turnOff() {
            isOn = false;
            System.out.println(location + "的恒温器关闭了");
        }
        
        public void setTemperature(double temp) {
            if (temp < 10) temp = 10;
            if (temp > 30) temp = 30;
            temperature = temp;
            System.out.println(location + "的温度设置为: " + temp + "°C");
        }
        
        public void increaseTemp() {
            setTemperature(temperature + 0.5);
        }
        
        public void decreaseTemp() {
            setTemperature(temperature - 0.5);
        }
        
        public double getTemperature() {
            return temperature;
        }
        
        public boolean isOn() {
            return isOn;
        }
        
        public String getStatus() {
            return location + "的恒温器: " + (isOn ? "开启 (" + temperature + "°C)" : "关闭");
        }
    }
    
    /**
     * 安全系统 - 命令接收者
     */
    static class SecuritySystem {
        private boolean armed;
        private boolean alarmActive;
        
        public SecuritySystem() {
            armed = false;
            alarmActive = false;
        }
        
        public void arm() {
            armed = true;
            System.out.println("安全系统已布防");
        }
        
        public void disarm() {
            armed = false;
            alarmActive = false;
            System.out.println("安全系统已撤防");
        }
        
        public void triggerAlarm() {
            if (armed && !alarmActive) {
                alarmActive = true;
                System.out.println("🚨 警报! 安全系统被触发");
            }
        }
        
        public void silenceAlarm() {
            if (alarmActive) {
                alarmActive = false;
                System.out.println("警报已静音");
            }
        }
        
        public boolean isArmed() {
            return armed;
        }
        
        public boolean isAlarmActive() {
            return alarmActive;
        }
        
        public String getStatus() {
            if (alarmActive) return "安全系统: 警报中!";
            return "安全系统: " + (armed ? "已布防" : "已撤防");
        }
    }
    
    /**
     * 电视 - 命令接收者
     */
    static class Television {
        private boolean isOn;
        private int volume;
        private int channel;
        
        public Television() {
            isOn = false;
            volume = 50;
            channel = 1;
        }
        
        public void turnOn() {
            isOn = true;
            System.out.println("电视已开启");
        }
        
        public void turnOff() {
            isOn = false;
            System.out.println("电视已关闭");
        }
        
        public void setVolume(int vol) {
            if (vol < 0) vol = 0;
            if (vol > 100) vol = 100;
            volume = vol;
            System.out.println("电视音量设置为: " + vol);
        }
        
        public void setChannel(int ch) {
            if (ch < 1) ch = 1;
            channel = ch;
            System.out.println("电视频道设置为: " + ch);
        }
        
        public void nextChannel() {
            setChannel(channel + 1);
        }
        
        public void prevChannel() {
            setChannel(channel - 1);
        }
        
        public void volumeUp() {
            setVolume(volume + 5);
        }
        
        public void volumeDown() {
            setVolume(volume - 5);
        }
        
        public boolean isOn() {
            return isOn;
        }
        
        public int getVolume() {
            return volume;
        }
        
        public int getChannel() {
            return channel;
        }
        
        public String getStatus() {
            if (!isOn) return "电视: 关闭";
            return "电视: 开启 (频道: " + channel + ", 音量: " + volume + ")";
        }
    }

    // ====================== 命令接口 ======================
    
    /**
     * 命令接口
     */
    interface Command {
        void execute();     // 执行命令
        void undo();        // 撤销命令
        String getName();   // 获取命令名称
    }

    // ====================== 具体命令类 ======================
    
    /**
     * 灯开关命令
     */
    static class LightOnCommand implements Command {
        private SmartLight light;
        
        public LightOnCommand(SmartLight light) {
            this.light = light;
        }
        
        @Override
        public void execute() {
            light.turnOn();
        }
        
        @Override
        public void undo() {
            light.turnOff();
        }
        
        @Override
        public String getName() {
            return light.location + "开灯";
        }
    }
    
    static class LightOffCommand implements Command {
        private SmartLight light;
        
        public LightOffCommand(SmartLight light) {
            this.light = light;
        }
        
        @Override
        public void execute() {
            light.turnOff();
        }
        
        @Override
        public void undo() {
            light.turnOn();
        }
        
        @Override
        public String getName() {
            return light.location + "关灯";
        }
    }
    
    /**
     * 灯亮度调整命令
     */
    static class LightBrightnessCommand implements Command {
        private SmartLight light;
        private int prevBrightness;
        private int newBrightness;
        
        public LightBrightnessCommand(SmartLight light, int brightness) {
            this.light = light;
            this.newBrightness = brightness;
        }
        
        @Override
        public void execute() {
            prevBrightness = light.getBrightness();
            light.setBrightness(newBrightness);
        }
        
        @Override
        public void undo() {
            light.setBrightness(prevBrightness);
        }
        
        @Override
        public String getName() {
            return "设置" + light.location + "灯亮度为" + newBrightness + "%";
        }
    }
    
    /**
     * 恒温器温度调整命令
     */
    static class ThermostatSetCommand implements Command {
        private Thermostat thermostat;
        private double prevTemp;
        private double newTemp;
        
        public ThermostatSetCommand(Thermostat thermostat, double temp) {
            this.thermostat = thermostat;
            this.newTemp = temp;
        }
        
        @Override
        public void execute() {
            prevTemp = thermostat.getTemperature();
            thermostat.setTemperature(newTemp);
        }
        
        @Override
        public void undo() {
            thermostat.setTemperature(prevTemp);
        }
        
        @Override
        public String getName() {
            return "设置" + thermostat.location + "温度为" + newTemp + "°C";
        }
    }
    
    /**
     * 安全系统布防/撤防命令
     */
    static class SecurityArmCommand implements Command {
        private SecuritySystem security;
        
        public SecurityArmCommand(SecuritySystem security) {
            this.security = security;
        }
        
        @Override
        public void execute() {
            security.arm();
        }
        
        @Override
        public void undo() {
            security.disarm();
        }
        
        @Override
        public String getName() {
            return "安全系统布防";
        }
    }
    
    static class SecurityDisarmCommand implements Command {
        private SecuritySystem security;
        
        public SecurityDisarmCommand(SecuritySystem security) {
            this.security = security;
        }
        
        @Override
        public void execute() {
            security.disarm();
        }
        
        @Override
        public void undo() {
            security.arm();
        }
        
        @Override
        public String getName() {
            return "安全系统撤防";
        }
    }
    
    /**
     * 电视开关命令
     */
    static class TVOnCommand implements Command {
        private Television tv;
        
        public TVOnCommand(Television tv) {
            this.tv = tv;
        }
        
        @Override
        public void execute() {
            tv.turnOn();
        }
        
        @Override
        public void undo() {
            tv.turnOff();
        }
        
        @Override
        public String getName() {
            return "打开电视";
        }
    }
    
    static class TVOffCommand implements Command {
        private Television tv;
        
        public TVOffCommand(Television tv) {
            this.tv = tv;
        }
        
        @Override
        public void execute() {
            tv.turnOff();
        }
        
        @Override
        public void undo() {
            tv.turnOn();
        }
        
        @Override
        public String getName() {
            return "关闭电视";
        }
    }
    
    /**
     * 宏命令 - 组合多个命令
     */
    static class MacroCommand implements Command {
        private Command[] commands;
        private String name;
        
        public MacroCommand(String name, Command... commands) {
            this.name = name;
            this.commands = commands;
        }
        
        @Override
        public void execute() {
            for (Command cmd : commands) {
                cmd.execute();
            }
        }
        
        @Override
        public void undo() {
            // 按相反顺序撤销
            for (int i = commands.length - 1; i >= 0; i--) {
                commands[i].undo();
            }
        }
        
        @Override
        public String getName() {
            return name + "宏命令";
        }
    }
    
    /**
     * 空命令 - 避免空值检查
     */
    static class NoCommand implements Command {
        @Override
        public void execute() {}
        
        @Override
        public void undo() {}
        
        @Override
        public String getName() {
            return "未定义命令";
        }
    }

    // ====================== 调用者类 ======================
    
    /**
     * 遥控器 - 命令调用者
     */
    static class RemoteControl {
        private Command[] onCommands;
        private Command[] offCommands;
        private Command undoCommand;
        private Deque<Command> commandHistory = new ArrayDeque<>();
        
        public RemoteControl() {
            // 10个开关按钮
            onCommands = new Command[10];
            offCommands = new Command[10];
            
            // 初始化为空命令
            Command noCmd = new NoCommand();
            for (int i = 0; i < 10; i++) {
                onCommands[i] = noCmd;
                offCommands[i] = noCmd;
            }
            undoCommand = noCmd;
        }
        
        /**
         * 设置按钮对应的命令
         * 
         * @param slot 按钮位置 (0-9)
         * @param onCmd 开命令
         * @param offCmd 关命令
         */
        public void setCommand(int slot, Command onCmd, Command offCmd) {
            if (slot < 0 || slot >= 10) {
                System.out.println("无效的按钮位置: " + slot);
                return;
            }
            
            onCommands[slot] = onCmd;
            offCommands[slot] = offCmd;
        }
        
        /**
         * 按下开按钮
         */
        public void pressOnButton(int slot) {
            if (slot < 0 || slot >= 10) {
                System.out.println("无效的按钮位置: " + slot);
                return;
            }
            
            onCommands[slot].execute();
            undoCommand = onCommands[slot];
            commandHistory.push(onCommands[slot]);
        }
        
        /**
         * 按下关按钮
         */
        public void pressOffButton(int slot) {
            if (slot < 0 || slot >= 10) {
                System.out.println("无效的按钮位置: " + slot);
                return;
            }
            
            offCommands[slot].execute();
            undoCommand = offCommands[slot];
            commandHistory.push(offCommands[slot]);
        }
        
        /**
         * 撤销上一个命令
         */
        public void undoLastCommand() {
            if (!commandHistory.isEmpty()) {
                Command lastCommand = commandHistory.pop();
                System.out.println("撤销: " + lastCommand.getName());
                lastCommand.undo();
                undoCommand = lastCommand;
            } else {
                System.out.println("没有可以撤销的命令");
            }
        }
        
        /**
         * 显示按钮配置
         */
        public void displayButtons() {
            System.out.println("\n===== 遥控器设置 =====");
            for (int i = 0; i < onCommands.length; i++) {
                System.out.printf("[按钮%d] ON: %-30s  OFF: %s\n", 
                    i, onCommands[i].getName(), offCommands[i].getName());
            }
            System.out.println("=====================");
        }
        
        /**
         * 重做上一次撤销的命令
         */
        public void redoLastUndo() {
            if (undoCommand != null && !(undoCommand instanceof NoCommand)) {
                System.out.println("重做: " + undoCommand.getName());
                undoCommand.execute();
                commandHistory.push(undoCommand);
            } else {
                System.out.println("没有可以重做的命令");
            }
        }
    }
    
    /**
     * 语音助手 - 另一种调用者
     */
    static class VoiceAssistant {
        private Map<String, Command> commands = new HashMap<>();
        
        public void registerCommand(String voiceCommand, Command command) {
            commands.put(voiceCommand.toLowerCase(), command);
        }
        
        public void executeVoiceCommand(String command) {
            Command cmd = commands.get(command.toLowerCase());
            if (cmd != null) {
                System.out.println("\n🎤 语音指令: " + command);
                cmd.execute();
            } else {
                System.out.println("不支持的语音指令: " + command);
            }
        }
    }

    // ====================== 客户端代码 ======================
    
    public static void main(String[] args) {
        System.out.println("============= 智能家居控制系统 =============");
        
        // 1. 创建接收者设备
        SmartLight livingRoomLight = new SmartLight("客厅");
        SmartLight kitchenLight = new SmartLight("厨房");
        Thermostat livingRoomThermostat = new Thermostat("客厅");
        SecuritySystem securitySystem = new SecuritySystem();
        Television tv = new Television();
        
        // 2. 创建命令对象
        Command livingRoomLightOn = new LightOnCommand(livingRoomLight);
        Command livingRoomLightOff = new LightOffCommand(livingRoomLight);
        Command livingRoomLightDim = new LightBrightnessCommand(livingRoomLight, 30);
        
        Command kitchenLightOn = new LightOnCommand(kitchenLight);
        Command kitchenLightOff = new LightOffCommand(kitchenLight);
        
        Command livingRoomThermoSet = new ThermostatSetCommand(livingRoomThermostat, 24.5);
        Command securityArm = new SecurityArmCommand(securitySystem);
        Command securityDisarm = new SecurityDisarmCommand(securitySystem);
        Command tvOn = new TVOnCommand(tv);
        Command tvOff = new TVOffCommand(tv);
        
        // 3. 创建宏命令
        Command movieNightMacro = new MacroCommand("电影之夜",
            livingRoomLightDim,
            tvOn,
            new ThermostatSetCommand(livingRoomThermostat, 22.0)
        );
        
        Command goodNightMacro = new MacroCommand("晚安",
            livingRoomLightOff,
            kitchenLightOff,
            new ThermostatSetCommand(livingRoomThermostat, 20.0),
            securityArm
        );
        
        Command allOffMacro = new MacroCommand("全部关闭",
            livingRoomLightOff,
            kitchenLightOff,
            tvOff,
            securityArm
        );
        
        // 4. 创建调用者 - 遥控器
        RemoteControl remote = new RemoteControl();
        
        // 配置遥控器按钮
        remote.setCommand(0, livingRoomLightOn, livingRoomLightOff);
        remote.setCommand(1, kitchenLightOn, kitchenLightOff);
        remote.setCommand(2, livingRoomThermoSet, null); // 没有关闭按钮
        remote.setCommand(3, securityDisarm, securityArm);
        remote.setCommand(4, tvOn, tvOff);
        remote.setCommand(5, movieNightMacro, null);
        remote.setCommand(6, goodNightMacro, null);
        remote.setCommand(7, allOffMacro, null);
        
        // 显示遥控器设置
        remote.displayButtons();
        
        // 5. 测试遥控器操作
        System.out.println("\n>>> 基本操作测试 <<<");
        remote.pressOnButton(0);    // 客厅开灯
        remote.pressOnButton(1);     // 厨房开灯
        remote.pressOnButton(3);    // 安全系统撤防
        remote.pressOnButton(4);     // 开电视
        
        // 撤销操作
        System.out.println("\n>>> 撤销操作测试 <<<");
        remote.undoLastCommand();  // 撤销开电视
        remote.undoLastCommand();  // 撤销安全系统撤防
        
        // 重做操作
        System.out.println("\n>>> 重做操作测试 <<<");
        remote.redoLastUndo();      // 重做安全系统撤防
        remote.redoLastUndo();      // 重做开电视
        
        // 宏命令操作
        System.out.println("\n>>> 宏命令测试 <<<");
        remote.pressOnButton(5);    // 电影之夜宏命令
        remote.pressOffButton(0);   // 客厅关灯(覆盖之前的开灯)
        
        // 使用遥控器撤销多个操作
        System.out.println("\n>>> 多级撤销测试 <<<");
        for (int i = 0; i < 4; i++) {
            remote.undoLastCommand();
        }
        
        // 6. 创建语音助手
        System.out.println("\n>>> 语音助手测试 <<<");
        VoiceAssistant voiceAssistant = new VoiceAssistant();
        voiceAssistant.registerCommand("打开客厅灯", livingRoomLightOn);
        voiceAssistant.registerCommand("关闭客厅灯", livingRoomLightOff);
        voiceAssistant.registerCommand("打开电视", tvOn);
        voiceAssistant.registerCommand("晚安模式", goodNightMacro);
        voiceAssistant.registerCommand("看电影", movieNightMacro);
        voiceAssistant.registerCommand("关灯关电视", allOffMacro);
        voiceAssistant.registerCommand("提高温度", new Command() {
            @Override
            public void execute() {
                livingRoomThermostat.increaseTemp();
            }
            
            @Override
            public void undo() {
                livingRoomThermostat.decreaseTemp();
            }
            
            @Override
            public String getName() {
                return "提高温度";
            }
        });
        
        // 执行语音命令
        voiceAssistant.executeVoiceCommand("打开客厅灯");
        voiceAssistant.executeVoiceCommand("打开电视");
        voiceAssistant.executeVoiceCommand("看电影");    // 宏命令
        voiceAssistant.executeVoiceCommand("提高温度");
        voiceAssistant.executeVoiceCommand("晚安模式");
        
        // 7. 模拟安全事件
        System.out.println("\n>>> 安全事件测试 <<<");
        remote.pressOffButton(3);   // 安全系统布防
        securitySystem.triggerAlarm(); // 触发警报
        voiceAssistant.executeVoiceCommand("关闭警报");  // 无此命令
        new SecurityDisarmCommand(securitySystem).execute(); // 直接撤防
        
        // 8. 显示设备状态
        System.out.println("\n>>> 设备状态 <<<");
        System.out.println(livingRoomLight.getStatus());
        System.out.println(kitchenLight.getStatus());
        System.out.println(livingRoomThermostat.getStatus());
        System.out.println(securitySystem.getStatus());
        System.out.println(tv.getStatus());
        
        // 模式优势总结
        System.out.println("命令模式优势总结：");
        System.out.println("1. 解耦设计：发送者与接收者无直接依赖");
        System.out.println("2. 灵活扩展：易于添加新命令和设备");
        System.out.println("3. 宏命令支持：组合多个操作为一个原子操作");
        System.out.println("4. 队列支持：命令可排队执行");
        System.out.println("5. 撤销/重做：内置历史记录支持撤销和重做");
        System.out.println("6. 多态调用：不同设备统一执行接口");
        
        // 模式应用场景
        System.out.println("命令模式实际应用场景：");
        System.out.println("① 智能家居控制系统（如本示例）");
        System.out.println("② GUI操作与菜单系统");
        System.out.println("③ 事务处理与数据库操作");
        System.out.println("④ 任务调度系统");
        System.out.println("⑤ 交互式游戏控制");
    }
}