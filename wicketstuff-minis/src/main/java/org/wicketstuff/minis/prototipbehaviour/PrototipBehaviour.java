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
package org.wicketstuff.minis.prototipbehaviour;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * Prototip behavior
 * 
 * The component you add this behavior to will be the component which the tooltip appears for
 * 
 * note this does not work if you add the behavior to a panel
 * 
 * 
 * @author Richard Wilkinson
 *
 */
public class PrototipBehaviour extends AbstractBehavior
{
	private static final long serialVersionUID = 1L;
	
	protected Component source;
	protected String tooltip = null;
	protected Component tooltipComponent = null;
	protected PrototipSettings settings = null;
	protected boolean overrideHeaderContributor = false;
	
	protected JS_TYPE selectedJsType = JS_TYPE.MIN;
	
	/**
	 * Default constructor
	 * If you use this then you must set either a string, or a component manually
	 */
	public PrototipBehaviour()
	{
	}
	
	/**
	 * Provide a simple string as a tooltip
	 * @param tooltip
	 */
	public PrototipBehaviour(String tooltip)
	{
		this.tooltip = tooltip;
	}

	/**
	 * Provide a component to show as the tool tip (eg a panel)
	 * @param panel
	 */
	public PrototipBehaviour(Component panel)
	{
		this.tooltipComponent = panel;
		panel.setOutputMarkupId(true);
	}

	/**
	 * Add the required css and js files to the page
	 * Permission to distribute prototip files given by prototip creator Nick Stakenburg (http://www.nickstakenburg.com)
	 * 
	 * Also add the javascript to create the tooltip
	 * 
	 *  @see org.apache.wicket.markup.html.IHeaderContributor#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderOnDomReadyJavascript(toJavascript());
		if(!overrideHeaderContributor)
		{
			response.renderCSSReference(new ResourceReference(PrototipBehaviour.class, "prototip.css"), "screen");
			switch(selectedJsType)
			{
				case NORMAL:
					response.renderJavascriptReference(new ResourceReference(PrototipBehaviour.class, "prototip.js"));
					break;
				case MIN:
					response.renderJavascriptReference(new ResourceReference(PrototipBehaviour.class, "prototip.js"));
					break;
				case MIN_GZIP:
					response.renderJavascriptReference(new ResourceReference(PrototipBehaviour.class, "prototip.js"));
					break;	
			}
		}
	}
	
	/**
	 * override bind so that the component you add this behavior to becomes the component the tooltip applies to
	 * 
	 */
	@Override
	public void bind(Component component) 
	{
		super.bind(component);
		this.source = component;
		source.setOutputMarkupId(true);
	}
	
	/**
	 * Given an ajax request target, remove this tip from the page
	 * @param target
	 */
	public void remove(AjaxRequestTarget target) 
	{
		if(source != null)
		{
			StringBuilder removeJs = new StringBuilder();
			removeJs.append("Tips.remove('").append(source.getMarkupId()).append("');");
			target.appendJavascript(removeJs.toString());
		}
	}

	/**
	 * Get string to add the prototip to the page
	 * @return the String
	 */
	protected String toJavascript()
	{
		StringBuilder script = new StringBuilder();
		String optionString = null;
		if(settings != null)
		{
			optionString = settings.getOptionsString();
		}
		if(tooltip != null)
		{
			script.append("new Tip($('").append(source.getMarkupId()).append("'),'").append(tooltip).append("'");
		}
		else if (tooltipComponent != null)
		{
			script.append("new Tip($('").append(source.getMarkupId()).append("'),$('").append(tooltipComponent.getMarkupId()).append("')");
		}
		if(optionString != null && !optionString.equals(""))
		{
			script.append(", ").append(optionString);
		}
		script.append(");");
		return script.toString();
	}

	/**
	 * @return the source
	 */
	public Component getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 * @return this object
	 */
	public PrototipBehaviour setSource(Component source) {
		this.source = source;
		return this;
	}

	/**
	 * @return the tooltip
	 */
	public String getTooltip() {
		return tooltip;
	}

	/**
	 * @param tooltip the tooltip to set
	 * @return this object
	 */
	public PrototipBehaviour setTooltip(String tooltip) {
		this.tooltip = tooltip;
		tooltipComponent = null;
		return this;
	}

	/**
	 * @return the tooltip component
	 */
	public Component getTooltipComponent() {
		return tooltipComponent;
	}

	/**
	 * @param panel the panel to set
	 * @return this object
	 */
	public PrototipBehaviour setTooltipComponent(Component tooltipComponent) {
		this.tooltipComponent = tooltipComponent;
		this.tooltipComponent.setOutputMarkupId(true);
		tooltip = null;
		return this;
	}

	/**
	 * @return the settings
	 */
	public PrototipSettings getSettings() {
		return settings;
	}

	/**
	 * @param settings the settings to set
	 * @return this object
	 */
	public PrototipBehaviour setSettings(PrototipSettings settings) {
		this.settings = settings;
		return this;
	}

	/**
	 * @return the overrideHeaderContributor
	 */
	public boolean isOverrideHeaderContributor() {
		return overrideHeaderContributor;
	}

	/**
	 * If you do not want this behavour to add the required javascript and css files to the header set this to true (default false)
	 * @param overrideHeaderContributor the overrideHeaderContributor to set
	 * @return this object
	 */
	public PrototipBehaviour setOverrideHeaderContributor(boolean overrideHeaderContributor) {
		this.overrideHeaderContributor = overrideHeaderContributor;
		return this;
	}

	/**
	 * @return the selectedJsType
	 */
	public JS_TYPE getSelectedJsType() {
		return selectedJsType;
	}

	/**
	 * There are 3 different js files which can be included:
	 * 	 a normal uncompressed one
	 * 	 a minified one
	 * 	 a minified and gziped one
	 *
	 *   To override the default (the minified one) set this parameter
	 *   
	 * @param selectedJsType the selectedJsType to set
	 * @return this object
	 */
	public PrototipBehaviour setSelectedJsType(JS_TYPE selectedJsType) {
		this.selectedJsType = selectedJsType;
		return this;
	}
}
