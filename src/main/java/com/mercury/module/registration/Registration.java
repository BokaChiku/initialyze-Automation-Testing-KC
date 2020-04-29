/**
 * In this registration class file, I've written all java logic, finding xpaths,
 * assert on specific conditions.
 */
package com.mercury.module.registration;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.mercury.interfaces.RegistrationInterface;
import com.mercury.util.Global;
import com.mercury.util.InvalidEmailException;

/**
 * @author Kunal Chavan 
 * @since 04/28/2020
 * @version 1.0
 */

public class Registration implements RegistrationInterface
{
	private WebDriver driver;
	private Global globalObj;
	private Properties prop;
	private String actualNote;
	private String validEmail;
	private String isEmailAlreadyAdded;
	private int count;
	private FileOutputStream out;
	private String[] splitString;
	private String newString;
	private String firstNameValue;
	private String lastNameValue;

	@FindBy(how = How.XPATH, using = ".//input[@id='email']")
	private WebElement userName;

	@FindBy(how = How.XPATH, using = ".//input[@name='register']")
	private WebElement submitButton;

	@FindBy(how = How.XPATH, using = ".//table//table//td//p//a//font//b[text()]")
	private WebElement note;

	@FindBy(how = How.XPATH, using = ".//input[@name='firstName']")
	private WebElement firstName;

	@FindBy(how = How.XPATH, using = ".//input[@name='lastName']")
	private WebElement lastName;

	@FindBy(how = How.XPATH, using = ".//input[@name='phone']")
	private WebElement phone;

	@FindBy(how = How.XPATH, using = ".//input[@id='userName']")
	private WebElement email;

	@FindBy(how = How.XPATH, using = ".//input[@name='address1']")
	private WebElement addressTextboxFirst;

	@FindBy(how = How.XPATH, using = ".//input[@name='address2']")
	private WebElement addressTextboxSecound;

	@FindBy(how = How.XPATH, using = ".//input[@name='city']")
	private WebElement city;

	@FindBy(how = How.XPATH, using = ".//input[@name='state']")
	private WebElement state;

	@FindBy(how = How.XPATH, using = ".//input[@name='postalCode']")
	private WebElement postalCode;

	@FindBy(how = How.XPATH, using = ".//select[@name='country']")
	private WebElement country;

	@FindBy(how = How.XPATH, using = ".//input[@name='password']")
	private WebElement password;

	@FindBy(how = How.XPATH, using = ".//input[@name='confirmPassword']")
	private WebElement confirmPassword;

	@FindBy(how = How.XPATH, using = ".//table//tbody//tr//td//table//tbody//tr//td[1]//p[1]//font//b[text()]")
	private WebElement verifyFirstNameLastName;

	@FindBy(how = How.XPATH, using = ".//table//tbody//tr//td//table//tbody//tr//td//table//tbody//tr//td//p[2]//font[text()]")
	private WebElement secondParagraph1;

	@FindBy(how = How.XPATH, using = ".//table//tbody//tr//td//table//tbody//tr//td//table//tbody//tr//td//p[2]//font[text()]//a[1]")
	private WebElement secondParagraph2;

	@FindBy(how = How.XPATH, using = ".//table//tbody//tr//td//table//tbody//tr//td//table//tbody//tr//td//p[2]//font[text()]//a[2]")
	private WebElement secondParagraph3;

	@FindBy(how = How.XPATH, using = ".//table//tbody//tr//td//table//tbody//tr//td//table//tbody//tr//td//p[3]//font[text()]")
	private WebElement verifyUserName;

	@FindBy(how = How.LINK_TEXT, using = "sign-in")
	private WebElement signInLink;

	@FindBy(how = How.XPATH, using = ".//input[@name='userName']")
	private WebElement loginUserName;

	@FindBy(how = How.XPATH, using = ".//input[@name='password']")
	private WebElement loginPassword;

	@FindBy(how = How.XPATH, using = ".//input[@name='login']")
	private WebElement loginButton;

	@FindBy(how = How.LINK_TEXT, using = "SIGN-OFF")
	private WebElement signOff;

	@FindBy(how = How.LINK_TEXT, using = "registration form")
	private WebElement registrationFormLink;

	@FindBy(how = How.LINK_TEXT, using = "REGISTER")
	private WebElement register;

	/**
	 * Registration() - This is parameterized constructor of the Registration class.
	 * 
	 * @param driver - This constructor needs WebDriver parameter.
	 */

	public Registration(WebDriver driver)
	{
		globalObj = new Global();
		this.driver = driver;
		PageFactory.initElements(driver, this);
		prop = globalObj.readProperties();
	}

	/**
	 * submit() - This method is used for smoke testing. in this method I am
	 * directly submitting the registration form without filling mandatory or any
	 * other information.
	 * 
	 * @return void
	 */

	public void submit()
	{
		globalObj.wait(driver).until(ExpectedConditions.visibilityOf(submitButton));
		submitButton.click();
		globalObj.wait(driver).until(ExpectedConditions.visibilityOf(note));
		actualNote = note.getText();
		globalObj.highLightElement(note, driver);
		Assert.assertEquals(actualNote, prop.getProperty("expectedNote"));
	}

	/**
	 * contactInformation() - This method is used for functional testing. In this
	 * method, I am filling contact information on the registration form. In this
	 * method I am creating every time new email id and validating the provided
	 * email id is valid or invalid.
	 * 
	 * @return void
	 * @throws InvalidEmailException
	 */

	public void contactInformation()
	{
		if (!driver.getCurrentUrl().equals(prop.getProperty("websiteURL")))
			register.click();
		globalObj.wait(driver).until(ExpectedConditions.visibilityOf(firstName));
		firstName.sendKeys(prop.getProperty("firstName"));
		firstNameValue = firstName.getAttribute("value");
		lastName.sendKeys(prop.getProperty("lastName"));
		lastNameValue = lastName.getAttribute("value");
		phone.sendKeys(prop.getProperty("phone"));
		isEmailAlreadyAdded = isEmailAlreadyAdded(prop.getProperty("email"));
		try
		{
			validEmail = globalObj.isValidEmail(isEmailAlreadyAdded);
			email.sendKeys(validEmail);
		}
		catch (InvalidEmailException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * mailingInformation() - This method is used for functional testing. In this
	 * method, I am filling mailing information on the registration form.
	 * 
	 * @return void
	 */

	public void mailingInformation()
	{
		addressTextboxFirst.sendKeys(prop.getProperty("addressTextboxFirst"));
		addressTextboxSecound.sendKeys(prop.getProperty("addressTextboxSecound"));
		city.sendKeys(prop.getProperty("city"));
		state.sendKeys(prop.getProperty("state"));
		postalCode.sendKeys(prop.getProperty("postalCode"));
		globalObj.highLightElement(country, driver);

		/*
		 * In Chrome browser it is considering space after 'States' so I write these two
		 * below lines to make it consistent on all browsers.
		 */

		splitString = globalObj.select(country).getFirstSelectedOption().getText().split(" ");
		newString = splitString[0] + " " + splitString[1];
		Assert.assertEquals(newString, prop.getProperty("defaultCountry"));
	}

	/**
	 * userInformation() - This method is used for functional testing. In this
	 * method, I am filling user information on the registration form. In this
	 * method, I have cross check the password with confirm password and confirm
	 * password with the password.
	 * 
	 * @return void
	 */

	public void userInformation()
	{
		globalObj.jsReturn(driver).executeScript("window.scrollBy(0,250)", "");
		userName.sendKeys(validEmail);
		password.sendKeys(prop.getProperty("password"));
		confirmPassword.sendKeys(prop.getProperty("confirmPassword"));
		globalObj.highLightElement(password, driver);
		Assert.assertEquals(password.getAttribute("value"), confirmPassword.getAttribute("value"));
		globalObj.highLightElement(confirmPassword, driver);
		Assert.assertEquals(confirmPassword.getAttribute("value"), password.getAttribute("value"));
		globalObj.jsReturn(driver).executeScript("arguments[0].click()", submitButton);
	}

	/**
	 * isEmailAlreadyAdded() - This method is used for functional testing. In this
	 * method, I am generating or creating a new email every time and when a new
	 * email is created I am storing count value to the config.properties.
	 * 
	 * @param email - This method needs String parameter.
	 * @return String
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public String isEmailAlreadyAdded(String email)
	{
		try
		{
			count = Integer.parseInt(prop.getProperty("count"));
			out = new FileOutputStream(System.getProperty("user.dir") + "/configuration file/config.properties");
			prop.setProperty("count", (count + 1) + "");
			prop.store(out, null);
			out.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return prop.getProperty("email") + prop.getProperty("count") + prop.getProperty("domain");
	}

	/**
	 * verifyRegistration() - This method is used for functional testing. In this
	 * method, I am verifying registration form information first name, last name
	 * and user name on the next page when we submit the registration form.
	 * 
	 * @return void
	 */

	public void verifyRegistration()
	{
		globalObj.wait(driver).until(ExpectedConditions.visibilityOf(note));
		globalObj.highLightElement(verifyFirstNameLastName, driver);
		Assert.assertEquals(verifyFirstNameLastName.getText(), (prop.getProperty("verifyFirstNameLastName1") + " "
				+ firstNameValue + " " + lastNameValue + prop.getProperty("verifyFirstNameLastName2")));
		globalObj.highLightElement(secondParagraph1, driver);
		globalObj.highLightElement(secondParagraph2, driver);
		globalObj.highLightElement(secondParagraph3, driver);
		Assert.assertEquals(secondParagraph1.getText() + secondParagraph2.getText() + secondParagraph3.getText(),
				prop.getProperty("secondParagraph"));
		globalObj.highLightElement(verifyUserName, driver);
		Assert.assertEquals(verifyUserName.getText(), prop.getProperty("thirdParagraph") + validEmail + ".");
	}

	/**
	 * verifyLoginByRegistrationCredentials() - This method is used for functional
	 * testing. In this method, I am verifying can the user can login with the
	 * registration form user name and password.
	 * 
	 * @return void
	 */

	public void verifyLoginByRegistrationCredentials()
	{
		signInLink.click();
		globalObj.wait(driver).until(ExpectedConditions.urlMatches(prop.getProperty("loginURL")));
		Assert.assertEquals(driver.getCurrentUrl(), prop.getProperty("loginURL"));
		loginUserName.sendKeys(validEmail);
		loginPassword.sendKeys(prop.getProperty("password"));
		loginButton.click();
		globalObj.wait(driver).until(ExpectedConditions.urlMatches(prop.getProperty("mercuryreservationURL")));
		Assert.assertEquals(driver.getCurrentUrl(), prop.getProperty("mercuryreservationURL"));
		globalObj.wait(driver).until(ExpectedConditions.visibilityOf(signOff));
		signOff.click();
		globalObj.wait(driver).until(ExpectedConditions.urlMatches(prop.getProperty("loginURL")));
		Assert.assertEquals(driver.getCurrentUrl(), prop.getProperty("loginURL"));
	}

	/**
	 * driverQuit() - This method is used to quit the browser.
	 * 
	 * @return void
	 */

	public void driverQuit()
	{
		globalObj.driverQuit(driver);
	}
}
