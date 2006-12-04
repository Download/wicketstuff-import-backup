package wicket.kronos.plugins.menu;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import wicket.kronos.plugins.IPlugin;
import wicket.kronos.KronosSession;
import wicket.kronos.plugins.menu.panels.MenuFrontpagePanel;

/**
 * This plugin is used to create menubars. It collects all the data from the
 * repository and creates a list of links
 * 
 * @author ted
 */
public class MenuPlugin extends IPlugin {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private boolean isHorizontal;

	/**
	 * Supports only the constructor without a specific contentUUID
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public MenuPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);

		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Node pluginNode = jcrSession.getNodeByUUID(pluginUUID);

			isHorizontal = pluginNode.getProperty("kronos:isHorizontal")
					.getBoolean();
		}
		catch (ItemNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}

		add(new MenuFrontpagePanel("panel", this.getMenuItems(), isHorizontal));
	}

	@Override
	public boolean isConfigurable()
	{
		return true;
	}

	/**
	 * retrieves a list of menuitem from the repository
	 * 
	 * @return List<MenuItem>
	 */
	public List<MenuItem> getMenuItems()
	{
		List<MenuItem> menuItems = new ArrayList<MenuItem>();

		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:menu[@kronos:menuname = '"
					+ pluginName + "']/kronos:menuitem", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();

				String menuItemName = n.getProperty("kronos:menuitemname")
						.getString();
				String linkType = n.getProperty("kronos:linkType").getString();
				MenuItem item = new MenuItem();
				item.setName(menuItemName);
				item.setLinkType(linkType);
				if (linkType.equalsIgnoreCase("intern"))
				{
					boolean isAdmin = n.getProperty("kronos:isAdmin")
							.getBoolean();
					String IDType = n.getProperty("kronos:IDType").getString();
					if (!IDType.equalsIgnoreCase("frontpage") && !IDType.equalsIgnoreCase("adminpage"))
					{
						String ID = n.getProperty("kronos:ID").getString();
						item.setID(ID);
					}
					item.setIDType(IDType);
					item.setIsAdmin(isAdmin);
				} else if (linkType.equalsIgnoreCase("extern"))
				{
					String link = n.getProperty("kronos:link").getString();
					item.setLink(link);
				}

				menuItems.add(item);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}

		return menuItems;
	}
}
