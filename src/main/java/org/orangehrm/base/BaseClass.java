package org.orangehrm.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.orangehrm.actiondriver.ActionDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;


public class BaseClass {


    protected static Properties prop;
    protected static WebDriver driver;
    private static ActionDriver actionDriver;


    @BeforeSuite
    public void loadConfig() throws IOException {
        //Load the config file
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
    }
    @BeforeMethod
    public void setUp() throws IOException {
        System.out.println("Setting up WebDriver for: " + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(5);

        //Initialize the actionDriver
        if (actionDriver == null){
            actionDriver = new ActionDriver(driver);
        }

    }

    private void launchBrowser(){
        String browser = prop.getProperty("browser");

        //Initialize the WebDriver based on browser defined in config file
        if (browser.equalsIgnoreCase("chrome")){
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")){
            driver = new EdgeDriver();
        }
        else {
            throw new IllegalArgumentException("Browser Not Suported: "+ browser);
        }


    }

    private void configureBrowser(){
        //Implicit Wait
        int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        //Browser maximize
        driver.manage().window().maximize();

        //Navigate to URL
        try {

            driver.navigate().to(prop.getProperty("url"));
        }
        catch (Exception e){
            System.out.println("Failed to Navigate to the URL "+ e.getMessage());
        }
    }


    @AfterMethod
    public void tearDown(){
        if (driver!=null) {
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("Unable to quit the driver"+e.getMessage());
            }
        }
        driver = null;
        actionDriver = null;
    }

    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }


    public static ActionDriver getActionDriver(){
        if (actionDriver == null){
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return actionDriver;
    }

    public static WebDriver getDriver() {
        if (driver == null){
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public static Properties getProp() {
        return prop;
    }



}
