package org.wicketstuff.accordion;

import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

public class AccordionPanelItem extends Panel {

	public final static String ITEM_ID = "item";

	public AccordionPanelItem(String title, List<WebMarkupContainer> items) {
		super(ITEM_ID);
		add(new Label("title", title));
		add(new ListView("content", items) {
			@Override
			protected void populateItem(ListItem item) {
				WebMarkupContainer child = (WebMarkupContainer) item
						.getModelObject();
				item.add(child);

			}
		});
	}

}
