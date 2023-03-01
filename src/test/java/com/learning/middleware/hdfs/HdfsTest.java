package com.learning.middleware.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class HdfsTest {

    private static final Logger logger = LogManager.getLogger(HdfsTest.class);

    private Configuration conf;

    private FileSystem fs;

    @BeforeTest
    public void conn() throws IOException, InterruptedException {
        logger.info("连接hadoop hdfs....");

        conf = new Configuration(true);


        // 1.
        // 指定hadoop用户。
        // System.setProperty("HADOOP_USER_NAME", "root");
        // 默认已window用户连接，可以通过环境变量指定用户
        // fs = FileSystem.get(conf);
        // logger.info("fs : {}", fs);

        // 2.
        fs = FileSystem.get(URI.create("hdfs://mycluster"), conf, "root");
    }

    @AfterTest
    public void close() throws IOException {
        fs.close();
        logger.info("关闭hadoop hdfs连接。");
    }

    @Test
    public void mkdir() throws IOException {
        Path path = new Path("/msb/test");
        if (fs.exists(path)) {
            fs.delete(path, true);
        }
        fs.mkdirs(path);
        logger.info("创建文件夹。");
    }

    @Test
    public void upload1() throws Exception {
        File file = new File("C:\\Users\\Lee\\Downloads\\hdfs-site.xml");
        logger.info("file : {}", file.getPath());
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        Path outfile = new Path("/msb/hdfs-site.xml");
        FSDataOutputStream output = fs.create(outfile);
        IOUtils.copyBytes(input, output, conf, true);
        logger.info("上传文件。");
    }

    @Test
    public void upload2() throws IOException {
        File file = new File("C:\\Users\\Lee\\Downloads\\data.txt");
        logger.info("file : {}", file.getPath());
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        Path outfile = new Path("/users/root/data.txt");
        FSDataOutputStream output = fs.create(outfile, true, 2048, (short) 2, 1048576L);
        IOUtils.copyBytes(input, output, conf, true);
        logger.info("上传文件。");
    }


    @Test
    public void blocks() throws IOException {
        Path file = new Path("/users/root/data.txt");
        FileStatus fss = fs.getFileStatus(file);
        BlockLocation[] blockLocations = fs.getFileBlockLocations(fss, 0, fss.getLen());
        for (BlockLocation b : blockLocations) {
            logger.info("block location : {}", b);
        }

        FSDataInputStream in = fs.open(file);
        in.seek(10485760L);
        String line = in.readLine();
        logger.info("line : {}", line);
    }

    @Test
    public void write() throws IOException {

        String filepath = "C:\\Users\\Lee\\Downloads\\data.txt";

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filepath))) {

            for (int i = 1; i <= 100000; i++) {
                bufferedOutputStream.write(("hello hadoop " + i + "\n").getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
