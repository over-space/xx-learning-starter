package com.learning.jol;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

public class ClassLayoutTest extends BaseTest {

    @Test
    void test() {
        sleep(10);
        Object obj = new Object();
        logger.info("obj layout : {}", ClassLayout.parseInstance(obj).toPrintable());
    }

}
