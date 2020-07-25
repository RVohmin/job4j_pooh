package ru.job4j;

/**
 * ru.job4j
 *
 * @author romanvohmin
 * @since 25.07.2020
 */
public class Starter {
    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser();
        new Thread(() -> {
            Server server = new Server(9000, jsonParser);
        }).start();
        var producer = new Producer("topic", jsonParser);
        producer.producer("weather");
        producer.close();
        var consumer = new Consumer("topic", "weather", jsonParser);
        consumer.consume();
        consumer.close();
    }
}
