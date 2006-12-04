package wicket.kronos.plugins;

public class PluginProperties {
	private String pluginUUID;

	private String name;

	private boolean published;

	private int order;

	private int position;

	private String pluginType;

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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getOrder()
	{
		return order;
	}

	public void setOrder(int order)
	{
		this.order = order;
	}

	public String getPluginType()
	{
		return pluginType;
	}

	public void setPluginType(String pluginType)
	{
		this.pluginType = pluginType;
	}

	public String getPluginUUID()
	{
		return pluginUUID;
	}

	public void setPluginUUID(String pluginUUID)
	{
		this.pluginUUID = pluginUUID;
	}

	public int getPosition(){
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

	public boolean getPublished()
	{
		return published;
	}

	public void setPublished(boolean published)
	{
		this.published = published;
	}
}
