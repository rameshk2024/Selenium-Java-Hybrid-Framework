package com.seleniumframework.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.seleniumframework.base.Base;
import com.seleniumframework.utilities.ExtentManager;
import com.seleniumframework.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer{

	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

	// Triggered when test is started
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.startTest(testName);
		// Start logging in extent report
		ExtentManager.logStep("Test started "+ testName);
	}

	// Triggered when test is passed
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logStepWithScreenshot(Base.getDriver(), "Test passed successfully!!", testName);
		
	}

	// Triggered when test is failed
	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		ExtentManager.logFailure(Base.getDriver(), "Test Failed", testName);
	}

	// Triggered when test is skipped
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped " +testName);
	}

	// Triggered when suite starts
	@Override
	public void onStart(ITestContext context) {
		ExtentManager.getReporter(); // Initialize extent report
	}

	// Triggered when test suite ends
	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.endTest(); // It will flush the report
	}
	

}
