package com.orangehrm.test;

import org.orangehrm.base.BaseClass;
import org.orangehrm.pages.HomePage;
import org.orangehrm.pages.LoginPage;
import org.orangehrm.utils.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class HomePageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;


    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test
    public void verifyOrangeHRMLogo(){
        ExtentManager.startTest("Home Page Verify Logo Test");

        ExtentManager.logStep("Navigating to Login Page entering username and password");
        loginPage.login("admin", "admin123");

        ExtentManager.logStep("Verifying Logo is visible or not");
        Assert.assertTrue(homePage.verifyOrangeHRMLogo(), "Logo is not visible");

        ExtentManager.logStep("Logged Out");
    }
}
