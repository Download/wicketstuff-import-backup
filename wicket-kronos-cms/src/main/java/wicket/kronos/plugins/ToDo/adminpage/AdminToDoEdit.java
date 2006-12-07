package wicket.kronos.plugins.ToDo.adminpage;

import wicket.extensions.markup.html.datepicker.DatePicker;
import wicket.kronos.plugins.ToDo.ToDoItem;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.form.CheckBox;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 *
 */
public class AdminToDoEdit extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	private InputForm inputForm = null;

	/**
	 * @param wicketId
	 * @param todoItem
	 */
	public AdminToDoEdit(String wicketId, ToDoItem todoItem)
	{
		super(wicketId);
		
		inputForm = new InputForm("admintodoedit", todoItem);
		add(inputForm);
	}

	private class InputForm extends Form {
		
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * @param name
		 * @param todoItem
		 */
		public InputForm(String name, ToDoItem todoItem)
		{
			super(name, new CompoundPropertyModel(todoItem));
			add(new TextField("title"));
			add(new TextField("subject"));
			add(new TextField("content"));
			add(new CheckBox("done"));
			
			WebMarkupContainer dateLabel = new WebMarkupContainer("datelabel");
			add(dateLabel);
			TextField datePropertyTextField = new TextField("date");
			add(datePropertyTextField).setEnabled(false);
			add(new DatePicker("datePicker", dateLabel, datePropertyTextField));
		}
	}
}
