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
import wicket.markup.html.image.resource.DynamicImageResource;
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
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private DropDownChoice dropDown;
		
		public BannerAdminForm(String wicketId, IModel model)
		{
			super(wicketId, model);
			
			add(dropDown = new DropDownChoice("imageUUID", uuidList, new IChoiceRenderer() {
				
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
		
		public void onSubmit()
		{
			imageUUID = dropDown.getModelObjectAsString();
			
			Session jcrSession = KronosSession.get().getJCRSession();
			DynamicImageResource resource = null;

			try
			{
				Workspace ws = jcrSession.getWorkspace();
				QueryManager qm = ws.getQueryManager();
				Query q = qm.createQuery(
						"//kronos:content/kronos:plugin/kronos:banner[@kronos:pluginname='"
								+ properties.getName() + "']", Query.XPATH);

				QueryResult result = q.execute();
				NodeIterator it = result.getNodes();

				if (it.hasNext())
				{
					Node bannerNode = it.nextNode();
					Node imageNode = jcrSession.getNodeByUUID(imageUUID);
					
					bannerNode.setProperty("kronos:image", imageNode);
				}
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
		
		public BannerImageModel(String _imageUUID)
		{
			imageUUID = _imageUUID;
		}

		public String getImageUUID()
		{
			return imageUUID;
		}

		public void setImageUUID(String imageUUID)
		{
			this.imageUUID = imageUUID;
		}	
	}
}
