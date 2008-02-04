package org.wicketstuff.openlayers;

import org.apache.wicket.markup.html.basic.Label;
import org.wicketstuff.openlayers.api.PopupWindowPanel;

public class PopupPanel extends PopupWindowPanel {
	public PopupPanel(String text) {
		super();
		add(new Label("text",text));
	}

}
