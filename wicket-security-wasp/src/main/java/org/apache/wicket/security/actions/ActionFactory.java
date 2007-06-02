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
package org.apache.wicket.security.actions;

import org.apache.wicket.authorization.Action;

/**
 * The actionFactory translates between the wicket actions, which are string
 * based, and the waspactions, which are based on something else (depending on
 * the implementation). Swarm for instance uses bitwise or to handle implies
 * checks.
 * 
 * @author marrink
 */
public interface ActionFactory
{

	/**
	 * Translates a wicket action to a wasp action. If the input is already a
	 * WaspAction the same object is returned.
	 * 
	 * @param actions
	 *            a wicket action
	 * @return a WaspAction or null if there is no mapping possible or the input
	 *         is null.
	 */
	public WaspAction getAction(Action actions);

	/**
	 * Creates or reuses a WaspAction based on string values.
	 * 
	 * @param actions
	 * @return a WaspAction
	 */
	public WaspAction getAction(String actions);

	/**
	 * Register a new action. By default {@link Access}, {@link Inherit},
	 * {@link Render} and {@link Enable} are already registered
	 * 
	 * @param waspActionClass
	 *            the class of the action to register (must be subclass of
	 *            {@link WaspAction}
	 * @param name
	 *            the name to register the action with
	 * @return an instance of the registered action
	 * @throws RegistrationException
	 *             if the action cannot be registered
	 */
	public WaspAction register(Class waspActionClass, String name) throws RegistrationException;

	/**
	 * Returns the registered action of this class.
	 * 
	 * @param waspActionClass
	 *            a subclass of {@link WaspAction}
	 * @return a new or reused instance of this class
	 * @throws IllegalArgumentException
	 *             if the class is not registered.
	 */
	public WaspAction getAction(Class waspActionClass);

	/**
	 * Clean up any resources this factory holds.
	 */
	public void destroy();
}
