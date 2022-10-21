package com.learning.aop;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 李芳
 * @since 2022/10/20
 */
@SpringBootTest
public class AdviceTest extends BaseTest {

    @Resource
    private AdviceService adviceService;

    @Test
    public void test(){
        adviceService.test();
    }

}
