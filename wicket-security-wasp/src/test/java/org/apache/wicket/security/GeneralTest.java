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
package org.apache.wicket.security;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.UnauthorizedActionException;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;
import org.apache.wicket.security.actions.ActionFactory;
import org.apache.wicket.security.actions.RegistrationException;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ComponentSecurityCheck;
import org.apache.wicket.security.checks.LinkSecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.security.components.ISecureContainer;
import org.apache.wicket.security.components.ISecurePage;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.components.markup.html.form.SecureTextField;
import org.apache.wicket.security.components.markup.html.links.SecurePageLink;
import org.apache.wicket.security.models.ISecureModel;
import org.apache.wicket.security.pages.container.MySecurePanel;
import org.apache.wicket.security.pages.insecure.SecureComponentPage;
import org.apache.wicket.security.pages.insecure.SecureLinkPage;
import org.apache.wicket.security.pages.login.LoginPage;
import org.apache.wicket.security.pages.secure.HomePage;
import org.apache.wicket.security.pages.secure.PageA;
import org.apache.wicket.security.pages.secure.PageB;
import org.apache.wicket.security.pages.secure.PageC;
import org.apache.wicket.security.pages.secure.PageC2;
import org.apache.wicket.security.pages.secure.PageD;
import org.apache.wicket.security.strategies.ClassAuthorizationStrategy;
import org.apache.wicket.security.strategies.LoginException;
import org.apache.wicket.security.strategies.SecurityException;
import org.apache.wicket.security.strategies.StrategyFactory;
import org.apache.wicket.security.strategies.WaspAuthorizationStrategy;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some general tests.
 * @author marrink
 */
public class GeneralTest extends TestCase
{
	private static final Logger log = LoggerFactory.getLogger(GeneralTest.class);

	private WicketTester mock = null;

	private WaspWebApplication application;

	private Class homePage = HomePage.class;

	private Class loginPage = LoginPage.class;

	private Class secureClass = ISecureComponent.class; // note your best option
														// is to go

	// for ISecurePage with this
	// strategy

	protected void setUp() throws Exception
	{
		mock = new WicketTester(application = new WaspWebApplication()
		{
			private ActionFactory actionFactory;

			private StrategyFactory strategyFactory;

			protected void setupActionFactory()
			{
				actionFactory = new ActionFactory()
				{
					public WaspAction getAction(Action actions)
					{
						return new StringAction(convertActions(actions.getName()));
					}

					public WaspAction getAction(String actions)
					{
						return new StringAction(convertActions(actions));
					}

					private String convertActions(String name)
					{
						if (name == null)
							return "";
						return name.toLowerCase();
					}

					public WaspAction getAction(Class waspActionClass)
					{
						return new StringAction(waspActionClass.getName().substring(
								waspActionClass.getName().lastIndexOf('.') + 1).toLowerCase());
					}

					public WaspAction register(Class waspActionClass, String name)
							throws RegistrationException
					{
						throw new RegistrationException(
								"this test factory does not allow registration");
					}

					public void destroy()
					{
						// noop
					}
				};
			}

			protected void setupStrategyFactory()
			{
				strategyFactory = new StrategyFactory()
				{
					public void destroy()
					{
						// noop
					}

					public WaspAuthorizationStrategy newStrategy()
					{
						return new TestStrategy(getSecureClass());
					}

				};
			}

			public Class getHomePage()
			{
				return GeneralTest.this.getHomePage();
			}

			public ActionFactory getActionFactory()
			{
				return actionFactory;
			}

			public Class getLoginPage()
			{
				return GeneralTest.this.getLoginPage();
			}

			public StrategyFactory getStrategyFactory()
			{
				return strategyFactory;
			}
		}, "src/test/java/" + getClass().getPackage().getName().replace('.', '/'));
	}

	protected void tearDown() throws Exception
	{
		mock.setupRequestAndResponse();
		mock.getWicketSession().invalidate();
		mock.processRequestCycle();
		mock.destroy();
		mock = null;
		application = null;
		setHomePage(HomePage.class);
		setLoginPage(LoginPage.class);
		setSecureClass(ISecureComponent.class);

	}

	/**
	 * @return Returns the secureClass.
	 */
	public Class getSecureClass()
	{
		return secureClass;
	}

	/**
	 * @param secureClass
	 *            The secureClass to set.
	 */
	public void setSecureClass(Class secureClass)
	{
		this.secureClass = secureClass;
	}

	/**
	 * @return Returns the homePage.
	 */
	public Class getHomePage()
	{
		return homePage;
	}

	/**
	 * @param homePage
	 *            The homePage to set.
	 */
	public void setHomePage(Class homePage)
	{
		this.homePage = homePage;
	}

	/**
	 * @return Returns the loginPage.
	 */
	public Class getLoginPage()
	{
		return loginPage;
	}

	/**
	 * @param loginPage
	 *            The loginPage to set.
	 */
	public void setLoginPage(Class loginPage)
	{
		this.loginPage = loginPage;
	}

	/**
	 * Login through the login page.
	 */
	public void doLogin()
	{
		mock.setupRequestAndResponse();
		mock.processRequestCycle();
		mock.assertRenderedPage(getLoginPage());
		// assertTrue(Session.get().isTemporary()); does not work in test
		FormTester form = mock.newFormTester("signInPanel:signInForm");
		form.setValue("username", "test");
		form.setValue("password", "test");
		form.submit();
		mock.assertRenderedPage(getHomePage());
		assertFalse(Session.get().isTemporary());
	}

	/**
	 * Test accessability of an unprotected page.
	 */
	public void testUnsecuredPage()
	{
		mock.startPage(org.apache.wicket.security.pages.insecure.HomePage.class);
		mock.assertRenderedPage(org.apache.wicket.security.pages.insecure.HomePage.class);
	}
	/**
	 * Test accessability of an unprotected page with a secure component.
	 */
	public void testUnsecuredPage2()
	{
		//continueto originaldestination does not work if there is no url available, so we need to fake one here(testing only hack)
		mock.setupRequestAndResponse();
		WebRequestCycle cycle = mock.createRequestCycle();
		String url1 = cycle.urlFor(new BookmarkablePageRequestTarget(SecureComponentPage.class,null)).toString();
		mock.getServletRequest().setURL("/GeneralTest$1/GeneralTest$1/" +url1);
		mock.processRequestCycle();
		mock.assertRenderedPage(getLoginPage());
		FormTester form = mock.newFormTester("signInPanel:signInForm");
		form.setValue("username", "test");
		form.setValue("password", "test");
		form.submit();
		mock.assertRenderedPage(SecureComponentPage.class);
		mock.assertInvisible("secure"); //no render rights on the component
	}
	/**
	 * Test accessability of an unprotected page with a secure link.
	 */
	public void testUnsecuredPage3()
	{
		//continueto originaldestination does not work if there is no url available, so we need to fake one here(testing only hack)
		mock.setupRequestAndResponse();
		WebRequestCycle cycle = mock.createRequestCycle();
		String url1 = cycle.urlFor(new BookmarkablePageRequestTarget(SecureLinkPage.class,null)).toString();
		mock.getServletRequest().setURL("/GeneralTest$1/GeneralTest$1/" +url1);
		mock.processRequestCycle();
		mock.assertRenderedPage(getLoginPage());
		FormTester form = mock.newFormTester("signInPanel:signInForm");
		form.setValue("username", "test");
		form.setValue("password", "test");
		form.submit();
		mock.assertRenderedPage(SecureLinkPage.class);
		//need to arrange enable rights for homepage
		Map authorized = new HashMap();
		authorized.put(getHomePage(), application.getActionFactory().getAction(
				"access render enable"));
		login(authorized);
		mock.startPage(mock.getLastRenderedPage());
		mock.assertVisible("secure");
		mock.clickLink("secure", false);
		mock.assertRenderedPage(getHomePage());
	}

	/**
	 * Test visibility and clickability of a secure link.
	 */
	public void testLink()
	{
		doLogin();
		mock.assertInvisible("link");
		mock.assertVisible("sorry");

		Page lastPage = mock.getLastRenderedPage();
		SecurePageLink link = (SecurePageLink)lastPage.get("link");
		LinkSecurityCheck linkcheck = ((LinkSecurityCheck)link.getSecurityCheck())
				.setUseAlternativeRenderCheck(true);
		// need to fake inherit for the link to show up.
		Map authorized = new HashMap();
		authorized.put(SecureComponentHelper.alias(link), application.getActionFactory().getAction(
				"access render"));
		login(authorized);
		mock.startPage(lastPage);
		mock.assertRenderedPage(getHomePage());
		assertSame(lastPage, mock.getLastRenderedPage());
		mock.assertInvisible("sorry");
		mock.assertVisible("link");
		TagTester tag = mock.getTagByWicketId("link");
		assertNull(tag.getAttribute("href"));
		assertNull(tag.getAttribute("onclick"));
		authorized.clear();
		authorized.put(PageA.class, application.getActionFactory().getAction(
		"access render enable"));
		login(authorized);
		mock.startPage(mock.getLastRenderedPage());
		tag = mock.getTagByWicketId("link");
		assertNotNull(tag.getAttribute("href"));
		logoff(authorized);
		authorized.clear();

		linkcheck.setUseAlternativeRenderCheck(false);
		authorized.put(PageA.class, application.getActionFactory().getAction("render"));
		login(authorized);
		mock.setupRequestAndResponse();
		mock.processRequestCycle();
		mock.assertRenderedPage(getHomePage());
		mock.assertInvisible("sorry");
		mock.assertVisible("link");
		mock.clickLink("link", false);
		mock.assertRenderedPage(application.getApplicationSettings().getAccessDeniedPage());
		authorized.put(PageA.class, application.getActionFactory()
				.getAction("access render enable"));
		// Note that normally access is implied by render, just not in this
		// simple
		// testcase
		login(authorized);
		mock.startPage(getHomePage());
		mock.clickLink("link", false);
		mock.assertRenderedPage(PageA.class);
	}

	/**
	 * login through the session bypassing the loginpage. Note that the login
	 * page automaticly grants access to the homepage, here you must do it
	 * yourself. Note these rights are added to any existing ones replacing only
	 * if the class was already added previously.
	 * 
	 * @param authorized
	 *            map containing classes and the actions the users has on them.
	 */
	private void login(Map authorized)
	{
		try
		{
			((WaspSession)mock.getWicketSession()).login(authorized);
		}
		catch (LoginException e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	/**
	 * Logoff. If a map is specified only a partial logoff is performed removing
	 * only those rights in the map.
	 * 
	 * @param authorized
	 */
	private void logoff(Map authorized)
	{
		((WaspSession)mock.getWicketSession()).logoff(authorized);
	}

	/**
	 * Test reading and writing from and to a secure textfield.
	 */
	public void testReadWrite()
	{
		doLogin();
		Map authorized = new HashMap();
		authorized.put(PageA.class, application.getActionFactory()
				.getAction("access render enable"));
		login(authorized);
		mock.setupRequestAndResponse();
		mock.processRequestCycle();
		mock.clickLink("link", false);
		mock.assertRenderedPage(PageA.class);
		mock.assertInvisible("secure");
		authorized.put(SecureComponentHelper.alias(mock.getLastRenderedPage().get("secure")),
				application.getActionFactory().getAction("access render"));
		login(authorized);
		mock.startPage(PageA.class);
		mock.assertVisible("secure");
		TagTester tag = mock.getTagByWicketId("secure");
		assertTrue(tag.hasAttribute("disabled"));
		try
		{
			mock.getComponentFromLastRenderedPage("secure").setModelObject("writing in textfield");
			fail("should not be able to write in textfield");
		}
		catch (UnauthorizedActionException e)
		{
		}
		authorized.put(
				SecureComponentHelper.alias(mock.getComponentFromLastRenderedPage("secure")),
				application.getActionFactory().getAction("access render enable"));
		login(authorized);
		mock.startPage(PageA.class);
		mock.assertVisible("secure");
		tag = mock.getTagByWicketId("secure");
		assertFalse(tag.hasAttribute("disabled"));
		String writings = "now we are getting somewhere";
		mock.getComponentFromLastRenderedPage("secure").setModelObject(writings);
		assertEquals(writings, mock.getComponentFromLastRenderedPage("secure").getModelObject());
		mock.startPage(mock.getLastRenderedPage());
		mock.assertRenderedPage(PageA.class);
		tag = mock.getTagByWicketId("secure");
		assertTrue(tag.getAttributeIs("value", writings));
	}

	/**
	 * Test behaviour of different secure classes in the
	 * {@link ClassAuthorizationStrategy}.
	 */
	public void testInstantiation()
	{
		doLogin();
		assertEquals(ISecureComponent.class, getSecureClass());
		mock.setupRequestAndResponse();
		new PageB();
		// even though we added the same securitycheck to a regular textfield as
		// a
		// secureTextfield has there is no instantiation check because it is not
		// an
		// IsecureComponent
		Map authorized = new HashMap();
		authorized.put(PageA.class, application.getActionFactory().getAction("access"));
		login(authorized);
		new PageA(); // still allowed here
		authorized.clear();
		authorized.put(SecureTextField.class, application.getActionFactory().getAction("access"));
		logoff(authorized);
		try
		{
			new PageA();
			fail("somehow page was instantiated");
		}
		catch (UnauthorizedInstantiationException e)
		{
			// because we are not allowed to instantiate a SecureTextField
			// anymore
		}
		mock.processRequestCycle();
		try
		{
			tearDown();
			setSecureClass(ISecurePage.class);
			setUp();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		doLogin();
		// now we only check pages so all components will be created, this does
		// not affect
		// the render check though
		mock.setupRequestAndResponse();
		logoff(authorized);
		authorized.clear();
		authorized.put(PageA.class, application.getActionFactory().getAction("access")); // need
		// to
		// enable
		// pagea
		// again
		login(authorized);
		new PageA(); // only pages are checked so now securetextfield is
						// allowed.
		mock.processRequestCycle();
	}

	public void testAdvancedInstantiationChecks()
	{
		doLogin();
		mock.startPage(PageC.class);
		// even if we are loged in and have access rights it will redirect us to
		// the
		// access denied page because it need render rights
		mock.assertRenderedPage(application.getApplicationSettings().getAccessDeniedPage());
		Map authorized = new HashMap();
		authorized.put(PageC.class, application.getActionFactory().getAction("access render"));
		login(authorized);
		mock.startPage(PageC.class);
		mock.assertRenderedPage(PageC.class);
		// PageC2 will render because it has all the required rights.
		mock.startPage(PageC2.class);
		mock.assertRenderedPage(PageC2.class);
		mock.startPage(HomePage.class);
		logoff(authorized);
		// but if we remove the render rights for PageC, PageC2 misses those
		// rights and
		// won't be able to be instantiated anymore
		mock.startPage(PageC2.class);
		mock.assertRenderedPage(application.getApplicationSettings().getAccessDeniedPage());
	}

	public void testSecureComponentHelper()
	{
		TextField field = new TextField("id");
		assertNull(SecureComponentHelper.getSecurityCheck(field));
		assertTrue(SecureComponentHelper.isActionAuthorized(field, "whatever"));
		ComponentSecurityCheck check = new ComponentSecurityCheck(field);
		// this did not register the check with the component
		assertNull(SecureComponentHelper.getSecurityCheck(field));
		SecureComponentHelper.setSecurityCheck(field, check);
		assertEquals(check, SecureComponentHelper.getSecurityCheck(field));
		try
		{
			// can not check this because component is not attached to a page
			// see SecureComponentHelper.alias(Component)
			SecureComponentHelper.isActionAuthorized(field, "whatever");
			fail();
		}
		catch(RestartResponseAtInterceptPageException e)
		{
		}
		catch (SecurityException e)
		{
		}
		assertFalse(SecureComponentHelper.hasSecureModel(field));
		field.setModel(new ISecureModel()
		{

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void detach()
			{
				// noop
			}

			public void setObject(Object object)
			{
				// noop
			}

			public Object getObject()
			{
				return "test";
			}

			public boolean isAuthorized(Component component, WaspAction action)
			{
				return false;
			}

			public boolean isAuthenticated(Component component)
			{
				return false;
			}

		});
		assertTrue(SecureComponentHelper.hasSecureModel(field));
	}

	public void testComponentSecurityCheckAndISecureModel()
	{
		doLogin();
		mock.startPage(PageD.class);
		mock.assertRenderedPage(PageD.class);
		mock.assertInvisible("componentcheck"); // no render rights
		mock.assertVisible("modelcheck");
		mock.assertInvisible("both"); // component says no
		mock.assertInvisible("bothcheck");// component says no, model says yes
		try
		{
			mock.getComponentFromLastRenderedPage("modelcheck").setModelObject("foobar");
			fail("should not be able to write to model");
		}
		catch (UnauthorizedActionException e)
		{
		}
		Map authorized = new HashMap();
		authorized.put(SecureTextField.class, application.getActionFactory().getAction(
				"access render"));
		authorized.put("model:modelcheck", application.getActionFactory().getAction(
				"access render enable"));
		login(authorized);
		mock.startPage(PageD.class);
		mock.assertRenderedPage(PageD.class);
		mock.assertVisible("componentcheck");
		mock.assertVisible("modelcheck");
		mock.assertVisible("both");
		mock.assertVisible("bothcheck");
		mock.getComponentFromLastRenderedPage("modelcheck").setModelObject("foobar");
		assertEquals("foobar", mock.getComponentFromLastRenderedPage("modelcheck").getModelObject());
		((ISecureComponent)mock.getComponentFromLastRenderedPage("both")).setSecurityCheck(null);
		authorized.clear();
		authorized.put("model:modelcheck", application.getActionFactory()
				.getAction("access render"));
		authorized.put("model:bothcheck", application.getActionFactory().getAction("access"));
		login(authorized);
		mock.startPage(mock.getLastRenderedPage());
		mock.assertRenderedPage(PageD.class);
		TagTester tag = mock.getTagByWicketId("modelcheck");
		assertTrue(tag.getAttributeIs("value", "foobar"));
		mock.assertVisible("componentcheck");
		mock.assertVisible("modelcheck");
		mock.assertInvisible("both"); // model says no
		mock.assertInvisible("bothcheck"); // model says no, component says yes
		try
		{
			mock.getComponentFromLastRenderedPage("modelcheck").setModelObject("blaat");
			fail("should not be able to write to model");
		}
		catch (UnauthorizedActionException e)
		{
		}
	}
	public void testPanelReplacement()
	{
		try
		{
			tearDown();
			setHomePage(org.apache.wicket.security.pages.container.HomePage.class);
			setSecureClass(ISecureContainer.class);
			setUp();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		mock.startPage(getHomePage());
		mock.assertRenderedPage(getHomePage());
		mock.clickLink("link");
		mock.assertRenderedPage(getLoginPage());
		FormTester form = mock.newFormTester("signInPanel:signInForm");
		form.setValue("username", "test");
		form.setValue("password", "test");
		form.submit();
		mock.assertRenderedPage(getHomePage());
		assertFalse(Session.get().isTemporary());
		mock.assertInvisible("panel");
		//note by adding a second panel visible if the main panel is invisible we could tell the user he is not authorized or something like that
		Map authorized = new HashMap();
		authorized.put(MySecurePanel.class, application.getActionFactory().getAction(
				"access render"));
		login(authorized);
		mock.startPage(mock.getLastRenderedPage());
		mock.assertVisible("panel");
		mock.clickLink("link");
		mock.assertVisible("panel");		
	}
	public void testBogusLoginPage()
	{
		try
		{
			tearDown();
			setHomePage(HomePage.class);
			setLoginPage(PageD.class);
			setSecureClass(ISecurePage.class);
			setUp();
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			fail(e.getMessage());
		}
		mock.startPage(getHomePage());
		log.info(""+mock.getLastRenderedPage().getClass());
		mock.assertRenderedPage(getLoginPage());
		//even though we accidentally made it a secure page!
	}
}
