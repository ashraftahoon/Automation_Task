package utilities;

import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationUtility {

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtility.class);
    private static final SoftAssert softAssert = new SoftAssert();

    // Private constructor to prevent instantiation
    private ValidationUtility() {
        throw new IllegalStateException("Utility class");
    }

    ///////////////////////////////////////////////////
    // Hard Assertions (Fail immediately)
    ///////////////////////////////////////////////////

    /**
     * Hard Assertion: Fails immediately if actual != expected.
     */
    public static void hardAssertEquals(String actual, String expected, String message) {
        logger.info("Hard Assertion: Validating if '{}' equals '{}'", actual, expected);
        if (!actual.equals(expected)) {
            logger.error("Hard Assertion FAILED: {}", message);
            throw new AssertionError(message + " | Actual: " + actual + ", Expected: " + expected);
        }
        logger.info("Hard Assertion PASSED: {}", message);
    }

    /**
     * Hard Assertion: Fails immediately if element is not displayed.
     */
    public static void hardAssertElementDisplayed(WebElement element, String message) {
        logger.info("Hard Assertion: Validating if element is displayed.");
        if (!element.isDisplayed()) {
            logger.error("Hard Assertion FAILED: {}", message);
            throw new AssertionError(message);
        }
        logger.info("Hard Assertion PASSED: {}", message);
    }

    ///////////////////////////////////////////////////
    // Soft Assertions (Collect failures and fail at the end)
    ///////////////////////////////////////////////////

    /**
     * Soft Assertion: Collects failure if actual != expected.
     */
    public static void softAssertEquals(String actual, String expected, String message) {
        logger.info("Soft Assertion: Validating if '{}' equals '{}'", actual, expected);
        softAssert.assertEquals(actual, expected, message);
    }

    /**
     * Soft Assertion: Collects failure if elements is not displayed.
     */
    public static void softAssertElementDisplayed(WebElement element, String message) {
        logger.info("Soft Assertion: Validating if element is displayed.");
        softAssert.assertTrue(element.isDisplayed(), message);
    }

    // Soft assert for boolean condition
    public static void softAssertTrue(boolean condition, String message) {
        logger.info("Soft Assertion: {}", message);
        softAssert.assertTrue(condition, message);
    }

    ///////////////////////////////////////////////////
    // Common Validations (Return boolean for flexibility)
    ///////////////////////////////////////////////////

    /**
     * Validates if two strings are equal (returns boolean).
     */
    public static boolean validateTextEquals(String actual, String expected) {
        logger.info("Validating if '{}' equals '{}'", actual, expected);
        boolean result = actual.equals(expected);
        logValidationResult(result, "Texts are equal");
        return result;
    }

    /**
     * Validates if a WebElement is displayed (returns boolean).
     */
    public static boolean validateElementDisplayed(WebElement element) {
        logger.info("Validating if element is displayed.");
        boolean result = element.isDisplayed();
        logValidationResult(result, "Element is displayed");
        return result;
    }

    // Helper method to log validation results
    private static void logValidationResult(boolean result, String successMessage) {
        if (result) {
            logger.info("Validation PASSED: {}", successMessage);
        } else {
            logger.error("Validation FAILED: {}", successMessage);
        }
    }

    ///////////////////////////////////////////////////
    // Assert All Functionality
    ///////////////////////////////////////////////////

    /**
     * Asserts all collected soft assertions.
     * This should be called at the end of the test execution.
     */
    public static void assertAll() {
        logger.info("Asserting all soft assertions.");
        softAssert.assertAll();
    }
}