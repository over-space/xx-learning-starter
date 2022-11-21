package com.learning.basic.thread;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李芳
 * @since 2022/11/9
 */
public class ThreadLocalTest extends BaseTest {

    private ThreadLocal<Map<String, Integer>> threadLocal = new InheritableThreadLocal<>();

    @Test
    public void test() {

        super.runAsync(() -> {

            Map<String, Integer> map = threadLocal.get() == null ? new HashMap<>() : threadLocal.get();

            map.put("hello", 1);

            threadLocal.set(map);

            logger.info("threadlocal map: {}", map);


            runAsync(() -> {

                Map<String, Integer> map1 = threadLocal.get();

                map1.put("world", 2);

                logger.info("map == map1 : {}", map == map1);

                logger.info("threadlocal map: {}", map1);
                return 2;
            });

            logger.info("threadlocal map: {}", map);

            return 1;
        });


        sleep(5);

        logger.info("threadlocal map: {}", threadLocal.get());
    }

}
