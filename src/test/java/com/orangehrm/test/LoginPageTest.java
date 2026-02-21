package com.orangehrm.test;

import org.orangehrm.base.BaseClass;
import org.orangehrm.pages.HomePage;
import org.orangehrm.pages.LoginPage;
import org.orangehrm.utils.ExtentManager;
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
        ExtentManager.startTest("Valid login test");
        ExtentManager.logStep("Navigating to Login Page entering username and password");

        loginPage.login("admin","admin123");
        ExtentManager.logStep("Verifying Admin tab is visible or not");
        Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successful login");

        ExtentManager.logStep("Validation Successful");
        homePage.logout();
        ExtentManager.logStep("Logged out Successfully");
        staticWait(2);
    }

    @Test
    public void invalidLoginTest(){

        ExtentManager.startTest("invalid login test");
        ExtentManager.logStep("Navigating to Login Page entering invalid username and password");
        loginPage.login("admin", "notthispass");
        String expectedErrorMessage = "Invalid credentials";
        Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMessage),"Test Failed: invalid error message");
        ExtentManager.logStep("Validation Successful");
        ExtentManager.logStep("Logged Out");
    }
}
