package wicket.kronos.plugins.titan;

import wicket.PageMap;
import wicket.kronos.plugins.IPlugin;
import wicket.kronos.plugins.titan.popup.TitanPopup;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.link.PopupSettings;

/**
 * @author postma
 */
public class TitanPlugin extends IPlugin {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This class only supports a constructor without specific contentUUID. Only
	 * adds a link that will trigger a popup
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public TitanPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
		PopupSettings popupSettings = new PopupSettings(PageMap
				.forName("popuppagemap")).setHeight(500).setWidth(500);
		add(new BookmarkablePageLink("popupLink", TitanPopup.class)
				.setPopupSettings(popupSettings));
	}

	@Override
	public boolean isConfigurable()
	{
		return false;
	}

}
