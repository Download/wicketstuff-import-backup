package wicket.kronos.plugins.menu.panels;

import java.io.Serializable;
import java.util.Iterator;
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

import wicket.PageParameters;
import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.adminpage.AdminPanel;
import wicket.kronos.plugins.menu.MenuItem;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;
import wicket.model.PropertyModel;

/**
 * @author ted
 */
public class MenuAdminpagePanel extends AdminPanel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param wicketId 
	 * @param menuItems 
	 * @param pluginUUID 
	 * @param isHorizontal 
	 * @param id
	 */
	public MenuAdminpagePanel(String wicketId, List<MenuItem> menuItems, String pluginUUID, boolean isHorizontal)
	{
		super(wicketId, pluginUUID);
		add(new MenuAdminForm("menuAdminForm", menuItems, new CompoundPropertyModel(new MenuModel(menuItems, isHorizontal))));
	}
	
	/**
	 * 
	 * @author roeloffzen
	 *
	 */
	public class MenuAdminForm extends Form {

		
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		
		private ListView delMenuItems = null;

		/**
		 * @param wicketId
		 * @param menuItems 
		 * @param model
		 */
		public MenuAdminForm(String wicketId, List<MenuItem> menuItems, IModel model)
		{
			super(wicketId, model);
			
			add(delMenuItems = new ListView("menuNameRepeater", menuItems) {
				@Override
				public void populateItem(ListItem item)
				{
					MenuItem menuItem = (MenuItem) item.getModelObject();
					item.add(new CheckBox("remove", new PropertyModel(menuItem, "remove")));
					item.add(new Label("menuItemLabel", menuItem.getName()));
				}
			});
			
			add(new CheckBox("isHorizontal"));
			
			add(new Button("save")
			{
				@Override
				public void onSubmit()
				{
					Session jcrSession = KronosSession.get().getJCRSession();
					
					MenuModel menuModel = (MenuModel)MenuAdminForm.this.getModelObject();
					
					try
					{
						Node menuNode = jcrSession.getNodeByUUID(properties.getPluginUUID());
						menuNode.setProperty("kronos:isHorizontal", menuModel.isHorizontal());
						jcrSession.save();
					}
					catch (ItemNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (RepositoryException e)
					{
						e.printStackTrace();
					}
					setResponsePage(AdminPage.class);
				}
			});
			
			add(new Button("delete")
			{
				@Override
				public void onSubmit()
				{
					Session jcrSession = KronosSession.get().getJCRSession();
					
					Iterator iter = delMenuItems.getList().iterator();
					while (iter.hasNext()) {
						MenuItem menuItem = (MenuItem)iter.next();
						System.out.println("Menuitem \"" + menuItem.getName() + 
								"\" remove: " + menuItem.isRemove());
						if (menuItem.isRemove())
						{
							Workspace ws = jcrSession.getWorkspace();
							QueryManager qm;
							try
							{								
								qm = ws.getQueryManager();
								Query q = qm.createQuery("//kronos:cms/kronos:menus/kronos:menu/kronos:menuitem[@kronos:menuitemname = '" + menuItem.getName() + "']", Query.XPATH);
								
								QueryResult result = q.execute();
								NodeIterator it = result.getNodes();
								if (it.hasNext())
								{
									it.nextNode().remove();									
								}
							}
							catch (RepositoryException e)
							{
								e.printStackTrace();
							}
							
						}
					}
					try {
						jcrSession.save();
						
						PageParameters param = new PageParameters();
						param.add("IDType", "plugin");
						param.add("ID", properties.getPluginUUID());
						setResponsePage(AdminPage.class, param);
					}
					catch (ItemNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (RepositoryException e)
					{
						e.printStackTrace();
					}
				}
			});
						
			add(new Button("internal")
			{
				@Override
				public void onSubmit()
				{
					PageParameters param = new PageParameters();
					param.add("IDType", "plugin");
					param.add("ID", properties.getPluginUUID());
					param.add("action", "newInternal");
					setResponsePage(AdminPage.class, param);
				}
			});
			
			add(new Button("external")
			{
				@Override
				public void onSubmit()
				{
					PageParameters param = new PageParameters();
					param.add("IDType", "plugin");
					param.add("ID", properties.getPluginUUID());
					param.add("action", "newExternal");
					setResponsePage(AdminPage.class, param);
				}
			});
		}
	}
	
	/**
	 * 
	 * @author roeloffzen
	 *
	 */
	public class MenuModel implements Serializable{
		
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private List<MenuItem> menuItems;
		private boolean isHorizontal;
		
		/**
		 * @param menuItems 
		 * @param isHorizontal
		 */
		public MenuModel(List<MenuItem> menuItems, boolean isHorizontal)
		{
			this.menuItems = menuItems;
			this.isHorizontal = isHorizontal;
		}
	
		/**
		 * @return the menuItems
		 */
		public List<MenuItem> getMenuItems()
		{
			return menuItems;
		}
		
		/**
		 * @param menuItems the menuItems to set
		 */
		public void setMenuItems(List<MenuItem> menuItems)
		{
			this.menuItems = menuItems;
		}
		
		/**
		 * @return boolean
		 */
		public boolean isHorizontal()
		{
			return isHorizontal;
		}

		/**
		 * @param isHorizontal
		 */
		public void setHorizontal(boolean isHorizontal)
		{
			this.isHorizontal = isHorizontal;
		}
	}
}
