 package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HeaderComponent extends BasePage {
    private By wishlistQty = By.cssSelector(".wishlist-qty, .wishlist-qty span");
    private By cartQty = By.cssSelector(".cart-qty, .cart-qty span");
    private By wishlistLink = By.cssSelector("a[href*='wishlist']");
    private By cartLink = By.cssSelector("a[href*='cart']");

    public int getWishlistQty() {
        try {
            WebElement el = driver.findElement(wishlistQty);
            String txt = el.getText().replaceAll("[^0-9]", "");
            return txt.isEmpty() ? 0 : Integer.parseInt(txt);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getCartQty() {
        try {
            WebElement el = driver.findElement(cartQty);
            String txt = el.getText().replaceAll("[^0-9]", "");
            return txt.isEmpty() ? 0 : Integer.parseInt(txt);
        } catch (Exception e) {
            return 0;
        }
    }

    public void clickWishlist() { try { waitForClickable(wishlistLink).click(); } catch (Exception ignored) {} }
    public void clickCart() { try { waitForClickable(cartLink).click(); } catch (Exception ignored) {} }
}
