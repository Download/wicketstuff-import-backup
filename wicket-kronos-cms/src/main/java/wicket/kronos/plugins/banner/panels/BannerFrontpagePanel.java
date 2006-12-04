package wicket.kronos.plugins.banner.panels;

import wicket.markup.html.image.Image;
import wicket.markup.html.image.resource.DynamicImageResource;
import wicket.markup.html.panel.Panel;

/**
 * @author roeloffzen
 */
public class BannerFrontpagePanel extends Panel {

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param img
	 */
	public BannerFrontpagePanel(String id, DynamicImageResource img)
	{
		super(id);

		Image bannerImg = new Image("bannerImage", img);
		add(bannerImg);
	}

}
