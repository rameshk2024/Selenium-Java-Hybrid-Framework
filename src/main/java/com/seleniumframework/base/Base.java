package com.seleniumframework.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.seleniumframework.actiondriver.ActionDriver;
import com.seleniumframework.utilities.ExtentManager;
import com.seleniumframework.utilities.LoggerManager;

public class Base {

	protected static Properties prop;
//	protected static WebDriver driver;
//	private static ActionDriver actionDriver;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	public static final Logger logger = LoggerManager.getLogger(Base.class);

	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}

	@BeforeMethod
	@Parameters("browser")
	public synchronized void setup(String browser) throws IOException {
		System.out.println("Setting webDrvier for: " + this.getClass().getSimpleName());
		launchBrowser(browser);
		configureBrowser();
		staticWait(2);
		logger.info("WebDriver is initialized and browser is configured!!");
//		// Initialize actionDriver instance only once
//		if (actionDriver == null) {
//			actionDriver = new ActionDriver(driver);
//			logger.info("ActionDriver instance is created!!");
//		}

		// Initialize actionDriver for the current Thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver initialized for thread: " + Thread.currentThread().getId());
	}

	@BeforeSuite
	public void loadConfig() throws IOException {
		// Load configuration file
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
		logger.info("Properties file is loadded");

		// Start the extent report
		ExtentManager.getReporter();

	}

	// Initialize webdriver based on browser defined in the config file
	private synchronized void launchBrowser(@Optional String browser) {
		//String browser = prop.getProperty("browser");
		
		boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("SeleniumGrid"));
		String gridURL = prop.getProperty("GridURL");
		if(seleniumGrid) {
			try {
				if (browser.equalsIgnoreCase("chrome")) {
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--headless","disable-gpu","--disable-notifications","--no-sandbox","--disable-dev-shm-usage");
					driver.set(new RemoteWebDriver(new URL(gridURL),options));
				}
				else if(browser.equalsIgnoreCase("firefox")) {
					FirefoxOptions options = new FirefoxOptions();
					options.addArguments("--headless"); // Run test in headless mode
					driver.set(new RemoteWebDriver(new URL(gridURL),options));
				}
				else if (browser.equalsIgnoreCase("edge")) {
					EdgeOptions options = new EdgeOptions();
					options.addArguments("--headless");
					driver.set(new RemoteWebDriver(new URL(gridURL),options));
			} else {
				throw new IllegalArgumentException("Browser not supported: "+ browser);
			}
			logger.info("RemoteWebDriver instance is created for selenium grid in headless mode");
		} catch(MalformedURLException e) {
			throw new RuntimeException("Invalid Grid URL", e);
		} 
		} else {
			browser = prop.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {

			//Create a map to store Chrome profile preferences
			Map<String, Object> prefs = new HashMap<String, Object>();
			// 1. Disable the "Save Password" prompt
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			// 2. Disable "Change your password" (Data Breach) alerts (Chrome 124+)
			prefs.put("profile.password_manager_leak_detection", false);
			// Create ChromeOptions
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", prefs);
			options.addArguments("--headless"); // Run test in headless mode
			options.addArguments("--disable-gpu"); // Disable gpu for headless mode
			options.addArguments("--disable-notifications"); // Disable browser notification
			options.addArguments("--no-sandbox"); // Require for CI environments
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in

			driver.set(new ChromeDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver instance is created!!");
		} else if (browser.equalsIgnoreCase("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--headless"); // Run test in headless mode
			options.addArguments("--disable-gpu"); // Disable gpu for headless mode
			options.addArguments("--disable-notifications"); // Disable browser notification
			options.addArguments("--no-sandbox"); // Require for CI environments
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in

			driver.set(new FirefoxDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver instance is created!!");
		} else if (browser.equalsIgnoreCase("edge")) {
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run test in headless mode
			options.addArguments("--disable-gpu"); // Disable gpu for headless mode
			options.addArguments("--disable-notifications"); // Disable browser notification
			options.addArguments("--no-sandbox"); // Require for CI environments
			options.addArguments("--disable-dev-shm-usage"); // Resolve issues in

			driver.set(new EdgeDriver(options));
			ExtentManager.registerDriver(getDriver());
			logger.info("EdgeDriver instance is created!!");
		} else {
			System.out.println("Provided browser is not supported!!");
		}
	}

	}

	// Configure browser settings - maximize browser, implicit wait, launch
	// application url
	private void configureBrowser() {
		// Implicit wait
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// Maximize browser
		getDriver().manage().window().maximize();

		// Launch application URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to launch URL: " + e.getMessage());
		}

	}

	@AfterMethod
	public synchronized void tearDown() {
		try {
			if (getDriver() != null) {
				getDriver().quit();
			}
		} catch (Exception e) {
			System.out.println("Browser not quit properly");
		}
		logger.info("WebDriver instance is closed!!");
		System.out.println("================================================");
//		driver = null;
//		actionDriver = null;

		driver.remove();
		actionDriver.remove();
		// ExtentManager.endTest(); Implemented in TestListener class
	}

	// Driver setter method
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver = driver;
	}

	// getter method for WebDriver
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			System.out.println("WebDriver instance is not initialized!!");
			throw new IllegalStateException("WebDriver instance is not initialized!!");
		}
		return driver.get();
	}

	// getter method for ActionDriver
	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			System.out.println("WebDriver instance is not initialized!!");
			throw new IllegalStateException("WebDriver instance is not initialized!!");
		}
		return actionDriver.get();
	}

	// Getter Method for prop
	public static Properties getProp() {
		return prop;
	}

	// Static wait for pause
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
