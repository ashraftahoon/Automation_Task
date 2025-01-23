package configReader;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigPropReader {
    private Properties properties;

    public  ConfigPropReader(String filePath) {

        try {
            // create file input stream to read properties from the file
            FileInputStream fileInputStream = new FileInputStream(filePath);
            // initialize properties object
            properties = new Properties();
            //load properties from file
            properties.load(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
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

