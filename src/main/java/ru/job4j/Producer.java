package ru.job4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Producer {
    private final String mode;
    private final JsonParser jsonParser;
    private final int size = Runtime.getRuntime().availableProcessors();
    private final ExecutorService pool = Executors.newFixedThreadPool(size + 1);

    public Producer(String mode, JsonParser jsonParser) {
        this.mode = mode;
        this.jsonParser = jsonParser;
    }

    public void producer(String theme) {
        for (int i = 0; i < 6; i++) {
            pool.execute(
                    () -> {
                        var header = jsonParser.getJsonHeader(
                                "POST", mode.concat("/").concat(theme), "HTTP/1.1");
                        var message = jsonParser.generateJson(mode, theme, "temp 20 C");
                        sendMessage(header, message);
                    });
        }
    }

    public void close() {
        pool.shutdown();
    }

    public void sendMessage(String header, String message) {
        try (var socket = new Socket("localhost", 9000);
             var writer = new PrintWriter(socket.getOutputStream())) {
            writer.println(header);
            writer.println(message);
            writer.println("END");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        var jsonParser = new JsonParser();
        var producer = new Producer("topic", jsonParser);
        producer.producer("weather");
        producer.close();
    }
}
