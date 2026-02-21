package com.orangehrm.listeners;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utils.ExtentManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context){
        ExtentManager.getReporter();
    }

    @Override
    public void onFinish(ITestContext context){
        ExtentManager.endTest();
    }

    @Override
    public void onTestStart(ITestResult result){
        String testName = result.getMethod().getMethodName();
        ExtentManager.startTest(testName);
        ExtentManager.logStep("Test Satarted: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result){
        String testName = result.getMethod().getMethodName();
        ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully", "Test End: " +testName +" ✅ _ Test Passed");

    }

    @Override
    public void onTestFailure(ITestResult result){
        String testName = result.getMethod().getMethodName();
        String failure = result.getThrowable().getMessage();
        ExtentManager.logStep(failure);
        ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed", "Test End: " +testName +" ❌ _ Test Failed");

    }

    @Override
    public void onTestSkipped(ITestResult result){
        String testName = result.getMethod().getMethodName();
        ExtentManager.logSkip("Test Skipped: " +testName);

    }
}
