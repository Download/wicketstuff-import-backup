package wicket.kronos.adminpage;

import java.util.List;

import wicket.kronos.AreaLocations;
import wicket.kronos.DataProcessor;
import wicket.kronos.plugins.PluginProperties;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 */
public class AdminNewPlugin extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param wicketId
	 */
	public AdminNewPlugin(String wicketId)
	{
		super(wicketId);
		add(new AdminNewPluginForm("newpluginform"));
	}

	/**
	 * @author postma
	 */
	public class AdminNewPluginForm extends Form {

		/**
		 * Default serialVersionUUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param wicketId
		 */
		public AdminNewPluginForm(String wicketId)
		{
			super(wicketId, new CompoundPropertyModel(new PluginProperties()));

			add(new TextField("name"));
			add(new CheckBox("published"));
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

			List<String> plugintypes = DataProcessor.getPluginTypes();
			add(new DropDownChoice("pluginType", plugintypes, new IChoiceRenderer() {

				public String getIdValue(Object object, int arg1)
				{
					return (String) object;
				}

				public Object getDisplayValue(Object object)
				{
					String canonicalName = (String) object;
					int lastPeriod = canonicalName.lastIndexOf(".");
					return canonicalName.substring(lastPeriod + 1);
				}

			}));
		}

		@Override
		public void onSubmit()
		{
			PluginProperties pluginProperties = (PluginProperties) this.getModelObject();

			DataProcessor.savePluginProperties(pluginProperties, pluginProperties.getName());

			setResponsePage(AdminPage.class);
		}
	}
}
