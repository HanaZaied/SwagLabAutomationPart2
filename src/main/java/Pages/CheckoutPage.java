package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private final WebDriverWait wait;


    private final By firstName = By.id("first-name");
    private final By lastName = By.id("last-name");
    private final By postalCode = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By itemTotalLabel = By.cssSelector(".summary_subtotal_label");

    public CheckoutPage(WebDriver driver){
        this.driver=driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterInfo(String fName, String lName,String code){
        driver.findElement(firstName).sendKeys(fName);
        driver.findElement(lastName).sendKeys(lName);
        driver.findElement(postalCode).sendKeys(code);
    }
    public void clickContinue(){
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }
    public double getItemTotal() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(itemTotalLabel)).getText();
        return Double.parseDouble(text.substring(text.indexOf('$') + 1).trim());
    }
    public boolean isFirstNameDisplayed(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(firstName)).isDisplayed();
    }


}
