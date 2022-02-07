package com.learning.hadoop.mapreduce.abc;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AbcMapper extends Mapper<Object, Text, Text, IntWritable> {

    private static IntWritable outValue = new IntWritable(1);

    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        context.write(value, outValue);
    }
}
