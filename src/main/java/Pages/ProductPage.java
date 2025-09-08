package Pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Utils.DriverFactory;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public ProductPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    public String getProductTitle() {
        try {
            By titleBy = By.cssSelector("div.product-name h1, .product-name h1, .product-title");
            WebElement titleEl = wait.until(ExpectedConditions.visibilityOfElementLocated(titleBy));
            return titleEl.getText().trim();
        } catch (Exception e) {
            try { return driver.getTitle(); } catch (Exception ex) { return ""; }
        }
    }

    /**
     * Detect option controls (e.g. RAM, HDD, OS) and select defaults where possible.
     * This method is defensive: it tries radio/select/checkbox options.
     */
    public void selectDefaultOptionsIfRequired() throws InterruptedException {
        try {
            // radio groups / option lists
            List<WebElement> optionGroups = driver.findElements(By.cssSelector(".product-options, .product-essential, .attributes, .product-options-wrapper"));
            if (optionGroups.isEmpty()) {
                // also try presence of known labels
                optionGroups = driver.findElements(By.xpath("//label[contains(text(),'RAM') or contains(text(),'HDD') or contains(text(),'Processor') or contains(text(),'Operating System')]"));
            }

            // pick first radio/checkbox/select in the page if visible
            if (!optionGroups.isEmpty()) {
                // radio inputs
                try {
                    List<WebElement> radios = driver.findElements(By.cssSelector("input[type='radio']"));
                    for (WebElement r : radios) {
                        if (r.isDisplayed() && r.isEnabled()) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", r);
                            // small sleep to allow UI update
                            Thread.sleep(300);
                        }
                    }
                } catch (Exception ignored) {}

                // selects (dropdown)
                try {
                    List<WebElement> selects = driver.findElements(By.cssSelector("select"));
                    for (WebElement s : selects) {
                        if (s.isDisplayed() && s.isEnabled()) {
                            // pick second option if first is placeholder
                            List<WebElement> opts = s.findElements(By.tagName("option"));
                            if (opts.size() > 1) {
                                opts.get(1).click();
                            } else if (!opts.isEmpty()) {
                                opts.get(0).click();
                            }
                        }
                    }
                } catch (Exception ignored) {}

                // checkboxes
                try {
                    List<WebElement> checks = driver.findElements(By.cssSelector("input[type='checkbox']"));
                    for (WebElement c : checks) {
                        if (c.isDisplayed() && c.isEnabled() && !c.isSelected()) {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", c);
                        }
                    }
                } catch (Exception ignored) {}
            }
        } catch (NoSuchElementException ignored) {
        } catch (Exception e) {
            System.err.println("[ProductPage] selectDefaultOptionsIfRequired issue: " + e.getMessage());
        }
    }

    private WebElement safeFindClickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public boolean setQuantity(int qty) {
        try {
            By qtyInput = By.cssSelector("input[id^='product_enteredQuantity'], input.qty");
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(qtyInput));
            input.clear();
            input.sendKeys(String.valueOf(qty));
            return true;
        } catch (Exception e) {
            System.err.println("[ProductPage] setQuantity failed - " + e.getMessage());
            return false;
        }
    }

    public boolean clickAddToCart() {
        try {
            // ensure defaults selected for configurable products
            selectDefaultOptionsIfRequired();

            By addBtn = By.cssSelector("button[id^='add-to-cart-button'], button.add-to-cart, input[type='submit'][value*='Add to cart']");
            WebElement btn = safeFindClickable(addBtn);
            try {
                btn.click();
            } catch (WebDriverException clickEx) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }

            // Wait for either success or validation error text that indicates options missing
            By wishlistOrCartSuccess = By.cssSelector(".bar-notification.success, .notification.success, .message-success, .ajax-cart-qty, .cart-qty");
            wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(wishlistOrCartSuccess),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".cart-qty"))
            ));
            return true;
        } catch (TimeoutException te) {
            System.err.println("[ProductPage] clickAddToCart failed - timed out waiting for success or cart update: " + te.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("[ProductPage] clickAddToCart failed - " + e.getMessage());
            return false;
        }
    }

    public boolean clickAddToWishlist() {
        try {
            selectDefaultOptionsIfRequired();

            By wishBtn = By.cssSelector("button[id^='add-to-wishlist-button'], .add-to-wishlist-button, a.add-to-wishlist");
            WebElement btn = safeFindClickable(wishBtn);
            try {
                btn.click();
            } catch (WebDriverException ex) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            }

            By wishlistSuccess = By.cssSelector(".bar-notification.success, .wishlist-notification, .notification.success, .message-success");
            wait.until(ExpectedConditions.visibilityOfElementLocated(wishlistSuccess));
            return true;
        } catch (TimeoutException te) {
            System.err.println("[ProductPage] clickAddToWishlist failed - timed out waiting for wishlist success: " + te.getMessage());
            // rely on header qty as fallback
            return false;
        } catch (Exception e) {
            System.err.println("[ProductPage] clickAddToWishlist failed - " + e.getMessage());
            return false;
        }
    }

    /**
     * Attempt to close visible notification/toast so it doesn't block clicks.
     */
    public void closeNotificationIfPresent() {
        try {
            List<WebElement> closes = driver.findElements(By.cssSelector(".bar-notification .close, .notification .close, .toast-close"));
            for (WebElement c : closes) {
                if (c.isDisplayed()) {
                    try { c.click(); } catch (Exception ex) { ((JavascriptExecutor) driver).executeScript("arguments[0].click();", c); }
                }
            }
        } catch (Exception ignored) {}
    }

    public void switchToNewestWindow() {
        try {
            String original = driver.getWindowHandle();
            for (String h : driver.getWindowHandles()) {
                if (!h.equals(original)) driver.switchTo().window(h);
            }
        } catch (Exception e) {
            System.err.println("[ProductPage] switchToNewestWindow failed - " + e.getMessage());
        }
    }

    public void closeAndSwitchToFirst() {
        try {
            String first = driver.getWindowHandles().iterator().next();
            driver.close();
            driver.switchTo().window(first);
        } catch (Exception e) {
            System.err.println("[ProductPage] closeAndSwitchToFirst failed - " + e.getMessage());
        }
    }
}



