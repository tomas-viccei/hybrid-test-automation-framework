package org.orangehrm.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.orangehrm.actiondriver.ActionDriver;
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
    protected static WebDriver driver;
    private static ActionDriver actionDriver;

    public static final Logger logger =
            LoggerManager.getLogger(BaseClass.class);

    // Load config
    @BeforeSuite
    public void loadConfig() {
        prop = new Properties();

        try (FileInputStream fis =
                     new FileInputStream("src/main/resources/config.properties")) {

            prop.load(fis);
            logger.info("Config.properties loaded successfully");

        } catch (IOException e) {
            logger.fatal("Failed to load config.properties", e);
            throw new RuntimeException("Config file not found", e);
        }
    }

    // Setup
    @BeforeMethod
    public void setUp() {
        logger.info("=== Test setup started for {} ===",
                this.getClass().getSimpleName());

        launchBrowser();
        configureBrowser();

        staticWait(2);

        if (actionDriver == null) {
            actionDriver = new ActionDriver(driver);
        }

        logger.info("Test setup completed");
    }

    // Browser init
    private void launchBrowser() {
        String browser = prop.getProperty("browser");

        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    logger.info("ChromeDriver initialized");
                    break;

                case "firefox":
                    driver = new FirefoxDriver();
                    logger.info("FirefoxDriver initialized");
                    break;

                case "edge":
                    driver = new EdgeDriver();
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

    // Browser config
    private void configureBrowser() {
        try {
            int implicitWait =
                    Integer.parseInt(prop.getProperty("implicitWait"));

            driver.manage().timeouts()
                    .implicitlyWait(Duration.ofSeconds(implicitWait));

            driver.manage().window().maximize();

            String url = prop.getProperty("url");
            driver.navigate().to(url);

            logger.info("Navigated to URL: {}", url);

        } catch (Exception e) {
            logger.fatal("Browser configuration failed", e);
            throw new RuntimeException("Browser setup failed", e);
        }
    }

    // Tear down
    @AfterMethod
    public void tearDown() {
        logger.info("=== Test teardown started ===");

        if (driver != null) {
            try {
                driver.quit();
                logger.info("Browser closed successfully");

            } catch (Exception e) {
                logger.error("Error while quitting driver", e);
            }
        }

        driver = null;
        actionDriver = null;

        logger.info("Teardown completed");
    }

    // Static wait (avoid when possible)
    public void staticWait(int seconds) {
        logger.debug("Static wait for {} seconds", seconds);
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

    // Getters
    public static ActionDriver getActionDriver() {
        if (actionDriver == null) {
            throw new IllegalStateException(
                    "ActionDriver not initialized");
        }
        return actionDriver;
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver not initialized");
        }
        return driver;
    }

    public static Properties getProp() {
        return prop;
    }

    public void setDriver(WebDriver driver) {
        BaseClass.driver = driver;
    }
}
