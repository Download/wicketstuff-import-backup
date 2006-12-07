package wicket.kronos.plugins.ToDo.frontpage;

import wicket.extensions.markup.html.datepicker.DatePicker;
import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 *
 */
public class FrontNewToDo extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param wicketId
	 */
	public FrontNewToDo(String wicketId)
	{
		super(wicketId);
		InputForm inputForm = new InputForm("frontnewtodo");
		add(inputForm);
	}
	
	private class InputForm extends Form {
		
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @param name
		 */
		public InputForm(String name)
		{
			super(name, new CompoundPropertyModel(new ToDoItem()));
			
			add(new TextField("title"));
			add(new TextField("subject"));
			add(new TextField("content"));
			add(new CheckBox("done"));
			WebMarkupContainer dateLabel = new WebMarkupContainer("datelabel");
			//add(dateLabel);
			TextField datePropertyTextField = new TextField("date");
			add(datePropertyTextField).setEnabled(false);
			add(new DatePicker("datePicker", dateLabel, datePropertyTextField));	
			
			add(new Link("savenewtodoitem")
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
}
