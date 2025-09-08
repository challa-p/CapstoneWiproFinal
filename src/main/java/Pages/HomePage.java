 package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import Utils.DriverFactory;
import Utils.WaitUtil;

public class HomePage {
    private WebDriver driver = DriverFactory.getDriver();
    private By searchBox = By.id("small-searchterms");
    private By searchButton = By.cssSelector("form[action='/search'] button[type='submit']");
    private By loginLink = By.cssSelector("a.ico-login");

    public void goToHome(String url) {
        driver.get(url);
    }

    public void clickLogin() {
        driver.findElement(loginLink).click();
    }

    public void search(String text) {
        WaitUtil.waitForVisible(driver, searchBox, 10);
        driver.findElement(searchBox).clear();
        driver.findElement(searchBox).sendKeys(text);
        driver.findElement(searchButton).click();
    }
}
