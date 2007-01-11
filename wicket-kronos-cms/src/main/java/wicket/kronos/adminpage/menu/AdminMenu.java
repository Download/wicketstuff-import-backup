package wicket.kronos.adminpage.menu;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import wicket.AttributeModifier;
import wicket.Component;
import wicket.kronos.KronosSession;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * @author postma
 */
public class AdminMenu extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param wicketId
	 */
	public AdminMenu(String wicketId)
	{
		super(wicketId);

		List<MenuItem> menuItems = this.getMenuItems();
		ListView menuItemsList = new ListView("menuRepeater", menuItems) {

			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				MenuItem menuItem = (MenuItem) item.getModelObject();
				item.add(menuItem);

				/* Scan for Help menu item and place it to the right of the menu bar */
				if (menuItem.getName().equalsIgnoreCase("Help"))
				{
					item.add(new AttributeModifier("id", true, new Model() {
						@Override
						public Object getObject(final Component component)
						{
							String cssClass = "menu_right";
							return cssClass;
						}
					}));
				}
			}
		};
		add(menuItemsList);
	}

	/**
	 * Retreive menu items from repository
	 * 
	 * @return List<MenuItem>
	 */
	public List<MenuItem> getMenuItems()
	{
		List<MenuItem> menuItems = new ArrayList<MenuItem>();
		MenuItem menuItem = null;
		Session jcrSession = ((KronosSession) KronosSession.get()).getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery(
					"//kronos:cms/kronos:adminmenus/kronos:adminmenu/kronos:adminmenuitem",
					Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();

				String name = n.getProperty("kronos:name").getString();
				String ID = n.getProperty("kronos:ID").getString();
				if (!ID.equalsIgnoreCase("#"))
				{
					String IDType = n.getProperty("kronos:IDType").getString();
					menuItem = new MenuItem("menuitem", name, ID, IDType);
				} else
				{
					menuItem = new MenuItem("menuitem", name, ID, null);
				}
				menuItems.add(menuItem);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		return menuItems;
	}
}
