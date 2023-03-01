package com.learning.custom;

import com.learning.BaseTest;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lifang
 * @since 2022/1/25
 */
public class Custom002 extends BaseTest {


    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {

        for (int i = 0; i < 200000; i++) {

            ReorderExample reorderExample = new ReorderExample();

            executorService.execute(() -> {
                reorderExample.writer();
            });

            executorService.execute(() -> {
                reorderExample.reader();
            });
        }

        executorService.shutdown();
        System.out.println("------------------------------------------------------------------------------");
    }


    static class ReorderExample {

        private int a = 0;

        private boolean flag = false;

        public void writer() {
            this.a = 1;
            this.flag = true;
        }

        public void reader() {
            if (flag) {
                if (0 == (a * a)) {
                    System.out.println("程序重排序了，a : " + a);
                }
            }
        }
    }

}
