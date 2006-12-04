package wicket.kronos.plugins.titan.popup;

import wicket.PageParameters;
import wicket.markup.html.WebPage;
import wicket.markup.html.link.PopupCloseLink;

/**
 * Simple popupWindow Only has some static text and a link to close the popup
 * 
 * @author roeloffzen
 */
public class TitanPopup extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param param
	 */
	public TitanPopup(PageParameters param)
	{
		add(new PopupCloseLink("close"));
	}
}
