package wicket.kronos.plugins.banner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.kronos.CMSImageResource;
import wicket.kronos.KronosSession;
import wicket.kronos.plugins.IPlugin;
import wicket.kronos.plugins.banner.panels.BannerAdminpagePanel;
import wicket.kronos.plugins.banner.panels.BannerFrontpagePanel;
import wicket.markup.html.image.resource.DynamicImageResource;

/**
 * Simple BannerPlugin. Retrieves the image from the repository and shows it on the screen.
 * 
 * @author roeloffzen
 */
public class BannerPlugin extends IPlugin {

	/**
	 * Default serialUID
	 */
	private static final long serialVersionUID = 1L;

	private String imageUUID;

	/**
	 * Default plugin constructor Retrieves the image data from the repository and create a new
	 * BannerImageResource with that data. than adds a new BannerFrontpagePanel.
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public BannerPlugin(Boolean isAdmin, String pluginUUID, String pluginname, Boolean ispublished,
			Integer order, Integer areaposition, String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order, areaposition, pluginType);
		DynamicImageResource resource = this.getImage();
		if (isAdmin)
		{
			add(new BannerAdminpagePanel("bannerplugin", pluginUUID, imageUUID));
		} else
		{
			add(new BannerFrontpagePanel("bannerplugin", resource));
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

			Node bannerNode = jcrSession.getNodeByUUID(pluginUUID);
			Node imageNode = bannerNode.getProperty("kronos:bannerimage").getNode();
			imageUUID = imageNode.getUUID();
			InputStream input = imageNode.getNode("jcr:content").getProperty("jcr:data")
					.getStream();

			/*
			 * Retrieve the image data from the repository and put it into a byteArray
			 */
			String imageName = imageNode.getName();
			ByteArrayOutputStream destination = new ByteArrayOutputStream();
			try
			{
				byte[] buffer = new byte[1024];
				for (int n = input.read(buffer); n != -1; n = input.read(buffer))
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
			byte[] image = destination.toByteArray();

			/* Create a BannerImageResource with the byteArray as a */
			resource = new CMSImageResource(image, imageName);
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
