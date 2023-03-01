package com.learning.io.rpc.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FlyImpl implements Fly {
    private static final Logger logger = LogManager.getLogger(FlyImpl.class);

    @Override
    public String getName(String msg) {
        logger.info("FlyImpl#getThreadName, msg:{}", msg);
        return msg;
    }
}
