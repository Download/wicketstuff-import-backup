package wicket.kronos.plugins.ToDo.adminpage;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import java.util.Date;
import java.util.GregorianCalendar;

import wicket.PageParameters;
import wicket.extensions.markup.html.datepicker.DatePicker;
import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;

/**
 * @author postma
 *
 */
public class AdminNewToDo extends Panel{

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param wicketId 
	 */
	public AdminNewToDo(String wicketId)
	{
		super(wicketId);
		InputForm inputForm = new InputForm("adminnewtodo");
		add(inputForm);
	}
	
	private class InputForm extends Form {
		
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private TextField titleText;
		private TextField subjectText;
		private TextArea contentText;
		private CheckBox doneCheck;
		
		/**
		 * @param name
		 */
		public InputForm(String name)
		{
			super(name, new CompoundPropertyModel(new ToDoItem()));

			add(titleText = new TextField("title", new Model()));
			add(subjectText = new TextField("subject", new Model()));
			add(contentText = new TextArea("content", new Model()));
			add(doneCheck = new CheckBox("done", new Model()));
		}
		
		@SuppressWarnings("boxing")
		@Override
		public void onSubmit()
		{
			PageParameters param = KronosSession.get().getPageParameters();
			String todoPluginUUID = param.getString("ID");
			Session jcrSession = ((KronosSession) KronosSession.get())
			.getJCRSession();
			
			try
			{
				Workspace ws = jcrSession.getWorkspace();
				QueryManager qm = ws.getQueryManager();
				Query q = qm.createQuery("//kronos:plugin/kronos:todoitems",
						Query.XPATH);

				QueryResult result = q.execute();
				NodeIterator it = result.getNodes();
				
				if(it.hasNext())
				{
					Node todoItems = it.nextNode();
				
					Node todoPluginNode = jcrSession.getNodeByUUID(todoPluginUUID);
					String todoPluginName = todoPluginNode.getProperty("kronos:name").getString();
					
					Node newItem = todoItems.addNode("kronos:todoitem");
					newItem.setProperty("kronos:pluginname", todoPluginName);
					newItem.setProperty("kronos:title", titleText.getModelObjectAsString());
					newItem.setProperty("kronos:subject", subjectText.getModelObjectAsString());
					newItem.setProperty("kronos:content", contentText.getModelObjectAsString());
					newItem.setProperty("kronos:done", (Boolean)doneCheck.getModelObject());
					GregorianCalendar cal = new GregorianCalendar();
					newItem.setProperty("kronos:date", cal);
					
					jcrSession.save();
					
					setResponsePage(AdminPage.class);
				}
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
	}
}
