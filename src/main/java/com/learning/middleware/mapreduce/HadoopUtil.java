package com.learning.middleware.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class HadoopUtil {

    private static final Logger logger = LogManager.getLogger(HadoopUtil.class);


    public static void put(Configuration conf, String sourcePath, String targetPath) throws IOException {
        put(conf, new File(sourcePath), new Path(targetPath));
    }

    public static void put(Configuration conf, File sourceFile, Path targetPath) throws IOException {
        if (!sourceFile.exists()) {
            throw new FileNotFoundException("不存在的文件，path: " + sourceFile.getAbsolutePath());
        }

        logger.info("开始上传文件：{}, 存放路径：{}", sourceFile, targetPath);

        FileSystem fs = FileSystem.get(conf);
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(sourceFile));
        FSDataOutputStream output = fs.create(targetPath, true);
        IOUtils.copyBytes(input, output, conf, true);
    }

}
