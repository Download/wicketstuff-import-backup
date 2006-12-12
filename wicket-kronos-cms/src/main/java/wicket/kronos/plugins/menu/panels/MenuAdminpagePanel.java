package wicket.kronos.plugins.menu.panels;

import java.io.Serializable;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPanel;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;

/**
 * @author ted
 */
public class MenuAdminpagePanel extends AdminPanel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public MenuAdminpagePanel(String wicketId, String pluginUUID, boolean isHorizontal)
	{
		super(wicketId, pluginUUID);
		add(new MenuAdminForm("menuAdminForm", new CompoundPropertyModel(new MenuModel(isHorizontal))));
	}
	
	/**
	 * 
	 * @author roeloffzen
	 *
	 */
	public class MenuAdminForm extends Form {

		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private CheckBox isHor;

		public MenuAdminForm(String wicketId, IModel model)
		{
			super(wicketId, model);
			add(isHor = new CheckBox("isHorizontal"));
		}
		
		public void onSubmit()
		{
			Session jcrSession = KronosSession.get().getJCRSession();
			
			MenuModel menuModel = (MenuModel)this.getModelObject();
			
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
			
		}
	}
	
	/**
	 * 
	 * @author roeloffzen
	 *
	 */
	public class MenuModel implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean isHorizontal;
		
		public	MenuModel(boolean isHorizontal)
		{
			this.isHorizontal = isHorizontal;
		}

		public boolean isHorizontal()
		{
			return isHorizontal;
		}

		public void setHorizontal(boolean isHorizontal)
		{
			this.isHorizontal = isHorizontal;
		}
	}
}
