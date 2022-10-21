package com.learning.leetcode;

import com.learning.BaseTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Stack;

/**
 * @author 李芳
 * @since 2022/10/14
 */
public class FileTest extends BaseTest {


    @Test
    public void test() {
        File file = new File("C:\\Users\\Lee\\OneDrive");

        // 递归方式
        recursive(file);

        // 遍历方式
        foreach(file);
    }

    /**
     * 遍历方式
     */
    public void foreach(File rootFile) {
        Stack<File> stack = new Stack<>();
        stack.add(rootFile);
        foreach(stack);
    }

    public void foreach(Stack<File> stack) {
        while (!stack.isEmpty()){
            File[] files = stack.pop().listFiles();
            if(files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        stack.push(file);
                    } else {
                        logger.info("文件名称：{}", file.getName());
                    }
                }
            }
        }
    }

    /**
     * 递归方式
     */
    public void recursive(File rootFile) {
        File[] files = rootFile.listFiles();
        if(files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    recursive(file);
                } else {
                    logger.info("文件名称：{}", file.getName());
                }
            }
        }
    }
}
