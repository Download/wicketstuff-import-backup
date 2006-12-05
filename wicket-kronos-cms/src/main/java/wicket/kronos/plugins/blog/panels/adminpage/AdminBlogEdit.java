package wicket.kronos.plugins.blog.panels.adminpage;

import wicket.kronos.plugins.blog.panels.BlogInputModel;
import wicket.kronos.plugins.blog.panels.BlogPost;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.form.TextField;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.Link;
import wicket.markup.html.panel.Panel;
import wicket.model.CompoundPropertyModel;

/**
 * @author postma
 */
public class AdminBlogEdit extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private BlogPost blogPost = null;

	private InputForm inputForm = null;

	/**
	 * Edit an existing blogpost or blogpost comment
	 * 
	 * @param id
	 * @param blogPost
	 * @param contentUUID
	 * @param blogposts
	 */
	public AdminBlogEdit(String id, BlogPost blogPost)
	{
		super(id);

		this.blogPost = blogPost;
		inputForm = new InputForm("inputForm");
		add(inputForm);
		this.createMenu();
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
			super(name, new CompoundPropertyModel(new BlogInputModel(blogPost
					.getTitle(), blogPost.getText())));
			add(new Label("titlelabel"));
			add(new TextField("title"));
			add(new Label("textlabel"));
			add(new TextArea("text"));
		}
	}

	private void createMenu()
	{
		add(new Link("savepost") {
			@Override
			public void onClick()
			{
				BlogInputModel changedModel = (BlogInputModel) inputForm
						.getModelObject();
				blogPost.setTitle(changedModel.getTitle());
				blogPost.setText(changedModel.getText());
			}
		}.add(new Image("/Kronos_Icon_Pack/Save.png")));
		/* ToDo: add all other necessary menu items */
	}
}
