package com.seleniumframework.test;

import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seleniumframework.base.Base;
import com.seleniumframework.pages.InventoryPage;
import com.seleniumframework.pages.LoginPage;
import com.seleniumframework.utilities.EncryptData;
import com.seleniumframework.utilities.ExtentManager;

public class InventoryDetailsTest extends Base {
	
	private LoginPage loginPage;
	private InventoryPage inventoryPage;
	private static final Logger logger = Base.logger;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		inventoryPage = new InventoryPage(getDriver());
	}
	
	@Test(description = "Verify that inventory details are displayed")
	public void verifyInventoryDetails() throws Exception {
		//ExtentManager.startTest("Verify Inventory Details");
		String decryptedUsername = EncryptData.decryptData(prop.getProperty("valid_userName"));
		String decryptedPassword = EncryptData.decryptData(prop.getProperty("valid_password"));
		loginPage.login(decryptedUsername, decryptedPassword);
		String productName = inventoryPage.getProductName();
		ExtentManager.logStep("Product Name: " + productName);
		String expectedProductName = prop.getProperty("productName");
		assertEquals(productName,expectedProductName);
		
		Boolean isAddToCartButtonDisplayed = inventoryPage.isAddToCartButtonDisplayed();
		ExtentManager.logStep("IS Add to Cart button displayed: " + isAddToCartButtonDisplayed);
		assert(isAddToCartButtonDisplayed);
		
		String priceOnInventory = inventoryPage.getProductPrice();
		ExtentManager.logStep("Product price on inventory: " + priceOnInventory);
		ExtentManager.logStep("======================================================================="  );
		System.out.println();

	}

}
