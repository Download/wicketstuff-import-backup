package wicket.kronos.plugins.blog.panels.frontpage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

import wicket.kronos.KronosSession;
import wicket.kronos.plugins.blog.panels.BlogPost;
import wicket.kronos.plugins.blog.panels.Comment;
import wicket.markup.html.basic.Label;
import wicket.markup.html.basic.MultiLineLabel;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 */
public class FrontBlogpost extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Show contents of one blogpost and its comments
	 * 
	 * @param id
	 * @param blogpost
	 */
	public FrontBlogpost(String id, BlogPost blogpost)
	{
		super(id);

		add(new Label("title", blogpost.getTitle()));
		add(new MultiLineLabel("text", blogpost.getText()).setEscapeModelStrings(false) );
		Date date = blogpost.getDate().getTime();
		SimpleDateFormat blogDateFormat = new SimpleDateFormat("dd-MM-yyyy");

		add(new Label("date", blogDateFormat.format(date)));
		add(new Label("author", blogpost.getAuthor()));
		ListView comments = getCommentsListView(blogpost.getComments());
		add(comments);

		if (KronosSession.get().isSignedIn())
		{
			add(new FrontAddCommentPanel("commentPanel", blogpost));
		} else
		{
			add(new Label("commentPanel", "You need to login to add a comment"));
		}
	}

	/**
	 * Generate a ListView of all blogpost's comments
	 * 
	 * @param comments
	 * @return ListView with comments
	 */
	public ListView getCommentsListView(List<Comment> comments)
	{
		ListView commentsList = new ListView("commentRepeater", comments) {
			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(ListItem item)
			{
				Comment comment = (Comment) item.getModelObject();

				item.add(new MultiLineLabel("commenttext", comment.getText()));
				Date date = comment.getDate().getTime();
				SimpleDateFormat blogDateFormat = new SimpleDateFormat(
						"dd-MM-yyyy");

				item.add(new Label("commentdate", blogDateFormat.format(date)));
				item.add(new Label("commentauthor", comment.getAuthor()));
			}
		};
		return commentsList;
	}
}
