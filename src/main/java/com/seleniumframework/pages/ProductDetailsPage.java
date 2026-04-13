package com.seleniumframework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.seleniumframework.actiondriver.ActionDriver;
import com.seleniumframework.base.Base;

public class ProductDetailsPage {
	private ActionDriver actions;
	// Locators using By class

	private By addToCartButton = By.xpath("//button[@data-test=\"add-to-cart\"]");
	private By productPrice = By.xpath("//div[@data-test=\"inventory-item-price\"]");
	private By productName = By.xpath("//div[@data-test=\"inventory-item-name\"]");
	private By removeButton = By.xpath("//button[@data-test=\"remove\"]");
	private By shoppingCartLink = By.xpath("//div[@id='shopping_cart_container']/a");

	// Constructor to initialize action driver object
	public ProductDetailsPage(WebDriver driver) {
			this.actions = Base.getActionDriver();
		}

	public String getProductName() {
		return actions.getText(productName);
	}

	public String getProductPrice() {
		return actions.getText(productPrice);
	}

	public String getAddToCartButtonText() {
		return actions.getText(addToCartButton);
	}

	public void clickAddToCartButton() {
		actions.click(addToCartButton);
	}

	public String getRemoveButtonText() {
		return actions.getText(removeButton);
	}

	public void clickShoppingCartLink() {
		actions.click(shoppingCartLink);
	}

}
