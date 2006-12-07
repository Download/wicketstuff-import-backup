package wicket.kronos.adminpage;

import wicket.kronos.plugins.PluginProperties;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.Link;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 *
 */
public class AdminNewPlugin extends Form{
	
	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param wicketId
	 */
	public AdminNewPlugin(String wicketId)
	{
		//TODO read position's from repository an give as parameter to PluginProperties
		super(wicketId, new CompoundPropertyModel(new PluginProperties()));
		
		add(new TextField("name"));
		add(new CheckBox("published"));
		add(new TextField("order"));
		add(new DropDownChoice("position"));
		add(new TextField("plugintype"));	
		
		add(new Link("savenewplugin")
		{
			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				//TODO save new plugin to repository
			}
		});
	}
}
