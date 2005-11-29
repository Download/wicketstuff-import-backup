/*
 * $Id$ $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.contrib.dojo.dojofx;

import wicket.AttributeModifier;
import wicket.Component;
import wicket.markup.html.HtmlHeaderContainer;
import wicket.model.Model;

/**
 * TODO HEY STUDENTS! didn't they tought you documentation is important? If
 * Martijn finds out, it'll cost you points.
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
		super("OnMouseOver", duration, trigger);
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
		super("OnmouseOver", duration, trigger);
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
		super("OnMouseOver", duration, trigger);
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
		System.out.println("bodycontributorfunctioncalled");
		if (!startDisplay)
		{
			System.out.println("dojo.html.setOpacity(document.getElementById('" + HTMLID + "'), "
					+ initOpac + ");");
			return "dojo.html.setOpacity(document.getElementById('" + HTMLID + "'), " + initOpac
					+ ");";
		}
		else
		{
			return null;
		}


	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wicket.AjaxHandler#renderHeadContribution(wicket.markup.html.HtmlHeaderContainer)
	 */
	protected void renderHeadContribution(HtmlHeaderContainer container)
	{
		// String to be written to header
		String s;
		// dojo function calls for fadein/out
		String fadeInFunction;
		String fadeOutFunction;

		// check for type, and call dojo.fx.html so that:
		// it fades node over duration (from startOpac to endOpac) and with
		// callback.
		// callback sets the right state variable to the present state and
		// does mouseover checks for stability improvements.
		// the following code might look a bit abracadabra, but it works and is
		// thouroughly stress-tested.
		if (type == "fadeHide")
		{
			fadeInFunction = "dojo.fx.html.fadeShow(node, duration, function(){" + componentId
					+ "_faderState='fadedIn';if(" + componentId + "_mouseover=="
					+ (startDisplay ? 1 : 0) + "){" + componentId + "_fade(id, duration);}})";
			fadeOutFunction = "dojo.fx.html.fadeHide(node, duration, function(){" + componentId
					+ "_faderState='fadedOut';if(" + componentId + "_mouseover=="
					+ (startDisplay ? 0 : 1) + "){" + componentId + "_fade(id, duration);}})";
		}
		else if (type == "fadeOpac")
		{
			fadeInFunction = "dojo.fx.html.fade(node, duration," + startOpac + "," + endOpac
					+ ", function(){" + componentId + "_faderState='fadedIn';if(" + componentId
					+ "_mouseover==" + (startDisplay ? 1 : 0) + "){" + componentId
					+ "_fade(id, duration);}});";
			fadeOutFunction = "dojo.fx.html.fade(node, duration," + endOpac + "," + startOpac
					+ ", function(){" + componentId + "_faderState='fadedOut';if(" + componentId
					+ "_mouseover==" + (startDisplay ? 0 : 1) + "){" + componentId
					+ "_fade(id, duration);}});";
		}
		else
		{
			fadeInFunction = "dojo.fx.html.fadeIn(node, duration, function(){" + componentId
					+ "_faderState='fadedIn';if(" + componentId + "_mouseover=="
					+ (startDisplay ? 1 : 0) + "){" + componentId + "_fade(id, duration);}})";
			fadeOutFunction = "dojo.fx.html.fadeOut(node, duration, function(){" + componentId
					+ "_faderState='fadedOut';if(" + componentId + "_mouseover=="
					+ (startDisplay ? 0 : 1) + "){" + componentId + "_fade(id, duration);}});";
		}

		if (startDisplay)
		{
			s = "\t<script language=\"JavaScript\" type=\"text/javascript\">\n" + "\t"
					+ componentId + "_faderState = 'fadedIn'; \n" + "\t" + componentId
					+ "_mouseover = 0; \n";
		}
		else
		{
			s = "\t<script language=\"JavaScript\" type=\"text/javascript\">\n" + "\t"
					+ componentId + "_faderState = 'fadedOut'; \n" + "\t" + componentId
					+ "_mouseover = 0; \n";
		}

		s = s + "\tfunction " + componentId + "_fade(id, duration) { \n" + "\t\tif(" + componentId
				+ "_faderState!='fading'){\n" + "\t\t\tnode = document.getElementById(id);\n"
				+ "\t\t\tif(" + componentId + "_faderState == 'fadedOut') \n" + "\t\t\t{ \n"
				+ "\t\t\t\t" + componentId + "_faderState = 'fading';\n" + "\t\t\t\t"
				+ fadeInFunction + "\n" + "\t\t\t} else {\n" + "\t\t\t\t" + componentId
				+ "_faderState = 'fading';\n" + "\t\t\t\t" + fadeOutFunction + "\n" + "\t\t\t}\n"
				+ "\t\t}\n" + "\t}\n";


		s = s + "\tfunction " + componentId + "_setMouseOver(ismouseover){\n"
				+ "\t\tif (ismouseover == 1){\n" + "\t\t\t" + componentId + "_mouseover = 1;\n"
				+ "\t\t}else{\n" + "\t\t\t" + componentId + "_mouseover = 0;\n" + "\t\t}\n"
				+ "\t}\n" + "\t</script>\n\n";
		container.getResponse().write(s);


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

		// create a unique HTML for the wipe component
		HTMLID = this.component.getId() + "_" + component.getPath();
		// Add ID to component, and bind effect to trigger
		this.component.add(new AttributeModifier("id", true, new Model(HTMLID)));

		/*
		 * add onmouseover and onmouseout handlers. setMouseOver handles correct
		 * mouseover states followed by fade() calls with needed variables.
		 */
		this.getTrigger().add(
				new AppendAttributeModifier(getEventName(), true, new Model(componentId
						+ "_setMouseOver(1);" + componentId + "_fade('" + HTMLID + "', "
						+ getDuration() + ");")));
		this.getTrigger().add(
				new AppendAttributeModifier("onmouseout", true, new Model(componentId
						+ "_setMouseOver(0);" + componentId + "_fade('" + HTMLID + "', "
						+ getDuration() + ");")));
	}

}
