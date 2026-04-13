package com.seleniumframework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.seleniumframework.actiondriver.ActionDriver;
import com.seleniumframework.base.Base;

public class ShoppingCartPage {

	private ActionDriver actions;
	// Locators using By class

	private By productPrice = By.xpath("//div[@data-test=\"inventory-item-price\"]");
	private By productName = By.xpath("//*[@id='item_4_title_link']/div");

	// Constructor to initialize action driver object
	public ShoppingCartPage(WebDriver driver) {
		this.actions = Base.getActionDriver();
	}
	
	public String getProductName() {
		return actions.getText(productName);
	}

	public String getProductPrice() {
		return actions.getText(productPrice);
	}
	
	

}
