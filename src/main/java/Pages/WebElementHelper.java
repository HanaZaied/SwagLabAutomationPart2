package Pages;

public class WebElementHelper {

    static String productWithDashe(String productName) {

        return productName.toLowerCase().trim().replace(" ", "-");
    }

}
