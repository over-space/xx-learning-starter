package com.learning.middleware.mapreduce.abc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class AbcReducer extends Reducer<Text, IntWritable, IntWritable, Text> {

    private static IntWritable line = new IntWritable(1);

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, IntWritable, Text>.Context context) throws IOException, InterruptedException {
        for (IntWritable value : values) {
            context.write(line, key);
            line = new IntWritable(line.get() + 1);
        }
    }
}
