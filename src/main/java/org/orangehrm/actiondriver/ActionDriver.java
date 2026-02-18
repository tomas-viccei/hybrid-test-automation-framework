package org.orangehrm.actiondriver;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.orangehrm.base.BaseClass;
import org.apache.logging.log4j.Logger;

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
        try {
            waitForElementToBeClickable(by);
            driver.findElement(by).click();
            logger.debug("Clicked on element: {}", by);

        } catch (Exception e) {
            logger.error("Failed to click element: {}", by, e);
            throw new RuntimeException("Click failed for: " + by, e);
        }
    }

    // Enter text
    public void enterText(By by, String value) {
        try {
            waitForElementToBeVisible(by);
            WebElement element = driver.findElement(by);
            element.clear();
            element.sendKeys(value);

            logger.debug("Entered text into element: {}", by);

        } catch (Exception e) {
            logger.error("Failed to enter text '{}' into element: {}", value, by, e);
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
                logger.info("Text matches. Expected='{}', Actual='{}'", expectedText, actualText);
            } else {
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
            return driver.findElement(by).isDisplayed();

        } catch (TimeoutException e) {
            logger.warn("Element not displayed within wait time: {}", by);
            return false;

        } catch (Exception e) {
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
}
