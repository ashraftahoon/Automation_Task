package tests;

import configReader.ConfigPropReader;
import factory.WebDriverFactory;
import io.qameta.allure.Attachment;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utilities.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestBase {
    public BrowserActions browserActions;
    public ConfigPropReader configPropReader;
    public ElementActions elementActions;
    public WaitUtility wait;
    public WebDriver driver;

    @Test
    public void setUp() {
        driver = WebDriverFactory.getDriver();
        browserActions = new BrowserActions(driver);
        configPropReader = new ConfigPropReader("src/main/resources/config.properties");
        String url = configPropReader.getProperty("baseUrl");
        TestFailureHandler.setWebDriver(driver); // dEnsure WebDriver is set before any test method
        wait = new WaitUtility(driver);
        wait.setImplicitWait();
        browserActions.navigateToURL(url);
        browserActions.maximizeWindow();
        elementActions = new ElementActions(driver);
        elementActions.typeText(driver.findElement(By.name("username")), "Admin");
        elementActions.typeText(driver.findElement(By.name("password")), "admin123");
        elementActions.click(driver.findElement(By.cssSelector(".orangehrm-login-button")));
        Assert.assertTrue(driver.findElement(By.cssSelector(".oxd-topbar-body-nav")).getText().equals("Dashboard"), "Login0 failed. User is not on the Dashboard page.");

    }
}

//    public static void main(String[] args) {
//        JsonDataReader jsonDataReader = new JsonDataReader();
//
//        try {
//            String filePath = "src/main/resources/testdata.json"; // Path to your JSON file
//            jsonDataReader.dataReader(filePath);
//
//            // Get the parsed JSON data as a map
//            Map<String, Object> dataMap = jsonDataReader.getJsonDataMap();
//
//            // Access the root-level data
//            if (dataMap.containsKey("root")) {00
//                Map<String, Object> root = (Map<String, Object>) dataMap.get("root");
//
//                // Get employee's name
//                String employeeName = (String) root.get("name");
//                System.out.println("Employee Name: " + employeeName);
//
//                // Get project names
//                List<Map<String, Object>> projects = (List<Map<String, Object>>) root.get("projects");
//                if (projects != null) {
//                    System.out.println("Project Names:");
//                    for (Map<String, Object> project : projects) {
//                        String projectName = (String) project.get("name");
//                        System.out.println("- " + projectName);
//                    }
//                } else {
//                    System.out.println("No projects found!");
//                }
//            } else {
//                System.out.println("'root' field not found!");
//            }
//
//        } catch (IOException | ParseException e) {
//            e.printStackTrace();
//        }


//    @AfterMethod
//    public void checkScreenshot(ITestResult result) {
//        if (result.getStatus() == ITestResult.FAILURE) {
//            // Capture screenshot and attach to Allure report
//            attachScreenshot();
//        }
//    }
//
//
//    @Attachment(value = "Screenshot", type = "image/png")
//    public byte[] attachScreenshot() {
//        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
//    }


