package wicket.kronos.plugins;

import wicket.model.Model;

/**
 * @author postma
 *
 */
public class PluginProperties extends Model {
	
	/**
	 * Default serialVersionUUID
	 */
	private static final long serialVersionUID = 1L;

	private String pluginUUID;

	private String name;

	private boolean published;

	private int order;

	private int position;

	private String pluginType;

	/**
	 * Empty constructor for use as Model object
	 */
	public PluginProperties()
	{
		
	}
	
	/**
	 * @param pluginUUID
	 * @param name
	 * @param published
	 * @param order
	 * @param position
	 * @param pluginType
	 */
	public PluginProperties(String pluginUUID, String name, boolean published, int order,
			int position, String pluginType)
	{
		this.pluginUUID = pluginUUID;
		this.name = name;
		this.published = published;
		this.order = order;
		this.position = position;
		this.pluginType = pluginType;
	}

	/**
	 * @return name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return order
	 */
	public int getOrder()
	{
		return order;
	}

	/**
	 * @param order
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}

	/**
	 * @return plugintype
	 */
	public String getPluginType()
	{
		return pluginType;
	}

	/**
	 * @param pluginType
	 */
	public void setPluginType(String pluginType)
	{
		this.pluginType = pluginType;
	}

	/**
	 * @return pluginUUID
	 */
	public String getPluginUUID()
	{
		return pluginUUID;
	}

	/**
	 * @param pluginUUID
	 */
	public void setPluginUUID(String pluginUUID)
	{
		this.pluginUUID = pluginUUID;
	}

	/**
	 * @return position
	 */
	public int getPosition(){
		return position;
	}

	/**
	 * @param position
	 */
	public void setPosition(int position)
	{
		this.position = position;
	}

	/**
	 * @return published
	 */
	public boolean getPublished()
	{
		return published;
	}

	/**
	 * @param published
	 */
	public void setPublished(boolean published)
	{
		this.published = published;
	}
}
