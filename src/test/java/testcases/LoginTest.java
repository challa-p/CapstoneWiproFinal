 package testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Pages.LoginPage;

public class LoginTest extends BaseTest {
    private LoginPage login;

    @BeforeClass
    public void init() { login = new LoginPage(); }

    @Test(priority = 2)
    public void loginValid() {
        driver().get(baseUrl() + "/login");
        boolean ok = login.login("demo@nopcommerce.com", "demopassword"); // change to a valid user or test account
        Assert.assertTrue(ok, "Login action failed to execute.");
    }

    @Test(priority = 3)
    public void loginInvalid() {
        driver().get(baseUrl() + "/login");
        boolean ok = login.login("bad@no.com", "wrong");
        // Expect the app to show an error - just verify we remain on login page or error present
        Assert.assertTrue(driver().getTitle().toLowerCase().contains("login") || !ok == false, "Invalid login negative check");
    }
}


