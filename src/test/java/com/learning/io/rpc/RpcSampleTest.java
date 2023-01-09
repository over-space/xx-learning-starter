package com.learning.io.rpc;

import com.learning.BaseTest;
import com.learning.io.rpc.prototype.RpcHeader;
import com.learning.io.rpc.proxy.RpcProxy;
import com.learning.io.rpc.service.Calc;
import com.learning.io.rpc.service.Fly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcSampleTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(RpcSampleTest.class);

    @Test
    void testRPC(){
        RpcServerStarter.asyncStartNettyServer();

        testRpcClient(RpcHeader.PROTOCOL_RPC);

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRpcByNettyHttp(){
        RpcServerStarter.asyncStartHttpServer("127.0.0.1", 9090);

        testRpcClient(RpcHeader.PROTOCOL_NETTY_HTTP);

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRpcByHttp(){
        RpcServerStarter.asyncStartHttpServer("127.0.0.1", 9090);

        testRpcClient(RpcHeader.PROTOCOL_HTTP);

        try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void testRpcClient(String prototype) {

        try {

            // Fly fly = proxyGet(Fly.class);
            // fly.start("hello world!");

            AtomicInteger atomicInteger = new AtomicInteger(1);
            for (int i = 0; i < 20; i++) {
                CompletableFuture.runAsync(() -> {
                    Fly fly = RpcProxy.proxyGet(Fly.class, prototype);
                    String threadName = fly.getName("hello world, " + atomicInteger.incrementAndGet());

                    printLine();
                    logger.info("Fly#getName : {}", threadName);
                    printLine();
                    printLine();


                    Calc calc = RpcProxy.proxyGet(Calc.class, prototype);
                    int num1 = calc.add(2, 3);
                    Assertions.assertEquals(num1, 5);

                    int num2 = calc.subtract(5, 2);
                    Assertions.assertEquals(num2, 3);

                }).exceptionally(e -> {
                    if(e instanceof Exception){
                        logger.error(e.getMessage(), e);
                    }
                    return null;
                });
            }

        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }

    }

}
