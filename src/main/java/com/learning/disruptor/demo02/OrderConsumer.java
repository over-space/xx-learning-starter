package com.learning.disruptor.demo02;

import com.lmax.disruptor.EventHandler;

import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;

public class OrderConsumer implements EventHandler<OrderEvent>{

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(OrderConsumer.class);
    
    
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
    
        Thread.sleep(50);

        logger.info("sequence: {}, msg : {}, endOfBatch: {}", sequence, event.getMessage(), endOfBatch);
    }
}
