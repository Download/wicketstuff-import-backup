/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.security.hive.authentication;

import junit.framework.TestCase;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.hive.authorization.TestPrincipal;
import org.apache.wicket.security.pages.VerySecurePage;
import org.apache.wicket.security.strategies.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author marrink
 */
public class LoginTest extends TestCase
{
	private static final Logger log = LoggerFactory.getLogger(LoginTest.class);

	/**
	 * Test method for
	 * {@link org.apache.wicket.security.hive.authentication.LoginContainer#login(org.apache.wicket.security.hive.authentication.LoginContext)}.
	 */
	public void testLogin()
	{
		LoginContainer container = new LoginContainer();
		try
		{
			container.login(null);
			fail("LoginContext is required");
		}
		catch (LoginException e)
		{
		}
		LoginContext ctx = new LoginContext(true)
		{
			private static final long serialVersionUID = 1L;

			public Subject login()
			{
				DefaultSubject subject = new DefaultSubject();
				subject.addPrincipal(new TestPrincipal("home"));
				return subject;
			}
		};
		assertFalse(container.isClassAuthenticated(getClass()));
		try
		{
			container.login(ctx);
		}
		catch (LoginException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		assertNotNull(container.getSubject());
		assertTrue(container.isClassAuthenticated(getClass()));
		// shows that even though the new context does not authenticate anything
		// the
		// previous one does
		ctx = new LoginContext(1)
		{
			// bad example, do not create an anonymous Subject in a LoginContext
			// as it will cause the context to be serialized along with the
			// subject
			public Subject login()
			{
				return new DefaultSubject()
				{
					private static final long serialVersionUID = 1L;

					/**
					 * 
					 * @see org.apache.wicket.security.hive.authentication.DefaultSubject#isClassAuthenticated(java.lang.Class)
					 */
					public boolean isClassAuthenticated(Class class1)
					{
						return false;
					}

					/**
					 * 
					 * @see org.apache.wicket.security.hive.authentication.DefaultSubject#isComponentAuthenticated(org.apache.wicket.Component)
					 */
					public boolean isComponentAuthenticated(Component component)
					{
						return false;
					}

					/**
					 * 
					 * @see org.apache.wicket.security.hive.authentication.DefaultSubject#isModelAuthenticated(org.apache.wicket.model.IModel,
					 *      org.apache.wicket.Component)
					 */
					public boolean isModelAuthenticated(IModel model, Component component)
					{
						return false;
					}
				};
			}

			/**
			 * @see org.apache.wicket.security.hive.authentication.LoginContext#preventsAdditionalLogins()
			 */
			public boolean preventsAdditionalLogins()
			{
				return true;
			}
		};
		try
		{
			container.login(ctx);
		}
		catch (LoginException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		assertTrue(container.isClassAuthenticated(getClass()));
		// note changing the order does not matter since the first that
		// authenticates true
		// is used.
	}

	/**
	 * Test method for
	 * {@link org.apache.wicket.security.hive.authentication.LoginContainer#logoff(org.apache.wicket.security.hive.authentication.LoginContext)}.
	 */
	public void testLogoff()
	{
		LoginContainer container = new LoginContainer();
		LoginContext ctx = new LoginContext()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Subject login()
			{
				return new DefaultSubject();
			}
		};
		try
		{
			container.login(ctx);
		}
		catch (LoginException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		assertNotNull(container.getSubject());
		container.logoff(ctx);
		assertNull(container.getSubject());
	}

	/**
	 * Test method for multi login.
	 */
	public void testIsClassAuthenticated()
	{
		// for multilogin to work the least authenticating login should be at
		// the bottom
		LoginContainer container = new LoginContainer();
		LoginContext low = new PrimaryLoginContext();
		LoginContext high = new SecondaryLoginContext();
		try
		{
			container.login(low);
			assertFalse(container.isClassAuthenticated(VerySecurePage.class));
			assertTrue(container.getSubject().getPrincipals().contains(new TestPrincipal("basic")));
			assertFalse(container.getSubject().getPrincipals().contains(new TestPrincipal("admin")));
			container.login(high);
			assertTrue(container.isClassAuthenticated(VerySecurePage.class));
			assertTrue(container.getSubject().getPrincipals().contains(new TestPrincipal("admin")));
		}
		catch (LoginException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	/**
	 * tests if the preventadditionallogin flag works as expected
	 * 
	 */
	public void testPreventLogin()
	{
		LoginContainer container = new LoginContainer();
		try
		{
			container.login(new myContext());
		}
		catch (LoginException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		try
		{
			container.login(new myContext());
			fail("Should not be able to login");
		}
		catch (LoginException e)
		{
		}
		container.logoff(new myContext());
		try
		{
			container.login(new myContext());
		}
		catch (LoginException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	private static final class myContext extends LoginContext
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @see org.apache.wicket.security.hive.authentication.LoginContext#login()
		 */
		public Subject login()
		{
			return new DefaultSubject();
		}

	}
}
