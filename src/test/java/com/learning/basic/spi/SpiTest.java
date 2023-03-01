package com.learning.basic.spi;

import com.learning.BaseTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

/**
 * @author 李芳
 * @since 2022/9/27
 */
public class SpiTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger(SpiTest.class);


    @Test
    public void test() {
        ServiceLoader<FileSearch> serviceLoaderList = ServiceLoader.load(FileSearch.class);
        for (FileSearch fileSearch : serviceLoaderList) {
            logger.info("spi result:{}", fileSearch.search("file"));
        }
    }

}
