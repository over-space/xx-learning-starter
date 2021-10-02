package com.learning.hadoop.mapreduce.sort;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SortMapper extends Mapper<Object, Text, IntWritable, IntWritable> {

    IntWritable outKey = new IntWritable();
    IntWritable outValue = new IntWritable(1);


    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, IntWritable, IntWritable>.Context context) throws IOException, InterruptedException {
        String line = value.toString();

        outKey.set(Integer.parseInt(line));

        context.write(outKey, outValue);
    }
}
