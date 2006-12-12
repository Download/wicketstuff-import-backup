package wicket.kronos.plugins.menu.panels;

import java.util.List;

import wicket.AttributeModifier;
import wicket.Component;
import wicket.PageParameters;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.frontpage.Frontpage;
import wicket.kronos.plugins.menu.MenuItem;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.ExternalLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * This class is used a Panel that contains all the menuItems.
 * 
 * @author roeloffzen
 */
public class MenuFrontpagePanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates links that are put on the panel. For external typed links an
	 * ExternalLink is made For internal typed links an BookmarkablePageLink is
	 * made
	 * 
	 * @param id
	 * @param menuItems
	 * @param isHorizontal
	 */
	public MenuFrontpagePanel(String id, List<MenuItem> menuItems,
			final boolean isHorizontal)
	{
		super(id);

		ListView list = new ListView("menuItems", menuItems) {
			@Override
			public void populateItem(ListItem item)
			{
				MenuItem menuItem = (MenuItem) item.getModelObject();
				if (menuItem.getLinkType().equalsIgnoreCase("extern"))
				{
					item.add(new ExternalLink("menuItem", menuItem
									.getLink()).add(new Label("menuItemLabel",
									menuItem.getName())));
				} else
				{
					if (menuItem.getIDType().equalsIgnoreCase("frontpage"))
					{
						item.add(new BookmarkablePageLink("menuItem",
								Frontpage.class, PageParameters.NULL)
								.add(new Label("menuItemLabel", menuItem
										.getName())));
					} else 
					{
						if (menuItem.getIDType().equalsIgnoreCase("adminpage"))
						{
							item.add(new BookmarkablePageLink("menuItem",
									AdminPage.class, PageParameters.NULL)
									.add(new Label("menuItemLabel", menuItem
											.getName())));
						} else
						{
							PageParameters param = new PageParameters();
							param.add("IDType", menuItem.getIDType());
							param.add("ID", menuItem.getID());
							item.add(new BookmarkablePageLink("menuItem",
									Frontpage.class, param).add(new Label(
									"menuItemLabel", menuItem.getName())));
						}
					}
				}
				item.add(new AttributeModifier("class", true, new Model() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Object getObject(final Component component)
					{
						String cssClass;
						if (isHorizontal)
						{
							cssClass = "menuItemsHorizontal";
						} else
						{
							cssClass = "menuItemsVertical";
						}
						return cssClass;
					}
				}));
			}
		};

		add(list);
	}
}
