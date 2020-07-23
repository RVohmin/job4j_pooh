package ru.job4j.test.storage;

import org.junit.Test;
import ru.job4j.JsonMessage;
import ru.job4j.Parser;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void generateJson() {
        JsonMessage jsonMessage = new JsonMessage();
        String actual = jsonMessage.
                generateJsonMessage(
                        "queue",
                        "weather",
                        "temperature +18 C");
        String expected = "{\n" +
                "\t\"queue\" : \"weather\",\n" +
                "\t\"text\" : \"temperature +18 C\"\n" +
                "}\n";
        assertEquals(expected, actual);
    }

    @Test
    public void getMode() {
        Parser parser = new Parser();
        JsonMessage jsonMessage = new JsonMessage();
        String json = jsonMessage.
                generateJsonMessage(
                        "queue",
                        "weather",
                        "temperature +18 C");
        String mode = parser.getMode(json);
        assertEquals("queue", mode);
    }

    @Test
    public void getTheme() {
        Parser parser = new Parser();

        String jsonText = "{\n" +
                "  \"queue\" : \"weather\",\n" +
                "  \"text\" : \"temperature +18 C\"\n" +
                "}";
        String theme = parser.getTheme(jsonText);
        assertEquals("weather", theme);
    }

    @Test
    public void getText() {
        Parser parser = new Parser();
        String jsonText = "{\n" +
                "  \"queue\" : \"weather\",\n" +
                "  \"text\" : \"temperature +18 C\"\n" +
                "}";
        String text = parser.getText(jsonText);
        assertEquals("temperature +18 C", text);
    }
}