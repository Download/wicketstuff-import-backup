package wicket.kronos;

import java.util.ArrayList;
import java.util.List;

import wicket.markup.html.panel.Panel;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.kronos.plugins.IPlugin;

/**
 * @author postma
 */
public class Area extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private int areaId = 0;

	private List<IPlugin> plugins = null;

	/**
	 * @param areaId
	 */
	public Area(int areaId)
	{
		super(AreaLocations.getLocationname(areaId));
		this.areaId = areaId;
		this.plugins = DataProcessor.getPlugins(this.areaId);
		add(new ListView("pluginRepeater", plugins) {
			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(ListItem item)
			{
				IPlugin plugin = (IPlugin) item.getModelObject();
				item.add(plugin);
			}
		});
	}

	/**
	 * @param areaId
	 * @param plugin
	 */
	public Area(int areaId, IPlugin plugin)
	{
		super(AreaLocations.getLocationname(areaId));
		this.areaId = areaId;

		this.plugins = new ArrayList<IPlugin>();
		plugins.add(plugin);
		add(new ListView("pluginRepeater", plugins) {
			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(ListItem item)
			{
				IPlugin plugin = (IPlugin) item.getModelObject();
				item.add(plugin);
			}
		});
	}

	/**
	 * @return area identification
	 */
	public int getIdentification()
	{
		return this.areaId;
	}

	/**
	 * List with all IPlugin objects for this area
	 * 
	 * @return List<IPlugin>
	 */
	public List<IPlugin> getPlugins()
	{
		return this.plugins;
	}
}
