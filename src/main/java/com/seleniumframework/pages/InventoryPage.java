package com.seleniumframework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.seleniumframework.actiondriver.ActionDriver;
import com.seleniumframework.base.Base;

public class InventoryPage {

	private ActionDriver actions;
	// Locators using By class

	private By addToCartButton = By.id("add-to-cart-sauce-labs-backpack");
	private By productPrice = By.xpath("//*[@id='inventory_container']/div/div[1]/div[2]/div[2]/div");
	private By productName = By.xpath("//a[@id='item_4_title_link']/div");
	private By sauceDemoLogo = By.xpath("//div[@class='app_logo']");
	private By mainMenu = By.xpath("//div[@class='bm-burger-button']");
	private By logoutButton = By.xpath("//a[contains(text(), 'Logout')]");

	// Constructor to initialize action driver object
	public InventoryPage(WebDriver driver) {
		this.actions = Base.getActionDriver();
	}
	
	// Method to get Product name
	public String getProductName() {
		return actions.getText(productName);
	}
	
	// Method to get Product name
		public String getProductPrice() {
			return actions.getText(productPrice);
		}
	
	// Method to verify that add to cart button is displayed
	public boolean isAddToCartButtonDisplayed() {
		return actions.isDisplayed(addToCartButton);
	}
	
	// Method to verify that sauceDemo logo is displayed
	public boolean isSauceDemoLogoDisplayed() {
		return actions.isDisplayed(sauceDemoLogo);
	}
	
	// Method to click logout Button
	public void logout() {
		actions.click(mainMenu);
		actions.click(logoutButton);
	}
	
	public void clickProductName() {
		actions.click(productName);
	}
}
