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
    private final ExecutorService pool = Executors.newFixedThreadPool(size + 1);
    private final String mode;
    private final String theme;
    private final JsonParser jsonParser;

    public Consumer(String mode, String theme, JsonParser jsonParser) {
        this.mode = mode;
        this.theme = theme;
        this.jsonParser = jsonParser;
    }

    public void consume() {
        for (int i = 0; i < 6; i++) {
            pool.execute(
                    () -> {
                        var request = jsonParser.getJsonHeader(
                                "GET", mode.concat("/").concat(theme), "HTTP/1.1");
                        var json = jsonParser.generateJson(mode, theme, "message");
                        System.out.println(sendMessage(request, json));
                    });
        }
    }

    public void close() {
        pool.shutdown();
    }

    public String sendMessage(String request, String json) {
        var response = "";
        try (var socket = new Socket("localhost", 9000);
             var out = new PrintWriter(socket.getOutputStream());
             var in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(request);
            out.println(json);
            out.println("END");
            out.flush();
            var string = "";
            while (!(string = in.readLine()).equals("END")) {
                if (!string.contains("HTTP")) {
                    response = string;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser();
        var consumer = new Consumer("topic", "weather", jsonParser);
        consumer.consume();
        consumer.close();
    }
}
