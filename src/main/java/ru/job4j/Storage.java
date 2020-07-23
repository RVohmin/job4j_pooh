package ru.job4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ru.job4j.test.storage
 *
 * @author romanvohmin
 * @since 21.07.2020
 */
public class Storage {
    /**
     * Temporary string value, used in fabric methods.
     */
    private String newName;
    private final AtomicInteger index = new AtomicInteger(0);

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
            System.out.println(queue.size() + " : " + queue.get("weather").size());
        }
        if (topic.size() > 0) {
            System.out.println(topic.size() + " : " + topic.get("weather").size());
        }
    }

    private void queueModeQueueFabric(String theme) {
        newName = generateName(theme);
        ConcurrentLinkedQueue<String> newName = new ConcurrentLinkedQueue<>();
        queue.put(theme, newName);
        System.out.println("queue mode: Created new theme for " + theme);
    }

    private void topicModeQueueFabric(String theme) {
        newName = generateName(theme);
        ConcurrentLinkedQueue<String> newName = new ConcurrentLinkedQueue<>();
        topic.put(theme, newName);
        System.out.println("topic mode: Created new theme - " + theme);
    }

    private String generateName(String theme) {
        return theme.concat(String.valueOf(index.incrementAndGet()));
    }

    public void addMessage(String mode, String theme, String message) {
        if (mode.equals("queue")) {
            if (queue.containsKey(theme)) {
                queue.get(theme).offer(message);
                System.out.println("Message added to storage with theme ".concat(theme));
            } else {
                queueModeQueueFabric(theme);
                queue.get(theme).offer(message);
                System.out.println("Message to queue added");
            }
        }
        if (mode.equals("topic")) {
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

    public String getMessage(String mode, String theme) {
        String message = "There are no messages";

        if (mode.equals("queue")) {
            if (queue.containsKey(theme)) {
                message = queue.get(theme).poll();
            } else {
                System.out.println("There are no this theme, create it");
            }
        } else if (mode.equals("topic")) {
            if (topic.containsKey(theme)) {
                message = topic.get(theme).poll();
            } else {
                System.out.println("There are no this theme, create it");
            }
        }
        return message;
    }

}
