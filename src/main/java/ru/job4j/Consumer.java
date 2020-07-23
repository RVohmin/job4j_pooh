package ru.job4j;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ru.job4j
 *
 * @author romanvohmin
 * @since 23.07.2020
 */
public class Consumer {
    private final int size = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(size + 1);
    String mode;
    String theme;

    public Consumer(String mode, String theme) {
        this.mode = mode;
        this.theme = theme;
    }

    public void consume(String theme) {
        for (int i = 0; i < 6; i++) {
            pool.execute(
                    () -> {
                        String request = "GET  /".concat(mode).concat("/").concat(theme);
                        String message = sendMessage(request);
                        System.out.println(message);
                    });
        }
    }

    public void close() {
        pool.shutdown();
    }

    public String sendMessage(String request) {
        StringBuilder response = new StringBuilder();

        try (Socket socket = new Socket("localhost", 9000);
             PrintWriter out = new PrintWriter(socket.getOutputStream());
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(request);
            out.println();
            out.flush();
            String string;
            while (in.ready()) {
                string = in.readLine();
                if (!string.contains("HTTP")) {
                    response.append(string);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static void main(String[] args) {
        Consumer consumer = new Consumer("topic", "weather");
        consumer.consume("weather");
        consumer.close();
    }
}
