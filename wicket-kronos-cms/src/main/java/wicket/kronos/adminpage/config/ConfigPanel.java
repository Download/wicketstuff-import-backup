package wicket.kronos.adminpage.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * @author postma
 */
public class ConfigPanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param wicketId
	 */
	public ConfigPanel(String wicketId)
	{
		super(wicketId);
		add(new ConfigForm("configForm"));
	}

	private class ConfigForm extends Form {

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		private DropDownChoice templateChoice = null;

		private TextField titleField = null;

		List<String> templateList = new ArrayList<String>();

		/**
		 * Constructor.
		 * 
		 * @param wicketId
		 */
		public ConfigForm(String wicketId)
		{
			super(wicketId);

			File file = new File("./src/webapp/templates"); 
			File[] files = file.listFiles(); 
			for (int i = 0; i < files.length; i++)
			{
				File theFile = files[i];
				if (theFile.isDirectory())
				{
					if (!theFile.getName().startsWith(".")) templateList.add(theFile.getName());
				}
			}

			Session jcrSession = KronosSession.get().getJCRSession();
			String templateName = null;
			String pageTitle = null;
			try
			{
				Node configuration = jcrSession.getRootNode().getNode("kronos:cms").getNode(
						"kronos:configuration");
				templateName = configuration.getProperty("kronos:activetemplate").getString();
				pageTitle = configuration.getProperty("kronos:pagetitle").getString();
			}
			catch (PathNotFoundException pnfe)
			{
				pnfe.printStackTrace();
			}
			catch (RepositoryException re)
			{
				re.printStackTrace();
			}

			titleField = new TextField("title", new Model());
			titleField.setModelObject(pageTitle);
			add(titleField);

			templateChoice = new DropDownChoice("templates", new Model(), templateList);
			templateChoice.setModelObject(templateName);
			add(templateChoice);
		}

		@Override
		public void onSubmit()
		{
			String templateName = templateChoice.getModelObjectAsString();
			String pageTitle = titleField.getModelObjectAsString();

			Session jcrSession = KronosSession.get().getJCRSession();

			try
			{
				Node root = jcrSession.getRootNode();
				Node configuration = root.getNode("kronos:cms").getNode("kronos:configuration");

				configuration.setProperty("kronos:activetemplate", templateName);
				configuration.setProperty("kronos:pagetitle", pageTitle);
				jcrSession.save();
				setResponsePage(AdminPage.class);
			}
			catch (RepositoryException e)
			{
				e.printStackTrace();
			}
		}
	}
}
