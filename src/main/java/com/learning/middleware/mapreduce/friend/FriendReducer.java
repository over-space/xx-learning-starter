package com.learning.middleware.mapreduce.friend;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FriendReducer extends Reducer<Text, IntWritable, Text, IntWritable> {


    IntWritable outValue = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        int flag = 0;
        int sum = 0;
        for (IntWritable v : values) {
            if (v.get() == 0) {
                flag = 1;
            }
            sum += v.get();
        }

        if (flag == 0) {
            outValue.set(sum);
            context.write(key, outValue);
        }
    }
}
