package com.learning.seckill.service;

import com.learning.seckill.domain.GoodsEntity;
import com.learning.seckill.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author lifang
 * @since 2022/3/21
 */
@Service
@Configurable
public class GoodsServiceImpl implements GoodsService{

    @Value("${xx.learning.seckill.goods.name:'HUAWEI META 50 PRO'}")
    private String goodsName;

    @Value("${xx.learning.seckill.goods.store:10}")
    private Integer store;

    @Value("${xx.learning.seckill.goods.threadNum:5}")
    private Integer threadNum;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 秒杀-mysql版本。
     * 流量直接打到数据库，压力都在数据库上。
     *
     * 优化点：引入redis，让redis先过滤掉一些无效流量。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean seckillByMySQL(String goodsNum) {
        goodsRepository.decrStoreByMySQL(goodsNum);
        return true;
    }

    /**
     * 秒杀-redis+mysql版本。
     * 先让redis扣减商品数量，返回值>=0表示有效流量，但此时redis也会遭受太多无效流量（redis中的库存已经变为负数）。
     *
     * 优化点：扣减为负数之后，将redis中key的结果改完字符串，通过异常捕获无效库存，减少redis扣减库存带来的损耗。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean seckillByRedisV1(String goodsNum) {
        // 先将流量打到redis上。
        ValueOperations<String, String> strOps = stringRedisTemplate.opsForValue();
        Long decrement = strOps.decrement(goodsNum);
        if(decrement >= 0) {
            // redis扣减成功了，再扣减数据库。
            goodsRepository.decrStoreByRedis(goodsNum);
            return true;
        }
        return false;
    }

    /**
     * 差距和seckillByRedisV1不大，只是尽可能减少了redis运算压力(redis只有在数值类型才会执行自增自减)，
     * 但没解决问题本质, 高并发的情况下，将value改成字符串的操作会重复执行，并且无效流量还是给到了redis。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean seckillByRedisV2(String goodsNum) {
        // 先将流量打到redis上。
        try {
            ValueOperations<String, String> strOps = stringRedisTemplate.opsForValue();
            Long decrement = strOps.decrement(goodsNum);
            if (decrement >= 0) {
                // redis扣减成功了，再扣减数据库。
                goodsRepository.decrStoreByRedis(goodsNum);
                return true;
            } else {
                strOps.set(goodsNum, "none");
            }
        }catch (Exception e){
            // ignore
        }
        return false;
    }

    /**
     * 减少高并发情况下，操作redis将value改成字符串类型，利用了singleThread线程的有序性执行。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean seckillByThreadV1(String goodsNum) {
        SeckillThreadPool seckillThreadPool = SeckillThreadPool.getInstance(threadNum);

        ExecutorService executorService = seckillThreadPool.getExecutorService(goodsNum);

        Future<Boolean> future = executorService.submit(() -> {
            try {
                ValueOperations<String, String> strOps = stringRedisTemplate.opsForValue();
                Long decrement = strOps.decrement(goodsNum);
                if(decrement >= 0){
                    return true;
                }else{
                    // 避免下次redis继续执行减操作，提供redis性能。
                    strOps.set(goodsNum, "none");
                }
            }catch (Exception e){
                //
            }
            return false;
        });

        try {
            Boolean success = future.get();
            if(success){
                goodsRepository.decrStoreByRedis(goodsNum);
                return true;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() {
        // 先清理一下
        goodsRepository.deleteAll();;

        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();

        List<GoodsEntity> goodsList = new ArrayList<>(1000);
        for (int i = 1000; i <= 1200; i++){

            // redis初始化
            stringValueOperations.set(String.valueOf(i), store.toString());

            // 数据库初始化
            GoodsEntity goods = new GoodsEntity();
            goods.setGoodsNum(String.valueOf(i));
            goods.setName(goodsName);
            goods.setStore(store);
            goodsList.add(goods);
        }
        goodsRepository.saveAll(goodsList);
    }
}
