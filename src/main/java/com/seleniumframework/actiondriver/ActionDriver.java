package com.seleniumframework.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.seleniumframework.base.Base;
import com.seleniumframework.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = Base.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(Base.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver instance is created!!");
	}

	// Method to click on element
	public void click(By locator) {

		try {
			waitForElementToBeClickable(locator);
			driver.findElement(locator).click();
		} catch (Exception e) {
			System.out.println("Unable to click on element " + e.getMessage());
			ExtentManager.logFailure(Base.getDriver(), "Unable to click on element ", "unable to click");
			logger.error("FAILED: Unable to click an element!!");
			ExtentManager.logStepWithScreenshot(Base.getDriver(), "Unable to click element ", e.getMessage());
		}
	}

	// Method to enter text
	public void enterText(By locator, String value) {
		try {
			waitForElementToBeVisible(locator);
			WebElement element = driver.findElement(locator);
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			System.out.println("Unable to enter text " + e.getMessage());
			logger.error("FAILED: Unable to enter text!!");
			ExtentManager.logStepWithScreenshot(Base.getDriver(), "Unable to enter text ", e.getMessage());
		}

	}

	// Method to get text
	public String getText(By locator) {
		try {
			waitForElementToBeVisible(locator);
			return driver.findElement(locator).getText();
		} catch (Exception e) {
			System.out.println("Unable to get text " + e.getMessage());
			logger.error("FAILED: Unable to get text!!");
			ExtentManager.logStepWithScreenshot(Base.getDriver(), "Unable to get text ", e.getMessage());
			return "";
		}
	}

	// Select dropdown option by using visible text
	public void selectByVisibleText(By locator, String value) {
		try {
			WebElement element = driver.findElement(locator);
			new Select(element).selectByVisibleText(value);
			logger.info("Selected dropdown value: " + value);
		} catch (Exception e) {
			logger.error("Unable to select dropdown value: " + value);
		}
	}

	// Select dropdown option by using visible value
	public void selectByValue(By locator, String value) {
		try {
			WebElement element = driver.findElement(locator);
			new Select(element).selectByValue(value);
			logger.info("Selected dropdown option by actual value: " + value);
		} catch (Exception e) {
			logger.error("Unable to select dropdown value using actual value : " + value);
		}
	}

	// Select dropdown option by using visible value
	public void selectByIndex(By locator, int index) {
		try {
			WebElement element = driver.findElement(locator);
			new Select(element).selectByIndex(index);
			logger.info("Selected dropdown option by index: " + index);
		} catch (Exception e) {
			logger.error("Unable to select dropdown value using index : " + index);
		}
	}

	// Method to get all dropdown options
	public List<String> getDropdownOptions(By locator) {
		List<String> optionList = new ArrayList<>();
		try {
			WebElement dropdownElement = driver.findElement(locator);
			Select select = new Select(dropdownElement);
			for (WebElement option : select.getOptions()) {
				optionList.add(option.getText());
			}
			logger.info("Retrieved dropdown options");
		} catch (Exception e) {
			logger.info("Unable to get dropdown options!!" + e.getMessage());
		}
		return optionList;
	}

	// Method to click using javaScript
	public void clickUsingJS(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			logger.info("Clicked element using Javascript!!");
		} catch (Exception e) {
			logger.error("Unable to click element using JS!!" + e.getMessage());

		}
	}

	// Method to scroll to the bottom of the page
	public void scrollToBottom() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
		logger.info("Scrolled to the bottom of the page!!");
	}

	// Method to scroll to the top of the page
	public void scrollToTop() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
		logger.info("Scrolled to the Top of the page!!");
	}

	// Method to highlight an element using JavaScript
	public void highlightElementJS(By by) {
		try {
			WebElement element = driver.findElement(by);
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid yellow'", element);
			logger.info("Highlighted element using JavaScript: ");
		} catch (Exception e) {
			logger.error("Unable to highlight element using JavaScript");
		}
	}

	// Method to check if element is displayed
	public boolean isDisplayed(By locator) {
		try {
			waitForElementToBeVisible(locator);
			return driver.findElement(locator).isDisplayed();
		} catch (Exception e) {
			System.out.println("Element is not displayed" + e.getMessage());
			logger.error("FAILED: Element is not displayed!!");
			ExtentManager.logStepWithScreenshot(Base.getDriver(), "Element is not displayed ",
					"Element is not displayed");
			return false;
		}
	}

	// Method to scroll to emement
	public void scrollToElement(By locator) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(locator);
			js.executeScript("arguements[0],scrollIntoView(true);", element);
		} catch (Exception e) {
			System.out.println("Unable to scroll to element " + e.getMessage());
			logger.error("FAILED: Unable to scroll element!!");
			ExtentManager.logStepWithScreenshot(Base.getDriver(), "Unable to scroll to element ",
					"Unable to scroll to element");
		}

	}

	// Wait for page to be loaded
	public void waitForPageToBeLoaded(int timeout) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeout)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			System.out.println("Page is loaded successfully");
		} catch (Exception e) {
			System.out.println("Page is not loaded within specified time " + e.getMessage());
			logger.error("FAILED: Page is not loadded within specified time!!");
			ExtentManager.logStepWithScreenshot(Base.getDriver(), "Page is not loaded ", e.getMessage());
		}
	}

	// wait for element to be clickable
	private void waitForElementToBeClickable(By locator) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (Exception e) {
			logger.error("FAILED: Element is not clickable!! " + e.getMessage());
			ExtentManager.logStepWithScreenshot(Base.getDriver(), "Element is not clickable ", e.getMessage());
		}
	}

	// Wait for element to be visible
	private void waitForElementToBeVisible(By locator) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		} catch (Exception e) {
			logger.error("FAILED: Element is not visible!! " + e.getMessage());
			ExtentManager.logStepWithScreenshot(Base.getDriver(), "Element is not visible ", e.getMessage());
		}
	}

	// Utility Method to Border an element
	public void applyBorder(By by, String color) {
		try {
			// Locate the element
			WebElement element = driver.findElement(by);
			// Apply the border
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color " + color + " to element!");
		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element!");
		}
	}

	// Method to switch between browser windows
	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					logger.info("Switched to window: " + windowTitle);
					return;
				}
			}
			logger.warn("Window with title " + windowTitle + " not found.");
		} catch (Exception e) {
			logger.error("Unable to switch window", e);
		}
	}

	// Method to switch to an iframe
	public void switchToFrame(By by) {
		try {
			driver.switchTo().frame(driver.findElement(by));
			logger.info("Switched to iframe: ");
		} catch (Exception e) {
			logger.error("Unable to switch to iframe", e);
		}
	}

	// Method to switch back to the default content
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
		logger.info("Switched back to default content.");
	}

	// Method to accept an alert popup
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert accepted.");
		} catch (Exception e) {
			logger.error("No alert found to accept", e);
		}
	}

	// Method to dismiss an alert popup
	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert dismissed.");
		} catch (Exception e) {
			logger.error("No alert found to dismiss", e);
		}
	}

	// Method to get alert text
	public String getAlertText() {
		try {
			return driver.switchTo().alert().getText();
		} catch (Exception e) {
			logger.error("No alert text found", e);
			return "";
		}
	}

	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtentManager.logStep("Page refreshed successfully.");
			logger.info("Page refreshed successfully.");
		} catch (Exception e) {
			ExtentManager.logFailure(Base.getDriver(), "Unable to refresh page", "refresh_page_failed");
			logger.error("Unable to refresh page: " + e.getMessage());
		}
	}

	public String getCurrentURL() {
		try {
			String url = driver.getCurrentUrl();
			ExtentManager.logStep("Current URL fetched: " + url);
			logger.info("Current URL fetched: " + url);
			return url;
		} catch (Exception e) {
			ExtentManager.logFailure(Base.getDriver(), "Unable to fetch current URL", "get_current_url_failed");
			logger.error("Unable to fetch current URL: " + e.getMessage());
			return null;
		}
	}

	public void maximizeWindow() {
		try {
			driver.manage().window().maximize();
			ExtentManager.logStep("Browser window maximized.");
			logger.info("Browser window maximized.");
		} catch (Exception e) {
			ExtentManager.logFailure(Base.getDriver(), "Unable to maximize window", "maximize_window_failed");
			logger.error("Unable to maximize window: " + e.getMessage());
		}
	}

	public void moveToElement(By by) {

		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(driver.findElement(by)).perform();
			ExtentManager.logStep("Moved to element: ");
			logger.info("Moved to element --> ");
		} catch (Exception e) {
			ExtentManager.logFailure(Base.getDriver(), "Unable to move to element", "_move_failed");
			logger.error("Unable to move to element: " + e.getMessage());
		}
	}

	public void dragAndDrop(By source, By target) {

		try {
			Actions actions = new Actions(driver);
			actions.dragAndDrop(driver.findElement(source), driver.findElement(target)).perform();
			ExtentManager.logStep("Dragged element: " + source + " and dropped on " + target);
			logger.info("Dragged element: " + source + " and dropped on " + target);
		} catch (Exception e) {
			ExtentManager.logFailure(Base.getDriver(), "Unable to drag and drop", source + "_drag_failed");
			logger.error("Unable to drag and drop: " + e.getMessage());
		}
	}

	public void doubleClick(By by) {

		try {
			Actions actions = new Actions(driver);
			actions.doubleClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Double-clicked on element: ");
			logger.info("Double-clicked on element --> ");
		} catch (Exception e) {
			ExtentManager.logFailure(Base.getDriver(), "Unable to double-click element", e.getMessage());
			logger.error("Unable to double-click element: " + e.getMessage());
		}
	}

	public void rightClick(By by) {
		// String elementDescription = getElementDescription(by);
		try {
			Actions actions = new Actions(driver);
			actions.contextClick(driver.findElement(by)).perform();
			ExtentManager.logStep("Right-clicked on element: ");
			logger.info("Right-clicked on element --> ");
		} catch (Exception e) {
			ExtentManager.logFailure(Base.getDriver(), "Unable to right-click element", e.getMessage());
			logger.error("Unable to right-click element: " + e.getMessage());
		}
	}

	public void sendKeysWithActions(By by, String value) {

		try {
			Actions actions = new Actions(driver);
			actions.sendKeys(driver.findElement(by), value).perform();
			ExtentManager.logStep("Sent keys to element: " + " | Value: " + value);
			logger.info("Sent keys to element --> " + " | Value: " + value);
		} catch (Exception e) {
			ExtentManager.logFailure(Base.getDriver(), "Unable to send keys", "_sendkeys_failed");
			logger.error("Unable to send keys to element: " + e.getMessage());
		}
	}

	public void clearText(By by) {

		try {
			driver.findElement(by).clear();
			ExtentManager.logStep("Cleared text in element: ");
			logger.info("Cleared text in element --> ");
		} catch (Exception e) {
			ExtentManager.logFailure(Base.getDriver(), "Unable to clear text", "_clear_failed");
			logger.error("Unable to clear text in element: " + e.getMessage());
		}
	}

	// Method to upload a file
	public void uploadFile(By by, String filePath) {
		try {
			driver.findElement(by).sendKeys(filePath);
			applyBorder(by, "green");
			logger.info("Uploaded file: " + filePath);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to upload file: " + e.getMessage());
		}
	}

}
