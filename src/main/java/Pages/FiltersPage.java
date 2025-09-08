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

    public FiltersPage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    By desktopsCheck = By.xpath("//label[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'desktops')]/preceding-sibling::input[1]");

    WebElement chk = wait.until(ExpectedConditions.elementToBeClickable(desktopsCheck));

    // Apply category filter (by visible label text)
    public boolean applyCategory(String category) {
        try {
            By categoryChk = By.xpath("//label[contains(text(),'" + category + "')]/preceding-sibling::input[@type='checkbox']");
            WebElement chk = wait.until(ExpectedConditions.elementToBeClickable(categoryChk));
            chk.click();
            wait.until(ExpectedConditions.stalenessOf(chk));
            return true;
        } catch (Exception e) {
            System.err.println("[FiltersPage] applyCategory failed - " + e.getMessage());
            return false;
        }
    }

    // Apply price filter (by visible link like "$500 - $1000")
    public boolean applyPrice(String priceRangeText) {
        try {
            By priceLink = By.linkText(priceRangeText);
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(priceLink));
            link.click();
            wait.until(ExpectedConditions.stalenessOf(link));
            return true;
        } catch (Exception e) {
            System.err.println("[FiltersPage] applyPrice failed - " + e.getMessage());
            return false;
        }
    }

    // Apply specification filter (like processor type, RAM, etc.)
    public boolean applySpecification(String specText) {
        try {
            By specChk = By.xpath("//label[contains(text(),'" + specText + "')]/preceding-sibling::input[@type='checkbox']");
            WebElement chk = wait.until(ExpectedConditions.elementToBeClickable(specChk));
            chk.click();
            wait.until(ExpectedConditions.stalenessOf(chk));
            return true;
        } catch (Exception e) {
            System.err.println("[FiltersPage] applySpecification failed - " + e.getMessage());
            return false;
        }
    }

    // Verify all results contain expected keyword
    public boolean resultsContain(String keyword) {
        try {
            By titlesBy = By.cssSelector(".product-title a, h2.product-title a");
            List<WebElement> titles = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(titlesBy));
            for (WebElement t : titles) {
                if (!t.getText().toLowerCase().contains(keyword.toLowerCase())) {
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
