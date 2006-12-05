package wicket.kronos.adminpage;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 */
public class AdminPanel extends Panel {

	private static final long serialVersionUID = 1L;

	//private IPlugin plugin = null;

	/**
	 * Default when no plugin is to be configured
	 * @param wicketId 
	 */
	public AdminPanel(String wicketId)
	{
		super(wicketId);
		
		add(new AdminForm("adminpanelform"));
	}

	/**
	 * Constructor when a specific plugin is to be configured
	 * 
	 * @param wicketId
	 * @param pluginId
	 */
	public AdminPanel(String wicketId, int pluginId)
	{
		super(wicketId);

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
			super(id, new CompoundPropertyModel(new FormInputModel()));

			List pluginType = Arrays.asList(new String[] {"HelloWorld", "Blog",
					"Menu"});

			/* changing pluginname */
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
	public class FormInputModel implements Serializable {
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

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
