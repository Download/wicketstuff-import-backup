package wicket.kronos.adminpage.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

public class ConfigPanel extends Panel{

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public ConfigPanel(String wicketId)
	{
		super(wicketId);
		add(new ConfigForm("templateForm"));
	}
	
	private class ConfigForm extends Form{

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private DropDownChoice templateChoice = null;
		List templateList = new ArrayList();
		
		public ConfigForm(String wicketId)
		{
			super(wicketId);
			
			File pathFile = new File("");
			File file = new File(pathFile.getAbsoluteFile() + "/src/webapp/templates");
			File[] files = file.listFiles();
			for(int i = 0; i < files.length; i++)
			{
				File theFile = files[i];
				if(theFile.isDirectory())
				{
					if(!theFile.getName().startsWith("."))
						templateList.add(theFile.getName());
				}
			}
			
			Session jcrSession = KronosSession.get().getJCRSession();
			String templateName = null;
			try
			{
				Node configuration = jcrSession.getRootNode().getNode("kronos:cms").getNode("kronos:configuration");
				templateName = configuration.getProperty("kronos:activetemplate").getString();
			}
			catch (PathNotFoundException e1)
			{
				e1.printStackTrace();
			}
			catch (RepositoryException e1)
			{
				e1.printStackTrace();
			}
			
			
			templateChoice = new DropDownChoice("templates", new Model(), templateList);
			templateChoice.setModelObject(templateName);
			add(templateChoice);
		}
		
		public void onSubmit()
		{
			String templateName = templateChoice.getModelObjectAsString();
			
			Session jcrSession = KronosSession.get().getJCRSession();

			try
			{
				Node root = jcrSession.getRootNode();
				Node configuration = root.getNode("kronos:cms").getNode("kronos:configuration");
				
				configuration.setProperty("kronos:activetemplate", templateName);
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
