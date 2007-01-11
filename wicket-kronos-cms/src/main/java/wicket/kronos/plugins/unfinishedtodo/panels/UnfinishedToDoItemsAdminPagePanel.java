package wicket.kronos.plugins.unfinishedtodo.panels;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.adminpage.AdminPanel;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.model.Model;

/**
 * @author roeloffzen
 */
public class UnfinishedToDoItemsAdminPagePanel extends AdminPanel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String pluginUUID;

	/**
	 * Constructor.
	 * 
	 * @param wicketId
	 * @param pluginUUID
	 */
	public UnfinishedToDoItemsAdminPagePanel(String wicketId, String pluginUUID)
	{
		super(wicketId, pluginUUID);
		this.pluginUUID = pluginUUID;
		List<String> names = this.getTodoPluginNames();
		add(new UnfinishedForm("unfinishedform", names));
	}

	/**
	 * Retreive a list with todoplugin names
	 * 
	 * @return todoplugin names
	 */
	private List<String> getTodoPluginNames()
	{
		List<String> todoNames = new ArrayList<String>();

		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm
					.createQuery(
							"//kronos:plugininstantiations/kronos:plugininstance[@kronos:pluginType='wicket.kronos.plugins.ToDo.ToDoPlugin'] order by @kronos:name ascending ",
							Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();
				String name = n.getProperty("kronos:name").getString();
				todoNames.add(name);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}

		return todoNames;
	}

	/**
	 * Change the name of the todo plugin
	 * 
	 * @param name
	 */
	private void saveTodoPluginName(String name)
	{
		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Node unfinishedToDoPlugin = jcrSession.getNodeByUUID(pluginUUID);
			unfinishedToDoPlugin.setProperty("kronos:plugincontentname", name);
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

	private class UnfinishedForm extends Form {

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		private DropDownChoice todoName;

		/**
		 * Constructor.
		 * 
		 * @param wicketId
		 * @param names
		 */
		public UnfinishedForm(String wicketId, List<String> names)
		{
			super(wicketId);
			todoName = new DropDownChoice("todoname", new Model(), names);
			add(todoName);
		}

		@Override
		public void onSubmit()
		{
			saveTodoPluginName(todoName.getModelObjectAsString());
			setResponsePage(AdminPage.class);
		}

	}
}
