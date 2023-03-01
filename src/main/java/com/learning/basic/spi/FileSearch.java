package com.learning.basic.spi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @author 李芳
 * @since 2022/9/27
 */
public class FileSearch implements Search {

    private static final Logger logger = LogManager.getLogger(DatabaseSearch.class);


    @Override
    public List<String> search(String keyword) {
        logger.info("调用FileSearch.....");
        return Arrays.asList("a", "b", "c");
    }
}
