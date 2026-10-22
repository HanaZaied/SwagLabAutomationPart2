package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class InventoryPage {
    private WebDriver driver;
    private final WebDriverWait wait;

    private final By cartIconLoc = By.className("shopping_cart_link");
    private final By inventoryItemLoc = By.cssSelector("div[class=\"inventory_item\"]");

    public final By LINKEDIN = By.cssSelector("[data-test='social-linkedin']");
    public final By FACEBOOK = By.cssSelector("[data-test='social-facebook']");
    public final By TWITTER = By.cssSelector("[data-test='social-x']");

    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    private final By itemName = By.cssSelector(".inventory_item_name");
    private final By itemPrice = By.cssSelector(".inventory_item_price");

    private final By cartBadgeLoc = By.cssSelector(".shopping_cart_badge");


    public InventoryPage(WebDriver driver){
        this.driver=driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
    public boolean isCartIconDisplayed(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartIconLoc)).isDisplayed();
    }
    public int getProductsCount(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryItemLoc));
        return driver.findElements(inventoryItemLoc).size();
    }

    public String openSocialLinkAndGetUrl(By locator,String expectedUrlPart){
        String original = driver.getWindowHandle();
        Set<String> before = driver.getWindowHandles();

        // Scroll so the icon is in the middle of the screen, then wait until it is clickable
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();

        // Wait for a NEW window (compare with the windows we had before the click)
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(d -> d.getWindowHandles().size() > before.size());

        String newHandle = null;
        for (String handle : driver.getWindowHandles()) {
            if (!before.contains(handle)) {
                newHandle = handle;
            }
        }
        driver.switchTo().window(newHandle);

        try {
            wait.until(ExpectedConditions.urlContains(expectedUrlPart));
        } catch (TimeoutException ignored) {
        }
        String url = driver.getCurrentUrl();
        driver.close();
        driver.switchTo().window(original);
        return url;
    }
    public String clickOnLinkedInAndGetURL(){
        return openSocialLinkAndGetUrl(LINKEDIN,"linkedin");
    }
    public String clickOnFaceBookAndGetURL(){
        return openSocialLinkAndGetUrl(FACEBOOK,"facebook");
    }
    public String  clickOnXAndGetURL(){
        return openSocialLinkAndGetUrl(TWITTER,"x.com");
    }

    public CartPage openCart(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartIconLoc)).click();
        return new CartPage(driver);
    }
    public void addToCart(String productName){
        By addToCartButton = By.cssSelector("[data-test='add-to-cart-" + WebElementHelper.productWithDashe(productName) + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton)).click();
    }
    public String getProductButtonText(String productName) {
        String slug = WebElementHelper.productWithDashe(productName);
        By button = By.cssSelector("[data-test='add-to-cart-" + slug + "'], [data-test='remove-" + slug + "']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(button)).getText().trim();
    }

    public double reedProductPrice(String productName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryItemLoc));
        for (WebElement item : driver.findElements(inventoryItemLoc)) {
            String name = item.findElement(itemName).getText().trim();
            if (name.equals(productName)) {
                String priceText = item.findElement(itemPrice).getText();
                return Double.parseDouble(priceText.replace("$", "").trim());
            }
        }
        throw new IllegalArgumentException("Product not found on inventory page: " + productName);
    }

    public int getCartBadgeCount(){
        try {
            String text = driver.findElement(cartBadgeLoc).getText();
            return Integer.parseInt(text.trim());
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(menuButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

}
