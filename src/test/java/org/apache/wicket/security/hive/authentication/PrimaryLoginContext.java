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
import org.apache.wicket.security.pages.HighSecurityPage;


public final class PrimaryLoginContext extends LoginContext
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PrimaryLoginContext()
	{
		super(0);
	}


	public Subject login()
	{
		DefaultSubject defaultSubject = new DefaultSubject()
		{
			private static final long serialVersionUID = 1L;

			public boolean isClassAuthenticated(Class class1)
			{
				// for this test class authentication is enough
				if (class1 == null)
					return false;
				return !HighSecurityPage.class.isAssignableFrom(class1);
			}
			
			public boolean isComponentAuthenticated(Component component)
			{
				return true;
			}
			
			public boolean isModelAuthenticated(IModel model, Component component)
			{
				return true;
			}

		};
		defaultSubject.addPrincipal(new TestPrincipal("basic"));
		return defaultSubject;
	}

	/**
	 * @see org.apache.wicket.security.hive.authentication.LoginContext#preventsAdditionalLogins()
	 */
	public boolean preventsAdditionalLogins()
	{
		return false;
	}
}