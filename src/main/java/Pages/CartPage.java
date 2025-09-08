 package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {
    private By firstQty = By.cssSelector("table.cart input.qty-input, .cart input.qty-input, .cart .qty");

    public int getQuantityForFirstItem() {
        try {
            WebElement q = waitForVisible(firstQty);
            String val = q.getAttribute("value");
            if (val == null) val = q.getText();
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}


