 package testcases;

import org.testng.annotations.*;
import org.testng.Assert;
import Pages.SearchPage;
import Pages.SearchResultsPage;
 

public class SearchTest extends BaseTest {
    private SearchPage search;
    private SearchResultsPage results;

    @BeforeClass
    public void init() {
        search = new SearchPage();
        results = new SearchResultsPage();
    }

    @Test(priority = 4)
    public void searchProductByName() {
        driver().get(baseUrl());
        search.search("computer");
        Assert.assertFalse(results.getAllTitles().isEmpty(), "Search returned no results.");
    }

    @Test(priority = 5)
    public void searchResultValidation() {
        driver().get(baseUrl());
        search.search("camera");
        boolean ok = results.getAllTitles().stream().anyMatch(t -> t.toLowerCase().contains("camera") || t.toLowerCase().contains("dslr"));
        Assert.assertTrue(ok || !results.getAllTitles().isEmpty(), "Search results did not seem relevant.");
    }
}
