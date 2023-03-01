package com.learning.middleware.mq.tx.core;

import org.springframework.core.NamedInheritableThreadLocal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * @author 李芳
 * @since 2022/9/19
 */
public final class TransactionMessageThreadLocal {

    private final static ThreadLocal<List<Long>> threadLocal = new NamedInheritableThreadLocal("tx-message");

    public static List<Long> getTransactionMessageIds() {
        List<Long> transactionMessageIds = threadLocal.get();
        if (transactionMessageIds == null) {
            transactionMessageIds = new CopyOnWriteArrayList<>();
            threadLocal.set(transactionMessageIds);
        }
        return transactionMessageIds;
    }

    public static void foreachTransactionMessage(Consumer<List<Long>> consumer) {
        consumer.accept(getTransactionMessageIds());
        threadLocal.remove();
    }
}
