package wicket.kronos.plugins.banner.panels;

import wicket.kronos.adminpage.AdminPanel;

/**
 * @author postma
 *
 */
public class BannerAdminpagePanel extends AdminPanel{
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param wicketId
	 */
	public BannerAdminpagePanel(String wicketId, String pluginUUID)
	{
		super(wicketId, pluginUUID);
	}
	
	class BannerAdminpageForm extends AdminForm
	{

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @param id
		 */
		public BannerAdminpageForm(String id)
		{
			super(id);
		}
		
	}
}
