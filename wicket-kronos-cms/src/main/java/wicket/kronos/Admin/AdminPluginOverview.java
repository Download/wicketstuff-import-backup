package wicket.kronos.Admin;

import java.util.List;

import wicket.PageParameters;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.DataProcessor;
import wicket.kronos.plugins.PluginProperties;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;

public class AdminPluginOverview extends Panel{

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public AdminPluginOverview(String id)
	{
		super(id);
		List<PluginProperties> propertiesList = DataProcessor.getPluginPropertiesObjects();
		
		add(new ListView("pluginRepeater", propertiesList)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				PluginProperties properties = (PluginProperties)item.getModelObject();
				
				PageParameters param = new PageParameters();
				param.put("IDType", "plugin");
				param.put("ID", properties.getPluginUUID());
				item.add(new BookmarkablePageLink("nameLink",
						AdminPage.class, param).add(new Label(
						"nameLabel", properties.getName())));
								
				item.add(new Label("published", ""+properties.getPublished() ));
				item.add(new Label("order", ""+properties.getOrder()));
				item.add(new Label("position", ""+properties.getPosition()));
				item.add(new Label("pluginType", properties.getPluginType()));
			}
		});
	}

}
