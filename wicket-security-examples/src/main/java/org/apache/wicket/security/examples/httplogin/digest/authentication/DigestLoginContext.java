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
package org.apache.wicket.security.examples.httplogin.digest.authentication;

import org.apache.wicket.security.examples.authorization.MyPrincipal;
import org.apache.wicket.security.hive.authentication.DefaultSubject;
import org.apache.wicket.security.hive.authentication.LoginContext;
import org.apache.wicket.security.hive.authentication.Subject;

/**
 * @author marrink
 */
public class DigestLoginContext extends LoginContext
{

	private transient String username;

	/**
	 * Construct.
	 * 
	 * @param username
	 */
	public DigestLoginContext(String username)
	{
		super();
		this.username = username;

	}

	/**
	 * @see org.apache.wicket.security.hive.authentication.LoginContext#login()
	 */
	public Subject login()
	{
		// username password combo is already verified, just get the user object
		// and create a subject for it
		// getting the user object is being skipped in this example
		DefaultSubject subject = new DefaultSubject();
		subject.addPrincipal(new MyPrincipal("digest"));
		username = null; // clean up
		return subject;
	}

}
