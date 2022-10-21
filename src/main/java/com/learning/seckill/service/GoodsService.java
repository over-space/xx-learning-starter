package com.learning.seckill.service;

/**
 * @author lifang
 * @since 2022/3/21
 */
public interface GoodsService {

    boolean seckillByMySQL(String goodsNum);

    boolean seckillByRedisV1(String goodsNum);

    boolean seckillByRedisV2(String goodsNum);

    boolean seckillByThreadV1(String goodsNum);

    void init();
}
