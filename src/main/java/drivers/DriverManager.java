package drivers;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    //Creates a new WebDriver instance and sets it in the ThreadLocal.
    public static WebDriver createInstance(){
        WebDriver driver = BrowserFactory.getDriver();
        setDriver(driver);
        return getDriver();
    }

    // Returns the WebDriver instance associated with the current thread.
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            throw new IllegalStateException("WebDriver not initialized. Call setDriver() first.");
        }
        return driverThreadLocal.get();
    }

    public static void setDriver(WebDriver driver) {
        driverThreadLocal.set(driver);
    }

    //Quits the WebDriver instance and removes it from the ThreadLocal.
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}