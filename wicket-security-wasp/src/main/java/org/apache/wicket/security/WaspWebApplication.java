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

import org.apache.wicket.ISessionFactory;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.security.actions.ActionFactory;
import org.apache.wicket.security.strategies.StrategyFactory;

/**
 * Base class for WebAplications with a wasp security framework.
 * 
 * @author marrink
 */
public abstract class WaspWebApplication extends WebApplication
		implements
			ISessionFactory,
			WaspApplication
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * creates a new secure webapplication. sets itself up as a
	 * {@link ISessionFactory}.
	 */
	public WaspWebApplication()
	{
		super();
		setSessionFactory(this);

	}

	/**
	 * Initializes the actionfactory and the strategyfactory. If you override
	 * this method you must either call super.init() or setup the actionfactory
	 * and the strategyfactory yourself. In that order.
	 * 
	 * @see wicket.protocol.http.WebApplication#init()
	 */
	protected void init()
	{
		setupActionFactory();
		setupStrategyFactory();
	}

	/**
	 * Creates a new WaspSession. If you override this method make sure you
	 * return a subclass of {@link WaspSession}.
	 * 
	 * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.Request,
	 *      org.apache.wicket.Response)
	 */
	public Session newSession(Request request, Response response)
	{
		return new WaspSession(this, request);
	}

	/**
	 * Called by the {@link WaspWebApplication#init()}. use this to create and
	 * initialize your factory. The factory created here should be returned when
	 * calling {@link WaspApplication#getStrategyFactory()}.
	 * 
	 * @see WaspApplication#getStrategyFactory()
	 */
	protected abstract void setupStrategyFactory();

	/**
	 * Called by the {@link WaspWebApplication#init()}. use this to create and
	 * initialize your factory. The factory created here should be returned when
	 * calling {@link WaspApplication#getActionFactory()}.
	 * 
	 * @see WaspApplication#getActionFactory()
	 */
	protected abstract void setupActionFactory();

	/**
	 * Destroys the strategy factory and the action factory. In that order. If
	 * you override ths method you must call super.destroy().
	 * 
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
