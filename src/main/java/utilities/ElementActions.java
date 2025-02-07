package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;

public class ElementActions {

    private final WebDriver driver;
    private final WaitUtility waitUtility;
    private final Scrolling scrolling;

    // Constructor with only WebDriver (reducing unnecessary dependencies)
    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waitUtility = new WaitUtility(driver); // Create instance inside the class
        this.scrolling = new Scrolling(driver);     // Create instance inside the class
    }


    // Click on an element using its locator
    @Step("Clicking on element located by: {0}")
    public void click(By locator) {
        executeAction(locator, "click", () -> {
            WebElement element = waitUtility.waitForElementToBeClickable((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            element.click();
        });
    }

    // Click on an element using Actions
    @Step("Clicking on element located by: {0} using Actions")
    public void clickByAction(By locator) {
        executeAction(locator, "click using Actions", () -> {
            WebElement element = waitUtility.waitForElementToBeClickable((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            new Actions(driver).moveToElement(element).click().perform();
        });
    }

    // Click on an element using JavaScript
    @Step("Clicking on element located by: {0} using JavaScript")
    public void clickByJavaScript(By locator) {
        executeAction(locator, "click using JavaScript", () -> {
            WebElement element = waitUtility.waitForElementToBeClickable((WebElement) locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        });
    }

    // Type text into an element using its locator
    @Step("Typing text '{1}' into element located by: {0}")
    public void typeText(By locator, String text) {
        executeAction(locator, "type text '" + text + "'", () -> {
            WebElement element = waitUtility.waitForElementVisibility((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            element.clear();
            element.sendKeys(text);
        });
    }

    // Select an option from a dropdown by visible text
    @Step("Selecting option '{1}' from dropdown located by: {0}")
    public void selectDropdownByText(By locator, String text) {
        executeAction(locator, "select dropdown option '" + text + "'", () -> {
            WebElement element = waitUtility.waitForElementVisibility((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            new Select(element).selectByVisibleText(text);
        });
    }

    // Select an option from a dropdown by value
    @Step("Selecting option with value '{1}' from dropdown located by: {0}")
    public void selectDropdownByValue(By locator, String value) {
        executeAction(locator, "select dropdown option with value '" + value + "'", () -> {
            WebElement element = waitUtility.waitForElementVisibility((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            new Select(element).selectByValue(value);
        });
    }

    // Select an option from a dropdown by index
    @Step("Selecting option at index {1} from dropdown located by: {0}")
    public void selectDropdownByIndex(By locator, int index) {
        executeAction(locator, "select dropdown option at index " + index, () -> {
            WebElement element = waitUtility.waitForElementVisibility((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            new Select(element).selectByIndex(index);
        });
    }

    // Check or uncheck a checkbox
    @Step("Setting checkbox located by: {0} to {1}")
    public void setCheckbox(By locator, boolean shouldBeChecked) {
        executeAction(locator, "set checkbox to " + shouldBeChecked, () -> {
            WebElement checkbox = waitUtility.waitForElementVisibility((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            if (checkbox.isSelected() != shouldBeChecked) {
                checkbox.click();
            }
        });
    }

    // Select a radio button
    @Step("Selecting radio button located by: {0}")
    public void selectRadioButton(By locator) {
        executeAction(locator, "select radio button", () -> {
            WebElement radioButton = waitUtility.waitForElementToBeClickable((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            if (!radioButton.isSelected()) {
                radioButton.click();
            }
        });
    }

    // Perform drag-and-drop
    @Step("Dragging element located by: {0} and dropping it on element located by: {1}")
    public void dragAndDrop(By sourceLocator, By targetLocator) {
        executeAction(sourceLocator, "drag and drop", () -> {
            WebElement sourceElement = waitUtility.waitForElementToBeClickable((WebElement) sourceLocator);
            WebElement targetElement = waitUtility.waitForElementToBeClickable((WebElement) targetLocator);
            scrolling.scrollToElement((WebElement) sourceLocator);
            scrolling.scrollToElement((WebElement) targetLocator);
            new Actions(driver).dragAndDrop(sourceElement, targetElement).perform();
        });
    }

    // Perform hover action
    @Step("Hovering over element located by: {0}")
    public void hover(By locator) {
        executeAction(locator, "hover", () -> {
            WebElement element = waitUtility.waitForElementVisibility((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            new Actions(driver).moveToElement(element).perform();
        });
    }

    // Upload a file using a file input element
    @Step("Uploading file '{1}' to element located by: {0}")
    public void uploadFile(By locator, String filePath) {
        executeAction(locator, "upload file '" + filePath + "'", () -> {
            WebElement fileInput = waitUtility.waitForElementVisibility((WebElement) locator);
            scrolling.scrollToElement((WebElement) locator);
            fileInput.sendKeys(new File(filePath).getAbsolutePath());
        });
    }

    // Handle alert with a specific action
    @Step("Handling alert: {0}")
    public void handleAlert(AlertAction action) {
        try {
            logInfo("Attempting to handle alert with action: " + action);
            Alert alert = waitUtility.waitForAlert();
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

    // Helper method to execute actions with logging and error handling
    private void executeAction(By locator, String actionDescription, Runnable action) {
        try {
            logInfo("Attempting to " + actionDescription + " on element located by: " + locator);
            action.run();
            logInfo("Successfully performed " + actionDescription + " on element located by: " + locator);
        } catch (Exception e) {
            logError("Failed to " + actionDescription + " on element located by: " + locator, e);
            throw new RuntimeException("Error performing " + actionDescription + " on element located by: " + locator, e);
        }
    }

    // Helper method to log info in Allure
    private static void logInfo(String message) {
        Allure.addAttachment("Info Log", message); // Attach info to Allure report
    }

    // Helper method to log error in Allure
    private static void logError(String message, Exception e) {
        Allure.addAttachment("Error Log", message + "\n" + e.getMessage()); // Attach error to Allure report
    }

    // Find an element using a locator
    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }
}