package testcases;
import org.testng.annotations.*;
import Utils.DriverFactory;
import Utils.ConfigReader;
import org.openqa.selenium.WebDriver;

public class BaseTest {
    @BeforeSuite(alwaysRun = true)
    @Parameters({"browser"})
    public void beforeSuite(@Optional String browser) {
        if (browser == null) browser = ConfigReader.get("browser");
        DriverFactory.initDriver(browser);
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        DriverFactory.quitDriver();
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }

    protected String baseUrl() {
        String u = ConfigReader.get("url");
        return u == null ? "https://demo.nopcommerce.com" : u;
    }
}

