package utilities;

import configReader.ConfigPropReader;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;
import java.util.logging.Logger;

public class WaitUtility {

    private static ConfigPropReader configPropReader;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final Logger logger = LogUtility.getLogger();
    private final WebDriver driver;

    // Constructor that accepts WebDriver instance
    public WaitUtility(WebDriver driver) {
        this.driver = driver;
        configPropReader = new ConfigPropReader(CONFIG_FILE_PATH);
    }

    // wait for element to present
    public WebElement customWaitForElementPresent(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(15)).until(driver1 -> driver1.findElement(locator));
    }

    // wait for element to be visible
    public WebElement customWaitForElementVisible(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(15)).until(driver1 -> {
            WebElement element = customWaitForElementPresent(locator);
            return element.isDisplayed() ? element : null;
        });
    }

    // wait for element to be clickable
    public WebElement customWaitForElementClickable(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(15)).until(driver1 -> {
            WebElement element = customWaitForElementVisible(locator);
            return element.isEnabled() ? element : null;
        });
    }


    // Wait for Element Visibility
    public WebElement waitForElementVisibility(WebElement element) {
        int timeout = Integer.parseInt(configPropReader.getProperty("explicitWait"));
        logger.info("Waiting for visibility of element: " + element);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Wait for Element to be Clickable
    public WebElement waitForElementToBeClickable(WebElement element) {
        int timeout = Integer.parseInt(configPropReader.getProperty("explicitWait"));
        logger.info("Waiting for element to be clickable: " + element);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }


    // waitForAlert
    public Alert waitForAlert() {
        // Get the timeout value from the config file
        int timeout = Integer.parseInt(configPropReader.getProperty("explicitWait"));
        // Log the waiting action for the alert
        logger.info("Waiting for alert to be present");
        // Create a WebDriverWait instance with the timeout
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        // Wait for the alert to be present and return the alert
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    // Fluent Wait for Element with Custom Polling Interval
    public WebElement fluentWaitForElement(WebElement element, int pollingTime) {
        int timeout = Integer.parseInt(configPropReader.getProperty("explicitWait"));
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofMillis(pollingTime)).ignoring(Exception.class);

        return fluentWait.until(ExpectedConditions.visibilityOf(element));
    }



    // Wait for Page to Load Completely
    public void waitForPageToLoad() {
        int timeout = Integer.parseInt(configPropReader.getProperty("pageLoadTimeout"));
        logger.info("Waiting for the page to load completely (timeout: " + timeout + " seconds).");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(driver1 -> Objects.equals(((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return document.readyState"), "complete"));
    }


}
