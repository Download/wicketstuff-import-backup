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
package wicket.contrib.dojo.widgets;

import java.util.Iterator;
import java.util.Map.Entry;

import wicket.MarkupContainer;
import wicket.markup.ComponentTag;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;

/**
 * {@link WebMarkupContainer} with style attribute
 * @author vdemay
 *
 */
public class StylingWebMarkupContainer extends WebMarkupContainer
{

	private StyleAttribute style;
	
	/**
	 * @param parent
	 * @param id
	 */
	public StylingWebMarkupContainer(MarkupContainer parent, String id)
	{
		super(parent, id);
		style = new StyleAttribute();
	}
	
	/**
	 * @param parent
	 * @param id
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	public StylingWebMarkupContainer(MarkupContainer parent, String id, IModel model)
	{
		super(parent, id, model);
		style = new StyleAttribute();
	}
	
	protected void onComponentTag(ComponentTag tag)
	{
		super.onComponentTag(tag);
		onStyleAttribute(style);
		Iterator<Entry<String, String>> ite = style.entrySet().iterator();
		String styleTag = "";
		while (ite.hasNext()){
			Entry<String,String> entry = ite.next();
			styleTag += entry.getKey()+":"+entry.getValue()+";";
		}
		if (styleTag != null && !"".equals(styleTag)){
			tag.put("style", styleTag);	
		}
	}
	
	/**
	 * @param height
	 */
	public final void setHeight(String height){
		style.setHeight(height);
	}
	
	/**
	 * @param width
	 */
	public final void setWidth(String width){
		style.setWidth(width);
	}
	
	/**
	 * @param minHeight
	 */
	public void setMinHeight(String minHeight){
		style.setMinHeight(minHeight);
	}
	
	/**
	 * @param minWidth
	 */
	public void setMinWidth(String minWidth){
		style.setMinWidth(minWidth);
	}
	
	/**
	 * @param display
	 */
	public final void setDisplay(String display){
		style.setDisplay(display);
	}
	
	protected void onStyleAttribute(StyleAttribute styleAttribute){
		
	}
	

}
