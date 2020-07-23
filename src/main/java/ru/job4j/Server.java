package ru.job4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
    private final int size = Runtime.getRuntime().availableProcessors();
    ExecutorService pool = Executors.newFixedThreadPool(size + 1);
    Storage storage = new Storage();
    String mode;
    String theme;

    public Server(int port) {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Server started");
            while (!server.isClosed()) {
                Socket socket = server.accept();
                pool.execute(
                        () -> {
                            try (
                                    BufferedReader in = new BufferedReader(
                                            new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                                    PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
                                String str;
                                writer.println("HTTP/1.1 200 OK");
                                writer.println();
                                writer.flush();
                                StringBuilder stringBuilder = new StringBuilder(System.lineSeparator());
                                String method = "";
                                String message;
                                while (!(str = in.readLine()).isEmpty()) {
                                    if (str.contains("POST")) {
                                        method = "POST";
                                        mode = getModeFromPath(str);
                                        theme = getThemeFromPath(str);
                                    } else if (str.contains("GET")) {
                                        method = "GET";
                                        mode = getModeFromPath(str);
                                        theme = getThemeFromPath(str);
                                    }
                                    stringBuilder.append(str);
                                }
                                if (method.equals("POST")) {
                                    storage.addMessage(mode, theme, stringBuilder.toString());
                                } else if (method.equals("GET")) {
                                    message = storage.getMessage(mode, theme);
                                    writer.println(message);
                                    writer.flush();
                                }
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

    private String getModeFromPath(String path) {
        return path.substring(6, path.lastIndexOf("/"));
    }

    private String getThemeFromPath(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    public static void main(String[] args) {
        Server server = new Server(9000);
    }
}
