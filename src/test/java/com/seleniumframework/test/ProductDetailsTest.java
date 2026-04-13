package com.seleniumframework.test;

//import static com.seleniumframework.base.Base.driver;
import static org.testng.Assert.assertNotEquals;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seleniumframework.base.Base;
import com.seleniumframework.pages.InventoryPage;
import com.seleniumframework.pages.LoginPage;
import com.seleniumframework.pages.ProductDetailsPage;
import com.seleniumframework.utilities.EncryptData;
import com.seleniumframework.utilities.ExtentManager;

public class ProductDetailsTest extends Base{
	
	private LoginPage loginPage;
	private InventoryPage inventoryPage;
	private ProductDetailsPage productDetailsPage;
	private static final Logger logger = Base.logger;
	
	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		inventoryPage = new InventoryPage(getDriver());
		productDetailsPage = new ProductDetailsPage(getDriver());
	}
	
	@Test(description = "Verify that inventory details are displayed")
	public void verifyProductDetails() throws Exception {
		//ExtentManager.startTest("Verify Product Details");
		
		String decryptedUsername = EncryptData.decryptData(prop.getProperty("valid_userName"));
		String decryptedPassword = EncryptData.decryptData(prop.getProperty("valid_password"));
		
		ExtentManager.logStep("Login Page Title: "+ getDriver().getTitle());
		System.out.println("Login Page Title: "+ getDriver().getTitle());
		loginPage.login(decryptedUsername,decryptedPassword );
		ExtentManager.logStep("Home Page Title: "+ getDriver().getTitle());
		ExtentManager.logStep("Page URL: " + getDriver().getCurrentUrl());
		
		String nameOnInventory = inventoryPage.getProductName();
		ExtentManager.logStep("Product name on inventory: " + nameOnInventory);
		String priceOnInventory = inventoryPage.getProductPrice();
		ExtentManager.logStep("Product price on inventory: " + priceOnInventory);
		inventoryPage.clickProductName();
		
		String productNameOnProductDetails = productDetailsPage.getProductName();
		ExtentManager.logStep("Product name on Product Details : " + productNameOnProductDetails);
		Assert.assertEquals(nameOnInventory, productNameOnProductDetails, prop.getProperty("productNameMissmatchError"));
		String priceOnProductDetails = productDetailsPage.getProductPrice();
		ExtentManager.logStep("Product price on Product Details: " + priceOnProductDetails);
		Assert.assertEquals(priceOnInventory, priceOnProductDetails, prop.getProperty("priceMissmatchError"));
		String addToCartText =productDetailsPage.getAddToCartButtonText();
		ExtentManager.logStep("Add To Cart Button Text: " + addToCartText);
		productDetailsPage.clickAddToCartButton();
		
		String addToCartButtonTextAfterClick = productDetailsPage.getRemoveButtonText();
		ExtentManager.logStep("Add To Cart Button Text after click: " + addToCartButtonTextAfterClick);
		assertNotEquals(addToCartText, addToCartButtonTextAfterClick, prop.getProperty("addToCartButtonTextError"));
		ExtentManager.logStep("======================================================================="  );
		System.out.println();


	}

}
