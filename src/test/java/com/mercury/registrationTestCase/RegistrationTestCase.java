/**
 * This class contains our test cases for registration form like smoke,
 * functional etc..
 */
package com.mercury.registrationTestCase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.mercury.module.registration.Registration;
import com.mercury.util.Global;
import enums.BrowserNames;

/**
 * @author Kunal Chavan
 * @implNote I'm creating two test cases for registration form 1) Smoke Testing 2)
 *           Functional/End-to-end Testing
 * @since 04/28/2020
 * @version 1.0
 */

public class RegistrationTestCase
{
	private WebDriver driver;
	private Global globalObj;
	private Registration reg;
	private Properties prop;
	private ExtentReports extent;
	private ExtentHtmlReporter htmlReporter;
	private ExtentTest extentLogger;
	private String screenshotPath;
	private String dateName;
	private File scrFile;

	/**
	 * To generate logs with Apache log4j I am writhing below lines code. I create
	 * the object of the Logger and in the static block, I configure the
	 * log4j.properties file.
	 */

	public static Logger log = Logger.getLogger("Registration Test Case");
	static
	{
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/configuration file/Log4j.properties");
	}

	/**
	 * beforeClass() - This method is used to initialize/setup which is need for out
	 * test case. This will execute first in this test case.
	 * 
	 * @return void
	 */

	@BeforeClass(alwaysRun = true)
	public void beforeClass()
	{
		log.info("Registration Test Case Starts Here");
		globalObj = new Global();
		driver = globalObj.driver();
		prop = globalObj.readProperties();
		reg = new Registration(driver);
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/reports/registration/RegistrationTestCase.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Author: ", prop.getProperty("author"));
		extent.setSystemInfo("Operating System: ", prop.getProperty("operatingSystem"));
		extent.setSystemInfo("Operating System Version: ", prop.getProperty("operatingSystemVersion"));
		extent.setSystemInfo("Browser Name: ", prop.getProperty("browser"));
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
						extent.setSystemInfo("Browser Version: ", prop.getProperty("chromeVersion"));
						break;
					case "FF":
					case "Firefox":
					case "FireFox":
					case "fireFox":
					case "FIREFOX":
					case "MFirefox":
					case "firefox":
						extent.setSystemInfo("Browser Version: ", prop.getProperty("firefoxVersion"));
						break;
					default:
						extent.setSystemInfo("Browser Version: ", prop.getProperty("ieVersion"));
						break;
				}
			}
		}
		extent.setSystemInfo("Selenium Version: ", prop.getProperty("selenium"));
		extent.setSystemInfo("Java Version: ", prop.getProperty("javaVersion"));
		extent.setSystemInfo("Eclipse IDE Version: ", prop.getProperty("eclipseIDEVersion"));
		htmlReporter.config().setReportName("Registration Test Case");
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a'('zzz')'");
		htmlReporter.loadXMLConfig(System.getProperty("user.dir") + "/configuration file/extent-config.xml");
	}

	/**
	 * registrationSmoke() - This method is used to perform smoke round of the
	 * registration form. I have set priority = 1 for this method.
	 * 
	 * @return - void
	 */

	@Test(groups = { "Smoke" }, priority = 1)
	public void registrationSmoke()
	{

		log.info("Test Case 1 (Smoke Testing): Check Registration Form is Saving Without Filling Any Information.");
		extentLogger = extent.createTest(
				"Test Case 1 (Smoke Testing): Check Registration Form is Saving Without Filling Any Information.");
		reg.submit();
	}

	/**
	 * registrationFunctional() - This method is used to perform Functional or
	 * End-to-end testing round of the registration form. I have set priority = 2
	 * for this method. i.e registrationSmoke() will execute first.
	 * 
	 * @return - void
	 */

	@Test(groups = { "Functional", "End-to-end" }, priority = 2)
	public void registrationFunctional()
	{
		log.info(
				"Test Case 2 (Functional or End-to-end Testing): Check Registration Form Functionality By Filling All Information And Verify End-to-end Testing Cycle.");
		extentLogger = extent.createTest(
				"Test Case 2 (Functional or End-to-end Testing): Check Registration Form Functionality By Filling All Information And Verify End-to-end Testing Cycle.");
		reg.contactInformation();
		reg.mailingInformation();
		reg.userInformation();
		reg.verifyRegistration();
		reg.verifyLoginByRegistrationCredentials();
	}

	/**
	 * takeScreenShotOnFailure() method is used to take a screenshot if the script
	 * fails and attach the screenshot in the report. This method will be executed
	 * after the each test finish.
	 * 
	 * @param testResult - This method requires TestNG listener ITestResult
	 * @return - void
	 * @throws - IOException
	 */

	@AfterMethod(alwaysRun = true)
	public void takeScreenShotOnFailure(ITestResult testResult) throws IOException
	{
		if (testResult.getStatus() == ITestResult.FAILURE)
		{
			extentLogger.log(Status.FAIL,
					MarkupHelper.createLabel(testResult.getName() + " - Test Case Failed", ExtentColor.RED));
			extentLogger.log(Status.FAIL,
					MarkupHelper.createLabel(testResult.getThrowable() + " - Test Case Failed", ExtentColor.RED));
			dateName = new SimpleDateFormat("dd MMMM yyyy zzzz").format(new Date());
			scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			screenshotPath = System.getProperty("user.dir") + "/screenshots/registration/" + testResult.getName()
					+ dateName + "_" + Arrays.toString(testResult.getParameters()) + ".png";
			FileUtils.copyFile(scrFile, new File(screenshotPath));
			extentLogger.fail(
					"Test Case Failed Screenshot Is Below " + extentLogger.addScreenCaptureFromPath(screenshotPath));
		}
		else if (testResult.getStatus() == ITestResult.SKIP)
		{
			extentLogger.log(Status.SKIP,
					MarkupHelper.createLabel(testResult.getName() + " - Test Case Skipped", ExtentColor.ORANGE));
		}
		else if (testResult.getStatus() == ITestResult.SUCCESS)
		{
			extentLogger.log(Status.PASS,
					MarkupHelper.createLabel(testResult.getName() + " Test Case Passed", ExtentColor.GREEN));
		}

	}

	/**
	 * afterClass() - This method is used to writes test information from the
	 * started reporters to their output view. This method will quit the browser.
	 * 
	 * @return - void
	 */

	@AfterClass
	public void afterClass()
	{
		log.info("Registration Test Case Ends Here");
		extent.flush();
		reg.driverQuit();
	}

}
