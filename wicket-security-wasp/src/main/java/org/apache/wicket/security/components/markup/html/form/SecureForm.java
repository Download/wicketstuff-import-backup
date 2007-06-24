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
package org.apache.wicket.security.components.markup.html.form;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.AbstractWaspAction;
import org.apache.wicket.security.checks.ComponentSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.components.ISecureComponent;
import org.apache.wicket.security.components.SecureComponentHelper;

/**
 * A secure Form. if the form does not have sufficient enable rights it replaces
 * the tag with a div to prevent clientside form submits Also it automaticly
 * disables all children. Other than that it behaves exactly like a regular
 * {@link Form} with a {@link ComponentSecurityCheck} attached.
 * 
 * @author marrink
 */
public class SecureForm extends Form implements ISecureComponent
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public SecureForm(String id)
	{
		super(id);
		setSecurityCheck(new ComponentSecurityCheck(this));
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public SecureForm(String id, IModel model)
	{
		super(id, model);
		setSecurityCheck(new ComponentSecurityCheck(this));
	}

	/**
	 * @see org.apache.wicket.security.components.ISecureComponent#getSecurityCheck()
	 */
	public ISecurityCheck getSecurityCheck()
	{
		return SecureComponentHelper.getSecurityCheck(this);
	}

	/**
	 * @see org.apache.wicket.security.components.ISecureComponent#isActionAuthorized(java.lang.String)
	 */
	public boolean isActionAuthorized(String waspAction)
	{
		return SecureComponentHelper.isActionAuthorized(this, waspAction);
	}

	/**
	 * @see org.apache.wicket.security.components.ISecureComponent#isActionAuthorized(org.apache.wicket.security.actions.AbstractWaspAction)
	 */
	public boolean isActionAuthorized(AbstractWaspAction action)
	{
		return SecureComponentHelper.isActionAuthorized(this, action);
	}

	/**
	 * @see org.apache.wicket.security.components.ISecureComponent#isAuthenticated()
	 */
	public boolean isAuthenticated()
	{
		return SecureComponentHelper.isAuthenticated(this);
	}

	/**
	 * @see org.apache.wicket.security.components.ISecureComponent#setSecurityCheck(org.apache.wicket.security.checks.ISecurityCheck)
	 */
	public void setSecurityCheck(ISecurityCheck check)
	{
		SecureComponentHelper.setSecurityCheck(this, check);
	}

	/**
	 * Override to make sure the form can not be submitted clientside.
	 * Offcourse this does not prevent fake urls send to the server.
	 * @see org.apache.wicket.markup.html.form.Form#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		// prevent client from submitting this form
		if (!isEnableAllowed())
		{
			tag.setName("div");
			tag.remove("method");
			tag.remove("action");
			tag.remove("enctype");

			// auto disbale all children
			visitChildren(new IVisitor()
			{
				public Object component(Component component)
				{
					component.setEnabled(false);
					return IVisitor.CONTINUE_TRAVERSAL;
				}
			});
		}
	}
}
