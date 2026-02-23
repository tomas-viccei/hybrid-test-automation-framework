package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HomePageTest extends BaseClass {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod
    public void setupPages(){
        loginPage = new LoginPage(getDriver());
        homePage = new HomePage(getDriver());
    }

    @Test(dataProvider="validLogin", dataProviderClass = DataProviders.class)
    public void verifyOrangeHRMLogo(String username, String password){

        loginPage.login(username, password);

        Assert.assertTrue(
                homePage.verifyOrangeHRMLogo(),
                "Logo is not visible"
        );

        homePage.logout();
    }
}