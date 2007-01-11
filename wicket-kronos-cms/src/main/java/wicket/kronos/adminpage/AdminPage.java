package wicket.kronos.adminpage;

import wicket.PageParameters;
import wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import wicket.kronos.DataProcessor;
import wicket.kronos.KronosPage;
import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.config.ConfigPanel;
import wicket.kronos.adminpage.media.MediaManagerPanel;
import wicket.kronos.adminpage.menu.AdminMenu;
import wicket.kronos.plugins.IPlugin;

/**
 * @author postma
 */
@AuthorizeInstantiation("ADMIN")
public class AdminPage extends KronosPage {

	private static final long serialVersionUID = 1L;

	/**
	 * Load Adminpage with default layout, without a plugin enabled for configuration.
	 */
	public AdminPage()
	{
		add(new AdminPluginOverview("plugin"));
		add(new AdminMenu("menu"));
	}

	/**
	 * Load Adminpage with a specific plugin to configure
	 * 
	 * @param pageParameters
	 */
	public AdminPage(PageParameters pageParameters)
	{
		add(new AdminMenu("menu"));
		if (pageParameters == null || pageParameters.isEmpty())
		{
			add(new AdminPluginOverview("plugin"));
		} else if (pageParameters.getString("IDType").equalsIgnoreCase("adminpage"))
		{
			add(new AdminPluginOverview("plugin"));
		} else if (pageParameters.getString("IDType").equalsIgnoreCase("adminnewplugin"))
		{
			add(new AdminNewPlugin("plugin"));
		} else if (pageParameters.getString("IDType").equalsIgnoreCase("adminpluginupload"))
		{
			add(new AdminPluginUpload("plugin"));
		} else if (pageParameters.getString("IDType").equalsIgnoreCase("mediamanager"))
		{
			add(new MediaManagerPanel("plugin"));
		} else if (pageParameters.getString("IDType").equalsIgnoreCase("config"))
		{
			add(new ConfigPanel("plugin"));
		} else
		{
			KronosSession currentsession = KronosSession.get();
			currentsession.setPageParameters(pageParameters);
			String IDType = pageParameters.getString("IDType");
			String ID = pageParameters.getString("ID");
			if (IDType.equalsIgnoreCase("plugin"))
			{
				IPlugin plugin = DataProcessor.getPlugin(true, ID);
				add(plugin);
			} else
			{
				IPlugin plugin = DataProcessor.getPluginByContent(true, ID);
				add(plugin);
			}
		}
	}
}
