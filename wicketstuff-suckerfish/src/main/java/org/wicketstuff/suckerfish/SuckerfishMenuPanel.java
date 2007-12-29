package org.wicketstuff.suckerfish;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Author: Julian Sinai http://javathoughts.capesugarbird.com
 * 
 * This Wicket component is released under the Apache 2 license.
 * 
 * Credit for the Suckerfish dropdown javascript code and CSS goes to Patrick
 * Griffiths and Dan Webb. The Javascript code and CSS is licensed as follows:
 * See http://www.alistapart.com/articles/dropdowns/. License is at
 * http://www.alistapart.com/copyright/, see "Free source"
 */
public class SuckerfishMenuPanel extends Panel
{
	private static final long serialVersionUID = -21832859336423477L;

	public static final String LINK_ID = "linkid";
	public static final String LINK_TEXT_ID = "linktext";
	private final List<MenuItem> topMenuItems = new ArrayList<MenuItem>();

	public SuckerfishMenuPanel(final String id)
	{
		super(id);
		// Add the Suckerfish CSS
		add(HeaderContributor.forCss(SuckerfishMenuPanel.class,
				"SuckerfishMenuPanel.css"));
		// Add the top menus
		add(new SubMenuListView("topmenuitems", new PropertyModel(this,
				"topMenuItems")));
	}

	/** Add one menu item */
	public void addMenu(final MenuItem menu)
	{
		topMenuItems.add(menu);
	}

	/** Add all menus at once */
	public void setMenuItems(final List<MenuItem> menuItems)
	{
		topMenuItems.clear();
		topMenuItems.addAll(menuItems);
	}

	/** Lightweight menu object that stores a menu and its label */
	public static class MenuItem implements Serializable
	{
		private static final long serialVersionUID = 8198442375175693685L;

		private final Link link;
		private final String label;
		private final List<MenuItem> subMenuItems = new ArrayList<MenuItem>();

		public MenuItem(final Link link, final String label)
		{
			if (link != null && !link.getId().equals(LINK_ID))
			{
				throw new IllegalArgumentException(
						"The id must be SuckerfishMenuPanel.LINK_ID");
			}
			this.link = link;
			this.link.add(new Label(LINK_TEXT_ID, label));
			this.label = label;
		}

		public MenuItem(final String label)
		{
			link = null;
			this.label = label;
		}

		/** Add one menu item */
		public void addMenu(final MenuItem menu)
		{
			subMenuItems.add(menu);
		}

		/** Add all menus at once */
		public void setMenuItems(final List<MenuItem> menuItems)
		{
			subMenuItems.clear();
			for (final MenuItem child : menuItems)
			{
				addMenu(child);
			}
		}

		public Link getLink()
		{
			return link;
		}

		public List<MenuItem> getChildren()
		{
			return subMenuItems;
		}

		public String getLabel()
		{
			return label;
		}
	}

	private final class MenuItemFragment extends Fragment
	{
		private static final long serialVersionUID = 0L;

		public MenuItemFragment(final MenuItem menuItem)
		{
			super("menuitemfragment", "MENUITEMFRAGMENT",
					SuckerfishMenuPanel.this);
			// Add the menu's label (hyperlinked if a link is provided)
			if (menuItem.getLink() != null)
			{
				add(new LinkFragment(menuItem.getLink(), menuItem.getLabel()));
			}
			else
			{
				add(new TextFragment(menuItem.getLabel()));
			}
			final WebMarkupContainer menuitemul = new WebMarkupContainer(
					"menuitemlist");
			add(menuitemul);
			// Hide the <ul> tag if there are no submenus
			menuitemul.setVisible(menuItem.getChildren().size() > 0);
			// Add the submenus
			menuitemul.add(new SubMenuListView("menuitemlinks", menuItem
					.getChildren()));
		}
	}

	private final class LinkFragment extends Fragment
	{
		private static final long serialVersionUID = 0L;

		public LinkFragment(final Link link, final String label)
		{
			super("linkfragment", "LINKFRAGMENT", SuckerfishMenuPanel.this);
			add(link);
		}
	}

	private final class TextFragment extends Fragment
	{
		private static final long serialVersionUID = 0L;

		public TextFragment(final String label)
		{
			super("linkfragment", "TEXTFRAGMENT", SuckerfishMenuPanel.this);
			add(new Label(LINK_TEXT_ID, label));
		}
	}

	private final class SubMenuListView extends ListView
	{
		private static final long serialVersionUID = -5875124377225299067L;

		private SubMenuListView(final String id, final IModel model)
		{
			super(id, model);
		}

		private SubMenuListView(final String id, final List<MenuItem> list)
		{
			super(id, list);
		}

		@Override
		protected void populateItem(final ListItem item)
		{
			final MenuItem menuItem = (MenuItem) item.getModelObject();
			item.add(new MenuItemFragment(menuItem));
		}
	}
}
