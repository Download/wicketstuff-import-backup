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

	private int areaId;

	private List<IPlugin> plugins;

	/**
	 * Constructor creates a area with a collection of plugins.
	 * 
	 * @param areaId
	 */
	public Area(int areaId)
	{
		
		super(AreaLocations.getLocationname(areaId));
		this.areaId = areaId;
		this.plugins = DataProcessor.getPlugins(this.areaId);
		add(new ListView("pluginRepeater", plugins) {

			@Override
			public void populateItem(ListItem item)
			{
				item.add((IPlugin) item.getModelObject());
			}
		});
	}

	/**
	 * Constructor creates a area with just one plugin.
	 * 
	 * @param areaId
	 * @param plugin
	 */
	public Area(int areaId, IPlugin plugin)
	{
		super(AreaLocations.getLocationname(areaId));
		this.areaId = areaId;

		this.plugins = new ArrayList<IPlugin>();
		this.plugins.add(plugin);
		add(new ListView("pluginRepeater", plugins) {
			
			@Override
			public void populateItem(ListItem item)
			{
				item.add((IPlugin)item.getModelObject());
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
