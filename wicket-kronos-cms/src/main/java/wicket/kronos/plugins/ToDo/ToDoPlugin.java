package wicket.kronos.plugins.ToDo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import wicket.PageParameters;
import wicket.kronos.KronosSession;
import wicket.kronos.plugins.IPlugin;
import wicket.kronos.plugins.ToDo.adminpage.AdminToDoPanel;
import wicket.kronos.plugins.ToDo.frontpage.FrontToDoPanel;

/**
 * @author postma
 *
 */
public class ToDoPlugin extends IPlugin {

	/**
	 * Default serialVErsionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public ToDoPlugin(Boolean isAdmin, String pluginUUID, String pluginname, Boolean ispublished, Integer order, Integer areaposition, String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order, areaposition, pluginType);
		List<ToDoItem> todoItems = this.getToDoItems();
		
		if (isAdmin)
		{
			PageParameters param = KronosSession.get().getPageParameters();
			String action = "";
			if(param.containsKey("action"))
			{
				action = param.getString("action");
			}
			if(action.equalsIgnoreCase("") || action == null)
			{
				add(new AdminToDoPanel("todoplugin", todoItems, pluginUUID));
			} else {	
				add(new AdminToDoPanel("todoplugin", (ToDoItem)null));
			}
		}else {
			add(new FrontToDoPanel("todoplugin", todoItems));
		}
	}
	
	/**
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 * @param contentUUID
	 */
	public ToDoPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType, String contentUUID)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
		ToDoItem todoItem = (ToDoItem) this.getToDoItem(contentUUID);
		if (isAdmin)
		{
			add(new AdminToDoPanel("todoplugin", todoItem));
		} else
		{
			add(new FrontToDoPanel("todoplugin"));
		}
	}
	
	/**
	 * Retreive all ToDo Items from Repository
	 * 
	 * @return List<ToDoItem>
	 */
	@SuppressWarnings("boxing")
	public List<ToDoItem> getToDoItems()
	{
		List<ToDoItem> todoItems = new ArrayList<ToDoItem>();
		ToDoItem todoItem = null;

		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery(
					"//kronos:plugin/kronos:todoitems/kronos:todoitem[@kronos:pluginname = '"+pluginName+"'] order by @kronos:done ascending", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();
				
				String todoUUID = n.getUUID();
				String title = n.getProperty("kronos:title").getString();
				String subject = n.getProperty("kronos:subject").getString();
				String description = n.getProperty("kronos:content").getString();
				Boolean done = n.getProperty("kronos:done").getBoolean();
				Calendar date = n.getProperty("kronos:date").getDate();
				
				todoItem = new ToDoItem(todoUUID, title, subject, description, done, date);
				todoItems.add(todoItem);
			}
		} catch(RepositoryException e) {
			e.printStackTrace();
		}
		return todoItems;
	}
	
	/**
	 * Retreive a list with unfinished todo items
	 * 
	 * @return List<ToDoItem>
	 */
	@SuppressWarnings("boxing")
	public List<ToDoItem> getUnfinishedToDoItems()
	{
		List<ToDoItem> todoItems = new ArrayList<ToDoItem>();
		ToDoItem todoItem = null;

		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery(
					"//kronos:plugin/kronos:todoitems/kronos:todoitem[@kronos:pluginname = '" 
							+ this.pluginName + "' @kronos:done='false']", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();
				
				String todoUUID = n.getUUID();
				String title = n.getProperty("kronos:title").getString();
				String subject = n.getProperty("kronos:subject").getString();
				String description = n.getProperty("kronos:content").getString();
				Boolean done = n.getProperty("kronos:done").getBoolean();
				Calendar date = n.getProperty("kronos:date").getDate();
				
				todoItem = new ToDoItem(todoUUID, title, subject, description, done, date);
				todoItems.add(todoItem);
			}
		} catch(RepositoryException e) {
			e.printStackTrace();
		}
		return todoItems;
	}
	
	/**
	 * Retreive one specific ToDo Item
	 * 
	 * @param contentUUID
	 * @return List<ToDoItem>
	 */
	@SuppressWarnings("boxing")
	public ToDoItem getToDoItem(String contentUUID)
	{
		ToDoItem todoItem = null;

		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Node n = jcrSession.getNodeByUUID(contentUUID);
			
			String title = n.getProperty("kronos:title").getString();
			String subject = n.getProperty("kronos:subject").getString();
			String description = n.getProperty("kronos:content").getString();
			Boolean done = n.getProperty("kronos:done").getBoolean();
			Calendar date = n.getProperty("kronos:date").getDate();
			
			todoItem = new ToDoItem(contentUUID, title, subject, description, done, date);
			
		} catch(RepositoryException e) {
			e.printStackTrace();
		}
		return todoItem;
	}
				
	/**
	 * Store a new or changed ToDo Item to the Repository
	 * 
	 * @param changedToDoItem
	 */
	@SuppressWarnings("boxing")
	public void saveToDoItem(ToDoItem changedToDoItem)
	{
		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Node n = jcrSession.getNodeByUUID(changedToDoItem.getTodoUUID());

			n.setProperty("title", changedToDoItem.getTitle());
			n.setProperty("subject", changedToDoItem.getSubject());
			n.setProperty("content", changedToDoItem.getContent());
			n.setProperty("done", changedToDoItem.getDone());
			n.setProperty("date", changedToDoItem.getDate());

			jcrSession.save();

		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isConfigurable()
	{
		return true;
	}
	
}
