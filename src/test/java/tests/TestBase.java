package tests;

import configReader.ConfigPropReader;
import drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.*;
import java.time.Duration;

public class TestBase {
    public BrowserActions browserActions;
    public ConfigPropReader configPropReader;
    public ElementActions elementActions;
    public WaitUtility wait;
    public WebDriver driver;
    public Scrolling scrolling;
    @Test
    public void setUp() {

        driver = DriverManager.createInstance();
        browserActions = new BrowserActions(driver);
        configPropReader = new ConfigPropReader("src/main/resources/config.properties");
        String url = configPropReader.getProperty("baseUrl");
        browserActions.navigateToURL(url);
        browserActions.maximizeWindow();
        scrolling= new Scrolling(driver);
        elementActions = new ElementActions(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));  // waits 10 seconds for elements to be present
        elementActions.typeText(By.name("username"), "Admin");
        elementActions.typeText(By.name("password"), "admin123");
        elementActions.click(By.cssSelector(".orangehrm-login-button"));
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


