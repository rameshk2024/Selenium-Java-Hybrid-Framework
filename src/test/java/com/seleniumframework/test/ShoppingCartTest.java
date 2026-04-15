package com.seleniumframework.test;

import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.seleniumframework.actiondriver.ActionDriver;
import com.seleniumframework.base.Base;
import com.seleniumframework.pages.InventoryPage;
import com.seleniumframework.pages.LoginPage;
import com.seleniumframework.pages.ProductDetailsPage;
import com.seleniumframework.pages.ShoppingCartPage;
import com.seleniumframework.utilities.EncryptData;
import com.seleniumframework.utilities.ExtentManager;

public class ShoppingCartTest extends Base {

	private LoginPage loginPage;
	private InventoryPage inventoryPage;
	private ProductDetailsPage productDetailsPage;
	private ShoppingCartPage shoppingCartPage;
	private ActionDriver actions;
	private static final Logger logger = Base.logger;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		inventoryPage = new InventoryPage(getDriver());
		productDetailsPage = new ProductDetailsPage(getDriver());
		shoppingCartPage = new ShoppingCartPage(getDriver());
		actions = Base.getActionDriver();
		;
	}

	@Test(description = "Verify that shopping cart details")
	public void verifyCartDetails() throws Exception {
		//ExtentManager.startTest("Verify Shopping Cart Details");
		String decryptedUsername = EncryptData.decryptData(prop.getProperty("valid_userName"));
		String decryptedPassword = EncryptData.decryptData(prop.getProperty("valid_password"));
		loginPage.login(decryptedUsername, decryptedPassword);

		inventoryPage.clickProductName();

		String productNameOnProductDetails = productDetailsPage.getProductName();
		ExtentManager.logStep("Product name on Product Details : " + productNameOnProductDetails);
		String priceOnProductDetails = productDetailsPage.getProductPrice();
		ExtentManager.logStep("Product price on Product Details: " + priceOnProductDetails);
		productDetailsPage.clickAddToCartButton();

		// actions.scrollToTop();
		productDetailsPage.clickShoppingCartLink();

		String productNameOnCart = shoppingCartPage.getProductName();
		ExtentManager.logStep("Product Name on Shopping Cart: " + productNameOnCart);
		Assert.assertEquals(productNameOnProductDetails, productNameOnCart,
				prop.getProperty("productNameMissmatchError"));
		String ProductPriceOnCart = shoppingCartPage.getProductPrice();
		ExtentManager.logStep("Product Price on Shopping Cart: " + ProductPriceOnCart);
		Assert.assertEquals(priceOnProductDetails, ProductPriceOnCart, prop.getProperty("priceMissmatchError"));
		ExtentManager.logStep("=======================================================================");

	}

}
