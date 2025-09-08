 package testcases;

import org.testng.annotations.*;
import org.testng.Assert;
import Utils.DriverFactory;
import Utils.ConfigReader;
import Pages.SearchPage;
import Pages.FiltersPage;
import listeners.ExtentTestNGListener;

@Listeners({ExtentTestNGListener.class})
public class PriceFilterTest {
    private SearchPage search;
    private FiltersPage filters;

    @BeforeClass
    @Parameters({"browser"})
    public void setup(@Optional("chrome") String browser) {
        DriverFactory.initDriver(browser);
        search = new SearchPage();
        filters = new FiltersPage();
    }

    @Test
    public void filterByPriceRange() {
        DriverFactory.getDriver().get(ConfigReader.get("url"));
        search.search("camera");

        boolean applied = filters.applyPrice("$500 - $1000");
        Assert.assertTrue(applied, "Price filter not applied");
    }

    @AfterClass
    public void teardown() {
        DriverFactory.quitDriver();
    }
}
