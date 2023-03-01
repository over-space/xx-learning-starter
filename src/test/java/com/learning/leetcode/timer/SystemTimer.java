package com.learning.leetcode.timer;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 李芳
 * @since 2022/7/21
 */
public class SystemTimer implements Timer {

    /**
     * 底层时间轮
     */
    private TimingWheel timeWheel;
    /**
     * 一个Timer只有一个延时队列
     */
    private DelayQueue<TimingWheel.TimerTaskList> delayQueue = new DelayQueue<>();

    /**
     * 过期任务执行线程
     */
    private ExecutorService workerThreadPool;

    /**
     * 轮询delayQueue获取过期任务线程
     */
    private ExecutorService bossThreadPool;

    public SystemTimer() {
        this.timeWheel = new TimingWheel(1, 20, System.currentTimeMillis(), delayQueue);
        this.workerThreadPool = Executors.newFixedThreadPool(100);
        this.bossThreadPool = Executors.newFixedThreadPool(1);
        // 20ms推动一次时间轮运转
        this.bossThreadPool.submit(() -> {
            for (; ; ) {
                this.advanceClock(20);
            }
        });
    }

    public void addTimerTaskEntry(TimingWheel.TimerTaskEntry entry) {
        if (!timeWheel.add(entry)) {
            // 已经过期了
            TimerTask timerTask = entry.getTimerTask();
            workerThreadPool.submit(timerTask);
        }
    }

    @Override
    public void add(TimerTask timerTask) {
        TimingWheel.TimerTaskEntry entry = new TimingWheel.TimerTaskEntry(timerTask.getDelayMs() + System.currentTimeMillis(), timerTask);
        timerTask.setTimerTaskEntry(entry);
        addTimerTaskEntry(entry);
    }

    /**
     * 推动指针运转获取过期任务
     *
     * @param timeout 时间间隔
     * @return
     */
    @Override
    public synchronized void advanceClock(long timeout) {
        try {
            TimingWheel.TimerTaskList bucket = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if (bucket != null) {
                // 推进时间
                timeWheel.advanceLock(bucket.getExpiration());
                // 执行过期任务(包含降级)
                bucket.clear(this::addTimerTaskEntry);
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void shutdown() {
        this.bossThreadPool.shutdown();
        this.workerThreadPool.shutdown();
        this.timeWheel = null;
    }
}
