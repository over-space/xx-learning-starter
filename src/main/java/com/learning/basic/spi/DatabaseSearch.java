package com.learning.basic.spi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/27
 */
public class DatabaseSearch implements Search{

    private static final Logger logger = LogManager.getLogger(DatabaseSearch.class);


    @Override
    public List<String> search(String keyword) {
        logger.info("调用DatabaseSearch.....");
        return Arrays.asList("1", "2", "3");
    }
}
