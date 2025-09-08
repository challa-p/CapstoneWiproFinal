 package Utils;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    private static WebDriver driver;

    public static synchronized void initDriver(String browser) {
        if (driver != null) return; // already initialized
        if (browser == null || browser.isEmpty()) browser = "chrome";

        switch (browser.toLowerCase()) {
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                opts.addArguments("--start-maximized");
                opts.addArguments("--no-sandbox");
                opts.addArguments("--disable-dev-shm-usage");
                // opts.addArguments("--headless=new"); // optional
                driver = new ChromeDriver(opts);
                break;
        }
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        // Keep implicit wait 0; rely on explicit waits in pages
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static synchronized void quitDriver() {
        try {
            if (driver != null) driver.quit();
        } catch (Exception ignored) {}
        driver = null;
    }

    public static boolean isDriverAlive() {
        try {
            return driver != null && ((RemoteWebDriver) driver).getSessionId() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
