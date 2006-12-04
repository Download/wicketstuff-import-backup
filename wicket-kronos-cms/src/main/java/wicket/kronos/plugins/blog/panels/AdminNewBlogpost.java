package wicket.kronos.plugins.blog.panels;

import wicket.extensions.markup.html.datepicker.DatePicker;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 */
public class AdminNewBlogpost extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create an inputform for a new blogposting
	 * 
	 * @param id
	 */
	public AdminNewBlogpost(String id)
	{
		super(id);

		add(new InputForm("inputForm"));
		/* ToDo: nieuwe blogpost opslaan */

	}

	/**
	 * Create inputform for a new blogpost
	 * 
	 * @author postma
	 */
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
			super(name, new CompoundPropertyModel(new BlogInputModel()));

			WebMarkupContainer dateLabel = new WebMarkupContainer("datelabel");
			add(dateLabel);
			TextField datePropertyTextField = new TextField("date");
			add(datePropertyTextField).setEnabled(false);
			add(new DatePicker("datePicker", dateLabel, datePropertyTextField));
			add(new Label("titlelabel"));
			add(new TextField("title"));
			add(new Label("authorlabel"));
			add(new TextField("author"));
			add(new Label("blogtextlabel"));
			add(new TextArea("text"));
		}
	}
}
