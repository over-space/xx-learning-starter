package com.learning.middleware.mapreduce.abc;

import com.learning.middleware.mapreduce.HadoopUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Abc {

    private static final Logger logger = LogManager.getLogger(Abc.class);


    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        System.setProperty("HADOOP_USER_NAME", "root");

        Configuration conf = new Configuration(true);

        // 解析args参数，将-D参数设置到conf中，获取剩余参数remainingArgs
        GenericOptionsParser parser = new GenericOptionsParser(conf, args);
        String[] remainingArgs = parser.getRemainingArgs();

        // 让框架知道是Window异构平台执行
        conf.set("mapreduce.app-submission.cross-platform", "true");

        HadoopUtil.put(conf, "C:\\Users\\Lee\\Documents\\Workspace\\xx-learning-bigdata\\src\\main\\resources\\data\\abc.txt", "/users/root/data/abc/abc.txt");

        Job job = Job.getInstance(conf, "job-abc");
        job.setJarByClass(Abc.class);
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
        job.setMapperClass(AbcMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setReducerClass(AbcReducer.class);

        // job.setNumReduceTasks(0);

        // 执行，并输出日志
        job.waitForCompletion(true);
    }

}
