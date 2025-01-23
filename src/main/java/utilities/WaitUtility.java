package utilities;

import configReader.ConfigPropReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class WaitUtility {

    private WebDriver driver;
    private static ConfigPropReader configPropReader;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";;
    private static final Logger logger = LogUtility.getLogger();
    // Constructor that accepts WebDriver instance
    public WaitUtility(WebDriver driver) {
        this.driver = driver;
        configPropReader = new ConfigPropReader(CONFIG_FILE_PATH);
    }

    // Get wait time from properties file
    private int getImplicitWait() {
        return Integer.parseInt(configPropReader.getProperty("implicitWait")); // default to 10 if not found
    }

    // Set Implicit Wait
    public void setImplicitWait() {
        int timeout = getImplicitWait();
        logger.info("Setting implicit wait to " + timeout + " seconds.");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
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

    // Fluent Wait for Element with Custom Polling Interval
    public WebElement fluentWaitForElement(WebElement element, int pollingTime) {
        int timeout = Integer.parseInt(configPropReader.getProperty("explicitWait"));
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(timeout)).pollingEvery(Duration.ofMillis(pollingTime)).ignoring(Exception.class);

        return fluentWait.until(ExpectedConditions.visibilityOf(element));
    }

    // Wait for Element by Locator
    public WebElement waitForElementByLocator(By locator) {
        int timeout = Integer.parseInt(configPropReader.getProperty("explicitWait"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Wait for Page to Load Completely
    public void waitForPageToLoad() {
        int timeout = Integer.parseInt(configPropReader.getProperty("pageLoadTimeout"));
        logger.info("Waiting for the page to load completely (timeout: " + timeout + " seconds).");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        wait.until(driver -> ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }
}
