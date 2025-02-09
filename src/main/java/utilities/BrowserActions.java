package utilities;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserActions {

    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BrowserActions.class);

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
        logger.info("Initialized BrowserActions with WebDriver: {}", driver);
    }

    public void navigateToURL(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
        logger.debug("Navigation complete. Current URL: {}", driver.getCurrentUrl());
    }

    public void maximizeWindow() {
        logger.info("Maximizing browser window.");
        driver.manage().window().maximize();
        logger.debug("Browser window maximized.");
    }

    public void refreshPage() {
        logger.info("Refreshing the page.");
        driver.navigate().refresh();
        logger.debug("Page refreshed.");
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

    public void closeBrowser() {
        logger.info("Closing the browser.");
        driver.quit();
        logger.debug("Browser closed successfully.");
    }
}
