package drivers;

import configReader.ConfigPropReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.net.URL;

public class BrowserFactory {

    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final ConfigPropReader configPropReader = new ConfigPropReader(CONFIG_FILE_PATH);
    private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);

    private BrowserFactory() {
        // Private 0constructor to prevent instantiation
    }

    public static WebDriver getDriver() {
        String browser = configPropReader.getBrowser();
        String executionMode = getExecutionMode();

        try {
            return switch (executionMode) {
                case "local" -> createLocalDriver(browser);
                case "remote" -> createRemoteDriver();
                case "grid" -> createGridDriver();
                case "cloud" -> createCloudDriver();
                default -> throw new IllegalArgumentException("Unsupported execution mode: " + executionMode);
            };
        } catch (Exception e) {
            logger.error("Error initializing WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }

    private static String getExecutionMode() {
        if (Boolean.parseBoolean(configPropReader.getProperty("runLocally"))) {
            return "local";
        } else if (Boolean.parseBoolean(configPropReader.getProperty("runRemotely"))) {
            return "remote";
        } else if (Boolean.parseBoolean(configPropReader.getProperty("runOnGrid"))) {
            return "grid";
        } else if (Boolean.parseBoolean(configPropReader.getProperty("runOnCloud"))) {
            return "cloud";
        } else {
            throw new IllegalArgumentException("No valid execution mode found in configuration.");
        }
    }

    private static WebDriver createLocalDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> new FirefoxDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        if (Boolean.parseBoolean(configPropReader.getProperty("chromeHeadless"))) {
            chromeOptions.addArguments("--headless");
        }
        logger.info("Initializing ChromeDriver with options: {}", chromeOptions);
        return new ChromeDriver(chromeOptions);
    }

    private static WebDriver createRemoteDriver() {
        String REMOTE_SERVER_URL = "http://remote-server-url:4444/wd/hub";
        try {
            URL remoteUrl = URI.create(REMOTE_SERVER_URL).toURL();
            logger.info("Initializing Remote WebDriver");
            return new RemoteWebDriver(remoteUrl, new ChromeOptions());
        } catch (Exception e) {
            logger.error("Error setting up remote WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize Remote WebDriver", e);
        }
    }

    private static WebDriver createGridDriver() {
        try {
            String gridHubUrl = configPropReader.getProperty("gridHubUrl");
            URL remoteUrl = URI.create(gridHubUrl).toURL();
            logger.info("Initializing Grid WebDriver with hub URL: {}", gridHubUrl);
            return new RemoteWebDriver(remoteUrl, new ChromeOptions());
        } catch (Exception e) {
            logger.error("Error setting up Grid WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize Grid WebDriver", e);
        }
    }

    private static WebDriver createCloudDriver() {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browserName", "chrome");
            caps.setCapability("platform", "Windows 10");
            caps.setCapability("browserVersion", "latest");

            String cloudUrl = "https://username:accessKey@hub-cloud.browserstack.com/wd/hub";
            URL remoteUrl = URI.create(cloudUrl).toURL();
            return new RemoteWebDriver(remoteUrl, caps);
        } catch (Exception e) {
            logger.error("Error setting up Cloud WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize Cloud WebDriver", e);
        }
    }
}