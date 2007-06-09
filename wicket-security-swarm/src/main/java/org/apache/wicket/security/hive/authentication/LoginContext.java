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

import org.apache.wicket.security.strategies.LoginException;

/**
 * A LoginContext is little more than a factory to create a {@link Subject} and
 * can be discarded afterwards. Usually it contains some credentials such as
 * username and password. Note that generally it is no a good idea to store
 * those type of credentials in the session, so if you plan on keeping a this
 * context in the session be sure to clear them before you return a Subject in
 * {@link #login()}. Some applications will require you to login with two or
 * more different LoginContexts before a user is fully authenticated. For that
 * purpose a sortOrder is available in the context. which is used in descending
 * order to pass authentication requests to the subjects untill one of them
 * authenticates. Sort orders are &gt;=0 and are not required to have an
 * interval of 1. For example 0, 5,6 are all perfectly legal sort orders for one
 * user. Duplicates are also allowed, in that case they are queried in reverse
 * order of login. The context also contains a flag to indicate if an additional
 * login is allowed. Note that both the sort order and the addiotional login
 * flag must be constant. Also note that all LoginContexts of the same class and
 * with the same sort order are equal, thus for logoff you do need to keep a
 * reference to the context but can simply use a new instance.
 * 
 * @author marrink
 * @see #preventsAdditionalLogins()
 */
public abstract class LoginContext
{
	private final int sortOrder;
	private final boolean additionalLoginsPrevented;

	/**
	 * Constructs a context for single login applications. At sortorder 0 and
	 * preventing additional logins.
	 */
	public LoginContext()
	{
		this(0, true);
	}

	/**
	 * Constructs a new context at the specified sort order. Additional logins are prevented.
	 * This constructor is usually used in mult-login scenario's for the context with the the highest sort order. 
	 * 
	 * @param sortOrder a number of 0 or higher.
	 */
	public LoginContext(int sortOrder)
	{
		this(sortOrder, true);
	}

	/**
	 * Constructs a new context with sort order 0 and a customizable flag for preventing additional logins.
	 * This constructor is mostly used in multi-login scenario's.
	 * 
	 * @param sortOrder
	 * @param allowAdditionalLogings
	 */
	public LoginContext(boolean allowAdditionalLogings)
	{
		this(0, allowAdditionalLogings);
	}

	/**
	 * Constructs a new context with customizable sort order and  flag for preventing additional logins.
	 * This constructor is mostly used in multi-login scenario's.
	 * 
	 * @param sortOrder
	 * @param allowAdditionalLogins
	 */
	public LoginContext(int sortOrder, boolean allowAdditionalLogins)
	{
		if (sortOrder < 0)
			throw new IllegalArgumentException("0 is the lowest sort order allowed, not "
					+ sortOrder);
		this.sortOrder = sortOrder;
		this.additionalLoginsPrevented = !allowAdditionalLogins;
	}


	/**
	 * Perform a login. If the login fails in any way a {@link LoginException}
	 * must be thrown rather then returning null.
	 * 
	 * @return a {@link Subject}, never null.
	 */
	public abstract Subject login() throws LoginException;

	/**
	 * Indicates the level of this context. the higher the level the more you
	 * are authorised / authenticated for.
	 * 
	 * @return the level
	 */
	protected final int getSortOrder()
	{
		return sortOrder;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public final int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + getClass().hashCode();
		result = PRIME * result + sortOrder;
		return result;
	}

	/**
	 * A loginContext is equal to a LoginContext of the same class (not
	 * subclass) and level.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LoginContext other = (LoginContext)obj;
		return sortOrder == other.sortOrder;
	}

	/**
	 * Signals to the {@link LoginContainer} that no additional context should
	 * be allowed to login. The return value must be constant from one
	 * invocation to another for this instance.This flag is checked once by the
	 * container inmediatly after {@link #login()}. Note in a multi login
	 * environment you will want your logincontext with the highest possible
	 * sort order to prevent additional logins. In a single login environment
	 * your logincontext should always prevent additional logins (as
	 * {@link SingleLoginContext} does.
	 * 
	 * @return true if you do not want additional logins for this session, false
	 *         otherwise.
	 */
	public boolean preventsAdditionalLogins()
	{
		return additionalLoginsPrevented;
	}
}
