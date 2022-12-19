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
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author 李芳
 * @since 2022/12/16
 */
public class BIOClient {

    private static final Logger logger = LogManager.getLogger(BIOClient.class);

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 9090);
        try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            handle(in, out);
        } catch (IOException e) {
            socket.close();
        }
    }

    private static void handle(InputStream in, OutputStream out) throws IOException {
        //获得一个字符输入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();

            logger.info("request: {}", line);

            writer.write(line);
            writer.newLine();
            writer.flush();

            String resp = reader.readLine();
            logger.info("response: {}", resp);

            if ("Bye".equalsIgnoreCase(line)) {
                break;
            }
        }

    }
}