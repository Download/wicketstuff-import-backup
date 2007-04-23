package org.wicketstuff.dojo.examples;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.wicketstuff.dojo.markup.html.DojoLink;
import org.wicketstuff.dojo.markup.html.container.DojoSimpleContainer;
import org.wicketstuff.dojo.markup.html.container.IDojoContainer;
import org.wicketstuff.dojo.markup.html.container.page.DojoPageContainer;
import org.wicketstuff.dojo.markup.html.container.tab.DojoTabContainer;
import org.apache.wicket.markup.html.WebPage;

public class TabContainerSample extends WebPage {

	public TabContainerSample() {
		super();
		final DojoTabContainer container = new DojoTabContainer("tabContainer"){
			@Override
			public void onSelectionChange(IDojoContainer selected, AjaxRequestTarget target) {
				System.out.println(selected.getMarkupId());
			}
		};
		add(container);
		container.setHeight("500px");
		
		final DojoSimpleContainer tab1 = new DojoSimpleContainer("tab1", "title1");
		container.add(tab1);
		
		final DojoSimpleContainer tab2 = new DojoSimpleContainer("tab2", "title2");
		container.add(tab2);
		
		DojoPageContainer page = new DojoPageContainer("tab3", DatePickerShower.class);
		page.setTitle("title3");
		container.add(page);
		//select the second item
		container.setSelected(tab1);
				
		add(new DojoLink("refresh"){

			@Override
			public void onClick(AjaxRequestTarget target) {
				container.setSelected(tab2);
				target.addComponent(container);
			}
			
		});
		
	}

}
