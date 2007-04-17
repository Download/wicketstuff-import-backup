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
package org.wicketstuff.dojo.dojofx;

import java.util.StringTokenizer;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.Model;


/**
 * @author jcompagner
 *
 */
public class FXOnMouseOverFader extends DojoFXHandler
{

	private final boolean startDisplay;
	private final String type;
	private String HTMLID;
	private String componentId;
	private double startOpac;
	private double endOpac;



	/**
	 * Standard constructor for a fader which listens to trigger's mouseover
	 * events. if startDisplay is true, component will start faded out.
	 * 
	 * @param duration
	 * @param trigger
	 * @param startDisplay
	 *            whether component starts faded out.
	 */
	public FXOnMouseOverFader(int duration, Component trigger, boolean startDisplay)
	{
		super("onmouseover", duration, trigger);
		this.startDisplay = startDisplay;
		this.type = "fade";
	}

	/**
	 * Constructor for a fader with allowHide option. If allowhide is set to
	 * true, component's display property (style) will be set to none, and
	 * compoennt will dissapear upon fadeout.
	 * 
	 * @param duration
	 * @param trigger
	 * @param startDisplay
	 *            whether component starts faded out and with display=none.
	 * @param allowHide
	 */
	public FXOnMouseOverFader(int duration, Component trigger, boolean startDisplay,
			boolean allowHide)
	{
		super("onmouseover", duration, trigger);
		this.startDisplay = startDisplay;

		if (allowHide)
		{
			this.type = "fadeHide";
		}
		else
		{
			this.type = "fade";
		}
	}

	/**
	 * Constructor for a fader with a set starting and ending opacity. Component
	 * will fade from start to end opacity.
	 * 
	 * @param duration
	 * @param trigger
	 * @param startDisplay
	 *            whether component starts with opacity=startOpac
	 * @param startOpac
	 * @param endOpac
	 */
	public FXOnMouseOverFader(int duration, Component trigger, boolean startDisplay,
			double startOpac, double endOpac)
	{
		super("onmouseover", duration, trigger);
		this.startDisplay = startDisplay;

		this.type = "fadeOpac";

		if ((startOpac < 0.0) || (startOpac > 1.0))
		{
			System.out.println("WARNING: startOpac has to be between 0 and 1");
		}

		if ((endOpac < 0.0) || (endOpac > 1.0))
		{
			System.out.println("WARNING: endOpac has to be between 0 and 1");
		}

		this.startOpac = startOpac;
		this.endOpac = endOpac;

	}

	/*
	 * @see wicket.AjaxHandler#getBodyOnloadContribution()
	 */
	protected String getBodyOnloadContribution()
	{
		// set initial opacity to fadeOpac if type is fadeOpac, else to 0
		double initOpac = (type == "fadeOpac" ? startOpac : 0);
		// if starDisplay is false, set starting opacity of component to
		// initOpac.
		if (!startDisplay)
		{
			return "dojo.html.setOpacity(document.getElementById('" + HTMLID + "'), " + initOpac
					+ ");";
		}
		else
		{
			return null;
		}


	}
	
	/*
	 * removes the colons in the componentPath. In order to use in Javascript variables
	 */
	private String removeColon(String s) {
		  StringTokenizer st = new StringTokenizer(s,":",false);
		  String t="";
		  while (st.hasMoreElements()) t += st.nextElement();
		  return t;
	  }

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see wicket.AjaxHandler#onBind()
	 */
	protected void onBind()
	{
		Component c = getComponent();
		this.component = (Component)c;
		this.componentId = c.getId();
		
		String componentpath = removeColon(component.getPath());
		// create a unique HTML for the wipe component
		this.HTMLID = "f_" + this.component.getId() + "_" + componentpath;
		// Add ID to component, and bind effect to trigger
		this.component.add(new AttributeModifier("id", true, new Model(HTMLID)));

		/*
		 * add onmouseover and onmouseout handlers. setMouseOver handles correct
		 * mouseover states followed by fade() calls with needed variables.
		 */
		this.getTrigger().add(
				new AppendAttributeModifier(getEventName(), true, new Model(HTMLID
						+ "_setMouseOver(1);" + HTMLID + "_fade('" + HTMLID + "', "
						+ getDuration() + ");")));
		this.getTrigger().add(
				new AppendAttributeModifier("onmouseout", true, new Model(HTMLID
						+ "_setMouseOver(0);" + HTMLID + "_fade('" + HTMLID + "', "
						+ getDuration() + ");")));
	}

	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);
//		 String to be written to header
		String s;
		// dojo function calls for fadein/out
		String fadeInFunction;
		String fadeOutFunction;

		// check for type, and call dojo.lfx.html so that:
		// it fades node over duration (from startOpac to endOpac) and with
		// callback.
		// callback sets the right state variable to the present state and
		// does mouseover checks for stability improvements.
		// the following code might look a bit abracadabra, but it works and is
		// thouroughly stress-tested.
		if (type == "fadeHide")
		{
			fadeInFunction = "dojo.lfx.html.fadeShow(node, duration, null, function(){" + HTMLID
					+ "_faderState='fadedIn';if(" + HTMLID + "_mouseover=="
					+ (startDisplay ? 1 : 0) + "){" + HTMLID + "_fade(id, duration);}}).play()";
			fadeOutFunction = "dojo.lfx.html.fadeHide(node, duration, null, function(){" + HTMLID
					+ "_faderState='fadedOut';if(" + HTMLID + "_mouseover=="
					+ (startDisplay ? 0 : 1) + "){" + HTMLID + "_fade(id, duration);}}).play()";
		}
		else if (type == "fadeOpac")
		{
			fadeInFunction = "dojo.lfx.html.fade(node, {start:" + startOpac + ",end:" + endOpac   + "}, duration,"
					+ "null, function(){" + HTMLID + "_faderState='fadedIn';if(" + HTMLID
					+ "_mouseover==" + (startDisplay ? 1 : 0) + "){" + HTMLID
					+ "_fade(id, duration);}}).play();";
			fadeOutFunction = "dojo.lfx.html.fade(node, {start:" + endOpac + ",end:" +  startOpac  + "}, duration," 
					+ "null, function(){" + HTMLID + "_faderState='fadedOut';if(" + HTMLID
					+ "_mouseover==" + (startDisplay ? 0 : 1) + "){" + HTMLID
					+ "_fade(id, duration);}}).play();";
		}
		else
		{
			fadeInFunction = "dojo.lfx.html.fadeIn(node, duration, null, function(){" + HTMLID
					+ "_faderState='fadedIn';if(" + HTMLID + "_mouseover=="
					+ (startDisplay ? 1 : 0) + "){" + HTMLID + "_fade(id, duration);}}).play();";
			fadeOutFunction = "dojo.lfx.html.fadeOut(node, duration, null, function(){" + HTMLID
					+ "_faderState='fadedOut';if(" + HTMLID + "_mouseover=="
					+ (startDisplay ? 0 : 1) + "){" + HTMLID + "_fade(id, duration);}}).play();";
		}

		if (startDisplay)
		{
			s = "\t<script language=\"JavaScript\" type=\"text/javascript\">\n" + "\t"
					+ HTMLID + "_faderState = 'fadedIn'; \n" + "\t" + HTMLID
					+ "_mouseover = 0; \n";
		}
		else
		{
			s = "\t<script language=\"JavaScript\" type=\"text/javascript\">\n" + "\t"
					+ HTMLID + "_faderState = 'fadedOut'; \n" + "\t" + HTMLID
					+ "_mouseover = 0; \n";
		}

		s = s + "\tfunction " + HTMLID + "_fade(id, duration) { \n" + "\t\tif(" + HTMLID
				+ "_faderState!='fading'){\n" + "\t\t\tnode = document.getElementById(id);\n"
				+ "\t\t\tif(" + HTMLID + "_faderState == 'fadedOut') \n" + "\t\t\t{ \n"
				+ "\t\t\t\t" + HTMLID + "_faderState = 'fading';\n" + "\t\t\t\t"
				+ fadeInFunction + "\n" + "\t\t\t} else {\n" + "\t\t\t\t" + HTMLID
				+ "_faderState = 'fading';\n" + "\t\t\t\t" + fadeOutFunction + "\n" + "\t\t\t}\n"
				+ "\t\t}\n" + "\t}\n";


		s = s + "\tfunction " + HTMLID + "_setMouseOver(ismouseover){\n"
				+ "\t\tif (ismouseover == 1){\n" + "\t\t\t" + HTMLID + "_mouseover = 1;\n"
				+ "\t\t}else{\n" + "\t\t\t" + HTMLID + "_mouseover = 0;\n" + "\t\t}\n"
				+ "\t}\n" + "\t</script>\n\n";
		
		response.renderString(s);
	}

}
