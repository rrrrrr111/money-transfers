package ru.roman.app.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class StringSign {

    /**
     * Returns string sign
     */
    public Map<Character, Integer> getSign(String str) {

        if (str == null) {
            return Collections.emptyMap();
        }
        return str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
    }

    public String upperCase4charsWords(String str) {

        if (str == null) {
            return null;
        }
        String[] words = str.split("\\s");
        return Arrays.stream(words)
                .map(w -> w.length() == 4 ? w.toUpperCase() : w)
                .collect(Collectors.joining(" "));
    }
}
