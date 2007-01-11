/*
 * $Id: AuthenticatedWebSession.java 4790 2006-03-06 22:59:16Z eelco12 $
 * $Revision: 4790 $
 * $Date: 2006-03-06 23:59:16 +0100 (Mon, 06 Mar 2006) $
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.authentication;

import wicket.Session;
import wicket.authorization.strategies.role.Roles;
import wicket.protocol.http.WebSession;

/**
 * Basic authenticated web session. Subclasses must provide a method that authenticates the session
 * based on a username and password, and a method implementation that gets the Roles
 * 
 * @author Jonathan Locke
 */
public abstract class AuthenticatedWebSession extends WebSession {
	protected User signedInUser = null;

	/**
	 * @return Current authenticated web session
	 */
	public static AuthenticatedWebSession get()
	{
		return (AuthenticatedWebSession) Session.get();
	}

	/**
	 * Construct.
	 * 
	 * @param application
	 *            The web application
	 */
	public AuthenticatedWebSession(final AuthenticatedWebApplication application)
	{
		super(application);
	}

	/**
	 * Signs user in by authenticating them with a username and password
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if the user was signed in successfully
	 */
	public abstract boolean signIn(final String username, final String password);

	/**
	 * @return Get the roles that this session can play
	 */
	public abstract Roles getRoles();

	/**
	 * Sign the user out
	 */
	public void signout()
	{
		this.signedInUser = null;
	}

	/**
	 * @return True if the user is signed in to this session
	 */
	public final boolean isSignedIn()
	{
		return (this.signedInUser != null);
	}
}
