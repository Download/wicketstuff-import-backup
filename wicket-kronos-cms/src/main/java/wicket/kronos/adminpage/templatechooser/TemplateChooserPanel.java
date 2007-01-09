package wicket.kronos.adminpage.templatechooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

public class TemplateChooserPanel extends Panel{

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public TemplateChooserPanel(String wicketId)
	{
		super(wicketId);
		add(new TemplateForm("templateForm"));
	}
	
	private class TemplateForm extends Form{

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private DropDownChoice templateChoice = null;
		List templateList = new ArrayList();
		
		public TemplateForm(String wicketId)
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
			
			templateChoice = new DropDownChoice("templates", new Model(), templateList);
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
