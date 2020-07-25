//package ru.job4j.test;
//
//import java.io.*;
//import java.net.Socket;
//
///**
// * ru.job4j.server
// *
// * @author romanvohmin
// * @since 21.07.2020
// */
//public class Client {
//    public static void main(String[] args) {
//        JsonMessage jsonMessage = new JsonMessage();
//
//        try (var socket = new Socket("localhost", 9000);
//             var writer = new PrintWriter(socket.getOutputStream());
//             BufferedReader reader =
//                     new BufferedReader(
//                             new InputStreamReader(
//                                     socket.getInputStream()))) {
//            System.out.println("Connected to Server");
//            String request = jsonMessage.generateJsonMessage(
//                    "queue",
//                    "weather",
//                    "tem 20 C");
//            String path = "POST /queue/weather\n";
//            writer.println(path.concat(request));
//            writer.println();
//            writer.flush();
//            String response;
//            while (reader.ready()) {
//                response = reader.readLine();
//                System.out.println(response);
//            }
////            String response = reader.readLine();
////            System.out.println("Response from server: " + response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}