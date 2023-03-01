package com.learning.seckill.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lifang
 * @since 3/22/2022
 */
public class SeckillThreadPool {

    public int threadNum;

    private Map<Integer, ExecutorService> executorServiceMap;

    private volatile static SeckillThreadPool threadPool;

    protected SeckillThreadPool(int threadNum) {
        this.threadNum = threadNum;
        this.executorServiceMap = new HashMap<>(threadNum);
    }

    public static SeckillThreadPool getInstance(int threadNum) {
        if (threadPool == null) {
            synchronized (SeckillThreadPool.class) {
                if (threadPool == null) {
                    threadPool = new SeckillThreadPool(threadNum);
                }
            }
        }
        return threadPool;
    }

    public ExecutorService getExecutorService(String goodsNum) {
        Integer threadHashKey = getThreadHashKey(goodsNum);
        return executorServiceMap.computeIfAbsent(threadHashKey, (key) -> {
            // 利用SingleThread的有序性
            return Executors.newSingleThreadExecutor();
        });
    }

    public Integer getThreadHashKey(String goodsNum) {
        return goodsNum.hashCode() % threadNum;
    }
}
