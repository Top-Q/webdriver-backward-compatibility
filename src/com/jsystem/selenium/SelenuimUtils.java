package com.jsystem.selenium;

import jsystem.framework.report.ListenerstManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;

import com.jsystem.selenuim.SeleniumClient;
import com.thoughtworks.selenium.Selenium;

public class SelenuimUtils {

	public static WebDriver getWebDriverFromSeleniumClient(SeleniumClient client) {
		WebDriver driverInstance = null;
		
		try {
			// Get the underlying WebDriver implementation back. This will refer
			// to the
			// same WebDriver instance as the "driver" variable above.
			
			Selenium selenium= client.getSeleniumDispatcher();
			WebDriverBackedSelenium webDriverBackedSelenium=((WebDriverBackedSelenium) selenium);
			driverInstance = webDriverBackedSelenium.getWrappedDriver();
			//driverInstance = webDriverBackedSelenium.getUnderlyingWebDriver();
			return driverInstance;

		} catch (Throwable e) {
			 ListenerstManager.getInstance().report("Error in getting webDriver(Selenium 2) from the Selenium Client(Selenium 1)",
					e.getMessage(), jsystem.framework.report.Reporter.WARNING);
		}
		return driverInstance;
	}
}
