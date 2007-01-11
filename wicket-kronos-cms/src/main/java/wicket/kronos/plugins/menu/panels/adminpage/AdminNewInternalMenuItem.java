package wicket.kronos.plugins.menu.panels.adminpage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.Component;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import wicket.kronos.DataProcessor;
import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.AbstractReadOnlyModel;
import wicket.model.IModel;
import wicket.model.PropertyModel;

/**
 * @author postma
 *
 */
public class AdminNewInternalMenuItem extends Panel{

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param wicketId
	 * @param menuName 
	 */
	public AdminNewInternalMenuItem(String wicketId, String menuName)
	{
		super(wicketId);
		add(new InternalMenuItemForm("newMenuItem", menuName));
	}
	
	/**
	 * @author postma
	 *
	 */
	public class InternalMenuItemForm extends Form{
		
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * @param wicketId
		 * @param menuName 
		 */
		public InternalMenuItemForm(String wicketId, final String menuName)
		{
			super(wicketId);
			
			final NewInternalMenuItemModel nmim = new NewInternalMenuItemModel();
			final DropDownChoice idtypeChoice;
			final DropDownChoice idChoice;
			List<String> idtypeList = new ArrayList<String>();
				idtypeList.add("plugin");
				idtypeList.add("content");
				idtypeList.add("adminpage");
				idtypeList.add("frontpage");
			final List<String> idListPlugin = DataProcessor.getPluginIdentities();
			final List<String> idListContent = DataProcessor.getContentIdentities();
		
			final IModel idChoices = new AbstractReadOnlyModel()
			{
				private List<String> idList = new ArrayList<String>();
				@Override
				public Object getObject(Component component)
				{	
					if(nmim.getIdtype().equalsIgnoreCase("plugin"))
					{
						idList = idListPlugin;
					} else if(nmim.getIdtype().equalsIgnoreCase("content"))
					{
						idList = idListContent;
					}
					return idList;
				}
			};
			
			add(new TextField("order", new PropertyModel(nmim, "order")));
			add(new TextField("linkname", new PropertyModel(nmim, "linkname")));
			add(idtypeChoice = new DropDownChoice("idtype", new PropertyModel(nmim, "idtype"), idtypeList));
			add(idChoice = new DropDownChoice("id", new PropertyModel(nmim, "id"), idChoices, new IChoiceRenderer()
			{
				public String getIdValue(Object object, int arg1)
				{	
					return (String)object;
				}
			
				public Object getDisplayValue(Object object)
				{
					String name = null;
					Session jcrSession = KronosSession.get().getJCRSession();
					Node node;
					try
					{
						node = jcrSession.getNodeByUUID((String)object);
						name = node.getProperty("kronos:name").getString();
					}
					catch (ItemNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (RepositoryException e)
					{
						e.printStackTrace();
					}
					return name;
				}
			}));
			
			idtypeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
			{
				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					target.addComponent(idChoice);
				}
			});
			
			idChoice.setOutputMarkupId(true);
			
			add(new Button("saveMenuItem")
			{
				@Override
				public void onSubmit()
				{
					String name = nmim.getLinkname();
					String IDType = nmim.getIdtype();
					String ID = nmim.getId();
					int order = nmim.getOrder();
					DataProcessor.saveNewInternalMenuItem(menuName, name, order, IDType, ID);

					setResponsePage(AdminPage.class);
				}
			});
		}
	}
		
	/**
	 * Model for new internal menu item
	 * 
	 * @author postma
	 *
	 */
	public class NewInternalMenuItemModel implements Serializable{
		
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		private String linkname;
		private String idtype;
		private String id;
		private String link;
		private int order;
		
		/**
		 * Constructor.
		 */
		public NewInternalMenuItemModel()
		{
			linkname = null;
			idtype = "";
			id = null;
			link = null;
			order = 1;
		}

		/**
		 * @return the id
		 */
		public String getId()
		{
			return id;
		}


		/**
		 * @param id
		 */
		public void setId(String id)
		{
			this.id = id;
		}


		/**
		 * @return the idtypes
		 */
		public String getIdtype()
		{
			return idtype;
		}


		/**
		 * @param idtype
		 */
		public void setIdtype(String idtype)
		{
			this.idtype = idtype;
		}


		/**
		 * @return the linkname
		 */
		public String getLinkname()
		{
			return linkname;
		}


		/**
		 * @param linkname
		 */
		public void setLinkname(String linkname)
		{
			this.linkname = linkname;
		}

		/**
		 * @return the link
		 */
		public String getLink()
		{
			return link;
		}

		/**
		 * @param link
		 */
		public void setLink(String link)
		{
			this.link = link;
		}

		/**
		 * @return order
		 */
		public int getOrder()
		{
			return order;
		}

		/**
		 * @param order
		 */
		public void setOrder(int order)
		{
			this.order = order;
		}
	}
}
