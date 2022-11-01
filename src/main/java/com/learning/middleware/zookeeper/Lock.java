package com.learning.middleware.zookeeper;

/**
 * @author 李芳
 * @since 2022/8/23
 */
public interface Lock {

    /**
     * 尝试获取锁
     */
    boolean tryLock();


    void lock();

    /**
     * 释放锁
     */
    void release();
}
