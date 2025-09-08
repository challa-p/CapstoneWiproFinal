 package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    private By email = By.id("Email");
    private By password = By.id("Password");
    private By loginBtn = By.cssSelector("button[type='submit']");

    public void open() {
        driver.get(System.getProperty("baseUrl", "https://demo.nopcommerce.com") + "/login");
    }

    public boolean login(String user, String pass) {
        try {
            // navigate to page explicitly
            open();
            // extended wait (12s) via BasePage wait
            WebElement e = waitForVisible(email);
            e.clear();
            e.sendKeys(user);
            WebElement p = waitForVisible(password);
            p.clear();
            p.sendKeys(pass);
            waitForClickable(loginBtn).click();
            // optional: wait until header indicates logged in (e.g., "My account" or logout link)
            return true;
        } catch (Exception ex) {
            System.err.println("[LoginPage] login failed - " + ex.getMessage());
            return false;
        }
    }
}


