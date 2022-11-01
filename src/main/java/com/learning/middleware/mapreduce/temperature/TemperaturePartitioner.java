package com.learning.middleware.mapreduce.temperature;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author lifang
 * @since 2021/9/30
 */
public class TemperaturePartitioner extends Partitioner<TemperatureMapKey, IntWritable> {

    @Override
    public int getPartition(TemperatureMapKey temperatureMapKey, IntWritable intWritable, int numPartitions) {
        // 按年分区
        return temperatureMapKey.getYear() & numPartitions;
    }
}
