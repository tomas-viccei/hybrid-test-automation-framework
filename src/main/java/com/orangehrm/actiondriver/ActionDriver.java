package com.orangehrm.actiondriver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.orangehrm.base.BaseClass;
import org.apache.logging.log4j.Logger;
import com.orangehrm.utils.ExtentManager;

import java.time.Duration;

public class ActionDriver {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = BaseClass.logger;

    public ActionDriver(WebDriver driver) {
        this.driver = driver;

        int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));

        logger.info("ActionDriver initialized with explicit wait: {} seconds", explicitWait);
    }

    // Click element
    public void click(By by) {
        String elementDescription = getElementDescription(by);
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            ExtentManager.logStep("Clicked an element: "+elementDescription);
            logger.debug("Clicked on element: {}", elementDescription);

        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element", elementDescription + "_unable to click");
            logger.error("Failed to click element: {}", elementDescription, e);
            throw new RuntimeException("Click failed for: " + elementDescription, e);
        }
    }

    // Enter text
    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);

            logger.debug("Entered text into element: {}", getElementDescription(by) + value);

        } catch (Exception e) {
            logger.error("Failed to enter text '{}' into element: {}", value, getElementDescription(by), e);
            throw new RuntimeException("Enter text failed for: " + by, e);
        }
    }

    // Get text
    public String getText(By by) {
        try {
            waitForElementToBeVisible(by);
            String text = driver.findElement(by).getText();

            logger.debug("Text from {} is '{}'", by, text);
            return text;

        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", by, e);
            throw new RuntimeException("Get text failed for: " + by, e);
        }
    }

    // Compare text
    public boolean compareText(By by, String expectedText) {
        try {
            waitForElementToBeVisible(by);
            String actualText = driver.findElement(by).getText();

            boolean match = expectedText.equals(actualText);

            if (match) {
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),"Compare Text","Text verified successfully!: Expected = "+"'"+expectedText+"'"+" Actual = "+"'"+actualText+"'");
                logger.info("Text matches. Expected='{}', Actual='{}'", expectedText, actualText);
            } else {
                ExtentManager.logFailure(BaseClass.getDriver(),"Compare Text","Text Comparison failed: Expected = "+"'"+expectedText+"'"+" Actual = "+"'"+actualText+"'");
                logger.warn("Text mismatch. Expected='{}', Actual='{}'", expectedText, actualText);
            }

            return match;

        } catch (Exception e) {
            logger.error("Failed to compare text for element: {}", by, e);
            throw new RuntimeException("Compare text failed for: " + by, e);
        }
    }

    // Check displayed
    public boolean isDisplayed(By by) {
        try {
            waitForElementToBeVisible(by);
            ExtentManager.logStep("Element is displayed "+ getElementDescription(by));
            logger.info("Element is displayed" + getElementDescription(by));
            return driver.findElement(by).isDisplayed();

        } catch (TimeoutException e) {
            logger.warn("Element not displayed within wait time: {}", by);
            return false;

        } catch (Exception e) {
            ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed", "Element is not displayed: "+ getElementDescription(by) );
            logger.error("Error checking display status for: {}", by, e);
            return false;
        }
    }

    // Wait for page load
    public void waitForPageLoad(int timeoutInSecs) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutInSecs))
                    .until(d -> ((JavascriptExecutor) d)
                            .executeScript("return document.readyState")
                            .equals("complete"));

            logger.debug("Page loaded successfully");

        } catch (Exception e) {
            logger.error("Page did not load within {} seconds", timeoutInSecs, e);
            throw new RuntimeException("Page load timeout", e);
        }
    }

    // Scroll to element
    public void scrollToElement(By by) {
        try {
            WebElement element = driver.findElement(by);
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", element);

            logger.debug("Scrolled to element: {}", by);

        } catch (Exception e) {
            logger.error("Failed to scroll to element: {}", by, e);
            throw new RuntimeException("Scroll failed for: " + by, e);
        }
    }

    // Wait clickable
    private void waitForElementToBeClickable(By by) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by));

        } catch (Exception e) {
            logger.error("Element not clickable: {}", by, e);
            throw new RuntimeException("Element not clickable: " + by, e);
        }
    }

    // Wait visible
    private void waitForElementToBeVisible(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));

        } catch (Exception e) {
            logger.error("Element not visible: {}", by, e);
            throw new RuntimeException("Element not visible: " + by, e);
        }
    }

    //Method to get the description of an element using By locator

    public String getElementDescription(By locator){
        //Check for null driver or locator to avoid NullPointer Exception
        if (driver == null)
            return "driver is null";
        if (locator == null)
            return "Locator is null";

        //Find the element using the locator
        WebElement element = driver.findElement(locator);

        try {
            //Get element attributes
            String name =  element.getDomAttribute("name");
            String id = element.getDomAttribute("id");
            String text = element.getText();
            String className = element.getDomAttribute("class");
            String placeHolder = element.getDomAttribute("placeholder");

            //Return the description based on element attributed
            //Return the description based on element attributes
            if (isNotEmpty(name)) {
                return "Element with name: " + name;
            }
            else if (isNotEmpty(id)) {
                return "Element with id: " + id;
            }
            else if (isNotEmpty(text)) {
                return "Element with text: " + truncate(text, 50);
            }
            else if (isNotEmpty(className)) {
                return "Element with class: " + className;
            }
            else if (isNotEmpty(placeHolder)) {
                return "Element with placeholder: " + placeHolder;
            }
            else {
                return "Element without identifiable attributes";
            }
        } catch (Exception e) {
            logger.error("Unable to describe the element",e);
            throw new RuntimeException(e);
        }

    }

    //TODO
    //Create an StringChecker class in utils
    //Utility Method to check a String is not null or empty
    private boolean isNotEmpty(String value){
        return value != null && !value.isEmpty();
    }

    //Utility Method to truncate long string
    private String truncate(String value, int maxLength){
        if (value == null || value.length()<= maxLength){
            return value;
        }
        return value.substring(0,maxLength);
    }

}
