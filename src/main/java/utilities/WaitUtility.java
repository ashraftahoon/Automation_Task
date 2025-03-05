package utilities;

import configReader.ConfigPropReader;
import drivers.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Function;

public class WaitUtility {

    private static final Logger logger = LoggerFactory.getLogger(WaitUtility.class);
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private final WebDriver driver;
    private final ConfigPropReader configPropReader;
    private final int defaultExplicitWait;
    private final int defaultPageLoadTimeout;

    // Constructor that accepts WebDriver instance
    public WaitUtility() {
        this.driver = DriverManager.getDriver();
        this.configPropReader = new ConfigPropReader(CONFIG_FILE_PATH);
        this.defaultExplicitWait = getConfigPropertyAsInt("explicitWait", 15); // Default timeout: 15 seconds
        this.defaultPageLoadTimeout = getConfigPropertyAsInt("pageLoadTimeout", 30); // Default timeout: 30 seconds
        logger.info("Initialized WaitUtility with defaultExplicitWait: {}s and defaultPageLoadTimeout: {}s",
                defaultExplicitWait, defaultPageLoadTimeout);
    }

    // Helper method to get configuration properties as integers with a default value
    private int getConfigPropertyAsInt(String propertyName, int defaultValue) {
        try {
            return Integer.parseInt(configPropReader.getProperty(propertyName));
        } catch (NumberFormatException e) {
            logger.warn("Invalid or missing property '{}'. Using default value: {}", propertyName, defaultValue, e);
            return defaultValue;
        }
    }

    // Helper method to create a WebDriverWait instance with a custom timeout
    private WebDriverWait createWebDriverWait(int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }

    // Wait for element to be present using its locator
    public WebElement waitForElementPresent(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for presence of element located by: {} with timeout: {}s", locator, timeoutInSeconds);
        WebDriverWait wait = createWebDriverWait(timeoutInSeconds);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    // Wait for element to be visible using its locator
    public WebElement waitForElementVisible(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for visibility of element located by: {} with timeout: {}s", locator, timeoutInSeconds);
        WebDriverWait wait = createWebDriverWait(timeoutInSeconds);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Wait for element to be clickable using its locator
    public WebElement waitForElementClickable(By locator, int timeoutInSeconds) {
        logger.debug("Waiting for element to be clickable: {} with timeout: {}s", locator, timeoutInSeconds);
        WebDriverWait wait = createWebDriverWait(timeoutInSeconds);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Wait for element visibility given a WebElement (already located)
    public WebElement waitForElementVisibility(WebElement element, int timeoutInSeconds) {
        logger.debug("Waiting for visibility of element {} with timeout: {}s", element, timeoutInSeconds);
        WebDriverWait wait = createWebDriverWait(timeoutInSeconds);
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Wait for element to be clickable given a WebElement (already located)
    public WebElement waitForElementToBeClickable(WebElement element, int timeoutInSeconds) {
        logger.debug("Waiting for element {} to be clickable with timeout: {}s", element, timeoutInSeconds);
        WebDriverWait wait = createWebDriverWait(timeoutInSeconds);
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    // Wait for an alert to be present and return it
    public Alert waitForAlert(int timeoutInSeconds) {
        logger.debug("Waiting for alert to be present with timeout: {}s", timeoutInSeconds);
        WebDriverWait wait = createWebDriverWait(timeoutInSeconds);
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    // Fluent wait for an element with a custom polling interval
    public WebElement fluentWaitForElement(By locator, int timeoutInSeconds, int pollingIntervalInMillis) {
        logger.debug("Fluently waiting for element {} with timeout: {}s and polling interval: {}ms",
                locator, timeoutInSeconds, pollingIntervalInMillis);
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofMillis(pollingIntervalInMillis))
                .ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

        return fluentWait.until(driver -> driver.findElement(locator));
    }

    // Wait for the page to load completely
    public void waitForPageToLoad(int timeoutInSeconds) {
        logger.debug("Waiting for page to load completely with timeout: {}s", timeoutInSeconds);
        WebDriverWait wait = createWebDriverWait(timeoutInSeconds);
        wait.until(driver -> {
            String state = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
            logger.debug("Current document.readyState: {}", state);
            return Objects.equals(state, "complete");
        });
        logger.info("Page loaded completely.");
    }

    // Overloaded methods with default timeouts
    public WebElement waitForElementPresent(By locator) {
        return waitForElementPresent(locator, defaultExplicitWait);
    }

    public WebElement waitForElementVisible(By locator) {
        return waitForElementVisible(locator, defaultExplicitWait);
    }

    public WebElement waitForElementClickable(By locator) {
        return waitForElementClickable(locator, defaultExplicitWait);
    }

    public WebElement waitForElementVisibility(WebElement element) {
        return waitForElementVisibility(element, defaultExplicitWait);
    }

    public WebElement waitForElementToBeClickable(WebElement element) {
        return waitForElementToBeClickable(element, defaultExplicitWait);
    }

    public Alert waitForAlert() {
        return waitForAlert(defaultExplicitWait);
    }

    public void waitForPageToLoad() {
        waitForPageToLoad(defaultPageLoadTimeout);
    }
}