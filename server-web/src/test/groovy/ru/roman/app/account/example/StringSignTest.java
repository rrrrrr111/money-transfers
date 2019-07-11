package ru.roman.app.account.example;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.roman.app.example.StringSign;

import java.util.Collections;
import java.util.Map;


@Test
public class StringSignTest {

    private StringSign stringSign = new StringSign();

    @DataProvider()
    private Object[][] dataProvider() {
        return new Object[][]{
                {"a", Map.of('a', 1)},
                {"bab", Map.of('a', 1, 'b', 2)},
                {"bba", Map.of('a', 1, 'b', 2)},
                {"", Collections.emptyMap()},
                {null, Collections.emptyMap()},
                {"ффф", Map.of('ф', 3)},
        };
    }

    @Test(dataProvider = "dataProvider")
    public void testSign(String str, Map<Character, Integer> expected) {

        Map<Character, Integer> res = stringSign.getSign(str);
        Assert.assertTrue(expected.equals(res));
    }

    @DataProvider()
    private Object[][] upperCase4charsWordsDataProvider() {
        return new Object[][]{
                {null, null},
                {"", ""},
                {"a", "a"},
                {"aaaa", "AAAA"},
                {"aaaa c bbbb", "AAAA c BBBB"},
                {"this is just a test", "THIS is JUST a TEST"},
                {"my name is Cesar", "my NAME is Cesar"},
        };
    }

    @Test(dataProvider = "upperCase4charsWordsDataProvider")
    public void testUpperCase4charsWords(String str, String expected) {
        String res = stringSign.upperCase4charsWords(str);
        Assert.assertEquals(res, expected);
    }
}