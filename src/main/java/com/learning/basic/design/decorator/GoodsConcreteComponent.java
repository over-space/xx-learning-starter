package com.learning.basic.design.decorator;

/**
 * @author 李芳
 * @since 2022/11/7
 */
public class GoodsConcreteComponent implements GoodsComponent {

    @Override
    public double price() {
        return 200.00;
    }
}
