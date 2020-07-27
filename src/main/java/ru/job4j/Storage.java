package ru.job4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

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
    ConcurrentHashMap<String, LinkedBlockingQueue<String>> queue = new ConcurrentHashMap<>();
    /**
     * Storage for Topic mode. String - theme name, Queue - storage of messages.
     */
    ConcurrentHashMap<String, LinkedBlockingQueue<String>> topic = new ConcurrentHashMap<>();

    /**
     * Method for debugging, print size of storage
     */
    public void size(String mode, String theme) {
        var map = getStorage(mode);
        if (map.size() > 0) {
            System.out.printf("Size of map: %d, size of queue: %d\r\n", map.size(), map.get(theme).size());
        }
    }
    /**
     * Get storage for your mode - queue or topic
     * @param mode - mode Queue or Topic
     * @return - Map <String, ConcurrentLinkedQueue<String>>
     */
    private ConcurrentHashMap<String, LinkedBlockingQueue<String>> getStorage(String mode) {
        return "queue".equals(mode) ? queue : topic;
    }
    /**
     * If requsted theme not exist, this fabric method create in Map storage new key - theme, value - new queue
     * @param mode - mode Queue or Topic
     * @param theme - theme of message
     */
    private void modeQueueFabric(String mode, String theme) {
        var map = getStorage(mode);
        map.put(theme, new LinkedBlockingQueue<>(5));
        System.out.printf("%s mode: Created new theme for %s\r\n", mode, theme);
    }
    /**
     * Add message to corresponding queue
     * @param mode - mode Queue or Topic
     * @param theme - theme of message, key in map
     * @param message - String message
     */
    public void addMessage(String mode, String theme, String message) {
        var map = getStorage(mode);
        if (map.containsKey(theme)) {
            map.get(theme).offer(message);
            System.out.println("Message added to storage with theme ".concat(theme));
        } else {
            modeQueueFabric(mode, theme);
            map.get(theme).offer(message);
            System.out.println("Message to queue added");
        }
    }
    /**
     * Method seek in storage (Map) by key (theme) corresponding queue with messages and return message from head queue
     * @param mode - mode Queue or Topic
     * @param theme - theme of message
     * @return - String text message
     */
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
