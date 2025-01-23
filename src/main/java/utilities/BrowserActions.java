package utilities;

import org.openqa.selenium.WebDriver;

public class BrowserActions {

    private WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToURL(String url) {
        driver.get(url);
    }

    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void closeBrowser() {
        driver.quit();
    }
}
