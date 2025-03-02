package utilities;

import com.jayway.jsonpath.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class JsonDataReader {

    private static final String JSON_File_Path = "src/test/resources/";         //  Path to the JSON file
    String jsonReader;         //  JSON reader instance
    String jsonFileName;      //  JSON file name
    private static final Logger logger = LoggerFactory.getLogger(JsonDataReader.class); //  Logger instance

    // Constructor to load the JSON file
    public JsonDataReader(String jsonFileName) {
        this.jsonFileName = jsonFileName;
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(JSON_File_Path + jsonFileName + ".json"));
            jsonReader = jsonObject.toJSONString();
        } catch (IOException e) {
            logger.error("File not found at the specified path: {}", JSON_File_Path + jsonFileName + ".json");
        } catch (ParseException e) {
            logger.error("Error parsing JSON file: {}", e.getMessage());
        }
    }

    // Method to get JSON data based on JSON path
    public String getJsonData(String jsonPath) {
        String testData = "";
        try {
            testData = JsonPath.read(jsonReader, jsonPath);
        } catch (Exception e) {
            logger.error("Error reading JSON data: {}", e.getMessage());
        }
        return testData;
    }

}
