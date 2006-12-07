package wicket.kronos.plugins.banner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import wicket.kronos.plugins.banner.panels.BannerAdminpagePanel;
import wicket.kronos.plugins.banner.panels.BannerFrontpagePanel;
import wicket.markup.html.image.resource.DynamicImageResource;

/**
 * Simple BannerPlugin. Retrieves the image from the repository and shows it on
 * the screen.
 * 
 * @author roeloffzen
 */
public class BannerPlugin extends IPlugin {

	/**
	 * Default serialUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default plugin constructor Retrieves the image data from the repository
	 * and create a new BannerImageResource with that data. than adds a new
	 * BannerFrontpagePanel.
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public BannerPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
		if(isAdmin)
		{
			add(new BannerAdminpagePanel("panel", pluginUUID));
		} else {
			add(new BannerFrontpagePanel("panel", this.getImage()));
		}
	}
	
	/**
	 * Retrieve the image for the banner from the repository
	 * 
	 * @return DynamicImageResource resource
	 */
	public DynamicImageResource getImage()
	{
		Session jcrSession = KronosSession.get().getJCRSession();
		DynamicImageResource resource = null;

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery(
					"//kronos:content/kronos:plugin/kronos:banner[@kronos:pluginname='"
							+ pluginName + "']", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			if (it.hasNext())
			{
				Node bannerNode = it.nextNode();
				Node imageNode = bannerNode.getProperty("kronos:image")
						.getNode();
				InputStream input = imageNode.getNode("jcr:content")
						.getProperty("jcr:data").getStream();

				/*
				 * Retrieve the image data from the repository and put it into a
				 * byteArray
				 */
				byte[] image;
				ByteArrayOutputStream destination = new ByteArrayOutputStream();
				try
				{
					byte[] buffer = new byte[1024];
					for (int n = input.read(buffer); n != -1; n = input
							.read(buffer))
					{
						destination.write(buffer, 0, n);
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						input.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				image = destination.toByteArray();
				/*
				 * Create a BannerImageResource with the byteArray as a
				 * parameter
				 */
				resource = new BannerImageResource(image);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		
		return resource;
	}
	
	@Override
	public boolean isConfigurable()
	{
		return false;
	}

}
