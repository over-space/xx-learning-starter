package com.learning.middleware.mapreduce.friend;


import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FriendMapper extends Mapper<Object, Text, Text, IntWritable> {

    private Text outKey = new Text();
    private IntWritable outValue = new IntWritable(0);


    @Override
    protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        // 周杰伦->方文山,林俊杰,陈奕迅,雪糕,蔡依林

        String[] friends = StringUtils.split(value.toString(), ",");

        for (int i = 1; i < friends.length; i++) {

            outKey.set(getKey(friends[0], friends[i]));
            outValue.set(0);
            context.write(outKey, outValue);

            for (int j = i + 1; j < friends.length; j++) {
                outKey.set(getKey(friends[i], friends[j]));
                outValue.set(1);
                context.write(outKey, outValue);
            }
        }
    }

    private String getKey(String s1, String s2) {
        if (s1.compareTo(s2) > 0) {
            return s1 + "-" + s2;
        } else {
            return s2 + "-" + s1;
        }
    }
}
