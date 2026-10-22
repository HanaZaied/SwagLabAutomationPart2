package Tests;

import Base.BaseTest;
import Pages.InventoryPage;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class InventoryTests extends BaseTest {
    @Test(description = "Verify Inventory Page Elements After Login")
    public void verifyInventoryPageElements(){
        InventoryPage inventoryPage  = loginAsValidUser();

        assertEquals(inventoryPage.getPageTitle(),"Swag Labs",
                "Page title is wrong");
        assertTrue(inventoryPage.isCartIconDisplayed(),"Cart icon is not displayed");
        assertEquals(inventoryPage.getProductsCount(),6,
                "Number of products is wrong");
    }
}
