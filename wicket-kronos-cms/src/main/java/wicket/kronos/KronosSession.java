/*
 * $Id: KronosSession.java 3158 2005-11-08 15:55:17Z eelco12 $
 * $Revision: 3158 $ $Date: 2005-11-08 16:55:17 +0100 (Tue, 08 Nov 2005) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.kronos;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import wicket.PageParameters;
import wicket.authentication.AuthenticatedWebApplication;
import wicket.authentication.AuthenticatedWebSession;
import wicket.authentication.User;
import wicket.authorization.strategies.role.Roles;

/**
 * Subclass of WebSession for KronosApplication to allow easy and typesafe access to session
 * properties.
 * 
 * @author Jonathan Locke
 */
public final class KronosSession extends AuthenticatedWebSession {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The JackRabbit Session that is stored
	 */
	private transient Session jcrSession = null;

	/**
	 * The page parameters that are stored
	 */
	private PageParameters pageParameters = null;

	/**
	 * Constructor
	 * 
	 * @param application
	 *            The application
	 */
	protected KronosSession(final AuthenticatedWebApplication application)
	{
		super(application);
	}

	@Override
	public Roles getRoles()
	{
		/* check if user not null */
		Roles rolesList = null;
		if (this.signedInUser != null)
		{
			rolesList = this.signedInUser.getRoles();
		}
		return rolesList;
	}

	/**
	 * @return The signed in user
	 */
	public User getSignedInUser()
	{
		return this.signedInUser;
	}

	@Override
	public boolean signIn(String username, String password)
	{
		assert (username != null);
		assert (password != null);
		User user = null;
		boolean success = false;
		if ((user = DataProcessor.getUser(username)) != null)
		{
			/* hash the argumented password and check with stored password */
			if (user.getPassword().equalsIgnoreCase(this.createHash(password)))
			{
				success = true;

				/* Store the current user in the session */
				this.signedInUser = user;
			}
		}

		return success;
	}

	/**
	 * Get the hash value for the password parameter
	 * 
	 * @param password
	 * @return hashed password
	 */
	public String createHash(String password)
	{
		assert (password != null);
		String hashedPassword = null;

		/* Generate a hashed password */
		try
		{
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashedPassword = hash.toString(16);
		}
		catch (NoSuchAlgorithmException nsae)
		{
			// ignore
		}
		return hashedPassword;
	}

	/**
	 * @return the current JackRabbit Session or generate one.
	 */
	public Session getJCRSession()
	{
		if (this.jcrSession == null)
		{
			try
			{
				this.jcrSession = KronosApplication.get().getRepository().login(
						new SimpleCredentials("userid", "".toCharArray()));

			}
			catch (RepositoryException e)
			{
				throw new RuntimeException(e);
			}
		}
		return this.jcrSession;
	}

	/**
	 * @param pageParameters
	 */
	public void setPageParameters(PageParameters pageParameters)
	{
		this.pageParameters = pageParameters;
	}

	/**
	 * @return the stored PageParameters
	 */
	public PageParameters getPageParameters()
	{
		return this.pageParameters;
	}

	/**
	 * @return KronosSession
	 */
	public static KronosSession get()
	{
		return (KronosSession) wicket.Session.get();
	}
}
