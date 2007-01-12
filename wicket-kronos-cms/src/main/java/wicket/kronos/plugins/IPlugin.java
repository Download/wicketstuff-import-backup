package wicket.kronos.plugins;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.kronos.KronosSession;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 */
@SuppressWarnings("boxing")
public abstract class IPlugin extends Panel {

	protected Boolean isAdmin;

	protected String pluginUUID;

	protected static String pluginName;

	protected Boolean isPublished;

	protected Integer order;

	protected Integer areaPosition;

	protected String pluginType;

	protected String contentUUID;

	/**
	 * Constructor without specific contentUUID
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public IPlugin(Boolean isAdmin, String pluginUUID, String pluginname, Boolean ispublished,
			Integer order, Integer areaposition, String pluginType)
	{
		super("plugin");
		assert (pluginUUID != null);
		assert (pluginname != null);
		assert (order >= 0);
		assert (areaposition >= 0);
		assert (pluginType != null);
		this.isAdmin = isAdmin;
		this.pluginUUID = pluginUUID;
		this.pluginName = pluginname;
		this.isPublished = ispublished;
		this.order = order;
		this.areaPosition = areaposition;
		this.pluginType = pluginType;
	}

	/**
	 * Constructor with specific contentUUID
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 * @param contentUUID
	 */
	public IPlugin(Boolean isAdmin, String pluginUUID, String pluginname, Boolean ispublished,
			Integer order, Integer areaposition, String pluginType, String contentUUID)
	{
		this(isAdmin, pluginUUID, pluginname, ispublished, order, areaposition, pluginType);
		this.contentUUID = contentUUID;
	}

	/**
	 * @return pluginUUID
	 */
	public String getPluginUUID()
	{
		return this.pluginUUID;
	}

	/**
	 * @return pluginName
	 */
	public String getPluginname()
	{
		return this.pluginName;
	}

	/**
	 * @return isPublished
	 */
	public boolean isPublished()
	{
		return this.isPublished;
	}

	/**
	 * Sets the isPublished variable to true.
	 */
	public void publish()
	{
		this.isPublished = true;
	}

	/**
	 * Sets the isPublished variable to false.
	 */
	public void unpublish()
	{
		this.isPublished = false;
	}

	/**
	 * @return order
	 */
	public int getOrder()
	{
		return this.order;
	}

	/**
	 * @return areaPosition
	 */
	public int getAreaPosition()
	{
		return this.areaPosition;
	}

	/**
	 * @return pluginType
	 */
	public String getPluginType()
	{
		return this.pluginType;
	}

	/**
	 * @param pluginname
	 */
	public void setPluginname(String pluginname)
	{
		this.pluginName = pluginname;
	}

	/**
	 * @param order
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}

	/**
	 * @param areaposition
	 */
	public void setAreaPosition(int areaposition)
	{
		this.areaPosition = areaposition;
	}

	/**
	 * @param pluginType
	 */
	public void setPluginType(String pluginType)
	{
		this.pluginType = pluginType;
	}

	/**
	 * @return if the plugin is configurable
	 */
	public abstract boolean isConfigurable();

	/**
	 * Default save method to save the data in the repository Plugins with more options should
	 * override this method
	 */
	public void save()
	{
		Session jcrSession = KronosSession.get().getJCRSession();
		Node pluginNode;
		try
		{
			pluginNode = jcrSession.getNodeByUUID(this.pluginUUID);
			pluginNode.setProperty("kronos:name", pluginName);
			pluginNode.setProperty("kronos:published", isPublished);
			pluginNode.setProperty("kronos:order", order);
			pluginNode.setProperty("kronos:position", areaPosition);
			pluginNode.setProperty("kronos:pluginType", pluginType);
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

	/**
	 * @return the contentUUID
	 */
	public String getContentUUID()
	{
		return contentUUID;
	}

	/**
	 * @param contentUUID
	 */
	public void setContentUUID(String contentUUID)
	{
		this.contentUUID = contentUUID;
	}
}
