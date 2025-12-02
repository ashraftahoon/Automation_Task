package tests;

import com.github.javafaker.Faker;
import configReader.ConfigPropReader;
import drivers.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.PurchasePage;
import pages.ReservePage;
import utilities.*;
import java.time.Duration;


public class PurchaseFlightTest {
    public BrowserActions browserActions;
    public ConfigPropReader configPropReader;
    public WebDriver driver;
    private HomePage homePageObject;
    private ReservePage reservePageObject;
    private PurchasePage purchasePageObject;

    @BeforeClass
    public void setUp() {
        driver = DriverManager.createInstance();
        browserActions = new BrowserActions();
        homePageObject= new HomePage();
        reservePageObject= new ReservePage();
        configPropReader = new ConfigPropReader("src/main/resources/config.properties");
        String url = configPropReader.getProperty("baseUrl");
        browserActions.navigateToURL(url);
        browserActions.maximizeWindow();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(7));  // waits 7 seconds for elements to be present
    }


    @Test
    public void purchaseEndToEnd(){
        Faker faker = new Faker();
        // Generate random data
        String fullName = faker.name().fullName();
        String address  = faker.address().streetAddress();
        String city     = faker.address().city();
        String state    = faker.address().state();
        String zip      = faker.address().zipCode();
        String creditCardNumber = faker.finance().creditCard().replace("-", "");
        String cardName = faker.name().fullName();
        int month = faker.number().numberBetween(1, 12);   // 1–12
        int year = faker.number().numberBetween(2025, 2035);
        reservePageObject= homePageObject.searchFlights();
        purchasePageObject= reservePageObject.chooseFirstFlight();
        purchasePageObject.purchaseFlight(fullName,address,city,state,zip,creditCardNumber,month,year,cardName);
        String status= purchasePageObject.validateStatus();
        ValidationUtility.softAssertEquals(status,"PendingCapture","Status validation failed");
        boolean isAmountValid= purchasePageObject.validateAmount();
        ValidationUtility.softAssertTrue(isAmountValid,"Amount validation passed");
        ValidationUtility.assertAll();
    }

    @AfterClass
    public void tearDown(){
        DriverManager.quitDriver();
    }

}




