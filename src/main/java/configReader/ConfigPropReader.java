package configReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigPropReader {
    private final Properties properties;

    public  ConfigPropReader(String filePath) {
        // initialize properties object
        properties = new Properties();
        try {
            // create file input stream to read properties from the file
            FileInputStream fileInputStream = new FileInputStream(filePath);
            //load properties from file
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file: " + filePath, e);
        }
    }

    // method to get the value of any property based on the key
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    // create Getter methods for specific properties
    public String getBrowser() {
        return getProperty("browser");
    }

}

