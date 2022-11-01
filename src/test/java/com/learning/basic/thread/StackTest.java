package com.learning.basic.thread;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author 李芳
 * @since 2022/9/27
 */
public class StackTest {

    private static LongAdder longAdder = new LongAdder();

    /**
     * -Xss512k
     * @param args
     */
    public static void main(String[] args) {

        try {
            a();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }catch (Error e){
            System.out.println("stack over flow");
        }

        System.out.println("------------------------------------------------------");
    }

    private static void a(){
        longAdder.increment();
        System.out.println("-------------------------递归调用 " +  longAdder.intValue());
        a();
    }

}
