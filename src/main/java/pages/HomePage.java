package pages;

import drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.ElementActions;

public class HomePage {

    private WebDriver driver;
    private ElementActions elementActions;

    // Constructor
    public HomePage() {
        this.driver = DriverManager.getDriver();
        this.elementActions= new ElementActions();
    }

    // locators for departure and destination city dropdowns and find flights button
    private static final By depCityDropdown = By.name("fromPort");
    private static final By destCityDropdown = By.name("toPort");
    private static final By findFlightsButton =By.cssSelector("input.btn.btn-primary[value='Find Flights']");


    // Search for flights
    public ReservePage searchFlights() {
        elementActions.selectDropdownByIndex(depCityDropdown,2);
        elementActions.selectDropdownByIndex(destCityDropdown,3);
        elementActions.click(findFlightsButton);
        return new ReservePage();
    }


}
