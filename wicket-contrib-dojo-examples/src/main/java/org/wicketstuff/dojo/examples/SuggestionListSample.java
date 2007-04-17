package org.wicketstuff.dojo.examples;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.wicket.PageParameters;
import org.wicketstuff.dojo.markup.html.form.DojoDropDownChoice;
import org.wicketstuff.dojo.markup.html.form.suggestionlist.DojoRequestSuggestionList;
import org.wicketstuff.dojo.markup.html.form.suggestionlist.SuggestionList;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class SuggestionListSample extends WebPage {
	
	public static SuggestionList allItems = new SuggestionList();

	public SuggestionListSample(PageParameters parameters){
		allItems.put("Alabama", "Alabama");
		allItems.put("Alaska", "Alaska");
		allItems.put("American Samoa", "American Samoa");
		allItems.put("Arizona", "Arizona");
		allItems.put("Arkansas", "Arkansas");
		allItems.put("California", "California");
		allItems.put("Colorado", "Colorado");
		allItems.put("Connecticut", "Connecticut");
		allItems.put("Delaware", "Delaware");
		allItems.put("Columbia", "Columbia");
		allItems.put("Florida", "Florida");
		allItems.put("Georgia", "Georgia");
		allItems.put("Guam", "Guam");
		allItems.put("Hawaii", "Hawaii");
		allItems.put("Idaho", "Idaho");
		allItems.put("Illinois", "Illinois");
		allItems.put("Indiana", "Indiana");
		allItems.put("Iowa", "Iowa");
		allItems.put("Kansas", "Kansas");
		allItems.put("Kentucky", "Kentucky");
		allItems.put("Louisiana", "Louisiana");
		allItems.put("Maine", "Maine");
		allItems.put("Marshall Islands", "Marshall Islands");
		
		
		add(new DojoRequestSuggestionList( "list1"){

			public SuggestionList getMatchingValues(String pattern) {
				if (pattern.equals("")) return allItems;
				SuggestionList list = new SuggestionList();
				Iterator it = allItems.entrySet().iterator();
				while(it.hasNext()){
					Entry item = (Entry)it.next();
					if (((String)item.getValue()).toLowerCase().startsWith(pattern.toLowerCase())){
						list.put(item.getKey(), item.getValue());
					}
				}
				return list;
			}
		});
		
		ArrayList personList = new ArrayList();
		
		personList.add(new person("JBQ", 1));
		personList.add(new person("Eelco", 2));
		personList.add(new person("Igor", 3));
		personList.add(new person("Vincent", 4));
		
		DojoDropDownChoice choice = new DojoDropDownChoice("dropdown", personList, new ChoiceRenderer("name", "id"));
		choice.setHandleSelectionChange(true);
		add(choice);
	}
	
	public class person{
		private String name;
		private int id;
		
		public person(String name, int id) {
			super();
			this.name = name;
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		
		
	}
}
