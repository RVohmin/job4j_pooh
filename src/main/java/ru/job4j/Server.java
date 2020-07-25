package ru.job4j;

import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ru.job4j.server
 * Simple multi thread server
 *
 * @author romanvohmin
 * @since 21.07.2020
 */
public class Server {
    private final Storage storage = new Storage();
    private final JsonParser jsonParser;

    public Server(int port, JsonParser jsonParser) {
        this.jsonParser = jsonParser;
        try (var server = new ServerSocket(port)) {
            ExecutorService pool = Executors.newCachedThreadPool();
            while (!server.isClosed()) {
                var socket = server.accept();
                pool.execute(
                        () -> {
                            try (var in = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                                 var writer = new PrintWriter(socket.getOutputStream())) {
                                writer.println("HTTP/1.1 200 OK");
                                writer.println();
                                writer.flush();
                                var inLine = "";
                                var method = "";
                                var json = "";
                                while (!(inLine = in.readLine()).isBlank()) {
                                    json = inLine;
                                    method = jsonParser.getMethod(json);
                                }
                                while (!(inLine = in.readLine()).equals("END")) {
                                    json = inLine;
                                }
                                process(method, json, writer);
                            } catch (IOException e) {
                                System.out.println("Error");
                                e.printStackTrace();
                            }
                            storage.size();
                        }
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(String method, String json, PrintWriter writer) {
        var mode = jsonParser.getMode(json);
        var theme = jsonParser.getTheme(json);
        if (method.equals("POST")) {
            storage.addMessage(mode, theme, json);
        } else if (method.equals("GET")) {
            writer.println(storage.getMessage(mode, theme));
            writer.println("END");
            writer.flush();
        }
    }

    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser();
        Server server = new Server(9000, jsonParser);
    }
}
