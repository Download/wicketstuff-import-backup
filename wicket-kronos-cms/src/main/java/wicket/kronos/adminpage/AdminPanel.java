package wicket.kronos.adminpage;

import java.util.Arrays;
import java.util.List;

import wicket.markup.html.basic.Label;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;
import wicket.kronos.plugins.IPlugin;

/**
 * @author postma
 */
public class AdminPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private IPlugin plugin = null;

	/**
	 * Default when no plugin is to be configured
	 */
	public AdminPanel(String wicketId)
	{
		super(wicketId);
	}

	/**
	 * Constructor when a specific plugin is to be configured
	 * 
	 * @param pluginId
	 */
	public AdminPanel(int pluginId)
	{
		super("adminpanel");
		// boolean isAdmin = true;
		// plugin = DataProcessor.getPlugin(isAdmin, pluginId);

		add(new AdminForm("adminpanelform"));

		/*
		 * Form adminPanelForm = new Form("adminpanelform") {
		 * super("adminpanelform", new CompoundPropertyModel(new
		 * FormInputModel())); }; //changing pluginname add(new
		 * Label("currentpluginname", "Currentpluginname \"" +
		 * plugin.getPluginname() + "\"")); add(new Label("namelabel", "New
		 * pluginname")); add(new TextField("newpluginname")); /* publish /
		 * unpublish //setIspublished(); add(new Label("publish", "Publish"));
		 * add(new CheckBox("ispublished")); /* set the plugin type add(new
		 * Label("plugintype", "Plugintype")); /* ToDo: add dropdown when
		 * repository has list of plugin types add(new DropDownChoice());
		 */

		/* ToDo: add child panels */

	}

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
			super(id, new CompoundPropertyModel(new FormInputModel(plugin
					.isPublished(), plugin.getPluginname(), plugin
					.getPluginType())));

			List pluginType = Arrays.asList(new String[] {"HelloWorld", "Blog",
					"Menu"});

			/* changing pluginname */
			add(new Label("currentpluginname", "Currentpluginname \""
					+ plugin.getPluginname() + "\""));
			add(new TextField("newpluginname"));

			/* publish / unpublish */
			// setIspublished();
			add(new CheckBox("ispublished"));

			/* set the plugin type */
			add(new DropDownChoice("plugintype", pluginType));
		}
	}

	/**
	 * Needed for changing checkbox's state
	 * 
	 * @author roeloffzen
	 */
	public class FormInputModel {
		boolean ispublished;

		String newpluginname;

		String plugintype;

		/**
		 * Default FromInputModel, parameters are set with setters
		 */
		public FormInputModel()
		{
			this.ispublished = false;
			this.newpluginname = null;
			this.plugintype = null;
		}

		/**
		 * @param ispublished
		 * @param newpluginname
		 * @param plugintype
		 */
		public FormInputModel(boolean ispublished, String newpluginname,
				String plugintype)
		{
			this.ispublished = ispublished;
			this.newpluginname = newpluginname;
			this.plugintype = plugintype;
		}

		/**
		 * @return true if the panel is published
		 */
		public boolean getIspublished()
		{
			return this.ispublished;
		}

		/**
		 * @param ispublished
		 */
		public void setIspublished(boolean ispublished)
		{
			this.ispublished = ispublished;
		}

		/**
		 * @return The new pluginname
		 */
		public String getNewpluginname()
		{
			return this.newpluginname;
		}

		/**
		 * @param name
		 */
		public void setNewpluginname(String name)
		{
			this.newpluginname = name;
		}

		/**
		 * @return the plugin type
		 */
		public String getPlugintype()
		{
			return this.plugintype;
		}

		/**
		 * @param plugintype
		 */
		public void setPlugintype(String plugintype)
		{
			this.plugintype = plugintype;
		}
	}
}
