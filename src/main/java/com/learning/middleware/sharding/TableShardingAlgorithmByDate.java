package com.learning.middleware.sharding;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Properties;

/**
 * @author 李芳
 * @since 2022/9/20
 */
public class TableShardingAlgorithmByDate implements StandardShardingAlgorithm<LocalDate> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<LocalDate> preciseShardingValue) {
        return null;
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<LocalDate> rangeShardingValue) {

        return null;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {

    }
}
