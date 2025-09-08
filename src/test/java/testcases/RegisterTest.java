 package testcases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Pages.RegisterPage;

public class RegisterTest extends BaseTest {
    private RegisterPage register;

    @BeforeClass
    public void init() {
        register = new RegisterPage(driver());
    }

    @Test(priority = 1)
    public void registerNewUser() {
        driver().get(baseUrl() + "/register");

        boolean ok = register.register(
                "TestFirst",
                "TestLast",
                "auto" + System.currentTimeMillis() + "@mail.com",
                "Password123!"
        );

        Assert.assertTrue(ok, "Registration flow failed.");
    }
}