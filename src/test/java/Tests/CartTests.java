package Tests;

import Base.BaseTest;
import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.InventoryPage;
import Pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import utils.DataDriven;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class CartTests extends BaseTest {
    private final List<String> cartProducts = DataDriven.getList("cartProducts");

    @Test(description = "Verify Social Links")
    public void testSocialLinks(){
        InventoryPage inventoryPage  = loginAsValidUser();
        assertTrue(inventoryPage.clickOnLinkedInAndGetURL().contains("linkedin"),
                "LinkedIn URL is wrong");
        assertTrue(inventoryPage.clickOnFaceBookAndGetURL().contains("facebook"),
                "facebook URL is wrong");
        assertTrue(inventoryPage.clickOnXAndGetURL().contains("x.com"),
                "x.com URL is wrong");
    }
    @Test(description = "Verify Cart Is Empty")
    public void testCartIsEmpty(){
        InventoryPage inventoryPage = loginAsValidUser();
        CartPage cart =inventoryPage.openCart();
        assertEquals(cart.getItemsCount(),0,
                "Cart should be empty");
    }

    @Test(description = "Add 3 specific products (data-driven) and verify order")
    public void testAddThreeProducts(){
        InventoryPage inventoryPage = loginAsValidUser();

        for (String product : cartProducts) {
            inventoryPage.addToCart(product);
        }
        CartPage cartPage = inventoryPage.openCart();
        assertEquals(cartPage.getItemNames(), cartProducts,
                "Cart items their order do not match the products that were added");
    }

    @Test(description = "Remove one product from the cart")
    public void testRemoveOneProduct(){
        InventoryPage inventoryPage = loginAsValidUser();
        for (String product : cartProducts) {
            inventoryPage.addToCart(product);
        }

        String removed = cartProducts.get(1);

        CartPage cart =inventoryPage.openCart();
        cart.removeItem(removed);
        inventoryPage = cart.continueShopping();

        assertEquals(inventoryPage.getProductButtonText(removed),"Add to cart",
                "Removed product should show 'Add to cart'");
        for(String product : cartProducts){
            if(!product.equals(removed)){
                assertEquals(inventoryPage.getProductButtonText(product),"Remove",
                        product + " should still show 'Remove'");
            }
        }

    }
    @Test(description = "Verify cart total price")
    public void testCartTotalPrice(){
        InventoryPage inventoryPage = loginAsValidUser();

        double expexctedTotal = 0;
        for(String product : cartProducts){
            expexctedTotal +=inventoryPage.reedProductPrice(product);
        }

        for (String product : cartProducts) {
            inventoryPage.addToCart(product);
        }

        CartPage cart =inventoryPage.openCart();
        CheckoutPage checkoutPage = cart.clickCheckout();
        checkoutPage.enterInfo(
                DataDriven.getString("checkoutInfo","firstName"),
                DataDriven.getString("checkoutInfo","lastName"),
                DataDriven.getString("checkoutInfo","postalCode")
        );
        checkoutPage.clickContinue();
        assertEquals(checkoutPage.getItemTotal(),expexctedTotal,
                "Item total at checkout does not match the sum of the inventory prices");
    }
    @Test(description = "Checkout With an Empty Cart")
    public void testCheckoutWithEmptyCart(){
        InventoryPage inventoryPage = loginAsValidUser();
        assertEquals(inventoryPage.getCartBadgeCount(),0,
                "Cart should be empty");

        CartPage cart =inventoryPage.openCart();
        assertEquals(cart.getItemsCount(),0,
                "Cart should be empty");

        CheckoutPage checkoutPage =cart.clickCheckout();

        assertTrue(checkoutPage.isFirstNameDisplayed(),
                "The checkout form should be displayed");
    }
    @Test(description = "Cart state after logout/login")
    public void testCartStateAfterLogoutLogin(){
        InventoryPage inventoryPage = loginAsValidUser();
        List<String> added = new ArrayList<>(cartProducts);
        for (String product : cartProducts) {
            inventoryPage.addToCart(product);
        }

        inventoryPage.logout();
        assertTrue(new LoginPage(driver).isLoginButtonDisplayed(),
                "Logout did not return to login page");
        inventoryPage = loginAsValidUser();
        CartPage cartPage = inventoryPage.openCart();

        assertEquals(cartPage.getItemNames(), added,
                "Cart items were expected to persist after logout/login");
    }

}
