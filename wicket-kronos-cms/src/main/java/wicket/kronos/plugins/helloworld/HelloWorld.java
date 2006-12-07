package wicket.kronos.plugins.helloworld;

import wicket.kronos.plugins.IPlugin;
import wicket.kronos.plugins.helloworld.panels.HelloWorldAdminpagePanel;
import wicket.markup.html.basic.Label;

/**
 * Simple hello world plugin
 * 
 * @author roeloffzen
 */
public class HelloWorld extends IPlugin {

	/**
	 * Default plugin constructor. Creates a Label with a Hello World text
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public HelloWorld(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
		if(isAdmin)
		{
			add(new HelloWorldAdminpagePanel("helloworld", pluginUUID));
		} else {
			add(new Label("helloworld",
					"Hello beautiful world how is theigh today?"));
		}
	}

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isConfigurable()
	{
		return true;
	}

	@Override
	public void save()
	{
		super.save();
	}

}
