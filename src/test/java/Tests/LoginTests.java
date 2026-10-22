package Tests;

import Base.BaseTest;
import Pages.LoginPage;
import org.testng.annotations.Test;
import utils.DataDriven;

import javax.xml.stream.events.DTD;

import static org.testng.Assert.*;

public class LoginTests extends BaseTest {

    @Test(description = "Verify Successful Login")
    public void verifySuccessfulLogin(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                DataDriven.getString("validUser","username"),
                DataDriven.getString("validUser","password")
        );

        assertTrue(driver.getCurrentUrl().contains("/inventory.html"),
                "User was not redirected to the inventory page. URL: " + driver.getCurrentUrl());
    }

    @Test(description = "Verify Invalid Login")
    public void verifyInvalidLogin(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                DataDriven.getString("invalidUser","username"),
                DataDriven.getString("invalidUser","password")
        );
        String message = loginPage.getErrorMessage();
        assertTrue(message.contains("Username and password do not match"),
                "Unexpected error message: " + message);
    }

    @Test(description = "Verify Login Without Password")
    public void verifyLoginWithoutPassword(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(DataDriven.getString("validUser", "username"),
                ""
        );
        String message = loginPage.getErrorMessage();
        assertTrue(message.contains("Password is required"),
                "Unexpected error message: " + message);
    }
}
