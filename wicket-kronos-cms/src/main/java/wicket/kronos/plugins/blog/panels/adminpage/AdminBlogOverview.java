package wicket.kronos.plugins.blog.panels.adminpage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wicket.PageParameters;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.adminpage.AdminPanel;
import wicket.kronos.plugins.blog.panels.BlogPost;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;
import wicket.markup.html.panel.Panel;

/**
 * @author postma
 */
public class AdminBlogOverview extends Panel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Shows an overview of all available blog postings
	 * 
	 * @param id
	 * @param blogposts
	 */
	public AdminBlogOverview(String id, List<BlogPost> blogposts)
	{
		super(id);
		ListView blogsList = new ListView("blogposts", blogposts) {
			/**
			 * Default serialVersionUID
			 */
			private static final long serialVersionUID = 1L;

			int i = 0;

			/* Create a ListItem for every BlogPost from List<BlogPost> */
			@Override
			protected void populateItem(ListItem item)
			{
				BlogPost post = (BlogPost) item.getModelObject();
				item.add(new Label("counter", String.valueOf(i)));
				PageParameters param = new PageParameters();
				param.put("IDType", "content");
				param.put("ID", post.getPostUUID());
				item.add(new BookmarkablePageLink("titlelink", AdminPage.class,
						param)
						.add(new Label("titlelinkLabel", post.getTitle())));
				Date date = post.getDate().getTime();
				SimpleDateFormat blogDateFormat = new SimpleDateFormat(
						"dd-MM-yyyy");
				item.add(new Label("datetime", blogDateFormat.format(date)));
				item.add(new Label("author", post.getAuthor()));
				item.add(new Label("nrcomments", String.valueOf(post
						.getNrComments())));
				i++;
			};
		};
		add(blogsList);
	}
}
