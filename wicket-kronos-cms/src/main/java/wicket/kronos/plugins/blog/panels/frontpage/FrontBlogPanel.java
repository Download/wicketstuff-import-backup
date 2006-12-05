package wicket.kronos.plugins.blog.panels.frontpage;

import java.util.List;

import wicket.kronos.plugins.blog.panels.BlogPost;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 */
public class FrontBlogPanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create an overview of al blog postings
	 * 
	 * @param id
	 * @param blogposts
	 */
	public FrontBlogPanel(String id, List<BlogPost> blogposts)
	{
		super(id);

		add(new FrontBlogOverview("frontblogpanel", blogposts));
	}

	/**
	 * Show contents of one blog posting
	 * 
	 * @param id
	 * @param blogpost
	 */
	public FrontBlogPanel(String id, BlogPost blogpost)
	{
		super(id);
		add(new FrontBlogpost("frontblogpanel", blogpost));
	}
}
