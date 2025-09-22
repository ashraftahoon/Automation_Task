package tests;

import io.qameta.allure.*;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.ElementActions;
import utilities.ValidationUtility;

import java.io.IOException;

public class LoginTests extends  TestBase{

    @Test
    @Description("Login with valid user")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("ashraf")
    @Issue("testcase#")
    public void testLoginWithHrUser() throws IOException, ParseException {
        testdata.dataReader("valid_user");
        loginPage.LoginUser(testdata.emails.get(0),testdata.passwords.get(0));
        System.out.println(elementActions.getTextOf(loginPage.welcomeText));
        String expectedWelcomeText = "test";
        ValidationUtility.softAssertEquals(elementActions.getTextOf(loginPage.welcomeText), expectedWelcomeText, "Welcome text does not match!");
    }


}


