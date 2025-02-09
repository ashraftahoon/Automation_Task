package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ElementActions {

    private final WebDriver driver;
    private final WaitUtility waitUtility;
    private final Scrolling scrolling;
    private static final Logger logger = LoggerFactory.getLogger(ElementActions.class);

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
            WebElement element = waitUtility.waitForElementClickable(locator);
            scrolling.scrollToElement(element);
            element.click();
            logger.info("Clicked on element located by: {}", locator);
        });
    }

    // Click on an element using Actions
    @Step("Clicking on element located by: {0} using Actions")
    public void clickByAction(By locator) {
        executeAction(locator, "click using Actions", () -> {
            WebElement element = waitUtility.waitForElementClickable(locator);
            scrolling.scrollToElement(element);
            new Actions(driver).moveToElement(element).click().perform();
            logger.info("Clicked on element located by: {} using Actions", locator);
        });
    }

    // Click on an element using JavaScript
    @Step("Clicking on element located by: {0} using JavaScript")
    public void clickByJavaScript(By locator) {
        executeAction(locator, "click using JavaScript", () -> {
            WebElement element = waitUtility.waitForElementClickable(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            logger.info("Clicked on element located by: {} using JavaScript", locator);
        });
    }

    // Type text into an element using its locator
    @Step("Typing text '{1}' into element located by: {0}")
    public void typeText(By locator, String text) {
        executeAction(locator, "type text '" + text + "'", () -> {
            WebElement element = waitUtility.waitForElementVisible(locator);
            scrolling.scrollToElement(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Typed text '{}' into element located by: {}", text, locator);
        });
    }

    // Select an option from a dropdown by visible text
    @Step("Selecting option '{1}' from dropdown located by: {0}")
    public void selectDropdownByText(By locator, String text) {
        executeAction(locator, "select dropdown option '" + text + "'", () -> {
            WebElement element = waitUtility.waitForElementVisible(locator);
            scrolling.scrollToElement(element);
            new Select(element).selectByVisibleText(text);
            logger.info("Selected dropdown option '{}' from element located by: {}", text, locator);
        });
    }

    // Select an option from a dropdown by value
    @Step("Selecting option with value '{1}' from dropdown located by: {0}")
    public void selectDropdownByValue(By locator, String value) {
        executeAction(locator, "select dropdown option with value '" + value + "'", () -> {
            WebElement element = waitUtility.waitForElementVisible(locator);
            scrolling.scrollToElement(element);
            new Select(element).selectByValue(value);
            logger.info("Selected dropdown option with value '{}' from element located by: {}", value, locator);
        });
    }

    // Select an option from a dropdown by index
    @Step("Selecting option at index {1} from dropdown located by: {0}")
    public void selectDropdownByIndex(By locator, int index) {
        executeAction(locator, "select dropdown option at index " + index, () -> {
            WebElement element = waitUtility.waitForElementVisible(locator);
            scrolling.scrollToElement(element);
            new Select(element).selectByIndex(index);
            logger.info("Selected dropdown option at index {} from element located by: {}", index, locator);
        });
    }

    // Check or uncheck a checkbox
    @Step("Setting checkbox located by: {0} to {1}")
    public void setCheckbox(By locator, boolean shouldBeChecked) {
        executeAction(locator, "set checkbox to " + shouldBeChecked, () -> {
            WebElement checkbox = waitUtility.waitForElementVisible(locator);
            scrolling.scrollToElement(checkbox);
            if (checkbox.isSelected() != shouldBeChecked) {
                checkbox.click();
            }
            logger.info("Set checkbox located by: {} to {}", locator, shouldBeChecked);
        });
    }

    // Select a radio button
    @Step("Selecting radio button located by: {0}")
    public void selectRadioButton(By locator) {
        executeAction(locator, "select radio button", () -> {
            WebElement radioButton = waitUtility.waitForElementClickable(locator);
            scrolling.scrollToElement(radioButton);
            if (!radioButton.isSelected()) {
                radioButton.click();
            }
            logger.info("Selected radio button located by: {}", locator);
        });
    }

    // Perform drag-and-drop
    @Step("Dragging element located by: {0} and dropping it on element located by: {1}")
    public void dragAndDrop(By sourceLocator, By targetLocator) {
        executeAction(sourceLocator, "drag and drop", () -> {
            WebElement sourceElement = waitUtility.waitForElementClickable(sourceLocator);
            WebElement targetElement = waitUtility.waitForElementClickable(targetLocator);
            scrolling.scrollToElement(sourceElement);
            scrolling.scrollToElement(targetElement);
            new Actions(driver).dragAndDrop(sourceElement, targetElement).perform();
            logger.info("Dragged element located by: {} and dropped it on element located by: {}", sourceLocator, targetLocator);
        });
    }

    // Perform hover action
    @Step("Hovering over element located by: {0}")
    public void hover(By locator) {
        executeAction(locator, "hover", () -> {
            WebElement element = waitUtility.waitForElementVisible(locator);
            scrolling.scrollToElement(element);
            new Actions(driver).moveToElement(element).perform();
            logger.info("Hovered over element located by: {}", locator);
        });
    }

    // Upload a file using a file input element
    @Step("Uploading file '{1}' to element located by: {0}")
    public void uploadFile(By locator, String filePath) {
        executeAction(locator, "upload file '" + filePath + "'", () -> {
            WebElement fileInput = waitUtility.waitForElementVisible(locator);
            scrolling.scrollToElement(fileInput);
            fileInput.sendKeys(new File(filePath).getAbsolutePath());
            logger.info("Uploaded file '{}' to element located by: {}", filePath, locator);
        });
    }

    // Handle alert with a specific action
    @Step("Handling alert: {0}")
    public void handleAlert(AlertAction action) {
        try {
            logger.info("Attempting to handle alert with action: {}", action);
            Alert alert = waitUtility.waitForAlert();
            switch (action) {
                case ACCEPT:
                    logger.info("Accepting alert: {}", alert.getText());
                    alert.accept();
                    break;
                case DISMISS:
                    logger.info("Dismissing alert: {}", alert.getText());
                    alert.dismiss();
                    break;
                case GET_TEXT:
                    logger.info("Alert text: {}", alert.getText());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported AlertAction: " + action);
            }
        } catch (Exception e) {
            logger.error("Failed to handle alert: {}", e.getMessage(), e);
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
            logger.info("Attempting to {} on element located by: {}", actionDescription, locator);
            action.run();
            logger.info("Successfully performed {} on element located by: {}", actionDescription, locator);
        } catch (Exception e) {
            logger.error("Failed to {} on element located by: {}", actionDescription, locator, e);
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