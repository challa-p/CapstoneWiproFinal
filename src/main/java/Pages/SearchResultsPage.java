package Pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.stream.Collectors;

public class SearchResultsPage extends BasePage {
    private By productLinks = By.cssSelector(".product-title a, h2.product-title a");

    public List<String> getAllTitles() {
        try {
            List<WebElement> els = driver.findElements(productLinks);
            return els.stream().map(WebElement::getText).collect(Collectors.toList());
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    public void clickFirstSelectableProduct() {
        List<WebElement> links = driver.findElements(productLinks);
        for (WebElement link : links) {
            try {
                String name = link.getText().toLowerCase();
                if (name.contains("build your own") || name.contains("configure") || name.contains("custom")) {
                    continue;
                }
                link.click();
                return;
            } catch (Exception e) {
                // retry next
            }
        }
        if (!links.isEmpty()) {
            try { links.get(0).click(); } catch (Exception ignored) {}
        }
    }

    public String getFirstProductTitle() {
        List<WebElement> links = driver.findElements(productLinks);
        return links.isEmpty() ? "" : links.get(0).getText().trim();
    }
}

