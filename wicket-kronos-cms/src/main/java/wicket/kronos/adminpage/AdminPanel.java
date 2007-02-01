package wicket.kronos.adminpage;

import wicket.kronos.DataProcessor;
import wicket.kronos.frontpage.AreaLocations;
import wicket.kronos.plugins.PluginProperties;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 */
public class AdminPanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	protected PluginProperties properties;

	protected String oldPluginName;

	private String myPluginType;

	protected String pluginUUID;

	/**
	 * Default constructor when no plugin is to be configured
	 * 
	 * @param wicketId
	 */
	public AdminPanel(String wicketId)
	{
		super(wicketId);
		properties = new PluginProperties();
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new AdminForm("adminpanelform"));
	}

	/**
	 * Constructor when a specific plugin is to be configured
	 * 
	 * @param wicketId
	 * @param pluginUUID
	 */
	public AdminPanel(String wicketId, String pluginUUID)
	{
		super(wicketId);
		this.pluginUUID = pluginUUID;
		properties = DataProcessor.getPluginProperties(pluginUUID);
		String tempType = properties.getPluginType();
		int lastPeriod = tempType.lastIndexOf(".");
		myPluginType = tempType.substring(lastPeriod + 1);
		oldPluginName = properties.getName();
		final FeedbackPanel feedback = new FeedbackPanel("feedback");
		add(feedback);
		add(new AdminForm("adminpanelform"));
	}

	/**
	 * @author postma
	 */
	public class AdminForm extends Form {

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param id
		 */
		public AdminForm(String id)
		{
			super(id, new CompoundPropertyModel(properties));

			add(new Label("settingslabel", "Settings for " + myPluginType));
			add(new TextField("name"));
			add(new CheckBox("published"));
			add(new Label("pluginType", myPluginType));
			add(new TextField("order"));
			add(new DropDownChoice("position", AreaLocations.getAreaLocations(),
				new IChoiceRenderer() {

					public String getIdValue(Object object, int arg1)
					{
						return ((Integer) object).toString();
					}

					public Object getDisplayValue(Object object)
					{
						int value = ((Integer) object).intValue();
						return AreaLocations.getLocationname(value);
					}

				}));
		}

		@Override
		public void onSubmit()
		{	
			if(!properties.getName().equals(oldPluginName))
			{
				if(!DataProcessor.pluginNameExists(properties.getName()))
				{
					DataProcessor.savePluginProperties(properties, oldPluginName);
					setResponsePage(AdminPage.class);
				} else {
					error("The pluginname already exists, please choose another");
				}
			} else {
				DataProcessor.savePluginProperties(properties, oldPluginName);
				setResponsePage(AdminPage.class);
			}
		}
	}
}
