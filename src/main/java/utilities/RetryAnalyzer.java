package utilities;

import configReader.ConfigPropReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class);

    private int retryCount = 0;
    private final int maxRetryCount;
    private final boolean retryFailedTests;

    // Constructor to initialize the RetryAnalyzer1
    public RetryAnalyzer() {
        // Read configuration properties
        ConfigPropReader configPropReader = new ConfigPropReader("src/main/resources/config.properties");
        maxRetryCount = getMaxRetryCount(configPropReader);
        retryFailedTests = isRetryFailedTestsEnabled(configPropReader);

        logger.info("Initialized RetryAnalyzer with maxRetryCount: {} and retryFailedTests: {}", maxRetryCount, retryFailedTests);
    }

    // Retry failed tests
    @Override
    public boolean retry(ITestResult result) {
        if (retryFailedTests && result.getStatus() == ITestResult.FAILURE) {
            logFailedTest(result);

            if (shouldRetry(result)) {
                retryCount++;
                logger.info("Retrying test '{}' (Retry {}/{})", result.getName(), retryCount, maxRetryCount);
                return true;
            } else {
                logger.info("Max retry count reached for test '{}'. Not retrying further.", result.getName());
            }
        } else {
            logger.info("Retrying failed tests is disabled by configuration or test did not fail.");
        }
        return false;
    }

    private boolean shouldRetry(ITestResult result) {
        // Custom retry logic can be added here based on the type of failure
        return retryCount < maxRetryCount;
    }

    // Log failed test details
    private void logFailedTest(ITestResult result) {
        String testName = result.getName();
        Throwable throwable = result.getThrowable();
        String failureReason = throwable != null ? throwable.getMessage() : "No exception";
        String stackTrace = throwable != null ? getStackTraceAsString(throwable) : "No stack trace";
        logger.error("Test Failed: '{}'. Failure Reason: {}\nStack Trace:\n{}", testName, failureReason, stackTrace);
    }

    // Get the stack trace as a string
    private String getStackTraceAsString(Throwable throwable) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            stackTrace.append(element.toString()).append("\n");
        }
        return stackTrace.toString();
    }

    // Get the max retry count from configuration
    private int getMaxRetryCount(ConfigPropReader configPropReader) {
        try {
            return Integer.parseInt(configPropReader.getProperty("maxRetries"));
        } catch (NumberFormatException e) {
            logger.error("Invalid maxRetries value in configuration. Using default value of 1.", e);
            return 1; // Default value
        }
    }

    // Check if retrying failed tests is enabled
    private boolean isRetryFailedTestsEnabled(ConfigPropReader configPropReader) {
        try {
            return Boolean.parseBoolean(configPropReader.getProperty("retryFailedTests"));
        } catch (Exception e) {
            logger.error("Invalid retryFailedTests value in configuration. Using default value of false.", e);
            return false; // Default value
        }
    }
}