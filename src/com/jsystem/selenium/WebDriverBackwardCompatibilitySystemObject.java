package com.jsystem.selenium;

// Copy com.jsystem.selenium.WebDriverBackwardCompatibilitySystemObject to the SUT file

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;

import com.jsystem.selenuim.BrowserType;
import com.jsystem.selenuim.SeleniumClient;
import com.jsystem.webdriver.WebDriverSystemObject;
import com.jsystem.webdriver.WebDriverWrapper;
import com.thoughtworks.selenium.Selenium;

public class WebDriverBackwardCompatibilitySystemObject extends WebDriverSystemObject {

	protected String serverIp = "localhost";
	protected String webBrowser;
	protected int port = 4444;
	protected String extFile = null; // Js ext-library
	protected int retries = 5;
	protected SeleniumClient client;
	protected String autoMap = null;
	protected boolean autoMapEnabled = false;
	protected String propFile = null;
	protected boolean isScreenshot = true;
	
	/**
	 * init the WebDriverSystemObject SystemObject.
	 */
	public void init() throws Exception {
		super.init();
	}

	/**
	 * Creates Selenium client using the SUT parameters
	 * 
	 * @return SeleniumClient
	 * @throws Exception
	 */
	@Override
	public WebDriverWrapper getBrowserInstance() throws Exception {
		WebDriverWrapper webDriverInstance = null;

		if (webBrowser != null) {
			webDriverInstance = webDriverFactory(BrowserType.getBrowserTypeFromString(webBrowser));
		} else {
			throw new Exception(
					"get Browser instance init Exception,the webDeriver type or the webBrowser type is not init");
		}
		return webDriverInstance;
	}

	/**
	 * return SeleniumClient browser instance
	 * 
	 * @return
	 * @throws Exception
	 */
	public SeleniumClient getSeleniumClient() throws Exception {

		SeleniumClient client = null;
		report.report("Init the BackedSelenium Web-Driver(Selenium RC API,Selenium V1.X) and not the WebDriver(Selenium 2 API).");
		client = getWebDriverBackedSelenium(webBrowser);
		return client;

	}

	/**
	 * registerScreenshot will add will listen to WebDriver events (like before
	 * and after navigation) and add a screen Screenshot if the user wish to
	 * override the default class that handle the
	 * events(WebDriverScreenshotEventHandler): 1.create your own class (that
	 * implements WebDriverEventListener interface) 2.create new instance in
	 * registerScreenshot function 3.use WebDriver API for register a event
	 * handler - driver.register(WebDriverEventListener);
	 */
	@Override
	protected void registerScreenshot(WebDriverWrapper driver) {
		super.registerScreenshot(driver);
	}

	private void setSeleniumWrapperConfigSettings(SeleniumWrapper wrapper) {
		if (isScreenshot) {
			wrapper.setScreenShot(isScreenshot);
			wrapper.setScreenShotPath(screenShotPath);
			wrapper.setScreenShotFolderName(screenShotFolderName);
		}

	}

	/**
	 * getWebDriverBackedSelenium function will return SeleniumClient instance
	 * from browser type -can be parameter from the SUT file this function will
	 * create the backward compatibility for WebDriver project.
	 * 
	 * @param browserType
	 *            -selenium browser type(e.g. *firefox , *googlechrome,
	 *            *iexplore...)
	 * @return SeleniumClient - the backward compatibility selenium instance
	 *         from Web project.
	 * @throws Exception
	 */
	private SeleniumClient getWebDriverBackedSelenium(String browserType) throws Exception {

		if (driver != null) {

			Selenium selenium = new WebDriverBackedSelenium(driver, domain);
			SeleniumClient client = new SeleniumClient(serverIp, port, webBrowser, domain);
			SeleniumWrapper wrapper = new SeleniumWrapper(driver, domain, selenium);
			setSeleniumWrapperConfigSettings(wrapper);
			client.setSeleniumDispatcher(wrapper);
			setSeleniumClientConfigSettings(client);
			return client;

		} else {
			String error = "can't create backward compatibility for the Selenium RC API without init the webDriver,check the BrowserType(="
					+ browserType
					+ ") value from the SUT file.\n\n"
					+ BrowserType.getAllSupportedBrowserTypesAsString();
			throw new IllegalArgumentException(error);
		}

	}

	/**
	 * set the the relevant parameters for the SeleniumClient(backward
	 * compatibility)
	 * 
	 * @param client
	 * @throws Exception
	 */
	private void setSeleniumClientConfigSettings(SeleniumClient client) throws Exception {

		client.setAutoMapEnabled(autoMapEnabled);
		client.setAutoMapPrefix(autoMap);
		client.setRetries(retries);
		client.setScreenShot(isScreenshot);
		client.setScreenShotPath(screenShotPath + "\\" + screenShotFolderName);
		if (seleniumTimeOut == null) {
			seleniumTimeOut = client.getTimeout();
		}
		client.setTimeout(seleniumTimeOut);
		if (extFile != null) {
			client.setExtensionJs(extFile);
		}

		client.setAutoMapEnabled(autoMapEnabled);
		client.setAutoMapPrefix(autoMap);

		// set browser Type
		client.setBrowser((BrowserType.getBrowserTypeFromString(webBrowser)));
	}

	/**
	 * create WebDriver instance by the given BrowserType option.
	 * 
	 * @param type
	 *            - the browser type
	 * @return WebDriver instnace
	 */
	private WebDriver getWebDriverFactory(BrowserType type) {

		WebDriver webDriverInstance = null;
		try {
			switch (type) {
			case GOOGLE_CHROME:
			case CHROME:
				webDriverInstance=	getChromeDriver();
				break;

			case FIREFOX3:
			case FIREFOX2:
			case FIREFOX:
				webDriverInstance = getFirefoxWebDriver();
				break;

			case IEXPLORE_PROXY:
			case IEXPLORE:
				webDriverInstance = getInternetExplorerWebDriver();
				break;

			case HTML_UNIT:
				webDriverInstance = getHtmlUnitDriver();
				break;

			case OPERA:
				webDriverInstance = getOperaDriver();
				break;

			default:
				throw new IllegalArgumentException("BrowserType(" + type.toString() + ") unsupported.");

			}
		} catch (IllegalArgumentException e) {
			report.report(e.getMessage() + BrowserType.getAllSupportedBrowserTypesAsString(), false);
			e.printStackTrace();
			throw e;
		}
		if (webDriverInstance == null) {
			String error = "Faild to init the  WebDriverBackwardCompatibilitySystemObject.";
			report.report(error, false);
			throw new IllegalArgumentException(error);
		}
		return webDriverInstance;
	}

	protected WebDriverWrapper webDriverFactory(BrowserType type) throws Exception {

		WebDriver webDriverInstance = getWebDriverFactory(type);
		if (null == webDriverInstance) {
			throw new Exception("getWebDriverFactory init Exception ,webDriverInstance was not init");
		}
		return new WebDriverWrapper(webDriverInstance);

	}

	public String getWebBrowser() {
		return webBrowser;
	}

	/**
	 * Sets the type of the browser
	 */
	public void setWebBrowser(String webBrowser) {
		this.webBrowser = webBrowser;
	}

	public String getExtFile() {
		return extFile;
	}

	/**
	 * Sets the external javascript extensions file. This file will be used by
	 * the Selenium server.
	 */
	public void setExtFile(String extFile) {
		this.extFile = extFile;
	}

	public int getPort() {
		return port;
	}

	/**
	 * Set the Selenium server port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public String getServerIp() {
		return serverIp;
	}

	/**
	 * Sets the RC server ip address
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getRetries() {
		return retries;
	}

	/**
	 * the number of retries of finding/action elements
	 * 
	 * @param retries
	 */
	public void setRetries(int retries) {
		this.retries = retries;
	}

	public String getAutoMap() {
		return autoMap;
	}

	/**
	 * Sets the link auto map mode. This mode allows using not links but
	 * directly properties. For example: jsystem.documents.tutorial. In this
	 * case the <code>autoMap</code> should be <b>jsystem</b>
	 */
	public void setAutoMap(String autoMap) {
		this.autoMap = autoMap;
	}

	public boolean isAutoMapEnabled() {
		return autoMapEnabled;
	}

	public void setAutoMapEnabled(boolean autoMapEnabled) {
		this.autoMapEnabled = autoMapEnabled;
	}

	public String getPropFile() {
		return propFile;
	}

	/**
	 * Sets properties file. This file should contain the links mapping the
	 * following format: property=XPath/Html/Javascript
	 */
	public void setPropFile(String propFile) {
		this.propFile = propFile;
	}

	public boolean isScreenshot() {
		return isScreenshot;
	}

	public void setScreenshot(boolean isScreenshot) {
		this.isScreenshot = isScreenshot;
	}
	
	

}
