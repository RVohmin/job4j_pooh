package ru.job4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ru.job4j.test.storage
 *
 * @author romanvohmin
 * @since 21.07.2020
 */
public class Storage {
    /**
     * Storage for Queue mode. String - theme name, Queue - storage of messages.
     */
    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    /**
     * Storage for Topic mode. String - theme name, Queue - storage of messages.
     */
    ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = new ConcurrentHashMap<>();

    public void size() {
        if (queue.size() > 0) {
            System.out.printf("Size of map: %d, size of queue: %d", queue.size(), queue.get("weather").size());
        }
        if (topic.size() > 0) {
            System.out.printf("Size of map: %d, size of queue: %d", topic.size(), topic.get("weather").size());
        }
    }

    private void queueModeQueueFabric(String theme) {
        queue.put(theme, new ConcurrentLinkedQueue<>());
        System.out.println("queue mode: Created new theme for " + theme);
    }

    private void topicModeQueueFabric(String theme) {
        topic.put(theme, new ConcurrentLinkedQueue<>());
        System.out.println("topic mode: Created new theme - " + theme);
    }

    public void addMessage(String mode, String theme, String message) {
        if ("queue".equals(mode)) {
            if (queue.containsKey(theme)) {
                queue.get(theme).offer(message);
                System.out.println("Message added to storage with theme ".concat(theme));
            } else {
                queueModeQueueFabric(theme);
                queue.get(theme).offer(message);
                System.out.println("Message to queue added");
            }
        }
        if ("topic".equals(mode)) {
            addTopicMessage(theme, message);
        }
    }

    public void addTopicMessage(String theme, String message) {
        if (topic.containsKey(theme)) {
            topic.get(theme).offer(message);
            System.out.println("Message added to storage with theme ".concat(theme));
        } else {
            topicModeQueueFabric(theme);
            topic.get(theme).offer(message);
            System.out.println("Message to topic added");
        }
    }

    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> getStorage(String mode) {
        return "queue".equals(mode) ? queue : topic;
    }

    public String getMessage(String mode, String theme) {
        var message = "";
        var map = getStorage(mode);
        if (map.containsKey(theme)) {
            message = map.get(theme).size() != 0 ? map.get(theme).poll() : "There are no messages";
        } else {
            message = "There are no this theme, create it";
        }
        return message;
    }
}
