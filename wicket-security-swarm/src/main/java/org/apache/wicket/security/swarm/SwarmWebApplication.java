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
package org.apache.wicket.security.swarm;

import org.apache.wicket.security.WaspWebApplication;
import org.apache.wicket.security.actions.ActionFactory;
import org.apache.wicket.security.strategies.StrategyFactory;
import org.apache.wicket.security.swarm.actions.SwarmActionFactory;
import org.apache.wicket.security.swarm.strategies.SwarmStrategyFactory;

/**
 * A default webapp. It sets up the strategy and action factories and triggers
 * the hive setup. but you must remember to call super in the init or do your
 * own factory setups.
 * 
 * @author marrink
 */
public abstract class SwarmWebApplication extends WaspWebApplication
{
	private ActionFactory actionFactory;

	private StrategyFactory strategyFactory;

	/**
	 * @see org.apache.wicket.security.WaspWebApplication#setupActionFactory()
	 */
	protected void setupActionFactory()
	{
		setActionFactory(new SwarmActionFactory());

	}

	/**
	 * Allows the {@link ActionFactory} field to be set once.
	 * 
	 * @param factory
	 *            the actionfactory
	 * @throws IllegalStateException
	 *             if the factory is set more than once.
	 */
	protected final void setActionFactory(ActionFactory factory)
	{
		if (actionFactory == null)
			actionFactory = factory;
		else
			throw new IllegalStateException("Can not initialize ActionFactory more then once");
	}

	/**
	 * @see org.apache.wicket.security.WaspWebApplication#setupStrategyFactory()
	 */
	protected void setupStrategyFactory()
	{
		setStrategyFactory(new SwarmStrategyFactory(getHiveKey()));
	}

	/**
	 * Allows the {@link StrategyFactory} field to be set once.
	 * 
	 * @param factory
	 *            the strategyfactory
	 * @throws IllegalStateException
	 *             if the factory is set more than once.
	 */
	protected final void setStrategyFactory(StrategyFactory factory)
	{
		if (strategyFactory == null)
			strategyFactory = factory;
		else
			throw new IllegalStateException("Can not initialize StrategyFactory more then once");
	}

	/**
	 * triggers the setup of the factories and the hive. Please remember to call
	 * super.init when you override this method.
	 * 
	 * @see org.apache.wicket.security.WaspWebApplication#init()
	 */
	protected void init()
	{
		setupActionFactory();
		setUpHive();
		setupStrategyFactory();
	}

	/**
	 * Set up a Hive for this Application. For Example<br>
	 * <code>
	 * PolicyFileHiveFactory factory = new PolicyFileHiveFactory();
	 * factory.addPolicyFile("/policy.hive");
	 * HiveMind.registerHive(getHiveKey(), factory);
	 * </code>
	 * Note that you must setup the actionfactory before you can setup the hive.
	 * Note that the hive is not automatically unregistered since there is a
	 * chance you want to share it with another webapp. If you want to
	 * unregister the hive please do so in the {@link #destroy()}
	 */
	protected abstract void setUpHive();

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
	 * Returns the key to specify the hive.
	 * 
	 * @return the key
	 */
	protected abstract Object getHiveKey();

}
