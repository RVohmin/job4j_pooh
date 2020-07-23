package ru.job4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ru.job4j.test.storage
 *
 * @author romanvohmin
 * @since 21.07.2020
 */
public class Parser {
    List<String> list = new ArrayList<>();

    private List<String> splitJson(String json) {
        Pattern pattern = Pattern.compile("[(,:\"{})]");
        String[] stringArray = pattern.split(json);
        for (String item : stringArray) {
            if (!item.isBlank()) {
                list.add(item);
            }
        }
        System.out.println(list);
        return list;
    }

    public String getMode(String headTitle) {
        splitJson(headTitle);
        return list.get(0);
    }

    public String getTheme(String jsonText) {
        splitJson(jsonText);
        return list.get(1);
    }

    public String getText(String jsonText) {
        splitJson(jsonText);
        return list.get(3);
    }
}
