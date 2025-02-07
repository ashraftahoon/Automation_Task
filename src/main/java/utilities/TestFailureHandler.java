package utilities;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.logging.Logger;

/**
 * Utility class for handling Selenium test failures and reporting.
 * Captures screenshots, ARIA snapshots, error details, and attaches them to Allure reports.
 */
public class TestFailureHandler implements ITestListener {

    // Logger for better debugging
    private static final Logger logger = Logger.getLogger(TestFailureHandler.class.getName());

    // ThreadLocal WebDriver for parallel execution
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Retrieves the WebDriver instance for the current thread.
     *
     * @return WebDriver instance or null if not set.
     */
    private static WebDriver getWebDriver() {
        return driver.get();
    }

    /**
     * Sets the WebDriver instance for the current thread.
     *
     * @param webDriver WebDriver instance to set.
     */
    public static void setWebDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }

    /**
     * Captures an ARIA snapshot of the page elements with ARIA attributes.
     *
     * @return ARIA snapshot as a JSON string.
     */
    private String getAriasnapshot() {
        try {
            WebDriver webDriver = getWebDriver();
            if (webDriver == null) {
                return "ARIA snapshot skipped: WebDriver not available.";
            }

            JavascriptExecutor js = (JavascriptExecutor) webDriver;

            // Wait for ARIA elements to be present if needed (adjust the wait time as necessary)
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[aria-*]")));

            // Execute JavaScript to capture ARIA elements
            String result = (String) js.executeScript(
                    "let elementsWithAria = [...document.querySelectorAll('*')]" +
                            ".filter(e => [...e.attributes].some(attr => attr.name.startsWith('aria-')));" +
                            "console.log('Elements with ARIA attributes:', elementsWithAria.length);" +  // Debugging line
                            "return JSON.stringify(elementsWithAria.map(e => ({" +
                            "tagName: e.tagName," +
                            "attributes: [...e.attributes]" +
                            ".filter(attr => attr.name.startsWith('aria-'))" +
                            ".map(attr => ({ name: attr.name, value: attr.value })), " +
                            "innerText: e.innerText" +
                            "})));"
            );

            // Check if the result is an empty array or null
            if (result == null || result.equals("[]")) {
                return "No ARIA elements found.";
            } else {
                return result;
            }
        } catch (Exception e) {
            return "Failed to capture ARIA snapshot: " + e.getMessage();
        }
    }

    /**
     * Captures a screenshot of the current browser state.
     *
     * @return Screenshot as a byte array.
     */
    private byte[] captureScreenshot() {
        try {
            WebDriver webDriver = getWebDriver();
            if (webDriver == null) {
                return null;
            }
            TakesScreenshot screenshot = (TakesScreenshot) webDriver;
            return screenshot.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.severe("Error capturing screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Generates a detailed AI prompt with relevant context.
     *
     * @param result TestNG test result object.
     * @return AI prompt string.
     */
    private String generateAIPrompt(ITestResult result) {
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "No error message available.";
        String testName = result.getName();
        String className = result.getTestClass().getName();
        String ariaSnapshot = getAriasnapshot();

        return "Test Case Failed: " + testName + "\n" +
                "Class: " + className + "\n" +
                "Error Message: " + errorMessage + "\n" +
                "ARIA Snapshot: " + ariaSnapshot + "\n";
    }

    /**
     * Attaches failure details to the Allure report when a test fails.
     *
     * @param result TestNG test result object.
     */
    private void attachFailureDetailsToAllure(ITestResult result) {
        // Attach AI Prompt
        Allure.addAttachment("AI Prompt", new ByteArrayInputStream(generateAIPrompt(result).getBytes()));

        // Attach Screenshot
        byte[] screenshot = captureScreenshot();
        if (screenshot != null) {
            Allure.addAttachment("Failure Screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.severe("Test failed: " + result.getName());
        attachFailureDetailsToAllure(result);
    }

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }
}
