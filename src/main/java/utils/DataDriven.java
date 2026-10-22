package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class DataDriven {
    private static final String JSON_PATH = "testData/testData.json";

    /**
     * Reads the whole JSON file and returns it as a JsonObject.
     */
    public static JsonObject jsonReader() {
        try (Reader reader = new FileReader(JSON_PATH)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("Could not read test data file: " + JSON_PATH, e);
        }
    }

    /**
     * Convenience: reads a string such as validUser -> username.
     */
    public static String getString(String section, String key) {
        return jsonReader().getAsJsonObject(section).get(key).getAsString();
    }

    /**
     * Convenience: reads a JSON array of strings such as cartProducts.
     */
    public static List<String> getList(String key) {
        JsonArray array = jsonReader().getAsJsonArray(key);
        List<String> result = new ArrayList<>();
        for (JsonElement element : array) {
            result.add(element.getAsString());
        }
        return result;
    }
}
