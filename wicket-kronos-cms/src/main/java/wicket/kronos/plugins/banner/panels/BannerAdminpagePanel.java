package wicket.kronos.plugins.banner.panels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPanel;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.model.CompoundPropertyModel;
import wicket.model.IModel;

/**
 * @author postma
 *
 */
public class BannerAdminpagePanel extends AdminPanel{
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String imageUUID;
	private BannerImageModel model;
	private List uuidList;
	private Map nameMap;
	
	/**
	 * @param wicketId
	 * @param pluginUUID 
	 * @param imageUUID 
	 */
	public BannerAdminpagePanel(String wicketId, String pluginUUID, String imageUUID)
	{
		super(wicketId, pluginUUID);
		this.imageUUID = imageUUID;
		this.fillImageCollections();
		model = new BannerImageModel(imageUUID);
		add(new BannerAdminForm("bannerAdminForm", new CompoundPropertyModel(model)));
	}
	
	private void fillImageCollections()
	{
		uuidList = new ArrayList();
		nameMap = new HashMap();
		
		Session jcrSession = KronosSession.get().getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:content/kronos:images/*", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();
				String name = n.getName();
				String uuid = n.getUUID();
				
				uuidList.add(uuid);
				nameMap.put(uuid, name);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author roeloffzen
	 *
	 */
	public class BannerAdminForm extends Form{

		/**
		 * Form for choosing the banner image to be used
		 */
		private static final long serialVersionUID = 1L;
		private DropDownChoice dropDown;
		
		/**
		 * @param wicketId
		 * @param model
		 */
		public BannerAdminForm(String wicketId, IModel model)
		{
			super(wicketId, model);
			
			add(dropDown = new DropDownChoice("imageUUID", uuidList, new IChoiceRenderer() {
				
				/**
				 * Default serialVersionUUID;
				 */
				private static final long serialVersionUID = 1L;

				public String getIdValue(Object object, int arg1)
				{	
					return (String)object;
				}
			
				public Object getDisplayValue(Object object)
				{
					return (String)nameMap.get(object);
				}
				
			}));
		}
		
		@Override
		public void onSubmit()
		{
			imageUUID = dropDown.getModelObjectAsString();
			
			Session jcrSession = KronosSession.get().getJCRSession();

			try
			{
				Node bannerNode = jcrSession.getNodeByUUID(pluginUUID);
				Node imageNode = jcrSession.getNodeByUUID(imageUUID);
				
				bannerNode.setProperty("kronos:bannerimage", imageNode);
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
	public class BannerImageModel implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String imageUUID;
		
		/**
		 * @param _imageUUID
		 */
		public BannerImageModel(String _imageUUID)
		{
			imageUUID = _imageUUID;
		}

		/**
		 * @return String imageUUID
		 */
		public String getImageUUID()
		{
			return imageUUID;
		}

		/**
		 * @param imageUUID
		 */
		public void setImageUUID(String imageUUID)
		{
			this.imageUUID = imageUUID;
		}	
	}
}
