package org.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.orangehrm.actiondriver.ActionDriver;

public class HomePage {

    private final ActionDriver actionDriver;

    //Define locators using By
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By userIDButton = By.className("oxd-userdropdown-name");
    private By logoutButton = By.xpath("//a[text()='Logout']");
    private By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");

    //Constructor
    public HomePage(WebDriver driver){
        this.actionDriver = new ActionDriver(driver);
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
