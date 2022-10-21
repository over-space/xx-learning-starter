package com.learning.thread;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 李芳
 * @since 2022/10/17
 */
public class ABCPrintTest extends BaseTest {

    private ReentrantLock LOCK = new ReentrantLock();
    private Condition aPrint = LOCK.newCondition();
    private Condition bPrint = LOCK.newCondition();
    private Condition cPrint = LOCK.newCondition();



    @Test
    @Override
    protected void test() {
        runAsync(() -> {
            try {
                LOCK.lock();
                bPrint.await();
                cPrint.await();
                print("A");
                bPrint.signal();
            }catch (Exception e){

            }finally {
                LOCK.unlock();
            }
            return true;
        });

        runAsync(() -> {
            try {
                LOCK.lock();
                aPrint.await();
                cPrint.await();
                print("B");
                cPrint.signal();
            }catch (Exception e){

            }finally {
                LOCK.unlock();
            }
            return true;
        });
        runAsync(() -> {
            try {
                LOCK.lock();
                aPrint.await();
                bPrint.await();
                print("C");
                aPrint.signal();
            }catch (Exception e){

            }finally {
                LOCK.unlock();
            }
            return true;
        });
        sleep(5);
    }
}
