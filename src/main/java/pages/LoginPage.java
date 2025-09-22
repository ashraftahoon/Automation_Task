package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.ElementActions;
import utilities.WaitUtility;

public class LoginPage {
    // LoginPage class implementation
    // This class will contain methods and elements related to the login page
    // For example, methods to enter username, password, and click login button

    // Elements can be defined using locators like By.id, By.name, etc.

   public WebDriver driver;
   public WaitUtility WaitUtilityobject;
   public ElementActions elementActions;
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.WaitUtilityobject= new WaitUtility();
    }

    private static final By email = By.id("signin-email");
    private static final By password = By.id("signin-password");
    private static final By loginButton = By.xpath("//button[text()='Sign In']");
    public By welcomeText = By.xpath("//li[@data-state='open']");

    public HomePage LoginUser(String userEmail, String userPassword) {
        elementActions.typeText(email , userEmail);
        elementActions.typeText(password, userPassword);
        elementActions.click(loginButton);
        WaitUtilityobject.waitForElementVisible(welcomeText);
        return new HomePage(driver);
    }
}
