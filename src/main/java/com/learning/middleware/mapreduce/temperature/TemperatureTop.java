package com.learning.middleware.mapreduce.temperature;

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

import java.io.File;
import java.io.IOException;

/**
 * @author lifang
 * @since 2021/9/30
 */
public class TemperatureTop {

    private static final Logger logger = LogManager.getLogger(TemperatureTop.class);

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        System.setProperty("HADOOP_USER_NAME", "root");

        Configuration conf = new Configuration(true);

        // 解析args参数，将-D参数设置到conf中，获取剩余参数remainingArgs
        GenericOptionsParser parser = new GenericOptionsParser(conf, args);
        String[] remainingArgs = parser.getRemainingArgs();

        HadoopUtil.put(conf, new File("C:\\Users\\Lee\\Documents\\Workspace\\xx-learning-bigdata\\src\\main\\resources\\data\\city.txt"), new Path("/users/root/city.txt"));
        HadoopUtil.put(conf, new File("C:\\Users\\Lee\\Documents\\Workspace\\xx-learning-bigdata\\src\\main\\resources\\data\\temperature.txt"), new Path("/users/root/temperature.txt"));

        // 让框架知道是Window异构平台执行
        conf.set("mapreduce.app-submission.cross-platform", "true");

        Job job = Job.getInstance(conf, "job:temperature-top");
        job.setJarByClass(TemperatureTop.class);
        job.setJar("C:\\Users\\Lee\\Documents\\Workspace\\xx-learning-bigdata\\target\\xx-learning-bigdata-1.0-SNAPSHOT.jar");
        job.addCacheFile(new Path("/users/root/city.txt").toUri());

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

        // mapTask
        job.setMapperClass(TemperatureMapper.class);
        job.setMapOutputKeyClass(TemperatureMapKey.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 分区, 定义分区规则，相同的key获得相同的分区号,以便进入不同的reducer.
        job.setPartitionerClass(TemperaturePartitioner.class);

        // 对进入同一个reducer的key进行排序
        job.setSortComparatorClass(TemperatureSortComparator.class);

        // reducerTask
        // job.setNumReduceTasks(0);
        // 两连两条记录分组
        job.setGroupingComparatorClass(TemperatureGroupingComparator.class);
        job.setReducerClass(TemperatureReducer.class);

        // 执行，并输出日志
        job.waitForCompletion(true);
    }
}
