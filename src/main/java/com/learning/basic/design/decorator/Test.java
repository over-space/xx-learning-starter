package com.learning.basic.design.decorator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 李芳
 * @since 2022/11/7
 */
public class Test {

    private static final Logger logger = LogManager.getLogger(Test.class);


    public static void main(String[] args) {
        GoodsComponent item = new GoodsConcreteComponent();

        logger.info("原价：{}", item.price());

        item = new DiscountDecorator(item);

        logger.info("8折：{}", item.price());

        item = new FullReductionDecorator(item);

        logger.info("满200减25：{}", item.price());
    }
}
