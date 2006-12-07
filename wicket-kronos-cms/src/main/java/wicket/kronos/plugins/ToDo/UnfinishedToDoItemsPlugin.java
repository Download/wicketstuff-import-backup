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

import wicket.kronos.KronosSession;
import wicket.kronos.plugins.IPlugin;
import wicket.kronos.plugins.ToDo.adminpage.AdminToDoPanel;
import wicket.kronos.plugins.ToDo.frontpage.FrontToDoPanel;

/**
 * @author postma
 *
 */
public class UnfinishedToDoItemsPlugin extends IPlugin {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create ToDo plugin with list of unfinished todo items
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 * @param todoPlugin 
	 */
	public UnfinishedToDoItemsPlugin(Boolean isAdmin, String pluginUUID, String pluginname, 
			Boolean ispublished, Integer order, Integer areaposition, String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order, areaposition, pluginType);
		List<ToDoItem> unfinishedToDoItems = this.getUnfinishedToDoItems( getToDoPluginName());
		
		if (isAdmin)
		{
			add(new AdminToDoPanel("unfinishedtodoplugin", unfinishedToDoItems));
		}else {
			add(new FrontToDoPanel("unfinishedtodoplugin", unfinishedToDoItems));
		}
	}

	/**
	 * Create ToDo plugin with one unfinished todo item
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 * @param contentUUID
	 * @param todoPlugin
	 */
	public UnfinishedToDoItemsPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType, String contentUUID)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
		ToDoItem todoItem = (ToDoItem) this.getToDoItem(contentUUID);
		if (isAdmin)
		{
			add(new AdminToDoPanel("unfinishedtodoplugin", todoItem));
		} else
		{
			add(new FrontToDoPanel("unfinishedtodoplugin"));
		}
	}

	/**
	 * Retreive the todo plugin name from which we will use the content 
	 * 
	 * @return todoPluginName 
	 */
	public String getToDoPluginName()
	{
		String todoPluginName = null;
		
		Session jcrSession = ((KronosSession) KronosSession.get()).getJCRSession();
		
		try
		{
			Node node = jcrSession.getNodeByUUID(pluginUUID);
						
			todoPluginName = node.getProperty("kronos:plugincontentname").getString();
			
		} catch(RepositoryException e) {
			e.printStackTrace();
		}	
		
		return todoPluginName;
	}
	
	/**
	 * Retreive a list with todo items that correspond with a specific pluginname
	 * 
	 * @param pluginName
	 * @return List<ToDoItem>
	 */
	@SuppressWarnings("boxing")
	public List<ToDoItem> getToDoItemsByPluginname(String pluginName)
	{
		List<ToDoItem> todoItems = new ArrayList<ToDoItem>();
		ToDoItem todoItem = null;

		Session jcrSession = ((KronosSession) KronosSession.get()).getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("kronos:plugin/kronos:todoitems/kronos:todoitem[@kronos:pluginname = '" 
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
	 * Retreive a list with unfinished todo items
	 * 
	 * @return List<ToDoItem>
	 */
	@SuppressWarnings("boxing")
	public List<ToDoItem> getUnfinishedToDoItems(String contentPluginName)
	{
		List<ToDoItem> todoItems = new ArrayList<ToDoItem>();
		ToDoItem todoItem = null;

		Session jcrSession = ((KronosSession) KronosSession.get()).getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery(
					"//kronos:plugin/kronos:todoitems/kronos:todoitem[@kronos:pluginname = '" 
							+ contentPluginName + "' and @kronos:done='false']", Query.XPATH);

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
	
	@Override
	public boolean isConfigurable()
	{
		return true;
	}
	
}
