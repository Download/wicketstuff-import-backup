package org.apache.wicket.contrib.markup.html.tooltip;



import org.apache.wicket.AttributeModifier;
import org.apache.wicket.contrib.dojo.markup.html.tooltip.DojoTooltip;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 
 * 
 * based on textsoft.it's multy-line html tooltip tutorial: 
 * <a href="http://www.texsoft.it/index.php?c=software&m=sw.js.htmltooltip&l=it">texsoft.it's Tooltip tutoroial</a><br/>
 * 
 * In short this is a fully customizable Javascript-HTML-Layout Wicket Tooltip.
 * What does it do? Well you make a MVOTooltip.java (My Very Own Tooltip)<br/>
 * which extends Tooltip.java, and write the corresponding MVOTooltip.html<br/> 
 * as if it were the HTML for a panel, you can make you're very own cusomized tooltip.<br/> 
 *
 * Note: Using setter methods afeter construction probably wont do much good, <br/>
 * because instance fields are used to render AttributeModifiers in the initTooltip() method.<br/>
 *
 *for usage examples see: <br/>
 *<a href="http://www.jroller.com/page/ruudmarco?entry=tooltip_tutioral_part_one">Tutorial 1: Static Tooltip</a><br/>
 *<a href="http://www.jroller.com/comments/ruudmarco/Weblog/tooltip_tutioral_part_2_dynamic">Tutorial 2: Dynamic Tooltip</a>
 * @author Marco & Ruud
 * @deprecated will be remove in 2.0 use {@link DojoTooltip}
 */

public class Tooltip extends Panel
{

	private final TooltipPanel tooltipPanel;
	private final WebMarkupContainer iframe;
	
	/**
	 * @param id
	 * @param panel
	 */
	public Tooltip(String id, TooltipPanel panel)
	{
		super(id);
		this.tooltipPanel = panel;
		add(panel);
		add(iframe = new WebMarkupContainer("iframe"));
		iframe.add(new AttributeModifier("id", true, new Model(tooltipPanel.getIFrameID())));
	}

	/**
	 * @return The iframe
	 */
	public WebMarkupContainer getIframe()
	{
		return iframe;
	}
	
	/**
	 * @return The tooltip panel
	 */
	public TooltipPanel getTooltipPanel()
	{
		return tooltipPanel;
	}
}
