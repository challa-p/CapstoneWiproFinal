package Pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utils.DriverFactory;

public class FiltersPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);

    public FiltersPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    // ----------------------
    // Helpers
    // ----------------------
    private void scrollIntoView(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'center'});", el);
        } catch (Exception ignored) {}
    }

    private void safeClick(WebElement el) throws ElementClickInterceptedException {
        try {
            el.click();
        } catch (WebDriverException e) {
            // fallback to javascript click
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    // wait for product results area to refresh after applying filter
    private boolean waitForResultsRefresh() {
        try {
            // adjust selector to whatever indicates results area on your site
            By resultsContainer = By.cssSelector(".product-grid, .product-list");
            wait.until(ExpectedConditions.visibilityOfElementLocated(resultsContainer));
            // optionally wait a short time to allow JS to finish
            Thread.sleep(500);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ----------------------
    // Public actions
    // ----------------------

    // Apply category filter (by visible label text)
    public boolean applyCategory(String category) {
        try {
            By categoryChk = By.xpath("//label[contains(normalize-space(.),'" + category + "')]/preceding-sibling::input[@type='checkbox']");
            WebElement chk = wait.until(ExpectedConditions.elementToBeClickable(categoryChk));
            scrollIntoView(chk);
            safeClick(chk);

            // Wait for results to reflect filter action, or verify checkbox selected state
            boolean refreshed = waitForResultsRefresh();
            if (!refreshed) {
                // as fallback, verify checkbox is selected
                return chk.isSelected();
            }
            return true;
        } catch (Exception e) {
            System.err.println("[FiltersPage] applyCategory failed - " + e.getMessage());
            return false;
        }
    }

    // Apply price filter (by visible link like "$500 - $1000")
    public boolean applyPrice(String priceRangeText) {
        try {
            // Try exact linkText first, fallback to partial link text
            List<WebElement> candidates = driver.findElements(By.linkText(priceRangeText));
            if (candidates.isEmpty()) {
                candidates = driver.findElements(By.partialLinkText(priceRangeText));
            }
            if (candidates.isEmpty()) {
                // try more flexible xpath (handles extra whitespace)
                By priceLinkXPath = By.xpath("//a[normalize-space()='" + priceRangeText + "']");
                candidates = driver.findElements(priceLinkXPath);
            }
            if (candidates.isEmpty()) {
                System.err.println("[FiltersPage] applyPrice - price link not found for: " + priceRangeText);
                return false;
            }

            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(candidates.get(0)));
            scrollIntoView(link);
            safeClick(link);

            // Verification: either URL contains price param or results refreshed
            boolean refreshed = waitForResultsRefresh();
            if (!refreshed) {
                // fallback: check URL contains an indicator (change to your site's param)
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl != null && currentUrl.toLowerCase().contains("price")) {
                    return true;
                }
                // last resort: check that the clicked link has selected/active class
                try {
                    String cls = link.getAttribute("class");
                    if (cls != null && (cls.contains("selected") || cls.contains("active"))) {
                        return true;
                    }
                } catch (Exception ignored) {}
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("[FiltersPage] applyPrice failed - " + e.getMessage());
            return false;
        }
    }

    // Apply specification filter (like processor type, RAM, etc.)
    public boolean applySpecification(String specText) {
        try {
            By specChk = By.xpath("//label[contains(normalize-space(.),'" + specText + "')]/preceding-sibling::input[@type='checkbox']");
            WebElement chk = wait.until(ExpectedConditions.elementToBeClickable(specChk));
            scrollIntoView(chk);
            safeClick(chk);

            boolean refreshed = waitForResultsRefresh();
            if (!refreshed) {
                return chk.isSelected();
            }
            return true;
        } catch (Exception e) {
            System.err.println("[FiltersPage] applySpecification failed - " + e.getMessage());
            return false;
        }
    }

    // Verify all results contain expected keyword
    public boolean resultsContain(String keyword) {
        try {
            By titlesBy = By.cssSelector(".product-title a, h2.product-title a, .product-item .name a");
            List<WebElement> titles = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(titlesBy));
            if (titles.isEmpty()) {
                System.err.println("[FiltersPage] resultsContain - no product titles found");
                return false;
            }
            for (WebElement t : titles) {
                String txt = t.getText();
                if (txt == null || !txt.toLowerCase().contains(keyword.toLowerCase())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.err.println("[FiltersPage] resultsContain failed - " + e.getMessage());
            return false;
        }
    }
}

