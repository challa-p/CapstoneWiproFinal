 package Utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    // sanitize filename by removing/replace problematic chars
    private static String sanitize(String name) {
        if (name == null || name.isBlank()) name = "screenshot";
        // replace any character not allowed in filenames with underscore
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static String takeScreenshot(WebDriver driver, String name) {
        try {
            if (driver == null) return null;
            if (!(driver instanceof TakesScreenshot)) return null;

            String safeName = sanitize(name);
            // add timestamp + uuid for uniqueness
            String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).replaceAll("[:]", "-");
            String unique = UUID.randomUUID().toString().substring(0, 8);
            String filename = String.format("%s_%s_%s.png", safeName, timestamp, unique);

            Path dest = Path.of("reports", "screenshots", filename);
            Files.createDirectories(dest.getParent());

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);

            // return absolute path for convenience
            return dest.toAbsolutePath().toString();
        } catch (Exception e) {
            // print stack trace for debugging; replace with logger if you have one
            System.err.println("Screenshot failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

