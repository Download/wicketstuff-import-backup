package wicket.kronos.adminpage;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import wicket.PageParameters;
import wicket.ajax.AjaxRequestTarget;
import wicket.ajax.markup.html.AjaxLink;
import wicket.kronos.AreaLocations;
import wicket.kronos.DataProcessor;
import wicket.kronos.plugins.PluginProperties;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Button;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.DropDownChoice;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.IChoiceRenderer;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;
import wicket.model.PropertyModel;

/**
 * @author postma
 */
public class AdminPluginOverview extends Panel {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private List<PluginProperties> propertiesList;

	/**
	 * Constructor.
	 * @param wicketId
	 */
	public AdminPluginOverview(String wicketId)
	{
		super(wicketId);
		propertiesList = DataProcessor.getPluginPropertiesObjects();
		add(new OverviewForm("overviewForm"));
	}

	/**
	 * @author roeloffzen
	 */
	public class OverviewForm extends Form {

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param wicketID
		 */
		public OverviewForm(String wicketID)
		{
			super(wicketID, new CompoundPropertyModel(new PluginPropertiesModel(propertiesList)));
			add(new ListView("properties") {
				
				/**
				 * Default serialVersionUID
				 */
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("serial")
				@Override
				protected void populateItem(ListItem item)
				{
					final PluginProperties props = (PluginProperties) item.getModelObject();
					final TextField orderField;

					item.add(new CheckBox("selected", new PropertyModel(props, "selected")));
					PageParameters param = new PageParameters();
					param.put("IDType", "plugin");
					param.put("ID", props.getPluginUUID());
					item.add(new BookmarkablePageLink("nameLink", AdminPage.class, param)
							.add(new Label("nameLabel", props.getName())));

					item.add(new CheckBox("published", new PropertyModel(props, "published")));
					item.add(orderField = new TextField("order", 
						new PropertyModel(props, "order")));

					orderField.setOutputMarkupId(true);

					item.add(new AjaxLink("incrementLink") {
						@Override
						public void onClick(AjaxRequestTarget target)
						{
							int newValue = props.getOrder() + 1;
							props.setOrder(newValue);
							target.addComponent(orderField);
						}
					});

					item.add(new AjaxLink("decrementLink") {
						@Override
						public void onClick(AjaxRequestTarget target)
						{
							int oldValue = props.getOrder();
							int newValue;
							if (oldValue > 1)
								newValue = oldValue--;
							else
								newValue = oldValue;
							props.setOrder(newValue);
							target.addComponent(orderField);
						}
					});

					item.add(new DropDownChoice("position", new PropertyModel(props, "position"),
							AreaLocations.getAreaLocations(), new IChoiceRenderer() {

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
					int lastPeriod = props.getPluginType().lastIndexOf(".");
					String pluginType = props.getPluginType().substring(lastPeriod++);
					item.add(new Label("pluginType", pluginType));
				}
			});

			add(new Button("deletebutton") {
				@Override
				public void onSubmit()
				{
					List<PluginProperties> properties = ((PluginPropertiesModel) OverviewForm.this
							.getModelObject()).getProperties();
					Iterator pluginIterator = properties.iterator();

					while (pluginIterator.hasNext())
					{
						PluginProperties pluginProps = (PluginProperties) pluginIterator.next();
						if (pluginProps.isSelected())
						{
							DataProcessor.removePluginInstance(pluginProps.getPluginUUID());
						}
					}
					setResponsePage(AdminPage.class);
				}
			});

			add(new Button("savebutton") {
				@Override
				public void onSubmit()
				{
					List<PluginProperties> properties = ((PluginPropertiesModel) OverviewForm.this
							.getModelObject()).getProperties();
					Iterator pluginIterator = properties.iterator();

					while (pluginIterator.hasNext())
					{
						PluginProperties pluginProps = (PluginProperties) pluginIterator.next();

						DataProcessor.savePluginProperties(pluginProps, pluginProps.getName());
					}

					setResponsePage(AdminPage.class);
				}
			});
		}
	}

	/**
	 * @author roeloffzen
	 */
	public class PluginPropertiesModel implements Serializable {

		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		private List<PluginProperties> properties;

		/**
		 * @param properties
		 */
		public PluginPropertiesModel(List<PluginProperties> properties)
		{
			this.properties = properties;
		}

		/**
		 * @return list with pluginProperties
		 */
		public List<PluginProperties> getProperties()
		{
			return properties;
		}

		/**
		 * @param properties
		 */
		public void setProperties(List<PluginProperties> properties)
		{
			this.properties = properties;
		}
	}
}
