package org.orangehrm.actiondriver;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orangehrm.base.BaseClass;


import java.time.Duration;
import java.util.Properties;

public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;

        int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    //Method to click an element
    public void click(By by){
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
        } catch (Exception e) {
            throw new RuntimeException("Unable to click: "+by,e);
        }
    }

    //Method to enter text into an input field
    public void enterText(By by, String value){
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);
        } catch (Exception e) {
            throw new RuntimeException("Unable to enter text: "+value, e);
        }
    }

    //Method to get text from an input field
    public String getText(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).getText();
        } catch (Exception e) {
            throw new RuntimeException("Unable to get text: "+ by, e);
        }
    }

    //Method to compare Two Text
    public boolean compareText(By by, String expectedText){
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();
            if (expectedText.equals(actualText)){
                System.out.println("Text are Matching: "+actualText + "=" + expectedText);
                return true;
            }
            else {
                System.out.println("Text are not equals: "+actualText + "!=" + expectedText);
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to compare Texts: "+by,e);
        }

    }

    //Method to check if an element is displayed
    public boolean isDisplayed(By by){
        try {
            waitForElementToBeVisible(by);
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }





    //Wait the page to be loaded
    public void waitForPageLoad(int timeOutInSecs)  {
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSecs)).until(WebDriver -> ((JavascriptExecutor) WebDriver))
                    .executeScript("return document.readyState").equals("complete");
        } catch (Exception e) {
            throw new RuntimeException("Page did not load within "+timeOutInSecs + "seconds. Exception: "+e.getMessage());
        }
    }

    //Scroll to an element
    public void scrollToElement(By by){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement element = driver.findElement(by);
            js.executeScript("arguments[0],scrollIntoView(true);", element);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //Wait for element to be clickable
    private void waitForElementToBeClickable(By by){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            throw new RuntimeException("Element is not clickeable: "+e.getMessage());
        }
    }

    //Wait for Element to be Visible
    private void waitForElementToBeVisible(By by){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception e) {
            throw new RuntimeException("Element is not visible: "+e.getMessage());
        }
    }
}
