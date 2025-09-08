 package testcases;

import org.testng.Assert;
import org.testng.annotations.*;
import Utils.DriverFactory;
import Utils.ConfigReader;
import Pages.SearchPage;
import Pages.FiltersPage;
import listeners.ExtentTestNGListener;

@Listeners({ExtentTestNGListener.class})
public class FilterTest {

    private SearchPage search;
    private FiltersPage filters;

    @BeforeClass
    @Parameters({"browser"})
    public void setup(@Optional("chrome") String browser) {
        DriverFactory.initDriver(browser);
        search = new SearchPage();
        filters = new FiltersPage();
    }

    @Test(priority = 1)
    public void filterByCategory() {
        DriverFactory.getDriver().get(ConfigReader.get("url"));
        search.search("computer");

        boolean applied = filters.applyCategory("Desktops");
        Assert.assertTrue(applied, "Category filter not applied");

        boolean validResults = filters.resultsContain("desktop");
        Assert.assertTrue(validResults, "Filtered results do not contain expected keyword");
    }

    @Test(priority = 2)
    public void filterByPrice() {
        DriverFactory.getDriver().get(ConfigReader.get("url"));
        search.search("camera");

        boolean applied = filters.applyPrice("$500 - $1000");
        Assert.assertTrue(applied, "Price filter not applied");
    }

    @Test(priority = 3)
    public void filterBySpecification() {
        DriverFactory.getDriver().get(ConfigReader.get("url"));
        search.search("notebook");

        boolean applied = filters.applySpecification("8GB RAM");
        Assert.assertTrue(applied, "Specification filter not applied");
    }

    @AfterClass
    public void teardown() {
        DriverFactory.quitDriver();
    }
}

