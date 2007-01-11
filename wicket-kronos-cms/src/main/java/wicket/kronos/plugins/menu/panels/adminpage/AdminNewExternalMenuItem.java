package wicket.kronos.plugins.menu.panels.adminpage;

import java.io.Serializable;

import wicket.kronos.DataProcessor;
import wicket.kronos.adminpage.AdminPage;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.PropertyModel;

/**
 * Creation of an new external menu item 
 * 
 * @author postma
 *
 */
public class AdminNewExternalMenuItem extends Panel{
	
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param wicketId
	 * @param menuName 
	 */
	public AdminNewExternalMenuItem(String wicketId, String menuName)
	{
		super(wicketId);
		add(new ExternalMenuItemForm("externalMenuitemForm", menuName));
	}
	
	/**
	 * @author postma
	 *
	 */
	public class ExternalMenuItemForm extends Form{
		
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor. 
		 * @param wicketId
		 * @param menuName 
		 */
		public ExternalMenuItemForm(String wicketId, final String menuName)
		{
			super(wicketId);
			final NewExternalMenuItemModel nemim = new NewExternalMenuItemModel();
			add(new TextField("order", new PropertyModel(nemim, "order")));
			add(new TextField("linkname", new PropertyModel(nemim, "name")));
			add(new TextField("externallink", new PropertyModel(nemim, "link")));
			
			//TODO check to make sure supplied link is valid
			
			add(new Button("saveMenuItem")
			{
				@Override
				public void onSubmit()
				{				
					String linkname = nemim.getName();
					String externallink = nemim.getLink();
					int order = nemim.getOrder();
					DataProcessor.saveNewExternalMenuItem(menuName, linkname, order, externallink);
					setResponsePage(AdminPage.class);
				}
			});
		}
	}
	
	/**
	 * Model for a new external menu item
	 * 
	 * @author postma
	 *
	 */
	public class NewExternalMenuItemModel implements Serializable{
		
		/**
		 * Default serialVErsionUID
		 */
		private static final long serialVersionUID = 1L;
		
		String name;
		String link;
		int order;
		
		/**
		 * Constructor.
		 */
		public NewExternalMenuItemModel()
		{
			name = null;
			link = null;
			order = 1;
		}

		/**
		 * @return the link
		 */
		public String getLink()
		{
			return link;
		}

		/**
		 * @param link the link to set
		 */
		public void setLink(String link)
		{
			this.link = link;
		}

		/**
		 * @return the name
		 */
		public String getName()
		{
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name)
		{
			this.name = name;
		}

		public int getOrder()
		{
			return order;
		}

		public void setOrder(int order)
		{
			this.order = order;
		}
	}
}
