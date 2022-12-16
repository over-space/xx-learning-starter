package com.learning.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author 李芳
 * @since 2022/12/5
 */
public class FileIO {

    private static final Logger logger = LogManager.getLogger(FileIO.class);

    private static final String path = "C:\\Users\\Lee\\Desktop\\1.txt";

    public static void main(String[] args) throws Exception {

        System.out.println("请选择文件IO类型：");
        System.out.println("普通IO：0");
        System.out.println("Buffer文件IO：1");

        int type = System.in.read();

        logger.info("type: {}", type);

        switch (type){
            case 48:
                testBasicFileIO(path);
                break;
            case 49:
                testBufferFileIO();
                break;
        }
        System.in.read();
    }

    private static void testBufferFileIO() {
        try(BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(Paths.get(path)), 1024)) {
            int i = 1;
            do {
                Thread.sleep(10);
                out.write("123456789\n".getBytes());
                i++;
            }while (i < 10000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testBasicFileIO(String path) throws Exception {
        try(FileOutputStream out = new FileOutputStream(path)) {
            int i = 1;
            do {
                Thread.sleep(10);
                out.write("123456789\n".getBytes());
                i++;
            }while (i < 10000);
        }
    }

}
