package com.learning.leetcode.timer;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * @author 李芳
 * @since 2022/7/21
 */
public class TimingWheel {

    /**
     * 一个槽的时间间隔（时间轮的最小刻度）
     */
    private long tickMs;

    /**
     * 时间轮大小（槽的个数）
     */
    private int wheelSize;

    /**
     * 一轮的时间跨度（tickMs * wheelSize）
     */
    private long interval;

    /**
     * 时间轮指针，用来表示时间轮当前所处时间，currentTime是tickMs的整数倍。
     */
    private long currentTime;

    /**
     * 时间轮槽
     */
    private TimerTaskList[] buckets;

    /**
     * 上一层时间轮
     */
    private TimingWheel overflowWheel;

    private DelayQueue<TimerTaskList> delayQueue;

    public TimingWheel(long tickMs, int wheelSize, long currentTime, DelayQueue<TimerTaskList> delayQueue) {
        this.tickMs = tickMs;
        this.wheelSize = wheelSize;
        this.interval = tickMs * wheelSize;
        this.buckets = new TimerTaskList[wheelSize];
        this.currentTime = currentTime - (currentTime % tickMs);
        this.delayQueue = delayQueue;

        // 初始化槽
        for (int i = 0; i < wheelSize; i++) {
            buckets[i] = new TimerTaskList();
        }
    }

    public boolean add(TimerTaskEntry entry) {
        long expireMs = entry.getExpireMs();
        if (expireMs < tickMs + currentTime) {
            // 已经到期了
            return false;
        } else if (expireMs < currentTime + interval) {
            // 满足当前槽
            // 扔进当前时间轮的某个槽里,只有时间大于某个槽,才会放进去
            long virtualId = expireMs / tickMs;
            int index = (int) (virtualId % wheelSize);

            TimerTaskList bucket = buckets[index];
            bucket.addTask(entry);

            // 设置bucket 过期时间
            if (bucket.setExpiration(virtualId * tickMs)) {
                // 设好过期时间的bucket需要入队
                delayQueue.offer(bucket);
                return true;
            }
        } else {
            // 当前轮不满足，需要扔到上一轮
            TimingWheel timingWheel = getOverflowWheel();
            return timingWheel.add(entry);
        }
        return false;
    }


    /**
     * 推进指针
     *
     * @param timestamp
     */
    public void advanceLock(long timestamp) {
        if (timestamp > currentTime + tickMs) {
            currentTime = timestamp - (timestamp % tickMs);
            if (overflowWheel != null) {
                this.getOverflowWheel().advanceLock(timestamp);
            }
        }
    }

    public long getTickMs() {
        return tickMs;
    }

    public void setTickMs(long tickMs) {
        this.tickMs = tickMs;
    }

    public int getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(int wheelSize) {
        this.wheelSize = wheelSize;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public TimerTaskList[] getBuckets() {
        return buckets;
    }

    public void setBuckets(TimerTaskList[] buckets) {
        this.buckets = buckets;
    }

    public TimingWheel getOverflowWheel() {
        if (overflowWheel == null) {
            synchronized (this) {
                if (overflowWheel == null) {
                    overflowWheel = new TimingWheel(interval, wheelSize, currentTime, delayQueue);
                }
            }
        }
        return overflowWheel;
    }

    public void setOverflowWheel(TimingWheel overflowWheel) {
        this.overflowWheel = overflowWheel;
    }

    public DelayQueue<TimerTaskList> getDelayQueue() {
        return delayQueue;
    }

    public void setDelayQueue(DelayQueue<TimerTaskList> delayQueue) {
        this.delayQueue = delayQueue;
    }

    public static class TimerTaskList implements Delayed {

        /**
         * TimerTaskList 环形链表使用一个虚拟根节点root
         */
        static TimerTaskEntry root = new TimerTaskEntry(-1, null);

        /**
         * bucket过期时间
         */
        private AtomicLong expiration = new AtomicLong(-1);

        static {
            root.next = root;
            root.prev = root;
        }

        public boolean addTask(TimerTaskEntry entry) {
            boolean done = false;
            while (!done) {
                // 如果TimerTaskEntry已经在别的list中就先移除,同步代码块外面移除,避免死锁,一直到成功为止
                entry.remove();
                synchronized (this) {
                    if (entry.timedTaskList == null) {
                        // 加到链表的末尾
                        entry.timedTaskList = this;
                        TimerTaskEntry tail = root.prev;
                        entry.prev = tail;
                        entry.next = root;
                        tail.next = entry;
                        root.prev = entry;
                        done = true;
                    }
                }
            }
            return done;
        }

        /**
         * 从 TimedTaskList 移除指定的 timerTaskEntry
         *
         * @param entry
         */
        public void remove(TimerTaskEntry entry) {
            synchronized (this) {
                if (entry.getTimedTaskList().equals(this)) {
                    entry.next.prev = entry.prev;
                    entry.prev.next = entry.next;
                    entry.next = null;
                    entry.prev = null;
                    entry.timedTaskList = null;
                }
            }
        }

        /**
         * 移除所有
         */
        public synchronized void clear(Consumer<TimerTaskEntry> entry) {
            TimerTaskEntry head = root.next;
            while (!head.equals(root)) {
                remove(head);
                entry.accept(head);
                head = root.next;
            }
            expiration.set(-1L);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return Math.max(0, unit.convert(expiration.get() - System.currentTimeMillis(), TimeUnit.MILLISECONDS));
        }

        @Override
        public int compareTo(Delayed o) {
            if (o instanceof TimerTaskList) {
                return Long.compare(expiration.get(), ((TimerTaskList) o).expiration.get());
            }
            return 0;
        }

        public long getExpiration() {
            return expiration.get();
        }

        public boolean setExpiration(long expirationMs) {
            return expiration.getAndSet(expirationMs) != expirationMs;
        }
    }

    public static class TimerTaskEntry implements Comparable<TimerTaskEntry> {

        /**
         * 延迟时间（到期时间）
         */
        private long expireMs;

        private TimerTask timerTask;

        private volatile TimerTaskList timedTaskList;

        private TimerTaskEntry next;

        private TimerTaskEntry prev;

        public TimerTaskEntry(long expireMs, TimerTask timerTask) {
            this.expireMs = expireMs;
            this.timerTask = timerTask;
        }

        void remove() {
            TimerTaskList currentList = timedTaskList;
            while (currentList != null) {
                currentList.remove(this);
                currentList = timedTaskList;
            }
        }

        public long getExpireMs() {
            return expireMs;
        }

        public void setExpireMs(long expireMs) {
            this.expireMs = expireMs;
        }

        public TimerTask getTimerTask() {
            return timerTask;
        }

        public void setTimerTask(TimerTask timerTask) {
            this.timerTask = timerTask;
        }

        public TimerTaskList getTimedTaskList() {
            return timedTaskList;
        }

        public void setTimedTaskList(TimerTaskList timedTaskList) {
            this.timedTaskList = timedTaskList;
        }

        public TimerTaskEntry getNext() {
            return next;
        }

        public void setNext(TimerTaskEntry next) {
            this.next = next;
        }

        public TimerTaskEntry getPrev() {
            return prev;
        }

        public void setPrev(TimerTaskEntry prev) {
            this.prev = prev;
        }

        @Override
        public int compareTo(TimerTaskEntry o) {
            return ((int) (this.expireMs - o.expireMs));
        }
    }
}
