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
package org.apache.wicket.security.hive;

import java.security.CodeSource;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.security.hive.authentication.Subject;
import org.apache.wicket.security.hive.authorization.Permission;
import org.apache.wicket.security.hive.authorization.Principal;
import org.apache.wicket.security.util.ManyToManyMap;

/**
 * Basic implementation of a Hive. It contains basic add methods to facilitate factories.
 * It also might be locked after which no changes can be made to the hive.
 * @author marrink
 */
public class BasicHive implements Hive
{
	/**
	 * Maps {@link Permission}s to {@link Principal}s
	 */
	private ManyToManyMap principals;

	private boolean locked = false;

	public BasicHive()
	{
		principals = new ManyToManyMap(500); // guess lots of principals
	}

	/**
	 * Locks this hive. No changes are allowed anymore. After this {@link #isLocked()}
	 * will return true;
	 */
	public final void lock()
	{
		locked = true;
	}

	/**
	 * Check if the hive is locked. If the hive is locked no changes can be made.
	 * @return true if the hive is locked, false otherwise.
	 */
	public boolean isLocked()
	{
		return locked;
	}

	/**
	 * Adds a new Principal to the hive.
	 * @param principal the principal
	 * @param permissions a required collection of granted permissions for the principal
	 * @throws IllegalStateException if the hive is locked
	 * @throws IllegalArgumentException if either parameter is null
	 */
	public void addPrincipal(Principal principal, Collection permissions)
	{
		if (isLocked())
			throw new IllegalStateException("While the hive is locked no changes are allowed.");
		if (principal == null)
			throw new IllegalArgumentException("A principal is required.");
		if (permissions == null)
			throw new IllegalArgumentException("At least one permission is required for principal "
					+ principal);
		Iterator it = permissions.iterator();
		while (it.hasNext())
			principals.add((Permission) it.next(), principal);
	}

	/**
	 * Adds a new permission to a principal.
	 * @param principal the principal
	 * @param permission the permission granted
	 * @see #addPrincipal(CodeSource, Principal, List)
	 * @throws IllegalStateException if the hive is locked
	 * @throws IllegalArgumentException if either parameter is null
	 */
	public void addPermission(Principal principal, Permission permission)
	{
		if (isLocked())
			throw new IllegalStateException("While the hive is locked no changes are allowed.");
		if (principal == null)
			throw new IllegalArgumentException("A principal is required.");
		if (permission == null)
			throw new IllegalArgumentException("A permission is required.");
		principals.add(permission, principal);
	}

	/**
	 * @see org.apache.wicket.security.hive.Hive#containsPrincipal(org.apache.wicket.security.hive.authorization.Principal)
	 */
	public boolean containsPrincipal(Principal principal)
	{
		return principals.contains(principal);
	}

	/**
	 * @see org.apache.wicket.security.hive.Hive#hasPermision(org.apache.wicket.security.hive.authentication.Subject,
	 *      org.apache.wicket.security.hive.authorization.Permission)
	 */
	public boolean hasPermision(Subject subject, Permission permission)
	{
		// TODO caching
		if (hasPrincipal(subject, principals.get(permission)))
			return true;
		// permission has no exact match, perform an implies check
		Iterator it = principals.iterator();
		Object temp = null;
		Permission possibleMatch = null;
		while (it.hasNext())
		{
			temp = it.next();
			if (temp instanceof Permission)
			{
				possibleMatch = (Permission) temp;
				if (!possibleMatch.implies(permission))
					continue;
				if (hasPrincipal(subject, principals.get(possibleMatch)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the subject has or implies any of the principals in the set.
	 * @param subject optional authenticated subject
	 * @param principalSet set of principals
	 * @return true if the subject has or implies atleast one of the principals, false
	 *         otherwise.
	 */
	private boolean hasPrincipal(Subject subject, Set principalSet)
	{
		if (!principalSet.isEmpty())
		{
			Iterator it = principalSet.iterator();
			Principal temp;
			Set subjectPrincipals;
			if (subject == null)
				subjectPrincipals = Collections.EMPTY_SET;
			else
				subjectPrincipals = subject.getPrincipals();
			while (it.hasNext())
			{
				temp = (Principal) it.next();
				if (subjectPrincipals.contains(temp) || temp.implies(subject))
					return true;
			}
		}
		return false;
	}

	/**
	 * @see org.apache.wicket.security.hive.Hive#containsPermission(org.apache.wicket.security.hive.authorization.Permission)
	 */
	public boolean containsPermission(Permission permission)
	{
		return principals.contains(permission);
	}
}
