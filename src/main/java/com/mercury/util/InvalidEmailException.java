/**
 * Custom exception is created in this class
 */
package com.mercury.util;

/**
 * @author Kunal Chavan
 * @implNote I have created custom exception if the user enters invalid email in
 *           the config.properties file.
 * @since 04/28/2020
 * @version 1.0
 */

public class InvalidEmailException extends Exception
{
	InvalidEmailException(String msg)
	{
		super(msg);
	}
}
