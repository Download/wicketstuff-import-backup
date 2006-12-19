package wicket.kronos.plugins.ToDo.adminpage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wicket.PageParameters;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.adminpage.AdminPanel;
import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * @author postma
 *
 */
public class AdminToDoOverview extends AdminPanel{

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private List<ToDoItem> todoItemList;
	private String todoPluginUUID;
	
	/**
	 * @param wicketId
	 * @param todoItemList
	 */
	public AdminToDoOverview(String wicketId, List<ToDoItem> todoItemList, String todoPluginUUID)
	{
		super(wicketId, todoPluginUUID);
		this.todoItemList = todoItemList;
		this.todoPluginUUID = todoPluginUUID;
		add(new ToDoForm("todoform"));
	}	
	
	public class ToDoForm extends Form {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public ToDoForm(String wicketId)
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
					param.put("IDType", "content");
					param.put("ID", todoItem.getTodoUUID());
					
					item.add(new BookmarkablePageLink("titlelink", AdminPage.class, param).add(
							new Label("title", new Model(todoItem.getTitle()))));
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
		
		public void onSubmit()
		{
			PageParameters param = new PageParameters();
			param.add("IDType", "plugin");
			param.add("ID", todoPluginUUID);
			param.add("action", "new");
			
			setResponsePage(AdminPage.class, param);
		}
	}
}
