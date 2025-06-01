package io.github.suzzt;

/**
 * 状态模式（State Pattern）实现：音乐播放器状态管理
 * 
 * <p><b>模式类型：</b>行为型设计模式</p>
 * 
 * <p><b>核心思想：</b>
 *   允许对象在其内部状态改变时改变其行为，使其表现得如同修改了自身类。将状态相关的行为封装到独立的状态类中，
 *   并让上下文对象在行为执行时委托给当前状态对象。
 * </p>
 * 
 * <p><b>适用场景：</b></p>
 * <ul>
 *   <li>✅ 对象行为取决于其状态，且需根据状态改变行为</li>
 *   <li>✅ 需要消除大量条件语句（if-else/switch）的场景</li>
 *   <li>✅ 存在复杂的状态转换逻辑时</li>
 *   <li>✅ 系统有多个状态，状态间的转换规则清晰</li>
 *   <li>❌ 状态变化很简单，仅有少量状态时的过度设计</li>
 * </ul>
 * 
 * <p><b>模式优势：</b></p>
 * <ul>
 *   <li>⭐ 消除庞大的条件分支语句</li>
 *   <li>⭐ 简化上下文类的复杂度</li>
 *   <li>⭐ 通过多态行为动态改变对象功能</li>
 *   <li>⭐ 符合开闭原则（扩展状态只需添加新类）</li>
 * </ul>
 */

public class StatePatternExample {

    // 1. 上下文类 - 音乐播放器
    static class MusicPlayer {
        private PlayerState currentState;
        private double playbackPosition;
        private String currentTrack;
        
        public MusicPlayer() {
            // 初始状态为停止状态
            this.currentState = new StoppedState();
            this.playbackPosition = 0.0;
            this.currentTrack = "";
        }
        
        // 设置当前状态（供状态对象使用）
        protected void setState(PlayerState state) {
            System.out.printf("【状态转换】%s → %s%n",
                currentState.getClass().getSimpleName(),
                state.getClass().getSimpleName()
            );
            this.currentState = state;
        }
        
        // 委托给当前状态对象的操作
        public void play() {
            currentState.play(this);
        }
        
        public void pause() {
            currentState.pause(this);
        }
        
        public void stop() {
            currentState.stop(this);
        }
        
        public void nextTrack() {
            currentState.nextTrack(this);
        }
        
        public void previousTrack() {
            currentState.previousTrack(this);
        }
        
        // 内部操作方法（供状态对象使用）
        protected void startPlayback() {
            System.out.printf("▶ 开始播放: %s (位置: %.1f)%n", currentTrack, playbackPosition);
        }
        
        protected void pausePlayback() {
            System.out.printf("⏸ 暂停播放: %s (位置: %.1f)%n", currentTrack, playbackPosition);
        }
        
        protected void stopPlayback() {
            System.out.printf("⏹ 停止播放: %s%n", currentTrack);
            playbackPosition = 0.0;
        }
        
        protected void changeTrack(int direction) {
            if (direction > 0) {
                System.out.println("⏩ 切换到下一首");
                // 实际业务中这里会有处理逻辑
            } else {
                System.out.println("⏪ 切换到上一首");
            }
        }
        
        protected void loadTrack(String track) {
            currentTrack = track;
            System.out.printf("📥 加载音轨: %s%n", track);
        }
        
        protected void setPlaybackPosition(double position) {
            playbackPosition = position;
        }
        
        public String getCurrentStateName() {
            return currentState.getClass().getSimpleName();
        }
        
        public String getCurrentTrack() {
            return currentTrack;
        }
    }
    
    // 2. 状态接口 - 定义所有支持的操作
    interface PlayerState {
        void play(MusicPlayer player);
        void pause(MusicPlayer player);
        void stop(MusicPlayer player);
        void nextTrack(MusicPlayer player);
        void previousTrack(MusicPlayer player);
    }
    
    // 3.1 具体状态类 - 准备状态
    static class ReadyState implements PlayerState {
        @Override
        public void play(MusicPlayer player) {
            if (player.getCurrentTrack().isEmpty()) {
                System.out.println("❌ 未选择任何音轨，无法播放");
                return;
            }
            
            System.out.println("📡 准备播放，加载资源...");
            player.setState(new PlayingState());
            player.startPlayback();
        }
        
        @Override
        public void pause(MusicPlayer player) {
            System.out.println("❌ 未在播放状态，无法暂停");
        }
        
        @Override
        public void stop(MusicPlayer player) {
            System.out.println("🔄 重置播放器");
            player.stopPlayback();
        }
        
        @Override
        public void nextTrack(MusicPlayer player) {
            System.out.println("⏭️ 选择下一首音轨");
            player.changeTrack(1);
        }
        
        @Override
        public void previousTrack(MusicPlayer player) {
            System.out.println("⏮️ 选择上一首音轨");
            player.changeTrack(-1);
        }
    }
    
    // 3.2 具体状态类 - 播放状态
    static class PlayingState implements PlayerState {
        @Override
        public void play(MusicPlayer player) {
            System.out.println("🔄 已在播放中，忽略播放操作");
        }
        
        @Override
        public void pause(MusicPlayer player) {
            System.out.println("⏸️ 准备暂停播放");
            player.setState(new PausedState());
            player.pausePlayback();
        }
        
        @Override
        public void stop(MusicPlayer player) {
            System.out.println("🛑 准备停止播放");
            player.setState(new StoppedState());
            player.stopPlayback();
        }
        
        @Override
        public void nextTrack(MusicPlayer player) {
            System.out.println("⏭️ 切换到下一首并继续播放");
            player.changeTrack(1);
        }
        
        @Override
        public void previousTrack(MusicPlayer player) {
            System.out.println("⏮️ 切换到上一首并继续播放");
            player.changeTrack(-1);
        }
    }
    
    // 3.3 具体状态类 - 暂停状态
    static class PausedState implements PlayerState {
        @Override
        public void play(MusicPlayer player) {
            System.out.println("▶️ 恢复播放");
            player.setState(new PlayingState());
            player.startPlayback();
        }
        
        @Override
        public void pause(MusicPlayer player) {
            System.out.println("🔄 已在暂停状态，忽略操作");
        }
        
        @Override
        public void stop(MusicPlayer player) {
            System.out.println("🛑 准备停止播放");
            player.setState(new StoppedState());
            player.stopPlayback();
        }
        
        @Override
        public void nextTrack(MusicPlayer player) {
            System.out.println("⏭️ 选择下一首音轨并准备播放");
            player.changeTrack(1);
            player.setState(new ReadyState());
        }
        
        @Override
        public void previousTrack(MusicPlayer player) {
            System.out.println("⏮️ 选择上一首音轨并准备播放");
            player.changeTrack(-1);
            player.setState(new ReadyState());
        }
    }
    
    // 3.4 具体状态类 - 停止状态
    static class StoppedState implements PlayerState {
        @Override
        public void play(MusicPlayer player) {
            System.out.println("🚀 准备开始播放");
            player.setState(new PlayingState());
            player.setPlaybackPosition(0.0);
            player.startPlayback();
        }
        
        @Override
        public void pause(MusicPlayer player) {
            System.out.println("❌ 停止状态下无法暂停");
        }
        
        @Override
        public void stop(MusicPlayer player) {
            System.out.println("🔄 已在停止状态，忽略操作");
        }
        
        @Override
        public void nextTrack(MusicPlayer player) {
            System.out.println("⏭️ 选择下一首音轨");
            player.changeTrack(1);
        }
        
        @Override
        public void previousTrack(MusicPlayer player) {
            System.out.println("⏮️ 选择上一首音轨");
            player.changeTrack(-1);
        }
    }
    
    // 4. 演示代码
    public static void main(String[] args) {
        System.out.println("===== 音乐播放器 - 状态模式演示 =====");
        
        MusicPlayer player = new MusicPlayer();
        // 加载音轨但未播放
        player.loadTrack("Imagine - John Lennon");
        
        System.out.println("\n[场景1] 常规播放流程");
        player.play();     // 进入播放状态
        player.pause();    // 进入暂停状态
        player.play();     // 恢复播放
        player.stop();     // 进入停止状态
        
        System.out.println("\n[场景2] 无效操作处理");
        player.pause();    // 尝试在停止状态下暂停 (应失败)
        
        System.out.println("\n[场景3] 曲目切换操作");
        player.nextTrack();    // 切换音轨 (进入准备状态)
        player.play();         // 播放新音轨
        
        System.out.println("\n[场景4] 状态边界测试");
        player.stop();         // 停止播放
        player.stop();         // 再次停止 (应忽略)
        player.previousTrack();// 切换回之前音轨
        player.play();         // 重新开始播放
        player.play();         // 播放中再次点击播放 (应忽略)
        
        System.out.println("\n===== 最终状态 =====");
        System.out.println("当前状态: " + player.getCurrentStateName());
        System.out.println("当前音轨: " + player.getCurrentTrack());
    }
}