package com.learning.basic.design.decorator;

/**
 * @author 李芳
 * @since 2022/11/7
 */
public class FullReductionDecorator extends AbstractGoodsDecorator {

    public FullReductionDecorator(GoodsComponent goodsComponent) {
        super(goodsComponent);
    }

    @Override
    public double price() {
        // 满200减20.
        double price = super.price();
        return price >= 200 ? price - 25 : price;
    }
}
