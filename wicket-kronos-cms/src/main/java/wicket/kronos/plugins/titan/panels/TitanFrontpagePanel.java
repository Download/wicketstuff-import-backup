package wicket.kronos.plugins.titan.panels;

import wicket.PageMap;
import wicket.kronos.plugins.titan.popup.TitanPopup;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.PopupSettings;
import wicket.markup.html.panel.Panel;

public class TitanFrontpagePanel extends Panel{

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public TitanFrontpagePanel(String wicketId)
	{
		super(wicketId);
		PopupSettings popupSettings = new PopupSettings(PageMap
				.forName("popuppagemap")).setHeight(500).setWidth(500);
		add(new BookmarkablePageLink("popupLink", TitanPopup.class)
				.setPopupSettings(popupSettings));
	}

}
