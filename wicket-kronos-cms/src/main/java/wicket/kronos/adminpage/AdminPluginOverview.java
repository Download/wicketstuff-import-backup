package wicket.kronos.adminpage;

import java.util.List;

import wicket.PageParameters;
import wicket.kronos.AreaLocations;
import wicket.kronos.DataProcessor;
import wicket.kronos.plugins.PluginProperties;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * @author postma
 *
 */
public class AdminPluginOverview extends Panel{

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param wicketId
	 */
	public AdminPluginOverview(String wicketId)
	{
		super(wicketId);
		List<PluginProperties> propertiesList = DataProcessor.getPluginPropertiesObjects();
		
		add(new ListView("pluginRepeater", propertiesList)
		{
			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

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
				
				item.add(new CheckBox("published", new Model(properties.getPublished())));
				item.add(new TextField("order", new Model(properties.getOrder())));
				String areaLocation = AreaLocations.getLocationname(properties.getPosition());
				item.add(new TextField("position", new Model(areaLocation)));
				int lastPeriod = properties.getPluginType().lastIndexOf(".");
				String pluginType = properties.getPluginType().substring(lastPeriod + 1);
				item.add(new Label("pluginType", pluginType));
			}
		});
	}

}
