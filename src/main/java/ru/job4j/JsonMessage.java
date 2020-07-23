package ru.job4j;

/**
 * ru.job4j.test
 *
 * @author romanvohmin
 * @since 21.07.2020
 */
public class JsonMessage {
    private String path;

    public JsonMessage() {
    }

    public String getPath() {
        return path;
    }

    public String generateJsonMessage(String modeMessage, String themeMessage, String textMessage) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer
                .append("{").append(System.lineSeparator())
                .append("\t\"").append(modeMessage.toLowerCase()).append("\" : ")
                .append("\"").append(themeMessage.toLowerCase()).append("\",").append(System.lineSeparator())
                .append("\t\"text\" : \"").append(textMessage).append("\"").append(System.lineSeparator())
                .append("}").append(System.lineSeparator());
        return stringBuffer.toString();
    }
}
