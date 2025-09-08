 package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class WishlistPage extends BasePage {
    @SuppressWarnings("unused")
	private By wishlistTable = By.cssSelector("table.wishlist");
    private By productTitles = By.cssSelector("table.wishlist .product-name a, .wishlist .product-name a");
    private By moveToCartBtn = By.cssSelector("button[name='addtocartbutton']");
    private By removeCheckbox = By.cssSelector("table.wishlist input[type='checkbox']");
    private By updateWishlistBtn = By.name("updatecart");

    public boolean moveFirstToCart() {
        try {
            // Select the first product checkbox
            WebElement firstItem = wait.until(ExpectedConditions.elementToBeClickable(removeCheckbox));
            firstItem.click();

            // Click Add to cart button
            WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(moveToCartBtn));
            addBtn.click();

            // Wait for success notification
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".bar-notification.success")));
            return true;
        } catch (Exception e) {
            System.out.println("[WishlistPage] moveFirstToCart failed - " + e.getMessage());
            return false;
        }
    }

    /** ✅ Check if wishlist contains product title */
    public boolean containsProductTitle(String expectedTitle) {
        try {
            List<WebElement> titles = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(productTitles));
            for (WebElement title : titles) {
                if (title.getText().trim().equalsIgnoreCase(expectedTitle.trim())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println("[WishlistPage] containsProductTitle failed - " + e.getMessage());
            return false;
        }
    }

    /** ✅ Remove first product from wishlist */
    public void removeFirstItem() {
        try {
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(removeCheckbox));
            checkbox.click();

            WebElement updateBtn = wait.until(ExpectedConditions.elementToBeClickable(updateWishlistBtn));
            updateBtn.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".bar-notification.success")));
        } catch (Exception e) {
            System.out.println("[WishlistPage] removeFirstItem failed - " + e.getMessage());
        }
    }
}



