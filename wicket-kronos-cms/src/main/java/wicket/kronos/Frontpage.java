package wicket.kronos;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.PageParameters;
import wicket.kronos.plugins.IPlugin;

/**
 * @author postma
 */
public class Frontpage extends KronosPage {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor of Frontpage accepts a PageParameters object as a
	 * parameter the PageParameter object can have a parameter combination of
	 * the String "IDType" with a value and the String "ID" with a value
	 * 
	 * @param pageParameters
	 */
	public Frontpage(PageParameters pageParameters)
	{

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
