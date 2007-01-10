package wicket.kronos.frontpage;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.AttributeModifier;
import wicket.Component;
import wicket.PageParameters;
import wicket.kronos.Area;
import wicket.kronos.DataProcessor;
import wicket.kronos.KronosPage;
import wicket.kronos.KronosSession;
import wicket.kronos.plugins.IPlugin;
import wicket.markup.html.WebComponent;
import wicket.markup.html.basic.Label;
import wicket.markup.html.resources.StyleSheetReference;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * @author postma
 */
public class Frontpage extends KronosPage {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String templateName;
	private String pageTitle;

	/**
	 * The constructor of Frontpage accepts a PageParameters object as a
	 * parameter the PageParameter object can have a parameter combination of
	 * the String "IDType" with a value and the String "ID" with a value
	 * 
	 * @param pageParameters
	 */
	public Frontpage(PageParameters pageParameters)
	{
		Session jcrSession = KronosSession.get().getJCRSession();
		try
		{
			Node configuration = jcrSession.getRootNode().getNode("kronos:cms").getNode("kronos:configuration");
			templateName = configuration.getProperty("kronos:activetemplate").getString();
			pageTitle = configuration.getProperty("kronos:pagetitle").getString();
		}
		catch (PathNotFoundException e1)
		{
			e1.printStackTrace();
		}
		catch (RepositoryException e1)
		{
			e1.printStackTrace();
		}
		add(new Label("title", pageTitle));
		WebComponent c = new WebComponent( "css" );
		  IModel model = new Model()
		  {
		      public Object getObject( Component c )
		      {	
		    	  return "templates/"+ templateName + "/css/style.css";
		      }
		  };
		  c.add( new AttributeModifier( "href", model ) );
		  add( c );
		
		//add(new StyleSheetReference("css", getClass(), "templates/"+ templateName + "/css/style.css"));
		
		if (pageParameters == null || pageParameters.isEmpty())
		{
			this.init(-1, null);
		} else
		{
			KronosSession currentsession = KronosSession.get();
			currentsession.setPageParameters(pageParameters);
			String IDType = pageParameters.getString("IDType");
			String ID = pageParameters.getString("ID");
			if (IDType.equalsIgnoreCase("content"))
			{
				IPlugin plugin = DataProcessor.getPluginByContent(false, ID);
				this.init(plugin.getAreaPosition(), plugin);
			} else
			{
				IPlugin plugin = DataProcessor.getPlugin(false, ID);
				this.init(plugin.getAreaPosition(), plugin);
			}
		}

		/*
		 * Temporary: Generate a XML-File with the entire contents of the
		 * repository
		 */
		try
		{
			Session session = KronosSession.get().getJCRSession();
			FileOutputStream out = new FileOutputStream("output.xml");
			session.exportDocumentView(session.getRootNode().getPath(), out,
					false, false);
		}
		catch (PathNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
	}

	private void init(int changedArea, IPlugin plugin)
	{
		/*
		 * Create all the area objects for this page, the Area objects are being
		 * initialised with the plugins that reside on the specific area.
		 */
		assert (changedArea >= 0);
		/* area, 0=header, 1=left, 2=center, 3=right, 4=footer */
		Area area = null;
		for (int areaCounter = 0; areaCounter <= 4; areaCounter++)
		{
			if (changedArea != areaCounter)
			{
				area = new Area(areaCounter);
				add(area);
			} else
			{
				area = new Area(areaCounter, plugin);
				add(area);
			}
		}
	}
}
