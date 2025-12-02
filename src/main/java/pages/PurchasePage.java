package pages;

import drivers.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utilities.ElementActions;

public class PurchasePage {

    private WebDriver driver;
    private ElementActions elementActions;

    // Constructor
    public PurchasePage () {
        this.driver = DriverManager.getDriver();
        this.elementActions= new ElementActions();
    }

    // locate elements
    private static final By firstNameField = By.id("inputName");
    private static final By addressField = By.id("address");
    private static final By cityField = By.id("city");
    private static final By stateField = By.id("state");
    private static final By zipCodeField = By.id("zipCode");
    private static final By cardTypeDropdown = By.id("cardType");
    private static final By creditCardNumber = By.id("creditCardNumber");
    private static final By creditCardMonthField = By.id("creditCardMonth");
    private static final By creditCardYearField = By.id("creditCardYear");
    private static final By nameOnCardField = By.id("nameOnCard");
    private static final By purchaseFlightButton = By.cssSelector("input.btn.btn-primary");
    private static final By statusMessage =By.xpath("//td[text()='Status']/following-sibling::td");
    private static final By amount  =By.xpath("//td[text()='Amount']/following-sibling::td");

    // method for purchase flight
    public PurchasePage purchaseFlight(String fname, String address, String city, String state,String zipCode,
                                       String cardNum,    int month,int year,  String nameOnCard){
        elementActions.typeText(firstNameField,fname);
        elementActions.typeText(addressField,address);
        elementActions.typeText(cityField,city);
        elementActions.typeText(stateField,state);
        elementActions.typeText(zipCodeField,zipCode);
        elementActions.selectDropdownByIndex(cardTypeDropdown,1);
        elementActions.typeText(creditCardNumber,cardNum);
        elementActions.typeText(creditCardMonthField, String.valueOf(month));
        elementActions.typeText(creditCardYearField, String.valueOf(year));
        elementActions.typeText(nameOnCardField,nameOnCard);
        elementActions.click(purchaseFlightButton);
        return this;
    }

    // method return status value
    public String validateStatus(){
        return elementActions.getTextOf(statusMessage);
    }

    // method to return amount value
    public boolean validateAmount(){
        String amountText= elementActions.getTextOf(amount);
        // Extract number from "555 USD"
        int amount = Integer.parseInt(amountText.split(" ")[0]);
        System.out.println(amount);
        return amount > 100;
    }






}
