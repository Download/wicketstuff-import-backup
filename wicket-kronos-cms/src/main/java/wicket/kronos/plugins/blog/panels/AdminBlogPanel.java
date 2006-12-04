package wicket.kronos.plugins.blog.panels;

import java.util.List;

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
	 */
	public AdminBlogPanel(String id, List<BlogPost> blogposts)
	{
		super(id);
		add(new AdminBlogOverview("adminblogpanel", blogposts));
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
		if (blogpost.equals(null))
		{
			add(new AdminNewBlogpost("adminblogpanel"));
		} else
		{
			add(new AdminBlogEdit("adminblogpanel", blogpost));
		}
	}

}
