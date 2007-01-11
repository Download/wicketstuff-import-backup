package wicket.kronos.adminpage.media.popup;

import wicket.kronos.CMSImageResource;
import wicket.markup.html.WebPage;
import wicket.markup.html.image.Image;

/**
 * Displays a popup with the complete image when clicking on thumbnail
 */
public class ImagePopup extends WebPage {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param image
	 */
	public ImagePopup(CMSImageResource image)
	{
		add(new Image("image", image));
	}
}
