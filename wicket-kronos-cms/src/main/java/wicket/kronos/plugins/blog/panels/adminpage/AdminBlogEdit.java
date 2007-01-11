package wicket.kronos.plugins.blog.panels.adminpage;

import java.util.GregorianCalendar;

import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import wicket.kronos.KronosSession;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.plugins.blog.panels.BlogInputModel;
import wicket.kronos.plugins.blog.panels.BlogPost;
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
	 */
	public AdminBlogEdit(String id, BlogPost blogPost)
	{
		super(id);

		this.blogPost = blogPost;
		inputForm = new InputForm("inputForm");
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
			super(name, new CompoundPropertyModel(new BlogInputModel(blogPost
					.getTitle(), blogPost.getText())));
			add(new TextField("title"));
			add(new TextArea("text"));
		}
		
		@Override
		public void onSubmit()
		{
			Session jcrSession = KronosSession.get().getJCRSession();
			Node blogNode;
			BlogInputModel model = (BlogInputModel)this.getModelObject();
			try
			{
				blogNode = jcrSession.getNodeByUUID(blogPost.getPostUUID());
				blogNode.setProperty("kronos:name", model.getTitle());
				blogNode.setProperty("kronos:text", model.getText());
				blogNode.setProperty("kronos:date", new GregorianCalendar());
			}
			catch (ItemNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (RepositoryException e)
			{
				e.printStackTrace();
			}
			setResponsePage(AdminPage.class);
			
		}
	}

	/*private void createMenu()
	{
		add(new Link("savepost") {
			
			/**
			 * Default serialVersionUID
			 
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				BlogInputModel changedModel = (BlogInputModel) inputForm
						.getModelObject();
				blogPost.setTitle(changedModel.getTitle());
				blogPost.setText(changedModel.getText());
			}
		}.add(new Image("save", "Save.png")));
		/* ToDo: add all other necessary menu items 
	}*/
}