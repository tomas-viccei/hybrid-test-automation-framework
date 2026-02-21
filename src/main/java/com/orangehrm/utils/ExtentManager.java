package com.orangehrm.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentManager {

    private static ExtentReports extentReports;
    private static ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();
    private static Map<Long, WebDriver> driverMap = new HashMap<>();

    //Initialize Extent Report

    public static synchronized ExtentReports getReporter(){
        if (extentReports == null){
           String reportPath = System.getProperty("user.dir")+"/src/test/resources/ExtentReport/ExtentReport.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setReportName("Automation Test Report");
            sparkReporter.config().setDocumentTitle("OrangeHRM Report");
            sparkReporter.config().setTheme(Theme.DARK);

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);

            extentReports.setSystemInfo("Operating System", System.getProperty("os.name"));
            extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
            extentReports.setSystemInfo("User Name", System.getProperty("user.name"));

        }
        return extentReports;
    }


    //Start the test
    public static synchronized ExtentTest startTest(String testName){
        ExtentTest extentTest = getReporter().createTest(testName);
        testThreadLocal.set(extentTest);
        return extentTest;
    }

    public static synchronized void endTest(){
        getReporter().flush();
    }

    //Get Current Thread's test
    public static synchronized ExtentTest getTest(){
        return testThreadLocal.get();
    }

    //Method to get the name of the current test
    public static String  getTestName(){
        ExtentTest currentTest = getTest();
        if (currentTest != null){
            return currentTest.getModel().getName();
        }else {
            return "No test is currently active for this thread";
        }
    }

    //Log a Step
    public static void logStep(String logMessage){
        getTest().info(logMessage);
    }

    public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage){
        getTest().pass(logMessage);
        attachedScreenshot(driver,screenshotMessage);

    }

    //Log a failure
    public static void logFailure(WebDriver driver, String logMessage, String screenshotMessage){
        String colorMessage = String.format("<span style='color:red;'>%s</span>",logMessage);
        getTest().fail(colorMessage);
        attachedScreenshot(driver,screenshotMessage);


    }

    //Log a skip
    public static void logSkip(String logMessage){
        String colorMessage = String.format("<span style='color:orange;'>%s</span>",logMessage);

        getTest().skip(colorMessage);

    }

    //Take Screenshot with date and time in the file
    public static synchronized String takeScreenshot(WebDriver driver, String screenshotName){
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        //Format date and Time for file name
        String timeStamp= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        String destinationPath = System.getProperty("user.dir")+"src/test/resources/screenshots"+ screenshotName +"_"+timeStamp+".png";

        File finalPath = new File(destinationPath);
        try {
            FileUtils.copyFile(src, finalPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Convert screenshot to Base64 fir embedding in the Report
        String base64Format = convertToBase64(src);
        return base64Format;
    }

    //Convert screenshot to Base64 format
    public static String convertToBase64(File screenshotFile){
        String base64Format = "";
        //Read the file content into a byte array
        try {
            byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
            base64Format = Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Convert the byte array to Base64 String
        return base64Format;
    }

    //Attach screenshot to report using Base 64
    public static synchronized void attachedScreenshot(WebDriver driver, String message){
        try {
            String screenshotBase64 =takeScreenshot(driver,getTestName());
            getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
        } catch (Exception e) {
            getTest().fail("Failed to attach screenshot: "+message);
            throw new RuntimeException(e);
        }
    }

    //Register WebDriver for current Thread
    public static void registerDriver(WebDriver driver){
        driverMap.put(Thread.currentThread().threadId(), driver);
    }
}
