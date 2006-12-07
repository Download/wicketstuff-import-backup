package wicket.kronos.adminpage;

import wicket.kronos.DataProcessor;
import wicket.kronos.plugins.PluginProperties;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 */
public class AdminPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private PluginProperties properties;

	/**
	 * Default when no plugin is to be configured
	 * @param wicketId 
	 */
	public AdminPanel(String wicketId)
	{
		super(wicketId);
		properties = new PluginProperties();
		add(new AdminForm("adminpanelform"));
	}

	/**
	 * Constructor when a specific plugin is to be configured
	 * 
	 * @param wicketId
	 * @param pluginId
	 */
	public AdminPanel(String wicketId, String pluginUUID)
	{
		super(wicketId);
		properties = DataProcessor.getPluginProperties(pluginUUID);
		add(new AdminForm("adminpanelform"));
	}

	/**
	 * @author postma
	 *
	 */
	public class AdminForm extends Form {
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @param id
		 */
		public AdminForm(String id)
		{
			super(id, new CompoundPropertyModel(properties));

			/*List pluginType = Arrays.asList(new String[] {"HelloWorld", "Blog",
					"Menu"});*/

			add(new TextField("name"));
			add(new CheckBox("published"));
			add(new TextField("pluginType").setEnabled(false));
			add(new TextField("order"));
			add(new TextField("position"));
		}
		
		public void onSubmit()
		{
			DataProcessor.savePluginProperties(properties);
		}
	}
}
