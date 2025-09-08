 package testcases;

import org.testng.annotations.*;
import org.testng.Assert;
import Pages.SearchPage;
import Pages.SearchResultsPage;
import Pages.ProductPage;
import Pages.HeaderComponent;
import Pages.CartPage;
 

public class ProductAndCartTest extends BaseTest {
    private SearchPage search;
    private SearchResultsPage results;
    private ProductPage product;
    private HeaderComponent header;
    private CartPage cart;

    @BeforeClass
    public void init() {
        search = new SearchPage();
        results = new SearchResultsPage();
        product = new ProductPage();
        header = new HeaderComponent();
        cart = new CartPage();
    }

    @Test(priority = 6)
    public void addToCartAndVerifyHeader() throws InterruptedException {
        driver().get(baseUrl());
        search.search("computer");
        results.clickFirstSelectableProduct();
        product.selectDefaultOptionsIfRequired();
        product.setQuantity(2);
        boolean added = product.clickAddToCart();
        Assert.assertTrue(added, "Add to cart returned false.");
        int qty = header.getCartQty();
        Assert.assertTrue(qty >= 1, "Header cart qty didn't update.");
    }

    @Test(priority = 7)
    public void viewCartAndVerifyQuantity() {
        header.clickCart();
        int qtyInCart = cart.getQuantityForFirstItem();
        Assert.assertEquals(qtyInCart, 2, "Cart quantity mismatch.");
    }
}
