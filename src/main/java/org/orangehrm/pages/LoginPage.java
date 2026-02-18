package org.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.orangehrm.actiondriver.ActionDriver;
import org.orangehrm.base.BaseClass;

public class LoginPage {

    private final ActionDriver actionDriver;
    // Locators using By
    private final By userNameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.xpath("//button[text()='Login']");
    private final By errorMessage = By.xpath("//p[text()='Invalid credentials']");

    // Constructor
    public LoginPage(WebDriver driver){
        this.actionDriver = BaseClass.getActionDriver();
    }
    // Business methods
    public void login(String userName, String password){
        actionDriver.enterText(userNameField, userName);
        actionDriver.enterText(passwordField, password);
        actionDriver.click(loginButton);
    }

    //Method to check if error message is displayed
    public boolean isErrorMessageDisplayed(){
        return actionDriver.isDisplayed(errorMessage);
    }

    //Method to ger the text from Error message
    public String setErrorMessage(){
        return actionDriver.getText(errorMessage);
    }

    //Verify if error is correct or not
    public boolean verifyErrorMessage(String expectedError){
        return actionDriver.compareText(errorMessage, expectedError);
    }


}
