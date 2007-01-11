package wicket.kronos.plugins;

import java.io.Serializable;

/**
 * @author postma *
 */
public class PluginProperties implements Serializable {

	/**
	 * Default serialVersionUUID
	 */
	private static final long serialVersionUID = 1L;

	private boolean selected;

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
		selected = false;
		pluginUUID = null;
		name = null;
		published = false;
		order = 0;
		position = 0;
		pluginType = null;
	}

	/**
	 * Constructor.
	 * 
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
		selected = false;
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
	public int getPosition()
	{
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

	/**
	 * @return the selected
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
}
