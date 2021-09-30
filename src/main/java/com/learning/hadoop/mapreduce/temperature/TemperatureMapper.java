package com.learning.hadoop.mapreduce.temperature;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lifang
 * @since 2021/9/30
 */
public class TemperatureMapper extends Mapper<Object, Text, TemperatureMapKey, IntWritable> {

    TemperatureMapKey outKey = new TemperatureMapKey();
    IntWritable outValue = new IntWritable();

    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, TemperatureMapKey, IntWritable>.Context context) throws IOException, InterruptedException {
        // 2021-09-30 11:31:10,S10001,23

        String[] split = StringUtils.split(value.toString(), ",");
        LocalDateTime localDateTime = LocalDateTime.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int temp = Integer.parseInt(split[2]);

        outKey.setYear(localDateTime.getYear());
        outKey.setMonth(localDateTime.getMonth().getValue());
        outKey.setDay(localDateTime.getDayOfMonth());
        outKey.setTemperature(temp);

        outValue.set(temp);
        context.write(outKey, outValue);
    }
}
