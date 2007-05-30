/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package org.apache.wicket.security;

import java.net.MalformedURLException;

import junit.framework.TestCase;

import org.apache.wicket.security.hive.HiveMind;
import org.apache.wicket.security.hive.authentication.SecondaryLoginContext;
import org.apache.wicket.security.hive.config.PolicyFileHiveFactory;
import org.apache.wicket.security.pages.MockHomePage;
import org.apache.wicket.security.pages.MockLoginPage;
import org.apache.wicket.security.pages.PageA;
import org.apache.wicket.security.pages.SecondaryLoginPage;
import org.apache.wicket.security.pages.VerySecurePage;
import org.apache.wicket.security.swarm.SwarmWebApplication;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author marrink
 */
public class GeneralTest extends TestCase
{
	private static final Logger log = LoggerFactory.getLogger(GeneralTest.class);

	private SwarmWebApplication application;

	private WicketTester mock;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		mock = new WicketTester(application = new SwarmWebApplication()
		{

			protected Object getHiveKey()
			{
				// if we were using servlet-api 2.5 we could get the contextpath from the
				// servletcontext
				return "test";
			}

			protected void setUpHive()
			{
				PolicyFileHiveFactory factory = new PolicyFileHiveFactory();
				try
				{
					factory.addPolicyFile(getServletContext().getResource("WEB-INF/policy.hive"));
					factory.setAlias("TestPrincipal", "org.apache.wicket.security.hive.authorization.TestPrincipal");
					factory.setAlias("myPackage","org.apache.wicket.security.pages");
				}
				catch (MalformedURLException e)
				{
					log.error(e.getMessage(), e);
				}
				HiveMind.registerHive(getHiveKey(), factory);
			}

			public Class getHomePage()
			{
				return MockHomePage.class;
			}

			public Class getLoginPage()
			{
				return MockLoginPage.class;
			}
		}, "src/test/java/" + getClass().getPackage().getName().replace('.', '/'));
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
//		mock.setupRequestAndResponse();
//		mock.getWicketSession().invalidate();
//		mock.processRequestCycle();
		mock.destroy();
		mock = null;
		application = null;
		HiveMind.unregisterHive("test");
	}

	/**
	 * Test multiple logins with auto redirect to the correct login page.
	 */
	public void testMultiLogin()
	{
		mock.startPage(MockHomePage.class);
		mock.assertRenderedPage(MockLoginPage.class);
		FormTester form = mock.newFormTester("form");
		form.setValue("username", "test");
		form.submit();
		mock.assertRenderedPage(MockHomePage.class);
		mock.clickLink("secret", false);
		mock.assertRenderedPage(SecondaryLoginPage.class);
		form = mock.newFormTester("form");
		form.setValue("username", "test");
		form.submit();
		mock.assertRenderedPage(VerySecurePage.class);
		assertTrue(((WaspSession)mock.getWicketSession()).logoff(new SecondaryLoginContext()));
		mock.startPage(mock.getLastRenderedPage());
		mock.assertRenderedPage(application.getApplicationSettings().getAccessDeniedPage());
		//accessdenied because the page is already constructed
	}
	public void testInheritance()
	{
		mock.startPage(MockHomePage.class);
		mock.assertRenderedPage(MockLoginPage.class);
		FormTester form = mock.newFormTester("form");
		form.setValue("username", "test");
		form.submit();
		mock.assertRenderedPage(MockHomePage.class);
		mock.clickLink("link");
		mock.assertRenderedPage(PageA.class);
		mock.assertInvisible("invisible");
		mock.assertVisible("readonly");
		assertTrue(mock.getTagByWicketId("readonly").hasAttribute("disabled"));
		mock.assertVisible("readonly");
	}
}
