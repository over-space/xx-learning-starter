package com.learning.basic.design.decorator;

/**
 * @author 李芳
 * @since 2022/11/7
 */
public abstract class AbstractGoodsDecorator implements GoodsComponent {

    private GoodsComponent goodsComponent;

    public AbstractGoodsDecorator(GoodsComponent goodsComponent) {
        this.goodsComponent = goodsComponent;
    }

    @Override
    public double price() {
        return goodsComponent.price();
    }
}
