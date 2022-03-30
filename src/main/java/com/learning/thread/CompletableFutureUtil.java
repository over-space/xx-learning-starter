package com.learning.thread;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lifang
 * @since 30/03/2022
 */
public class CompletableFutureUtil {

    /**
     * @param allList 需要分页执行的数据
     * @param function 函数式接口
     */
    public static <T, R> List<R> submit(List<T> allList, Function<List<T>, R> function) {
        return submit(100, allList, function);
    }

    /**
     * @param pageSize 每页数量
     * @param allList 需要分页执行的数据
     * @param function 函数式接口
     */
    public static <T, R> List<R> submit(int pageSize, List<T> allList, Function<List<T>, R> function) {
        List<R> result = new ArrayList<>(allList.size() / pageSize);

        submit(pageSize, allList, function, result::add);

        return result;
    }

    /**
     * @param pageSize 每页数量
     * @param allList 需要分页执行的数据
     * @param function 函数式接口
     */
    public static <T, R> void submit(int pageSize, List<T> allList, Function<List<T>, R> function, Consumer<R> consumer) {

        List<List<T>> partition = Lists.partition(allList, pageSize);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 15,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(partition.size()),
                new ThreadFactoryBuilder().setNameFormat("completable-future-util-%d").build());

        List<CompletableFuture<R>> futureList = partition.stream()
                .map(list -> CompletableFuture.supplyAsync(() -> function.apply(list), threadPoolExecutor))
                .collect(Collectors.toList());

        // 等待线程执行结束
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();
        threadPoolExecutor.shutdown();

        futureList.stream()
                .map(future -> future.getNow(null))
                .filter(Objects::nonNull)
                .forEach(consumer::accept);
    }
}
