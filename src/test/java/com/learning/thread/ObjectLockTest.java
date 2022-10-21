package com.learning.thread;

import com.learning.BaseTest;
import jdk.internal.misc.Unsafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author 李芳
 * @since 2022/10/13
 */
public class ObjectLockTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(ObjectLockTest.class);

    Object lock = new Object();

    @Test
    protected void test() throws Exception {

        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);

        long markword1 = unsafe.getInt(lock, 0L);

        logger.info("object is locked : {}", markword1);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (lock){
                    long markword2 = unsafe.getInt(lock, 0L);
                    logger.info("object is locked : {}", markword2);
                }
            }).start();
        }

        long markword4= unsafe.getInt(lock, 0L);
        logger.info("object is locked : {}", markword4);
        sleep(8);
    }
}
