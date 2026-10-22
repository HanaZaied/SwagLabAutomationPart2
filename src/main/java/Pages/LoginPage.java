package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage {
    private WebDriver driver;
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.cssSelector("form[aria-label=\"Login\"] #login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");


    public LoginPage(WebDriver driver){
        this.driver=driver;
    }

    public void enterUserName(String username){
        driver.findElement(usernameField).sendKeys(username);
    }
    public void enterPassword(String password){
        driver.findElement(passwordField).sendKeys(password);
    }
    public void clickLoginButton(){
        driver.findElement(loginButton).click();
    }
    public void login(String username,String password){
        enterUserName(username);
        enterPassword(password);
        clickLoginButton();
    }
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public boolean isLoginButtonDisplayed() {
        return driver.findElement(loginButton).isDisplayed();
    }

}
