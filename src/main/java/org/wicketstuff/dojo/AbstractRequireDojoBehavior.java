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
package org.wicketstuff.dojo;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.wicketstuff.dojo.skin.manager.SkinManager;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * <p>
 * This behavior has to be extended to write a new Behavior (Handler) using
 * Dojo. Adding the required dojo libs (see <tt>dojo.require(...)</tt>) can
 * be achieved using the <code>setRequire()</code> method and adding the
 * required libs to the <tt>libs</tt> variable.
 * </p>
 * 
 * <p>
 * This behavior also takes care to refresh a Dojo component when it is
 * re-rendered via an ajax request.
 * </p>
 * 
 * @author vdemay
 */
public abstract class AbstractRequireDojoBehavior extends AbstractDefaultDojoBehavior {
	private RequireDojoLibs libs = new RequireDojoLibs();

	/*
	 * (non-Javadoc)
	 * 
	 * @see wicket.contrib.dojo.AbstractDefaultDojoBehavior#renderHead(wicket.markup.html.IHeaderResponse)
	 * TODO : is there a way to put all dojo.require at the same place on the rendered page??????
	 */
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		StringBuffer require = getRequire();

		response.renderJavascript(require, AbstractRequireDojoBehavior.class.getName());
		
		//Dojo auto parsing is disactivated so we declare here each widget we need to parse with dojo
		if (!(RequestCycle.get().getRequestTarget() instanceof AjaxRequestTarget)) {
			response.renderJavascript("djConfig.searchIds.push(\""+getComponent().getMarkupId()+"\");", getComponent().getMarkupId() + "DojoParse");
		}
	}
	
	public StringBuffer getRequire(){
		setRequire(libs); // will be implemented by childs
		StringBuffer require = new StringBuffer();

		Iterator ite = libs.iterator();
		while (ite.hasNext()) {
			require.append("	dojo.require(\"");
			require.append((String) ite.next());
			require.append("\");\n");
		}

		require.append("\n");
		
		return require;
	}

	/**
	 * allow subclass to register new Dojo require libs
	 * 
	 * @param libs
	 */
	public abstract void setRequire(RequireDojoLibs libs);

	/**
	 * this method is used to interpret dojoWidgets rendered via XMLHTTPRequest
	 */
	protected void onComponentRendered() {
		
		// if a Dojo Widget is rerender needs to run some javascript to refresh
		// it. TargetRefresherManager contains top level dojo widgets
		if (RequestCycle.get().getRequestTarget() instanceof AjaxRequestTarget) {
			AjaxRequestTarget target = (AjaxRequestTarget) RequestCycle.get().getRequestTarget();
			//and register listener
			target.addListener(TargetRefresherManager
					.getInstance());
			TargetRefresherManager.getInstance().addComponent(getComponent());
			onComponentReRendered(target);
		}
	}
	

	/**
	 * Add Javascript scripts when a dojo component is Rerender in a
	 * {@link AjaxRequestTarget}
	 * 
	 * @param ajaxTarget
	 */
	public void onComponentReRendered(AjaxRequestTarget ajaxTarget) {

	}

	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("widgetId", getComponent().getMarkupId());
		SkinManager.getInstance().setSkinOnComponent(getComponent(), this, tag);
	}

	/**
	 * @author vdemay
	 * 
	 */
	@SuppressWarnings("serial")
	public class RequireDojoLibs extends HashSet<String> {

	}

}
