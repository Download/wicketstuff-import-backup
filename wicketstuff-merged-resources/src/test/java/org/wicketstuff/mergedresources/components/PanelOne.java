package org.wicketstuff.mergedresources.components;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.panel.Panel;
import org.wicketstuff.mergedresources.annotations.CssContribution;
import org.wicketstuff.mergedresources.annotations.JsContribution;

@JsContribution(value = "PanelOne.js")
@CssContribution(value = "PanelOne.css")
public class PanelOne extends Panel {

	private static final long serialVersionUID = 1L;

	public PanelOne(String id) {
		super(id);
		add(HeaderContributor.forCss(PanelOne.class, PanelOne.class.getSimpleName() + ".css"));
		add(HeaderContributor.forJavaScript(PanelOne.class, PanelOne.class.getSimpleName() + ".js"));
		add(new MyForm("myForm"));
		add(new ComponentB("componentB"));
	}

}
