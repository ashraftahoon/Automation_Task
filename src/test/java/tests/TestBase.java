package tests;

import configReader.ConfigPropReader;
import drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.*;
import java.time.Duration;

public class TestBase {
    public BrowserActions browserActions;
    public ConfigPropReader configPropReader;
    public ElementActions elementActions;
    public WaitUtility wait;
    public WebDriver driver;
    public LoginPage loginPage;
    public Scrolling scrolling;
    public JsonDataReader testdata;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.createInstance();
        browserActions = new BrowserActions();
        loginPage = new LoginPage(driver);
        configPropReader = new ConfigPropReader("src/main/resources/config.properties");
        String url = configPropReader.getProperty("baseUrl");
        browserActions.navigateToURL(url);
        browserActions.maximizeWindow();
        testdata = new JsonDataReader();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));  // waits 10 seconds for elements to be present
    }

    @AfterClass
    public void tearDown() {

    }

}




