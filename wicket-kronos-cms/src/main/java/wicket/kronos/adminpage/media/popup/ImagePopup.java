package wicket.kronos.adminpage.media.popup;

import wicket.kronos.CMSImageResource;
import wicket.markup.html.WebPage;
import wicket.markup.html.image.Image;

public class ImagePopup extends WebPage{

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public ImagePopup(CMSImageResource image)
	{
		add(new Image("image", image));
	}
}
