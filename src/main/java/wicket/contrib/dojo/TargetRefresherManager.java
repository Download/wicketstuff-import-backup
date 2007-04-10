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
package wicket.contrib.dojo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import wicket.Component;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.AjaxRequestTarget.IJavascriptResponse;
import wicket.ajax.AjaxRequestTarget.IListener;

/**
 * A Manager to deal with {@link AjaxRequestTarget} and makeWidget in dojo.
 * Only top level dojoComponents needs to be rerender during a response 
 * to a Ajax Query
 * 
 * @author Vincent Demay
 *
 */
public class TargetRefresherManager implements IListener
{
	private static TargetRefresherManager instance;
	private HashMap<String, Component> dojoComponents;
	private AjaxRequestTarget target;
	
	private TargetRefresherManager(){	
		dojoComponents = new HashMap<String, Component>();
	}
	
	public static TargetRefresherManager getInstance(){
		if (instance == null){
			instance = new TargetRefresherManager();
		}
		return instance;
	}
	

	public void onAfterRespond(Map map, IJavascriptResponse response)
	{
		//we need to find all dojoWidget that should be reParsed
		Iterator it = dojoComponents.entrySet().iterator();
		HashMap<String, Component> real = new HashMap<String, Component>();
		
		while (it.hasNext()){
			Component c = (Component)((Entry)it.next()).getValue();
			if (!hasParentAdded(c)){
				//we do not need to reParse This widget, remove it
				real.put(c.getMarkupId(), c);
			}
		}
		dojoComponents = real;

		if (generateReParseJs()!=null){
			response.addJavascript(generateReParseJs());
		}
		instance=null;
		
	}

	public void onBeforeRespond(Map map, AjaxRequestTarget target)
	{	
		this.target = target;
	}
	
	/**
	 * 
	 */
	public void addComponent(Component component){
		dojoComponents.put(component.getMarkupId(), component);
	}
	
	private String generateReParseJs(){
		if (!dojoComponents.isEmpty()){
			Iterator it = dojoComponents.values().iterator();
			String parseJs = "[";
			while (it.hasNext()){
				Component c = (Component)it.next();
				parseJs += "'" + c.getMarkupId() + "',";
			}
			parseJs = parseJs.substring(0, parseJs.length() -1);
			parseJs += "]";
			return "djConfig.searchIds = " + parseJs + ";dojo.hostenv.makeWidgets();";
		}
		else return null;
	}
	
	/**
	 * Look for ancestor in the hierarchie
	 * @param component 
	 * @return
	 */
	private boolean hasParentAdded(Component component){
		Component current = component;
		while(current.getParent()!= null){
			 if (dojoComponents.containsKey(current.getParent().getMarkupId())){
				 return true;
			 }
			 current = current.getParent();
		}
		return false;
	}

	

}
