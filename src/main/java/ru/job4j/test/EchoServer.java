package ru.job4j.test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * ru.job4j.test.EchoServer
 *
 * This is simplest Echo Server created for learning targets
 *
 * @author romanvohmin
 * @version 0.1
 * @since 26.03.2020
 */
public class EchoServer {

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                try (BufferedReader in = new BufferedReader(
                             new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                        PrintWriter output = new PrintWriter(socket.getOutputStream())) {
                    String str;
                    String msg = "";
                    while (!(str = in.readLine()).isEmpty()) {
                        if (str.startsWith("GET /?msg=")) {
                            msg = (str.split(" ")[1]).substring(6);
                        }
                        if (msg.equalsIgnoreCase("exit")) {
                            msg = ("Server closing");
                            server.close();
                        }
                        if (msg.equalsIgnoreCase("Hello")) {
                            msg = "Hello, dear friend!";
                        }
                    }
                    output.println("HTTP/1.1 200 OK\r\n");
                    output.println(msg);
                    output.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}