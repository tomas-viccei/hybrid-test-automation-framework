package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;
import com.orangehrm.utils.ExtentManager;

public class LoginPage {

    private final ActionDriver actionDriver;

    // Locators
    private final By userNameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.xpath("//button[@type='submit']");
    private final By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    // Constructor
    public LoginPage(WebDriver driver){
        this.actionDriver = BaseClass.getActionDriver();
    }

    // Business Methods

    public void login(String userName, String password){

        ExtentManager.logStep("Entering username: " + userName);
        actionDriver.enterText(userNameField, userName);

        ExtentManager.logStep("Entering password");
        actionDriver.enterText(passwordField, password);

        ExtentManager.logStep("Clicking Login button");
        actionDriver.click(loginButton);
    }

    // Validations

    public boolean isErrorMessageDisplayed(){

        boolean displayed = actionDriver.isDisplayed(errorMessage);

        if(displayed){
            ExtentManager.logStep("Error message is displayed.");
        } else {
            ExtentManager.logFailure(
                    BaseClass.getDriver(),
                    "Error message is NOT displayed.",
                    "Error message not visible"
            );
        }

        return displayed;
    }

    public String getErrorMessage(){

        String message = actionDriver.getText(errorMessage);

        ExtentManager.logStep("Captured error message: " + message);

        return message;
    }

    public boolean verifyErrorMessage(String expectedError){

        boolean result = actionDriver.compareText(errorMessage, expectedError);

        if(result){

            ExtentManager.logStepWithScreenshot(
                    BaseClass.getDriver(),
                    "Error message validation passed.",
                    "Validation Success Screenshot"
            );

        } else {

            ExtentManager.logFailure(
                    BaseClass.getDriver(),
                    "Error message validation FAILED. Expected: " + expectedError,
                    "Validation Failure Screenshot"
            );
        }

        return result;
    }
}