package ru.job4j;

import org.json.HTTP;
import org.json.JSONObject;

/**
 * ru.job4j
 * Class use json.org library
 * @author romanvohmin
 * @since 25.07.2020
 */
public class JsonParser {
    /**
     * Generate json text message
     * @param mode - mode Queue or Topic
     * @param theme - theme of message
     * @param message - text message
     * @return String in json format
     */
    public synchronized String generateJson(String mode, String theme, String message) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("mode", mode);
        jsonMessage.put("theme", theme);
        jsonMessage.put("text", message);
        return jsonMessage.toString();
    }
    /**
     * Generate HTTP Header
     * @param method - method HTTP (GET, POST, ...)
     * @param path - path to save message
     * @param version - HTTP version (HTTP 1.1)
     * @return String json
     */
    public String getJsonHeader(String method, String path, String version) {
        JSONObject header = new JSONObject();
        header.put("Method", method);
        header.put("Request-URI", path);
        header.put("HTTP-Version", version);
        return HTTP.toString(header);
    }

    /**
     * Parse json method for get string method
     * @param header - json HEADER string
     * @return method
     */
    public String getMethod(String header) {
        JSONObject obj = HTTP.toJSONObject(header);
        return obj.getString("Method");
    }

    /**
     * Parse json method for get string method
     * @param json - json string for parsing
     * @return mode - queue or topic
     */
    public synchronized String getMode(String json) {
        var jsonObject = new JSONObject(json);
        return jsonObject.getString("mode");
    }

    /**
     * Parse json for get theme
     * @param json - String json for parsing
     * @return theme (used in Map storage as key)
     */
    public synchronized String getTheme(String json) {
        var jsonObject = new JSONObject(json);
        return jsonObject.get("theme").toString();
    }
}
