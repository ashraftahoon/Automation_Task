package utilities;

import configReader.ConfigPropReader;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ElementActions {

    private WebDriver driver;
    private WebDriverWait wait;
    private static ConfigPropReader configPropReader;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        configPropReader = new ConfigPropReader(CONFIG_FILE_PATH);
        int timeout = Integer.parseInt(configPropReader.getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    // click element
    @Step("Clicking on element: {0}")
    public void click(WebElement element) {
        try {
            logInfo("Attempting to click on element: " + element);
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
            logInfo("Successfully clicked on element: " + element);
        } catch (Exception e) {
            logError("Failed to click on element: " + element, e);
            throw new RuntimeException("Error clicking on element: " + element, e);
        }
    }

    // click element using action
    @Step("Clicking on element using Actions: {0}")
    public void clickByAction(WebElement element) {
        try {
            logInfo("Attempting to click on element using Actions: " + element);
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().perform();
            logInfo("Successfully clicked on element using Actions: " + element);
        } catch (Exception e) {
            logError("Failed to click on element using Actions: " + element, e);
            throw new RuntimeException("Error clicking on element with Actions: " + element, e);
        }
    }

    // click element using JS
    @Step("Clicking on element using JavaScript: {0}")
    public void clickByJavaScript(WebElement element) {
        try {
            logInfo("Attempting to click on element using JavaScript: " + element);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            logInfo("Successfully clicked on element using JavaScript: " + element);
        } catch (Exception e) {
            logError("Failed to click on element using JavaScript: " + element, e);
            throw new RuntimeException("Error clicking on element with JavaScript: " + element, e);
        }
    }

    // method to typing text
    @Step("Typing text '{1}' into element: {0}")
    public void typeText(WebElement element, String text) {
        try {
            logInfo("Attempting to click on element: " + element);
            wait.until(ExpectedConditions.visibilityOf(element)).clear();
            element.sendKeys(text);
            logInfo("Successfully typed text '" + text + "' into element: " + element);
        } catch (Exception e) {
            logError("Failed to type text '" + text + "' into element: " + element, e);
            throw new RuntimeException("Error typing text into element: " + element, e);
        }
    }

    // method to handle alert
    @Step("Handling alert: {0}")
    public void handleAlert(AlertAction action) {
        try {
            logInfo("Attempting to handle alert with action: " + action);
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();

            switch (action) {
                case ACCEPT:
                    logInfo("Accepting alert: " + alert.getText());
                    alert.accept();
                    break;
                case DISMISS:
                    logInfo("Dismissing alert: " + alert.getText());
                    alert.dismiss();
                    break;
                case GET_TEXT:
                    logInfo("Alert text: " + alert.getText());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported AlertAction: " + action);
            }
        } catch (Exception e) {
            logError("Failed to handle alert: " + e.getMessage(), e);
            throw new RuntimeException("Error handling alert", e);
        }
    }

    // Enum for alert actions
    public enum AlertAction {
        ACCEPT, DISMISS, GET_TEXT
    }

    // Helper method to log info in Allure
    private void logInfo(String message) {
        Allure.addAttachment("Info Log", message); // Attach info to Allure report
    }

    // Helper method to log error in Allure
    private void logError(String message, Exception e) {
        Allure.addAttachment("Error Log", message + "\n" + e.getMessage()); // Attach error to Allure report
    }

}
