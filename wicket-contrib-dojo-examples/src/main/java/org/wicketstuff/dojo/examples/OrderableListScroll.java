package org.wicketstuff.dojo.examples;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.wicketstuff.dojo.markup.html.container.DojoSimpleContainer;
import org.wicketstuff.dojo.markup.html.list.DojoOrderableListRemover;
import org.wicketstuff.dojo.markup.html.list.DojoOrderableListScrollerBehavior;
import org.wicketstuff.dojo.markup.html.list.DojoOrderableListView;
import org.wicketstuff.dojo.markup.html.list.DojoOrderableListContainer;
import org.wicketstuff.dojo.markup.html.list.DojoOrderableRepeatingView;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.Model;

public class OrderableListScroll extends WebPage {

	static final List objList  = new  ArrayList();
	DojoOrderableRepeatingView list;
	DojoOrderableListContainer container;
	DojoSimpleContainer simpleContainer;
	
	public OrderableListScroll() {
		super();
		if (objList.size() == 0){
			objList.add("foo1");
			objList.add("bar1");
			objList.add("foo2");
			objList.add("bar2");
			objList.add("foo3");
			objList.add("bar3");
			objList.add("foo4");
			objList.add("bar4");
			objList.add("foo5");
			objList.add("bar5");
			objList.add("foo6");
			objList.add("bar6");
		}
		
		simpleContainer = new DojoSimpleContainer("scrollable");
		add(simpleContainer);
		container = new DojoOrderableListContainer("container");
		container.add(new DojoOrderableListScrollerBehavior(simpleContainer));
		simpleContainer.add(container);
		list = new DojoOrderableRepeatingView("list"){

			public void moveItem(int from, int to, AjaxRequestTarget target) {
				String drag = (String) objList.remove(from);
				objList.add(to, drag);
				
			}

			public void removeItem(Item item, AjaxRequestTarget target) {
				objList.remove(item.getModelObject());
				
			}

			protected Iterator getItemModels() {
				ArrayList modelList = new ArrayList();
				Iterator it = objList.iterator();
				while (it.hasNext()){
					modelList.add(new Model((String)it.next()));
				}
				return modelList.iterator();
			}

			protected void populateItem(Item item) {
				item.add(new Label("label",(String)item.getModelObject()));
				item.add(new DojoOrderableListRemover("remover", item));
			}
			
		};
		container.add(list);
		
		add(new AjaxLink("link"){

			public void onClick(AjaxRequestTarget target) {
				DojoOrderableListContainer containerNew = new DojoOrderableListContainer("container");
				containerNew.add(new DojoOrderableListScrollerBehavior(simpleContainer));
				DojoOrderableRepeatingView listNew = new DojoOrderableRepeatingView("list"){

					public void moveItem(int from, int to, AjaxRequestTarget target) {
						String drag = (String) objList.remove(from);
						objList.add(to, drag);
						
					}

					public void removeItem(Item item, AjaxRequestTarget target) {
						objList.remove(item);
						
					}

					protected Iterator getItemModels() {
						ArrayList modelList = new ArrayList();
						Iterator it = objList.iterator();
						while (it.hasNext()){
							modelList.add(new Model((String)it.next()));
						}
						return modelList.iterator();
					}

					protected void populateItem(Item item) {
						item.add(new Label("label",(String)item.getModelObject()));
						item.add(new DojoOrderableListRemover("remover", item));
					}
					
				};
				containerNew.add(listNew);
				container.replaceWith(containerNew);
				container = containerNew;
				target.addComponent(containerNew);
			}
		});
	}

}
