package Utils;
 

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtil {
    public static String takeScreenshot(WebDriver driver, String name) {
        try {
            if (driver == null) return null;
            if (!(driver instanceof TakesScreenshot)) return null;
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path dest = Path.of("reports", "screenshots", name + ".png");
            Files.createDirectories(dest.getParent());
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            return dest.toString();
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
            return null;
        }
    }
}

