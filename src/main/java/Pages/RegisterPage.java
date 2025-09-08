package Pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RegisterPage extends BasePage {
	 
    private WebDriver driver;
    private WebDriverWait wait;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        // The timeout has been increased to 30 seconds to handle slow-loading pages.
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 
    }

    public boolean register(String firstName, String lastName, String email, String password) {
        try {
            // Wait for the "Register" button to become visible. The increased timeout should
            // resolve the "TimeoutException".
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("register-button")));

            try {
                WebElement genderMale = wait.until(ExpectedConditions.elementToBeClickable(By.id("gender-male")));
                genderMale.click();
            } catch (Exception ex) {
                WebElement genderFemale = wait.until(ExpectedConditions.elementToBeClickable(By.id("gender-female")));
                genderFemale.click();
            }

            driver.findElement(By.id("FirstName")).sendKeys(firstName);
            driver.findElement(By.id("LastName")).sendKeys(lastName);
            driver.findElement(By.id("Email")).sendKeys(email);
            driver.findElement(By.id("Password")).sendKeys(password);
            driver.findElement(By.id("ConfirmPassword")).sendKeys(password);

            driver.findElement(By.id("register-button")).click();

            WebElement result = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.result"))
            );
            return result.getText().contains("completed");
        } catch (Exception e) {
            System.out.println("[RegisterPage] register failed - " + e.getMessage());
            return false;
        }
    }
}

