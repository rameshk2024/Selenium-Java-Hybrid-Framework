package com.seleniumframework.utilities;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer{
	private static int retryCount=0; // Number of retry count
	private static final int maxRetryCount = 0; // Maximum no retry count

	@Override
	public boolean retry(ITestResult result) {
		if(retryCount < maxRetryCount) {
			retryCount++;
			return true; // Retry the test
		}
		return false;
	}

}
