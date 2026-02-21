package org.orangehrm.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.orangehrm.actiondriver.ActionDriver;
import org.orangehrm.utils.ExtentManager;
import org.orangehrm.utils.LoggerManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static Properties prop;

    // ThreadLocal instances
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

    public static final Logger logger =
            LoggerManager.getLogger(BaseClass.class);

    // Load config
    @BeforeSuite
    public void loadConfig() {

        prop = new Properties();

        try (FileInputStream fis =
                     new FileInputStream("src/main/resources/config.properties")) {

            prop.load(fis);
            logger.info("config.properties loaded successfully");

        } catch (IOException e) {
            logger.fatal("Failed to load config.properties", e);
            throw new RuntimeException("Config file not found", e);
        }

        ExtentManager.getReporter();


    }

    // Setup
    @BeforeMethod
    public synchronized void setUp() {

        logger.info("--- Test setup started for {} ---",
                this.getClass().getSimpleName());

        launchBrowser();
        configureBrowser();

        staticWait(2);

        // Initialize ActionDriver for current thread
        actionDriver.set(new ActionDriver(getDriver()));

        logger.info("Test setup completed");
    }

    // Browser initialization
    private synchronized void launchBrowser() {

        String browser = prop.getProperty("browser");

        try {

            switch (browser.toLowerCase()) {

                case "chrome":
                    driver.set(new ChromeDriver());
                    ExtentManager.registerDriver(getDriver());
                    logger.info("ChromeDriver initialized");
                    break;

                case "firefox":
                    driver.set(new FirefoxDriver());
                    ExtentManager.registerDriver(getDriver());
                    logger.info("FirefoxDriver initialized");
                    break;

                case "edge":
                    driver.set(new EdgeDriver());
                    ExtentManager.registerDriver(getDriver());
                    logger.info("EdgeDriver initialized");
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unsupported browser: " + browser);
            }

        } catch (Exception e) {
            logger.fatal("Failed to initialize browser: {}", browser, e);
            throw new RuntimeException("Browser initialization failed", e);
        }
    }


    // Browser configuration
    private void configureBrowser() {

        try {

            int implicitWait =
                    Integer.parseInt(prop.getProperty("implicitWait"));

            getDriver().manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(implicitWait));

            getDriver().manage().window().maximize();

            String url = prop.getProperty("url");
            getDriver().navigate().to(url);

            logger.info("Navigated to URL: {}", url);

        } catch (Exception e) {
            logger.fatal("Browser configuration failed", e);
            throw new RuntimeException("Browser setup failed", e);
        }
    }


    // Tear down

    @AfterMethod
    public synchronized void tearDown() {

        logger.info("--- Test teardown started ---");

        try {

            if (driver.get() != null) {
                driver.get().quit();
                logger.info("Browser closed successfully");
            }

        } catch (Exception e) {
            logger.error("Error while quitting driver", e);

        } finally {

            // Prevent memory leaks in parallel execution
            driver.remove();
            actionDriver.remove();
            ExtentManager.endTest();
        }

        logger.info("Teardown completed");
    }


    // Static wait (avoid if possible)

    public void staticWait(int seconds) {

        logger.debug("Static wait for {} seconds", seconds);
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }


    // Getters

    public static WebDriver getDriver() {

        if (driver.get() == null) {
            throw new IllegalStateException(
                    "WebDriver not initialized");
        }

        return driver.get();
    }

    public static ActionDriver getActionDriver() {

        if (actionDriver.get() == null) {
            throw new IllegalStateException(
                    "ActionDriver not initialized");
        }

        return actionDriver.get();
    }

    public static Properties getProp() {
        return prop;
    }
}