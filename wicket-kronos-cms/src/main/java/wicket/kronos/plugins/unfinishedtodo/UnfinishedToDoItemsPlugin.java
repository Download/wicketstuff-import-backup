package wicket.kronos.plugins.unfinishedtodo;

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
import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.kronos.plugins.unfinishedtodo.panels.UnfinishedToDoItemsAdminPagePanel;
import wicket.kronos.plugins.unfinishedtodo.panels.UnfinishedToDoItemsFrontpagePanel;

/**
 * @author postma
 *
 */
public class UnfinishedToDoItemsPlugin extends IPlugin {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String toDoPluginName;
	private String toDoPluginUUID;
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
		toDoPluginName = getToDoPluginName();
		toDoPluginUUID = getToDoPluginUUID();
		
		List<ToDoItem> unfinishedToDoItems = this.getUnfinishedToDoItems(toDoPluginName);
		
		
		
		if (isAdmin)
		{
			add(new UnfinishedToDoItemsAdminPagePanel("unfinishedtodo", pluginUUID));
		}else {
			add(new UnfinishedToDoItemsFrontpagePanel("unfinishedtodo", unfinishedToDoItems, toDoPluginUUID));
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
		this(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
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
	
	public String getToDoPluginUUID()
	{
		String todoPluginUUID = null;
		
		Session jcrSession = ((KronosSession) KronosSession.get()).getJCRSession();
		
		try
		{
			Node node = jcrSession.getNodeByUUID(pluginUUID);
						
			todoPluginUUID = node.getProperty("kronos:plugincontentname").getString();
			
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery(
					"//kronos:plugininstantiations/kronos:plugininstance[@kronos:name = '" 
							+ toDoPluginName + "']", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			if (it.hasNext())
			{
				Node n = it.nextNode();
				
				todoPluginUUID = n.getUUID();
			}
			
		} catch(RepositoryException e) {
			e.printStackTrace();
		}	
		
		return todoPluginUUID;
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
				String title = n.getProperty("kronos:name").getString();
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

	@Override
	public boolean isConfigurable()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
