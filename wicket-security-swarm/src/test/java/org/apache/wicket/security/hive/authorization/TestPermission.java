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
package org.apache.wicket.security.hive.authorization;

import org.apache.wicket.security.hive.authorization.Permission;

public class TestPermission extends Permission
{
	private static final long serialVersionUID = 1L;
	private String actions = "";

	public TestPermission(String name)
	{
		super(name);
	}

	public TestPermission(String name, String actions)
	{
		super(name);
		this.actions = actions;
	}

	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass().equals(this.getClass()))
		{
			TestPermission other = (TestPermission) obj;
			return other.getName().equals(getName()) && other.getActions().equals(getActions());
		}
		return false;
	}

	public String getActions()
	{
		return actions;
	}

	public int hashCode()
	{
		return getName().hashCode();
	}

	public boolean implies(Permission permission)
	{
		if (permission == this)
			return true;
		if (permission == null)
			return false;
		if (permission.getClass().equals(this.getClass()))
		{
			TestPermission other = (TestPermission) permission;
			return other.getName().equals(getName())
					&& getActions().indexOf(other.getActions()) > -1;
		}
		return false;
	}

}