package wicket.kronos.plugins.unfinishedtodo.panels;

import java.util.List;

import wicket.PageParameters;
import wicket.kronos.frontpage.Frontpage;
import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;

public class UnfinishedToDoItemsFrontpagePanel extends Panel{

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public UnfinishedToDoItemsFrontpagePanel(String wicketId, List unfinishedToDoItems, final String toDoPluginUUID)
	{
		super(wicketId);
		
		BookmarkablePageLink link;
		
		PageParameters param = new PageParameters();
		param.add("IDType", "plugin");
		param.add("ID", toDoPluginUUID);
		add(link = new BookmarkablePageLink("todolink", Frontpage.class, param));
		link.add(new ListView("linkrepeater", unfinishedToDoItems){

			@Override
			protected void populateItem(ListItem item)
			{
				ToDoItem todoItem = (ToDoItem)item.getModelObject();
				item.add(new Label("todolinklabel", todoItem.getTitle()));
			}
			
		});
	}

}
