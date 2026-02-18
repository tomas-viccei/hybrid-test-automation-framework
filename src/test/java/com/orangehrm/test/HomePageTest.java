package com.orangehrm.test;

import org.orangehrm.base.BaseClass;
import org.orangehrm.pages.HomePage;
import org.orangehrm.pages.LoginPage;
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
        loginPage.login("admin", "admin123");
        Assert.assertTrue(homePage.verifyOrangeHRMLogo(), "Logo is not visible");
    }
}
