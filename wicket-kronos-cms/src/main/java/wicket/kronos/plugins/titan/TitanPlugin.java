package wicket.kronos.plugins.titan;

import wicket.kronos.plugins.IPlugin;
import wicket.kronos.plugins.titan.panels.TitanAdminPanel;
import wicket.kronos.plugins.titan.panels.TitanFrontpagePanel;

/**
 * @author postma
 */
public class TitanPlugin extends IPlugin {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This class only supports a constructor without specific contentUUID. Only
	 * adds a link that will trigger a popup
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public TitanPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
		if(isAdmin)
		{
			add(new TitanAdminPanel("titan", pluginUUID));
		} else {
			add(new TitanFrontpagePanel("titan"));
		}
	}

	@Override
	public boolean isConfigurable()
	{
		return false;
	}

}
