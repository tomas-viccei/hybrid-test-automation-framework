package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider="validLogin", dataProviderClass = DataProviders.class)
    public void verifyValidLoginTest(String username, String password){

        loginPage.login(username, password);

        Assert.assertTrue(
                homePage.isAdminTabVisible(),
                "Admin tab should be visible after successful login"
        );

        homePage.logout();
        staticWait(2);
    }

    @Test(dataProvider="invalidLogin", dataProviderClass = DataProviders.class)
    public void invalidLoginTest(String username, String password){

        loginPage.login(username, password);

        Assert.assertTrue(
                loginPage.verifyErrorMessage("Invalid credentials"),
                "Invalid error message"
        );
    }
}