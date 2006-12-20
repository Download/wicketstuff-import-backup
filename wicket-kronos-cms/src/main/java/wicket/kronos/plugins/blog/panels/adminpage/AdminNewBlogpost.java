package wicket.kronos.plugins.blog.panels.adminpage;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.plugins.blog.BlogPlugin;
import wicket.kronos.plugins.blog.panels.BlogInputModel;
import wicket.kronos.plugins.blog.panels.BlogPost;
import wicket.kronos.plugins.blog.panels.Comment;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.TextField;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;
import wicket.model.Model;

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
	 * @param blogPlugin 
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
		private TextField title;
		private TextArea text;

		/**
		 * @param name
		 * @param blogPlugin 
		 */
		public InputForm(String name)
		{
			super(name, new CompoundPropertyModel(new BlogInputModel()));

			add(title = new TextField("title", new Model()));
			add(text = new TextArea("text", new Model()));
		}
		
		@Override
		public void onSubmit()
		{
			String postUUID = null;
			Calendar date = new GregorianCalendar();
			String title = this.title.getModelObjectAsString();
			String text = this.text.getModelObjectAsString();
			//TODO author must be set in jcrSession and read out of it
			String author = "todo";
			List<Comment> comments = null;
			int nrComments = 0;
			
			BlogPlugin.saveNewBlogPost(
				new BlogPost(postUUID, date, title, text, author,
					comments, nrComments));
			
			setResponsePage(AdminPage.class);
		}
	}
}
