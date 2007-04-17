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
package org.wicketstuff.dojo.markup.html.toaster;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.wicketstuff.dojo.DojoIdConstants;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Duration;

/**
 * A dojo toaster is a message that will appear in a corner of the screen such.
 * <p>
 * <b>Sample</b>
 * 	<pre>
 * package org.wicketstuff.dojo.examples;
 * 
 * import org.apache.wicket.PageParameters;
 * import org.apache.wicket.ajax.AjaxRequestTarget;
 * import org.apache.wicket.ajax.markup.html.AjaxLink;
 * import org.wicketstuff.dojo.markup.html.toaster.DojoToaster;
 * import org.apache.wicket.markup.html.WebPage;
 * import org.apache.wicket.model.Model;
 * import org.apache.wicket.util.time.Duration;
 * 
 * public class ToasterSample extends WebPage {
 * 	
 * 	public ToasterSample(PageParameters parameters){
 * 		final DojoToaster toaster1 = new DojoToaster("toaster1",new Model("Some messages here. Funy isn\'t it ;)"));
 * 		AjaxLink link1 = new AjaxLink( "link1"){
 * 			public void onClick(AjaxRequestTarget target) {
 * 				toaster1.publishMessage(target);
 * 				
 * 			}
 * 		};
 * 		
 * 		final DojoToaster toaster2 = new DojoToaster("toaster2",new Model("Some messages here. Funy isn\'t it ;)"));
 * 		AjaxLink link2 = new AjaxLink("link2"){
 * 			public void onClick(AjaxRequestTarget target) {
 * 				toaster2.publishMessage(target,DojoToaster.ERROR);
 * 				
 * 			}
 * 		};
 * 
 * 		
 * 		final DojoToaster toaster4 = new DojoToaster("toaster4",new Model("Some messages here. Funy isn\'t it ;)"));
 * 		toaster4.setPosition(DojoToaster.BOTTOM_LEFT_UP);
 * 		toaster4.setDuration(Duration.seconds(10));
 * 		AjaxLink link4 = new AjaxLink("link4"){
 * 			public void onClick(AjaxRequestTarget target) {
 * 				toaster4.publishMessage(target,DojoToaster.WARNING);
 * 				
 * 			}
 * 		};
 * 		
 * 		add(toaster1);
 * 		add(link1);
 * 		add(toaster2);
 * 		add(link2);
 * 		add(toaster4);
 * 		add(link4);
 * 	}
 * }
 * 
 *  </pre>
 * </p>
 * @author <a href="http://www.demay-fr.net/blog/index.php/en">Vincent Demay</a>
 *
 */
@SuppressWarnings("serial")
public class DojoToaster extends WebMarkupContainer{

	public static final String INFO = "INFO";
	public static final String WARNING = "WARNING";
	public static final String ERROR = "ERROR";
	public static final String FATAL = "FATAL";
	
	public static final String BOTTOM_RIGHT_UP = "br-up";
	public static final String BOTTOM_RIGHT_LEFT = "br-left";
	public static final String BOTTOM_LEFT_UP = "bl-up";
	public static final String BOTTOM_LEFT_RIGHT = "bl-right";
	public static final String TOP_RIGHT_DOWM = "tr-down";
	public static final String TOP_RIGHT_LEFT = "tr-left";
	public static final String TOP_LEFT_DOWN = "tl-down";
	public static final String TOP_LEFT_RIGHT = "tl-right";
	
	private String position;
	private Duration duration;
	/**
	 * Construct a new DojoToaster. The message displayed in it is the model
	 * @param id the name of the widget
	 * @param model A String representing the message
	 */
	public DojoToaster(String id, IModel model){
		super(id, model);
		add(new DojoToasterHandler());
	}

	/**
	 * Construct a new DojoToaster. The message displayed in it is the model
	 * @param id the name of the widget
	 */
	public DojoToaster(String id){
		super(id);
		add(new DojoToasterHandler());
	}

	protected void onComponentTag(ComponentTag tag){
		super.onComponentTag(tag);
		tag.put(DojoIdConstants.DOJO_TYPE, DojoIdConstants.DOJO_TYPE_TOASTER);
		if (position != null){
			tag.put("positionDirection", position);
		}
		tag.put("messageTopic", getMarkupId());
		tag.put("templateCssPath", urlFor(new ResourceReference(DojoToaster.class, "Toaster.css")));
		if (duration != null){
			tag.put("showDelay", duration.getMilliseconds()+"");
		}
		tag.put("separator", "<hr/>");
	}

	/**
	 * get the position where the toaster will be displayed
	 * @return the position where the toaster will be displayed
	 */
	public String getPosition(){
		return position;
	}

	/**
	 * set the position where the toaster will be displayed
	 * @param position the position where the toaster will be displayed
	 */
	public void setPosition(String position){
		this.position = position;
	}
	
	/**
	 * Show the massage
	 * @param target ajax target
	 */
	public void publishMessage(AjaxRequestTarget target){
		target.appendJavascript("dojo.event.topic.publish(\"" + getMarkupId() + "\",\"" + getModelObject() + "\")");
	}

	/**
	 * get the duration
	 * @return duration see {@link Duration}
	 */
	public Duration getDuration(){
		return duration;
	}

	/**
	 * set the direction
	 * @param duration duration see {@link Duration}
	 */
	public void setDuration(Duration duration){
		this.duration = duration;
	}
	
	/**
	 * Show the massage
	 * @param target ajax target
	 */
	public void publishMessage(AjaxRequestTarget target, String type){
		target.appendJavascript("dojo.event.topic.publish(\"" + getMarkupId() + "\",{message:\"" + getModelObject() + "\",type:\"" + type + "\"})");
	}

}
