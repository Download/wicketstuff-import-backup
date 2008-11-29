package org.wicketstuff.yui.markup.html.menu2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.collections.MiniMap;
import org.apache.wicket.velocity.VelocityHeaderContributor;
import org.apache.wicket.velocity.VelocityJavascriptContributor;

public class YuiMenu extends AbstractYuiMenu {
	private static final long serialVersionUID = 1L;
	private static final String MENU_ID = "menu";
	private static final String MENU_ITEMS_ID = "menuItems";

	private List<AbstractYuiMenuItem> items = new ArrayList<AbstractYuiMenuItem>();
	private ListView list;
	private WebMarkupContainer menu;
	private boolean firstOfType = false;

	public YuiMenu(String elementId) {
		this(elementId, true, true);
	}

	YuiMenu(String elementId, final boolean firstMenu, boolean addInit) {
		super(MENU_ID, elementId);

		menu = new WebMarkupContainer(MENU_ID);

		if (firstMenu) {
			addFirstOfType();
		}

		WebMarkupContainer menuContainer = getMenuContainer();
		setRenderBodyOnly(true);
		
		menuContainer = (menuContainer == null) ? this : menuContainer;

		menuContainer.add(menu);

		list = new ListView(MENU_ITEMS_ID, items) {

			@Override
			protected void populateItem(ListItem item) {
				item.setRenderBodyOnly(true);
				YuiMenuItem mi = (YuiMenuItem) item.getModelObject();
				mi.setIndex(item.getIndex());
				mi.setRenderBodyOnly(true);
				item.add(mi);
			}

		}.setReuseItems(true);
		menu.add(list);

		if (addInit) {
			add(getMenuInit());
		}
	}

	void addFirstOfType() {
		if (firstOfType == false) {
			menu.add(new AttributeAppender("class", true, new Model(
					"first-of-type"), " "));
			firstOfType = true;
		}
	}

	public AbstractYuiMenuItem addMenuItem(String label, Link link) {
		YuiMenuItem item = new YuiMenuItem(label, link);
		addMenuItem(item);
		return item;
	}

	public AbstractYuiMenuItem addMenuItem(IYuiMenuAction action) {
		YuiMenuItem item = new YuiMenuItem(action);
		addMenuItem(item);
		return item;
	}

	public void addMenuItem(AbstractYuiMenuItem menuItem) {
		items.add(menuItem);
		list.setList(items);
	}

	public AbstractYuiMenuItem getMenuItem(int idx) {
		ListItem item = (ListItem) list.getList().get(idx);
		return item == null ? null : (YuiMenuItem) item.getModelObject();
	}

	@Override
	protected String getMenuClass() {
		return "yuimenu";
	}

	@Override
	public String getMenuName() {
		return "yuiMenu" + getMenuId();
	}

	private IBehavior getMenuInit() {
		final Map<String, String> vars = new MiniMap(2);
		vars.put("menuName", getMenuName());
		vars.put("elementId", getMenuId());
		return new VelocityHeaderContributor()
				.add(new VelocityJavascriptContributor(YuiMenu.class,
						"res/menuinit.vm", Model.valueOf(vars), getMenuId()
								+ "Script"));
	}

}
