 package Pages;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Utils.DriverFactory;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
    }

    protected WebElement waitForVisible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }
    protected WebElement waitForClickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }
    protected void clickJS(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
}
