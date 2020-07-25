package ru.job4j;

import org.json.HTTP;
import org.json.JSONObject;

/**
 * ru.job4j
 *
 * @author romanvohmin
 * @since 25.07.2020
 */
public class JsonParser {

    public synchronized String generateJson(String mode, String theme, String message) {
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("mode", mode);
        jsonMessage.put("theme", theme);
        jsonMessage.put("text", message);
        return jsonMessage.toString();
    }

    public String getJsonHeader(String method, String path, String version) {
        JSONObject header = new JSONObject();
        header.put("Method", method);
        header.put("Request-URI", path);
        header.put("HTTP-Version", version);
        return HTTP.toString(header);
    }
    public String getMethod(String header) {
        JSONObject obj = HTTP.toJSONObject(header);
        return obj.getString("Method");
    }

    public synchronized String getMode(String json) {
        var jsonObject = new JSONObject(json);
        return jsonObject.getString("mode");
    }
    public synchronized String getTheme(String json) {
        var jsonObject = new JSONObject(json);
        return jsonObject.get("theme").toString();
    }
}
