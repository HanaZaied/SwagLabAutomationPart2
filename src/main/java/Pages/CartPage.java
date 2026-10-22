package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CartPage {
    private WebDriver driver;
    private final WebDriverWait wait;

    private final By cartItems = By.cssSelector(".cart_quantity");
    private final By cartList = By.cssSelector(".cart_list");
    private final By cartItemNames = By.cssSelector(".cart_item .inventory_item_name");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver){
        this.driver=driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/cart.html"));
    }

    public int getItemsCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartList));
        return driver.findElements(cartItems).size();
    }
    public List<String> getItemNames(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartList));
        List<String> names = new ArrayList<>();
        for (WebElement element : driver.findElements(cartItemNames)) {
            names.add(element.getText().trim());
        }
        return names;
    }
    public void removeItem(String productName) {
        By removeButton = By.cssSelector("[data-test='remove-" + WebElementHelper.productWithDashe(productName) + "']");
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
    }
    public InventoryPage continueShopping() {
        wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
        return new InventoryPage(driver);
    }
    public CheckoutPage clickCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
        return new CheckoutPage(driver);
    }
}
