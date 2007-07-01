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

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.hive.authorization.TestPrincipal;

/**
 * A context for multi login, this context is used to grant the most amount of permissions.
 * @author marrink
 */
public final class SecondaryLoginContext extends LoginContext
{
	/**
	 * Subject for secondary logins.
	 * Note try not to serialize the logincontext with the subject.
	 * @author marrink
	 */
	private static final class MySecondSubject extends DefaultSubject
	{
		private static final long serialVersionUID = 1L;
		/**
		 * 
		 * @see org.apache.wicket.security.hive.authentication.DefaultSubject#isClassAuthenticated(java.lang.Class)
		 */
		public boolean isClassAuthenticated(Class class1)
		{
			return true;
			// we also could just return true if the class is a HighSecurityPage
			// if we did that we would have to login again for a "normal" page
			// now the 2nd login is good for all pages
		}
		/**
		 * 
		 * @see org.apache.wicket.security.hive.authentication.DefaultSubject#isComponentAuthenticated(org.apache.wicket.Component)
		 */
		public boolean isComponentAuthenticated(Component component)
		{
			return true;
		}
		/**
		 * 
		 * @see org.apache.wicket.security.hive.authentication.DefaultSubject#isModelAuthenticated(org.apache.wicket.model.IModel, org.apache.wicket.Component)
		 */
		public boolean isModelAuthenticated(IModel model, Component component)
		{
			return true;
		}
	}
	/**
	 * 
	 * Constructor, set the sort order to 1 (above the sort order of the {@link PrimaryLoginContext})
	 */
	public SecondaryLoginContext()
	{
		super(1);
	}

	/**
	 * 
	 * @see org.apache.wicket.security.hive.authentication.LoginContext#login()
	 */
	public Subject login()
	{
		DefaultSubject defaultSubject = new MySecondSubject();
		defaultSubject.addPrincipal(new TestPrincipal("admin"));
		return defaultSubject;
	}

	/**
	 * @see org.apache.wicket.security.hive.authentication.LoginContext#preventsAdditionalLogins()
	 */
	public boolean preventsAdditionalLogins()
	{
		return true;
	}
}