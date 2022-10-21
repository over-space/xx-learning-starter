package com.learning.leetcode.timer;

/**
 * @author 李芳
 * @since 2022/7/21
 */
public class TimerTask implements Runnable{

    /**
     * 延时时间
     */
    private long delayMs;

    /**
     * 任务所在的entry
     */
    private TimingWheel.TimerTaskEntry timerTaskEntry;

    @Override
    public void run() {
        System.out.println("----------------------------------------------------");
    }

    public TimerTask(long delayMs) {
        this.delayMs = delayMs;
    }

    public long getDelayMs() {
        return delayMs;
    }

    public void setDelayMs(long delayMs) {
        this.delayMs = delayMs;
    }

    public TimingWheel.TimerTaskEntry getTimerTaskEntry() {
        return timerTaskEntry;
    }

    public synchronized void setTimerTaskEntry(TimingWheel.TimerTaskEntry entry) {
        // 如果这个timetask已经被一个已存在的TimerTaskEntry持有,先移除一个
        if (timerTaskEntry != null && timerTaskEntry != entry) {
            timerTaskEntry.remove();
        }
        timerTaskEntry = entry;
    }
}
