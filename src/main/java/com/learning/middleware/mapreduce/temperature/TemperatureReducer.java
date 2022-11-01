package com.learning.middleware.mapreduce.temperature;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * @author lifang
 * @since 2021/9/30
 */
public class TemperatureReducer extends Reducer<TemperatureMapKey, IntWritable, Text, IntWritable> {

    Text outKey = new Text();
    IntWritable outValue = new IntWritable();

    @Override
    protected void reduce(TemperatureMapKey key, Iterable<IntWritable> values, Reducer<TemperatureMapKey, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {

        Iterator<IntWritable> iterator = values.iterator();

        boolean flag = false;
        int day = 0;
        while (iterator.hasNext()) {

            // 遍历values，但随着next的调用，key的值也会变化，引用传递。
            IntWritable next = iterator.next();

            if(!flag){
                flag = true;
                day = key.getDay();

                outKey.set(String.format("%s-%02d-%02d,%s", key.getYear(), key.getMonth(), key.getDay(), key.getCity()));
                outValue.set(next.get());
                context.write(outKey, outValue);
            }else if(day != key.getDay()){
                outKey.set(String.format("%s-%02d-%02d,%s", key.getYear(), key.getMonth(), key.getDay(), key.getCity()));
                outValue.set(next.get());
                context.write(outKey, outValue);
                break;
            }
        }
    }
}
