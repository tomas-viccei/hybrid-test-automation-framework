package org.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.orangehrm.actiondriver.ActionDriver;
import org.orangehrm.base.BaseClass;

public class HomePage {

    private final ActionDriver actionDriver;

    //Define locators using By
    private final By adminTab = By.xpath("//span[text()='Admin']");
    private final By userIDButton = By.className("oxd-userdropdown-name");
    private final By logoutButton = By.xpath("//a[text()='Logout']");
    private final By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");

    //Constructor
    public HomePage(WebDriver driver){
        this.actionDriver = BaseClass.getActionDriver();
    }

    public boolean isAdminTabVisible(){
        return actionDriver.isDisplayed(adminTab);
    }

    public boolean verifyOrangeHRMLogo(){
        return actionDriver.isDisplayed(orangeHRMLogo);
    }

    //Method to perform logout operation
    public void logout(){
        actionDriver.click(userIDButton);
        actionDriver.click(logoutButton);
    }

}
