package com.learning.basic.design.decorator;

/**
 * @author 李芳
 * @since 2022/11/7
 */
public class DiscountDecorator extends AbstractGoodsDecorator {

    public DiscountDecorator(GoodsComponent goodsComponent) {
        super(goodsComponent);
    }

    @Override
    public double price() {
        // 8折
        return 0.8 * super.price();
    }
}
