package utilities;

import configReader.ConfigPropReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private final int maxRetryCount;
    private final boolean retryFailedTests;

    public RetryAnalyzer() {
        // Read maxRetries from the properties file
        ConfigPropReader configPropReader = new ConfigPropReader("src/main/resources/config.properties");
        maxRetryCount = Integer.parseInt(configPropReader.getProperty("maxRetries"));
        retryFailedTests = Boolean.parseBoolean(configPropReader.getProperty("retryFailedTests"));

    }

    @Override
    public boolean retry(ITestResult result) {
        // Check if retrying is enabled before applying retry logic
        if (retryFailedTests) {
            // Log failed test case details if the test failed
            if (result.getStatus() == ITestResult.FAILURE) {
                logFailedTest(result);
            }

            // Retry the test if retry count is less than max retry count
            if (retryCount < maxRetryCount) {
                retryCount++;
                return true; // Retry the test
            }
        }
        return false; // Don't retry if retryFailedTests is false or after max retries
    }

    private void logFailedTest(ITestResult result) {
        String testName = result.getName();
        String failureReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "No exception";
        // Log the failure details to the console
        System.out.println("Test Failed: " + testName);
        System.out.println("Failure Reason: " + failureReason);
    }
}
