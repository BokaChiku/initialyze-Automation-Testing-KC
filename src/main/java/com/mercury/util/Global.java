/**
 * This class contains all methods which we are going to use in this framework
 */

package com.mercury.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import enums.BrowserNames;

/**
 * @author Kunal Chavan
 * @implNote I have added common functions in this class
 * @since 04/28/2020
 * @version 1.0
 */

public class Global
{
	private WebDriver driver;
	private JavascriptExecutor jS;
	private WebDriverWait wait;
	private File file;
	private ChromeOptions options;
	private Properties prop;
	private FileInputStream input;
	private Map<String, Object> prefs;
	private DesiredCapabilities capablities;
	private DesiredCapabilities caps;
	private ChromeDriverService service;
	private FirefoxProfile profile;
	private InternetExplorerOptions optionsIE;
	private Pattern pat;
	private String emailRegex;
	private Select selectObj;

	/**
	 * driver() - This method returns the object of WebDriver.
	 * 
	 * @return WebDriver
	 * @implNote As we defined browser value in the config.properties this method
	 *           will create an object of WebDriver. This method will check
	 *           config.properties browser name with enum browser names and as per
	 *           match it will execute the code in the switch statement.
	 */

	public WebDriver driver()
	{
		prop = readProperties();
		BrowserNames[] browserNames = BrowserNames.values();
		for (BrowserNames browser : browserNames)
		{
			if (prop.getProperty("browser").equals(browser.name()))
			{
				switch (prop.getProperty("browser"))
				{
					case "Chrome":
					case "CHROME":
					case "GoogleChrome":
					case "googleChrome":
					case "googlechrome":
					case "chrome":
						options = new ChromeOptions();
						options.addArguments("--start-maximized");
						options.addArguments("--disable-web-security");
						options.addArguments("--no-proxy-server");
						options.addArguments("disable-infobars");
						options.addArguments("--disable-browser-side-navigation");
						options.addArguments("enable-automation");
						options.addArguments("--aggressive-cache-discard");
						options.addArguments("--disable-cache");
						options.addArguments("--disable-application-cache");
						options.addArguments("--window-size=1920,1080");
						options.addArguments("--incognito");
						options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
								UnexpectedAlertBehaviour.IGNORE);
						options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
						prefs = new HashMap<String, Object>();
						prefs.put("credentials_enable_service", false);
						prefs.put("profile.password_manager_enabled", false);
						options.setExperimentalOption("prefs", prefs);
						capablities = DesiredCapabilities.chrome();
						capablities.setCapability(ChromeOptions.CAPABILITY, options);
						capablities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
						service = new ChromeDriverService.Builder()
								.usingDriverExecutable(new File(
										System.getProperty("user.dir") + "/drivers/chromedriver/chromedriver.exe"))
								.usingAnyFreePort().build();
						options.merge(capablities);
						System.setProperty("webdriver.chrome.driver",
								System.getProperty("user.dir") + "/drivers/chromedriver/chromedriver.exe");
						driver = new ChromeDriver(service, options);
						driver.get(prop.getProperty("websiteURL"));
						driver.manage().window().maximize();
						break;
					case "FF":
					case "Firefox":
					case "FireFox":
					case "FIREFOX":
					case "MFirefox":
					case "firefox":
						profile = new FirefoxProfile();
						profile.setAcceptUntrustedCertificates(false);
						profile.setAssumeUntrustedCertificateIssuer(false);
						profile.setPreference("browser.download.folderList", 2);
						profile.setPreference("browser.helperApps.alwaysAsk.force", false);
						profile.setPreference("browser.download.manager.showWhenStarting", false);
						FirefoxOptions optionsFF = new FirefoxOptions();
						optionsFF.setProfile(profile);
						System.setProperty("webdriver.gecko.driver",
								System.getProperty("user.dir") + "/drivers/geckodriver/geckodriver.exe");
						driver = new FirefoxDriver(optionsFF);
						driver.get(prop.getProperty("websiteURL"));
						driver.manage().window().maximize();
						break;
					default:
						caps = DesiredCapabilities.internetExplorer();
						caps.setCapability("disable-popup-blocking", true);
						caps.setCapability("unexpectedAlertBehaviour", "accept");
						caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
						caps.setJavascriptEnabled(true);
						caps.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS,true);
						optionsIE = new InternetExplorerOptions(caps);
						optionsIE.disableNativeEvents();
						optionsIE.enablePersistentHovering();
						optionsIE.introduceFlakinessByIgnoringSecurityDomains();
						optionsIE.ignoreZoomSettings();
						System.setProperty("webdriver.ie.driver",
								System.getProperty("user.dir") + "/drivers/iedriver/IEDriverServer.exe");
						driver = new InternetExplorerDriver(optionsIE);
						driver.manage().window().maximize();
						driver.get(prop.getProperty("websiteURL"));
						driver.manage().window().maximize();
						break;
				}
			}
		}
		return driver;
	}

	/**
	 * readProperties() - This method returns the object of Properties. This method
	 * is also check if the file is exxisting on the given location.
	 * 
	 * @return Properties
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public Properties readProperties()
	{
		file = new File(System.getProperty("user.dir") + "/configuration file/config.properties");
		if (file.exists() == true)
		{
			try
			{
				input = new FileInputStream(file);

			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			prop = new Properties();
			try
			{
				prop.load(input);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return prop;
	}

	/**
	 * wait() - This method returns the object of WebDriverWait.
	 * 
	 * @param driver - This method need WebDriver parameter.
	 * @return WebDriverWait
	 */

	public WebDriverWait wait(WebDriver driver)
	{
		wait = new WebDriverWait(driver, 200);
		return wait;
	}

	/**
	 * jsReturn() - This method returns the object of JavascriptExecutor.
	 * 
	 * @param driver - This method need WebDriver parameter.
	 * @return JavascriptExecutor
	 */

	public JavascriptExecutor jsReturn(WebDriver driver)
	{
		jS = (JavascriptExecutor) driver;
		return jS;
	}

	/**
	 * driverQuit() - This method is used to quit the browser.
	 * 
	 * @param driver - This method need WebDriver parameter.
	 * @return void
	 */

	public void driverQuit(WebDriver driver)
	{
		driver.quit();
	}

	/**
	 * highLightElement() - This method is used to highlight an element on the web
	 * page.
	 * 
	 * @param element - This method needs WebElement parameter.
	 * @param driver  - This method needs WebDriver parameter.
	 * @return void
	 */

	public void highLightElement(WebElement element, WebDriver driver)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style','background: yellow; border: 2px solid red;');", element);
	}

	/**
	 * select() - This method is used to return an object of the Select.
	 * 
	 * @param element - This method needs WebElement parameter.
	 * @return Select
	 */

	public Select select(WebElement element)
	{
		selectObj = new Select(element);
		return selectObj;
	}

	/**
	 * isValidEmail() - This method is used to check whether an email is valid or
	 * not. If the email is valid it returns the email and if the email is not valid
	 * then it throw an object of user-defined exception.
	 * 
	 * @param email - This method needs String parameter.
	 * @return String
	 * @throws InvalidEmailException
	 */

	public String isValidEmail(String email) throws InvalidEmailException
	{
		emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

		pat = Pattern.compile(emailRegex);
		if (pat.matcher(email).matches())
		{
			return email;
		}
		else
		{
			throw new InvalidEmailException("Invalid Email");
		}
	}
}
