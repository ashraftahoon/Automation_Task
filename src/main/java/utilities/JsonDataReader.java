package utilities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonDataReader {

    private final Map<String, Object> jsonDataCache = new HashMap<>();
    private static final String CACHE_KEY = "jsonData";

    /**
     * Reads the JSON file and stores the data in a Map, handling various types of JSON objects.
     * Caches the data for future use.
     *
     * @param jsonFilePath the path to the JSON file.
     * @throws IOException    if there is an error reading the file.
     * @throws ParseException if there is an error parsing the JSON data.
     */
    public void dataReader(String jsonFilePath) throws IOException, ParseException {
        // Check if the data is already cached
        if (jsonDataCache.containsKey(CACHE_KEY)) {
            System.out.println("Using cached data...");
            return;  // Skip re-reading if data is already cached
        }

        File jsonFile = new File(jsonFilePath);

        // Ensure file exists before proceeding
        if (!jsonFile.exists()) {
            throw new IOException("JSON file not found: " + jsonFilePath);
        }

        try (FileReader reader = new FileReader(jsonFile)) {
            JSONParser jsonParser = new JSONParser();
            Object parsedData = jsonParser.parse(reader);

            // If the root object is an array
            if (parsedData instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) parsedData;
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    jsonDataCache.put("object" + i, parseObject(jsonObject));
                }
            }
            // If the root object is a JSONObject (key-value pairs)
            else if (parsedData instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) parsedData;
                jsonDataCache.put("root", parseObject(jsonObject));
            } else {
                throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN,
                        "Unexpected root element, expected JSONObject or JSONArray.");
            }
        } catch (IOException | ParseException e) {
            // Catch specific exceptions and rethrow them for further handling
            throw e;
        }
    }

    /**
     * Parses a JSONObject recursively and stores it as a Map<String, Object>.
     *
     * @param jsonObject the JSONObject to be parsed.
     * @return a Map containing the key-value pairs of the JSONObject.
     */
    private Map<String, Object> parseObject(JSONObject jsonObject) {
        Map<String, Object> objectMap = new HashMap<>();
        for (Object key : jsonObject.keySet()) {
            String keyStr = (String) key;
            Object value = jsonObject.get(keyStr);

            // Handle nested objects (recursive parsing)
            if (value instanceof JSONObject) {
                objectMap.put(keyStr, parseObject((JSONObject) value));
            }
            // Handle arrays (iterate over arrays)
            else if (value instanceof JSONArray) {
                objectMap.put(keyStr, parseArray((JSONArray) value));
            } else {
                objectMap.put(keyStr, value);
            }
        }
        return objectMap;
    }

    /**
     * Parses a JSONArray and stores it as a list of objects.
     *
     * @param jsonArray the JSONArray to be parsed.
     * @return a list of objects inside the array.
     */
    private List<Object> parseArray(JSONArray jsonArray) {
        List<Object> arrayList = new ArrayList<>();
        for (Object element : jsonArray) {
            if (element instanceof JSONObject) {
                arrayList.add(parseObject((JSONObject) element));
            } else {
                arrayList.add(element);
            }
        }
        return arrayList;
    }

    // Getter for the parsed JSON data (can be accessed later for validation, testing, etc.)
    public Map<String, Object> getJsonDataMap() {
        return jsonDataCache;  // Return the cached data
    }
}
