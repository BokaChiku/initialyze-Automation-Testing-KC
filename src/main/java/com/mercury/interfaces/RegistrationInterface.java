/**
 * Registration interface contains 5 methods declaration. To achieve 100%
 * abstraction I have used the interface.
 */
package com.mercury.interfaces;

/**
 * @author Kunal Chavan
 * @since 04/28/2020
 * @version 1.0
 */

public interface RegistrationInterface
{
	public abstract void contactInformation();

	public abstract void mailingInformation();

	public abstract void userInformation();

	public abstract void verifyRegistration();

	public abstract void verifyLoginByRegistrationCredentials();
}
