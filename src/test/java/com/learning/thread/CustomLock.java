package com.learning.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author 李芳
 * @since 2022/10/17
 */
public class CustomLock extends AbstractQueuedSynchronizer {

    @Override
    protected boolean tryAcquire(int arg) {
        return compareAndSetState(0, 1);
    }

    @Override
    protected boolean tryRelease(int arg) {
        return compareAndSetState(1, 0);
    }

    public static void main(String[] args) throws InterruptedException {
        CustomLock lock = new CustomLock();

        new Thread(() -> {
            System.out.println("thread1 acquire mutex");
            lock.acquire(1);
            // 获取资源后sleep保持
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch(InterruptedException ignore) {

            }
            lock.release(1);
            System.out.println("thread1 release mutex");
        }).start();

        new Thread(() -> {
            // 保证线程2在线程1启动后执行
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch(InterruptedException ignore) {

            }
            // 等待线程1 sleep结束释放资源
            lock.acquire(1);
            System.out.println("thread2 acquire mutex");
            lock.release(1);
        }).start();

        System.out.println("--------------------------------------------");
        TimeUnit.SECONDS.sleep(5);
    }
}
