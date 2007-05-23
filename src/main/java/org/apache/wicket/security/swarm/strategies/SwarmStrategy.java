/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package org.apache.wicket.security.swarm.strategies;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.components.ISecurePage;
import org.apache.wicket.security.components.SecureComponentHelper;
import org.apache.wicket.security.hive.Hive;
import org.apache.wicket.security.hive.HiveMind;
import org.apache.wicket.security.hive.authentication.LoginContainer;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.hive.authorization.permissions.ComponentPermission;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;
import org.apache.wicket.security.models.ISecureModel;
import org.apache.wicket.security.strategies.ClassAuthorizationStrategy;
import org.apache.wicket.security.strategies.LoginException;
import org.apache.wicket.security.strategies.SecurityException;
import org.apache.wicket.security.swarm.actions.SwarmAction;
import org.apache.wicket.security.swarm.models.SwarmModel;

/**
 * Implementation of a {@link ClassAuthorizationStrategy}. It allows for both simple
 * logins as multi level logins.
 * @author marrink
 */
public class SwarmStrategy extends ClassAuthorizationStrategy
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Key to the hive.
	 */
	private Object hiveQueen;

	private LoginContainer loginContainer;

	/**
	 * Constructs a new strategy linked to the specified hive.
	 * @param hiveQueen A key to retrieve the {@link Hive}
	 */
	public SwarmStrategy(Object hiveQueen)
	{
		this(ISecurePage.class, hiveQueen);
	}

	/**
	 * Constructs a new strategy linked to the specified hive.
	 * @param secureClass instances of this class will be required to have access
	 *            authorization.
	 * @param hiveQueen A key to retrieve the {@link Hive}
	 */
	public SwarmStrategy(Class secureClass, Object hiveQueen)
	{
		super(secureClass);
		this.hiveQueen = hiveQueen;
		loginContainer = new LoginContainer();
	}

	/**
	 * Returns the hive.
	 * @return the hive.
	 * @throws SecurityException if no hive is registered.
	 */
	protected final Hive getHive()
	{
		Hive hive = HiveMind.getHive(hiveQueen);
		if (hive == null)
			throw new SecurityException("No hive registered for " + hiveQueen);
		return hive;
	}

	/**
	 * The currently logged in subject, note that at any time there is at most 1 subject
	 * logged in.
	 * @return the subject or null if no login has succeeded yet
	 */
	protected final Subject getSubject()
	{
		return loginContainer.getSubject();
	}

	/**
	 * @see org.apache.wicket.security.strategies.WaspAuthorizationStrategy#isClassAuthenticated(java.lang.Class)
	 */
	public boolean isClassAuthenticated(Class clazz)
	{
		return loginContainer.isClassAuthenticated(clazz);
	}

	/**
	 * @see org.apache.wicket.security.strategies.WaspAuthorizationStrategy#isClassAuthorized(java.lang.Class,
	 *      org.apache.wicket.security.actions.WaspAction)
	 */
	public boolean isClassAuthorized(Class clazz, WaspAction action)
	{
		return getHive().hasPermision(getSubject(),
				new ComponentPermission(SecureComponentHelper.alias(clazz), (SwarmAction) action));
	}

	/**
	 * @see org.apache.wicket.security.strategies.WaspAuthorizationStrategy#isComponentAuthenticated(wicket.Component)
	 */
	public boolean isComponentAuthenticated(Component component)
	{
		return loginContainer.isComponentAuthenticated(component);
	}

	/**
	 * @see org.apache.wicket.security.strategies.WaspAuthorizationStrategy#isComponentAuthorized(wicket.Component,
	 *      org.apache.wicket.security.actions.WaspAction)
	 */
	public boolean isComponentAuthorized(Component component, WaspAction action)
	{
		return getHive().hasPermision(getSubject(),
				new ComponentPermission(component, (SwarmAction) action));
	}

	/**
	 * @see org.apache.wicket.security.strategies.WaspAuthorizationStrategy#isModelAuthenticated(IModel,
	 *      wicket.Component)
	 */
	public boolean isModelAuthenticated(IModel model, Component component)
	{
		return loginContainer.isModelAuthenticated(model, component);
	}

	/**
	 * Checks if some action is granted on the model. Although {@link SwarmModel}s are
	 * prefered any {@link ISecureModel} can be used, in that case it uses the
	 * {@link ISecureModel#toString()} method as the name of the {@link DataPermission}
	 * @see org.apache.wicket.security.strategies.WaspAuthorizationStrategy#isModelAuthorized(ISecureModel,
	 *      wicket.Component, org.apache.wicket.security.actions.WaspAction)
	 */
	public boolean isModelAuthorized(ISecureModel model, Component component, WaspAction action)
	{
		DataPermission permission;
		if (model instanceof SwarmModel)
			permission = new DataPermission(component, (SwarmModel) model, (SwarmAction) action);
		else
			permission = new DataPermission(String.valueOf(model), action.getName());
		return getHive().hasPermision(getSubject(), permission);

	}

	/**
	 * Loggs a user in. Note that the context must be an instance of {@link LoginContext}.
	 * @see org.apache.wicket.security.strategies.WaspAuthorizationStrategy#login(java.lang.Object)
	 */
	public void login(Object context) throws LoginException
	{
		if (context instanceof LoginContext)
		{
			loginContainer.login((LoginContext) context);
		}
		else
			throw new SecurityException("Unable to process login with context: " + context);
	}

	/**
	 * Loggs a user off. Note that the context must be an instance of {@link LoginContext}
	 * and must be the same (or equal) to the logincontext used to log in.
	 * @see org.apache.wicket.security.strategies.WaspAuthorizationStrategy#logoff(Object)
	 */
	public boolean logoff(Object context)
	{
		if (context instanceof LoginContext)
		{
			return loginContainer.logoff((LoginContext) context);
		}
		else
			throw new SecurityException("Unable to process logoff with context: " + context);
	}
}
