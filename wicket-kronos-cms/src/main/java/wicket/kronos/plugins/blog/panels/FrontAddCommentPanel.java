package wicket.kronos.plugins.blog.panels;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import wicket.authentication.User;
import wicket.kronos.Frontpage;
import wicket.kronos.KronosSession;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.TextArea;
import wicket.markup.html.panel.Panel;
import wicket.model.Model;

/**
 * Add a Comment Panel to a blog posting
 * 
 * @author postma
 */
public class FrontAddCommentPanel extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private BlogPost blogpost;

	/**
	 * @param id
	 * @param blogpost
	 */
	public FrontAddCommentPanel(String id, BlogPost blogpost)
	{
		super(id);
		this.blogpost = blogpost;
		add(new CommentForm("addCommentForm"));
	}

	class CommentForm extends Form {
		/**
		 * Default serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		private TextArea text = null;

		/**
		 * @param name
		 */
		public CommentForm(String name)
		{
			super(name);

			add(text = new TextArea("commentText", new Model(new String())));
		}

		@Override
		public void onSubmit()
		{
			String comment = text.getModelObjectAsString();
			User user = KronosSession.get().getSignedInUser();

			Session jcrSession = KronosSession.get().getJCRSession();

			Calendar date = new GregorianCalendar();

			String postTitle = blogpost.getTitle();

			try
			{
				Workspace ws = jcrSession.getWorkspace();
				QueryManager qm;
				qm = ws.getQueryManager();

				Query q = qm.createQuery(
						"//kronos:blogpostings/kronos:blogpost[@kronos:title='"
								+ postTitle + "']", Query.XPATH);

				QueryResult result = q.execute();
				NodeIterator it = result.getNodes();

				if (it.hasNext())
				{
					Node postNode = it.nextNode();
					Node comments = postNode.getNode("kronos:comments");
					Node commentNode = comments.addNode("kronos:comment");

					commentNode.setProperty("kronos:text", comment);
					commentNode.setProperty("kronos:author", user.getName());
					commentNode.setProperty("kronos:date", date);

					jcrSession.save();
					KronosSession currentSession = KronosSession.get();
					setResponsePage(new Frontpage(currentSession
							.getPageParameters()));
				}

			}
			catch (RepositoryException e)
			{
				e.printStackTrace();
			}
		}
	}
}
