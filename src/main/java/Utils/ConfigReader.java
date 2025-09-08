 package Utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties prop = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            prop.load(fis);
        } catch (Exception e) {
            System.err.println("[ConfigReader] Could not load config.properties: " + e.getMessage());
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }
}
