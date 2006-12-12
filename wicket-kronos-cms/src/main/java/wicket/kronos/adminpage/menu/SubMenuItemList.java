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
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 *
 */
public class SubMenuItemList extends Panel {
	
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param wicketId 
	 * @param menuItemName 
	 */
	public SubMenuItemList(String wicketId, String menuItemName)
	{
		super(wicketId);
		List<SubMenuItem> subMenuItems = this.getSubMenuItems(menuItemName);
		ListView subMenuItemsList = new ListView("submenuRepeater", subMenuItems) {
			
			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item)
			{
				SubMenuItem subMenuItem = (SubMenuItem) item.getModelObject();

				PageParameters param = new PageParameters(subMenuItem.getID(), subMenuItem.getIDType());
				item.add(new BookmarkablePageLink("menuLink",
						AdminPage.class, param).add(new Label(
						"menuLabel", subMenuItem.getName())));
			}
		};
		add(subMenuItemsList);
	}
	
	/**
	 * @param menuItemName
	 * @return List<SubMenuItem>
	 */
	public List<SubMenuItem> getSubMenuItems(String menuItemName)
	{
		List<SubMenuItem> subMenuItems = new ArrayList<SubMenuItem>();
		SubMenuItem subMenuItem = null;
		
		Session jcrSession = ((KronosSession) KronosSession.get())
		.getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:cms/kronos:adminmenus/kronos:adminmenu/kronos:adminmenuitem [@kronos:name = '"
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
