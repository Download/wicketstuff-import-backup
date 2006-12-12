package wicket.kronos.adminpage;

import wicket.PageParameters;
import wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import wicket.kronos.DataProcessor;
import wicket.kronos.KronosPage;
import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.menu.AdminMenu;
import wicket.kronos.frontpage.Frontpage;
import wicket.kronos.plugins.IPlugin;
import wicket.markup.html.link.BookmarkablePageLink;

/**
 * @author postma
 */
@AuthorizeInstantiation("ADMIN")
public class AdminPage extends KronosPage {

	private static final long serialVersionUID = 1L;

	/**
	 * Load Adminpage with default layout, without a plugin enabled for
	 * configuration.
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
		} else if(pageParameters.getString("IDType").equalsIgnoreCase("adminpage")) {
			add(new AdminPluginOverview("plugin"));
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
