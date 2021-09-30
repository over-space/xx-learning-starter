package com.learning.hadoop.mapreduce.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author lifang
 * @since 2021/9/30
 */
public class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {

    Text text = new Text();
    IntWritable intWritable = new IntWritable(1);

    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        StringTokenizer tokenizer = new StringTokenizer(value.toString());
        while (tokenizer.hasMoreElements()) {
            String val = tokenizer.nextToken();
            text.set(val);
            context.write(text, intWritable);
        }
    }
}
