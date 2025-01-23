package utilities;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.*;

public class LogUtility {

    private static Logger logger;

    static {
        try {
            // Load properties
            Properties properties = new Properties();
            properties.load(LogUtility.class.getClassLoader().getResourceAsStream("config.properties"));

            // Create logger instance
            logger = Logger.getLogger("TestLogger");

            // Set log level from properties
            Level logLevel = Level.parse(properties.getProperty("logLevel", "INFO").toUpperCase());
            logger.setLevel(logLevel);

            // Get log file path from properties
            String logFilePath = properties.getProperty("logFilePath", "logs/test.log");

            // Ensure the directory exists
            File logFile = new File(logFilePath);
            File logDir = logFile.getParentFile(); // Get the parent directory of the log file
            if (logDir != null && !logDir.exists()) {
                logDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Configure file handler
            FileHandler fileHandler = new FileHandler(logFilePath, true); // Append to the log file
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // Configure console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(logLevel);
            logger.addHandler(consoleHandler);

        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
