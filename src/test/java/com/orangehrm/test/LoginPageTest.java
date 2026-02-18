package com.orangehrm.test;

import org.orangehrm.base.BaseClass;
import org.orangehrm.pages.HomePage;
import org.orangehrm.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void verifyValidLoginTest(){
        loginPage.login("admin","admin123");
        Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successful login");
        homePage.logout();
        staticWait(2);
    }

    @Test
    public void invalidLoginTest(){
        loginPage.login("admin", "notthispass");
        String expectedErrorMessage = "Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: invalid error message");
    }
}
