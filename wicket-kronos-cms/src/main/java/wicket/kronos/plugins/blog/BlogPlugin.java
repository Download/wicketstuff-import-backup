package wicket.kronos.plugins.blog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import wicket.kronos.KronosSession;
import wicket.kronos.plugins.IPlugin;
import wicket.kronos.plugins.blog.panels.BlogPost;
import wicket.kronos.plugins.blog.panels.Comment;
import wicket.kronos.plugins.blog.panels.adminpage.AdminBlogPanel;
import wicket.kronos.plugins.blog.panels.frontpage.FrontBlogPanel;

/**
 * @author postma
 */
public class BlogPlugin extends IPlugin {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Generates a overview of all available blogpostings
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 */
	public BlogPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
		List<BlogPost> blogposts = this.getBlogPosts();
		if (isAdmin)
		{
			add(new AdminBlogPanel("blogpluginpanel", blogposts));
		} else
		{
			add(new FrontBlogPanel("blogpluginpanel", blogposts));
		}
	}

	/**
	 * Show an complete blogposting or create a new blogpost
	 * 
	 * @param isAdmin
	 * @param pluginUUID
	 * @param pluginname
	 * @param ispublished
	 * @param order
	 * @param areaposition
	 * @param pluginType
	 * @param contentUUID
	 */
	public BlogPlugin(Boolean isAdmin, String pluginUUID, String pluginname,
			Boolean ispublished, Integer order, Integer areaposition,
			String pluginType, String contentUUID)
	{
		super(isAdmin, pluginUUID, pluginname, ispublished, order,
				areaposition, pluginType);
		BlogPost blogpost = this.getBlogPost(contentUUID);
		if (isAdmin)
		{
			add(new AdminBlogPanel("blogpluginpanel", blogpost));
		} else
		{
			add(new FrontBlogPanel("blogpluginpanel", blogpost));
		}
	}

	/**
	 * Retreive all available blogposts from repository
	 * 
	 * @return List with all blogposts
	 */
	public List<BlogPost> getBlogPosts()
	{
		List<BlogPost> blogposts = new ArrayList<BlogPost>();
		BlogPost post = null;

		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery(
					"//kronos:plugin/kronos:blogpostings/kronos:blogpost[@kronos:pluginname = '"
							+ pluginName + "']", Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();

				String postUUID = n.getUUID();
				Calendar date = n.getProperty("kronos:date").getDate();
				String title = n.getProperty("kronos:title").getString();
				String text = n.getProperty("kronos:text").getString();
				String author = n.getProperty("kronos:author").getString();
				int nrComments = 0;
				List<Comment> comments = this.getComments(nrComments, postUUID);

				post = new BlogPost(postUUID, date, title, text, author,
						comments, nrComments);
				blogposts.add(post);
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}

		return blogposts;
	}

	/**
	 * Retreive an blogpost it's comments is used by getBlogPost to retreive all
	 * comments of an blogpost
	 * 
	 * @param nrComments
	 * @return List of Comments
	 */
	private List<Comment> getComments(int nrComments, String blogPostUUID)
	{
		List<Comment> comments = new ArrayList<Comment>();
		Comment newComment = null;

		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Workspace ws = jcrSession.getWorkspace();
			QueryManager qm = ws.getQueryManager();
			Query q = qm.createQuery("//kronos:blogpost[@jcr:uuid='"
					+ blogPostUUID + "']/kronos:comments/kronos:comment",
					Query.XPATH);

			QueryResult result = q.execute();
			NodeIterator it = result.getNodes();

			while (it.hasNext())
			{
				Node n = it.nextNode();

				String commentUUID = n.getUUID();
				String text = n.getProperty("kronos:text").getString();
				String author = n.getProperty("kronos:author").getString();
				Calendar date = n.getProperty("kronos:date").getDate();
				newComment = new Comment(commentUUID, text, author, date);

				comments.add(newComment);
				nrComments++;
			}
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
		return comments;
	}

	/**
	 * Retreive BlogPost from repository, identified by contentUUID
	 * 
	 * @param contentUUID
	 * @return BlogPost
	 */
	public BlogPost getBlogPost(String contentUUID)
	{
		BlogPost blogpost = null;

		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Node n = jcrSession.getNodeByUUID(contentUUID);

			String postUUID = n.getUUID();
			Calendar date = n.getProperty("kronos:date").getDate();
			String title = n.getProperty("kronos:title").getString();
			String text = n.getProperty("kronos:text").getString();
			String author = n.getProperty("kronos:author").getString();
			int nrComments = 0;
			List<Comment> comments = this.getComments(nrComments, postUUID);

			blogpost = new BlogPost(postUUID, date, title, text, author,
					comments, nrComments);
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}

		return blogpost;
	}

	/**
	 * Save changed BlogPost to repository
	 * 
	 * @param changedBlogPost
	 */
	public void saveBlogPost(BlogPost changedBlogPost)
	{
		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Node n = jcrSession.getNodeByUUID(changedBlogPost.getPostUUID());

			n.setProperty("date", changedBlogPost.getDate());
			n.setProperty("title", changedBlogPost.getTitle());
			n.setProperty("text", changedBlogPost.getText());
			n.setProperty("author", changedBlogPost.getAuthor());

			jcrSession.save();

		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Save changed Comment to repository
	 * 
	 * @param changedComment
	 */
	public void saveComment(Comment changedComment)
	{
		Session jcrSession = ((KronosSession) KronosSession.get())
				.getJCRSession();

		try
		{
			Node n = jcrSession.getNodeByUUID(changedComment.getCommentUUID());

			n.setProperty("text", changedComment.getText());
			n.setProperty("author", changedComment.getAuthor());
			n.setProperty("date", changedComment.getDate());
		}
		catch (RepositoryException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConfigurable()
	{
		return true;
	}
}
