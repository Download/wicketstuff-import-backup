package wicket.kronos.plugins.version;

import wicket.kronos.plugins.IPlugin;
import wicket.kronos.plugins.version.panels.VersionAdminPanel;
import wicket.kronos.plugins.version.panels.VersionFrontpagePanel;

/**
 * @author postma
 */
public class VersionPlugin extends IPlugin {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This plugin only supports a constructor without specific contentUUID
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public VersionPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition, String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order, areaposition, pluginType);
		if (isAdmin)
		{
			add(new VersionAdminPanel("version", pluginUUID));
		} else
		{
			add(new VersionFrontpagePanel("version"));
		}
	}

	@Override
	public boolean isConfigurable()
	{
		return false;
	}

}
