 package Pages;

import org.openqa.selenium.By;

public class SearchPage extends BasePage {
    private By searchBox = By.id("small-searchterms");
    private By searchBtn = By.cssSelector("button[type='submit']");

    public void search(String term) {
        try {
            waitForVisible(searchBox).clear();
            waitForVisible(searchBox).sendKeys(term);
            waitForClickable(searchBtn).click();
        } catch (Exception e) {
            System.err.println("[SearchPage] search failed - " + e.getMessage());
        }
    }
}
