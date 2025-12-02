package pages;

import drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.ElementActions;

public class ReservePage {

    private WebDriver driver;
    private ElementActions elementActions;

    // Constructor
    public ReservePage() {
        this.driver = DriverManager.getDriver();
        this.elementActions= new ElementActions();
    }

    // locators for first flight button
    private static final By firstFlightButton= By.xpath("//table[@class='table']/tbody/tr[1]//input[@value='Choose This Flight']");

    // Choose the first available flight
    public PurchasePage chooseFirstFlight() {
        elementActions.click(firstFlightButton);
        return new PurchasePage();
    }
}
