package com.learning.middleware.mapreduce.sort;

import com.learning.middleware.mapreduce.HadoopUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Sort {

    private static final Logger logger = LogManager.getLogger(Sort.class);


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        System.setProperty("HADOOP_USER_NAME", "root");

        Configuration conf = new Configuration(true);

        // 解析args参数，将-D参数设置到conf中，获取剩余参数remainingArgs
        GenericOptionsParser parser = new GenericOptionsParser(conf, args);
        String[] remainingArgs = parser.getRemainingArgs();

        // 让框架知道是Window异构平台执行
        conf.set("mapreduce.app-submission.cross-platform", "true");

        HadoopUtil.put(conf, "C:\\Users\\Lee\\Documents\\Workspace\\xx-learning-bigdata\\src\\main\\resources\\data\\sort-01.txt", "/users/root/data/sort/sort-01.txt");
        HadoopUtil.put(conf, "C:\\Users\\Lee\\Documents\\Workspace\\xx-learning-bigdata\\src\\main\\resources\\data\\sort-02.txt", "/users/root/data/sort/sort-02.txt");
        HadoopUtil.put(conf, "C:\\Users\\Lee\\Documents\\Workspace\\xx-learning-bigdata\\src\\main\\resources\\data\\sort-03.txt", "/users/root/data/sort/sort-03.txt");

        Job job = Job.getInstance(conf, "job-sort");
        job.setJarByClass(Sort.class);
        job.setJar("C:\\Users\\Lee\\Documents\\Workspace\\xx-learning-bigdata\\target\\xx-learning-bigdata-1.0-SNAPSHOT.jar");

        // input
        logger.info("input path : {}", remainingArgs[0]);
        Path inputPath = new Path(remainingArgs[0]);
        TextInputFormat.addInputPath(job, inputPath);

        // output
        logger.info("output path : {}", remainingArgs[1]);
        Path outputPath = new Path(remainingArgs[1]);
        if (outputPath.getFileSystem(conf).exists(outputPath)) {
            // 存在先删除
            outputPath.getFileSystem(conf).delete(outputPath, true);
        }
        TextOutputFormat.setOutputPath(job, outputPath);

        // 设置map, reducer.
        job.setMapperClass(SortMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setReducerClass(SortReducer.class);

        // job.setNumReduceTasks(0);

        // 执行，并输出日志
        job.waitForCompletion(true);
    }

}
