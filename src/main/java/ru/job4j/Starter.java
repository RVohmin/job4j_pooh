package ru.job4j;

/**
 * В этом проекте мы сделаем аналог асинхронной очереди RabbitMQ.
 * Приложение запускает Socket и ждем клиентов.
 * Клиенты могут быть двух типов: отправители (publisher), получатели (subscriver).
 * В качестве протокола будет использовать HTTP. Сообщения в формате JSON.
 * Существуют два режима: queue, topic.
 *
 * Queue.
 * Отправитель посылает сообщение с указанием очереди.
 * Получатель читает первое сообщение и удаляет его из очереди.
 * Если приходят несколько получателей, то они читают из одной очереди.
 * Уникальное сообщение может быть прочитано, только одним получателем.
 *
 * Пример запросов.
 *
 * POST /queue
 * {
 *   "queue" : "weather",
 *   "text" : "temperature +18 C"
 * }
 *
 * GET /queue/weather
 * {
 *   "queue" : "weather",
 *   "text" : "temperature +18 C"
 * }
 *
 * Topic.
 * Отправить посылает сообщение с указанием темы.
 * Получатель читает первое сообщение и удаляет его из очереди.
 * Если приходят несколько получателей, то они читают отдельные очереди.
 *  POST /topic
 * {
 *   "topic" : "weather",
 *   "text" : "temperature +18 C"
 * }
 *
 * GET /topic/weather
 * {
 *   "topic" : "weather",
 *   "text" : "temperature +18 C"
 * }
 *
 *
 * ru.job4j
 * Test class for load testing
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
