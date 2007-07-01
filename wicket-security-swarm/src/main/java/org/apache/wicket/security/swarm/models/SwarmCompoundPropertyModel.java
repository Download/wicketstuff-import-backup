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
package org.apache.wicket.security.swarm.models;

import org.apache.wicket.Component;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.security.models.SecureCompoundPropertyModel;

/**
 * Swarm version of {@link SecureCompoundPropertyModel}. Because of the
 * wrappedmodel it was not as easy as slapping an implements SwarmModel on this
 * class, now it is as easy as providing an implementation for
 * {@link SwarmModel#getSecurityId(Component)}
 * 
 * @author marrink
 */
public abstract class SwarmCompoundPropertyModel extends SecureCompoundPropertyModel
		implements
			SwarmModel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param object
	 */
	public SwarmCompoundPropertyModel(Object object)
	{
		super(object);
	}

	/**
	 * @see org.apache.wicket.security.models.SecureCompoundPropertyModel#wrapOnInheritance(org.apache.wicket.Component)
	 */
	public IWrapModel wrapOnInheritance(Component component)
	{
		return new AttachedSwarmCompoundPropertyModel(component);
	}

	/**
	 * A wrapping model delegating all security calls to the
	 * {@link SwarmCompoundPropertyModel} instance.
	 * 
	 * @author marrink
	 */
	protected class AttachedSwarmCompoundPropertyModel extends AttachedSecureCompoundPropertyModel
			implements
				SwarmModel
	{
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 * Construct.
		 * 
		 * @param owner
		 */
		public AttachedSwarmCompoundPropertyModel(Component owner)
		{
			super(owner);
		}

		/**
		 * 
		 * @see org.apache.wicket.security.swarm.models.SwarmModel#getSecurityId(org.apache.wicket.Component)
		 */
		public String getSecurityId(Component component)
		{
			return SwarmCompoundPropertyModel.this.getSecurityId(component != null
					? component
					: getOwner());
		}

	}

}
