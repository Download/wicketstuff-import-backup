package wicket.kronos.plugins.ToDo.frontpage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wicket.PageParameters;
import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * @author postma
 *
 */
public class FrontToDoOverview extends Panel{

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param wicketId
	 * @param todoItemList
	 */
	public FrontToDoOverview(String wicketId, List<ToDoItem> todoItemList)
	{
		super(wicketId);
		
		add(new ListView("todoRepeater", todoItemList)
		{
			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				ToDoItem todoItem = (ToDoItem)item.getModelObject();
				
				PageParameters param = new PageParameters();
				param.put("IDType", "plugin");
				param.put("ID", todoItem.getTodoUUID());
				
				item.add(new Label("title", new Model(todoItem.getTitle())));
				item.add(new Label("subject", new Model(todoItem.getSubject())));
				item.add(new Label("content", new Model(todoItem.getContent())));
				item.add(new CheckBox("done", new Model(todoItem.getDone())));
				Date date = todoItem.getDate().getTime();
				SimpleDateFormat blogDateFormat = new SimpleDateFormat(
						"dd-MM-yyyy");
				item.add(new Label("date", blogDateFormat.format(date)));
			}
		});
	}	
}
