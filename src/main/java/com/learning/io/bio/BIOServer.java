package com.learning.io.bio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author 李芳
 * @since 2022/12/16
 */
public class BIOServer {

    private static final Logger logger = LogManager.getLogger(BIOServer.class);


    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(9090, 20);

        logger.info("step1: socket port :{} ", server.getLocalPort());

        new Handler(server.accept()).start();
    }

    private static class Handler extends Thread {

        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
            logger.info("客户端连接成功，client: {}", socket.getRemoteSocketAddress());
        }

        @Override
        public void run() {
            try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
                handle(in, out);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }


        private void handle(InputStream in, OutputStream out) throws IOException {
            // 获得一个字符输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            writer.write("连接成功！\n");
            writer.flush();
            while (true) {

                // 接收客户端消息。
                String str = reader.readLine();
                logger.info("收到客户端信息：{}", str);

                if ("Bye".equalsIgnoreCase(str)) {
                    // 当客户端传来"Bye"代表断开连接
                    writer.write("Bye\n");
                    writer.flush();
                    break;
                }

                // 将消息发送回客户端。
                writer.write("已经收到：" + str + "\n");
                writer.flush();
            }
        }
    }

}
