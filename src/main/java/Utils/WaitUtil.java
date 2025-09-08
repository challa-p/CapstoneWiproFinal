 package Utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.function.Supplier;

public class WaitUtil {
    public static void waitForVisible(WebDriver driver, By locator, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForClickable(WebDriver driver, By locator, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
            .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitFor(Supplier<Boolean> condition, int seconds) {
        long end = System.currentTimeMillis() + seconds * 1000L;
        while (System.currentTimeMillis() < end) {
            try { if (condition.get()) return; } catch (Exception ignored) {}
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }
        throw new TimeoutException("Condition not met within " + seconds + "s");
    }
}
