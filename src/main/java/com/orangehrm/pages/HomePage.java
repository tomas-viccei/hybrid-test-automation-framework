package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utils.ExtentManager;

public class HomePage {

    private final ActionDriver actionDriver;

    // Locators
    private final By adminTab = By.xpath("//span[text()='Admin']");
    private final By userIDButton = By.className("oxd-userdropdown-name");
    private final By logoutButton = By.xpath("//a[text()='Logout']");
    private final By orangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']//img");

    // Constructor
    public HomePage(WebDriver driver){
        this.actionDriver = BaseClass.getActionDriver();
    }

    // Validations

    public boolean isAdminTabVisible(){

        boolean visible = actionDriver.isDisplayed(adminTab);

        if (visible) {
            ExtentManager.logStep("Admin tab is visible.");
        } else {
            ExtentManager.logFailure(
                    BaseClass.getDriver(),
                    "Admin tab is NOT visible.",
                    "Admin Tab Not Visible"
            );
        }

        return visible;
    }

    public boolean verifyOrangeHRMLogo(){

        boolean visible = actionDriver.isDisplayed(orangeHRMLogo);

        if (visible) {
            ExtentManager.logStepWithScreenshot(
                    BaseClass.getDriver(),
                    "OrangeHRM logo is visible.",
                    "Logo Validation Success"
            );
        } else {
            ExtentManager.logFailure(
                    BaseClass.getDriver(),
                    "OrangeHRM logo is NOT visible.",
                    "Logo Validation Failure"
            );
        }

        return visible;
    }

    // Actions

    public void logout(){

        ExtentManager.logStep("Clicking user dropdown.");
        actionDriver.click(userIDButton);

        ExtentManager.logStep("Clicking logout button.");
        actionDriver.click(logoutButton);

        ExtentManager.logStep("Logout completed successfully.");
    }
}