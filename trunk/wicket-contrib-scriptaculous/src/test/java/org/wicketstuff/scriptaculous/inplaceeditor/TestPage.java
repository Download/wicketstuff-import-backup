package org.wicketstuff.scriptaculous.inplaceeditor;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.Model;

public class TestPage extends WebPage {

	public TestPage() {
		add(new AjaxEditInPlaceLabel("label", new Model("me & you")));
	}
}
