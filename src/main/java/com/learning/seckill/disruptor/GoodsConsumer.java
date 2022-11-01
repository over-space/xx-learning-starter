package com.learning.seckill.disruptor;


import com.learning.basic.disruptor.AbstractMessageConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 消费者
 */
public class GoodsConsumer extends AbstractMessageConsumer<GoodsEvent> {

    private static final Logger logger = LogManager.getLogger(GoodsConsumer.class);

    public GoodsConsumer(String consumerName) {
        super(consumerName);
    }

    @Override
    public void onEvent(GoodsEvent event, long sequence, boolean endOfBatch) throws Exception {
        super.onEvent(event, sequence, endOfBatch);
    }
}
