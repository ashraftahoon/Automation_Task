package utilities;

import drivers.DriverManager;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserActions {

    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BrowserActions.class);

    public BrowserActions() {
        this.driver = DriverManager.getDriver();
        logger.info("Initialized BrowserActions with WebDriver: {}", driver);
    }

    // Browser actions
    public BrowserActions navigateToURL(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
        logger.debug("Navigation complete. Current URL: {}", driver.getCurrentUrl());
        return this;
    }

    //  Maximize the browser window
    public void maximizeWindow() {
        logger.info("Maximizing browser window");
        driver.manage().window().maximize();
        logger.debug("Browser window maximized.");
    }


    // Refresh the page
    public BrowserActions refreshPage() {
        logger.info("Refreshing the page.");
        driver.navigate().refresh();
        logger.debug("Page refreshed.");
        return this;
    }
    // Navigate forward
    public BrowserActions navigateForward() {
        driver.navigate().forward();
        return this;
    }

    //
    public BrowserActions navigateBack() {
        driver.navigate().back();
        return this;
    }

    /***************************  Cookies  ***********************************/

    public BrowserActions addCookie(Cookie cookie) {
        driver.manage().addCookie(cookie);
        return this;
    }


    public BrowserActions deleteCookie(Cookie cookie) {
        driver.manage().deleteCookie(cookie);
        return this;
    }

    public BrowserActions deleteCookieWithName(String cookieName) {
        driver.manage().deleteCookieNamed(cookieName);
        return this;
    }

    public BrowserActions deleteAllCookies() {
        driver.manage().deleteAllCookies();
        return this;
    }
    public String getPagTitle() {
        String title = driver.getTitle();
        logger.info("Retrieved page title: {}", title);
        return title;
    }

    public String getCurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        logger.info("Retrieved current URL: {}", currentUrl);
        return currentUrl;
    }

    public BrowserActions closeBrowser() {
        driver.quit();
        logger.info("Browser closed successfully.");
        return this;
    }
}
