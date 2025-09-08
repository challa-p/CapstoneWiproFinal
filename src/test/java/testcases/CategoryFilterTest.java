 package testcases;

import org.testng.annotations.*;
import org.testng.Assert;
import Utils.DriverFactory;
import Utils.ConfigReader;
import Pages.SearchPage;
import Pages.FiltersPage;
import listeners.ExtentTestNGListener;

@Listeners({ExtentTestNGListener.class})
public class CategoryFilterTest {
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
    public void filterByComputersCategory() {
        DriverFactory.getDriver().get(ConfigReader.get("url"));
        search.search("computer");

        boolean applied = filters.applyCategory("Desktops");
        Assert.assertTrue(applied, "Category filter not applied");
    }

    @AfterClass
    public void teardown() {
        DriverFactory.quitDriver();
    }
}

