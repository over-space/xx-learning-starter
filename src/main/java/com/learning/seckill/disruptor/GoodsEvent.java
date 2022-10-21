package com.learning.seckill.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 消息体
 */
public class GoodsEvent implements EventFactory<GoodsEvent> {

    private Long goodsNum;

    public Long getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Long goodsNum) {
        this.goodsNum = goodsNum;
    }

    @Override
    public GoodsEvent newInstance() {
        return new GoodsEvent();
    }
}
