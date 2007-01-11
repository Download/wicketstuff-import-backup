package wicket.kronos.plugins.blog.panels.adminpage;

import java.util.List;

import wicket.kronos.plugins.blog.panels.BlogPost;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 */
public class AdminBlogPanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an overview of all available blog posts
	 * 
	 * @param id
	 * @param blogposts
	 * @param pluginUUID 
	 */
	public AdminBlogPanel(String id, List<BlogPost> blogposts, String pluginUUID)
	{
		super(id);
		add(new AdminBlogOverview("adminblogpanel", blogposts, pluginUUID));
	}

	/**
	 * Shows only one blog post to edit or shows new blog post input
	 * 
	 * @param id
	 * @param blogpost
	 */
	public AdminBlogPanel(String id, BlogPost blogpost)
	{
		super(id);
		if (blogpost == null)
		{
			add(new AdminNewBlogpost("adminblogpanel"));
		} else
		{
			add(new AdminBlogEdit("adminblogpanel", blogpost));
		}
	}

}
