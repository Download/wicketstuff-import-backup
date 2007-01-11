package wicket.kronos.plugins.blog.panels.adminpage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import wicket.PageParameters;
import wicket.kronos.adminpage.AdminPage;
import wicket.kronos.adminpage.AdminPanel;
import wicket.kronos.plugins.blog.panels.BlogPost;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.Form;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;

/**
 * @author postma
 */
public class AdminBlogOverview extends AdminPanel {

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	List<BlogPost> blogposts;

	private String blogPluginUUID = null;

	/**
	 * Shows an overview of all available blog postings
	 * 
	 * @param id
	 * @param blogposts
	 * @param blogPluginUUID
	 */
	public AdminBlogOverview(String id, List<BlogPost> blogposts, final String blogPluginUUID)
	{
		super(id, blogPluginUUID);
		this.blogPluginUUID = blogPluginUUID;
		this.blogposts = blogposts;
		add(new postOverviewForm("testForm"));
	}

	/**
	 * @author postma
	 */
	public class postOverviewForm extends Form {

		/**
		 * Default serialVersionUUID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Form with a overview of one blogposting
		 * 
		 * @param wicketId
		 */
		public postOverviewForm(String wicketId)
		{
			super(wicketId);
			ListView blogsList = new ListView("blogposts", blogposts) {
				/**
				 * Default serialVersionUID
				 */
				private static final long serialVersionUID = 1L;

				int i = 1;

				/* Create a ListItem for every BlogPost from List<BlogPost> */
				@Override
				protected void populateItem(ListItem item)
				{
					BlogPost post = (BlogPost) item.getModelObject();
					item.add(new Label("counter", String.valueOf(i)));
					PageParameters param = new PageParameters();
					param.put("IDType", "content");
					param.put("ID", post.getPostUUID());
					item.add(new BookmarkablePageLink("titlelink", AdminPage.class, param)
							.add(new Label("titlelinkLabel", post.getTitle())));
					Date date = post.getDate().getTime();
					SimpleDateFormat blogDateFormat = new SimpleDateFormat("dd-MM-yyyy");
					item.add(new Label("datetime", blogDateFormat.format(date)));
					item.add(new Label("author", post.getAuthor()));
					item.add(new Label("nrcomments", String.valueOf(post.getNrComments())));
					i++;
				};
			};
			add(blogsList);
		}

		/**
		 * Activates the inputform for adding a new blogposting
		 */
		@Override
		public void onSubmit()
		{
			PageParameters newPostParam = new PageParameters();
			newPostParam.add("IDType", "plugin");
			newPostParam.add("ID", blogPluginUUID);
			newPostParam.add("action", "new");

			setResponsePage(AdminPage.class, newPostParam);
		}
	}
}
