package unittests;

import jsystem.framework.TestProperties;
import junit.framework.SystemTestCase4;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.jsystem.selenium.SelenuimUtils;
import com.jsystem.selenium.WebDriverBackwardCompatibilitySystemObject;
import com.jsystem.selenuim.SeleniumClient;

public class SeleniumRcTestsWithWebDriver extends SystemTestCase4 {
	
	public WebDriverBackwardCompatibilitySystemObject seleniumSystemObject;
	public SeleniumClient seleniumClient;
	private String googleUrl ="http://www.google.com";
	private String textXpathLocator = "//*[contains(@id,'lst-ib')]";
	private String submitXpathLocator = "//input[contains(@class,'lsb')]";
	
	@Before
	public void setUp() throws Exception {
		seleniumSystemObject = (WebDriverBackwardCompatibilitySystemObject) system.getSystemObject("seleniumSystemObject");
		seleniumClient = seleniumSystemObject.getSeleniumClient();
	}
	
	@Test
	@TestProperties()
	public void googleCheeseSeleniumRcAndSelenium2() throws Exception{
		seleniumRcApi();
		webDriverApi();
	}
	
	@Test
	@TestProperties()
	public void googleCheeseOnlySeleniumRc() throws Exception{
		seleniumRcApi();
	}
	
	@Test
	@TestProperties()
	public void googleCheeseOnlySelenium2() throws Exception{
		seleniumRcApi();
	}

	/**
	 * WebDriver(Selenium2)  Api
	 * 
	 * 1. navigate to google
	 * 2. type "WebDriver"
	 * 3. submit
	 */
	private void webDriverApi() {
		WebDriver driver = SelenuimUtils.getWebDriverFromSeleniumClient(seleniumClient);
		
		driver.get(googleUrl);
		WebElement textBox = driver.findElement(By.xpath(textXpathLocator));
		textBox.sendKeys("WebDriver");
		textBox.submit();
		
	}

	/**
	 * Selenium Rc Api
	 * 
	 * 1. navigate to google
	 * 2. type "Selenium Rc"
	 * 3. submit
	 */
	private void seleniumRcApi() {
		 // And now use this to visit Google
		seleniumClient.open(googleUrl);
		
		seleniumClient.type(textXpathLocator,"Selenium Rc");
		seleniumClient.click(submitXpathLocator,true);
		
	}

}
