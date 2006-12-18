package wicket.kronos.adminpage;

import wicket.kronos.AreaLocations;
import wicket.kronos.DataProcessor;
import wicket.kronos.plugins.PluginProperties;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;
import wicket.model.PropertyModel;

/**
 * @author postma
 */
public class AdminPanel extends Panel {

	private static final long serialVersionUID = 1L;
	protected PluginProperties properties;
	protected String oldPluginName;
	private String myPluginType = null;

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
		String tempType = properties.getPluginType();
		int lastPeriod = tempType.lastIndexOf(".");
		myPluginType = tempType.substring(lastPeriod+1);
		oldPluginName = properties.getName();
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
			add(new Label("pluginType", myPluginType));
			add(new TextField("order"));
			add(new DropDownChoice("position", AreaLocations.getAreaLocations(), new IChoiceRenderer() {
				
				public String getIdValue(Object object, int arg1)
				{	
					return ((Integer)object).toString();
				}
			
				public Object getDisplayValue(Object object)
				{
					int value = ((Integer)object).intValue();
					return AreaLocations.getLocationname(value);
				}
				
			}));
		}
		
		public void onSubmit()
		{
			DataProcessor.savePluginProperties(properties, oldPluginName);
			setResponsePage(AdminPage.class);
		}
	}
}
