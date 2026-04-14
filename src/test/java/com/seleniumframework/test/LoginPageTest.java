package com.seleniumframework.test;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.seleniumframework.base.Base;
import com.seleniumframework.pages.InventoryPage;
import com.seleniumframework.pages.LoginPage;
import com.seleniumframework.utilities.DataProviders;
import com.seleniumframework.utilities.EncryptData;
import com.seleniumframework.utilities.ExtentManager;

public class LoginPageTest extends Base {

	private LoginPage loginPage;
	private InventoryPage inventoryPage;
	private static final Logger logger = Base.logger;
	SoftAssert softAssert = getSoftAssert();

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		inventoryPage = new InventoryPage(getDriver());
	}
	
	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class,description="Verify that user is able to login successfully with valid credentials")
	public void verifyLoginTest(String username, String password) throws Exception {
		//ExtentManager.startTest("Valid login test"); -- this is configured in listeners so removed
		String decryptedUsername = EncryptData.decryptData(username);
		String decryptedPassword = EncryptData.decryptData(password);
		ExtentManager.logStep("Enter valid username and password");
		loginPage.login(decryptedUsername, decryptedPassword);
		ExtentManager.logStep("Validate SauceDemo logo is displayed");
		softAssert.assertFalse(inventoryPage.isSauceDemoLogoDisplayed(),"Failed: Login is not successful!!");
		ExtentManager.logStep("Logout from the application");
		inventoryPage.logout();
		staticWait(2);
		ExtentManager.logStep("Validate redirected to login and login button is displayed");
		Assert.assertTrue(loginPage.isLoginButtonDisplayed());
		ExtentManager.logStep("=======================================================================");

	}
	
	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class,description="Verify that error is displayed for invalid credentials")
	public void verifyInvalidLogin(String username, String password) {
		//ExtentManager.startTest("InValid login test");
		ExtentManager.logStep("Enter invalid username and passowrd");
		loginPage.login(username, password);
		ExtentManager.logStep("Validate error for invalid login is displayed");
		ExtentManager.logStep("Error for invalid login: "+ loginPage.getErrorMessage());
		Assert.assertTrue(loginPage.isErrorMessageDisplayed(),"Failed:Error for invalid login is not displayed!!");
		ExtentManager.logStep("=======================================================================");

	}

}
