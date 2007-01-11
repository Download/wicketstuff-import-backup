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

import wicket.PageParameters;
import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.frontpage.Frontpage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.ExternalLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 */
public class MenuItem extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String name = null;

	/**
	 * Constructor.
	 * 
	 * @param wicketId
	 * @param name
	 * @param ID
	 * @param IDType
	 */
	public MenuItem(String wicketId, String name, String ID, String IDType)
	{
		super(wicketId);
		this.name = name;

		PageParameters menulinkParam = new PageParameters();
		menulinkParam.add("IDType", IDType);
		menulinkParam.add("ID", ID);

		if (ID.equals("#"))
		{
			add(new ExternalLink("menulink", "#").add(new Label("menulinklabel", name)));
		} else if (IDType.equalsIgnoreCase("adminpage"))
		{
			add(new BookmarkablePageLink("menulink", AdminPage.class, menulinkParam).add(new Label(
					"menulinklabel", name)));
		} else if (IDType.equalsIgnoreCase("adminnewplugin"))
		{
			add(new BookmarkablePageLink("menulink", AdminPage.class, menulinkParam).add(new Label(
					"menulinklabel", name)));
		} else if (IDType.equalsIgnoreCase("frontpage"))
		{
			add(new BookmarkablePageLink("menulink", Frontpage.class, PageParameters.NULL)
					.add(new Label("menulinklabel", name)));
		} else
		{
			add(new BookmarkablePageLink("menulink", AdminPage.class, PageParameters.NULL)
					.add(new Label("menulinklabel", name)));
		}

		List<SubMenuItem> subMenuItems = this.getSubMenuItems(name);
		ListView subMenuItemsList = new ListView("submenurepeater", subMenuItems) {

			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				SubMenuItem subMenuItem = (SubMenuItem) item.getModelObject();
				
				if ((subMenuItem.getIDType().equals("frontpage")))
				{
					item.add(new BookmarkablePageLink("submenulink", Frontpage.class,
							PageParameters.NULL).add(new Label("submenulabel", subMenuItem
							.getName())));
				} else
				{
					PageParameters param = new PageParameters();
					param.add("IDType", subMenuItem.getIDType());
					param.add("ID", subMenuItem.getID());
					
					item.add(new BookmarkablePageLink("submenulink", AdminPage.class, param)
							.add(new Label("submenulabel", subMenuItem.getName())));
				}
			}
		};
		add(subMenuItemsList);
	}

	/**
	 * @return name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Retreive list with submenu menu items
	 * 
	 * @param menuItemName
	 * @return List<SubMenuItem>
	 */
	public List<SubMenuItem> getSubMenuItems(String menuItemName)
	{
		List<SubMenuItem> subMenuItems = new ArrayList<SubMenuItem>();
		SubMenuItem subMenuItem = null;

		Session jcrSession = ((KronosSession) KronosSession.get()).getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery(
					"//kronos:cms/kronos:adminmenus/kronos:adminmenu/kronos:adminmenuitem [@kronos:name = '"
							+ menuItemName + "']/kronos:adminsubmenuitem", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();

				String name = n.getProperty("kronos:name").getString();
				String ID = n.getProperty("kronos:ID").getString();
				String IDType = n.getProperty("kronos:IDType").getString();

				subMenuItem = new SubMenuItem(name, ID, IDType);

				subMenuItems.add(subMenuItem);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}

		return subMenuItems;
	}
}
