package ru.job4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Producer {
    String mode;
    JsonMessage jsonMessage = new JsonMessage();
    private final int size = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(size + 1);

    public Producer(String mode) {
        this.mode = mode;
    }

    public void producer(String theme) {
        for (int i = 0; i < 6; i++) {
            pool.execute(
                    () -> {
                        String message = jsonMessage.generateJsonMessage(
                                mode,
                                theme,
                                "temp 20 C");
                        sendMessage(message, "POST /", mode.concat("/").concat(theme));
                    });
        }
    }

    public void close() {
        pool.shutdown();
    }

    public void sendMessage(String message, String method, String path) {
        try (Socket socket = new Socket("localhost", 9000);
             PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
            String request = method.concat(path).concat(System.lineSeparator()).concat(message);
            writer.println(request);
            writer.println();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Producer producer = new Producer("topic");
        producer.producer("weather");
        producer.close();
    }
}
