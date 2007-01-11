package wicket.kronos.plugins.ToDo.adminpage;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.PageParameters;
import wicket.kronos.DataProcessor;
import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.adminpage.AdminPanel;
import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;
import wicket.model.PropertyModel;

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
	 * Constructor.
	 * @param wicketId
	 * @param todoItemList
	 * @param todoPluginUUID 
	 */
	public AdminToDoOverview(String wicketId, List<ToDoItem> todoItemList, String todoPluginUUID)
	{
		super(wicketId, todoPluginUUID);
		this.todoItemList = todoItemList;
		this.todoPluginUUID = todoPluginUUID;
		add(new ToDoForm("todoform"));
	}	
	
	/**
	 * @author postma
	 *
	 */
	public class ToDoForm extends Form {

		/**
		 * Default serialVErsionUID
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Form that generates an overview of all ToDo items
		 * 
		 * @param wicketId
		 */
		public ToDoForm(String wicketId)
		{
			super(wicketId,  new CompoundPropertyModel(new ToDoItemsModel(todoItemList)));
			final ListView todoList = new ListView("todoRepeater", todoItemList)
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
					
					item.add(new CheckBox("deletecheck", new PropertyModel(todoItem, "delete")));
					item.add(new BookmarkablePageLink("titlelink", AdminPage.class, param).add(
							new Label("title", new Model(todoItem.getTitle()))));
					item.add(new Label("subject", new Model(todoItem.getSubject())));
					item.add(new Label("content", new Model(todoItem.getContent())));
					item.add(new CheckBox("done", new PropertyModel(todoItem, "done")));
					Date date = todoItem.getDate().getTime();
					SimpleDateFormat blogDateFormat = new SimpleDateFormat(
							"dd-MM-yyyy");
					item.add(new Label("date", blogDateFormat.format(date)));
				}
			};
			
			add(todoList);
			
			add(new Button("newitembutton") 
			{
				@Override
				public void onSubmit()
				{
					PageParameters param = new PageParameters();
					param.add("IDType", "plugin");
					param.add("ID", todoPluginUUID);
					param.add("action", "new");
					
					setResponsePage(AdminPage.class, param);
				}				
			});
			
			add(new Button("deletebutton")
			{
				@Override
				public void onSubmit()
				{
					List<ToDoItem> todoItems = ((ToDoItemsModel)ToDoForm.this.getModelObject()).getTodoItems();			
					Iterator todoItemIterator = todoItems.iterator();
					
					while(todoItemIterator.hasNext())
					{
						ToDoItem todoItem = (ToDoItem)todoItemIterator.next();
						if(todoItem.isDelete())
						{
							DataProcessor.removeContent(todoItem.getTodoUUID());
						}
					}
					
					PageParameters param = new PageParameters();
					param.add("IDType", "plugin");
					param.add("ID", todoPluginUUID);
					
					setResponsePage(AdminPage.class, param);
				}
			});
			
			add(new Button("savebutton")
			{
				@Override
				public void onSubmit()
				{
					List<ToDoItem> todoItems = ((ToDoItemsModel)ToDoForm.this.getModelObject()).getTodoItems();
					Iterator todoItemIterator = todoItems.iterator();
					
					Session jcrSession = KronosSession.get().getJCRSession();
					
					while(todoItemIterator.hasNext())					{
						
						ToDoItem todoItem = (ToDoItem)todoItemIterator.next();
						
						try
						{
							Node todoItemNode = jcrSession.getNodeByUUID(todoItem.getTodoUUID());
							todoItemNode.setProperty("kronos:done", todoItem.getDone());
							jcrSession.save();
						}
						catch (ItemNotFoundException e)
						{
							e.printStackTrace();
						}
						catch (RepositoryException e)
						{
							e.printStackTrace();
						}
					}
					
					PageParameters param = new PageParameters();
					param.add("IDType", "plugin");
					param.add("ID", todoPluginUUID);
					
					setResponsePage(AdminPage.class, param);
				}
			});
		}
	}
	
	/**
	 * @author postma
	 *
	 * Model for setting and getting a @see List of todo items
	 */
	public class ToDoItemsModel implements Serializable{
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		
		private List<ToDoItem> todoItems;
		
		/**
		 * @param todoItems
		 */
		public ToDoItemsModel(List<ToDoItem> todoItems)
		{
			this.todoItems = todoItems;
		}

		/**
		 * @return the todoItems
		 */
		public List<ToDoItem> getTodoItems()
		{
			return todoItems;
		}

		/**
		 * @param todoItems the todoItems to set
		 */
		public void setTodoItems(List<ToDoItem> todoItems)
		{
			this.todoItems = todoItems;
		}
	}
}
