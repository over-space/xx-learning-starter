package com.learning.aop;

import com.learning.BaseApplication;
import com.learning.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author 李芳
 * @since 2022/10/20
 */
@SpringBootTest(classes = BaseApplication.class,webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdviceTest extends BaseTest {

    @Resource
    private AdviceService adviceService;

    @Test
    public void test(){
        adviceService.test();
    }

}
