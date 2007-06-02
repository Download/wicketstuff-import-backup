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

import java.net.MalformedURLException;

import org.apache.wicket.ISessionFactory;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.security.actions.ActionFactory;
import org.apache.wicket.security.hive.HiveMind;
import org.apache.wicket.security.hive.config.PolicyFileHiveFactory;
import org.apache.wicket.security.pages.MockHomePage;
import org.apache.wicket.security.pages.MockLoginPage;
import org.apache.wicket.security.strategies.StrategyFactory;
import org.apache.wicket.security.swarm.SwarmWebApplication;
import org.apache.wicket.security.swarm.actions.SwarmActionFactory;
import org.apache.wicket.security.swarm.strategies.SwarmStrategyFactory;
import org.apache.wicket.util.tester.WicketTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test if everything still works if we don't inherit from {@link SwarmWebApplication} but
 * simply implement all the required interfaces. This simply runs all the tet from
 * {@link GeneralTest} and if we don't get a {@link ClassCastException} or something like
 * that everythings works ok.
 * @author marrink
 */
public class ExtendsTest extends GeneralTest
{
	private static final class TestApplication extends WebApplication implements WaspApplication,
			ISessionFactory
	{

		private ActionFactory actionFactory;

		private StrategyFactory strategyFactory;

		/**
		 * 
		 */
		public TestApplication()
		{
			super();
			setSessionFactory(this);
		}

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
				factory.setAlias("TestPrincipal",
						"org.apache.wicket.security.hive.authorization.TestPrincipal");
				factory.setAlias("myPackage", "org.apache.wicket.security.pages");
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

		public Session newSession(Request request, Response response)
		{
			return new WaspSession(this, request);
		}

		/**
		 * @see org.apache.wicket.security.WaspApplication#getActionFactory()
		 */
		public ActionFactory getActionFactory()
		{
			return actionFactory;
		}

		/**
		 * @see org.apache.wicket.security.WaspApplication#getStrategyFactory()
		 */
		public StrategyFactory getStrategyFactory()
		{
			return strategyFactory;
		}

		/**
		 * @see org.apache.wicket.security.WaspWebApplication#setupActionFactory()
		 */
		protected void setupActionFactory()
		{
			if (actionFactory == null)
				actionFactory = new SwarmActionFactory();
			else
				throw new IllegalStateException("Can not initialize ActionFactory more then once");

		}

		/**
		 * @see org.apache.wicket.security.WaspWebApplication#setupStrategyFactory()
		 */
		protected void setupStrategyFactory()
		{
			if (strategyFactory == null)
				strategyFactory = new SwarmStrategyFactory(getHiveKey());
			else
				throw new IllegalStateException("Can not initialize StrategyFactory more then once");
		}

		/**
		 * triggers the setup of the factories and the hive. Please remember to call
		 * super.init when you override this method.
		 * @see org.apache.wicket.security.WaspWebApplication#init()
		 */
		protected void init()
		{
			setupActionFactory();
			setUpHive();
			setupStrategyFactory();
		}

		/**
		 * Destroys the strategy factory and the action factory. In that order. If you
		 * override ths method you must call super.destroy().
		 * @see wicket.Application#destroy()
		 */
		protected void destroy()
		{
			StrategyFactory factory = getStrategyFactory();
			if (factory != null)
				factory.destroy();
			ActionFactory factory2 = getActionFactory();
			if (factory2 != null)
				factory2.destroy();
		}
	}

	private static final Logger log = LoggerFactory.getLogger(ExtendsTest.class);

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		mock = new WicketTester(application = new TestApplication(), "src/test/java/"
				+ getClass().getPackage().getName().replace('.', '/'));
	}

}
