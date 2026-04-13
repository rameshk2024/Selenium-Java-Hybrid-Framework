package com.seleniumframework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.seleniumframework.actiondriver.ActionDriver;
import com.seleniumframework.base.Base;

public class LoginPage {

	private ActionDriver actions;
	
	// Define all the locators using By class
	private By userNameInputBox = By.id("user-name");
	private By passwordInputBox = By.name("password");
	private By loginButton = By.xpath("//input[@class=\"submit-button btn_action\"]");
	private By loginError = By.xpath("//div[@class=\"error-message-container error\"]/h3");
	
	// Constructor to initialize action driver object
	public LoginPage(WebDriver driver){
		this.actions = Base.getActionDriver();		
	}
	
	// Method to login to application
	public void login(String username, String password) {
		actions.enterText(userNameInputBox, username);
		actions.enterText(passwordInputBox, password);
		actions.click(loginButton);
	}
	
	// Method to get Error message invalid login
	public String getErrorMessage() {
		return actions.getText(loginError);
	}
	
	// Method to check if error is displayed
	public boolean isErrorMessageDisplayed() {
		return actions.isDisplayed(loginError);
	}
	
	public boolean isLoginButtonDisplayed() {
		return actions.isDisplayed(loginButton);
	}
	
	

}
