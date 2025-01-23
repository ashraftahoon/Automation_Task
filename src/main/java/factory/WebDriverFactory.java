package factory;

import configReader.ConfigPropReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.logging.Logger;

public class WebDriverFactory {

    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static ConfigPropReader configPropReader;
    private static final Logger logger = Logger.getLogger(WebDriverFactory.class.getName());

    static {
        configPropReader = new ConfigPropReader(CONFIG_FILE_PATH);
    }

    public static WebDriver getDriver() {
        WebDriver driver = null;
        String browser = configPropReader.getBrowser();

        boolean runLocally = Boolean.parseBoolean(configPropReader.getProperty("runLocally"));
        boolean runRemotely = Boolean.parseBoolean(configPropReader.getProperty("runRemotely"));
        boolean runOnGrid = Boolean.parseBoolean(configPropReader.getProperty("runOnGrid"));
        boolean runOnCloud = Boolean.parseBoolean(configPropReader.getProperty("runOnCloud"));

        try {
            if (runLocally) {
                driver = createLocalDriver(browser);
            } else if (runRemotely) {
                driver = createRemoteDriver();
            } else if (runOnGrid) {
                driver = createGridDriver();
            } else if (runOnCloud) {
                driver = createCloudDriver();
            }
        } catch (Exception e) {
            logger.severe("Error initializing WebDriver: " + e.getMessage());
            e.printStackTrace();
        }

        return driver;
    }

    private static WebDriver createLocalDriver(String browser) {
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (Boolean.parseBoolean(configPropReader.getProperty("chromeHeadless"))) {
                    chromeOptions.addArguments("--headless");
                }
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        return driver;
    }


    private static WebDriver createRemoteDriver() {
        try {
            return new RemoteWebDriver(new URL("http://remote-server-url:4444/wd/hub"), new ChromeOptions());
        } catch (Exception e) {
            logger.severe("Error setting up remote WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize Remote WebDriver", e);
        }
    }

    private static WebDriver createGridDriver() {
        try {
            String gridHubUrl = configPropReader.getProperty("gridHubUrl");
            return new RemoteWebDriver(new URL(gridHubUrl), new ChromeOptions());
        } catch (Exception e) {
            logger.severe("Error setting up Grid WebDriver: " + e.getMessage());
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
            return new RemoteWebDriver(new URL(cloudUrl), caps);
        } catch (Exception e) {
            logger.severe("Error setting up Cloud WebDriver: " + e.getMessage());
            throw new RuntimeException("Failed to initialize Cloud WebDriver", e);
        }
    }
}
