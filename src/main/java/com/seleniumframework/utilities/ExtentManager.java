package com.seleniumframework.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize the Extent Report
	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
			System.out.println(reportPath);
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
			sparkReporter.config().setReportName("Selenium Java Hybrid Automation");
			sparkReporter.config().setDocumentTitle("Selenium Automation Framework");
			sparkReporter.config().setTimeStampFormat("dd/MM/YYYY hh:mm:ss");
			sparkReporter.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.attachReporter(sparkReporter);
			// Adding System Properties
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));

		}
		return extent;
	}

	// Start the test
	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// End the test
	public synchronized static void endTest() {
		getReporter().flush();
	}

	// Method to get the current thread
	public synchronized static ExtentTest getTest() {
		return test.get();
	}

	// Method to get test name
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "Currently no test is active!!";
		}
	}

	// log step
	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}

	// log step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().pass(logMessage);
		attachScreenshot(driver, screenshotMessage);
	}

	// Log failure
	public static void logFailure(WebDriver driver, String logMessage, String screenshotMessage) {
		String colorMessage = String.format("<span style='color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
		// Screenshot Method
		attachScreenshot(driver, screenshotMessage);
	}

	// Log skip
	public static void logSkip(String logMessage) {
		String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}

	// TakeScreenshot with date and time
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File src = screenshot.getScreenshotAs(OutputType.FILE);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		// Save screenshot to File
		String destinationPath = System.getProperty("user.dir") + "/src/test/resources/Screenshots/" + screenshotName
				+ "_" + timeStamp + ".png";
		File finalPath = new File(destinationPath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Convert screenshots to Base64 embeding in the report
		String base64Format = convertToBase64(src);
		return base64Format;
	}

	// Method to convert screenshot to base64 Format
	public static String convertToBase64(File screenshotFile) {
		String base64Format = "";
		// Read file content into a byte array
		// byte[] fileContent = null;
		try {
			byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// convert byte to base64
		return base64Format;
	}

	// Attach screenshot using base64
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenshotBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenshotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach the screenshot" + message);
			e.printStackTrace();
		}

	}

	// Register WebDriver for current Thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}
