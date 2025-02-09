package drivers;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverManager {

    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Creates a new WebDriver instance, stores it in ThreadLocal, and returns it.
     * @return The newly created WebDriver instance.
     */
    public static WebDriver createInstance() {
        try {
            WebDriver driver = BrowserFactory.getDriver();
            setDriver(driver);
            logger.info("WebDriver instance created and set in ThreadLocal for thread: {}",
                    Thread.currentThread().getName());
            return getDriver();
        } catch (Exception e) {
            logger.error("Error creating WebDriver instance: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create WebDriver instance", e);
        }
    }

    /**
     * Retrieves the WebDriver instance associated with the current thread.
     * @return The WebDriver instance.
     * @throws IllegalStateException if the driver is not initialized.
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            logger.error("WebDriver not initialized. Call setDriver() first for thread: {}",
                    Thread.currentThread().getName());
            throw new IllegalStateException("WebDriver not initialized. Call setDriver() first.");
        }
        logger.debug("Retrieving WebDriver instance for thread: {}", Thread.currentThread().getName());
        return driverThreadLocal.get();
    }

    /**
     * Sets the WebDriver instance for the current thread.
     * @param driver The WebDriver instance to be stored.
     */
    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
        logger.info("WebDriver instance set in ThreadLocal for thread: {}", Thread.currentThread().getName());
    }

    /**
     * Quits the WebDriver instance and removes it from ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver instance quit successfully for thread: {}",
                        Thread.currentThread().getName());
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver: {}", e.getMessage(), e);
            } finally {
                driverThreadLocal.remove();
                logger.debug("WebDriver instance removed from ThreadLocal for thread: {}",
                        Thread.currentThread().getName());
            }
        } else {
            logger.warn("No WebDriver instance found for quitting in thread: {}",
                    Thread.currentThread().getName());
        }
    }
}
