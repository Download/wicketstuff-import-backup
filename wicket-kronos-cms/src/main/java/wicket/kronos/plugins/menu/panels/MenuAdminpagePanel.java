package wicket.kronos.plugins.menu.panels;

import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.Panel;

/**
 * @author ted
 */
public class MenuAdminpagePanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 */
	public MenuAdminpagePanel(String id)
	{
		super(id);
		add(new Label("test", "Dit is een test voor de frontpage"));
	}
}
